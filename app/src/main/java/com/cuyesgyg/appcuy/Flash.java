package com.cuyesgyg.appcuy;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class Flash extends AppCompatActivity {

    Button btn_flash;
    Camera camara;
    Camera.Parameters parametros;

    boolean isflash = false;
    boolean  isOn = false;



    @Override
    protected void onStop(){
        super.onStop();
        if (camara!=null){
            camara.release();
            camara=null;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED&&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA,},1000);
        }

        if (getBaseContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){
            camara = Camera.open();
            parametros = camara.getParameters();
            isflash = true;
        } else {
            Toast.makeText(getBaseContext(),"NO es compatible en FLASH",Toast.LENGTH_SHORT).show();
        }


        btn_flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isflash) {
                    if (!isOn) {
                        parametros.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        camara.setParameters(parametros);
                        camara.startPreview();
                        isOn=true;
                    }
                } else {
                    parametros.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    camara.setParameters(parametros);
                    camara.stopPreview();
                    isOn=false;
                }
            }
        });

    }
}
