package com.cuyesgyg.appcuy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class consu_reproductores extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener{

    Button consultar;
    TextView resultado, mensaje;
    Spinner sexo, raza, poza;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    String cons_recu_raza, cons_recu_sexo, cons_recu_poza, f_resultado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consu_reproductores);

        consultar = (Button)(findViewById(R.id.btnconsultar));
        sexo = (Spinner)(findViewById(R.id.spn_sexo2));
        raza = (Spinner)(findViewById(R.id.spn_raza2));
        poza = (Spinner)(findViewById(R.id.spn_poza2));
        resultado = (TextView)(findViewById(R.id.txtresultado_vivos));
        mensaje = (TextView)(findViewById(R.id.txtmensaje));

        request= Volley.newRequestQueue(getBaseContext());

        consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarwebservices();
            }
        });

    }

    private void cargarwebservices() {
        mensaje.setText("");
        resultado.setText("");

        cons_recu_sexo=sexo.getSelectedItem().toString();
        cons_recu_raza=raza.getSelectedItem().toString();
        cons_recu_poza=poza.getSelectedItem().toString();

        if(!cons_recu_poza.equalsIgnoreCase("POZA") && !cons_recu_sexo.equalsIgnoreCase("SEXO") && !cons_recu_raza.equalsIgnoreCase("RAZA")){

            String url = "https://cuyesgyg.com/intranet/webservices/consulta_reproductores.php?sexo="+cons_recu_sexo+
                    "&raza="+cons_recu_raza+"&poza="+cons_recu_poza;

            jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
            request.add(jsonObjectRequest);
        }
        else
        {
            Toast.makeText(getBaseContext(),"Debe ingresar todo los datos", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getBaseContext(), "No hay conexion con el servidor", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {

        Consulta_reproductores miConsulta = new Consulta_reproductores();
        JSONArray json = response.optJSONArray("reproductores");
        JSONObject jsonObject=null;

        try{
            jsonObject=json.getJSONObject(0);
            miConsulta.setcant_reproductores(jsonObject.optString("cantidad"));

        }
        catch (JSONException e){
            e.printStackTrace();
        }

        f_resultado=miConsulta.getcant_reproductores();


        mensaje.setText("LA CANTIDAD DE REPRODUCTORES ES : ");
        resultado.setText(f_resultado);


    }
}
