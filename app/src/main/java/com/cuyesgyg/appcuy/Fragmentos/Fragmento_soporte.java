package com.cuyesgyg.appcuy.Fragmentos;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.cuyesgyg.appcuy.R;
import com.cuyesgyg.appcuy.cuadro_soporte;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragmento_soporte extends Fragment {

    Fragmento_soporte contexto;
    ImageButton llamar_sis, llamar_pro;
    View vista;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        contexto = this;

        vista = inflater.inflate(R.layout.fragmento_soporte, container, false);
        llamar_sis = (ImageButton)vista.findViewById(R.id.imgcelular_sistemas);
        llamar_pro = (ImageButton)vista.findViewById(R.id.imgcelular_produccion);

        llamar_sis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new cuadro_soporte(getContext(), "966105060");
            }
        });

        llamar_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new cuadro_soporte(getContext(), "947505755");
            }
        });

        return vista;

    }

}
