package com.example.jose.gastosapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class SpinnerAdapter(internal var context: Context, internal var resource: Int, internal var data: JSONArray, internal var valor: String) : BaseAdapter() {
    internal var inflater: LayoutInflater

    init {
        this.inflater = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        return this.data.length()
    }

    override fun getItem(position: Int): Any? {
        var a = this.data.getJSONObject(position)
        return a
    }

    override fun getItemId(position: Int): Long {
        var a = this.data.getJSONObject(position).getString("id").toLong()
        return a
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var convertView = convertView
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(resource, parent,false)

        val valores = convertView!!.findViewById<View>(R.id.custom_Spinner_textView) as TextView
        try {
            var obj : JSONObject = this.data.getJSONObject(position)
            valores.text = obj.getString(valor)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return convertView
    }
}
