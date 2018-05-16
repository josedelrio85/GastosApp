package com.example.jose.gastosapp

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.JsonReader
import android.view.View
import android.widget.*
import com.example.jose.gastosapp.services.entidadesService
import org.json.JSONObject
import android.util.Log
import com.example.jose.gastosapp.services.gastosService
import com.example.jose.gastosapp.services.tiposService
import org.json.JSONArray
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class EditGastoActivity : AppCompatActivity() {

    private var jsonObj : JSONObject = JSONObject()

    override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_edit_gasto)

         jsonObj = JSONObject(intent.getStringExtra("key"))
         MyAsyncTask().execute()
    }

    inner class MyAsyncTask: AsyncTask<String, Int, Int>(){

        lateinit var listaEntidades: JSONArray
        lateinit var listaTipos: JSONArray
        var position: Int = 0

        override fun onPreExecute() {
            super.onPreExecute()
            Log.e("onPreExecute", "hola" )
        }

        override fun doInBackground(vararg params: String?): Int {
            listaEntidades =entidadesService.getEntidades()
            listaTipos = tiposService.getTipos()

            if(jsonObj.has("id")){
                for(i in (0..listaEntidades.length()-1)){
                    if(listaEntidades.getJSONObject(i).get("id") == jsonObj.getJSONObject("entidad").get("id")){
                        this.position = i
                    }
                }

                for(i in (0..listaTipos.length()-1)){
                    if(listaTipos.getJSONObject(i).get("id") == jsonObj.getJSONObject("tipo").get("id")){
                        this.position = i
                    }
                }
            }
            return 1
        }

        override fun onPostExecute(result: Int?) {
            super.onPostExecute(result)

            var activo: Int = 0;
            val txtFecha = findViewById<EditText>(R.id.editTextFecha)

            val calendar : Calendar = Calendar.getInstance()
            val dateListener = object : DatePickerDialog.OnDateSetListener{
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    txtFecha.setText(actualizaVistaFecha(calendar))

                }
            }

            val imageBtnCalendario = findViewById(R.id.imageBtnCalendario) as ImageView
            imageBtnCalendario.setOnClickListener(object: View.OnClickListener{
                override fun onClick(v: View?) {
                    DatePickerDialog(this@EditGastoActivity, dateListener,
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)).show()

                }
            })

            val txtDescripcion = findViewById (R.id.editTextDescripcion) as EditText
            val txtImporte = findViewById (R.id.editTextImporte) as EditText

            val checkActivo = findViewById (R.id.checkBox) as CheckBox
            checkActivo.setOnCheckedChangeListener { buttonView, isChecked ->
                activo = if(isChecked) 1 else 0
            }

            val spinEntidad = findViewById(R.id.spinnerEntidad) as Spinner
            spinEntidad.adapter = SpinnerAdapter(applicationContext, R.layout.custom_spinner_items, listaEntidades, "nombreEntidad")
            spinEntidad.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {}

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

            val spinTipo = findViewById<Spinner>(R.id.spinnerTipo)
            spinTipo.adapter = SpinnerAdapter(applicationContext, R.layout.custom_spinner_items, listaTipos, "tipoMovimiento")
            spinTipo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

            val btnGuardar = findViewById(R.id.spinnerGuardar) as Button
            val btnEliminar = findViewById(R.id.btnEliminar) as Button

            if(jsonObj.has("id")){

                val formato = "dd-MM-yyyy"
                val sdf = SimpleDateFormat(formato, Locale.FRENCH)
                val fecha = jsonObj.getString("fecha")
                val a = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.FRENCH)
                try{
                    val date: Date = a.parse(fecha)
                    txtFecha.setText(sdf.format(date).toString())
                }catch (e: ParseException){
                    e.printStackTrace()
                }

                txtDescripcion.setText(jsonObj.getString("descripcion"))
                txtImporte.setText(jsonObj.getString("importe"))

                spinEntidad.setSelection(this.position)
                spinTipo.setSelection(this.position)

                if(jsonObj.getInt("activo") == 1)
                    checkActivo.isChecked = true

                btnEliminar.visibility = View.VISIBLE
            }

            var msg : String = "Es necesario indicar un valor para: "

            btnGuardar.setOnClickListener(object: View.OnClickListener{

                override fun onClick(v: View?) {

                    var validado : Boolean = true

                    if(txtFecha.text.isEmpty()){
                        msg += " \n - Fecha "
                        validado = false
                    }

                    if(txtDescripcion.text.isEmpty()){
                        validado = false
                        msg += " \n - Descripción "
                    }

                    if(txtImporte.text.isEmpty()){
                        validado = false
                        msg += " \n - Importe "
                    }


                    val obj = JSONObject().apply {
                        put("fecha", formateaFechaApi(txtFecha.text.toString()))
                        put("descripcion", txtDescripcion.text)
                        put("importe", txtImporte.text)
                        put("activo", activo)
                        put("isActivo", checkActivo.isChecked)
                    }

                    val entidad: JSONObject = spinEntidad.selectedItem as JSONObject
                    val tipoMovimiento: JSONObject = spinTipo.selectedItem as JSONObject
                    obj.put("idTipoMovimiento", entidad["id"].toString())
                    obj.put("idEntidad", tipoMovimiento["id"].toString())

                    if(validado){
                        if(jsonObj.has("id")) {
                            obj.put("id", jsonObj.getInt("id"))
                            gastosService.updateGasto(obj, applicationContext, this@EditGastoActivity)
                        }else{
                            //gastos service post
                            gastosService.guardaGasto(obj, applicationContext, this@EditGastoActivity)
                        }
                    }else{
                        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
                        msg = "Es necesario indicar un valor para: "
                    }
                }
            })

            btnEliminar.setOnClickListener(object: View.OnClickListener{
                override fun onClick(v: View?) {
                    if(jsonObj.has("id")){
                        gastosService.eliminaGasto(jsonObj.getInt("id"), applicationContext, this@EditGastoActivity)
                    }
                }
            })
        }

        private fun actualizaVistaFecha(cal: Calendar) : String{
            val formato = "dd-MM-yyyy"
            val sdf = SimpleDateFormat(formato, Locale.FRENCH)
            val a  = cal.time as Date
            return sdf.format(a).toString()
        }

        private fun formateaFechaApi(fec: String) : String{
            val fOrigen = "dd-MM-yyyy"
            val fSalida = "yyyy-MM-dd"
            try{
                val sdfOrigen = SimpleDateFormat(fOrigen, Locale.FRENCH)
                val myDate = sdfOrigen.parse(fec)

                val sdfSalida = SimpleDateFormat(fSalida, Locale.FRENCH)
                return sdfSalida.format(myDate)
            }catch(e: Exception){
                Log.e("error", e.toString())
            }
            return ""
        }
    }

    override fun finish() {
        var returnIntent = Intent()
        returnIntent.putExtra("centinela", 1)
        setResult(RESULT_OK, returnIntent)
        super.finish()
    }
}
