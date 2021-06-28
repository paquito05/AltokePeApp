package com.example.altokepeapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UsuarioApi(
     val idusuario: Int?,
    val apellidos: String,
    val celular: String,
    val contrasenia: String,
    val correo: String,
    val dni: String,
     val dinero: Double? ,
    val nombres: String
):Parcelable

