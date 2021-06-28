package com.example.altokepeapp.ui.fragment.Perfil

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.altokepeapp.R
import com.example.altokepeapp.firestore.FirestoreClass
import com.example.altokepeapp.models.Usuario
import com.example.altokepeapp.ui.activities.BaseActivity
import com.example.altokepeapp.ui.activities.DashboardActivity
import com.example.altokepeapp.utils.Constants
import com.example.altokepeapp.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_editar_perfil.*
import java.io.IOException


class EditarPerfilActivity :  BaseActivity(), View.OnClickListener {

    private var mSelectImageFileUri: Uri? = null
    private  var mUserProfileImageURL: String = ""
    private lateinit var mUsuarioDetails: Usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_perfil)


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

        RespuestaCargarDatosUsuario()


        iv_user_photo_editar.setOnClickListener(this@EditarPerfilActivity)
        btn_submit_editar.setOnClickListener(this@EditarPerfilActivity)


    }


    fun cargarDatosUsuario(user: Usuario) {

        mUsuarioDetails = user

        GlideLoader(this@EditarPerfilActivity).loadUserPicture(mUsuarioDetails.image,iv_user_photo_editar)

        if (mUsuarioDetails.telefono != 0L){
            et_telefono_editar.setText(mUsuarioDetails.telefono.toString())
        }
        if (mUsuarioDetails.sexo == Constants.MASCULINO){
            rb_masculino_editar.isChecked = true
        }else{
            rb_femenino_editar.isChecked = true
        }

        et_apellidos_editar.isEnabled = true
        et_apellidos_editar.setText(mUsuarioDetails.apellidos)

        et_nombres_editar.isEnabled = true
        et_nombres_editar.setText(mUsuarioDetails.nombres)

        et_email_editar.isEnabled = true
        et_email_editar.setText(mUsuarioDetails.email)

        et_direccion_editar.isEnabled = true
        et_direccion_editar.setText(mUsuarioDetails.direccion)
    }


    override fun onStart() {
        super.onStart()

    }

    private fun RespuestaCargarDatosUsuario() {
        FirestoreClass().getUsuarioDetails(this)
    }


    private fun setupActionBar(){
        setSupportActionBar(toolbar_usuario_editar_perfil_activity)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_black_24dp)
        }

        toolbar_usuario_editar_perfil_activity.setNavigationOnClickListener { onBackPressed() }
    }



    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {

                R.id.iv_user_photo_editar -> {

                    // Aquí comprobaremos id, el permiso ya está permitido o necesitamos solicitarlo.
                    // En primer lugar comprobaremos el permiso READ_EXTERNAL_STORAGE y si no está permitido
                    if (
                        ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        //showErrorSnackBar("Ya tienes el permiso de almacenamiento.", false)
                        Constants.showImageChooser(this)
                    } else {

                        /*
                        Solicite que se otorguen permisos a esta aplicación. Estos permisos
                        deben solicitarse en su manifiesto, no deben otorgarse a su aplicación,
                        y deberían tener nivel de protección
                        */

                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )

                    }
                }

                R.id.btn_submit_editar -> {

                    if (validarUsuarioPerfilDetails()) {
                        showProgressDialog(resources.getString(R.string.espere_por_favor))
                        if(mSelectImageFileUri != null)
                            FirestoreClass().subirImagealStorage(this,mSelectImageFileUri)
                        else{
                            updateUsuarioPerfilDetails()
                        }

                    }

                }
            }
        }
    }




    private fun updateUsuarioPerfilDetails(){
        val useHashMap = HashMap<String, Any>()

        val firstName = et_nombres_editar.text.toString().trim{it <= ' '}
        if (firstName != mUsuarioDetails.nombres){
            useHashMap[Constants.NOMBRES] = firstName
        }

        val lastName = et_apellidos_editar.text.toString().trim{it <= ' '}
        if (lastName != mUsuarioDetails.apellidos){
            useHashMap[Constants.APELLIDOS] = lastName
        }


        val telefono = et_telefono_editar.text.toString().trim { it <= ' ' }

        val direccion = et_direccion_editar.text.toString().trim { it <= ' ' }

        val sexo = if (rb_femenino_editar.isChecked) {
            Constants.FEMENINO
        } else {
            Constants.MASCULINO
        }

        if (mUserProfileImageURL.isNotEmpty()){
            useHashMap[Constants.IMAGE] = mUserProfileImageURL
        }

        if (telefono.isNotEmpty() && telefono != Constants.telefono.toString()) {
            useHashMap[Constants.TELEFONO] = telefono.toLong()
        }

        if (sexo.isNotEmpty() && sexo != Constants.sexo) {
            useHashMap[Constants.SEXO] = sexo
        }

        // KEY : Gender vale: male
        useHashMap[Constants.SEXO] = sexo

        useHashMap[Constants.DIRECCION] = direccion

        useHashMap[Constants.PERFIL_COMPLETO] = 1

        //showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().updateUserProfileData(this, useHashMap)

        //showErrorSnackBar("Your details are valid. You can update them.",false)
    }



    fun EditaruserProfileUpdateSuccess() {
        hideProgessDialog()

        Toast.makeText(
            this@EditarPerfilActivity,
            resources.getString(R.string.msg_perfil_update_exitoso), Toast.LENGTH_SHORT
        ).show()

        startActivity(Intent(this@EditarPerfilActivity, DashboardActivity::class.java))
        finish()

    }

    private fun validarUsuarioPerfilDetails(): Boolean {
        return when {

            TextUtils.isEmpty(et_nombres_editar.text.toString().trim() { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_nombres), true)
                false
            }

            TextUtils.isEmpty(et_apellidos_editar.text.toString().trim() { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_apellidos), true)
                false
            }

            TextUtils.isEmpty(et_email_editar.text.toString().trim() { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(et_direccion_editar.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_direccion), true)
                false
            }

            TextUtils.isEmpty(et_telefono_editar.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_telefono), true)
                false
            }

            else -> {
                true
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    try {
                        // El uri de la imagen seleccionada del almacenamiento del teléfono.
                        mSelectImageFileUri = data.data!!

                        //iv_user_photo.setImageURI(selectedImageFileUri)
                        GlideLoader(this).loadUserPicture(mSelectImageFileUri!!, iv_user_photo_editar)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@EditarPerfilActivity,
                            resources.getString(R.string.image_seleccion_fallida), Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            // Se imprime un Log cuando la clase de usuario cancela la selección de la imagen.
            Log.e("Request", "Selección de imagen cancelada")
        }

    }


    fun EditarimageUploadSuccess(imageURL: String) {
        //hideProgessDialog()

        mUserProfileImageURL = imageURL
        updateUsuarioPerfilDetails()

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            // Si se concede el permiso
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // showErrorSnackBar ("Se concede el permiso de almacenamiento", falso)
                Constants.showImageChooser(this)
            } else {
                //Desplay another toast if permission is not granted
                Toast.makeText(
                    this, resources.getString(R.string.read_storage_permisos_denegado),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }



}