package com.cuyesgyg.appcuy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.lang.String;

public class registrar_termohigrometro extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener{

    EditText temp_celsius,humedad;
    Button guardar;


    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    private String v_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperatura);

        temp_celsius=null;
        humedad=null;

        temp_celsius=(EditText)(findViewById(R.id.edttemperatura));
        humedad=(EditText)(findViewById(R.id.edthumedad));
        guardar=(Button)(findViewById(R.id.btnguardar_re));

        temp_celsius.requestFocus();

        request= Volley.newRequestQueue(getBaseContext());

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarwebservices();
            }
        });

    }

    private void cargarwebservices() {

        String tiempo=null;

        Date fecha = new Date();

        SimpleDateFormat outSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tiempo = outSDF.format(fecha);

            String url = "https://cuyesgyg.com/intranet/webservices/registrar_termohigrometro.php?temp_celsius="+temp_celsius.getText().toString() +
                    "&humedad="+humedad.getText().toString()+"&tiempo="+tiempo+"&usuario="+v_login;

            jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
            request.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        /*Toast.makeText(getBaseContext(),"No se tiene conexion con el servidor "+error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("Error",error.toString());*/

        Toast.makeText(getBaseContext(),"Se ha registrado exitosamente /E", Toast.LENGTH_SHORT).show();
        temp_celsius.setText("");
        humedad.setText("");
        temp_celsius.requestFocus();

    }

    @Override
        public void onResponse(JSONObject response) {
        Toast.makeText(getBaseContext(),"Se ha registrado exitosamente", Toast.LENGTH_SHORT).show();
        temp_celsius.setText("");
        humedad.setText("");
        temp_celsius.requestFocus();

    }
}
