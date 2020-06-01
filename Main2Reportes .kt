package com.example.proyect

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest

import kotlinx.android.synthetic.main.activity_main2_reportes.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_main2_antenas.*
import kotlinx.android.synthetic.main.content_main2_comunidades.*
import kotlinx.android.synthetic.main.content_main2_reportes.*
import org.json.JSONObject

class Main2Reportes : AppCompatActivity() {
    val IP = "http://192.168.56.1" //direccion ip del servidor web que almacena los servicios web
    var bandera: Boolean = false
    var clave = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2_reportes)
        setSupportActionBar(toolbar)


        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    fun getAllReportes(v: View) {
        val wsURL = IP + "/WSReportes/getReportes.php"
        val admin = adminBD(this)
        admin.Ejecuta("DELETE FROM reportes")
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, wsURL, null,
            Response.Listener { response ->
                val succ = response["success"]
                val msg = response["message"]
                val sensadoJson = response.getJSONArray("reportes")
                for (i in 0 until sensadoJson.length()) {
                    //los nombres del getstring son como los arroja el servicio web
                    val numreport = sensadoJson.getJSONObject(i).getString("numReport")
                    val emple = sensadoJson.getJSONObject(i).getString("empleado")
                    val cali = sensadoJson.getJSONObject(i).getString("calidad")
                    val inten = sensadoJson.getJSONObject(i).getString("intensidad")
                    val cxq = sensadoJson.getJSONObject(i).getString("cxq")
                    val velo = sensadoJson.getJSONObject(i).getString("velocidad")
                    val sentencia =
                        "Insert into reportes(numReport,empleado,calidad,intensidad,cxq,velocidad) values " +
                                "(${numreport},'${emple}','${cali}',${inten},${cxq},${velo})"
                    val res = admin.Ejecuta(sentencia)
                }
                Toast.makeText(this, "Reporte Agregado", Toast.LENGTH_SHORT).show();
            },
            Response.ErrorListener { error -> Toast.makeText(this, "Error getAllReportes:" + error.message.toString(), Toast.LENGTH_SHORT).show();
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
                    txtNumReport.setText("0")
                    txtEmpleReport.setText("")
                    txtCalidadReport.setText("")
                    txtIntensiReport.setText("0")
                    txtCxqReport.setText("0")
                    txtVeloReport.setText("0")
                    txtNumReport.requestFocus()
                    Toast.makeText(
                        this,
                        "Success:${succ} Message:${msg} Se Inserto el Reporte en el Servidor Web",
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

    fun btnAgregarReporte(v: View){
        if (txtNumReport.text.isEmpty() || txtEmpleReport.text.isEmpty() || txtCalidadReport.text.isEmpty() ||
            txtIntensiReport.text.isEmpty() || txtCxqReport.text.isEmpty() || txtVeloReport.text.isEmpty()) {
            txtNumReport.setError("Falta información en los datos")
            txtNumReport.requestFocus()
        } else {
            val num = txtNumReport.text.toString()
            val emple = txtEmpleReport.text.toString()
            val cali = txtCalidadReport.text.toString()
            val inten = txtIntensiReport.text.toString()
            val cxq = txtCxqReport.text.toString()
            val velo = txtVeloReport.text.toString()
            var jsonEntrada = JSONObject()
            jsonEntrada.put("numReport", num)
            jsonEntrada.put("empleado", emple)
            jsonEntrada.put("calidad", cali)
            jsonEntrada.put("intensidad",inten)
            jsonEntrada.put("cxq",cxq)
            jsonEntrada.put("velocidad",velo)
            sendRequest(IP + "/WSReportes/insertReporte.php", jsonEntrada)
        }

    }

    fun btnConsultarReporte(v: View){
        if (txtNumReport.text.toString().isEmpty()) {
            txtNumReport.setError("Falta ingresar clave del producto")
            txtNumReport.requestFocus()
        } else {
            val wsURL = IP + "/WSReportes/getReporte.php"
            val numreport = txtNumReport.text.toString()
            var jsonEntrada = JSONObject()
            jsonEntrada.put("numReport",numreport)
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, wsURL, jsonEntrada,
                Response.Listener { response ->
                    val succ = response["success"]
                    val msg = response["message"]
                    if (succ == 200) {
                        val sensadoJson = response.getJSONArray("reportes")
                        //Los nombres del getString son como los arroja el servicio web val idprod = sensadoJson.getJSONObject(i).getString("idProd")
                        val numreport = sensadoJson.getJSONObject(0).getString("numReport")
                        val emple = sensadoJson.getJSONObject(0).getString("empleado")
                        val cali = sensadoJson.getJSONObject(0).getString("calidad")
                        val inten = sensadoJson.getJSONObject(0).getString("intensidad")
                        val cxq = sensadoJson.getJSONObject(0).getString("cxq")
                        val velo = sensadoJson.getJSONObject(0).getString("velocidad")

                        txtNumReport.setText(numreport)
                        txtEmpleReport.setText(emple)
                        txtCalidadReport.setText(cali)
                        txtIntensiReport.setText(inten)
                        txtCxqReport.setText(cxq)
                        txtVeloReport.setText(velo)
                    }
                    Toast.makeText(this, "Reporte Cargada ", Toast.LENGTH_LONG).show();
                },
                Response.ErrorListener { error ->
                    Toast.makeText(
                        this,
                        "Error getALLReportes:  " + error.message.toString(),
                        Toast.LENGTH_SHORT).show();
                    Log.d("García", error.message.toString())
                }
            )
            VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
        }
    }

    fun btnActualizarReporte(v: View) {
        if (txtNumReport.text.toString().isEmpty() || txtEmpleReport.text.toString().isEmpty()
            || txtCalidadReport.text.toString().isEmpty() || txtIntensiReport.text.isEmpty()
            || txtCxqReport.text.isEmpty() || txtVeloReport.text.isEmpty()) {
            txtNumReport.setError("Falta información por agregar")
            txtNumReport.requestFocus()
        } else {
            val numreport = txtNumReport.text.toString()
            val emple = txtEmpleReport.text.toString()
            val cali = txtCalidadReport.text.toString()
            val inten = txtIntensiReport.text.toString()
            val cxq = txtCxqReport.text.toString()
            val velo = txtVeloReport.text.toString()
            var jsonEntrada = JSONObject()
            jsonEntrada.put("numReport", numreport)
            jsonEntrada.put("empleado", emple)
            jsonEntrada.put("calidad", cali)
            jsonEntrada.put("intensidad",inten)
            jsonEntrada.put("cxq",cxq)
            jsonEntrada.put("velocidad",velo)
            sendRequest(IP + "/WSReportes/updateReporte.php", jsonEntrada)
        }
    }
    fun LimpiarAnt(v: View){
        txtNumReport.setText("0")
        txtEmpleReport.setText("")
        txtCalidadReport.setText("")
        txtIntensiReport.setText("0")
        txtCxqReport.setText("0")
        txtVeloReport.setText("0")
    }

    fun btnBorrarReporte(v: View){
        if (txtNumReport.text.toString().isEmpty()){
            txtNumReport.setError("Falta información en la clave")
            txtNumReport.requestFocus()
        }else{
            val numreport = txtNumReport.text.toString()
            var jsonEntrada = JSONObject()
            jsonEntrada.put("numReport",numreport)
            sendRequest(IP + "/WSReportes/deleteReporte.php",jsonEntrada)
        }
    }

    fun btnListaReport(v: View){
        var actividad = Intent(this,Main2ActivityRecyclerReportes::class.java)
        startActivity(actividad)
    }


}
