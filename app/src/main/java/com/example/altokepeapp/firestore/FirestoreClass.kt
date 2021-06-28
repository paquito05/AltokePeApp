package com.example.altokepeapp.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.example.altokepeapp.models.Usuario
import com.example.altokepeapp.ui.activities.CompletarUsuarioPerfilActivity
import com.example.altokepeapp.ui.activities.DashboardActivity
import com.example.altokepeapp.ui.activities.LoginActivity
import com.example.altokepeapp.ui.activities.RegistrarseActivity
import com.example.altokepeapp.ui.fragment.Perfil.EditarPerfilActivity
import com.example.altokepeapp.ui.fragment.Perfil.RecargarSaldoActivity
import com.example.altokepeapp.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun getCurrentUserID(): String {
        // Una instancia del usuario actual que usa Firebase Auth
        val currentUser = FirebaseAuth.getInstance().currentUser

        // Una variable para asignar el currentUserId si no es nulo o estará en blanco.
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid

        }
        return currentUserID
    }

    fun registerUser(activity: RegistrarseActivity, usuarioInfo: Usuario) {

        // los "usuarios" es el nombre de la colección. Si la colección ya está creada, no volverá a crearla.
        mFireStore.collection(Constants.USUARIO)
                // ID de documento para campos de usuarios. Aquí el documento es el ID de usuario.
            .document(usuarioInfo.id)
                // Aquí el campo userInfo es y Setoption está configurado para fusionarse. Es por si queremos fusionar más adelante en lugar de reemplazar los campos
            .set(usuarioInfo, SetOptions.merge())
            .addOnSuccessListener {

                // Aquí llama a una función de actividad base para transferirle el resultado.
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgessDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error al registrar al usuario.",
                    e
                )
            }
    }

    fun getUsuarioDetails(activity: Activity) {

        // Aquí pasamos el nombre de la colección de los datos que  queremos .
        mFireStore.collection(Constants.USUARIO)

            // La identificación del documento para obtener los campos de usuario.
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.toString())

                // Aquí hemos recibido la instantánea del documento que se convierte en el objeto del modelo de datos de usuario.
                val usuario = document.toObject(Usuario::class.java)!!

                val sharedPreferences = activity.getSharedPreferences(Constants.ALTOKE_PREFERENCES, Context.MODE_PRIVATE)

                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                //Key: logged_in_username
                //Value:

                editor.putString(
                    Constants.LOGGED_IN_USERNAME,
                    "${usuario.nombres} ${usuario.apellidos}"
                )
                editor.apply()


                //START
                when (activity) {
                    is LoginActivity -> {
                        // Llamar a una función de la actividad base para transferirle el resultado.
                        activity.userLoggedInSuccess(usuario)
                    }
                    is RecargarSaldoActivity -> {
                        activity.userRecargarDetailSucess(usuario)
                    }
                    is  DashboardActivity -> {
                        activity.cargarDatosUsuario(usuario)
                    }
                    is EditarPerfilActivity -> {
                        activity.cargarDatosUsuario(usuario)
                    }
//
//                    is SettingsActivity -> {
//                        activity.userDetailSucess(user)
//                    }
                }

                //END
            }
            .addOnFailureListener { e ->
                // Ocultar el diálogo de progreso si hay algún error. E imprime el error en el registro
                when (activity) {
                    is LoginActivity -> {
                        activity.hideProgessDialog()
                    }
                    is RecargarSaldoActivity -> {
                        activity.hideProgessDialog()
                    }
                    is  DashboardActivity -> {
                        activity.hideProgessDialog()
                    }
                    is EditarPerfilActivity -> {
                        activity.hideProgessDialog()
                    }
                }

                Log.e(activity.javaClass.simpleName, "Error al obtener los detalles del usuario.", e)
            }
    }

    fun subirImagealStorage(activity: Activity, imageFileURI: Uri?) {
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            Constants.USERIO_PERFIL_IMAGE + System.currentTimeMillis() + "."
                    + Constants.getFileExtension(
                activity, imageFileURI
            )
        )

        sRef.putFile(imageFileURI!!).addOnSuccessListener { taskSnapshot ->
            // La carga de la imagen es exitosa
            Log.e(
                "Firebase Image URL", taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
            )

            // Obtenga la URL descargable de la instantánea de la tarea
            taskSnapshot.metadata!!.reference!!.downloadUrl
                .addOnSuccessListener { uri ->
                    Log.e("Downloadable Image URL", uri.toString())

                    when(activity){
                        is CompletarUsuarioPerfilActivity -> {
                            activity.imageUploadSuccess(uri.toString())
                        }
                        is EditarPerfilActivity -> {
                            activity.EditarimageUploadSuccess(uri.toString())
                        }
                    }

                }
        }

            .addOnFailureListener{ exception ->

                //hide the progress dialog if there is any error. And print the error in log.
                when(activity){
                    is CompletarUsuarioPerfilActivity -> {
                        activity.hideProgessDialog()
                    }
                    is EditarPerfilActivity -> {
                        activity.hideProgessDialog()
                    }
                }

                Log.e(activity.javaClass.simpleName,exception.message,exception)

            }
    }

    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>) {

        mFireStore.collection(Constants.USUARIO)
            .document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener {
                when (activity) {
                    is CompletarUsuarioPerfilActivity -> {
                        //Hide the progress dialog is any error. And print the error in Log.
                        activity.userProfileUpdateSuccess()
                    }
                    is EditarPerfilActivity ->{
                        activity.EditaruserProfileUpdateSuccess()
                    }
                }
            }
            .addOnFailureListener { e ->
                when (activity) {
                    is CompletarUsuarioPerfilActivity -> {
                        //Hide the progress dialog if there is any error. And print the error in log
                        activity.hideProgessDialog()
                    }
                    is EditarPerfilActivity -> {
                        activity.hideProgessDialog()
                    }

                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating the user details.", e
                )
            }
    }

}