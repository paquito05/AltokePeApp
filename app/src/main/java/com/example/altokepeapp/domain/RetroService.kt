package com.example.altokepeapp.domain

import com.example.altokepeapp.models.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RetroService {

    @GET("menu/listar/2")
    fun ListarMenu() : Call<List<Menu>>

    @POST("pedidos/insertar")
    fun insertarPedido(@Body pedido: Pedido): Call<List<Pedido>>

    @POST("movimientos/insertar")
    fun insertarRecargaSaldo(@Body recargarApi: RecargarApi): Call<List<RecargarApi>>

    @POST("usuario/insertar")
    fun registrarUsuarioApi(@Body usuarioApi: UsuarioApi): Call<List<UsuarioApi>>

    @GET("usuario/listar/usuario/{id}")
    fun listarUsuario(@Path("id") id: Int): Call<List<UsuarioApi>>

    @GET("movimientos/listarP/{id}")
    fun ListarMovimientoPedidos(@Path("id") id: Int) : Call<List<PedidosApi>>

    @GET("movimientos/listarR/{id}")
    fun ListarMovimientoRecargas(@Path("id") id: Int) : Call<List<RecargaSaldoApi>>


}