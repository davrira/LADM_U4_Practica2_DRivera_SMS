package com.example.practica3_prueba
//Unidad4_Practica3_AutoContestadoraSMS_DRivera
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.database.getStringOrNull
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var hilo : Hilo? = null
    val siPermisoEnvioSms = 1
    val siPermisoReceiver = 2
    val siPermisoLecturaSms = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        hilo = Hilo(this)
        hilo!!.start()

        //Permiso reciver
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECEIVE_SMS), siPermisoReceiver)
        }//if-receiver

        //Permiso bandeja entrada
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_SMS), siPermisoLecturaSms)
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.SEND_SMS), siPermisoEnvioSms)
        }//if-else enviar sms

        btnLista.setOnClickListener {

            cargarLista()

        }//btnLista

        btnVerifica.setOnClickListener {
            verificarSMS()
        }


    }//onCreate


    fun cargarLista(){

        try {

            var con = mensajesEntrantes("","","")
            con.asignarPuntero(this)
            var data = con.mostrarTodo()

            if(data.size == 0){
                if (con.error==2){
                    mensaje("Error en la tabla/vacia")
                }
            }


            var total = data.size-1
            var vector = Array<String>(data.size, {""})

            (0..total).forEach {

                var sms = data[it]
                var item = "Numero de telefono"+sms.numero+"\n"+"Plataforma buscada: "+sms.plataforma+"\n"+"Respondido: "+sms.respondido
                vector[it] = item

            }//forEach

            lista.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, vector)

        }catch (e : SQLiteException) {

        }

    }//cargarLista


    fun verificarSMS() {

        try{

            var base = BaseDatos(this,"contenido",null, 1)
            var select = base.readableDatabase
            var SQL = "SELECT * FROM entrantes"
            var cursor = select.rawQuery(SQL,null)



            if(cursor.moveToFirst()){

                do {

                    var numero = cursor.getString(0)
                    var plataforma = cursor.getString(1)
                    var estado = cursor.getString(2)
                    plataforma = plataforma.toUpperCase()

                    when(plataforma){
                        "NETFLIX" ->{

                            if(estado == "N"){

                                var datos = traerDatos("NETFLIX")
                                //mensaje(estado)
                                enviarSMS(datos,numero)
                                actualizaEstado(numero)

                            }//if-estado

                        }//netFlix

                        "HBO" ->{

                            if(estado == "N"){

                                var datos = traerDatos("HBO")
                                //mensaje(estado)
                                enviarSMS(datos,numero)
                                actualizaEstado(numero)

                            }//if-estado

                        }//hbo

                        "DISNEY"->{

                            if(estado=="N"){

                                var datos = traerDatos("DISNEY")
                                //mensaje(estado)
                                enviarSMS(datos,numero)
                                actualizaEstado(numero)

                            }//if-estado

                        }//disney

                        else->{
                            var mensaje = "el mensaje debe contener un servicio de streming HBO,NETFLIX, DISNEY"
                            enviarSMS(mensaje,numero)
                        }//else

                    }//when-platafomra

                }while (cursor.moveToNext())

            }//if-cursorMoveToFirst

        }catch(e: SQLiteException){
            mensaje(e.message.toString())
        }

    }//verificarSMS


    fun actualizaEstado(numero: String){
        try {

            var base = BaseDatos(this,"contenido",null,1)
            var update = base.writableDatabase
            var SQL = "UPDATE entrantes SET Respondido = 'S' WHERE Celular = ?"
            var parametro = arrayOf(numero)

            update.execSQL(SQL,parametro)
            base.close()

        }catch (e:SQLiteException){
            mensaje(e.message.toString())
        }
    }//actualizaEstado


    fun traerDatos(plataforma:String) : String{
        var datos = ""
        try {

            var plataformaBusqueda = arrayOf(plataforma)
            var base = BaseDatos(this, "contenido", null, 1)
            var select = base.readableDatabase
            var SQL = "SELECT * FROM PeliculasNetflix WHERE Plataforma = ?"
            var cursor = select.rawQuery(SQL,plataformaBusqueda)

            if (cursor.moveToFirst()){
                do {

                    datos +=cursor.getString(1)+"\n"

                }while (cursor.moveToNext())
            }

            base.close()

        }catch (e : SQLiteException){

        }

        return  datos

    }//traerDatos


    fun enviarSMS(mensaje:String, numero:String){

        SmsManager.getDefault().sendTextMessage(numero,null,mensaje,null,null)

    }//enviarSMS


    fun mensaje(m : String){
        Toast.makeText(this,m,Toast.LENGTH_LONG).show()
    }//mensaje


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == siPermisoReceiver){
            AlertDialog.Builder(this)
                .setMessage("Se otorgo el permiso Receiver")
                .show()
        }

        if (requestCode == siPermisoLecturaSms){
            verificarSMS()
        }

    }//onRequestPermission

}//class
