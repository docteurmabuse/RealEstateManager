package com.openclassrooms.realestatemanager.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.AuthUI.IdpConfig
import com.firebase.ui.auth.AuthUI.IdpConfig.EmailBuilder
import com.firebase.ui.auth.AuthUI.IdpConfig.GoogleBuilder
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.snackbar.Snackbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityAuthBinding
import com.openclassrooms.realestatemanager.ui.property.MainActivity
import com.openclassrooms.realestatemanager.utils.Constant.RC_SIGN_IN
import com.openclassrooms.realestatemanager.utils.FirebaseAuthContract
import com.openclassrooms.realestatemanager.utils.Utils


class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding

    private val firebaseAuthContract =
        registerForActivityResult(FirebaseAuthContract()) { result ->
            if (result != null) {
                //  this.createUserInFirestore();
                Log.d(Utils.TAG, "auth: start MainActivity")
                startMainActivity()
                showSnackBar(binding.root, getString(R.string.connection_succeed))
            } else {
                Log.e(Utils.TAG, "auth: authentication error")
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        // setContentView(binding.root)
        firebaseAuthContract.launch(RC_SIGN_IN)
        // startSignInActivity()
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        Log.d(Utils.TAG, "auth: start MainActivity")
    }

    private fun showSnackBar(view: ConstraintLayout, message: String?) {
        Snackbar.make(view, message!!, Snackbar.LENGTH_SHORT).show()
    }

    private fun startSignInActivity() {
        //Login
        val customLayout = AuthMethodPickerLayout.Builder(R.layout.activity_auth)
            .setGoogleButtonId(R.id.google_signin)
            .setEmailButtonId(R.id.email_signin)
            .build()

        //  new AuthUI.IdpConfig.PhoneBuilder().build(),
        val providers: List<IdpConfig> = arrayListOf(
            EmailBuilder().build(),
            GoogleBuilder().build()
        )
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAuthMethodPickerLayout(customLayout)
                //.setTheme(R.style.LoginTheme)
                .setIsSmartLockEnabled(false, true)
                .setAvailableProviders(providers) //EMAIL
                .build(), RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val response: IdpResponse? = IdpResponse.fromResultIntent(data)
            if (resultCode == RESULT_OK) { // SUCCESS
                // this.createUserInFirestore()
                startMainActivity()
                showSnackBar(binding.root, getString(R.string.connection_succeed))
            } else { // ERRORS
                if (response == null) {
                    showSnackBar(binding.root, getString(R.string.connection_succeed))
                    Log.e(Utils.TAG, "auth: authentication error")
                    finish()
                }
            }
        }
    }
}