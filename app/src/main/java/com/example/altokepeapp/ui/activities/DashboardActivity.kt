package com.example.altokepeapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.altokepeapp.R
import com.example.altokepeapp.firestore.FirestoreClass
import com.example.altokepeapp.models.Usuario
import com.example.altokepeapp.ui.fragment.Perfil.PerfilFragment
import com.example.altokepeapp.ui.fragment.menu.MenuFragment
import com.example.altokepeapp.ui.fragment.movimientos.MovimientosFragment
import com.example.altokepeapp.utils.Constants
import kotlinx.android.synthetic.main.activity_dashboard.*

import com.example.altokepeapp.domain.RetroInstance
import com.example.altokepeapp.domain.RetroService
import com.example.altokepeapp.models.UsuarioApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardActivity : BaseActivity() {

    private lateinit var mUserDetails: Usuario



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)



        val menuFragmet = MenuFragment()
        val perfilFragment = PerfilFragment()
        val movimientosFragment = MovimientosFragment()

        makeCurrentFragment(menuFragmet)

        nav_view.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_menu -> makeCurrentFragment(menuFragmet)
                R.id.navigation_movimientos -> makeCurrentFragment(movimientosFragment)
                R.id.navigation_perfil -> makeCurrentFragment(perfilFragment)

            }
            true
        }

    }

    private fun makeCurrentFragment(Fragmet: Fragment) =
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.nav_host_fragment, Fragmet)
                commit()
            }

    override fun onStart() {
        super.onStart()
        RespuestaCargarDatosUsuario()

    }


    private fun RespuestaCargarDatosUsuario() {
        FirestoreClass().getUsuarioDetails(this)
    }

    fun cargarDatosUsuario(user: Usuario) {

        mUserDetails = user

        Constants.idusuario = mUserDetails.idusuario!!
        Log.i("RegistrarUsuario", "${Constants.idusuario}")

        ListarUsuarioApi(Constants.idusuario)


        Constants.nombres = mUserDetails.nombres
        Log.i("RegistrarUsuario", "${Constants.nombres}")

        Constants.apellido = mUserDetails.apellidos
        Log.i("RegistrarUsuario", "${Constants.apellido}")

        Constants.direccion = mUserDetails.direccion
        Log.i("RegistrarUsuario", "${Constants.direccion}")

        Constants.telefono = mUserDetails.telefono!!
        Log.i("RegistrarUsuario", "${Constants.telefono}")

        Constants.dni = mUserDetails.dni
        Log.i("RegistrarUsuario", "${Constants.dni}")

        Constants.sexo = mUserDetails.sexo
        Log.i("RegistrarUsuario", "${Constants.sexo}")

        Constants.imagen = mUserDetails.image
        Log.i("RegistrarUsuario", "${Constants.imagen}")

        Constants.email = mUserDetails.email
        Log.i("RegistrarUsuario", "${Constants.email}")

        Constants.perfilcompleto = mUserDetails.perfilCompletado
        Log.i("RegistrarUsuario", "${Constants.perfilcompleto}")


    }


    fun ListarUsuarioApi(id: Int) {
        val service = RetroInstance.getRetroInstance().create<RetroService>(RetroService::class.java)
        val listarUsu = service.listarUsuario(id)
        listarUsu.enqueue(object : Callback<List<UsuarioApi>> {
            override fun onResponse(call: Call<List<UsuarioApi>>, response: Response<List<UsuarioApi>>) {
                if (response.isSuccessful) {
                    Constants.saldoApi = response.body()?.get(0)?.dinero!!
                    Log.i("RegistrarUsuario", "1 ${Constants.saldoApi}")
                } else {
                    Log.i("RegistrarUsuario", "OnResponse: Fallo la llamada")
                }

            }

            override fun onFailure(call: Call<List<UsuarioApi>>, t: Throwable) {
                Log.i("RegistrarUsuario", "onFailure: Fallo la llamada")
            }


        })
    }
}



