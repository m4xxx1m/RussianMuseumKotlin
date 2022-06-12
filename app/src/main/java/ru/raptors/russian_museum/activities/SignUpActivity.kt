package ru.raptors.russian_museum.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import ru.raptors.russian_museum.R
import ru.raptors.russian_museum.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private var _binding: ActivitySignUpBinding? = null
    private val binding get() = _binding!!

    private lateinit var emailInputText: TextInputEditText
    private lateinit var passwordInputText: TextInputEditText
    private lateinit var passwordRepeatInputText: TextInputEditText

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        emailInputText = binding.signUpEmailInput
        passwordInputText = binding.signUpPasswordInput
        passwordRepeatInputText = binding.signUpRepeatPasswordInput

        mAuth = FirebaseAuth.getInstance()
    }

    fun signUp(v: View) {
        val inputEmail = emailInputText.text.toString().trim()
        val inputPassword = passwordInputText.text.toString().trim()

        if (!checkPasswordCorrect(inputPassword)) {
            showError(getString(R.string.password_length_error))
        }
        else if (!checkPasswordsSame(inputPassword)) {
            showError(getString(R.string.passwords_isnt_equal_error))
        }
        else {
            mAuth.createUserWithEmailAndPassword(inputEmail, inputPassword).addOnCompleteListener {
                if (it.isSuccessful) {
                    returnToSignIn()
                }
                else {
                    showError(getString(R.string.unknown_error))
                }
            }
        }

    }

    private fun showError(message: String) {
        val builder = AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton(getString(R.string.got_it)) { dialogInterface, _ ->
                dialogInterface.cancel()
            }
        builder.show()
    }

    private fun checkPasswordCorrect(inputPassword: String): Boolean = inputPassword.length >= 6

    private fun checkPasswordsSame(inputPassword: String): Boolean {
        val inputRepeatPassword = passwordRepeatInputText.text.toString().trim()
        return inputPassword == inputRepeatPassword
    }

    fun returnToSignIn(v: View) {
        returnToSignIn()
    }

    private fun returnToSignIn() {
        val intent = Intent(this, AuthenticationActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}