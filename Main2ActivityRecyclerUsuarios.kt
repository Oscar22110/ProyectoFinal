package com.example.proyect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main2_recycler_usuarios2.*

class Main2ActivityRecyclerUsuarios : AppCompatActivity() {
    private lateinit var viewAdapter: UsuarioAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    val usuariosList: List<usuario> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2_recycler_usuarios2)

        viewManager = LinearLayoutManager(this)
        viewAdapter = UsuarioAdapter(usuariosList,this,{estud: usuario -> onItemClickListener(estud) })

        rv_usuarios_list.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
            addItemDecoration(
                DividerItemDecoration(this@Main2ActivityRecyclerUsuarios,
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
                if (admin.Ejecuta("DELETE FROM usuario WHERE correoUsr=" + estud[position].corr) == 1){
                    retrieveUsuarios()
                }
            }
        }).attachToRecyclerView(rv_usuarios_list)
    }

    // Evento clic cuando damos clic en un elemento del Recyclerview
    private fun onItemClickListener(Estud: usuario) {
        Toast.makeText(this, "Clicked item" + Estud.nom, Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        retrieveUsuarios()
    }

    private fun retrieveUsuarios() {
        val usuariotex = getUsuarios()
        viewAdapter.setTask(usuariotex!!)
    }

    fun getUsuarios(): MutableList<usuario>{
        var usuarios:MutableList<usuario> = ArrayList()
        val admin = adminBD(this)

        //                                          0       1       2      3
        val tupla = admin.Consulta("SELECT correoUsr,nomusr FROM usuario ORDER BY correoUsr")
        while (tupla!!.moveToNext()) {
            val corr = tupla.getString(0)
            val nom = tupla.getString(1)



            usuarios.add(usuario(corr,nom))
        }
        tupla.close()
        admin.close()
        return usuarios

    }
}
