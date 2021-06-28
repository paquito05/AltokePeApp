package com.example.altokepeapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Usuario (
    val id: String = "",
    val nombres: String = "",
    val apellidos: String = "",
    val email: String = "",
    val dni:String = "",
    val idusuario:Int? = 0,
    val direccion:String = "",
    val image: String = "",
    val telefono: Long = 0,
    val sexo: String = "",
    val perfilCompletado: Int = 0
):Parcelable