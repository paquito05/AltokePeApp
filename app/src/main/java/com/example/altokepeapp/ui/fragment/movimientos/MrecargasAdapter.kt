package com.example.altokepeapp.ui.fragment.movimientos

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.altokepeapp.R
import com.example.altokepeapp.models.Menu
import com.example.altokepeapp.models.RecargaSaldoApi

class MrecargasAdapter(private val context: Context)
    : RecyclerView.Adapter<MyViewHolderR>() {

    var mRecargasList =  mutableListOf<RecargaSaldoApi>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderR {
        val intemView = LayoutInflater.from(context).inflate(R.layout.layout_mrecargas_row, parent, false)
        return MyViewHolderR(intemView)
    }

    override fun getItemCount(): Int {
        return mRecargasList.size!!
    }

    override fun onBindViewHolder(MyViewHolderR: MyViewHolderR, position: Int) {
        val recargas = mRecargasList.get(position)
        MyViewHolderR.bind(recargas)

    }


}


class MyViewHolderR(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var tvFechaRecarga: TextView = itemView.findViewById(R.id.tv_fecha_recarga)
    var tvMontoRecarga: TextView = itemView.findViewById(R.id.tv_monto_recarga)

    fun bind(recargas: RecargaSaldoApi) {
        tvFechaRecarga.text = recargas.fecha
        tvMontoRecarga.text = recargas.monto.toString()

    }

}




