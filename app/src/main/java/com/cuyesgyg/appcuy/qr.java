package com.cuyesgyg.appcuy;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.webkit.URLUtil;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class qr extends AppCompatActivity {

    private CameraSource cameraSource;
    private SurfaceView cameraView;
    private final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private String token = "";
    private String tokenanterior = "";

    TextView mensajeQR;
    TextView mensaje1;
    TextView mensaje2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        cameraView = (SurfaceView) findViewById(R.id.camera_view);
        mensajeQR = (TextView)findViewById(R.id.txtmensajeandroid);

        initQR();

        mensaje1 = (TextView) findViewById(R.id.mensaje_id);
        mensaje2 = (TextView) findViewById(R.id.mensaje_id2);

    }

    public void initQR() {

        // creo el detector qr
        BarcodeDetector barcodeDetector =
                new BarcodeDetector.Builder(this)
                        .setBarcodeFormats(Barcode.ALL_FORMATS)
                        .build();

        // Creo la camara
        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1600, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        // Listener de ciclo de vida de la camara
        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                // verifico si el usuario dio los permisos para la camara
                if (ActivityCompat.checkSelfPermission(qr.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        // verificamos la version de ANdroid que sea al menos la M para mostrar
                        // el dialog de la solicitud de la camara
                        if (shouldShowRequestPermissionRationale(
                            Manifest.permission.CAMERA));
                            requestPermissions(new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CAMERA);
                    }
                    return;
                } else {
                    try {
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException ie) {
                        Log.e("CAMERA SOURCE", ie.getMessage());
                    }
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        // preparo el detector de QR
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes.size() > 0) {
                    // obtenemos el token
                    token = barcodes.valueAt(0).displayValue.toString();
                    // verificamos que el token anterior no se igual al actual
                    // esto es util para evitar multiples llamadas empleando el mismo token
                    if (!token.equals(tokenanterior)) {
                        // guardamos el ultimo token proceado
                        tokenanterior = token;
                        Log.i("token", token);

                        if (URLUtil.isValidUrl(token)) {
                            // Si es una URL valida abre el navegador
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(token));
                            startActivity(browserIntent);
                        } else {
                            // Comparte en otras apps
                            Intent shareIntent = new Intent();
                            shareIntent.setAction(Intent.ACTION_SEND);
                            shareIntent.putExtra(Intent.EXTRA_TEXT, token);
                            shareIntent.setType("text/plain");
                            shareIntent.getDataString();

                            String result = shareIntent.getStringExtra(Intent.EXTRA_TEXT);
                            mensajeQR.setText(result);

                            startActivity(shareIntent);
                        }
                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    synchronized (this) {
                                        wait(5000);
                                        // limpiamos el token
                                        tokenanterior = "";
                                    }
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    Log.e("Error", "Waiting didnt work!!");
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                }
            }
        });
    }






}