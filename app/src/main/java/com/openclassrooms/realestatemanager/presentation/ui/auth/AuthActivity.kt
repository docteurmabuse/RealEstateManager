package com.openclassrooms.realestatemanager.presentation.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityAuthBinding
import com.openclassrooms.realestatemanager.presentation.MainActivity
import com.openclassrooms.realestatemanager.presentation.ui.BaseActivity
import com.openclassrooms.realestatemanager.utils.FirebaseAuthContract
import com.openclassrooms.realestatemanager.utils.RC_SIGN_IN
import timber.log.Timber


class AuthActivity : BaseActivity() {
    private lateinit var binding: ActivityAuthBinding

    private val firebaseAuthContract =
        registerForActivityResult(FirebaseAuthContract()) { result ->
            if (result != null) {
                //  this.createUserInFirestore();
                Timber.d("auth: start MainActivity2")
                startMainActivity()
                showSnackBar(binding.root, getString(R.string.connection_succeed))
            } else {
                Timber.e("auth: authentication error")
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuthContract.launch(RC_SIGN_IN)
    }

    override fun onResume() {
        super.onResume()
        if (this.isCurrentUserLogged()) {
            startMainActivity()
        }
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        Timber.d("auth: start MainActivity")
    }

    private fun showSnackBar(view: ConstraintLayout, message: String?) {
        Snackbar.make(view, message!!, Snackbar.LENGTH_SHORT).show()
    }
}