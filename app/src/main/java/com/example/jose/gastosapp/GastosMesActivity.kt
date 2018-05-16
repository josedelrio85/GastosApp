package com.example.jose.gastosapp

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.jose.gastosapp.services.gastosService.getGastosMes
import org.json.JSONArray
import java.util.*
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import com.example.jose.gastosapp.Model.EnumMeses
import org.json.JSONObject
import kotlin.collections.HashMap


class GastosMesActivity : AppCompatActivity() {
    private var datos: JSONArray = JSONArray();
    private lateinit var recyclerView: RecyclerView;
    private lateinit var viewAdapter: RecyclerView.Adapter<*>;
    private lateinit var viewManager: RecyclerView.LayoutManager;

    private var listaMeses : JSONArray = JSONArray()
    private var listaAnhos : JSONArray = JSONArray()

    private var centinela: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gastos_mes)

        var year = Calendar.getInstance().get(Calendar.YEAR)
        var month = Calendar.getInstance().get(Calendar.MONTH)
        var monthActual = month + 1
        var f = year.toString() + "-" + monthActual.toString()

        var intMes: Int = 0
        var stringAnho: String = ""

        listaMeses = generaMeses()
        var spinMeses = findViewById(R.id.spinnerMes) as Spinner
        spinMeses.adapter = SpinnerAdapter(applicationContext, R.layout.custom_spinner_items, listaMeses, "nombreMes")
        spinMeses.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(centinela){
                    intMes = position + 1
                    f = stringAnho + "-" + intMes.toString()
                    loadData().execute(f)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
        spinMeses.setSelection(month)


        listaAnhos = generaAnhos()
        var spinAnhos = findViewById(R.id.spinnerAnho) as Spinner
        spinAnhos.adapter = SpinnerAdapter(applicationContext, R.layout.custom_spinner_items, listaAnhos, "nombreAnho")
        spinAnhos.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var elemento = spinAnhos.selectedItem as JSONObject
                stringAnho = elemento.getString("nombreAnho")
                if(centinela){
                    f = stringAnho + "-" + intMes.toString()
                    loadData().execute(f)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
        spinAnhos.setSelection(1)



        loadData().execute(f)
    }

    inner class loadData : AsyncTask<String, Int, Int>(){

        override fun doInBackground(vararg params: String?): Int {
            var fecha = params[0].toString()
            datos = getGastosMes(param = fecha)
            return 1
        }

        override fun onPostExecute(result: Int?) {
            super.onPostExecute(result)

            var prueba = HashMap<String, MutableList<JSONObject>>()
            var total: Double = 0.0

            for(i in 0..datos.length()-1){
                var gasto : JSONObject = datos[i] as JSONObject
                var entidad = gasto.getJSONObject("entidad")
                var key = entidad.getString("nombreEntidad")

                if(prueba.containsKey(key)){
                    var a: MutableList<JSONObject> = prueba.get(key)!!
                    a.add(gasto)
                }else{
                    var b = mutableListOf<JSONObject>()
                    b.add(gasto)
                    prueba.put(key, b)
                }
            }

            viewManager = LinearLayoutManager(applicationContext);
            viewAdapter = GastosMesAdapter(prueba, this@GastosMesActivity);

             recyclerView = findViewById<RecyclerView>(R.id.recicler_view_gastosmes).apply {

                setHasFixedSize(true);

                layoutManager = viewManager;
                adapter = viewAdapter;
            }

            centinela = true
        }
    }

    private fun generaAnhos(): JSONArray{
        var anho =  Calendar.getInstance().get(Calendar.YEAR)
        var anhoAnt = anho - 1
        var anhoSig = anho + 1
        var arrayMeses = JSONArray()

        var jsonobj = JSONObject()
        jsonobj.put("id", 0)
        jsonobj.put("nombreAnho", anhoAnt)
        arrayMeses.put(jsonobj.getInt("id"), jsonobj)

        var jsonobj1 = JSONObject()
        jsonobj1.put("id", 1)
        jsonobj1.put("nombreAnho", anho)
        arrayMeses.put(jsonobj1.getInt("id"), jsonobj1)

        var jsonobj2 = JSONObject()
        jsonobj2.put("id", 2)
        jsonobj2.put("nombreAnho", anhoSig)
        arrayMeses.put(jsonobj2.getInt("id"), jsonobj2)

        return arrayMeses
    }

    private fun generaMeses(): JSONArray{
        var arrayMeses = EnumMeses.values()
        var listaM = JSONArray()
        arrayMeses.forEachIndexed { index, enumMeses ->
            var jsonobj = JSONObject()
            jsonobj.put("id", index)
            jsonobj.put("nombreMes", enumMeses.toString())
            listaM.put(index, jsonobj)
        }
        return listaM
    }
}
