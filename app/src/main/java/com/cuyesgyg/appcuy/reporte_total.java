package com.cuyesgyg.appcuy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class reporte_total extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    TextView crias, reproductores, crecimiento, total;
    TextView msncrias, msnreproductores, msncrecimiento, msntotal;
    Button mostrar;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte_total);

        crias = (TextView)(findViewById(R.id.txtcrias_rt));
        reproductores = (TextView)(findViewById(R.id.txtreproductores_rt));
        crecimiento = (TextView)(findViewById(R.id.txtcrecimiento_rt));
        total = (TextView)(findViewById(R.id.txttotal_rt));

        msncrias = (TextView)(findViewById(R.id.txtmsncrias_rt));
        msnreproductores = (TextView)(findViewById(R.id.txtmsnreproductores_rt));
        msncrecimiento = (TextView)(findViewById(R.id.txtmsncrecimiento_rt));
        msntotal = (TextView)(findViewById(R.id.txtmsntotal_rt));


        mostrar = (Button)(findViewById(R.id.btnmostrar));


        request= Volley.newRequestQueue(getBaseContext());

        mostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarwebservices();
            }
        });



    }

    private void mostrarwebservices() {

        msnreproductores.setText("");
        msncrecimiento.setText("");
        msncrias.setText("");
        msntotal.setText("");

        reproductores.setText("");
        crecimiento.setText("");
        crias.setText("");
        total.setText("");

        String url = "https://cuyesgyg.com/intranet/webservices/reporte_total_cuyes.php?dato=1";

        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);


    }

    @Override
    public void onResponse(JSONObject response) {


        msnreproductores.setText("REPRODUCTORES :");
        msncrecimiento.setText("RECRIA :");
        msncrias.setText("CRIAS :");
        msntotal.setText("TOTAL :");


        Consulta_reporte_total miConsulta = new Consulta_reporte_total();
        JSONArray json = response.optJSONArray("reporte_total");

        JSONObject jsonObject=null;

        try {
            jsonObject=json.getJSONObject(0);
            miConsulta.setTotal_crecimiento(jsonObject.optString("cantidad"));

            jsonObject=json.getJSONObject(1);
            miConsulta.setTotal_reproductores(jsonObject.optString("cantidad"));

            jsonObject=json.getJSONObject(2);
            miConsulta.setTotal_crias(jsonObject.optString("cantidad"));


        } catch (JSONException e) {
            e.printStackTrace();
        }


        reproductores.setText(miConsulta.getTotal_reproductores());
        crecimiento.setText(miConsulta.getTotal_crecimiento());
        crias.setText(miConsulta.getTotal_crias());


        total.setText(String.valueOf(Integer.parseInt(miConsulta.getTotal_crias()) + Integer.parseInt(miConsulta.getTotal_crecimiento()) + Integer.parseInt(miConsulta.getTotal_reproductores())) );




    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getBaseContext(),"No se pudo conectar con sel servidor "+error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("Error",error.toString());
    }
}
