package com.example.altokepeapp.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
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
import com.example.altokepeapp.utils.Constants
import com.example.altokepeapp.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_completar_usuario_perfil.*
import java.io.IOException

class CompletarUsuarioPerfilActivity : BaseActivity(), View.OnClickListener {

    private lateinit var mUsuarioDetails: Usuario
    private var mSelectImageFileUri: Uri? = null
    private  var mUserProfileImageURL: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completar_usuario_perfil)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
            //Get the user details from intent as a ParcelableExtraa.
            mUsuarioDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }




        et_nombres.setText(mUsuarioDetails.nombres)
        et_apellidos.setText(mUsuarioDetails.apellidos)
        et_email.isEnabled = false
        et_email.setText(mUsuarioDetails.email)

        if (mUsuarioDetails.perfilCompletado == 0){
            tv_title.text = resources.getString(R.string.title_completar_perfil)

            et_nombres.isEnabled = false


            et_apellidos.isEnabled = false

        }else{
            setupActionBar()
            tv_title.text = resources.getString(R.string.title_editar_perfil)
            GlideLoader(this@CompletarUsuarioPerfilActivity).loadUserPicture(mUsuarioDetails.image,iv_user_photo)

            if (mUsuarioDetails.telefono != 0L){
                et_telefono.setText(mUsuarioDetails.telefono.toString())
            }
            if (mUsuarioDetails.sexo == Constants.MASCULINO){
                rb_masculino.isChecked = true
            }else{
                rb_femenino.isChecked = true
            }

        }

        et_apellidos.isEnabled = false
        et_apellidos.setText(mUsuarioDetails.apellidos)

        et_nombres.isEnabled = false
        et_nombres.setText(mUsuarioDetails.nombres)

        et_email.isEnabled = false
        et_email.setText(mUsuarioDetails.email)

        iv_user_photo.setOnClickListener(this@CompletarUsuarioPerfilActivity)
        btn_submit.setOnClickListener(this@CompletarUsuarioPerfilActivity)




    }

    private fun setupActionBar(){
        setSupportActionBar(toolbar_usuario_Completar_perfil_activity)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_black_24dp)
        }

        toolbar_usuario_Completar_perfil_activity.setNavigationOnClickListener { onBackPressed() }
    }



    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {

                R.id.iv_user_photo -> {

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

                R.id.btn_submit -> {

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

        val firstName = et_nombres.text.toString().trim{it <= ' '}
        if (firstName != mUsuarioDetails.nombres){
            useHashMap[Constants.NOMBRES] = firstName
        }

        val lastName = et_apellidos.text.toString().trim{it <= ' '}
        if (lastName != mUsuarioDetails.apellidos){
            useHashMap[Constants.APELLIDOS] = lastName
        }


        val telefono = et_telefono.text.toString().trim { it <= ' ' }

        val direccion = et_direccion.text.toString().trim { it <= ' ' }

        val sexo = if (rb_femenino.isChecked) {
            Constants.FEMENINO
        } else {
            Constants.MASCULINO
        }

        if (mUserProfileImageURL.isNotEmpty()){
            useHashMap[Constants.IMAGE] = mUserProfileImageURL
        }

        if (telefono.isNotEmpty() && telefono != mUsuarioDetails.telefono.toString()) {
            useHashMap[Constants.TELEFONO] = telefono.toLong()
        }

        if (sexo.isNotEmpty() && sexo != mUsuarioDetails.sexo) {
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

    fun userProfileUpdateSuccess() {
        hideProgessDialog()

        Toast.makeText(
            this@CompletarUsuarioPerfilActivity,
            resources.getString(R.string.msg_perfil_update_exitoso), Toast.LENGTH_SHORT
        ).show()

        startActivity(Intent(this@CompletarUsuarioPerfilActivity, DashboardActivity::class.java))
        finish()

    }

    private fun validarUsuarioPerfilDetails(): Boolean {


        return when {

            TextUtils.isEmpty(et_direccion.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_direccion), true)
                false
            }

            TextUtils.isEmpty(et_telefono.text.toString().trim { it <= ' ' }) -> {
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
                        // The uri of selected image from phone storage.
                        mSelectImageFileUri = data.data!!

                        //iv_user_photo.setImageURI(selectedImageFileUri)
                        GlideLoader(this).loadUserPicture(mSelectImageFileUri!!, iv_user_photo)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@CompletarUsuarioPerfilActivity,
                            resources.getString(R.string.image_seleccion_fallida), Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            //A Log is printed when user clase ir cancel the image selection.
            Log.e("Request", "Image selection cancelled")
        }

    }

    fun imageUploadSuccess(imageURL: String) {
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
            //If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // showErrorSnackBar("The storage permision is granted.", false)
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