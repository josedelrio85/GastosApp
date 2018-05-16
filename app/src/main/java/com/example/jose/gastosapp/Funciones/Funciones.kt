package com.example.jose.gastosapp.Funciones

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object Funciones{

    fun stringDateToDate(dateString : String):String{
//        val spanish = Locale("es", "ES")
//        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", spanish)
        var localDateTime = LocalDateTime.parse(dateString)

        return localDateTime.toLocalDate().toString()
    }
}
