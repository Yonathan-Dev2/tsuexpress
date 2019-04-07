package com.cuyesgyg.appcuy.Fragmentos;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.cuyesgyg.appcuy.Alertas;
import com.cuyesgyg.appcuy.Buscar_crecimiento;
import com.cuyesgyg.appcuy.R;

import org.json.JSONObject;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cuyesgyg.appcuy.cuadro_dialogo;
import com.cuyesgyg.appcuy.cuadro_dialogo_afirmacion;
import org.json.JSONArray;
import org.json.JSONException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragmento_recria extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener{

    EditText poza, cantidad, fecha_ini, fecha_fin, peso;
    Button buscar, actualizar, guardar, limpiar;
    Spinner sexo;
    String recu_sexo, fecha_recu_fin, recu_poza, fecha_recu_ini, recu_cantidad;
    int aux;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    private String v_login;
    View vista;

    Fragmento_recria contexto;

    ProgressDialog pdp = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contexto = this;

        cargarpreferencias();

        vista=inflater.inflate(R.layout.fragmento_recria, container, false);

        poza = (EditText)vista.findViewById(R.id.edtpoza_crec);
        sexo = (Spinner) vista.findViewById(R.id.spnsexo_crec);
        cantidad = (EditText)vista.findViewById(R.id.edtcantidad_crec);
        fecha_ini = (EditText)vista.findViewById(R.id.edtfechaini_crec);
        fecha_fin = (EditText)vista.findViewById(R.id.edtfechafin_crec);
        peso = (EditText)vista.findViewById(R.id.edtpeso);
        buscar  = (Button)vista.findViewById(R.id.btnbuscar_crec);
        actualizar = (Button)vista.findViewById(R.id.btnactualiza_crec);
        guardar = (Button)vista.findViewById(R.id.btnguardar_crec);
        limpiar = (Button)vista.findViewById(R.id.btnlimpiar_crec);

        request= Volley.newRequestQueue(getContext());

        fecha_fin.setEnabled(false);
        fecha_ini.setEnabled(false);

        actualizar.setEnabled(false);
        limpiar.setEnabled(false);
        aux=0;
        recu_sexo="";


        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarwebservices();
            }
        });

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarwebservices();
            }
        });

        limpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpiarwebservices();
            }
        });

        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarwebservices();
            }
        });

        return vista;
    }



    @Override
    public void onErrorResponse(VolleyError error) {
        pdp.dismiss();
        if (aux==2){
            poza.setText("");
            fecha_fin.setText("");
            fecha_ini.setText("");
            sexo.setSelection(0);
            cantidad.setText("");
            peso.setText("");
            poza.requestFocus();
            new cuadro_dialogo_afirmacion (getContext(), "Se actualizo los datos correctamente");
            aux=0;
        }
        else {
            if (aux == 1) {
                new cuadro_dialogo(getContext(), "La poza no cuenta con cuyes de RECRIA");
                buscar.setEnabled(true);
                limpiar.setEnabled(true);
                actualizar.setEnabled(false);
            }
            else{
                new cuadro_dialogo (getContext(), "No se pudo conectar con el servidor");
                buscar.setEnabled(true);
                limpiar.setEnabled(false);
                actualizar.setEnabled(false);
                aux = 0;
            }
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        pdp.dismiss();

        if(aux==1){
            Buscar_crecimiento miCrecimiento = new Buscar_crecimiento();
            JSONArray json = response.optJSONArray("b_crecimiento");
            JSONObject jsonObject=null;

            try {

                jsonObject=json.getJSONObject(0);

                miCrecimiento.setCantidad(jsonObject.optString("eCantidad"));
                miCrecimiento.setFecha_inicio(jsonObject.optString("fFecha_inicio"));
                miCrecimiento.setFecha_fin(jsonObject.optString("fFecha_fin"));
                miCrecimiento.setPeso(jsonObject.optString("ePeso"));

                cantidad.setText(miCrecimiento.getCantidad());
                if(!miCrecimiento.getPeso().equalsIgnoreCase("0"))
                    peso.setText(miCrecimiento.getPeso());
                else
                    peso.setText("");

                SimpleDateFormat inSDF = new SimpleDateFormat("yyyy-mm-dd");
                SimpleDateFormat outSDF = new SimpleDateFormat("dd/mm/yyyy");
                Date date = null;
                Date date2= null;
                String fecha_in="";
                String fecha_fini="";
                try {
                    date = inSDF.parse(miCrecimiento.getFecha_inicio());

                    if (!miCrecimiento.getFecha_fin().equalsIgnoreCase("0000-00-00")){
                        date2 = inSDF.parse(miCrecimiento.getFecha_fin());
                        fecha_fini = outSDF.format(date2);
                    }


                } catch (ParseException e) {
                    e.printStackTrace();
                }

                fecha_in = outSDF.format(date);

                fecha_ini.setText(fecha_in);
                fecha_fin.setText(fecha_fini);

                fecha_fin.setEnabled(true);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            aux=0;
        }
        else{
            //ALgoritmo cuando se registra correctamente la RECRIA envia el mensaje desde el WebServices con JSON
            Alertas miAlerta = new Alertas();
            JSONArray json = response.optJSONArray("mensaje");
            JSONObject jsonObject=null;

            try {
                jsonObject=json.getJSONObject(0);
                miAlerta.setMensaje(jsonObject.optString("msn"));
                new cuadro_dialogo (getContext(), miAlerta.getMensaje());

            } catch (JSONException e) {
                e.printStackTrace();

            }

            poza.setText("");
            sexo.setSelection(0);
            cantidad.setText("");
            fecha_ini.setText("");
            fecha_fin.setText("");
            peso.setText("");
            poza.requestFocus();
            limpiar.setEnabled(false);
            actualizar.setEnabled(false);
            buscar.setEnabled(true);
            guardar.setEnabled(true);

        }


    }


    private void actualizarwebservices() {

        recu_sexo=sexo.getSelectedItem().toString();
        String fecha_peso=null;

        SimpleDateFormat inSDF = new SimpleDateFormat("dd/mm/yyyy");
        SimpleDateFormat outSDF = new SimpleDateFormat("yyyy-mm-dd");

        SimpleDateFormat ouSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        fecha_recu_ini = fecha_ini.getText().toString();
        fecha_recu_fin = fecha_fin.getText().toString();

        Date fecha = new Date();
        fecha_peso = ouSDF.format(fecha);


        String fecha_mysql = "";
        String fecha_mysql2 = "";

        try {
            Date date = inSDF.parse(fecha_recu_ini);
            fecha_mysql = outSDF.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if(!fecha_recu_fin.equalsIgnoreCase("")){

            try {
                Date date2 = inSDF.parse(fecha_recu_fin);
                fecha_mysql2 = outSDF.format(date2);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }



        if(!poza.getText().toString().equalsIgnoreCase("") && !recu_sexo.equalsIgnoreCase("SEXO") && !cantidad.getText().toString().equalsIgnoreCase("") && !fecha_recu_ini.equalsIgnoreCase("") ){

            String url = "https://cuyesgyg.com/intranet/webservices/actualizar_crecimiento.php?poza="+poza.getText().toString()+"&sexo="+recu_sexo
                    +"&cantidad="+cantidad.getText().toString()+"&fecha_inicio="+fecha_mysql+"&fecha_fin="+fecha_mysql2+"&peso="+peso.getText().toString()
                    +"&fecha_peso="+fecha_peso+"&usuario="+v_login;

            jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
            request.add(jsonObjectRequest);

            poza.setText("");
            fecha_fin.setText("");
            fecha_ini.setText("");
            peso.setText("");
            sexo.setSelection(0);
            cantidad.setText("");
            poza.requestFocus();

            actualizar.setEnabled(false);
            guardar.setEnabled(true);
            buscar.setEnabled(true);
            limpiar.setEnabled(false);
            poza.setEnabled(true);
            sexo.setEnabled(true);
            aux=2;
        }
        else
            new cuadro_dialogo (getContext(), "Debes ingresar todo los datos");

        pdp = new ProgressDialog(getContext());
        pdp.show();
        pdp.setContentView(R.layout.progressbar);
        pdp.setCancelable(false);
        pdp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


    }

    private void limpiarwebservices() {
        poza.setText("");
        sexo.setSelection(0);
        cantidad.setText("");
        fecha_ini.setText("");
        fecha_fin.setText("");
        peso.setText("");
        poza.requestFocus();
        buscar.setEnabled(true);
        limpiar.setEnabled(false);
        actualizar.setEnabled(false);
        guardar.setEnabled(true);
        poza.setEnabled(true);
        sexo.setEnabled(true);
        fecha_ini.setEnabled(false);
        fecha_fin.setEnabled(false);
    }

    private void buscarwebservices() {
        poza.setEnabled(false);
        sexo.setEnabled(false);
        fecha_ini.setEnabled(true);

        recu_sexo=sexo.getSelectedItem().toString();

        if(!poza.getText().toString().equalsIgnoreCase("") && !recu_sexo.equalsIgnoreCase("SEXO")){
            String url = "https://cuyesgyg.com/intranet/webservices/buscar_crecimiento.php?poza="+poza.getText().toString()+"&sexo="+recu_sexo;

            jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
            request.add(jsonObjectRequest);
            aux=1;

            buscar.setEnabled(false);
            guardar.setEnabled(false);
            actualizar.setEnabled(true);
            limpiar.setEnabled(true);
        }
        else{
            new cuadro_dialogo (getContext(), "Debes ingresar los datos necesarios");
            poza.setEnabled(true);
            sexo.setEnabled(true);
        }

        pdp = new ProgressDialog(getContext());
        pdp.show();
        pdp.setContentView(R.layout.progressbar);
        pdp.setCancelable(false);
        pdp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    private void guardarwebservices() {


        recu_sexo=sexo.getSelectedItem().toString();

        String fecha_inicio_r=null;
        Date fecha = new Date();
        SimpleDateFormat outSDF = new SimpleDateFormat("yyyy-MM-dd");
        fecha_inicio_r = outSDF.format(fecha);

        SimpleDateFormat inSDF = new SimpleDateFormat("dd/mm/yyyy");
        SimpleDateFormat ouSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        SimpleDateFormat oSDF = new SimpleDateFormat("yyyy-mm-dd");

        fecha_recu_fin =fecha_fin.getText().toString();
        String fecha_ar = "";

        if(!fecha_recu_fin.equalsIgnoreCase("")){

            try {
                Date date = inSDF.parse(fecha_recu_fin);
                fecha_ar = oSDF.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (!poza.getText().toString().equalsIgnoreCase("") && !recu_sexo.equalsIgnoreCase("SEXO") && !cantidad.getText().toString().equalsIgnoreCase("")){
            String url = "https://cuyesgyg.com/intranet/webservices/registrar_recria.php?poza="+poza.getText().toString()+"&sexo="+recu_sexo+
                    "&cantidad="+cantidad.getText().toString()+"&fecha_inicio="+fecha_inicio_r+"&fecha_fin="+fecha_ar+"&peso="+peso.getText().toString()+
                    "&usuario="+v_login;

            guardar.setEnabled(false);
            jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
            request.add(jsonObjectRequest);

        }

        else{
            new cuadro_dialogo (getContext(), "Debes ingresar todo los datos");
            }

        pdp = new ProgressDialog(getContext());
        pdp.show();
        pdp.setContentView(R.layout.progressbar);
        pdp.setCancelable(false);
        pdp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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
