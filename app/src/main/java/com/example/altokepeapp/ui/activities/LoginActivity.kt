package com.example.altokepeapp.ui.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.example.altokepeapp.R
import com.example.altokepeapp.firestore.FirestoreClass
import com.example.altokepeapp.models.Usuario
import com.example.altokepeapp.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(), View.OnClickListener {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        if ( FirestoreClass().getCurrentUserID() != ""){
            //redirect the user Main Screen after log in.
            startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
            finish()
        }

        //Click event assigned to Forgot Password text.
        tv_olvido_password.setOnClickListener(this)
        //Click event assigned to Login button
        btn_login.setOnClickListener(this)
        //Click event assigned to register text.
        tv_register.setOnClickListener(this)



    }

    fun userLoggedInSuccess(usuario: Usuario){

        //Hide the progess dialog.
        hideProgessDialog()

        //Print the user details in the log as of now.
        Log.i("Nombres : ",usuario.nombres)
        Log.i("Apellidos : ",usuario.apellidos)
        Log.i("Email: ",usuario.email)


        if(usuario.perfilCompletado == 0){

            //If the user profile is incomplete then launch the UserProfilectivity.
            val intent = Intent(this@LoginActivity, CompletarUsuarioPerfilActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS, usuario)
            startActivity(intent)
            finish()

        }else{
            //redirect the user Main Screen after log in.
            startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
            finish()
        }




        //Redirect the user Main Screen after log in.
        //startActivity(Intent(this@LoginActivity,MainActivity::class.java))
        finish()
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.tv_olvido_password -> {

                    // Inicie la pantalla Olvidé la contraseña cuando el usuario haga clic en el texto
                    val intent = Intent(this@LoginActivity, OlvidoPasswordActivity::class.java)
                   startActivity(intent)
                }

                R.id.btn_login -> {
                    logInRegistereduser()
                }

                R.id.tv_register -> {
                    // Inicia la pantalla de registro cuando el usuario hace clic en el texto
                    val intent = Intent(this@LoginActivity, RegistrarseActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(et_email.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(et_password.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            else -> {
                //showErrorSnackBar("Tus datos son válidos", false)
                true
            }
        }
    }

    private fun logInRegistereduser() {

        if (validateLoginDetails()) {

            // Muestra el diálogo de progreso.
            showProgressDialog(resources.getString(R.string.espere_por_favor))

            // OBTENER el texto de editText y recortar el espacio
            val email = et_email.text.toString().trim { it <= ' ' }
            val password = et_password.text.toString().trim { it <= ' ' }

            // Iniciar sesión con FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    // Ocultar el diálogo de progreso
                    //hideProgessDialog()

                    if (task.isSuccessful) {

                       FirestoreClass().getUsuarioDetails(this@LoginActivity)
                       // showErrorSnackBar("Ha iniciado sesión correctamente.", false)
                    } else {
                        hideProgessDialog()
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }

    }




}