package com.example.proyect

import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlinx.android.synthetic.main.activity_main2_grafic.*


class Main2ActivityGRAFICA : AppCompatActivity() {

    val entries = ArrayList<BarEntry>() // Arreglo para cargar los decibeles de las antenas en eje de las Y
    val labels = ArrayList<String>() // Arreglo para cargar los nombres de las antenas en el eje de las X
    var cursor : Cursor?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2_grafic)
        cargarDatos()
        setBarChart()
    }
    fun cargarDatos(){
        val admin = adminBD(this)
        cursor = admin.Consulta("Select * from decibeles Order by dbi")
    }
    fun setBarChart() {
        var i = 0
        if (cursor!!.moveToFirst()){
            do {
                val nom = cursor!!.getString(1)
                val dbi = cursor!!.getFloat(2)
                entries.add(BarEntry(dbi, i)) // Agregamos los valores de las antenas
                labels.add(nom) // Agregamos nombre de las antenas
                i++
            } while (cursor!!.moveToNext())
            val barDataSet = BarDataSet(entries, "Datos")
            val data = BarData(labels, barDataSet)
            barChart.data = data
            barChart.setDescription("Graficas de decibeles por Antena")
            barDataSet.color = resources.getColor(R.color.colorAccent)
            barChart.animateY(4000)
        }
    }

}
