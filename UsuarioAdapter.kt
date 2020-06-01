package com.example.proyect

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.celda_prototipo_usuarios.view.*

//-------------------------------------------------------------------------------------------
//    Alejandro Guzmán Zazueta                        Agosto 2019
//    Adaptador del RecyclerView
//    Un adapter es la clase que hace de puente entre la vista (el recyclerview) y los datos
//    La clase RecyclerAdapter se encargará de recorrer la lista de estudiantes que le
//    pasaremos más tarde, y llamando a otra clase interna que rellenará los campos.
//--------------------------------------------------------------------------------------------
class UsuarioAdapter(private var mListaUsuarios:List<usuario>,
                    private val mContext: Context, private val clickListener: (usuario) -> Unit)
    : RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder>() {


    /**
     * onCreateViewHolder() que como su nombre indica lo que hará será devolvernos
     * un objeto ViewHolder al cual le pasamos la celda prototió que hemos creado.
     *
     * @return Un nuevo EstudianteViewHolder que contiene la vista para cada estudiante
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val layoutInflater = LayoutInflater.from(mContext)
        return UsuarioViewHolder(layoutInflater.inflate(R.layout.celda_prototipo_usuarios, parent, false))
    }

    /**
     * La clase RecyclerView. onBindViewHolder() se encarga de coger cada una de las
     * posiciones de la lista de estudiantes y pasarlas a la clase ViewHolder(
     *
     * @param holder   Vincular los datos del cursor al ViewHolder
     * @param position La posición de los datos en la lista
     */
    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        holder.bind(mListaUsuarios[position], mContext, clickListener)
    }

    /**
     * El método getItemCount() nos devuelve el tamaño de la lista, que lo necesita
     * el RecyclerView.
     */
    override fun getItemCount(): Int = mListaUsuarios.size

    /**
     * Cuando los datos cambian, este metodo actualiza la lista de estudiantes
     * y notifica al adaptador a usar estos nuevos valores
     */
    fun setTask(usuarios: List<usuario>){
        mListaUsuarios = usuarios
        notifyDataSetChanged()
    }

    fun getTasks(): List<usuario> = mListaUsuarios

    /**
     * Clase interna para crear ViewHolders
     */
    class UsuarioViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind (estud:usuario, context: Context, clickListener: (usuario) -> Unit){
            //Asigna los valores a los elementos del la celda_prototipo_estudiante
            itemView.txtCorrUsuario.text = estud.corr.toString()
            itemView.txtNomUsuario.text = estud.nom.toString()


            itemView.setOnClickListener{ clickListener(estud)}
        }
    }

}

