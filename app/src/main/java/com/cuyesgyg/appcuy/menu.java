package com.cuyesgyg.appcuy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cuyesgyg.appcuy.Fragmentos.Fragmento_capital;
import com.cuyesgyg.appcuy.Fragmentos.Fragmento_crias;
import com.cuyesgyg.appcuy.Fragmentos.Fragmento_gasto;
import com.cuyesgyg.appcuy.Fragmentos.Fragmento_indicador_total;
import com.cuyesgyg.appcuy.Fragmentos.Fragmento_ingreso;
import com.cuyesgyg.appcuy.Fragmentos.Fragmento_recria;
import com.cuyesgyg.appcuy.Fragmentos.Fragmento_reproductor;
import com.cuyesgyg.appcuy.Fragmentos.Fragmento_soporte;

public class menu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String v_login;
    private String v_nomb_apel;
    private String v_correo;
    private String v_cargo;

    public static final String STRING_PREFERENCES = "DatosUsuario";
    public static final String PREFERENCES_ESTADO_SESION = "EstadoSesion";

    TextView usario_nav, correo_nav;
    View hview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cargarpreferencias();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //*********************************************************************************************
        Fragment fragment = new Fragmento_indicador_total();
        getSupportFragmentManager().beginTransaction().add(R.id.contenedorFragmento,fragment).commit();
        //*********************************************************************************************

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        hview = navigationView.getHeaderView(0);
        usario_nav = (TextView) hview.findViewById(R.id.txtnombre_user);
        correo_nav = (TextView) hview.findViewById(R.id.txtcorreo_user);
        navigationView.setNavigationItemSelectedListener(this);

        correo_nav.setText(v_correo);
        usario_nav.setText(v_nomb_apel);

        //PERMISOS GENERALES
        navigationView.getMenu().findItem(R.id.men_ingre_capital).setVisible(false);

        if (v_cargo.equalsIgnoreCase("APO")){
            navigationView.getMenu().setGroupVisible(R.id.grupo_regi_tecn,false);
            navigationView.getMenu().setGroupVisible(R.id.grupo_repo_prod, false);
            navigationView.getMenu().setGroupVisible(R.id.grupo_repo_tecn, false);
        }
        if (v_cargo.equalsIgnoreCase("SIS")){
            navigationView.getMenu().findItem(R.id.men_ingre_capital).setVisible(true);
        }


    }

    private static final int INTERVALO = 2000;
    private long tiempoPrimerClick;
    @Override
    public void onBackPressed() {
        if (tiempoPrimerClick + INTERVALO > System.currentTimeMillis()){
            super.onBackPressed();
            finish();
            return;
        }else {
            Toast.makeText(this, "Vuelve a presionar para salir", Toast.LENGTH_SHORT).show();
        }
        tiempoPrimerClick = System.currentTimeMillis();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.menreproductores) {
            CargarFragmento(new Fragmento_reproductor());

        } else if (id == R.id.mencrecimiento) {

            CargarFragmento(new Fragmento_recria());

        } else if (id == R.id.meningresos) {

            CargarFragmento(new Fragmento_ingreso());

        } else if (id == R.id.mengastos) {
            CargarFragmento(new Fragmento_gasto());

        }else if (id == R.id.mentermohigrometro) {
            Intent i = new Intent(getApplicationContext(),registrar_termohigrometro.class);
            startActivity(i);
        }
        else if (id == R.id.mencontrol_nacimientos) {

            CargarFragmento(new Fragmento_crias());
        }
        else if (id == R.id.consu_reproductores) {
            Intent i = new Intent(getApplicationContext(), consu_reproductores.class);
            startActivity(i);

        } else if (id == R.id.consu_crecimiento) {
            Intent i = new Intent(getApplicationContext(), Consulta_crecimiento.class);
            startActivity(i);
        }else if (id == R.id.consu_estimacion) {
            Intent i = new Intent(getApplicationContext(), consulta_estimacion.class);
            i.putExtra("parametro", v_login);//Pasamos el login para identificar al usuario
            startActivity(i);
        }
        else if (id == R.id.consu_comercializacion) {
            Intent i = new Intent(getApplicationContext(), consu_comercializacion.class);
            startActivity(i);
        } else if (id == R.id.consu_control_nacimientos) {
            Intent i = new Intent(getApplicationContext(), reporte_nacimientos.class);
            startActivity(i);
        } else if (id == R.id.venta_cuyes) {
            Intent i = new Intent(getApplicationContext(), venta_cuyes.class);
            startActivity(i);
        } else if (id == R.id.meninventario) {
            Intent i = new Intent(getApplicationContext(), inventario.class);
            startActivity(i);
        } else if (id == R.id.menforraje) {
            Intent i = new Intent(getApplicationContext(), registrar_forraje.class);
            startActivity(i);
        } else if (id == R.id.men_qr) {
            Intent i = new Intent(getApplicationContext(), qr.class);
            startActivity(i);
        }
        else if (id == R.id.cerrar_sesion) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            guardarsesion();
            finish();
        }
        else if (id == R.id.inicio) {
            CargarFragmento(new Fragmento_indicador_total());
        }
        else if (id == R.id.soporte_tecnico) {
            CargarFragmento(new Fragmento_soporte());
        }
        else if (id == R.id.men_ingre_capital) {
            CargarFragmento(new Fragmento_capital());
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void CargarFragmento(Fragment fragmento){
        FragmentManager Manager = getSupportFragmentManager();
        Manager.beginTransaction().replace(R.id.contenedorFragmento,fragmento).commit();
    }

    public void guardarsesion(){
        SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCES, MODE_PRIVATE);
        preferences.edit().putBoolean(PREFERENCES_ESTADO_SESION,false).apply();
    }

    private void cargarpreferencias() {
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);

        String user = preferences.getString("log","");
        String pass = preferences.getString("pass","");
        String nom_ape = preferences.getString("nom_ape","");
        String corr = preferences.getString("corr","");
        String carg = preferences.getString("car","");
        Boolean check = preferences.getBoolean("check",false);

        v_nomb_apel = nom_ape;
        v_correo = corr;
        v_cargo = carg;
    }


}
