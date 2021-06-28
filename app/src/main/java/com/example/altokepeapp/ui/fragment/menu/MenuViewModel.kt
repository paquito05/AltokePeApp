package com.example.altokepeapp.ui.fragment.menu

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.altokepeapp.domain.RetroInstance
import com.example.altokepeapp.domain.RetroService
import com.example.altokepeapp.models.Menu
import com.example.altokepeapp.models.UsuarioApi
import com.example.altokepeapp.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuViewModel : ViewModel() {


    lateinit var recyclerListData: MutableLiveData<List<Menu>>

    private val _mostrarSaldo = MutableLiveData<String>().apply {
        value = ""+Constants.saldoApi
    }
    val mostrarSaldo: LiveData<String> = _mostrarSaldo

    init {
        recyclerListData = MutableLiveData()
    }

    fun getMenuListObserverable() : MutableLiveData<List<Menu>>{
        return recyclerListData
    }



    fun ListarMenus(){
        val menuService = RetroInstance.getRetroInstance().create<RetroService>(RetroService::class.java)
        val menuLista = menuService.ListarMenu()
        menuLista.enqueue(object : Callback<List<Menu>> {
            override fun onResponse(call: Call<List<Menu>>, response: Response<List<Menu>>) {
                if (response.isSuccessful){
                    recyclerListData.postValue(response.body())
                }else{
                    recyclerListData.postValue(null)
                }
            }

            override fun onFailure(call: Call<List<Menu>>, t: Throwable) {
               recyclerListData.postValue(null)
            }
        })
    }




}