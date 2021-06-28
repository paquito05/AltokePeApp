package com.example.altokepeapp.ui.fragment.movimientos

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.altokepeapp.domain.RetroInstance
import com.example.altokepeapp.domain.RetroService
import com.example.altokepeapp.models.Menu
import com.example.altokepeapp.models.PedidosApi
import com.example.altokepeapp.models.RecargaSaldoApi
import com.example.altokepeapp.models.RecargarApi
import com.example.altokepeapp.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovimientosViewModel : ViewModel() {

    lateinit var recyclerViewPedidos: MutableLiveData<List<PedidosApi>>
    lateinit var recyclerViewRecargas : MutableLiveData<List<RecargaSaldoApi>>

    init {
        recyclerViewPedidos = MutableLiveData()
        recyclerViewRecargas = MutableLiveData()
    }

    fun getPedidosListObserverable(): MutableLiveData<List<PedidosApi>>{
        return recyclerViewPedidos
    }
    fun getRecargasListObserverable(): MutableLiveData<List<RecargaSaldoApi>>{
        return recyclerViewRecargas
    }

    fun ListarPedidos(){
        val RetroInstance = RetroInstance.getRetroInstance().create<RetroService>(RetroService::class.java)
        val pedidoLista = RetroInstance.ListarMovimientoPedidos(Constants.idusuario)
        pedidoLista.enqueue(object : Callback<List<PedidosApi>>{
            override fun onResponse(call: Call<List<PedidosApi>>, response: Response<List<PedidosApi>>) {
                if (response.isSuccessful){
                    recyclerViewPedidos.postValue(response.body())
                }else{
                    recyclerViewPedidos.postValue(null)
                }
            }

            override fun onFailure(call: Call<List<PedidosApi>>, t: Throwable) {
                recyclerViewPedidos.postValue(null)
            }
        })

    }

    fun ListarRecargas(){
        val RetroInstance = RetroInstance.getRetroInstance().create<RetroService>(RetroService::class.java)
        val recargarLista = RetroInstance.ListarMovimientoRecargas(Constants.idusuario)
        recargarLista.enqueue(object : Callback<List<RecargaSaldoApi>>{
            override fun onResponse(call: Call<List<RecargaSaldoApi>>, response: Response<List<RecargaSaldoApi>>) {
                if (response.isSuccessful){
                    recyclerViewRecargas.postValue(response.body() )
                }else{
                    recyclerViewRecargas.postValue(null)
                }
            }

            override fun onFailure(call: Call<List<RecargaSaldoApi>>, t: Throwable) {
                recyclerViewRecargas.postValue(null)
            }
        })
    }




}