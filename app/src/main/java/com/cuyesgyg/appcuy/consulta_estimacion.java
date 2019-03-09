package com.cuyesgyg.appcuy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
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

public class consulta_estimacion extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    Spinner sexo_es, mes_es;
    TextView msn_es, estimacion_es;
    Button consultar;
    String recu_mes, recu_sexo;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_estimacion);

        sexo_es = (Spinner)(findViewById(R.id.spnsexo_es));
        mes_es = (Spinner)(findViewById(R.id.spnmes_es));
        msn_es = (TextView)(findViewById(R.id.txtmsn_es));
        estimacion_es = (TextView)(findViewById(R.id.txtestimacion));
        consultar = (Button)(findViewById(R.id.btnconsultar_es));

        request= Volley.newRequestQueue(getBaseContext());

        consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultarwebservices();
            }
        });

    }

    private void consultarwebservices() {
        msn_es.setText("");
        estimacion_es.setText("");

        recu_mes=mes_es.getSelectedItem().toString();
        recu_sexo=sexo_es.getSelectedItem().toString();


        if (!recu_sexo.equalsIgnoreCase("SEXO") && !recu_mes.equalsIgnoreCase("MES") ){
            String url = "https://cuyesgyg.com/intranet/webservices/estimacion_cuyes.php?mes="+recu_mes+
                    "&sexo="+recu_sexo;

            jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
            request.add(jsonObjectRequest);
        }
        else
            Toast.makeText(getBaseContext(),"Debe seleccionar el SEXO y MES", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onResponse(JSONObject response) {

        Consu_estimacion miConsulta = new Consu_estimacion();

        JSONArray json = response.optJSONArray("e_cuyes");
        JSONObject jsonObject=null;

        try {

                jsonObject=json.getJSONObject(0);
                miConsulta.setMes(jsonObject.optString("mes"));
                miConsulta.setAno(jsonObject.optString("ano"));
                miConsulta.setCantidad(jsonObject.optString("cantidad"));

                msn_es.setText("Cantidad de cuyes para venta");
                if (!miConsulta.getCantidad().equalsIgnoreCase("NULL"))
                estimacion_es.setText(miConsulta.getCantidad());
                else
                estimacion_es.setText("0");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {

        Toast.makeText(getBaseContext(),"No se pudo conectar con sel servidor "+error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("Error",error.toString());

        msn_es.setText("");
        estimacion_es.setText("");

    }
}
