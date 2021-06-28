package com.example.altokepeapp.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.example.altokepeapp.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_olvido_password.*

class OlvidoPasswordActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_olvido_password)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        setupActionBar()

    }


    private fun setupActionBar() {
        setSupportActionBar(toolbar_olvido_password_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_black_24dp)
        }
        toolbar_olvido_password_activity.setNavigationOnClickListener { onBackPressed() }





        btn_enviar.setOnClickListener {

            val email: String = et_email_olvido_pw.text.toString().trim { it <= ' ' }

            if (email.isEmpty()) {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
            } else {
                showProgressDialog(resources.getString(R.string.espere_por_favor))

                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        hideProgessDialog()

                        if (task.isSuccessful) {
                            // Muestre el mensaje de brindis y finalice la actividad de contraseña olvidada para volver al
                            Toast.makeText(
                                this@OlvidoPasswordActivity,
                                resources.getString(R.string.email_enviado_correctamente),
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()

                        } else {
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    }
            }
        }
    }
}