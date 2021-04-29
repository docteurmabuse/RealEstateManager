package com.openclassrooms.realestatemanager.presentation.ui

import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


open class BaseActivity : AppCompatActivity() {
    // UTILS

    // UTILS
    @Nullable
    protected fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    protected fun isCurrentUserLogged(): Boolean? {
        return getCurrentUser() != null
    }
}