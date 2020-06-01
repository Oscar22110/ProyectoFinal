package com.example.proyect

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.print.PrintManager
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_main2_administrador.*
import kotlinx.android.synthetic.main.content_main.*

class Main2ActivityAdministrador : AppCompatActivity() {
    val menu = arrayOf("comunidades","antenas","reportes","pdf","grafica","usuario")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2_administrador)
        setSupportActionBar(toolbar)

        circle_menu.setMainMenu(Color.parseColor("#fff176"),R.drawable.configuracion,R.drawable.cerrar)
            .addSubMenu(Color.parseColor("#4e342e"),R.drawable.comunidades)
            .addSubMenu(Color.parseColor("#ffeb3b"),R.drawable.antenas)
            .addSubMenu(Color.parseColor("#4e342e"),R.drawable.reportes)
            .addSubMenu(Color.parseColor("#ffeb3b"),R.drawable.cerrar)
            .addSubMenu(Color.parseColor("#4e342e"),R.drawable.grafica)
            .addSubMenu(Color.parseColor("#ffeb3b"),R.drawable.usuario)
            .setOnMenuSelectedListener{
                    index: Int ->
                Toast.makeText(this, menu[index], Toast.LENGTH_SHORT).show();
                when(menu[index]){
                    "reportes"->{
                        val intent  = Intent(this,Main2ActivityRecyclerReportes::class.java)
                        startActivity(intent)
                    }
                    "antenas"->{
                        val intent = Intent(this,Main2Antenas::class.java)
                        startActivity(intent)
                    }
                    "comunidades"->{
                        val intent = Intent(this,Main2Comunidades::class.java)
                        startActivity(intent)
                    }
                    "grafica"->{
                        val intent = Intent(this,Main2ActivityGRAFICA::class.java)
                        startActivity(intent)
                    }
                    "usuario"->{
                        val intent = Intent(this,Main2ActivityUsuario::class.java)
                        startActivity(intent)
                    }
                }
            }

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

}
