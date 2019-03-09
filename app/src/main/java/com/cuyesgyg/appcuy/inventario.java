package com.cuyesgyg.appcuy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class inventario extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    Spinner bien_in, estado_in;
    EditText cantidad_in, fechaingreso_in;
    Button guardar_in;
    String recu_bien, recu_estado;

    private String v_login;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_inventario);

        bien_in = (Spinner)(findViewById(R.id.spn_bien));
        estado_in = (Spinner)(findViewById(R.id.spnestado_bien));
        cantidad_in = (EditText)(findViewById(R.id.edtcantidad_bien));
        fechaingreso_in = (EditText)(findViewById(R.id.edtfechaingreso_bien));
        guardar_in = (Button)(findViewById(R.id.btnguardar_in));

        fechaingreso_in.setEnabled(false);
        bien_in.requestFocus();

        request= Volley.newRequestQueue(getBaseContext());

        v_login = String.valueOf(getIntent().getExtras().getString("parametro")) ;

        guardar_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarwebservices();
            }
        });


    }

    private void guardarwebservices() {

        String fecha_detalle=null;
        Date fecha = new Date();
        SimpleDateFormat outSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        fecha_detalle = outSDF.format(fecha);

        recu_bien = bien_in.getSelectedItem().toString();
        recu_estado = estado_in.getSelectedItem().toString();


        if(!recu_bien.equalsIgnoreCase("BIEN") && !recu_estado.equalsIgnoreCase("ESTADO") && !cantidad_in.getText().toString().equalsIgnoreCase("")){
            String url = "https://cuyesgyg.com/intranet/webservices/registrar_inventario.php?bien="+recu_bien+
                    "&cantidad="+cantidad_in.getText().toString()+"&estado="+recu_estado+"&fecha_ingreso="+fecha_detalle;

            jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
            request.add(jsonObjectRequest);

        }
        else
            Toast.makeText(getBaseContext(),"Debe ingresar todo los datos", Toast.LENGTH_SHORT).show();


    }


    @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(getBaseContext(),"Se ha registrado exitosamente", Toast.LENGTH_SHORT).show();
        bien_in.setSelection(0);
        cantidad_in.setText("");
        estado_in.setSelection(0);
        fechaingreso_in.setText("");
        bien_in.requestFocus();

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getBaseContext(),"No se pudo conectar con sel servidor "+error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("Error",error.toString());

    }
}
