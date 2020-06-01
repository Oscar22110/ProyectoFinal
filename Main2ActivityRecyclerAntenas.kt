package com.example.proyect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main2_recycler_antenas.*
import kotlinx.android.synthetic.main.activity_main2_recycler_comunidades.*

class Main2ActivityRecyclerAntenas : AppCompatActivity() {

    private lateinit var viewAdapter: AntenaAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    val antenasList: List<antena> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2_recycler_antenas)

        viewManager = LinearLayoutManager(this)
        viewAdapter = AntenaAdapter(antenasList,this,{estud: antena -> onItemClickListener(estud) })

        rv_antenas_list.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
            addItemDecoration(
                DividerItemDecoration(this@Main2ActivityRecyclerAntenas,
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
                if (admin.Ejecuta("DELETE FROM antenas WHERE idantena=" + estud[position].idan) == 1){
                    retrieveAntenas()
                }
            }
        }).attachToRecyclerView(rv_antenas_list)
    }

    // Evento clic cuando damos clic en un elemento del Recyclerview
    private fun onItemClickListener(Estud: antena) {
        Toast.makeText(this, "Clicked item" + Estud.nomant, Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        retrieveAntenas()
    }

    private fun retrieveAntenas() {
        val antenatex = getAntenas()
        viewAdapter.setTask(antenatex!!)
    }

    fun getAntenas(): MutableList<antena>{
        var antenas:MutableList<antena> = ArrayList()
        val admin = adminBD(this)

        //                                          0       1       2      3
        val tupla = admin.Consulta("SELECT idantena,nomAnt,modelo,tipoAnt FROM antenas ORDER BY idantena")
        while (tupla!!.moveToNext()) {
            val idan = tupla.getInt(0)
            val nomant = tupla.getString(1)
            val model = tupla.getString(2)
            val tipo = tupla.getString(3)


            antenas.add(antena(idan,nomant,model,tipo))
        }
        tupla.close()
        admin.close()
        return antenas

    }
}
