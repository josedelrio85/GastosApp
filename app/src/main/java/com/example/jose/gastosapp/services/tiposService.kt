package com.example.jose.gastosapp.services

import android.util.Log
import com.example.jose.gastosapp.BuildConfig
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import com.loopj.android.http.SyncHttpClient
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

object tiposService{
    private var client = SyncHttpClient()
    private val URL_BASE = BuildConfig.BASE_URL_DEV
//    private var URL_BASE = BuildConfig.BASE_URL_PROD

    private var listaTipos: JSONArray = JSONArray()

    fun getTipos() : JSONArray{
        val url = URL_BASE + "tiposmovimiento/"
        var requestParams = RequestParams()

        client.get(url, requestParams, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONArray) {
                super.onSuccess(statusCode, headers, response)
                listaTipos = response
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseString: String?, throwable: Throwable?) {
                super.onFailure(statusCode, headers, responseString, throwable)
                Log.e("onFailure -> ", throwable.toString())
            }
        })
        return listaTipos
    }
}