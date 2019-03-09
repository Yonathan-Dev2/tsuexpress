package com.cuyesgyg.appcuy;

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

import com.cuyesgyg.appcuy.Fragmentos.Fragmento_crias;
import com.cuyesgyg.appcuy.Fragmentos.Fragmento_recria;
import com.cuyesgyg.appcuy.Fragmentos.Fragmento_reproductor;

public class menu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String v_login;
    private String v_ape_paterno;
    private String v_ape_materno;
    private String v_nombres;

    public static final String STRING_PREFERENCES = "DatosUsuario";
    public static final String PREFERENCES_ESTADO_SESION = "EstadoSesion";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /*v_login = getIntent().getExtras().getString("parametro"); // Recepciona el login del Main principal
        v_ape_paterno = getIntent().getExtras().getString("ape_paterno");
        v_ape_materno = getIntent().getExtras().getString("ape_materno");
        v_nombres = getIntent().getExtras().getString("nombres");*/

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

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
            // Handle the camera action
            /*Intent i = new Intent(getApplicationContext(), registrar_reproductores.class);
            i.putExtra("parametro", v_login);//Pasamos el login para identificar al usuario
            startActivity(i);*/
            CargarFragmento(new Fragmento_reproductor());


        } else if (id == R.id.mencrecimiento) {
            /*Intent i = new Intent(getApplicationContext(),registrar_crecimiento.class);
            i.putExtra("parametro", v_login);//Pasamos el login para identificar al usuario
            startActivity(i);*/

            CargarFragmento(new Fragmento_recria());

        } else if (id == R.id.meningresos) {
            Intent i = new Intent(getApplicationContext(),ingresos.class);
            i.putExtra("parametro", v_login);//Pasamos el login para identificar al usuario
            startActivity(i);

        } else if (id == R.id.mengastos) {
            Intent i = new Intent(getApplicationContext(),gastos.class);
            i.putExtra("parametro", v_login);//Pasamos el login para identificar al usuario
            startActivity(i);

        }else if (id == R.id.mentermohigrometro) {
            Intent i = new Intent(getApplicationContext(),registrar_termohigrometro.class);
            i.putExtra("parametro", v_login);//Pasamos el login para identificar al usuario
            startActivity(i);
        }
        else if (id == R.id.mencontrol_nacimientos) {
            /*Intent i = new Intent(getApplicationContext(),registrar_crias.class);
            i.putExtra("parametro", v_login);//Pasamos el login para identificar al usuario
            startActivity(i);*/

            CargarFragmento(new Fragmento_crias());
        }
        else if (id == R.id.consu_reproductores) {
            Intent i = new Intent(getApplicationContext(), consu_reproductores.class);
            i.putExtra("parametro", v_login);//Pasamos el login para identificar al usuario
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
            i.putExtra("parametro", v_login);//Pasamos el login para identificar al usuario
            startActivity(i);
        } else if (id == R.id.consu_control_nacimientos) {
            Intent i = new Intent(getApplicationContext(), reporte_nacimientos.class);
            i.putExtra("parametro", v_login);//Pasamos el login para identificar al usuario
            startActivity(i);
        } else if (id == R.id.venta_cuyes) {
            Intent i = new Intent(getApplicationContext(), venta_cuyes.class);
            i.putExtra("parametro", v_login);//Pasamos el login para identificar al usuario
            startActivity(i);
        } else if (id == R.id.meninventario) {
            Intent i = new Intent(getApplicationContext(), inventario.class);
            i.putExtra("parametro", v_login);//Pasamos el login para identificar al usuario
            startActivity(i);
        } else if (id == R.id.consu_total_cuyes) {
            Intent i = new Intent(getApplicationContext(), reporte_total.class);
            i.putExtra("parametro", v_login);//Pasamos el login para identificar al usuario
            startActivity(i);
        }
        else if (id == R.id.menforraje) {
            Intent i = new Intent(getApplicationContext(), registrar_forraje.class);
            i.putExtra("parametro", v_login);//Pasamos el login para identificar al usuario
            startActivity(i);
        }
        else if (id == R.id.cerrar_sesion) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            guardarsesion();
            finish();
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


}
