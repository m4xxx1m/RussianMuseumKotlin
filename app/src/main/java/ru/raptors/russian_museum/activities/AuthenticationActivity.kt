package ru.raptors.russian_museum.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import ru.raptors.russian_museum.databinding.ActivityAuthenticationBinding

class AuthenticationActivity : AppCompatActivity() {
    private var _binding: ActivityAuthenticationBinding? = null
    private val binding get() = _binding!!

    private lateinit var emailInputText: TextInputEditText
    private lateinit var passwordInputText: TextInputEditText

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        emailInputText = binding.emailInput
        passwordInputText = binding.passwordInput

        mAuth = FirebaseAuth.getInstance()
    }

    fun goToMainMenu(v: View) {
        goToMainMenu()
    }

    private fun goToMainMenu() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun signIn(v: View) {
        val inputEmail = emailInputText.text.toString().trim()
        val inputPassword = passwordInputText.text.toString().trim()

        mAuth.signInWithEmailAndPassword(inputEmail, inputPassword).addOnCompleteListener {
            if (it.isSuccessful) {
                goToMainMenu()
            }
            else {
                Log.i("error signing in", it.exception?.message ?:"")
            }
        }
    }

    fun signUp(v: View) {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}