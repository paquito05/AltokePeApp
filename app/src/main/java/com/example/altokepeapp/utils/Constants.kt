package com.example.altokepeapp.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {

    const val  USUARIO: String = "usuario"
    const val ALTOKE_PREFERENCES: String = "ALTOKEPrefs"
    const val LOGGED_IN_USERNAME: String ="logged_in_username"
    const val EXTRA_USER_DETAILS: String = "extra_user_details"
    const val  PICK_IMAGE_REQUEST_CODE = 1
    const val READ_STORAGE_PERMISSION_CODE = 2
    const val USERIO_PERFIL_IMAGE: String = "Userio_Perfil_Image"
    const val MASCULINO: String = "Masculino"
    const val FEMENINO: String = "Femenino"
    const val NOMBRES : String = "nombres"
    const val DIRECCION : String = "direccion"
    const val APELLIDOS : String = "apellidos"
    const val TELEFONO: String = "telefono"
    const val SEXO: String = "sexo"
    const val  IMAGE: String = "image"
    const val  IDUSUARIO: String = "idusuario"
    const val PERFIL_COMPLETO: String = "perfilCompletado"

    var idusarioApi : Int = 0
    var nombres : String = ""
    var direccion : String = ""
    var apellido : String = ""
    var telefono: Long = 0
    var dni: String = ""
    var sexo: String = ""
    var email: String = ""
    var  imagen: String = ""
    var idusuario: Int = 0
    var perfilcompleto: Int = 0



    var saldoApi: Double = 0.0

    fun showImageChooser(activity: Activity){

        //An intent for launching the image selection of phone storage
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        //Lauches the image selection of phone storage using the constant code
        activity.startActivityForResult(galleryIntent,PICK_IMAGE_REQUEST_CODE)
    }

    fun getFileExtension(activity: Activity, uri: Uri?):String?{

        /**
         * MineTypeMap: mapa bidireccional que asigna tipos MIME a extensiones de archivo y viceversa.
         *
         * getSingleton (): Obtiene la instancia singleton de MimeTypeMap.
         *
         * getExtensionFromMimeType: Devuelve la extensión registrada para ese tipo de MINA dado
         *
         * contentResolver.getType: Devuelve el tipo MIME de la URL de contenido dada
         */
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }

}