package com.example.altokepeapp.ui.fragment.menu

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.altokepeapp.R
import com.example.altokepeapp.domain.RetroInstance
import com.example.altokepeapp.domain.RetroService
import com.example.altokepeapp.models.Menu
import com.example.altokepeapp.models.Pedido
import com.example.altokepeapp.ui.activities.DashboardActivity
import com.example.altokepeapp.ui.activities.LoginActivity
import com.example.altokepeapp.ui.fragment.Perfil.PerfilFragment
import com.example.altokepeapp.ui.fragment.pedido.PedidoActivity
import com.example.altokepeapp.utils.Constants
import kotlinx.android.synthetic.main.fragment_menu.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MenuFragment : Fragment(),OnItemClickListener {
    private lateinit var menuAdapter: MenuAdapter
    private lateinit var menuViewModel: MenuViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_menu, container, false)
        menuViewModel = ViewModelProvider(this).get(MenuViewModel::class.java)


        return root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            InicioReciclerView()
            InicioViewModelMenu()

    }

    override fun onStart() {
        super.onStart()
        tv_saldo_m.setText(Constants.saldoApi.toString())
    }

    @SuppressLint("FragmentLiveDataObserve")
    fun InicioViewModelMenu(){
        menuViewModel.getMenuListObserverable().observe(this,Observer<List<Menu>>{
            if (it == null){
                Toast.makeText(activity,"No Hay Menus del dia",Toast.LENGTH_SHORT).show()
            }else{
                menuAdapter.menuList = it.toMutableList()
                menuAdapter.notifyDataSetChanged()
            }
        })
        menuViewModel.ListarMenus()
    }

    private fun InicioReciclerView(){
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(activity,DividerItemDecoration.VERTICAL))
            menuAdapter =  MenuAdapter(requireContext(),this@MenuFragment)
            adapter = menuAdapter
        }
    }



    override fun onItemClicked(menu: Menu){
        val i = Intent(activity, PedidoActivity::class.java)
        i.putExtra("nombre", menu.nombre)
        i.putExtra("descripcion", menu.descripcion)
        i.putExtra("imagen", menu.imagen)
        i.putExtra("idmenu", menu.idmenu)
        i.putExtra("precio", menu.precio)
        startActivity(i)
    }
}