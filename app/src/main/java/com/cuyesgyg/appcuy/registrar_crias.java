package com.cuyesgyg.appcuy;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class registrar_crias extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener {

    EditText poza, cantidad, fecha_control, observacion;
    Button guardar, buscar, limpiar;
    String estado_recu, valor_estado;

    Spinner estado;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    int aux=0;

    private String v_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_crias);

        cargarpreferencias();

        poza = (EditText)(findViewById(R.id.edtpoza_re));
        cantidad = (EditText)(findViewById(R.id.edtcantidad));
        fecha_control = (EditText)(findViewById(R.id.edtfecha_control));
        observacion = (EditText)(findViewById(R.id.edtobservacion));
        estado = (Spinner)(findViewById(R.id.spnestado_cri));
        guardar = (Button)(findViewById(R.id.btnguardar_re));
        buscar = (Button)(findViewById(R.id.btnbuscar_re));
        limpiar = (Button)(findViewById(R.id.btnlimpiar_re));

        fecha_control.setEnabled(false);

        poza.requestFocus();
        request= Volley.newRequestQueue(getBaseContext());


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

    }


    private void cargarwebservices() {

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
            Toast.makeText(getBaseContext(),"Debes ingresar todo los datos", Toast.LENGTH_SHORT).show();
        }

    }


    private void buscarnacimientos() {
        if (poza.getText().toString().equalsIgnoreCase("") ){
            Toast.makeText(getBaseContext(),"Debes ingresar el numero de poza", Toast.LENGTH_SHORT).show();

        }
        else{
            String url = "https://cuyesgyg.com/intranet/webservices/consultar_control.php?poza="+poza.getText().toString();
            jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
            request.add(jsonObjectRequest);
        }

    }


    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getBaseContext(),"No se pudo conectar con el servidor "+error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("Error",error.toString());
        guardar.setEnabled(true);

    }

    @Override
    public void onResponse(JSONObject response) {
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


                    Toast.makeText(getBaseContext(), "Se ha encontrado los datos de la poza", Toast.LENGTH_SHORT).show();
                    poza.requestFocus();



            } catch (JSONException e) {
                e.printStackTrace();
            }

        aux=0;
        }

        //Para el boton guardar
        else {
            Toast.makeText(getBaseContext(), "Se ha registrado exitosamente", Toast.LENGTH_SHORT).show();
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
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);

        String user = preferences.getString("log","");
        String pass = preferences.getString("pass","");
        String nom_ape = preferences.getString("nom_ape","");
        String corr = preferences.getString("corr","");
        Boolean check = preferences.getBoolean("check",false);

        v_login = user;

    }




}