package com.cuyesgyg.appcuy;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class registrar_reproductores extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener {

    private static final String CARPETA_PRINCIPAL="misImagenesApp";
    private static final String CARPETA_IMAGEN="imagenes";
    private static final String DIRECTORIO_IMAGEN=CARPETA_PRINCIPAL+CARPETA_IMAGEN;
    private String path;
    File fileImagen;
    Bitmap bitmap;



    private static final int COD_SELECCIONA = 10;
    private static final int COD_FOTO = 20;

    EditText poza_re, fecha_empadre_re, codigo_re, fecha_c_estado_re;
    Button guardar_re, buscar_re, actualizar_re, limpiar_re;
    //Button capturar;
    Spinner sexo_re, raza_re, estado_re;
    //ImageView foto;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    private String v_login;

    String recu_sexo, recu_raza, fecha_recu, recu_estado, fecha_c_recuestado;
    int aux=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_cuyes);

        codigo_re = (EditText)(findViewById(R.id.edtcodigo_re));
        poza_re = (EditText)(findViewById(R.id.edtpoza_re));
        raza_re = (Spinner) (findViewById(R.id.spnraza_re));
        sexo_re=(Spinner) (findViewById(R.id.spnsexo_re));
        fecha_empadre_re = (EditText)(findViewById(R.id.edtfecha_empadre_re));
        estado_re = (Spinner)(findViewById(R.id.spnestado_cuy_re));
        fecha_c_estado_re = (EditText)(findViewById(R.id.edtfecha_c_estado_re));

        v_login = String.valueOf(getIntent().getExtras().getString("parametro")) ;

        //En este layout se esta eliminando dos controles que en caso se desee colocar son imgfoto y btncapturar
        //capturar=(Button)(findViewById(R.id.btncapturar));
        //foto = (ImageView)(findViewById(R.id.imgfoto));

        guardar_re=(Button)(findViewById(R.id.btnguardar_re));
        buscar_re = (Button)(findViewById(R.id.btnbuscar_re));
        actualizar_re = (Button)(findViewById(R.id.btnactualizar_re));
        limpiar_re = (Button)(findViewById(R.id.btnlimpiar_re));

        codigo_re.requestFocus();


        request= Volley.newRequestQueue(getBaseContext());

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


        /*capturar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoopciones();
            }
        });*/



    }


    /*private void mostrarDialogoopciones() {

        final CharSequence[] opciones = {"Tomar foto","Elegir de Galeria","Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Elige una opcion");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar foto")){
                    abrircamara();
                }
                else
                {
                    if (opciones[i].equals("Elegir de Galeria")) {
                        Intent intent= new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent,"Seleccione"),COD_SELECCIONA);
                    }
                    else{
                            dialogInterface.dismiss();
                        }
                }

            }
        }) ;
        builder.show();
    }
*/
    /*private void abrircamara() {
        File miFile= new File(Environment.getExternalStorageDirectory(), DIRECTORIO_IMAGEN );
        boolean isCreada = miFile.exists();

        if(isCreada==false){
            isCreada=miFile.mkdirs();

            Long consecutivo = System.currentTimeMillis()/1000;
            String nombre = consecutivo.toString()+".jpg";

            path=Environment.getExternalStorageDirectory()+File.separator+DIRECTORIO_IMAGEN
                    +File.separator+nombre;

            fileImagen=new File(path);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);

            intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(fileImagen));
            startActivityForResult(intent.createChooser(intent,"Seleccione"), COD_FOTO);
        }


        if(isCreada==true){
            Long consecutivo = System.currentTimeMillis()/1000;
            String nombre = consecutivo.toString()+".jpg";

            path=Environment.getExternalStorageDirectory()+File.separator+DIRECTORIO_IMAGEN
                    +File.separator+nombre;

            fileImagen=new File(path);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(fileImagen));
            startActivityForResult(intent.createChooser(intent,"Seleccione"), COD_FOTO);

        }

    }
*/

/*    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){

            case COD_SELECCIONA:
                Uri miPath=data.getData();
                foto.setImageURI(miPath);

                break;

            case COD_FOTO:
                MediaScannerConnection.scanFile(getBaseContext(), new String[]{path}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("Path",""+path);
                            }
                        }
                );
                bitmap= BitmapFactory.decodeFile(path);
                foto.setImageBitmap(bitmap);

                /*foto.buildDrawingCache();
                Bitmap bmap = foto.getDrawingCache();
                foto.setImageBitmap(bmap);*/

                //break;
                  //          }
    //}


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
        //foto.buildDrawingCache();
        //Bitmap bmap = foto.getDrawingCache();
        //String imagen = convertirImgString(bmap);


        if (!codigo_re.getText().toString().equalsIgnoreCase("") && !recu_sexo.equalsIgnoreCase("SEXO") && !recu_raza.equalsIgnoreCase("RAZA") && !fecha_mysql.equalsIgnoreCase("") &&
        !poza_re.getText().toString().equalsIgnoreCase("") && !recu_estado.equalsIgnoreCase("ESTADO") ) {

        String url = "https://cuyesgyg.com/intranet/webservices/registrar_reproductores.php?poza="+poza_re.getText().toString()+"&raza="+recu_raza+
                  "&sexo="+recu_sexo+"&fecha_empadre="+fecha_mysql+"&codigo="+codigo_re.getText().toString()+"&estado="+recu_estado;

          jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
           request.add(jsonObjectRequest);
        }
        else{
          Toast.makeText(getBaseContext(),"Debe ingresar todo los datos", Toast.LENGTH_SHORT).show();

        }

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
            Toast.makeText(getBaseContext(),"Debes ingresar el codigo", Toast.LENGTH_SHORT).show();

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
          Toast.makeText(getBaseContext(),"Debes ingresar todo los datos", Toast.LENGTH_SHORT).show();

    }


    private String convertirImgString(Bitmap bitmap) {
          ByteArrayOutputStream array = new ByteArrayOutputStream();
          bitmap.compress(Bitmap.CompressFormat.JPEG,100,array);
          byte[] imagenByte=array.toByteArray();
          String imagenString = Base64.encodeToString(imagenByte,Base64.DEFAULT);

          return imagenString;
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
            Toast.makeText(getBaseContext(),"Se actualizo los datos correctamente", Toast.LENGTH_SHORT).show();
            aux=0;
        }
        else{
            Toast.makeText(getBaseContext(),"No se pudo conectar con el servidor "+error.toString(), Toast.LENGTH_SHORT).show();
            Log.i("Error",error.toString());
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
            Toast.makeText(getBaseContext(),"Se ha registrado exitosamente", Toast.LENGTH_SHORT).show();
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
