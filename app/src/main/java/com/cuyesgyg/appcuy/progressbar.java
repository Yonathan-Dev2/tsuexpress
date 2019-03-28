package com.cuyesgyg.appcuy;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.RunnableFuture;
import java.util.logging.Handler;

/**
 * Created by Yonathan on 23/03/2019.
 */

public class progressbar {
    Timer timer;

    public progressbar(final Context Contexto, String msj) {

        final Dialog dialogo = new Dialog(Contexto);
        dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogo.setCancelable(false);
        dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogo.setContentView(R.layout.progressbar);

        TextView mensaje = (TextView)dialogo.findViewById(R.id.txtprogress);
        mensaje.setText(msj);

        dialogo.show();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                //Intent i = new Intent(Contexto, MainActivity.class);
                //Contexto.startActivity(i);
                dialogo.dismiss();
            }
        };

        Timer timer = new Timer();
        timer.schedule(task,1000);

    }

}