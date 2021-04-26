package com.openclassrooms.realestatemanager.ui.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.AuthUI.IdpConfig
import com.firebase.ui.auth.AuthUI.IdpConfig.EmailBuilder
import com.firebase.ui.auth.AuthUI.IdpConfig.GoogleBuilder
import com.google.android.material.snackbar.Snackbar
import com.openclassrooms.realestatemanager.MainActivity
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityAuthBinding


class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        startSignInActivity()
        setContentView(binding.root)
    }

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                //  this.createUserInFirestore();
                this.startMainActivity()
                showSnackBar(binding.root, getString(R.string.connection_succeed))
            }
        }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }


    private fun startSignInActivity() {
        //Login
        val customLayout = AuthMethodPickerLayout.Builder(R.layout.activity_auth)
            .setGoogleButtonId(R.id.google_signin)
            .setEmailButtonId(R.id.email_signin)
            .build()

        //  new AuthUI.IdpConfig.PhoneBuilder().build(),
        val providers: List<IdpConfig> = listOf(
            EmailBuilder().build(),
            GoogleBuilder().build()
        )

        AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAuthMethodPickerLayout(customLayout)
            //  .setTheme(R.style.LoginTheme)
            .setIsSmartLockEnabled(false, true)
            .setAvailableProviders(providers) //EMAIL
            .build().apply {
                resultLauncher.launch(this)
            }
    }

    private fun showSnackBar(view: View?, message: String?) {
        if (view != null) {
            Snackbar.make(view, message!!, Snackbar.LENGTH_SHORT).show()
        }
    }
}