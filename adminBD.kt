package com.example.proyect

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.lang.Exception

class adminBD(context: Context): SQLiteOpenHelper(context,DataBase,null,1) {
    companion object{
        val DataBase = "Contactos"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table usuario" +
                "(correoUsr text primary key," +
                " nomusr text, contra text)")

        db?.execSQL("create table comunidades" +
        "(clave text primary key," +
                "nomComu text, guardian text)")

        db?.execSQL("create table antenas" +
        "(idantena INTEGER primary key," +
        "nomAnt text, modelo text, tipoAnt text)")

        db?.execSQL("create table reportes"+
                "(numReport INTEGER primary key, " +
                "empleado text,"+
                "calidad text, " +
                "intensidad float, " +
                "cxq float, " +
                "velocidad float)"
        )
        db?.execSQL("create table decibeles" +
        "(idDbi INTEGER primary key, " +
        "antenaGrafic text, "+
        "dbi INTEGER)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    //Funcion para mandar ejecutar un insert, update o delete
    fun Ejecuta(sentencia: String) : Int{
        try {
            val db = this.writableDatabase
            db.execSQL(sentencia)
            return 1
        }
        catch (ex: Exception){
            return 0 //terminacion no exitosa
        }
    }

    //funcion para mandar ejecutar una consulta SQL (select)
    fun Consulta(query: String): Cursor?{
        try {
            val db = this.readableDatabase
            return db.rawQuery(query,null)

        }
        catch (ex:java.lang.Exception) {
            return null
        }
    }
}

