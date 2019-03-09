package com.cuyesgyg.appcuy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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


public class MainActivity extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener{

    Button ingresar;
    EditText login, clave;
    String v_login, v_clave, v_estado;
    CheckBox recordar;
    public static final String STRING_PREFERENCES = "DatosUsuario";
    public static final String PREFERENCES_ESTADO_SESION = "EstadoSesion";

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    private ProgressDialog pd = null;
    String link = "https://cuyesgyg.com/intranet/webservices/consultar_usuarios.php?";


    Context contexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contexto = this;

        if(obtenersesion()){
            Intent i = new Intent(getApplicationContext(), menu.class);
            startActivity(i);
            finish();
        }

        ingresar=(Button)findViewById(R.id.btningresar);
        login=(EditText)findViewById(R.id.edtlogin);
        clave=(EditText)findViewById(R.id.edtpassword);
        recordar = (CheckBox)findViewById(R.id.chkrecordar);

        request= Volley.newRequestQueue(getBaseContext());

        ingresar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cargarwebservices();
                }
        });

        cargarpreferencias();

    }


    private void cargarwebservices() {
            String url = link+"login="+login.getText().toString()+"&clave="+clave.getText().toString();

            jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
            request.add(jsonObjectRequest);

            this.pd = ProgressDialog.show(this, "Procesando", "Espere unos segundos...", true,false);
    }

    private void cargarpreferencias() {
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);

        String user = preferences.getString("user","");
        String pass = preferences.getString("pass","");
        Boolean check = preferences.getBoolean("check",false);

        login.setText(user);
        clave.setText(pass);
        recordar.setChecked(check);

    }

    private void guardarpreferencia() {
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        String usuario = login.getText().toString();
        String contraseña = clave.getText().toString();
        Boolean mar = recordar.isChecked();

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user", usuario);
        editor.putString("pass", contraseña);
        editor.putBoolean("check",mar);

        editor.commit();

    }

    public void guardarsesion(){
        SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCES, Context.MODE_PRIVATE);
        //preferences.edit().putBoolean(PREFERENCES_ESTADO_SESION,recordar.isChecked()).apply();
        preferences.edit().putBoolean(PREFERENCES_ESTADO_SESION,true).apply();
    }

    public boolean obtenersesion(){
        SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCES, MODE_PRIVATE);
        return preferences.getBoolean(PREFERENCES_ESTADO_SESION,false);
    }


    @Override
    public void onErrorResponse(VolleyError error) {

        if(recordar.isChecked()==true){
            guardarpreferencia();
        }

        new cuadro_dialogo(contexto, "No se puede conectar con el servidor");
        Log.i("Error",error.toString());
        ingresar.setEnabled(true);
        pd.dismiss();

    }

    @Override
    public void onResponse(JSONObject response) {

        Usuario miUsuario = new Usuario();
        JSONArray json = response.optJSONArray("usuarios");
        JSONObject jsonObject=null;

        try{
            jsonObject=json.getJSONObject(0);
            miUsuario.setLogin(jsonObject.optString("cLogin"));
            miUsuario.setClave(jsonObject.optString("cClave"));
            miUsuario.setEstado(jsonObject.optString("cEstado"));
            miUsuario.setApe_paterno(jsonObject.optString("cApe_paterno"));
            miUsuario.setApe_materno(jsonObject.optString("cAp_materno"));
            miUsuario.setNombres(jsonObject.optString("cNombres"));

        }
        catch (JSONException e){
            e.printStackTrace();
        }

        v_clave=miUsuario.getClave();
        v_login=miUsuario.getLogin();
        v_estado=miUsuario.getEstado();

        if(v_estado.equalsIgnoreCase("Activo")){

            guardarsesion();//Guarda el estado de sesion

            if(recordar.isChecked()==true){
                guardarpreferencia();
            }

            else{
                login.setText("");
                clave.setText("");
                login.requestFocus();
                guardarpreferencia();
            }

            Intent i = new Intent(getApplicationContext(), menu.class);
            i.putExtra("parametro", v_login);//Pasamos el login para identificar al usuario
            i.putExtra("ape_paterno", miUsuario.getApe_paterno());
            i.putExtra("ape_materno", miUsuario.getApe_materno());
            i.putExtra("nombres", miUsuario.getNombres());
            startActivity(i);
            finish();

        }

        else {

            if(recordar.isChecked()==true){
                guardarpreferencia();
            }
            else{
                login.setText("");
                clave.setText("");
                login.requestFocus();
                ingresar.setEnabled(true);
                guardarpreferencia();
            }

            pd.dismiss();

            new cuadro_dialogo(contexto, "El usuario y/o contraseña son incorrectos");

        }
    }
}
