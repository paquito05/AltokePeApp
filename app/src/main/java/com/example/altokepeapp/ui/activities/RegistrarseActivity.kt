package com.example.altokepeapp.ui.activities

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.example.altokepeapp.R
import com.example.altokepeapp.domain.RetroInstance
import com.example.altokepeapp.domain.RetroService
import com.example.altokepeapp.firestore.FirestoreClass
import com.example.altokepeapp.models.Usuario
import com.example.altokepeapp.models.UsuarioApi
import com.example.altokepeapp.utils.Constants
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_registrarse.*
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegistrarseActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrarse)

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

        tv_login.setOnClickListener { onBackPressed() }
        btn_registrarse.setOnClickListener {

            if(validateRegisterDetails()){

                if(et_password.length() > 8){

                    val usuarioApi = UsuarioApi(
                            idusuario = 0,
                            apellidos = et_apellidos.text.toString(),
                            nombres = et_nombres.text.toString(),
                            dni = et_dni.text.toString(),
                            celular = "",
                            dinero = 0.0,
                            correo = et_email.text.toString(),
                            contrasenia = ""

                    )

                    RegistarUsuarioApi(usuarioApi)

                }else{
                    showErrorSnackBar(resources.getString(R.string.err_msg_enter_password_mayor_10_caracteres), true)
                }

            }

        }

    }


    private fun setupActionBar() {
        setSupportActionBar(toolbar_registrase_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_black_24dp)
        }

        toolbar_registrase_activity.setNavigationOnClickListener { onBackPressed() }
    }


    /**
     *
     */
    private fun validateRegisterDetails(): Boolean {
        return when {
            TextUtils.isEmpty(et_nombres.text.toString().trim() { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_nombres), true)
                false
            }

            TextUtils.isEmpty(et_apellidos.text.toString().trim() { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_apellidos), true)
                false
            }

            TextUtils.isEmpty(et_email.text.toString().trim() { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(et_password.text.toString().trim() { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }

            TextUtils.isEmpty(et_confirma_password.text.toString().trim() { it <= ' ' }) -> {
                showErrorSnackBar(
                        resources.getString(R.string.err_msg_enter_confirma_password),
                        true
                )
                false
            }


            et_password.text.toString().trim { it <= ' ' } != et_confirma_password.text.toString()
                    .trim { it <= ' ' } -> {
                showErrorSnackBar(
                        resources.getString(R.string.err_msg_enter_password_and_confirma_password_no_conciden),
                        true
                )
                false
            }

            !cb_terminos_condiciones.isChecked -> {
                showErrorSnackBar(
                        resources.getString(R.string.err_msg_agree_terminos_y_condiciones),
                        true
                )
                false
            }

            else -> {
                //showErrorSnackBar(resources.getString(R.string.registrarse_exitosamente), false)
                true
            }

        }
    }



    fun RegistarUsuarioApi(usuarioApi: UsuarioApi) {

        showProgressDialog(resources.getString(R.string.espere_por_favor))

        val service = RetroInstance.getRetroInstance().create<RetroService>(RetroService::class.java)
        val usuarioRegistrar = service.registrarUsuarioApi(usuarioApi)
        usuarioRegistrar.enqueue(object : Callback<List<UsuarioApi>> {

            override fun onResponse(call: Call<List<UsuarioApi>>, response: Response<List<UsuarioApi>>) {
                if (response.isSuccessful) {

                    Constants.idusarioApi = response.body()?.get(0)?.idusuario!!
                    Constants.saldoApi = response.body()?.get(0)?.dinero!!
                    Log.i("RegistrarUsuario", "2 ${Constants.idusarioApi}")

                    // Verifique con la función de validación si las entradas son válidas o no.
                    if (validateRegisterDetails()) {

                        showProgressDialog(resources.getString(R.string.espere_por_favor))

                        val email: String = et_email.text.toString().trim() { it <= ' ' }
                        val password: String = et_password.text.toString().trim() { it <= ' ' }

                        // Cree una instancia y cree un registro de usuario con correo electrónico y contraseña.
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(
                                        OnCompleteListener<AuthResult> { task ->

                                            //hideProgessDialog()

                                            //If the registration is successfully done
                                            if (task.isSuccessful) {

                                                //Firebase registered user
                                                val firebaseUser: FirebaseUser = task.result!!.user!!

                                                val usuario = Usuario(
                                                        firebaseUser.uid,
                                                        et_nombres.text.toString().trim { it <= ' ' },
                                                        et_apellidos.text.toString().trim { it <= ' ' },
                                                        et_email.text.toString().trim { it <= ' ' },
                                                        et_dni.text.toString().trim { it <= ' ' },
                                                        idusuario = Constants.idusarioApi


                                                )

                                                Log.i("RegistrarUsuario", "1 ${Constants.idusarioApi}")

                                                hideProgessDialog()
                                                FirestoreClass().registerUser(this@RegistrarseActivity, usuario)

//                            showErrorSnackBar(
//                                "You are registered successfully. Your user id is ${firebaseUser.uid}",
//                                false
//                            )

                                                FirebaseAuth.getInstance().signOut()
                                                finish()
                                            } else {
                                                hideProgessDialog()
                                                //If the registering is not successful then show error message.
                                                showErrorSnackBar(task.exception!!.message.toString(), true)
                                            }
                                        })
                    }






                    Log.i("RegistrarUsuario", "3 ${Constants.idusarioApi}")

                    Toast.makeText(
                            applicationContext,
                            "Se insertó correctamente",
                            Toast.LENGTH_LONG
                    ).show()

                } else {
                    Log.e("RegistrarActivity", "OnResponse: Error al insertar")
                }
            }

            override fun onFailure(call: Call<List<UsuarioApi>>, t: Throwable) {
                Log.e("RegistrarActivity", "OnFailure: Error al insertar")
            }
        })

    }


    fun userRegistrationSuccess() {

        //Hide the progress dialog
        //hideProgessDialog()

        Toast.makeText(
                this@RegistrarseActivity,
                resources.getString(R.string.registrase_exitoso),
                Toast.LENGTH_SHORT
        ).show()
    }

}


