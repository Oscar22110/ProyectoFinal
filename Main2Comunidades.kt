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

import kotlinx.android.synthetic.main.activity_main2_comunidades.*
import kotlinx.android.synthetic.main.content_main2_comunidades.*
import org.json.JSONObject

class Main2Comunidades : AppCompatActivity() {
    val IP = "http://192.168.56.1" //direccion ip del servidor web que almacena los servicios web
    var bandera: Boolean = false
    var clave = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2_comunidades)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }


    fun getAllComunidades(v: View) {
        val wsURL = IP + "/WSComunidades/getComunidades.php"
        val admin = adminBD(this)
        admin.Ejecuta("DELETE FROM comunidades")
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, wsURL, null,
            Response.Listener { response ->
                val succ = response["success"]
                val msg = response["message"]
                val sensadoJson = response.getJSONArray("comunidades")
                for (i in 0 until sensadoJson.length()) {
                    //los nombres del getstring son como los arroja el servicio web
                    val clave = sensadoJson.getJSONObject(i).getString("clave")
                    val nomcomu = sensadoJson.getJSONObject(i).getString("nomComu")
                    val guardi = sensadoJson.getJSONObject(i).getString("guardian")
                    val sentencia =
                        "Insert into comunidades(clave,nomComu,guardian) values ('${clave}','${nomcomu}','${guardi}')"
                    val res = admin.Ejecuta(sentencia)
                }
                Toast.makeText(this, "Comunidad Agregada", Toast.LENGTH_SHORT).show();
            },
            Response.ErrorListener { error ->
                Toast.makeText(
                    this,
                    "Error getAllComunidades:" + error.message.toString(),
                    Toast.LENGTH_SHORT
                ).show();
                Log.d("García", error.message.toString())
            }
        )
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    //Rutina para mandar ejecutar un web service de tipo INSERT,UPdate o DElete
    fun sendRequest(wsURL: String, jsonEnt: JSONObject) {
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, wsURL, jsonEnt,
            Response.Listener { response ->
                val succ = response["success"]
                val msg = response["message"]
                if (succ == 200) {
                    txtClaveComu.setText("")
                    txtNomComu.setText("")
                    txtCorrUsuario.setText("")
                    txtClaveComu.requestFocus()
                    Toast.makeText(
                        this,
                        "Success:${succ} Message:${msg} Se Inserto la Comunidad en el Servidor Web",
                        Toast.LENGTH_SHORT
                    ).show();
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

    fun btnAgregarComunidad(v: View) {
        if (txtClaveComu.text.isEmpty() || txtNomComu.text.isEmpty() || txtCorrUsuario.text.isEmpty()) {
            txtClaveComu.setError("Falta información en los datos")
            txtClaveComu.requestFocus()
        } else {
            val clave = txtClaveComu.text.toString()
            val nom = txtNomComu.text.toString()
            val guardian = txtCorrUsuario.text.toString()
            var jsonEntrada = JSONObject()
            jsonEntrada.put("clave", clave)
            jsonEntrada.put("nomComu", nom)
            jsonEntrada.put("guardian", guardian)
            sendRequest(IP + "/WSComunidades/insertComunidad.php", jsonEntrada)
        }
    }

    fun btnConsultarComunidad(v: View) {
        if (txtClaveComu.text.toString().isEmpty()) {
            txtClaveComu.setError("Falta ingresar clave del producto")
            txtClaveComu.requestFocus()
        } else {
            val wsURL = IP + "/WSComunidades/getComunidad.php"
            val clave = txtClaveComu.text.toString()
            var jsonEntrada = JSONObject()
            jsonEntrada.put("clave",clave)
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, wsURL, jsonEntrada,
                Response.Listener { response ->
                    val succ = response["success"]
                    val msg = response["message"]
                    if (succ == 200) {
                        val sensadoJson = response.getJSONArray("comunidades")
                        //Los nombres del getString son como los arroja el servicio web val idprod = sensadoJson.getJSONObject(i).getString("idProd")
                        val clave = sensadoJson.getJSONObject(0).getString("clave")
                        val nomcomu = sensadoJson.getJSONObject(0).getString("nomComu")
                        val guardi = sensadoJson.getJSONObject(0).getString("guardian")


                        txtClaveComu.setText(clave)
                        txtNomComu.setText(nomcomu)
                        txtCorrUsuario.setText(guardi)

                    }
                    Toast.makeText(this, "Comunidad Cargada ", Toast.LENGTH_LONG).show();
                },
                Response.ErrorListener { error ->
                    Toast.makeText(
                        this,
                        "Error getALLProductos:  " + error.message.toString(),
                        Toast.LENGTH_SHORT).show();
                    Log.d("García", error.message.toString())
                }
            )
            VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
        }
    }

    fun btnActualizarComunidad(v: View) {
        if (txtClaveComu.text.toString().isEmpty() || txtNomComu.text.toString().isEmpty() ||
            txtCorrUsuario.text.toString().isEmpty()) {
            txtClaveComu.setError("Falta información por agregar")
            txtClaveComu.requestFocus()
        } else {
            val clave = txtClaveComu.text.toString()
            val nom = txtNomComu.text.toString()
            val guardi = txtCorrUsuario.text.toString()
            var jsonEntrada = JSONObject()
            jsonEntrada.put("clave", clave)
            jsonEntrada.put("nomComu", nom)
            jsonEntrada.put("guardian", guardi)
            sendRequest(IP + "/WSComunidades/updateComunidad.php", jsonEntrada)
        }
    }

    fun Limpiar(v: View){
        txtClaveComu.setText("")
        txtNomComu.setText("")
        txtCorrUsuario.setText("")
    }

    fun btnBorrarComunidad(v: View){
        if (txtClaveComu.text.toString().isEmpty()){
            txtClaveComu.setError("Falta información en la clave")
            txtClaveComu.requestFocus()
        }else{
            val clave = txtClaveComu.text.toString()
            var jsonEntrada = JSONObject()
            jsonEntrada.put("clave",clave)
            sendRequest(IP + "/WSComunidades/deleteComunidad.php",jsonEntrada)
        }
    }

    fun btnLista(v: View){
        var actividad = Intent(this,Main2ActivityRecyclerComunidades::class.java)
        startActivity(actividad)
    }

}

