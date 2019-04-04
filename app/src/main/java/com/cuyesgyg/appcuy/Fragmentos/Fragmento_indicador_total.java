package com.cuyesgyg.appcuy.Fragmentos;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cuyesgyg.appcuy.Consulta_reporte_total;
import com.cuyesgyg.appcuy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragmento_indicador_total extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {

    TextView crias, reproductores, crecimiento, total;
    TextView msncrias, msnreproductores, msncrecimiento, msntotal;
    Button mostrar;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    View vista;
    Fragmento_indicador_total contexto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        contexto = this;
        vista = inflater.inflate(R.layout.fragmento_indicador_total, container, false);

        crias = (TextView)vista.findViewById(R.id.txtcrias_rt);
        reproductores = (TextView)vista.findViewById(R.id.txtreproductores_rt);
        crecimiento = (TextView)vista.findViewById(R.id.txtcrecimiento_rt);
        total = (TextView)vista.findViewById(R.id.txttotal_rt);

        msncrias = (TextView) vista.findViewById(R.id.txtmsncrias_rt);
        msnreproductores = (TextView) vista.findViewById(R.id.txtmsnreproductores_rt);
        msncrecimiento = (TextView)vista.findViewById(R.id.txtmsncrecimiento_rt);
        msntotal = (TextView)vista.findViewById(R.id.txtmsntotal_rt);

        request= Volley.newRequestQueue(getContext());

        mostrarindicador();

        return vista;
    }

    private void mostrarindicador() {
        msnreproductores.setText("");
        msncrecimiento.setText("");
        msncrias.setText("");
        msntotal.setText("");

        reproductores.setText("");
        crecimiento.setText("");
        crias.setText("");
        total.setText("");

        String url = "https://cuyesgyg.com/intranet/webservices/indicador_cuyes.php";

        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);
    }


    @Override
    public void onErrorResponse(VolleyError error) {

        Toast.makeText(getContext(),"No se pudo conectar con sel servidor "+error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("Error",error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {

        msnreproductores.setText("REPRODUCTORES");
        msncrecimiento.setText("RECRIA");
        msncrias.setText("CRIAS");
        msntotal.setText("TOTAL");


        Consulta_reporte_total miConsulta = new Consulta_reporte_total();
        JSONArray json = response.optJSONArray("indicador_cuyes");

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
}
