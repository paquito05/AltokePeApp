package com.example.altokepeapp.ui.fragment.pedido

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.altokepeapp.R
import com.example.altokepeapp.domain.RetroInstance
import com.example.altokepeapp.domain.RetroService
import com.example.altokepeapp.models.Pedido
import com.example.altokepeapp.ui.activities.BaseActivity
import com.example.altokepeapp.utils.Constants
import kotlinx.android.synthetic.main.activity_pedido.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class PedidoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pedido)
        setupActionBar()

        //Obtengo la fecha actual.
        val fechaActual = SimpleDateFormat("yyyy-MM-dd").format(Date())


        val Inombre = intent.getStringExtra("nombre")
        val Idescripcion = intent.getStringExtra("descripcion")
        val Iimagen = intent.getStringExtra("imagen")
        val Iidmenu = intent.getIntExtra("idmenu",0)
        val Iprecio = intent.getDoubleExtra("precio",0.0)


        Log.i("PedidoActivity","N ${Inombre}")
        Log.i("PedidoActivity","D ${Idescripcion}")
        Log.i("PedidoActivity","I ${Iimagen}")
        Log.i("PedidoActivity","IM ${Iidmenu}")
        Log.i("PedidoActivity","P ${Iprecio}")

        //Cargar la imagen del menu
        Glide.with(this)
                .load(Iimagen)
                .centerCrop()
                .into(iv_menu_pedido)

        tv_menu_pedido.setText(Inombre)
        tv_descripcion_pedido.setText(Idescripcion)
        tv_precio_pedido.setText(Iprecio.toString())
        et_direccion_pedido.setText(Constants.direccion)


        btn_realizarPedido.setOnClickListener {

            var cantidad:Int = et_cantidad_pedido.text.toString().toInt()

            var total = Iprecio * cantidad

            showProgressDialog(resources.getString(R.string.espere_por_favor))

            if (validateRegisterDetails()){

                if (total > Constants.saldoApi){
                    showErrorSnackBar(resources.getString(R.string.err_msg_enter_saldo_insuficiente), true)
                    hideProgessDialog()
                }else{
                    val pedido = Pedido(
                            cantidad = et_cantidad_pedido.text.toString().toInt(),
                            descripcion = Idescripcion.toString(),
                            direccion = et_direccion_pedido.text.toString(),
                            fechapedido = fechaActual,
                            idusuario = Constants.idusuario,
                            nombre = " ",
                            idmenu = Iidmenu
                    )

                    RealizarPedido(pedido)
                }
            }
        }

    }


    private fun setupActionBar() {
        setSupportActionBar(toolbar_realizar_pedido_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_black_24dp)
        }

        toolbar_realizar_pedido_activity.setNavigationOnClickListener { onBackPressed() }

    }


    /**
     *
     */
    private fun validateRegisterDetails(): Boolean {
        return when {
            TextUtils.isEmpty(et_cantidad_pedido.text.toString().trim() { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_cantidad_p), true)
                false
            }

            TextUtils.isEmpty(et_direccion_pedido.text.toString().trim() { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_direccion_p), true)
                false
            }

            else -> {
                showErrorSnackBar(resources.getString(R.string.pedido_exitosamente), false)
                true
            }

        }
    }


    fun RealizarPedido(pedido: Pedido){
        val pedidoService = RetroInstance.getRetroInstance().create<RetroService>(RetroService::class.java)
        val pedidoCall = pedidoService.insertarPedido(pedido)

        pedidoCall.enqueue(object : Callback<List<Pedido>>{
            override fun onResponse(call: Call<List<Pedido>>, response: Response<List<Pedido>>) {
                if (response.isSuccessful){
                    Toast.makeText(
                        applicationContext,
                        "Pedido Realizado Correctamente",
                        Toast.LENGTH_LONG
                    ).show()

                    Log.i("PedidoActivity","${response.body()}")
                    // Una vez insertado, finalizamos el activity para retornar a MainActivity

                    hideProgessDialog()

                    finish()
                }else{
                    Log.i("PedidoActivity Response","Error al insertar response")
                }

            }

            override fun onFailure(call: Call<List<Pedido>>, t: Throwable) {
                Log.i("PedidoActivity Failure","Error al insertar ")
            }
        })
    }


}