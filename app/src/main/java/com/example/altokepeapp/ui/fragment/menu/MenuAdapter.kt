package com.example.altokepeapp.ui.fragment.menu

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

class MenuAdapter(private val context: Context, val itemClickListener: OnItemClickListener)
    : RecyclerView.Adapter<MyViewHolder>() {

    var menuList = mutableListOf<Menu>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val intemView = LayoutInflater.from(context).inflate(R.layout.layout_menu_row, parent, false)
        return MyViewHolder(intemView)
    }

    override fun getItemCount(): Int {
        return menuList.size!!
    }

    override fun onBindViewHolder(MyViewHolder: MyViewHolder, position: Int) {
        val menu = menuList.get(position)
        MyViewHolder.bind(menu, itemClickListener)

    }


}


class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var ivMenu: ImageView = itemView.findViewById(R.id.iv_menu)
    var tvTitle: TextView = itemView.findViewById(R.id.tv_title)
    var tvCosto: TextView = itemView.findViewById(R.id.tv_costo)

    fun bind(menu: Menu,clickListener: OnItemClickListener) {
        tvTitle.text = menu.nombre
        tvCosto.text = menu.precio.toString()
        Glide.with(itemView)
                .load(menu.imagen)
                .centerCrop()
                .into(ivMenu)

        itemView.setOnClickListener {
            clickListener.onItemClicked(menu)
        }
    }

}

interface OnItemClickListener{
    fun onItemClicked(menu: Menu)
}




