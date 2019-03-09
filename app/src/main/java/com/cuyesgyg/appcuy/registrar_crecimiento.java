package com.cuyesgyg.appcuy;

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

public class registrar_crecimiento extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener {

    EditText poza, cantidad, fecha_ini, fecha_fin, peso;
    Button buscar, actualizar, guardar, limpiar;
    Spinner sexo;
    String recu_sexo, fecha_recu_fin, recu_poza, fecha_recu_ini, recu_cantidad;
    int aux;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    private String v_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_crecimiento);

        poza = (EditText)(findViewById(R.id.edtpoza_crec));
        sexo = (Spinner) (findViewById(R.id.spnsexo_crec));
        cantidad = (EditText)(findViewById(R.id.edtcantidad_crec));
        fecha_ini = (EditText)(findViewById(R.id.edtfechaini_crec));
        fecha_fin = (EditText)(findViewById(R.id.edtfechafin_crec));
        peso = (EditText)(findViewById(R.id.edtpeso));
        buscar  = (Button)(findViewById(R.id.btnbuscar_crec));
        actualizar = (Button)(findViewById(R.id.btnactualiza_crec));
        guardar = (Button)(findViewById(R.id.btnguardar_crec));
        limpiar = (Button)(findViewById(R.id.btnlimpiar_crec));

        request= Volley.newRequestQueue(getBaseContext());

        fecha_fin.setEnabled(false);
        fecha_ini.setEnabled(false);

        actualizar.setEnabled(false);
        limpiar.setEnabled(false);
        aux=0;
        recu_sexo="";

        v_login = String.valueOf(getIntent().getExtras().getString("parametro")) ;

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
            Toast.makeText(getBaseContext(),"Debes ingresar todo los datos", Toast.LENGTH_SHORT).show();

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
            Toast.makeText(getBaseContext(),"Debes ingresar los datos necesarios", Toast.LENGTH_SHORT).show();
            poza.setEnabled(true);
            sexo.setEnabled(true);
        }

    }

    private void guardarwebservices() {

        recu_sexo=sexo.getSelectedItem().toString();

        String fecha_inicio_r=null;
        String fecha_peso = null;

        Date fecha = new Date();
        SimpleDateFormat outSDF = new SimpleDateFormat("yyyy-MM-dd");
        fecha_inicio_r = outSDF.format(fecha);

        SimpleDateFormat inSDF = new SimpleDateFormat("dd/mm/yyyy");
        SimpleDateFormat ouSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        SimpleDateFormat oSDF = new SimpleDateFormat("yyyy-mm-dd");

        fecha_peso = ouSDF.format(fecha);

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
            String url = "https://cuyesgyg.com/intranet/webservices/registrar_crecimiento.php?poza="+poza.getText().toString()+"&sexo="+recu_sexo+
                    "&cantidad="+cantidad.getText().toString()+"&fecha_inicio="+fecha_inicio_r+"&fecha_fin="+fecha_ar+"&peso="+peso.getText().toString()+
                     "&fecha_peso="+fecha_peso+"&usuario="+v_login;

            guardar.setEnabled(false);

            jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
            request.add(jsonObjectRequest);

        }

        else{
            Toast.makeText(getBaseContext(),"Debe ingresar todo los datos", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onErrorResponse(VolleyError error) {

        if (aux==2){
            poza.setText("");
            fecha_fin.setText("");
            fecha_ini.setText("");
            sexo.setSelection(0);
            cantidad.setText("");
            peso.setText("");
            poza.requestFocus();
            Toast.makeText(getBaseContext(),"Se actualizo los datos correctamente", Toast.LENGTH_SHORT).show();
            aux=0;
        }
        else {
            if (aux == 1) {
                Toast.makeText(getBaseContext(), "La poza no cuenta con cuyes en RECRIA ", Toast.LENGTH_SHORT).show();
                buscar.setEnabled(true);
                limpiar.setEnabled(true);
                actualizar.setEnabled(false);
            }
             else{
                    Toast.makeText(getBaseContext(), "No se pudo conectar con el servidor " + error.toString(), Toast.LENGTH_SHORT).show();
                    Log.i("Error", error.toString());
                    buscar.setEnabled(true);
                    limpiar.setEnabled(false);
                    actualizar.setEnabled(false);
                    aux = 0;
                }
            }
    }

    @Override
    public void onResponse(JSONObject response) {

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

                Toast.makeText(getBaseContext(),"Se ha registrado exitosamente", Toast.LENGTH_SHORT).show();
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

}
