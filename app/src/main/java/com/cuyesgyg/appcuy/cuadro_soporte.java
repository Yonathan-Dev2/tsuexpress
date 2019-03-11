package com.cuyesgyg.appcuy;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;



import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by Yonathan on 10/03/2019.
 */

public class cuadro_soporte {

    private final cuadro_soporte contexto;

    public cuadro_soporte(final Context Contexto, final String numero) {

        contexto = this;

        final Dialog dialogo = new Dialog(Contexto);
        dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogo.setCancelable(false);
        dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogo.setContentView(R.layout.cuadro_soporte);

        TextView numerot = (TextView)dialogo.findViewById(R.id.txtnumero_llamar);
        numerot.setText(numero);
        final Button cerrar = (Button)dialogo.findViewById(R.id.btncancelar_llamar);
        final Button llamar = (Button)dialogo.findViewById(R.id.btnaceptar_llamar);

        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogo.dismiss();
            }
        });

        llamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri number = Uri.parse("tel:"+numero);
                Intent callIntent = new Intent(Intent.ACTION_DIAL,number);
                Contexto.startActivity(callIntent);
            }

        });


        dialogo.show();
    }
}
