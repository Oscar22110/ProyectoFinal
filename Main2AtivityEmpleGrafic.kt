package com.example.proyect

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest

import kotlinx.android.synthetic.main.content_main2_activity_emple_grafic.*
import kotlinx.android.synthetic.main.content_main2_comunidades.*
import org.json.JSONObject

class Main2ActivityEmpleGrafic : AppCompatActivity() {
    val IP = "http://192.168.56.1" //direccion ip del servidor web que almacena los servicios web
    var bandera: Boolean = false
    var clave = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2_emple_grafic)
    }

    fun graficaClic(v: View) {

        val intGraf = Intent(this, Main2ActivityGRAFICA::class.java)
        startActivity(intGraf)
    }
    fun getAllComunidades(v: View) {
        val wsURL = IP + "/WSGrafica/getGraficas.php"
        val admin = adminBD(this)
        admin.Ejecuta("DELETE FROM decibeles")
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, wsURL, null,
            Response.Listener { response ->
                val succ = response["success"]
                val msg = response["message"]
                val sensadoJson = response.getJSONArray("decibeles")
                for (i in 0 until sensadoJson.length()) {
                    //los nombres del getstring son como los arroja el servicio web
                    val id = sensadoJson.getJSONObject(i).getString("idDbi")
                    val nom = sensadoJson.getJSONObject(i).getString("antenaGrafic")
                    val dbi = sensadoJson.getJSONObject(i).getString("dbi")
                    val sentencia =
                        "Insert into decibeles(idDbi,antenaGrafic,dbi) values ('${id}','${nom}','${dbi}')"
                    val res = admin.Ejecuta(sentencia)
                }
                Toast.makeText(this, "Decibeles Agregados", Toast.LENGTH_SHORT).show();
            },
            Response.ErrorListener { error ->
                Toast.makeText(
                    this,
                    "Error getAllGrafica:" + error.message.toString(),
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
                    txtidDecibel.setText("")
                    txtNomAntena.setText("")
                    txtValorGrafic.setText("")
                    txtidDecibel.requestFocus()
                    Toast.makeText(
                        this,
                        "Success:${succ} Message:${msg} Se Inserto grafica  en el Servidor Web",
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

    fun btnAgregarDato(v: View) {
        if (txtidDecibel.text.isEmpty() || txtNomAntena.text.isEmpty() || txtValorGrafic.text.isEmpty()) {
            txtidDecibel.setError("Falta información de ingresar")
            txtidDecibel.requestFocus()
        } else {
            val iddbi = txtidDecibel.text.toString()
            val nom = txtNomAntena.text.toString()
            val dbi = txtValorGrafic.text.toString()
            var jsonEntrada = JSONObject()
            jsonEntrada.put("idDbi", iddbi)
            jsonEntrada.put("antenaGrafic", nom)
            jsonEntrada.put("dbi", dbi)
            sendRequest(IP + "/WSGrafica/insertGrafica.php", jsonEntrada)
        }
    }

    fun btnConsultarGrafic(v: View) {
        if (txtidDecibel.text.toString().isEmpty()) {
            txtidDecibel.setError("Falta ingresar clave del producto")
            txtidDecibel.requestFocus()
        } else {
            val wsURL = IP + "/WSGrafica/getGrafica.php"
            val id = txtidDecibel.text.toString()
            var jsonEntrada = JSONObject()
            jsonEntrada.put("idDbi",id)
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, wsURL, jsonEntrada,
                Response.Listener { response ->
                    val succ = response["success"]
                    val msg = response["message"]
                    if (succ == 200) {
                        val sensadoJson = response.getJSONArray("decibeles")
                        //Los nombres del getString son como los arroja el servicio web val idprod = sensadoJson.getJSONObject(i).getString("idProd")
                        val id = sensadoJson.getJSONObject(0).getString("idDbi")
                        val nom = sensadoJson.getJSONObject(0).getString("antenaGrafic")
                        val dbi = sensadoJson.getJSONObject(0).getString("dbi")


                        txtidDecibel.setText(id)
                        txtNomAntena.setText(nom)
                        txtValorGrafic.setText(dbi)

                    }
                    Toast.makeText(this, "Datos Cargados ", Toast.LENGTH_LONG).show();
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

    fun btnActualizarGrafica(v: View) {
        if (txtidDecibel.text.toString().isEmpty() || txtNomAntena.text.toString().isEmpty() ||
            txtValorGrafic.text.toString().isEmpty()) {
            txtidDecibel.setError("Falta información por agregar")
            txtidDecibel.requestFocus()
        } else {
            val id = txtidDecibel.text.toString()
            val nom = txtNomAntena.text.toString()
            val dbi = txtValorGrafic.text.toString()
            var jsonEntrada = JSONObject()
            jsonEntrada.put("idDbi", id)
            jsonEntrada.put("antenaGrafic", nom)
            jsonEntrada.put("dbi", dbi)
            sendRequest(IP + "/WSGrafica/updateGrafica.php", jsonEntrada)
        }
    }

    fun LimpiarGrafic(v: View){
        txtidDecibel.setText("")
        txtNomAntena.setText("")
        txtValorGrafic.setText("")
    }

    fun btnBorrarGrafic(v: View){
        if (txtidDecibel.text.toString().isEmpty()){
            txtidDecibel.setError("Falta información en la clave")
            txtidDecibel.requestFocus()
        }else{
            val id = txtidDecibel.text.toString()
            var jsonEntrada = JSONObject()
            jsonEntrada.put("idDbi",id)
            sendRequest(IP + "/WSGrafica/deleteGrafica.php",jsonEntrada)
        }
    }

    fun btnListaGrafic(v: View){
        val inten = Intent(this,Main2ActivityRecyclerGrafica::class.java)
        startActivity(inten)
    }

}

