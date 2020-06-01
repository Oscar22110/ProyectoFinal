package com.example.proyect

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.celda_prototipo_comunidades.view.*


//-------------------------------------------------------------------------------------------
//    Alejandro Guzmán Zazueta                        Agosto 2019
//    Adaptador del RecyclerView
//    Un adapter es la clase que hace de puente entre la vista (el recyclerview) y los datos
//    La clase RecyclerAdapter se encargará de recorrer la lista de estudiantes que le
//    pasaremos más tarde, y llamando a otra clase interna que rellenará los campos.
//--------------------------------------------------------------------------------------------
class ComunidadAdapter(private var mListaComunidades:List<comunidad>,
                        private val mContext: Context, private val clickListener: (comunidad) -> Unit)
    : RecyclerView.Adapter<ComunidadAdapter.ComunidadViewHolder>() {


    /**
     * onCreateViewHolder() que como su nombre indica lo que hará será devolvernos
     * un objeto ViewHolder al cual le pasamos la celda prototió que hemos creado.
     *
     * @return Un nuevo EstudianteViewHolder que contiene la vista para cada estudiante
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComunidadViewHolder {
        val layoutInflater = LayoutInflater.from(mContext)
        return ComunidadViewHolder(layoutInflater.inflate(R.layout.celda_prototipo_comunidades, parent, false))
    }

    /**
     * La clase RecyclerView. onBindViewHolder() se encarga de coger cada una de las
     * posiciones de la lista de estudiantes y pasarlas a la clase ViewHolder(
     *
     * @param holder   Vincular los datos del cursor al ViewHolder
     * @param position La posición de los datos en la lista
     */
    override fun onBindViewHolder(holder: ComunidadViewHolder, position: Int) {
        holder.bind(mListaComunidades[position], mContext, clickListener)
    }

    /**
     * El método getItemCount() nos devuelve el tamaño de la lista, que lo necesita
     * el RecyclerView.
     */
    override fun getItemCount(): Int = mListaComunidades.size

    /**
     * Cuando los datos cambian, este metodo actualiza la lista de estudiantes
     * y notifica al adaptador a usar estos nuevos valores
     */
    fun setTask(comunidades: List<comunidad>){
        mListaComunidades = comunidades
        notifyDataSetChanged()
    }

    fun getTasks(): List<comunidad> = mListaComunidades

    /**
     * Clase interna para crear ViewHolders
     */
    class ComunidadViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind (estud:comunidad, context: Context, clickListener: (comunidad) -> Unit){
            //Asigna los valores a los elementos del la celda_prototipo_estudiante
            itemView.txtNomComu.text = estud.nomcomu.toString()
            itemView.txtCorrUsuario.text = estud.guardi.toString()

            itemView.setOnClickListener{ clickListener(estud)}
        }
    }

}

