package com.cuyesgyg.appcuy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class consu_comercializacion extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener {

    Spinner mes, ano;
    TextView subtotalingreso, subtotalegreso, total, txtingreso, txtgasto, txttotal, capital, msncapital;
    Button comercializar;
    String recu_mes, recu_ano;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consu_comercializacion);

        mes = (Spinner)(findViewById(R.id.spnmes));
        ano = (Spinner)(findViewById(R.id.spnano_comer));
        subtotalingreso = (TextView)(findViewById(R.id.txtingreso));
        subtotalegreso = (TextView)(findViewById(R.id.txtegreso));
        total = (TextView)(findViewById(R.id.txttotal));
        comercializar  = (Button)(findViewById(R.id.btnconsultarcomercializacion));
        txtingreso = (TextView)(findViewById(R.id.txting));
        txtgasto = (TextView)(findViewById(R.id.txtgas));
        txttotal = (TextView)(findViewById(R.id.txttotales));
        capital = (TextView)(findViewById(R.id.txtcapital));
        msncapital = (TextView)(findViewById(R.id.txtmsncapital));


        request= Volley.newRequestQueue(getBaseContext());

        comercializar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarwebservices();
            }
        });

    }

    private void cargarwebservices() {

        recu_mes=mes.getSelectedItem().toString();
        recu_ano=ano.getSelectedItem().toString();

        if (!recu_ano.equalsIgnoreCase("AÑO") && !recu_mes.equalsIgnoreCase("MES")){
            String url = "https://cuyesgyg.com/intranet/webservices/consultar_comercializacion.php?mes="+recu_mes+"&ano="+recu_ano;

            jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
            request.add(jsonObjectRequest);
        }
        else{
            Toast.makeText(getBaseContext(),"Debes ingresar el AÑO y MES", Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getBaseContext(),"No se pudo conectar con sel servidor "+error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("Error",error.toString());

        subtotalegreso.setText("");
        subtotalingreso.setText("");
        total.setText("");
        mes.setSelection(0);
        ano.setSelection(0);
        msncapital.setText("");
        capital.setText("");
        txtingreso.setText("");
        txtgasto.setText("");
        txttotal.setText("");

    }

    @Override
    public void onResponse(JSONObject response) {

        Consulta_comercializacion miConsulta = new Consulta_comercializacion();

        JSONArray json = response.optJSONArray("comercializacion");
        JSONObject jsonObject=null;


        try{

            jsonObject=json.getJSONObject(0);
            miConsulta.setCapital(jsonObject.optString("Capital"));
            miConsulta.setGasto(jsonObject.optString("Gasto"));
            miConsulta.setIngreso(jsonObject.optString("Ingreso"));

            if (!miConsulta.getGasto().equalsIgnoreCase("0")|| !miConsulta.getIngreso().equalsIgnoreCase("0")){
                subtotalegreso.setText("");
                subtotalingreso.setText("");
                total.setText("");
                msncapital.setText("");
                capital.setText("");
                txtingreso.setText("");
                txtgasto.setText("");
                txttotal.setText("");

                txtingreso.setText("INGRESOS :");
                subtotalingreso.setText("S/. "+miConsulta.getIngreso());

                txtgasto.setText("GASTOS :");
                subtotalegreso.setText("S/. " + miConsulta.getGasto());

                txttotal.setText("BALANCE DEL MES :");
                float balance = (Float.parseFloat(miConsulta.getIngreso()))-(Float.parseFloat(miConsulta.getGasto()));
                total.setText("S/. "+balance);

                if(Float.parseFloat(miConsulta.getCapital())>0){
                    msncapital.setText("CAPITAL");
                    capital.setText("S/. "+(Float.parseFloat(miConsulta.getCapital()) +  balance));
                }
                else
                    capital.setText("S/. "+Float.parseFloat(miConsulta.getCapital()));



            }
            else{
                Toast.makeText(getBaseContext(),"No se tiene registros en el MES y AÑO", Toast.LENGTH_SHORT).show();

                subtotalegreso.setText("");
                subtotalingreso.setText("");
                total.setText("");
                msncapital.setText("");
                capital.setText("");
                txtingreso.setText("");
                txtgasto.setText("");
                txttotal.setText("");

            }


        }

        catch (JSONException e){
            e.printStackTrace();
        }

    }




    }
