package com.example.proyect

class reporte(numReport: Int, empleado: String, calidad: String, intensidad: Int, cxq: Int, velocidad: Int) {
    var numreport: Int = 0
    var emple: String = ""
    var cali: String = ""
    var intensi: Int = 0
    var cxq: Int = 0
    var velo: Int =0

    init {
        this.numreport = numReport
        this.emple = empleado
        this.cali = calidad
        this.intensi = intensidad
        this.cxq = cxq
        this.velo = velocidad
    }
}
