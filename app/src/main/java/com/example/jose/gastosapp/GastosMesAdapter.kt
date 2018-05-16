package com.example.jose.gastosapp

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.gastos_entidad_mes_recycler.view.*
import org.json.JSONObject
import java.math.RoundingMode
import java.text.DecimalFormat

class GastosMesAdapter(private val datos: HashMap<String, MutableList<JSONObject>>, activity: GastosMesActivity) : RecyclerView.Adapter<GastosMesAdapter.ViewHolder>(){

    var activity = activity
    private lateinit var context: Context


    class ViewHolder(var constraintLayout: ConstraintLayout) : RecyclerView.ViewHolder(constraintLayout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GastosMesAdapter.ViewHolder {
        //crea una nueva vista
        context = parent.context
        val linearLayout = LayoutInflater.from(parent.context).inflate(R.layout.gastos_entidad_mes_recycler, parent, false) as ConstraintLayout
        return GastosMesAdapter.ViewHolder(linearLayout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        var keys = datos.keys

        var keysClave = ArrayList<String>()
        keys.forEach { t ->
            keysClave.add(t)
        }
        var indice = keysClave[position]
        var gastosPorEntidad = datos[indice]

        holder.constraintLayout.textView2.setText(indice)

        holder.constraintLayout.recicler_view_entidadgastosmes.adapter = GastosEntidadMesAdapter(gastosPorEntidad!!)
        holder.constraintLayout.recicler_view_entidadgastosmes.layoutManager = LinearLayoutManager(context)

        //crear total
        var total = getTotal(gastosPorEntidad)
        holder.constraintLayout.textView3.setText(total)

    }

    override fun getItemCount(): Int {
        return datos.count()
    }


    fun getTotal(obj : MutableList<JSONObject>?): String {
        var total: Double = 0.0
        obj!!.forEach { t ->
            total += t.getString("importe").toDouble()
        }
        var df: DecimalFormat = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        return df.format(total)
    }
}