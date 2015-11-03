package com.example.carmen.aadd1;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carmen on 28/10/2015.
 */
public class ClaseAdaptador extends ArrayAdapter<Contacto>{


    private Context ctx;
    private int res;
    private LayoutInflater lInflator;
    private ArrayList<Contacto> valores;
    private List<String> Tlf;
    private OrigenDeDatosContactos od;

    public ClaseAdaptador(Context context, int resource, List<Contacto> lista) {
        super(context, resource, lista);
        this.ctx = context;
        this.res = resource;
        this.valores = (ArrayList<Contacto>) lista;
        this.lInflator = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
    }

    static class ViewHolder {
        public TextView tv1, tv2;
        public ImageView iv;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder gv = new ViewHolder();
        if(convertView==null){
            convertView = lInflator.inflate(res, null);
            ImageView iv = (ImageView) convertView.findViewById(R.id.ivContacto);
            TextView tv = (TextView) convertView.findViewById(R.id.tvContacto);
            gv.tv1 = tv;
            tv = (TextView) convertView.findViewById(R.id.tvTelefono);
            gv.tv2 = tv;
            gv.iv = iv;
            convertView.setTag(gv);
        } else {
            gv = (ViewHolder) convertView.getTag();
        }

        Tlf= valores.get(position).getListTelf();
        valores.get(position).setlistTelf(Tlf);

        if(valores.get(position).getListTelf().size()>1){
            gv.iv.setImageResource(R.drawable.mas);
        }else{
            gv.iv.setImageResource(R.drawable.menos);
        }
        gv.tv1.setText(valores.get(position).getNombre().toString());
        gv.tv2.setText(valores.get(position).getNumeros());
        valores.get(position).setlistTelf(Tlf);
        return convertView;
    }
}