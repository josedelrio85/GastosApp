package com.example.jose.gastosapp

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.jose.gastosapp.Funciones.Funciones
import kotlinx.android.synthetic.main.gasto_item_recycler.view.*
import kotlinx.android.synthetic.main.gastosmes_item_recycler.view.*
import org.json.JSONArray
import org.json.JSONObject

class GastosEntidadMesAdapter(private val datos: MutableList<JSONObject>) : RecyclerView.Adapter<GastosEntidadMesAdapter.ViewHolder>(){


    class ViewHolder(val linearLayout: LinearLayout) : RecyclerView.ViewHolder(linearLayout)

    //Crea nuevas vistas (invocada por layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GastosEntidadMesAdapter.ViewHolder{
        //crea una nueva vista
        val linearLayout = LayoutInflater.from(parent.context).inflate(R.layout.gastosmes_item_recycler, parent,false ) as LinearLayout
        return ViewHolder(linearLayout);
    }

    override fun onBindViewHolder(holder: GastosEntidadMesAdapter.ViewHolder, position: Int) {
        val obj = datos[position]
        holder.linearLayout.fecha_gastomes.text = Funciones.stringDateToDate(obj.getString("fecha"))
        holder.linearLayout.descripcion_gastomes.text = obj.getString("descripcion")
        holder.linearLayout.importe_gastomes.text = obj.getString("importe")
    }

    override fun getItemCount(): Int = datos.count()

}