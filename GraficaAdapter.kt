package com.example.proyect

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.celda_prototipo_grafica.view.*

//-------------------------------------------------------------------------------------------
//    Alejandro Guzmán Zazueta                        Agosto 2019
//    Adaptador del RecyclerView
//    Un adapter es la clase que hace de puente entre la vista (el recyclerview) y los datos
//    La clase RecyclerAdapter se encargará de recorrer la lista de estudiantes que le
//    pasaremos más tarde, y llamando a otra clase interna que rellenará los campos.
//--------------------------------------------------------------------------------------------
class GraficaAdapter(private var mListaGraficas:List<grafica>,
                    private val mContext: Context, private val clickListener: (grafica) -> Unit)
    : RecyclerView.Adapter<GraficaAdapter.GraficaViewHolder>() {


    /**
     * onCreateViewHolder() que como su nombre indica lo que hará será devolvernos
     * un objeto ViewHolder al cual le pasamos la celda prototió que hemos creado.
     *
     * @return Un nuevo EstudianteViewHolder que contiene la vista para cada estudiante
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GraficaViewHolder {
        val layoutInflater = LayoutInflater.from(mContext)
        return GraficaViewHolder(layoutInflater.inflate(R.layout.celda_prototipo_grafica, parent, false))
    }

    /**
     * La clase RecyclerView. onBindViewHolder() se encarga de coger cada una de las
     * posiciones de la lista de estudiantes y pasarlas a la clase ViewHolder(
     *
     * @param holder   Vincular los datos del cursor al ViewHolder
     * @param position La posición de los datos en la lista
     */
    override fun onBindViewHolder(holder: GraficaViewHolder, position: Int) {
        holder.bind(mListaGraficas[position], mContext, clickListener)
    }

    /**
     * El método getItemCount() nos devuelve el tamaño de la lista, que lo necesita
     * el RecyclerView.
     */
    override fun getItemCount(): Int = mListaGraficas.size

    /**
     * Cuando los datos cambian, este metodo actualiza la lista de estudiantes
     * y notifica al adaptador a usar estos nuevos valores
     */
    fun setTask(graficas: List<grafica>){
        mListaGraficas = graficas
        notifyDataSetChanged()
    }

    fun getTasks(): List<grafica> = mListaGraficas

    /**
     * Clase interna para crear ViewHolders
     */
    class GraficaViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind (estud:grafica, context: Context, clickListener: (grafica) -> Unit){
            //Asigna los valores a los elementos del la celda_prototipo_estudiante
            itemView.txtidDecibel.text = estud.id.toString()
            itemView.txtNomAntena.text = estud.nom.toString()
            itemView.txtValorGrafic.text = estud.db.toString()

            itemView.setOnClickListener{ clickListener(estud)}
        }
    }

}

