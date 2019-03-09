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

public class reporte_nacimientos extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener {

    Spinner poza, mes_cri;
    TextView muertos, vivos, destetados, msnmuertos, msnvivos, msndestetados;
    Button consultar;
    String recu_poza, recu_mes;



    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte_nacimientos);
        poza = (Spinner)(findViewById(R.id.spnpoza_control));
        mes_cri = (Spinner)(findViewById(R.id.spnmes_cri));
        muertos = (TextView)(findViewById(R.id.txtresultado_muertos));
        vivos = (TextView)(findViewById(R.id.txtresultado_vivos));
        destetados = (TextView)(findViewById(R.id.txtresultado_destetados));
        consultar = (Button)(findViewById(R.id.btnconsultar_control));

        msnmuertos = (TextView)(findViewById(R.id.txtmuertos_control));
        msnvivos = (TextView)(findViewById(R.id.txtvivos_control));
        msndestetados = (TextView)(findViewById(R.id.txtdestetados_control));

        request= Volley.newRequestQueue(getBaseContext());

        consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarwebservices();
                muertos.setText("");
                vivos.setText("");
                destetados.setText("");
                msnvivos.setText("");
                msnmuertos.setText("");
                msndestetados.setText("");

            }
        });
    }

    private void cargarwebservices() {
        recu_poza=poza.getSelectedItem().toString();
        recu_mes = mes_cri.getSelectedItem().toString();

        if((!recu_poza.equalsIgnoreCase("TODOS") && recu_mes.equalsIgnoreCase("TODOS")) ){
            Toast.makeText(getBaseContext(),"No existe el MES 'TODOS' seleccione la opcion correcta", Toast.LENGTH_SHORT).show();

        }
        else {
            if (recu_poza.equalsIgnoreCase("POZA") && recu_mes.equalsIgnoreCase("MES")) {
                Toast.makeText(getBaseContext(),"Debe colocar el numero POZA", Toast.LENGTH_SHORT).show();
            } else {
                String url = "https://cuyesgyg.com/intranet/webservices/reporte_nacimientos.php?poza=" + recu_poza + "&mes=" + recu_mes;

                jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
                request.add(jsonObjectRequest);
            }
        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getBaseContext(),"No se pudo conectar con el servidor "+error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("Error",error.toString());

    }

    @Override
    public void onResponse(JSONObject response) {

        muertos.setText("");
        vivos.setText("");
        destetados.setText("");
        msnvivos.setText("");
        msnmuertos.setText("");
        msndestetados.setText("");

        Consulta_reporte_control miConsulta = new Consulta_reporte_control();
        JSONArray json = response.optJSONArray("controlito");
        JSONObject jsonObject=null;

        try {
            jsonObject=json.getJSONObject(0);
            miConsulta.setMuertos(jsonObject.optString("muertos"));
            miConsulta.setVivos(jsonObject.optString("vivos"));
            miConsulta.setDestetados(jsonObject.optString("destetados"));


            if(!miConsulta.getVivos().equalsIgnoreCase("null")){
                muertos.setText(miConsulta.getMuertos());
                vivos.setText(miConsulta.getVivos());
                destetados.setText(miConsulta.getDestetados());

               if(!recu_mes.equalsIgnoreCase("MES") && !recu_poza.equalsIgnoreCase("TODOS")) {
                   msnvivos.setText("NACIERON :");
                   msnmuertos.setText("MUERTOS :");
                   msndestetados.setText("DESTETADOS :");
               }
               else{
               if (recu_poza.equalsIgnoreCase("TODOS") && !recu_mes.equalsIgnoreCase("MES")){
                   msnvivos.setText("NACIERON :");
                   msnmuertos.setText("MUERTOS :");
                   msndestetados.setText("DESTETADOS :");
               }
                   else{
                       msnvivos.setText("VIVOS :");
                       msnmuertos.setText("MUERTOS :");
                       msndestetados.setText("DESTETADOS :");
                    }


               }

            }

            else{
                muertos.setText("");
                vivos.setText("");
                destetados.setText("");

                msnvivos.setText("");
                msnmuertos.setText("");
                msndestetados.setText("");
                Toast.makeText(getBaseContext(),"La poza aun no tiene registro de CRIAS en el mes", Toast.LENGTH_SHORT).show();
            }

        }
        catch (JSONException e){
            e.printStackTrace();
        }


    }
}
