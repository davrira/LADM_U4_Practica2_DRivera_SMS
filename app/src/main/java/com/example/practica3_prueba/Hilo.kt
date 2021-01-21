package com.example.practica3_prueba

class Hilo(p:MainActivity) : Thread() {

    var iniciar = false
    var puntero = p

    override fun run() {
        super.run()

        iniciar = true
        while (iniciar){
            sleep(1000)
            puntero.runOnUiThread {
                puntero.verificarSMS()
            }
        }

    }//run

    fun terminar(){
        iniciar = false
    }

}//class