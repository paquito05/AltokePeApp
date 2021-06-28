package com.example.altokepeapp.models

data class RecargaSaldoApi(
    val estado: Int,
    val fecha: String,
    val idmovimiento: Int,
    val monto: Int,
    val tipo: String
)