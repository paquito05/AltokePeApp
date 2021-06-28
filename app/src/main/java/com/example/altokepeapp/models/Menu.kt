package com.example.altokepeapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Menu(
    val descripcion: String,
    val fechamenu: String,
    val idmenu: Int,
    val imagen: String,
    val nombre: String,
    val precio: Double
): Parcelable