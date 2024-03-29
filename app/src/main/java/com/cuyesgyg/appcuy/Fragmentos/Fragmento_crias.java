package com.cuyesgyg.appcuy.Fragmentos;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.View;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.cuyesgyg.appcuy.Consulta_control_nacimientos;
import com.cuyesgyg.appcuy.R;
import com.cuyesgyg.appcuy.cuadro_dialogo;
import com.cuyesgyg.appcuy.cuadro_dialogo_afirmacion;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragmento_crias extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener {

    EditText poza, cantidad, fecha_control, observacion;
    Button guardar, buscar, limpiar;
    String estado_recu, valor_estado;

    Spinner estado;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    int aux=0;
    View vista;
    private String v_login;

    ProgressDialog pdp = null;

    Fragmento_crias contexto;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contexto = this;

        vista= inflater.inflate(R.layout.fragmento_crias, container, false);
        guardar = (Button)vista.findViewById(R.id.btnguardar_re);

        cargarpreferencias();

        poza = (EditText)vista.findViewById(R.id.edtpoza_re);
        cantidad = (EditText)vista.findViewById(R.id.edtcantidad);
        fecha_control = (EditText)vista.findViewById(R.id.edtfecha_control);
        observacion = (EditText)vista.findViewById(R.id.edtobservacion);
        estado = (Spinner)vista.findViewById(R.id.spnestado_cri);
        guardar = (Button)vista.findViewById(R.id.btnguardar_re);
        buscar = (Button)vista.findViewById(R.id.btnbuscar_re);
        limpiar = (Button)vista.findViewById(R.id.btnlimpiar_re);

        fecha_control.setEnabled(false);
        poza.requestFocus();
        request= Volley.newRequestQueue(getContext());

        limpiar.setEnabled(false);

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarwebservices();
                buscar.setEnabled(true);
            }
        });

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aux=1;
                buscarnacimientos();
                limpiar.setEnabled(true);
                buscar.setEnabled(false);
                poza.setEnabled(false);
                cantidad.setEnabled(false);
                observacion.setEnabled(false);
                guardar.setEnabled(false);
                estado.setEnabled(false);

            }
        });

        limpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                poza.setText("");
                cantidad.setText("");
                fecha_control.setText("");
                poza.requestFocus();
                observacion.setText("");
                buscar.setEnabled(true);
                limpiar.setEnabled(false);
                estado.setSelection(0);
                poza.setEnabled(true);
                cantidad.setEnabled(true);
                observacion.setEnabled(true);
                guardar.setEnabled(true);
                estado.setEnabled(true);
            }
        });


        return vista;

    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

    }


    private void cargarwebservices() {

        //this.pd = ProgressDialog.show(getContext(), "Procesando", "Espere unos segundos...", true,false);

        String fecha_controli = null;
        String tiempo=null;

        Date fecha = new Date();
        SimpleDateFormat out = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat outSDF = new SimpleDateFormat("yyyy-MM-dd");

        tiempo = out.format(fecha);
        fecha_controli = outSDF.format(fecha);

        estado_recu = estado.getSelectedItem().toString();
        if(estado_recu.equalsIgnoreCase("NACIDOS")){
            estado_recu = "VIVOS";
        }


        if(fecha_controli!=null && !poza.getText().toString().equalsIgnoreCase("") && !cantidad.getText().toString().equalsIgnoreCase("") && !estado_recu.equalsIgnoreCase("ESTADO") ){

            String url = "https://cuyesgyg.com/intranet/webservices/registrar_cria.php?poza="+poza.getText().toString()+"&cantidad="+cantidad.getText().toString()
                    +"&fecha_control="+fecha_controli+"&observacion="+observacion.getText().toString()+"&estado="+estado_recu+"&tiempo_registro="+tiempo
                    +"&usuario="+v_login;

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


    private void buscarnacimientos() {
        if (poza.getText().toString().equalsIgnoreCase("") ){
            new cuadro_dialogo(getContext(), "Debes ingresar el numero de poza");
        }
        else{
            String url = "https://cuyesgyg.com/intranet/webservices/consultar_control.php?poza="+poza.getText().toString();
            jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
            request.add(jsonObjectRequest);
        }

    }



    @Override
    public void onErrorResponse(VolleyError error) {
        pdp.dismiss();
        new cuadro_dialogo(getContext(), "No se pudo conectar con el servidor");
        guardar.setEnabled(true);
    }

    @Override
    public void onResponse(JSONObject response) {

        pdp.dismiss();
        //Para el boton buscar
        if (aux==1){
            Consulta_control_nacimientos miConsulta = new Consulta_control_nacimientos();
            JSONArray json = response.optJSONArray("b_control_cria");
            JSONObject jsonObject=null;
            String fecha_con=null;

            try {

                jsonObject=json.getJSONObject(0);

                miConsulta.setFecha_control(jsonObject.optString("fFecha_control"));
                miConsulta.setObservacion(jsonObject.optString("cObservacion"));
                miConsulta.setCantidad(jsonObject.optString("eCantidad"));
                miConsulta.setEstado(jsonObject.optString("cEstado"));

                SimpleDateFormat inSDF = new SimpleDateFormat("yyyy-mm-dd");
                SimpleDateFormat outSDF = new SimpleDateFormat("dd/mm/yyyy");

                Date date = null;

                try {
                    date = inSDF.parse(miConsulta.getFecha_control());

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                fecha_con = outSDF.format(date);

                cantidad.setText(miConsulta.getCantidad());
                fecha_control.setText(fecha_con);
                observacion.setText(miConsulta.getObservacion());

                valor_estado = miConsulta.getEstado();
                if(valor_estado.equalsIgnoreCase("DESTETADOS")){
                    estado.setSelection(1);
                }
                else{
                    if(valor_estado.equalsIgnoreCase("MUERTOS")){
                        estado.setSelection(2);
                    }
                    else{
                        estado.setSelection(3);

                    }

                }

                new cuadro_dialogo_afirmacion (getContext(), "Se ha encontrado los datos de la poza");
                poza.requestFocus();



            } catch (JSONException e) {
                e.printStackTrace();
            }

            aux=0;
        }

        //Para el boton guardar
        else {
            new cuadro_dialogo_afirmacion (getContext(), "Se ha registrado exitosamente");
            poza.setText("");
            cantidad.setText("");
            fecha_control.setText("");
            observacion.setText("");
            estado.setSelection(0);
            poza.requestFocus();
            guardar.setEnabled(true);
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
