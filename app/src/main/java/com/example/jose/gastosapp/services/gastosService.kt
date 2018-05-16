package com.example.jose.gastosapp.services

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.jose.gastosapp.BuildConfig
import com.loopj.android.http.*
import org.json.JSONArray
import org.json.JSONObject
import cz.msebera.android.httpclient.Header
import cz.msebera.android.httpclient.HttpResponse
import cz.msebera.android.httpclient.entity.StringEntity
import cz.msebera.android.httpclient.protocol.HTTP
import cz.msebera.android.httpclient.message.BasicHeader
import org.w3c.dom.Text
import java.io.UnsupportedEncodingException


object gastosService{
    private val client = SyncHttpClient()
    private val asyncClient = AsyncHttpClient()
    private val URL_BASE = BuildConfig.BASE_URL_DEV
//    private var URL_BASE = BuildConfig.BASE_URL_PROD

    private var listaGastos: JSONArray = JSONArray()
    private var gasto: JSONObject = JSONObject()

    fun getGastos() : JSONArray {
        val url = URL_BASE + "gastos"
        val params = RequestParams()
        client.get(url, params, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>?, res: JSONArray?) {
                Log.d("hola", res!!.toString())
                listaGastos = res
            }

            override fun onFailure(statusCode: Int, headeres: Array<Header>?, res: String?, t: Throwable) {
                Log.getStackTraceString(t)
                println("Received event with data: " + res!!)
            }
        })

        return listaGastos
    }

    fun getGasto(id: Int): JSONObject {
        var url = URL_BASE + "gastos/" + id
        val params = RequestParams()

        client.get(url, params, object: JsonHttpResponseHandler(){

            override fun onStart() {
                super.onStart()
                Log.e("onStart", "hola")
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONArray?) {
                super.onFailure(statusCode, headers, throwable, errorResponse)
                Log.e("onFailure", errorResponse.toString())
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONObject?) {
                super.onFailure(statusCode, headers, throwable, errorResponse)
                Log.e("onFailure", errorResponse.toString())
            }

            override fun onProgress(bytesWritten: Long, totalSize: Long) {
                super.onProgress(bytesWritten, totalSize)
                Log.e("onProgress", bytesWritten.toString())
            }

            override fun onPreProcessResponse(instance: ResponseHandlerInterface?, response: HttpResponse?) {
                super.onPreProcessResponse(instance, response)
                Log.e("onPreProcessResponse", response.toString())
            }

            override fun onPostProcessResponse(instance: ResponseHandlerInterface?, response: HttpResponse?) {
                super.onPostProcessResponse(instance, response)
                Log.e("onPostProcessResponse", instance.toString())
            }

            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONObject) {
                Log.e("onSuccess", response.toString())
                gasto = response
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>?, responseString: String?, throwable: Throwable?) {
                Log.getStackTraceString(throwable)
                println("Received event with data: " + responseString!!)
            }
        })

        return gasto
    }

    fun updateGasto(gasto: JSONObject, context: Context, activity: Activity) {
        var url = URL_BASE + "gastos/" + gasto.getInt("id").toString()
        var se: StringEntity? = null
        try{
            se = StringEntity(gasto.toString())
        }catch (e: UnsupportedEncodingException){
            Log.e("gastosService updateGasto -> expcetion StringEntity asynClient", e.toString())
        }

        se!!.contentType = BasicHeader(HTTP.CONTENT_TYPE, "application/json")

        asyncClient.put(null, url, se, "application/json", object: TextHttpResponseHandler(){
            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseString: String?, throwable: Throwable?) {
                Log.e("asynClient error", "Se produjo un error guardando el gasto")
                Toast.makeText(context, "Se produjo un error actualizando el gasto", Toast.LENGTH_SHORT).show()            }

            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseString: String?) {
                Log.d("asynClient Ok!", "Gasto almacenado correctamente")
                Toast.makeText(context, "Gasto actualizado correctamente", Toast.LENGTH_SHORT).show()
                activity.finish()
            }
        })
    }


    fun guardaGasto(gasto: JSONObject, context: Context, activity: Activity){
        var url = URL_BASE + "gastos"

        // params is a JSONObject
        var se: StringEntity? = null
        try {
            se = StringEntity(gasto.toString())
        } catch (e: UnsupportedEncodingException) {
            Log.e("gastosService guardaGasto -> expcetion StringEntity asynClient", e.toString())
        }

        se!!.contentType = BasicHeader(HTTP.CONTENT_TYPE, "application/json")

        asyncClient.post(null, url, se, "application/json", object: TextHttpResponseHandler(){
            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseString: String?, throwable: Throwable?) {
                Log.e("asynClient error", "Se produjo un error guardando el gasto")
                Toast.makeText(context, "Se produjo un error guardando el gasto", Toast.LENGTH_SHORT).show()
            }

            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseString: String?) {
                Log.d("asynClient Ok!", "Gasto almacenado correctamente")
                Toast.makeText(context, "Gasto almacenado correctamente", Toast.LENGTH_SHORT).show()
                activity.finish()
            }
        })
    }

    fun eliminaGasto(idGasto: Int, context: Context, activity: Activity){
        if(idGasto != null && idGasto > 0){
            var url = URL_BASE + "gastos/" + idGasto

            asyncClient.delete(url, object: TextHttpResponseHandler(){
                override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseString: String?, throwable: Throwable?) {
                    Log.e("asynClient error", "Se produjo un error eliminando el gasto")
                    Toast.makeText(context, "Se produjo un error eliminando el gasto", Toast.LENGTH_SHORT).show()
                }

                override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseString: String?) {
                    Log.d("asynClient Ok!", "Gasto eliminado correctamente")
                    Toast.makeText(context, "Gasto eliminado correctamente", Toast.LENGTH_SHORT).show()
                    activity.finish()
                }
            })
        }
    }

    fun getGastosMes(param: String) : JSONArray{
        if(!param.isNullOrEmpty()){
            var url = URL_BASE + "/gastos/gastosmes/" + param

            client.get(url, object : JsonHttpResponseHandler(){
                override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONArray?) {
                    super.onFailure(statusCode, headers, throwable, errorResponse)
                }

                override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONArray?) {
                    super.onSuccess(statusCode, headers, response)
                    listaGastos = response!!
                }
            })
        }
        return listaGastos
    }

}



