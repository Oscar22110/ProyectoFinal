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

import kotlinx.android.synthetic.main.activity_main2_antenas.*
import kotlinx.android.synthetic.main.content_main2_antenas.*
import kotlinx.android.synthetic.main.content_main2_comunidades.*
import org.json.JSONObject

class Main2Antenas : AppCompatActivity() {
    val IP = "http://192.168.56.1" //direccion ip del servidor web que almacena los servicios web
    var bandera: Boolean = false
    var clave = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2_antenas)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }
    fun getAllAntenas(v: View) {
        val wsURL = IP + "/WSAntenas/getAntenas.php"
        val admin = adminBD(this)
        admin.Ejecuta("DELETE FROM antenas")
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, wsURL, null,
            Response.Listener { response ->
                val succ = response["success"]
                val msg = response["message"]
                val sensadoJson = response.getJSONArray("antenas")
                for (i in 0 until sensadoJson.length()) {
                    //los nombres del getstring son como los arroja el servicio web
                    val idantena = sensadoJson.getJSONObject(i).getString("idantena")
                    val nomant = sensadoJson.getJSONObject(i).getString("nomAnt")
                    val model = sensadoJson.getJSONObject(i).getString("modelo")
                    val tipoant = sensadoJson.getJSONObject(i).getString("tipoAnt")
                    val sentencia =
                        "Insert into antenas(idantena,nomAnt,modelo,tipoAnt) values (${idantena},'${nomant}','${model}','${tipoant}')"
                    val res = admin.Ejecuta(sentencia)
                }
                Toast.makeText(this, "Antena Agregada", Toast.LENGTH_SHORT).show();
            },
            Response.ErrorListener { error ->
                Toast.makeText(
                    this,
                    "Error getAllAntenas:" + error.message.toString(),
                    Toast.LENGTH_SHORT
                ).show();
                Log.d("García", error.message.toString())
            }
        )
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    fun sendRequest(wsURL: String, jsonEnt: JSONObject) {
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, wsURL, jsonEnt,
            Response.Listener { response ->
                val succ = response["success"]
                val msg = response["message"]
                if (succ == 200) {
                    txtidantena.setText("")
                    txtnomAnt.setText("")
                    txtmodeloAnt.setText("")
                    txttipoAntt.setText("")
                    txtidantena.requestFocus()
                    Toast.makeText(
                        this,
                        "Success:${succ} Message:${msg} Se Inserto la Antena en el Servidor Web",
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

    fun btnAgregarAntena(v: View){
        if (txtidantena.text.isEmpty() || txtnomAnt.text.isEmpty() || txtmodeloAnt.text.isEmpty() || txttipoAntt.text.isEmpty()) {
            txtidantena.setError("Falta información en los datos")
            txtidantena.requestFocus()
        } else {
            val idant = txtidantena.text.toString()
            val nomant = txtnomAnt.text.toString()
            val model = txtmodeloAnt.text.toString()
            val tipoant = txttipoAntt.text.toString()
            var jsonEntrada = JSONObject()
            jsonEntrada.put("idantena", idant)
            jsonEntrada.put("nomAnt", nomant)
            jsonEntrada.put("modelo", model)
            jsonEntrada.put("tipoAnt",tipoant)
            sendRequest(IP + "/WSAntenas/insertAntena.php", jsonEntrada)
        }
    }

    fun btnConsultarAntena(v: View){
        if (txtidantena.text.toString().isEmpty()) {
            txtidantena.setError("Falta ingresar clave del producto")
            txtidantena.requestFocus()
        } else {
            val wsURL = IP + "/WSAntenas/getAntena.php"
            val idantena = txtidantena.text.toString()
            var jsonEntrada = JSONObject()
            jsonEntrada.put("idantena",idantena)
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, wsURL, jsonEntrada,
                Response.Listener { response ->
                    val succ = response["success"]
                    val msg = response["message"]
                    if (succ == 200) {
                        val sensadoJson = response.getJSONArray("antenas")
                        //Los nombres del getString son como los arroja el servicio web val idprod = sensadoJson.getJSONObject(i).getString("idProd")
                        val idantena = sensadoJson.getJSONObject(0).getString("idantena")
                        val nomant = sensadoJson.getJSONObject(0).getString("nomAnt")
                        val model = sensadoJson.getJSONObject(0).getString("modelo")
                        val tipoant = sensadoJson.getJSONObject(0).getString("tipoAnt")

                        txtidantena.setText(idantena)
                        txtnomAnt.setText(nomant)
                        txtmodeloAnt.setText(model)
                        txttipoAntt.setText(tipoant)
                    }
                    Toast.makeText(this, "Antena Cargada ", Toast.LENGTH_LONG).show();
                },
                Response.ErrorListener { error ->
                    Toast.makeText(this, "Error getALLProductos:  " + error.message.toString(), Toast.LENGTH_SHORT).show();
                    Log.d("García", error.message.toString())
                }
            )
            VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
        }
    }

    fun btnActualizarAntena(v: View) {
        if (txtidantena.text.toString().isEmpty() || txtnomAnt.text.toString().isEmpty() ||
            txtmodeloAnt.text.toString().isEmpty() || txttipoAntt.text.isEmpty()) {
            txtidantena.setError("Falta información por agregar")
            txtidantena.requestFocus()
        } else {
            val idantena = txtidantena.text.toString()
            val nomant = txtnomAnt.text.toString()
            val model = txtmodeloAnt.text.toString()
            val tipoant = txttipoAntt.text.toString()
            var jsonEntrada = JSONObject()
            jsonEntrada.put("idantena", idantena)
            jsonEntrada.put("nomAnt", nomant)
            jsonEntrada.put("modelo", model)
            jsonEntrada.put("tipoAnt",tipoant)
            sendRequest(IP + "/WSAntenas/updateAntena.php", jsonEntrada)
        }
    }

    fun LimpiarAnt(v: View){
        txtidantena.setText("0")
        txtnomAnt.setText("")
        txtmodeloAnt.setText("")
        txttipoAntt.setText("")
    }

    fun btnBorrarAntena(v: View){
        if (txtidantena.text.toString().isEmpty()){
            txtidantena.setError("Falta información en la clave")
            txtidantena.requestFocus()
        }else{
            val idantena = txtidantena.text.toString()
            var jsonEntrada = JSONObject()
            jsonEntrada.put("idantena",idantena)
            sendRequest(IP + "/WSAntenas/deleteAntena.php",jsonEntrada)
        }
    }
    fun btnListaAnt(v: View){
        var actividad = Intent(this,Main2ActivityRecyclerAntenas::class.java)
        startActivity(actividad)
    }


}
