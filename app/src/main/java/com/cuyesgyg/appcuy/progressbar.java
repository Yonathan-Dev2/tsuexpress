package com.cuyesgyg.appcuy;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Yonathan on 23/03/2019.
 */

public class progressbar {

    public progressbar(Context Contexto, String msj) {

        final Dialog dialogo = new Dialog(Contexto);
        dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogo.setCancelable(false);
        dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogo.setContentView(R.layout.progressbar);

        TextView mensaje = (TextView)dialogo.findViewById(R.id.txtprogress);
        mensaje.setText(msj);
        dialogo.show();

    }

}
