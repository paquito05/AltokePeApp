package com.example.altokepeapp.ui.fragment.Perfil

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.altokepeapp.R
import com.example.altokepeapp.domain.RetroInstance
import com.example.altokepeapp.domain.RetroService
import com.example.altokepeapp.firestore.FirestoreClass
import com.example.altokepeapp.models.RecargarApi
import com.example.altokepeapp.models.Usuario
import com.example.altokepeapp.ui.activities.BaseActivity
import com.example.altokepeapp.ui.activities.DashboardActivity
import com.example.altokepeapp.utils.Constants
import kotlinx.android.synthetic.main.activity_recagar_saldo.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RecargarSaldoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recagar_saldo)

        setupActionBar()


        btn_recargar_saldo.setOnClickListener {

            getUserDetailsProcessRecarga()
        }

    }


    private fun getUserDetailsProcessRecarga(){
        showProgressDialog(resources.getString(R.string.espere_por_favor))
        FirestoreClass().getUsuarioDetails(this)
    }

    fun userRecargarDetailSucess(user: Usuario){

        if (validateRecargaTargetaDetails()){
            val recarga = RecargarApi(
                    idusuario = Constants.idusuario,
                    monto = et_monto_recarga.text.toString().toInt()

            )
            Log.i("RegistrarUsuario","${Constants.idusuario}")

            RealizarRecargaSaldo(recarga)


        }else{

            hideProgessDialog()
        }


        //hideProgessDialog()

    }



    private fun setupActionBar() {
        setSupportActionBar(toolbar_recargar_saldo_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_black_24dp)
        }

        toolbar_recargar_saldo_activity.setNavigationOnClickListener { onBackPressed() }
    }


    /**
     *
     */
    private fun validateRecargaTargetaDetails(): Boolean {
        return when {
            TextUtils.isEmpty(et_numero_targeta.text.toString().trim() { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_numero_targeta), true)
                false
            }

            TextUtils.isEmpty(et_fecha_vencimiento_recargar_saldo.toString().trim() { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_fecha_targeta), true)
                false
            }

            TextUtils.isEmpty(et_cvv.text.toString().trim() { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_numero_cvv), true)
                false
            }

            TextUtils.isEmpty(et_monto_recarga.text.toString().trim() { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_monto_recarga), true)
                false
            }

            else -> {
                showErrorSnackBar(resources.getString(R.string.recargar_saldo_exitosamente), false)
                true
            }

        }
    }


    fun RealizarRecargaSaldo(recargarApi: RecargarApi){
        val Service = RetroInstance.getRetroInstance().create<RetroService>(RetroService::class.java)
        val recargaSaldoCall = Service.insertarRecargaSaldo(recargarApi)

        recargaSaldoCall.enqueue(object : Callback<List<RecargarApi>> {
            override fun onResponse(call: Call<List<RecargarApi>>, response: Response<List<RecargarApi>>) {
                if (response.isSuccessful){

                    hideProgessDialog()
                    Toast.makeText(applicationContext, "Se recargo correctamente ", Toast.LENGTH_LONG
                    ).show()
                    startActivity(Intent(this@RecargarSaldoActivity, DashboardActivity::class.java))

                    Log.i("RecargarSaldoactivity","${response.body()}")
                    // Una vez insertado, finalizamos el activity para retornar a MainActivity
                    finish()

                }else{
                    Log.i("RecargarSaldoactivity","Error al insertar la recarga Response")
                }

            }

            override fun onFailure(call: Call<List<RecargarApi>>, t: Throwable) {
                Log.i("RecargarSaldoactivity","Error al insertar la recarga Failure")
            }
        })
    }
}