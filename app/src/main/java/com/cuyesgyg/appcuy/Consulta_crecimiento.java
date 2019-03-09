package com.cuyesgyg.appcuy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Consulta_crecimiento extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    Button consultar;
    Spinner sexo, poza;
    TextView msncantidad, cantidad, msnfecha_crec, fecha_crec, msnfechaengorde, fecha_engorde, msnfechapeso, fecha_peso;
    String recu_poza, recu_sexo, f_resultado, fcrec_fecha, fecha_resulpe;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_crecimiento);

        consultar = (Button)(findViewById(R.id.btnconsultar_crec));
        sexo = (Spinner)(findViewById(R.id.spnsexo_rcre));
        poza = (Spinner) (findViewById(R.id.spnpoza_rcre));
        msncantidad = (TextView)(findViewById(R.id.txtmsn_cant));
        cantidad = (TextView)(findViewById(R.id.txtcantidad));
        msnfecha_crec = (TextView)(findViewById(R.id.txtmsnbalanceado_crec));
        fecha_crec = (TextView)(findViewById(R.id.txtbalanceado_crec));
        msnfechaengorde = (TextView)(findViewById(R.id.txtmsnengorde));
        fecha_engorde = (TextView)(findViewById(R.id.txtengorde));
        msnfechapeso = (TextView)(findViewById(R.id.txtmsnfechapeso));
        fecha_peso = (TextView)(findViewById(R.id.txtfechapeso));

        request= Volley.newRequestQueue(getBaseContext());

        consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultarwebservices();

            }
        });

    }

    private void consultarwebservices() {

        msncantidad.setText("");
        cantidad.setText("");
        msnfecha_crec.setText("");
        fecha_crec.setText("");
        msnfechaengorde.setText("");
        fecha_engorde.setText("");
        msnfechapeso.setText("");
        fecha_peso.setText("");

        recu_poza = poza.getSelectedItem().toString();
        recu_sexo = sexo.getSelectedItem().toString();

        if (!recu_poza.equalsIgnoreCase("POZA") && !recu_sexo.equalsIgnoreCase("SEXO") ){
            String url = "https://cuyesgyg.com/intranet/webservices/consultar_crecimiento.php?sexo="+recu_sexo+
                    "&poza="+recu_poza;

            jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
            request.add(jsonObjectRequest);
        }
        else {
            Toast.makeText(getBaseContext(),"Debe ingresar el SEXO y POZA", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onResponse(JSONObject response) {

        consu_crecimiento miConsulta = new consu_crecimiento();
        JSONArray json = response.optJSONArray("r_crecimiento");
        JSONObject jsonObject=null;

        try{
            jsonObject=json.getJSONObject(0);
            miConsulta.setCant_crecimiento(jsonObject.optString("cantidad"));
            miConsulta.setFecha_inicio(jsonObject.optString("fFecha_inicio"));
            miConsulta.setFecha_peso(jsonObject.optString("fFecha_peso"));
            miConsulta.setPeso(jsonObject.optString("ePeso"));
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        f_resultado = miConsulta.getCant_crecimiento();
        fcrec_fecha = miConsulta.getFecha_inicio();
        fecha_resulpe = miConsulta.getFecha_peso();

        msncantidad.setText("LA CANTIDAD DE CUYES ES : ");
        cantidad.setText(f_resultado);

        if(!fcrec_fecha.equalsIgnoreCase("") && !f_resultado.equalsIgnoreCase("0")){
            msnfecha_crec.setText("BALANCEADO - RECRIA");
            msnfechaengorde.setText("BALANCEADO - ENGORDE");
            msnfechapeso.setText("ULTIMO CONTROL");

            SimpleDateFormat inSDF = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat outSDF = new SimpleDateFormat("dd/MM/yyyy");


            String fecha_mysql = "";
            String fecha_mysql2="";

            try {
                Date date = inSDF.parse(fcrec_fecha);
                fecha_mysql = outSDF.format(date);
                fecha_crec.setText(fecha_mysql);

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DATE,14);
                fecha_crec.setText(fecha_mysql+ " AL "+outSDF.format(calendar.getTime()) );

                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(date);
                calendar2.add(Calendar.DATE,44);

                Calendar calendar3 = Calendar.getInstance();
                calendar3.setTime(date);
                calendar3.add(Calendar.DATE,15);

                fecha_engorde.setText(outSDF.format(calendar3.getTime())+ " AL "+outSDF.format(calendar2.getTime()) );

                Date date2 = inSDF.parse(fecha_resulpe);
                fecha_mysql2 = outSDF.format(date2);

                fecha_peso.setText("FECHA: "+fecha_mysql2+" PESO: "+miConsulta.getPeso()+" gr");

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getBaseContext(), "No hay conexion con el servidor", Toast.LENGTH_SHORT).show();
    }
}
