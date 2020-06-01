package com.example.proyect

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.celda_prototipo_antenas.view.*


//-------------------------------------------------------------------------------------------
//    Alejandro Guzmán Zazueta                        Agosto 2019
//    Adaptador del RecyclerView
//    Un adapter es la clase que hace de puente entre la vista (el recyclerview) y los datos
//    La clase RecyclerAdapter se encargará de recorrer la lista de estudiantes que le
//    pasaremos más tarde, y llamando a otra clase interna que rellenará los campos.
//--------------------------------------------------------------------------------------------
class AntenaAdapter(private var mListaAntenas:List<antena>,
                       private val mContext: Context, private val clickListener: (antena) -> Unit)
    : RecyclerView.Adapter<AntenaAdapter.AntenaViewHolder>() {


    /**
     * onCreateViewHolder() que como su nombre indica lo que hará será devolvernos
     * un objeto ViewHolder al cual le pasamos la celda prototió que hemos creado.
     *
     * @return Un nuevo EstudianteViewHolder que contiene la vista para cada estudiante
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AntenaViewHolder {
        val layoutInflater = LayoutInflater.from(mContext)
        return AntenaViewHolder(layoutInflater.inflate(R.layout.celda_prototipo_antenas, parent, false))
    }

    /**
     * La clase RecyclerView. onBindViewHolder() se encarga de coger cada una de las
     * posiciones de la lista de estudiantes y pasarlas a la clase ViewHolder(
     *
     * @param holder   Vincular los datos del cursor al ViewHolder
     * @param position La posición de los datos en la lista
     */
    override fun onBindViewHolder(holder: AntenaViewHolder, position: Int) {
        holder.bind(mListaAntenas[position], mContext, clickListener)
    }

    /**
     * El método getItemCount() nos devuelve el tamaño de la lista, que lo necesita
     * el RecyclerView.
     */
    override fun getItemCount(): Int = mListaAntenas.size

    /**
     * Cuando los datos cambian, este metodo actualiza la lista de estudiantes
     * y notifica al adaptador a usar estos nuevos valores
     */
    fun setTask(antenas: List<antena>){
        mListaAntenas = antenas
        notifyDataSetChanged()
    }

    fun getTasks(): List<antena> = mListaAntenas

    /**
     * Clase interna para crear ViewHolders
     */
    class AntenaViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind (estud:antena, context: Context, clickListener: (antena) -> Unit){
            //Asigna los valores a los elementos del la celda_prototipo_estudiante
            itemView.txtnomAnt.text = estud.nomant.toString()
            itemView.txtmodeloAnt.text = estud.model.toString()
            itemView.txttipoAntt.text = estud.tipo.toString()

            itemView.setOnClickListener{ clickListener(estud)}
        }
    }

}

