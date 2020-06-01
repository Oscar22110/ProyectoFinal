package com.example.proyect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main2_recycler_antenas.*
import kotlinx.android.synthetic.main.activity_main2_recycler_grafica.*

class Main2ActivityRecyclerGrafica : AppCompatActivity() {

    private lateinit var viewAdapter: GraficaAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    val graficasList: List<grafica> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2_recycler_grafica)

        viewManager = LinearLayoutManager(this)
        viewAdapter = GraficaAdapter(graficasList,this,{ estud: grafica -> onItemClickListener(estud) })

        rv_graficas_list.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
            addItemDecoration(
                DividerItemDecoration(this@Main2ActivityRecyclerGrafica,
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
                if (admin.Ejecuta("DELETE FROM decibeles WHERE idDbi=" + estud[position].id) == 1){
                    retrieveGraficas()
                }
            }
        }).attachToRecyclerView(rv_graficas_list)
    }

    // Evento clic cuando damos clic en un elemento del Recyclerview
    private fun onItemClickListener(Estud: grafica) {
        Toast.makeText(this, "Clicked item" + Estud.nom, Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        retrieveGraficas()
    }

    private fun retrieveGraficas() {
        val graficatex = getGraficas()
        viewAdapter.setTask(graficatex!!)
    }

    fun getGraficas(): MutableList<grafica>{
        var graficas:MutableList<grafica> = ArrayList()
        val admin = adminBD(this)

        //                                          0       1       2      3
        val tupla = admin.Consulta("SELECT idDbi,antenaGrafic,dbi FROM decibeles ORDER BY idDbi")
        while (tupla!!.moveToNext()) {
            val id = tupla.getInt(0)
            val nom = tupla.getString(1)
            val dbi = tupla.getInt(2)



            graficas.add(grafica(id,nom,dbi))
        }
        tupla.close()
        admin.close()
        return graficas

    }
}
