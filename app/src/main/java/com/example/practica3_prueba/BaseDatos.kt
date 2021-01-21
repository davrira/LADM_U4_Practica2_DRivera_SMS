package com.example.practica3_prueba

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BaseDatos(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    //nombreBase = contenido
    var plataforma = "NETFLIX"
    var tipo = "PELICULA"
    var nombre = "dfdsfasf"

    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL("CREATE TABLE entrantes (" +
                "Celular VARCHAR(200)," +
                "Plataforma VARCHAR(200)," +
                "Respondido VARCHAR(2)"+
                ")"
        )//CreateTable

        db.execSQL(
            "CREATE TABLE PeliculasNetflix (" +
                    "Plataforma VARCHAR(200),"+
                    "Nombre VARCHAR(200)"+
                    ")"
        )//create


        db.execSQL(
            "INSERT INTO PeliculasNetflix (Plataforma, Nombre) VALUES " +
                    "('NETFLIX','The blackList'),"+
                    "('NETFLIX','Ozark'),"+
                    "('NETFLIX','Marianne')," +
                    "('HBO','Juego de tronos'),"+
                    "('HBO','Sharp Objects'),"+
                    "('HBO','Watchmen'),"+
                    "('DISNEY','The mandalorian')," +
                    "('DISNEY','Loki'),"+
                    "('DISNEY','Falcon y el soldado de invierno')"


        )//insert



    }//onCreate

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }//onUpgrade

}//class

