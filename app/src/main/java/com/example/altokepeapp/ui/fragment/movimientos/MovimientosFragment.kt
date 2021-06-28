package com.example.altokepeapp.ui.fragment.movimientos

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.altokepeapp.R
import com.example.altokepeapp.models.PedidosApi
import com.example.altokepeapp.models.RecargaSaldoApi
import com.example.altokepeapp.models.Usuario
import kotlinx.android.synthetic.main.fragment_movimientos.*


class MovimientosFragment : Fragment() {

    private lateinit var mPedidosAdapter: MpedidosAdapter
    private lateinit var mRecargasAdapter: MrecargasAdapter
    private lateinit var movimientosViewModel: MovimientosViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_movimientos, container, false)
        movimientosViewModel = ViewModelProvider(this).get(MovimientosViewModel::class.java)

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        InicioReciclerViewPedidos()
        InicioPedidosViewModelMovimientos()
        InicioReciclerViewRecargas()
        InicioRecargasViewModelMovimientos()

    }

    @SuppressLint("FragmentLiveDataObserve")
    fun InicioPedidosViewModelMovimientos(){
        movimientosViewModel.getPedidosListObserverable().observe(this,Observer<List<PedidosApi>>{
            if(it == null){
                    Toast.makeText(activity,"No Tienes ningun Pedido Realizado",Toast.LENGTH_SHORT).show()
            }else{
                mPedidosAdapter.mPedidoMList = it.toMutableList()
                mPedidosAdapter.notifyDataSetChanged()
            }
        })
        movimientosViewModel.ListarPedidos()
    }

    private fun InicioReciclerViewPedidos(){
        rv_pedidos_m.apply {
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(activity,DividerItemDecoration.VERTICAL))
            mPedidosAdapter = MpedidosAdapter(requireContext())
            adapter = mPedidosAdapter
        }
    }

    @SuppressLint("FragmentLiveDataObserve")
    fun InicioRecargasViewModelMovimientos(){
        movimientosViewModel.getRecargasListObserverable().observe(this,Observer<List<RecargaSaldoApi>>{
            if (it == null){
                //Toast.makeText(activity,"No tienes Niguna Recarga Realizada",Toast.LENGTH_SHORT).show()
            } else {
                mRecargasAdapter.mRecargasList = it.toMutableList()
                mRecargasAdapter.notifyDataSetChanged()
            }
        })
        movimientosViewModel.ListarRecargas()
    }

    private fun InicioReciclerViewRecargas(){
        rv_recargas_m.apply {
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(activity,DividerItemDecoration.VERTICAL))
            mRecargasAdapter = MrecargasAdapter(requireContext())
            adapter = mRecargasAdapter
        }
    }

}