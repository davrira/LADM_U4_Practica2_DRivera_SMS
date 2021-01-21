package com.example.practica3_prueba

import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity

class mensajesEntrantes (numeroP:String, plataformaP:String, respondidoP:String) {

    var numero = numeroP
    var plataforma = plataformaP
    var respondido = respondidoP
    var puntero : AppCompatActivity? = null
    var error = -1


    /*
    Nombre base -->contenido

    Lista
    1=Error en tabla, no se creo o no se pudo conectar
    2=No se pudo realizar consulta/tabla vacia
    */

    fun asignarPuntero (p:AppCompatActivity){
        puntero = p
    }//asignarPuntero


    fun mostrarTodo() : ArrayList<mensajesEntrantes>{

        var datos = ArrayList<mensajesEntrantes>()

        error = -1

        try {

            var base = BaseDatos(puntero!!, "contenido", null, 1)
            var select = base.readableDatabase
            var SQL = "SELECT * FROM entrantes"
            var cursor = select.rawQuery(SQL,null,null)

            if(cursor.moveToFirst()){
                do {

                    var datosTmp = mensajesEntrantes(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2)
                    )

                    datos.add(datosTmp)

                }while (cursor.moveToNext())
            }else{
                error = 2
            }

        }catch (e : SQLiteException){

        }

        return datos
    }//mostrarTodo

}//class