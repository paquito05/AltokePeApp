package com.example.altokepeapp.ui.fragment.Perfil

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.altokepeapp.R

import com.example.altokepeapp.ui.activities.LoginActivity
import com.example.altokepeapp.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_perfil.*


class PerfilFragment : Fragment() {

    private lateinit var notificationsViewModel: PerfilViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProvider(this).get(PerfilViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_perfil, container, false)

        notificationsViewModel.text.observe(viewLifecycleOwner, Observer {

        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        iv_recargar_saldo_p.setOnClickListener {
            startActivity(Intent(activity,RecargarSaldoActivity::class.java))
        }

        btn_logout.setOnClickListener {

            FirebaseAuth.getInstance().signOut()
            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        tv_edit.setOnClickListener {
            startActivity(Intent(activity,EditarPerfilActivity::class.java))
        }

        //Cargar la imagen del perfil
        Glide.with(this)
                .load(Constants.imagen)
                .centerCrop()
                .into(iv_user_photo_perfil)

        tv_nombre_perfil.setText(Constants.nombres +"  "+Constants.apellido)
        tv_sexo_perfil.setText(Constants.sexo)
        tv_email_perfil.setText(Constants.email)
        tv_telefono_perfil.setText(Constants.telefono.toString())
        tv_saldo_p.setText(""+Constants.saldoApi)
    }





}