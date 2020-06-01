package com.example.proyect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main2_recycler_comunidades.*

class Main2ActivityRecyclerComunidades : AppCompatActivity() {
    private lateinit var viewAdapter: ComunidadAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    val comunidadesList: List<comunidad> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2_recycler_comunidades)

        viewManager = LinearLayoutManager(this)
        viewAdapter = ComunidadAdapter(comunidadesList,this,{estud: comunidad -> onItemClickListener(estud) })

        rv_comunidades_list.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
            addItemDecoration(DividerItemDecoration(this@Main2ActivityRecyclerComunidades,
                DividerItemDecoration.VERTICAL))
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
                if (admin.Ejecuta("DELETE FROM comunidades WHERE clave=" + estud[position].clav) == 1){
                    retrieveComunidades()
                }
            }
        }).attachToRecyclerView(rv_comunidades_list)
    }

    // Evento clic cuando damos clic en un elemento del Recyclerview
    private fun onItemClickListener(Estud: comunidad) {
        Toast.makeText(this, "Clicked item" + Estud.nomcomu, Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        retrieveComunidades()
    }

    private fun retrieveComunidades() {
        val comunidadtex = getComunidades()
        viewAdapter.setTask(comunidadtex!!)
    }

    fun getComunidades(): MutableList<comunidad>{
        var comunidades:MutableList<comunidad> = ArrayList()
        val admin = adminBD(this)

        //                                          0       1       2      3
        val tupla = admin.Consulta("SELECT clave,nomComu,guardian FROM comunidades ORDER BY nomComu")
        while (tupla!!.moveToNext()) {
            val clav = tupla.getString(0)
            val nomcomu = tupla.getString(1)
            val guardi = tupla.getString(2)


            comunidades.add(comunidad(clav,nomcomu,guardi))
        }
        tupla.close()
        admin.close()
        return comunidades

    }
}
