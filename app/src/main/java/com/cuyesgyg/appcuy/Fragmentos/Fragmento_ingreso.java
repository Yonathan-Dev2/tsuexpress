package com.cuyesgyg.appcuy.Fragmentos;


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

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    View vista;
    Fragmento_recria contexto;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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

        String fecha_detalle=null;
        Date fecha = new Date();
        SimpleDateFormat outSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        fecha_detalle = outSDF.format(fecha);

        recu_producto=producto.getSelectedItem().toString();


        if (!recu_producto.equalsIgnoreCase("INGRESO") && !cantidad.getText().toString().equalsIgnoreCase("")  && !precio.getText().toString().equalsIgnoreCase("") )  {

            suma_capital = Double.parseDouble(cantidad.getText().toString())* Double.parseDouble(precio.getText().toString());

            String url = "https://cuyesgyg.com/intranet/webservices/registrar_comercializacion.php?producto="+recu_producto+"&detalle=Ingreso"+
                    "&cantidad="+cantidad.getText().toString()+"&precio="+precio.getText().toString()+"&fecha_detalle="+fecha_detalle
                    +"&capital="+suma_capital;

            jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
            request.add(jsonObjectRequest);
            guardar.setEnabled(false);
        }
        else{
            Toast.makeText(getContext(),"Debe ingresar todo los datos", Toast.LENGTH_SHORT).show();
        }

    }

    private void calculare() {

        if (!precio.getText().toString().equalsIgnoreCase("")  && !cantidad.getText().toString().equalsIgnoreCase("")){
            recu_total=Integer.parseInt(cantidad.getText().toString())* Float.parseFloat(precio.getText().toString()) ;
            total.setText("S/. "+String.valueOf(recu_total));
        }
        else{
            Toast.makeText(getContext(),"Debe ingresar todo los datos", Toast.LENGTH_SHORT).show();
        }
        
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(),"No se pudo conectar con sel servidor "+error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("Error",error.toString());
        guardar.setEnabled(true);
    }
    @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(getContext(),"Se ha registrado exitosamente", Toast.LENGTH_SHORT).show();
        producto.setSelection(0);
        cantidad.setText("");
        precio.setText("");
        total.setText("");
        cantidad.requestFocus();
        guardar.setEnabled(true);
    }
}
