package adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cuyesgyg.appcuy.Consulta_control_nacimientos;
import com.cuyesgyg.appcuy.R;

import java.util.List;

/**
 * Created by Yonathan on 26/11/2018.
 */

public class NacimientosAdapter extends RecyclerView.Adapter<NacimientosAdapter.NacimientoHolder> {

    List<Consulta_control_nacimientos> listaNacimientos;

    public NacimientosAdapter(List<Consulta_control_nacimientos> listaNacimientos){
        this.listaNacimientos = listaNacimientos;
    }

    @Override
    public NacimientoHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_nacimiento_list,parent,false);
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        return new NacimientoHolder(vista);
    }

    @Override
    public void onBindViewHolder(NacimientoHolder holder, int position) {
        //holder.txtcodigo_r.setText(listaNacimientos.get(position).get.toString);
        holder.poza.setText(listaNacimientos.get(position).getPoza().toString());

    }

    @Override
    public int getItemCount() {
        return listaNacimientos.size();

    }

    public class NacimientoHolder extends RecyclerView.ViewHolder{

        TextView txtcodigo_r, poza, vivos, muertos;

        public NacimientoHolder(View itemView){
            super(itemView);
            //txtcodigo_r = (TextView)itemView.findViewById(R.id.txtcodigo_r);
            poza = (TextView)itemView.findViewById(R.id.txtpoza_r);
            vivos = (TextView)itemView.findViewById(R.id.txtvivos_r);
            muertos = (TextView)itemView.findViewById(R.id.txtmuertos_r);
        }
    }


}
