package com.cuyesgyg.appcuy.Fragmentos;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cuyesgyg.appcuy.R;
import com.cuyesgyg.appcuy.cuadro_dialogo;
import com.cuyesgyg.appcuy.cuadro_dialogo_afirmacion;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragmento_ingreso extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener {

    Spinner producto;
    EditText cantidad, precio, total;
    String recu_producto;
    float recu_total;
    Button guardar, calcular;
    double suma_capital=0;
    String v_login;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    View vista;
    Fragmento_ingreso contexto;
    ProgressDialog pdp = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contexto = this;
        cargarpreferencias();

        vista = inflater.inflate(R.layout.fragmento_ingreso, container, false);

        producto = (Spinner) vista.findViewById(R.id.spnproducto);
        cantidad=(EditText)vista.findViewById(R.id.edtcantidad);
        precio = (EditText)vista.findViewById(R.id.edtprecio);
        total  = (EditText)vista.findViewById(R.id.edttotal);
        guardar = (Button)vista.findViewById(R.id.btnguardar_re);
        calcular = (Button)vista.findViewById(R.id.btncalcular);
        cantidad.requestFocus();
        total.setEnabled(false);

        request= Volley.newRequestQueue(getContext());

        calcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calculare();

            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardaringreso();
            }


        });

        return vista;
    }

    private void guardaringreso() {

        recu_producto=producto.getSelectedItem().toString();


        if (!recu_producto.equalsIgnoreCase("INGRESO") && !cantidad.getText().toString().equalsIgnoreCase("")  && !precio.getText().toString().equalsIgnoreCase("") )  {

            suma_capital = Double.parseDouble(cantidad.getText().toString())* Double.parseDouble(precio.getText().toString());

            String url = "https://cuyesgyg.com/intranet/webservices/registrar_comercializacion.php?producto="+recu_producto+"&detalle=Ingreso"+
                    "&cantidad="+cantidad.getText().toString()+"&precio="+precio.getText().toString()+"&capital="+suma_capital+"&usuario="+v_login;

            jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
            request.add(jsonObjectRequest);
            guardar.setEnabled(false);
        }
        else{
            new cuadro_dialogo(getContext(), "Debes ingresar todo los datos");
        }

        pdp = new ProgressDialog(getContext());
        pdp.show();
        pdp.setContentView(R.layout.progressbar);
        pdp.setCancelable(false);
        pdp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    private void calculare() {

        if (!precio.getText().toString().equalsIgnoreCase("")  && !cantidad.getText().toString().equalsIgnoreCase("")){
            recu_total=Integer.parseInt(cantidad.getText().toString())* Float.parseFloat(precio.getText().toString()) ;
            total.setText("S/. "+String.valueOf(recu_total));
        }
        else{
            new cuadro_dialogo(getContext(), "Debes ingresar todo los datos");
        }
        
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        pdp.dismiss();
        new cuadro_dialogo(getContext(), "No se pudo conectar con sel servidor ");
        guardar.setEnabled(true);
    }
    @Override
    public void onResponse(JSONObject response) {
        pdp.dismiss();
        new cuadro_dialogo_afirmacion(getContext(), "Se ha registrado exitosamente");
        producto.setSelection(0);
        cantidad.setText("");
        precio.setText("");
        total.setText("");
        cantidad.requestFocus();
        guardar.setEnabled(true);
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
