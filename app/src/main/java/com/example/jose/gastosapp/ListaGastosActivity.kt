package com.example.jose.gastosapp

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.example.jose.gastosapp.services.gastosService
import org.json.JSONArray
import org.json.JSONObject

class ListaGastosActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView;
    private lateinit var viewAdapter: RecyclerView.Adapter<*>;
    private lateinit var viewManager: RecyclerView.LayoutManager;

    private var datos: JSONArray = JSONArray();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_gastos)
        loadData().execute()
    }

    inner class loadData : AsyncTask<Int, Int ,Int>(){

        override fun doInBackground(vararg params: Int?): Int {
            datos = gastosService.getGastos()
            return 1
        }

        override fun onPostExecute(result: Int?) {
            super.onPostExecute(result)

            viewManager = LinearLayoutManager(applicationContext);
            viewAdapter = ListaGastosAdapter(datos, this@ListaGastosActivity);

            recyclerView = findViewById<RecyclerView>(R.id.recicler_view_gastos).apply {
                setHasFixedSize(true);

                layoutManager = viewManager;
                adapter = viewAdapter;
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var menuInflater : MenuInflater = MenuInflater(applicationContext)
        menuInflater.inflate(R.menu.prueba, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val idMenu : Int = item!!.itemId
        when(idMenu){
            2131165210 ->{
                intent = Intent(applicationContext, EditGastoActivity::class.java)
                val b = Bundle()
                val obj : JSONObject = JSONObject()
                b.putString("key", obj.toString())
                intent.putExtras(b)
                startActivityForResult(intent, 1)
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1 && resultCode == RESULT_OK){
//            var valorPasado = data!!.extras.get("centinela")
//            viewAdapter.notifyDataSetChanged();
            loadData().execute()
        }
    }

}
