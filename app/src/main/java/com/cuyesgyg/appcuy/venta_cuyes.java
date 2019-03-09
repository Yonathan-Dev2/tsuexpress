package com.cuyesgyg.appcuy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class venta_cuyes extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    Spinner ano_v, mes_v;
    TextView msnventa, cant_venta;
    Button consultar_vent;

    String recu_ano_v, recu_mes_v;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venta_cuyes);

        ano_v = (Spinner)(findViewById(R.id.spnano_venta));
        mes_v = (Spinner) (findViewById(R.id.spnmes_venta));
        consultar_vent = (Button)(findViewById(R.id.btnconsultar_venta));
        msnventa = (TextView)(findViewById(R.id.txtmsnventa));
        cant_venta = (TextView)(findViewById(R.id.txtventa)) ;


        request= Volley.newRequestQueue(getBaseContext());

        consultar_vent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultarventawebservices();
            }
        });


    }

    private void consultarventawebservices() {
        msnventa.setText("");
        cant_venta.setText("");

        recu_ano_v=ano_v.getSelectedItem().toString();
        recu_mes_v=mes_v.getSelectedItem().toString();

        String url = "https://cuyesgyg.com/intranet/webservices/consulta_venta_cuyes.php?mes="+recu_mes_v+"&ano="+recu_ano_v;

        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);
    }


    @Override
    public void onResponse(JSONObject response) {
        Consulta_ventas miConsulta = new Consulta_ventas();

        JSONArray json = response.optJSONArray("v_cuyes");
        JSONObject jsonObject=null;

        try {
            jsonObject=json.getJSONObject(0);
            miConsulta.setCantidad(jsonObject.optString("cantidad"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

            if(!miConsulta.getCantidad().equalsIgnoreCase("null")){
                msnventa.setText("La cantidad de venta");
                cant_venta.setText(miConsulta.getCantidad());
            }
            else {
                msnventa.setText("La cantidad de venta");
                cant_venta.setText("0");
            }


    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getBaseContext(),"No se pudo conectar con sel servidor "+error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("Error",error.toString());
    }
}
