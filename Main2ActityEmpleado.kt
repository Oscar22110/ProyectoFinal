package com.example.proyect

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_main2_empleado.*
import kotlinx.android.synthetic.main.content_main.*

class Main2ActivityEmpleado : AppCompatActivity() {

    val menu = arrayOf("reportes","grafica")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2_empleado)
        setSupportActionBar(toolbar)

        circle_menu.setMainMenu(Color.parseColor("#fff176"),R.drawable.configuracion,R.drawable.cerrar)
            .addSubMenu(Color.parseColor("#4e342e"),R.drawable.reportes)
            .addSubMenu(Color.parseColor("#ffeb3b"),R.drawable.grafica)
            .setOnMenuSelectedListener { index: Int ->
                Toast.makeText(this, menu[index], Toast.LENGTH_SHORT).show();
                when (menu[index]) {
                    "reportes" -> {
                        val intent = Intent(this, Main2Reportes::class.java)
                        startActivity(intent)
                    }
                    "grafica"->{
                        val intent = Intent(this, Main2ActivityEmpleGrafic::class.java)
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
