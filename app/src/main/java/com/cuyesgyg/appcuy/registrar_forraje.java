package com.cuyesgyg.appcuy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class registrar_forraje extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {
    EditText cantidad_for, obs_for;
    Button guardar_for;
    String fecha_recu;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    private String v_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_forraje);

        cantidad_for = (EditText)(findViewById(R.id.edtcantidad_for));
        guardar_for = (Button)(findViewById(R.id.btnforraje));
        obs_for = (EditText) (findViewById(R.id.edtobserva_for));

        cantidad_for.requestFocus();
        request= Volley.newRequestQueue(getBaseContext());

        guardar_for.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarwebservices();
            }
        });


    }

    private void guardarwebservices() {

        String fecha_peso="";

        Date fecha = new Date();
        SimpleDateFormat outSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        fecha_peso = outSDF.format(fecha);

        if (!cantidad_for.getText().toString().equalsIgnoreCase("0") && !cantidad_for.getText().toString().equalsIgnoreCase("")){
            String url = "https://cuyesgyg.com/intranet/webservices/registrar_forraje.php?cantidad="+cantidad_for.getText().toString()+"&fecha_corte="+fecha_peso
                    +"&observacion="+obs_for.getText().toString()+"&usuario="+v_login;


            jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
            request.add(jsonObjectRequest);
        }
        else{
            Toast.makeText(getBaseContext(),"Se debe ingresar la cantidad", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onResponse(JSONObject response) {

        Toast.makeText(getBaseContext(),"Se ha registrado exitosamente", Toast.LENGTH_SHORT).show();
        cantidad_for.setText("");
        cantidad_for.requestFocus();
        obs_for.setText("");

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getBaseContext(),"No se pudo conectar con el servidor "+error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("Error",error.toString());
    }
}
