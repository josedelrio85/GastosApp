package com.example.jose.gastosapp

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

class ListaGastosDummyActivity : Activity(){
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private val dataSet: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_gastos_dummy)

        myDataSet()
        
        viewManager = LinearLayoutManager(this)
        viewAdapter = ListaGastosDummyAdapter(dataSet)

        recyclerView = findViewById<RecyclerView>(R.id.recicler_view_gastos).apply{
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }
    }


    fun myDataSet(){
        dataSet.add("hola")
        dataSet.add("kase")
        dataSet.add("adios")
        dataSet.add("pepe")
        dataSet.add("manolo")
        dataSet.add("sanjismundo")
        dataSet.add("mecedora")
        dataSet.add("lacasito")
        dataSet.add("gominola")
        dataSet.add("dibartolomei")
        dataSet.add("modeloM")
    }
}
