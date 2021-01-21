package com.example.practica3_prueba

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteException
import android.os.Build
import android.provider.Telephony
import android.telephony.SmsMessage
import android.widget.Toast

class SmsReceiver : BroadcastReceiver () {

    override fun onReceive(context: Context, intent: Intent) {

        val extras = intent.extras
        var plataforma = ""
        var tipo = ""
        var respondido = "N"

        if(extras != null){

            var sms = extras.get("pdus") as Array<Any>

            for(indice in sms.indices) {

                var formato = extras.getString("format")

                var smsMensaje = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    SmsMessage.createFromPdu(sms[indice] as ByteArray, formato)
                } else {
                    SmsMessage.createFromPdu(sms[indice] as ByteArray)
                }//if-else

                var celularOrigen = smsMensaje.originatingAddress
                var contenidoSMS = smsMensaje.messageBody.toString()

                try {

                    var base = BaseDatos(context,"contenido",null, 1)
                    var insertar = base.writableDatabase
                    var SQL = "INSERT INTO entrantes (Celular, Plataforma, Respondido) VALUES ('${celularOrigen}','${contenidoSMS}','${respondido}')"

                    insertar.execSQL(SQL)
                    base.close()

                } catch (e: SQLiteException){
                    Toast.makeText(context, e.message.toString(), Toast.LENGTH_LONG).show()
                }//try-catch

            }//for

        }//if

    }//onReceive

}//class