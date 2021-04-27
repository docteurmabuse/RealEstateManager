package com.openclassrooms.realestatemanager.utils

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseAuthContract :
    ActivityResultContract<Int, FirebaseUser?>() {

    private val providers = arrayListOf(
        AuthUI.IdpConfig.EmailBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build()
    )

    override fun createIntent(
        context: Context, code: Int?
    ): Intent {
        return AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
    }

    override fun parseResult(
        resultCode: Int, intent: Intent?
    ): FirebaseUser? {
        return if (resultCode == RESULT_OK) {
            FirebaseAuth.getInstance().currentUser
        } else {
            null
        }
    }
}