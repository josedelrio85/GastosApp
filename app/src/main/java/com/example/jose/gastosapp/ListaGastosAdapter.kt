package com.example.jose.gastosapp

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.jose.gastosapp.Funciones.Funciones
import kotlinx.android.synthetic.main.gasto_item_recycler.view.*
import org.json.JSONArray
import android.os.Bundle
import android.content.Intent
import android.support.v4.app.ActivityCompat.startActivityForResult


class ListaGastosAdapter(private val datos: JSONArray, activity : Activity) : RecyclerView.Adapter<ListaGastosAdapter.ViewHolder>(){

    var activity = activity

    class ViewHolder(val linearLayout: LinearLayout) : RecyclerView.ViewHolder(linearLayout)


    //Crea nuevas vistas (invocada por layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListaGastosAdapter.ViewHolder{
        //crea una nueva vista
        val linearLayout = LayoutInflater.from(parent.context).inflate(R.layout.gasto_item_recycler, parent,false ) as LinearLayout
        return ViewHolder(linearLayout);
    }

    //sustituye el contenido de una vista(invocada por el layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        // coge el elemento del conjunto de datos en position
        // sustituye el contenido de la vista con ese elemento
        val obj = datos.getJSONObject(position);

        holder.linearLayout.fecha_gasto.text = Funciones.stringDateToDate(obj.getString("fecha"));
        val entidad = obj.getJSONObject("entidad")
        holder.linearLayout.entidad_gasto.text = entidad.getString("nombreEntidad");
        holder.linearLayout.descripcion_gasto.text = obj.getString("descripcion");
        if(obj.getString("activo").toInt() == 0){
            holder.linearLayout.activo_gasto.text = "-"
        }
        holder.linearLayout.importe_gasto.text = obj.getString("importe");

        holder.linearLayout.setOnClickListener {
            val intent = Intent(it.context, EditGastoActivity::class.java)
            val b = Bundle()
            b.putString("key", obj.toString())
            intent.putExtras(b)
            startActivityForResult(activity, intent, 1, b)
        }
    }

    override fun getItemCount(): Int = datos.length();

}