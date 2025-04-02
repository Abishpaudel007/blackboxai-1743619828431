package com.example.mealmate.ui.delegate

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.mealmate.R
import com.example.mealmate.data.dao.GroceryDao
import com.example.mealmate.databinding.ActivityDelegateBinding
import com.example.mealmate.util.formatGroceryList
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DelegateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDelegateBinding

    @Inject lateinit var groceryDao: GroceryDao

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            sendSMS()
        } else {
            Toast.makeText(
                this,
                getString(R.string.error_sms_permission_denied),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDelegateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        loadGroceryList()
        setupListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.title_delegate)
    }

    private fun loadGroceryList() {
        CoroutineScope(Dispatchers.IO).launch {
            val items = groceryDao.getActiveItems().firstOrNull() ?: emptyList()
            val formattedList = formatGroceryList(items)

            runOnUiThread {
                binding.groceryListEditText.setText(formattedList)
            }
        }
    }

    private fun setupListeners() {
        binding.sendButton.setOnClickListener {
            checkPermissionAndSend()
        }

        binding.contactsButton.setOnClickListener {
            openContacts()
        }
    }

    private fun checkPermissionAndSend() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.SEND_SMS
            ) == PackageManager.PERMISSION_GRANTED -> {
                sendSMS()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.SEND_SMS)
            }
        }
    }

    private fun sendSMS() {
        val phoneNumber = binding.phoneEditText.text.toString().trim()
        val message = binding.groceryListEditText.text.toString().trim()

        if (phoneNumber.isEmpty()) {
            binding.phoneEditText.error = getString(R.string.error_phone_empty)
            return
        }

        try {
            SmsManager.getDefault().sendTextMessage(
                phoneNumber,
                null,
                message,
                null,
                null
            )
            Toast.makeText(
                this,
                getString(R.string.success_sms_sent),
                Toast.LENGTH_SHORT
            ).show()
            finish()
        } catch (e: Exception) {
            Toast.makeText(
                this,
                getString(R.string.error_sms_failed),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun openContacts() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "vnd.android.cursor.dir/phone_v2"
        }
        startActivityForResult(intent, REQUEST_PICK_CONTACT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PICK_CONTACT && resultCode == RESULT_OK) {
            data?.data?.let { contactUri ->
                val phoneNumber = getPhoneNumberFromUri(contactUri)
                binding.phoneEditText.setText(phoneNumber)
            }
        }
    }

    private fun getPhoneNumberFromUri(uri: Uri): String {
        // Simplified implementation - in a real app you'd query ContactsContract
        return uri.lastPathSegment ?: ""
    }

    companion object {
        private const val REQUEST_PICK_CONTACT = 1001
    }
}