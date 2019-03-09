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

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
public class gastos extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener{

    Spinner producto;
    EditText cantidad, precio, total;
    String recu_producto;
    float recu_total;
    Button guardar, calcular;
    Double resta_capital;

    private String v_login;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gastos);

        producto = (Spinner)(findViewById(R.id.spnproducto_2));
        cantidad=(EditText)(findViewById(R.id.edtcantidad));
        precio = (EditText)(findViewById(R.id.edtprecio));
        total  = (EditText)(findViewById(R.id.edttotal));
        guardar = (Button)(findViewById(R.id.btnguardar_re));
        calcular = (Button)(findViewById(R.id.btncalcular));
        cantidad.requestFocus();
        total.setEnabled(false);

        request= Volley.newRequestQueue(getBaseContext());

        v_login = String.valueOf(getIntent().getExtras().getString("parametro")) ;

        calcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculare();

            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarwebservices();
            }


        });

    }

    private void calculare() {
        if(!precio.getText().toString().equalsIgnoreCase("") && !cantidad.getText().toString().equalsIgnoreCase("")){
            recu_total=Integer.parseInt(cantidad.getText().toString())* Float.parseFloat(precio.getText().toString()) ;
            total.setText("S/. "+String.valueOf(recu_total));
        }

        else{
            Toast.makeText(getBaseContext(),"Debe ingresar todo los datos", Toast.LENGTH_SHORT).show();
        }

    }

    private void cargarwebservices() {

        String fecha_detalle=null;
        Date fecha = new Date();
        SimpleDateFormat outSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        fecha_detalle = outSDF.format(fecha);

        recu_producto=producto.getSelectedItem().toString();


        if (!recu_producto.equalsIgnoreCase("GASTOS") && !cantidad.getText().toString().equalsIgnoreCase("") && !precio.getText().toString().equalsIgnoreCase("")) {

            resta_capital = Double.parseDouble(cantidad.getText().toString())* Double.parseDouble(precio.getText().toString())*-1;

            String url = "https://cuyesgyg.com/intranet/webservices/registrar_comercializacion.php?producto="+recu_producto+"&detalle=Gastos"+
                    "&cantidad="+cantidad.getText().toString()+"&precio="+precio.getText().toString()+"&fecha_detalle="+fecha_detalle
                    +"&capital="+resta_capital;

            jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
            request.add(jsonObjectRequest);

            guardar.setEnabled(false);
        }
        else{
            Toast.makeText(getBaseContext(),"Debe ingresar todo los datos", Toast.LENGTH_SHORT).show();

        }



    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getBaseContext(),"No se pudo conectar con sel servidor "+error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("Error",error.toString());
        guardar.setEnabled(true);
    }

    @Override
    public void onResponse(JSONObject response) {

        Toast.makeText(getBaseContext(),"Se ha registrado exitosamente", Toast.LENGTH_SHORT).show();
        producto.setSelection(0);
        cantidad.setText("");
        precio.setText("");
        total.setText("");
        cantidad.requestFocus();
        guardar.setEnabled(true);

    }
}
