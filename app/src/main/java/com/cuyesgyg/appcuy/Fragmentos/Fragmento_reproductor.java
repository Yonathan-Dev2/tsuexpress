package com.cuyesgyg.appcuy.Fragmentos;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.cuyesgyg.appcuy.Buscar_reproductor;
import com.cuyesgyg.appcuy.R;
import com.cuyesgyg.appcuy.cuadro_dialogo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Fragmento_reproductor extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener {

    EditText poza_re, fecha_empadre_re, codigo_re, fecha_c_estado_re;
    Button guardar_re, buscar_re, actualizar_re, limpiar_re;

    Spinner sexo_re, raza_re, estado_re;


    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    private String v_login;

    String recu_sexo, recu_raza, fecha_recu, recu_estado, fecha_c_recuestado;
    int aux=0;

    View vista;

    Fragmento_reproductor contexto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        contexto = this;

        vista = inflater.inflate(R.layout.fragmento_reproductor, container, false);


        codigo_re = (EditText) vista.findViewById(R.id.edtcodigo_re);
        poza_re = (EditText)vista.findViewById(R.id.edtpoza_re);
        raza_re = (Spinner)vista.findViewById(R.id.spnraza_re);
        sexo_re=(Spinner)vista.findViewById(R.id.spnsexo_re);
        fecha_empadre_re = (EditText)vista.findViewById(R.id.edtfecha_empadre_re);
        estado_re = (Spinner)vista.findViewById(R.id.spnestado_cuy_re);
        fecha_c_estado_re = (EditText)vista.findViewById(R.id.edtfecha_c_estado_re);


        guardar_re=(Button)vista.findViewById(R.id.btnguardar_re);
        buscar_re = (Button)vista.findViewById(R.id.btnbuscar_re);
        actualizar_re = (Button)vista.findViewById(R.id.btnactualizar_re);
        limpiar_re = (Button)vista.findViewById(R.id.btnlimpiar_re);

        codigo_re.requestFocus();


        request= Volley.newRequestQueue(getContext());

        limpiar_re.setEnabled(false);
        actualizar_re.setEnabled(false);

        guardar_re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cargarwebservices();
            }
        });

        buscar_re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarwebservices();
            }
        });

        actualizar_re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarwebservices();
            }
        });

        limpiar_re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codigo_re.setText("");
                poza_re.setText("");
                raza_re.setSelection(0);
                sexo_re.setSelection(0);
                fecha_empadre_re.setText("");
                estado_re.setSelection(0);
                fecha_c_estado_re.setText("");
                codigo_re.requestFocus();
                buscar_re.setEnabled(true);
                actualizar_re.setEnabled(false);
                guardar_re.setEnabled(true);
                limpiar_re.setEnabled(false);
            }
        });

        return vista;
    }


    private void actualizarwebservices() {

        recu_sexo=sexo_re.getSelectedItem().toString();
        recu_raza=raza_re.getSelectedItem().toString();
        recu_estado=estado_re.getSelectedItem().toString();

        SimpleDateFormat inSDF = new SimpleDateFormat("dd/mm/yyyy");
        SimpleDateFormat outSDF = new SimpleDateFormat("yyyy-mm-dd");

        fecha_recu=fecha_empadre_re.getText().toString();
        fecha_c_recuestado = fecha_c_estado_re.getText().toString();

        String fecha_mysql = "";
        String fecha_mysql2 = "";

        if(!fecha_recu.equalsIgnoreCase("")){

            try {
                Date date = inSDF.parse(fecha_recu);
                Date date2 = inSDF.parse(fecha_c_recuestado);
                fecha_mysql = outSDF.format(date);
                fecha_mysql2 = outSDF.format(date2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if(!codigo_re.getText().toString().equalsIgnoreCase("") && !recu_sexo.equalsIgnoreCase("SEXO") && !recu_raza.equalsIgnoreCase("RAZA") && !fecha_mysql.equalsIgnoreCase("") &&
                !poza_re.getText().toString().equalsIgnoreCase("") &&
                !recu_estado.equalsIgnoreCase("ESTADO")){

            String url = "https://cuyesgyg.com/intranet/webservices/actualizar_reproductor.php?codigo="+codigo_re.getText().toString()+"&poza="+poza_re.getText().toString()
                    +"&raza="+recu_raza+"&sexo="+recu_sexo+"&fecha_empadre="+fecha_mysql+"&estado="+recu_estado+"&fecha_c_estado="+fecha_mysql2;

            jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
            request.add(jsonObjectRequest);

            poza_re.setText("");
            fecha_empadre_re.setText("");
            fecha_c_estado_re.setText("");
            sexo_re.setSelection(0);
            raza_re.setSelection(0);
            estado_re.setSelection(0);
            codigo_re.setText("");
            codigo_re.requestFocus();

            actualizar_re.setEnabled(false);
            guardar_re.setEnabled(true);
            buscar_re.setEnabled(true);
            limpiar_re.setEnabled(false);
            aux=2;
        }
        else
            new cuadro_dialogo(getContext(), "Debes ingresar todo los datos");
    }


    private void buscarwebservices() {
        if(!codigo_re.getText().toString().equalsIgnoreCase("")){
            String url = "https://cuyesgyg.com/intranet/webservices/buscar_reproductor.php?codigo="+codigo_re.getText().toString();
            jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
            request.add(jsonObjectRequest);
            aux=1;

            buscar_re.setEnabled(false);
            guardar_re.setEnabled(false);
            actualizar_re.setEnabled(true);
            limpiar_re.setEnabled(true);
        }
        else
            new cuadro_dialogo(getContext(), "Debes ingresar el codigo");
    }

    private void cargarwebservices() {
        SimpleDateFormat inSDF = new SimpleDateFormat("dd/mm/yyyy");
        SimpleDateFormat outSDF = new SimpleDateFormat("yyyy-mm-dd");

        fecha_recu=fecha_empadre_re.getText().toString();
        String fecha_mysql = "";

        if(!fecha_recu.equalsIgnoreCase("")){

            try {
                Date date = inSDF.parse(fecha_recu);
                fecha_mysql = outSDF.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        recu_sexo=sexo_re.getSelectedItem().toString();
        recu_raza=raza_re.getSelectedItem().toString();
        recu_estado=estado_re.getSelectedItem().toString();


        if (!codigo_re.getText().toString().equalsIgnoreCase("") && !recu_sexo.equalsIgnoreCase("SEXO") && !recu_raza.equalsIgnoreCase("RAZA") && !fecha_mysql.equalsIgnoreCase("") &&
                !poza_re.getText().toString().equalsIgnoreCase("") && !recu_estado.equalsIgnoreCase("ESTADO") ) {

            String url = "https://cuyesgyg.com/intranet/webservices/registrar_reproductores.php?poza="+poza_re.getText().toString()+"&raza="+recu_raza+
                    "&sexo="+recu_sexo+"&fecha_empadre="+fecha_mysql+"&codigo="+codigo_re.getText().toString()+"&estado="+recu_estado;

            jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
            request.add(jsonObjectRequest);
        }
        else{
            new cuadro_dialogo(getContext(), "Debe ingresar todo los datos");
        }
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        if (aux==2){
            poza_re.setText("");
            fecha_empadre_re.setText("");
            fecha_c_estado_re.setText("");
            sexo_re.setSelection(0);
            raza_re.setSelection(0);
            estado_re.setSelection(0);
            codigo_re.setText("");
            codigo_re.requestFocus();
            Toast.makeText(getContext(),"Se actualizo los datos correctamente", Toast.LENGTH_SHORT).show();
            aux=0;
        }
        else{
            new cuadro_dialogo(getContext(), "No de pudo conectar con el servidor");
        }
    }

    @Override
    public void onResponse(JSONObject response) {

        if(aux==1){
            Buscar_reproductor miReproductor = new Buscar_reproductor();
            JSONArray json = response.optJSONArray("b_reproductor");
            JSONObject jsonObject=null;

            try {

                jsonObject=json.getJSONObject(0);
                miReproductor.setPoza(jsonObject.optString("ePoza"));
                miReproductor.setRaza(jsonObject.optString("cRaza"));
                miReproductor.setSexo(jsonObject.optString("cSexo"));
                miReproductor.setFecha_empadre(jsonObject.optString("fFecha_empadre"));
                miReproductor.setEstado(jsonObject.optString("cEstado"));
                miReproductor.setFecha_c_estado(jsonObject.optString("fFecha_fin_empadre"));

                poza_re.setText(miReproductor.getPoza());

                SimpleDateFormat inSDF = new SimpleDateFormat("yyyy-mm-dd");
                SimpleDateFormat outSDF = new SimpleDateFormat("dd/mm/yyyy");
                Date date = null;
                Date date2= null;
                String fecha_nac="";
                String fecha_c_estadin="";
                try {
                    date = inSDF.parse(miReproductor.getFecha_empadre());

                    if (!miReproductor.getFecha_c_estado().equalsIgnoreCase(null)){
                        date2 = inSDF.parse(miReproductor.getFecha_c_estado());
                        fecha_c_estadin = outSDF.format(date2);
                    }


                } catch (ParseException e) {
                    e.printStackTrace();
                }
                fecha_nac = outSDF.format(date);


                fecha_empadre_re.setText(fecha_nac);
                fecha_c_estado_re.setText(fecha_c_estadin);

                int val=0;
                switch(miReproductor.getRaza()){
                    case "ANDINA":
                        val = 1;
                        break;
                    case "CALIFORNIA":
                        val=2;
                        break;
                    case "INTI":
                        val=3;
                        break;
                    case "MANTARO":
                        val=4;
                        break;
                    case "PERU":
                        val=5;
                        break;
                }
                raza_re.setSelection(val);

                val=0;
                switch(miReproductor.getSexo()){
                    case "HEMBRA":
                        val = 1;
                        break;
                    case "MACHO":
                        val=2;
                        break;
                }
                sexo_re.setSelection(val);

                val=0;
                switch(miReproductor.getEstado()){
                    case "MUERTO":
                        val = 1;
                        break;
                    case "VIVO":
                        val=2;
                        break;
                    case "RETIRADO":
                        val=3;
                        break;
                    case "TRASLADADO":
                        val=4;
                        break;

                }
                estado_re.setSelection(val);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            aux=0;
        }

        else{
            Toast.makeText(getContext(),"Se ha registrado exitosamente", Toast.LENGTH_SHORT).show();
            poza_re.setText("");
            fecha_empadre_re.setText("");
            fecha_c_estado_re.setText("");
            sexo_re.setSelection(0);
            raza_re.setSelection(0);
            estado_re.setSelection(0);
            //foto.setImageDrawable(null);
            codigo_re.setText("");
            codigo_re.requestFocus();
        }


    }



}
