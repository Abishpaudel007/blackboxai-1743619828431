package com.example.mealmate.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mealmate.MainActivity
import com.example.mealmate.R
import com.example.mealmate.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (validateInputs(email, password)) {
                loginUser(email, password)
            }
        }

        binding.registerTextView.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun validateInputs(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            binding.emailEditText.error = getString(R.string.error_email_empty)
            return false
        }

        if (password.isEmpty()) {
            binding.passwordEditText.error = getString(R.string.error_password_empty)
            return false
        }

        if (password.length < 6) {
            binding.passwordEditText.error = getString(R.string.error_password_length)
            return false
        }

        return true
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.error_login_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}