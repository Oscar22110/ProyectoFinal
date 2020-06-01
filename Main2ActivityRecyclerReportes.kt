package com.example.proyect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main2_recycler_antenas.*
import kotlinx.android.synthetic.main.activity_main2_recycler_reportes.*

class Main2ActivityRecyclerReportes : AppCompatActivity() {

    private lateinit var viewAdapter: ReporteAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    val reportesList: List<reporte> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2_recycler_reportes)

        viewManager = LinearLayoutManager(this)
        viewAdapter = ReporteAdapter(reportesList,this,{estud: reporte -> onItemClickListener(estud) })

        rv_reportes_list.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
            addItemDecoration(
                DividerItemDecoration(this@Main2ActivityRecyclerReportes,
                    DividerItemDecoration.VERTICAL)
            )
        }

        // Metodo para implementar la eliminación de un estudiante, cuando el ususario da un onswiped en
        // el recyclerview
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val position = viewHolder.adapterPosition
                val estud= viewAdapter.getTasks()
                val admin = adminBD(baseContext)
                if (admin.Ejecuta("DELETE FROM reportes WHERE numReport=" + estud[position].numreport) == 1){
                    retrieveReportes()
                }
            }
        }).attachToRecyclerView(rv_reportes_list)
    }

    // Evento clic cuando damos clic en un elemento del Recyclerview
    private fun onItemClickListener(Estud: reporte) {
        Toast.makeText(this, "Clicked item" + Estud.numreport, Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        retrieveReportes()
    }

    private fun retrieveReportes() {
        val reportetex = getReportes()
        viewAdapter.setTask(reportetex!!)
    }

    fun getReportes(): MutableList<reporte>{
        var reportes:MutableList<reporte> = ArrayList()
        val admin = adminBD(this)

        //                                          0       1       2      3
        val tupla = admin.Consulta("SELECT numReport,empleado,calidad,intensidad,cxq,velocidad FROM reportes ORDER BY numReport")
        while (tupla!!.moveToNext()) {
            val numreport = tupla.getInt(0)
            val emple = tupla.getString(1)
            val cali = tupla.getString(2)
            val intensi = tupla.getInt(3)
            val cxq = tupla.getInt(4)
            val velo = tupla.getInt(5)


            reportes.add(reporte(numreport,emple,cali,intensi,cxq,velo))
        }
        tupla.close()
        admin.close()
        return reportes

    }
}
