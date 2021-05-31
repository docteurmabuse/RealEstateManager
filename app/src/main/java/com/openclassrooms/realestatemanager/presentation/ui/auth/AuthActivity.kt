package com.openclassrooms.realestatemanager.presentation.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityAuthBinding
import com.openclassrooms.realestatemanager.presentation.MainActivity
import com.openclassrooms.realestatemanager.presentation.ui.BaseActivity
import com.openclassrooms.realestatemanager.utils.FirebaseAuthContract
import com.openclassrooms.realestatemanager.utils.RC_SIGN_IN
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class AuthActivity : BaseActivity() {
    private lateinit var binding: ActivityAuthBinding
    private val authViewModel: AuthViewModel by viewModels()
    private val firebaseAuthContract =
        registerForActivityResult(FirebaseAuthContract()) { result ->
            if (result != null) {
                //  this.createUserInFirestore();
                Timber.d("auth: start MainActivity")
                startMainActivity()
                showSnackBar(binding.root, getString(R.string.connection_succeed))
             /*   val agent =
                    Agent(result.uid, result.email, result.email, result.photoUrl.toString())
                authViewModel.addAgentToRoomDb(agent)*/

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

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        Timber.d("auth: start MainActivity")
    }

    private fun showSnackBar(view: ConstraintLayout, message: String?) {
        Snackbar.make(view, message!!, Snackbar.LENGTH_SHORT).show()
    }
}
