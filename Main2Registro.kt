package com.example.proyect

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest

import kotlinx.android.synthetic.main.activity_main2_registro.*
import kotlinx.android.synthetic.main.content_main2_registro.*
import org.json.JSONObject

class Main2Registro : AppCompatActivity() {
    val IP = "http://192.168.56.1" //direccion ip del servidor web que almacena los servicios web
    var bandera: Boolean = false
    var clave = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2_registro)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->

            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    //Rutina para mandar ejecutar un web service de tipo INSERT,UPdate o DElete
    fun sendRequest(wsURL: String, jsonEnt: JSONObject) {
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, wsURL, jsonEnt,
            Response.Listener { response ->
                val succ = response["success"]
                val msg = response["message"]
                if (succ == 200) {
                    txtcorr.setText("")
                    txtnom.setText("")
                    txtnom.requestFocus()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "${error.message}", Toast.LENGTH_SHORT).show();
                Log.d("ERROR", "${error.message}");
                Toast.makeText(this, "Error de capar 8 checa el URL", Toast.LENGTH_SHORT).show();
            }
        )
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }


    fun btnRegistrar(v:View){
        if (txtcorr.text.isEmpty() || txtnom.text.isEmpty()) {
            txtcorr.setError("Falta información en los datos")
            txtcorr.requestFocus()
        } else {
            val corr = txtcorr.text.toString()
            val nom = txtnom.text.toString()

            var jsonEntrada = JSONObject()
            jsonEntrada.put("correoUsr", corr)
            jsonEntrada.put("nomusr", nom)

            sendRequest(IP + "/WSUsuarios/insertUsuario.php", jsonEntrada)
            Toast.makeText(this, "Usuario guardado en el servidor web", Toast.LENGTH_SHORT).show();
            val acti: Intent = Intent(this,MainActivity::class.java)
            acti.putExtra(MainActivity.EXTRA_CORR,corr)
            acti.putExtra(MainActivity.EXTRA_NOM,nom)
            startActivity(acti)
        }


    }

}
