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

public class cuadro_dialogo_afirmacion {

    public cuadro_dialogo_afirmacion(Context Contexto, String msj) {


        final Dialog dialogo = new Dialog(Contexto);
        dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogo.setCancelable(false);
        dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogo.setContentView(R.layout.cuadro_dialogo_afirmacion);

        TextView mensaje = (TextView)dialogo.findViewById(R.id.txtprogress);
        mensaje.setText(msj);
        final Button afirmacion = (Button)dialogo.findViewById(R.id.btn_ok);

        afirmacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogo.dismiss();
            }
        });

        dialogo.show();
    }


}
