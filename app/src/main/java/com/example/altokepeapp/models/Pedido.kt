package com.example.altokepeapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Pedido(
    val cantidad: Int,
    val descripcion: String,
    val direccion: String,
    val fechapedido: String,
    val idmenu: Int,
    val idusuario: Int,
    val nombre: String
): Parcelable