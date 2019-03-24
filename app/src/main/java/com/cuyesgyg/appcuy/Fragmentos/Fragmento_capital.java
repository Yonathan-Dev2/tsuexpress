package com.cuyesgyg.appcuy.Fragmentos;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cuyesgyg.appcuy.Alertas;
import com.cuyesgyg.appcuy.R;
import com.cuyesgyg.appcuy.cuadro_dialogo;
import com.cuyesgyg.appcuy.cuadro_dialogo_afirmacion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragmento_capital extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener {

    EditText capital;
    Button guardar;
    String v_login;
    View vista;

    Fragmento_capital contexto;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contexto = this;
        cargarpreferencias();
        vista = inflater.inflate(R.layout.fragmento_capital, container, false);
        capital = (EditText) vista.findViewById(R.id.edt_capi);
        guardar = (Button) vista.findViewById(R.id.btn_guar_capi);
        request= Volley.newRequestQueue(getContext());
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarcapital();
            }
        });
        return vista;
    }

    private void registrarcapital() {

        if (!capital.getText().toString().equalsIgnoreCase("")){
            String url = "https://cuyesgyg.com/intranet/webservices/registrar_capital.php?capital="+capital.getText().toString()
                         +"&usuario="+v_login;
            jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
            request.add(jsonObjectRequest);
        }
        else{
            new cuadro_dialogo(getContext(), "Debes ingresar todo los datos");
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        new cuadro_dialogo (getContext(), "No se tiene conexion al servidor");
    }

    @Override
    public void onResponse(JSONObject response) {

        Alertas miAlerta = new Alertas();
        JSONArray json = response.optJSONArray("mensaje");
        JSONObject jsonObject=null;

        try {
            jsonObject=json.getJSONObject(0);
            miAlerta.setMensaje(jsonObject.optString("msn"));
            new cuadro_dialogo_afirmacion (getContext(), miAlerta.getMensaje());
            capital.setText("");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void cargarpreferencias() {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("credenciales", Context.MODE_PRIVATE);

        String user = preferences.getString("log","");
        String pass = preferences.getString("pass","");
        String nom_ape = preferences.getString("nom_ape","");
        String corr = preferences.getString("corr","");
        Boolean check = preferences.getBoolean("check",false);

        v_login = user;

    }


}
