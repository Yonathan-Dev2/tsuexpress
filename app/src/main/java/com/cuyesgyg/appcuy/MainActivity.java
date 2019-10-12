package com.cuyesgyg.appcuy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    String v_login, v_clave, v_estado, v_correo, v_nomb_apel, v_cargo;
    CheckBox recordar;
    public static final String STRING_PREFERENCES = "DatosUsuario";
    public static final String PREFERENCES_ESTADO_SESION = "EstadoSesion";
    Boolean reachable;


    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    String link = "https://cuyesgyg.com/intranet/webservices/consultar_usuarios.php?";

    Context contexto;

    ProgressDialog pdp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //isOnlineNet();

        if(obtenersesion()){
            Intent i = new Intent(getApplicationContext(), menu.class);
            startActivity(i);
            contexto = this;
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
                //isOnlineNet();

                if (isNetDisponible()) {
                    cargarwebservices();
                } else {
                    Toast.makeText(getBaseContext(), "No hay conexion a Internet.",Toast.LENGTH_SHORT).show();
                    //new cuadro_dialogo(getBaseContext(),"No hay conexión a Internet en este momento");
                }

                }
        });

        cargarpreferencias();

    }


    private boolean isNetDisponible() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();

        return (actNetInfo != null && actNetInfo.isConnected());
    }

    private void cargarwebservices() {
            String url = link+"login="+login.getText().toString()+"&clave="+clave.getText().toString();

            jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
            request.add(jsonObjectRequest);

            pdp = new ProgressDialog(this);
            pdp.show();
            pdp.setContentView(R.layout.progressbar);
            pdp.setCancelable(false);
            pdp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void cargarpreferencias() {
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);

        String user = preferences.getString("log","");
        String pass = preferences.getString("pass","");
        String nom_ape = preferences.getString("nom_ape","");
        String corr = preferences.getString("corr","");
        String carg = preferences.getString("car","");
        Boolean check = preferences.getBoolean("check",false);

        login.setText(user);
        clave.setText(pass);
        recordar.setChecked(check);
    }

    private void guardarpreferencia() {
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        String usuario = login.getText().toString();
        String contraseña = clave.getText().toString();
        String nombres_apellidos = v_nomb_apel;
        String correo = v_correo;
        String cargo = v_cargo;
        Boolean mar = recordar.isChecked();

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("log", usuario);
        editor.putString("pass", contraseña);
        editor.putString("corr",correo);
        editor.putString("nom_ape",nombres_apellidos);
        editor.putString("car",cargo);
        editor.putBoolean("check",mar);

        editor.commit();

    }

    public void guardarsesion(){
        SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCES, Context.MODE_PRIVATE);
        preferences.edit().putBoolean(PREFERENCES_ESTADO_SESION,true).apply();
    }

    public boolean obtenersesion(){
        SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCES, MODE_PRIVATE);
        return preferences.getBoolean(PREFERENCES_ESTADO_SESION,false);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        pdp.dismiss();

        if(recordar.isChecked()==true){
            guardarpreferencia();
        }

        new cuadro_dialogo(contexto, "No se puede conectar con el servidor");
        ingresar.setEnabled(true);
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
            miUsuario.setCorreo(jsonObject.optString("cCorreo"));
            miUsuario.setCargo(jsonObject.optString("cCargo"));
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        v_clave=miUsuario.getClave();
        v_login=miUsuario.getLogin();
        v_correo=miUsuario.getCorreo();
        v_nomb_apel=miUsuario.getNombres()+" "+miUsuario.getApe_paterno()+" "+miUsuario.getApe_materno();
        v_cargo = miUsuario.getCargo();
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

            new cuadro_dialogo(contexto, "El usuario y/o contraseña son incorrectos");

        }

        pdp.dismiss();
    }


    public Boolean isOnlineNet() {


        reachable  = false;
        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
            int val = p.waitFor();
            if (val==1){
                reachable = true;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return reachable;
    }


}
