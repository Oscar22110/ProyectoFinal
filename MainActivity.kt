package com.example.proyect

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {
    var nomu: String = ""
    var corru: String = ""

    companion object{
        val EXTRA_CORR = "Correo"
        val EXTRA_NOM = "Nombre"
    }

    val menu = arrayOf("administrador","empleado")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        circle_menu.setMainMenu(Color.parseColor("#fff176"),R.drawable.configuracion,R.drawable.cerrar)
            .addSubMenu(Color.parseColor("#4e342e"),R.drawable.admin)
            .addSubMenu(Color.parseColor("#ffeb3b"),R.drawable.empleado)

            .setOnMenuSelectedListener{
                index: Int ->
                Toast.makeText(this, menu[index], Toast.LENGTH_SHORT).show();
                when(menu[index]){
                    "administrador"->{
                        val intent  = Intent(this,Main2ActivityAdministrador::class.java)
                        startActivity(intent)
                    }
                    "empleado"->{
                        val intent = Intent(this,Main2ActivityEmpleado::class.java)
                        startActivity(intent)
                    }

                }
            }

        val reg = intent
        if(reg != null && reg.hasExtra(EXTRA_CORR) && reg.hasExtra(EXTRA_NOM)){
            corru = reg.getStringExtra(EXTRA_CORR)
            nomu = reg.getStringExtra(EXTRA_NOM)
        }
        else {
            val admin = adminBD(this)
            val consulta = "Select correoUsr, nomusr from usuario"
            val registro = admin.Consulta(consulta)
            if (registro!!.moveToFirst()) {
                corru = registro.getString(0)
                nomu = registro.getString(1)
            } else {
                val acti: Intent = Intent(this, Main2Registro::class.java)
                startActivity(acti)
            }
        }

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
