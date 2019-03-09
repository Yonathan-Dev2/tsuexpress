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
 * Created by Yonathan on 07/03/2019.
 */

public class cuadro_dialogo {




    public cuadro_dialogo(Context Contexto) {

        final Dialog dialogo = new Dialog(Contexto);
        dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogo.setCancelable(false);
        dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialogo.setContentView(R.layout.cuadro_dialogo);

        //TextView mensaje = (TextView)dialogo.findViewById(R.id.txtmensaje);

        final Button cerrar = (Button)dialogo.findViewById(R.id.btncerrar);


        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogo.dismiss();
            }
        });

        dialogo.show();
    }


}
