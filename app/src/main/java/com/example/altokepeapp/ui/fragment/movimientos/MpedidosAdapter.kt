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
import com.example.altokepeapp.models.PedidosApi

class MpedidosAdapter(private val context: Context)
    : RecyclerView.Adapter<MyViewHolderP>() {

    var mPedidoMList= mutableListOf<PedidosApi>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderP {
        val intemView = LayoutInflater.from(context).inflate(R.layout.layout_mpedidos_row, parent, false)
        return MyViewHolderP(intemView)
    }

    override fun getItemCount(): Int {
        return mPedidoMList.size!!
    }

    override fun onBindViewHolder(MyViewHolderP: MyViewHolderP, position: Int) {
        val menu = mPedidoMList.get(position)
        MyViewHolderP.bind(menu)

    }


}


class MyViewHolderP(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var tvFechaPedido: TextView = itemView.findViewById(R.id.tv_fecha_pedido)
    var tvNombrePedido: TextView = itemView.findViewById(R.id.tv_nombre_pedido)
    var tvCantidadPedido: TextView = itemView.findViewById(R.id.tv_cantidad_pedido)
    var tvTotalPedido: TextView = itemView.findViewById(R.id.tv_total_pedido)

    fun bind(pedidosApi: PedidosApi) {
        tvFechaPedido.text = pedidosApi.fechamenu
        tvNombrePedido.text = pedidosApi.nombre
        tvCantidadPedido.text = pedidosApi.cantidad.toString()
        tvTotalPedido.text = pedidosApi.monto.toString()
    }

}






