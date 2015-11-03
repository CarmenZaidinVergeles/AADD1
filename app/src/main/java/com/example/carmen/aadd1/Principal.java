package com.example.carmen.aadd1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Principal extends AppCompatActivity {

    private ClaseAdaptador cl;
    private List<Contacto> lista;
    private OrigenDeDatosContactos origDatos;
    private ListView lv;
    private SharedPreferences pc;
    private TextView tv;
    private long date;
    //private ArchivosXML arch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        lv = (ListView) findViewById(R.id.lvlista);
        tv=(TextView)findViewById(R.id.tvFecha);
        //arch = new ArchivosXML();
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //METODO INIT()
    private void init() throws IOException, XmlPullParserException {
        //Preferencias compartidas:
        pc = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String aux = pc.getString("start","no");
        if(aux.equals("no")){
            SharedPreferences.Editor ed = pc.edit();
            ed.putString("start", "si");
            ed.commit();
            lista = new ArrayList<>();
            lista = (ArrayList<Contacto>) origDatos.getListaCompleta(this);
            escribir(lista);
            Log.v("FAILL","CREANDO");
        }else{
            lista = new ArrayList<>();
            lista = leer();
            Log.v("FAILL","LEYENDO");
        }
        //Adaptador
        cl = new ClaseAdaptador(this, R.layout.item, lista);
        lv.setAdapter(cl);
        lv.setTag(lista);
        /*Fecha: creamos un archivo con el nombre del xml donde guardamos los datos,
        * si el arhcivo exite, obtenemos la fecha de la última modificación de éste a través de lastModified(),
        * lo convertimos en string y hacemos que aparezca por pantalla*/
        File file = new File(getExternalFilesDir(null),"probando3.xml");
        if (file.exists()) {
            Date lastModified = new Date(file.lastModified());
            String r = lastModified.toString();
            tv.setText(r);
        }
        //Registramos menú contextual
        registerForContextMenu(lv);
        //Contactos ordenados
        Collections.sort(lista, new OrdenaNombresAsc());
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contextual, menu);
    }

    //****************************************** MENU CONTEXTUAL ******************************************************

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        long id = item.getItemId();
        AdapterView.AdapterContextMenuInfo vistaInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int posicion = vistaInfo.position;

        if (id == R.id.menu_borrar) {
            borrar(posicion);
            return true;
        } else if (id == R.id.menu_editar) {
            try {
                editDialog(posicion);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        return super.onContextItemSelected(item);
    }

   //PROGRAMACIÓN DE BORRAR
   public void borrar(final int posicion) {
       lista.remove(posicion);
       try {
           escribir(lista);
       } catch (IOException e) {
           e.printStackTrace();
       }
       cl.notifyDataSetChanged();
   }

    //PROGRAMACIÓN DE EDITAR (a través de un diálogo)
    public void editDialog(final int posicion) throws IOException {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.str_dialog_editar_setTitulo);
        LayoutInflater inflater = LayoutInflater.from(this);
        final View vista = inflater.inflate(R.layout.dialogo_editar_contacto, null);
        final EditText etNome, etNume,etNume2,etNume3;
        String nom, num="",num2="Añade un nuevo número",num3="Añade un nuevo número";

        etNome=(EditText)vista.findViewById(R.id.etNome);
        etNume=(EditText)vista.findViewById(R.id.etNume);
        etNume2=(EditText)vista.findViewById(R.id.etNume2);
        etNume3=(EditText)vista.findViewById(R.id.etNume3);

        nom = lista.get(posicion).getNombre().toString();
        if(lista.get(posicion).getListTelf().size()<2) {
            num = lista.get(posicion).getTelefono(0).toString();
        }else {
            if(lista.get(posicion).getListTelf().size()<3){
                num = lista.get(posicion).getTelefono(0).toString();
                num2 =lista.get(posicion).getTelefono(1).toString();
            }else {
                num = lista.get(posicion).getTelefono(0).toString();
                num2 =lista.get(posicion).getTelefono(1).toString();
                num3 = lista.get(posicion).getTelefono(2).toString();
            }}

        etNome.setHint(nom);
        etNume.setHint(num);
        etNume2.setHint(num2);
        etNume3.setHint(num3);

        alert.setView(vista);
        alert.setPositiveButton(R.string.str_dialog_editar_setPositiveButton_aceptarCambios,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        int idd = (int) lista.get(posicion).getId();
                        lista.remove(posicion);
                        EditText etnom, etnum;
                        ArrayList<String> telf = new ArrayList<String>();

                        if (!(etNume.getText().toString().equals(""))) {
                            telf.add(etNume.getText().toString());
                        }
                        if (!(etNume2.getText().toString().equals(""))) {
                            telf.add(etNume2.getText().toString());
                        }
                        if (!(etNume3.getText().toString().equals(""))) {
                            telf.add(etNume3.getText().toString());
                        }
                        Contacto c = new Contacto(idd, etNome.getText().toString(), telf);
                        lista.add(c);//Añadirmos el contacto a la lista
                        try {
                            escribir(lista); //Escribir los cambios del contacto en el archivo xml
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.v("INSERTO TLF", c.toString());
                        cl.notifyDataSetChanged();
                    }
                });

        alert.setView(vista);
        alert.setNegativeButton(R.string.str_dialog_editar_negativeButton_atras, null);
        alert.show();
    }

//*********************************PROGRAMACIÓN DE BOTONES**************************************************

    //PROGRAMACIÓN DE BOTÓN AÑADIR CONTACTO (se realiza a través de un dialogo)
    public void añadir(final View view) {
        AlertDialog.Builder alert= new AlertDialog.Builder(this);
        alert.setTitle(R.string.str_dialog_insertar_titulo_dialogo_insertar);
        LayoutInflater inflater= LayoutInflater.from(this);

        final View vista = inflater.inflate(R.layout.dialogo_insertar_contacto, null);
        alert.setView(vista);
        alert.setPositiveButton(R.string.str_dialog_insertar_positiveButton_insertar,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        long id = lista.size() - 1;
                        EditText et1, et2;
                        et1 = (EditText) vista.findViewById(R.id.etInsertarNombre);
                        et2 = (EditText) vista.findViewById(R.id.etInsertarTelefono);

                        List<String> telf = new ArrayList<String>();
                        telf.add(et2.getText().toString());

                        Contacto c = new Contacto(id, et1.getText().toString(),telf);
                        lista.add(c);
                        try {
                            escribir(lista); //Escribir el nuevo contacto en el archivo xml
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        cl = new ClaseAdaptador(Principal.this, R.layout.item, lista);
                        cl.notifyDataSetChanged();
                        lv.setAdapter(cl);
                    }
                });
        alert.setNegativeButton(R.string.str_dialog_insertar_negativeButton_cancelar, null);
        alert.show();
    }

    //PROGRAMACION DE BOTON SINCRONIZAR (Obtener los cambios producidos en el xml al añadir, borrar o editar contactos)
    public void sincronizar (View v) throws IOException, XmlPullParserException {
        origDatos = new OrigenDeDatosContactos();
        lista = new ArrayList<>();
        lista = leer();//Lee el xml con los cambios
        try {
            escribir(lista); //Escribimos para que aparezcan los cambios en pantalla
        } catch (IOException e) {

        }
        cl = new ClaseAdaptador(this, R.layout.item, lista);
        lv.setAdapter(cl);
        lv.setTag(lista);
    }

    //PROGRAMACION BOTON LISTA INICIAL (recuperar la lista inicial: los contactos del teléfono)
    public void recuperarListaAnterior(View v){
        origDatos = new OrigenDeDatosContactos();
        lista = new ArrayList<>();
        lista = (ArrayList<Contacto>)origDatos.getListaCompleta(this);

        try{
            escribirListaInicial(lista);
        }catch (IOException e){
        }

        cl = new ClaseAdaptador(this, R.layout.item, lista);
        lv.setAdapter(cl);
        lv.setTag(lista);
    }

    //*********************************** METODOS ESCRIBIR Y LEER (SINCRONIZAR)****************************************

    public void escribir(List<Contacto> x) throws IOException {
        Random r = new Random();
        FileOutputStream fosxml = new FileOutputStream(new File(getExternalFilesDir(null),"probando3.xml"));
        XmlSerializer docxml = Xml.newSerializer();
        docxml.setOutput(fosxml, "UTF-8");
        docxml.startDocument(null, Boolean.valueOf(true));
        docxml.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        ArrayList<String> l= new ArrayList<>();
        docxml.startTag(null, "contactos");
        for(int i = 0; i<x.size();i++){
            docxml.startTag(null, "contacto");
            docxml.startTag(null, "nombre");
            docxml.attribute(null, "id", String.valueOf(x.get(i).getId()));
            docxml.text(x.get(i).getNombre().toString());
            docxml.endTag(null, "nombre");
            for(int j=0; j<x.get(i).getListTelf().size(); j++) {
                docxml.startTag(null, "telefono");
                docxml.text(x.get(i).getTelefono(j).toString());
                docxml.endTag(null, "telefono");
            }
            docxml.endTag(null, "contacto");
        }
        docxml.endDocument();
        docxml.flush();
        fosxml.close();
    }

    public List<Contacto> leer() throws IOException, XmlPullParserException {
        List<Contacto> lis=new ArrayList();
        Contacto c= null;
        int id=0;
        ArrayList <String> telf= new ArrayList<>();
        String nom="";
        XmlPullParser lectorxml = Xml.newPullParser();
        lectorxml.setInput(new FileInputStream(new File(getExternalFilesDir(null),"probando3.xml")),"utf-8");
        int evento = lectorxml.getEventType();
        int atrib=0;
        while (evento != XmlPullParser.END_DOCUMENT){

            if(evento == XmlPullParser.START_TAG){

                String etiqueta = lectorxml.getName();
                Log.v("etiqueta",etiqueta);
                if(etiqueta.compareTo("contacto")==0){
                    telf=new ArrayList<>();
                    c=null;
                    atrib=0;
                    nom="";
                }
                if(etiqueta.compareTo("nombre")==0){
                    Log.v("etiqueta","entra");
                    atrib = Integer.parseInt(lectorxml.getAttributeValue(null, "id"));
                    Log.v("etiqueta", String.valueOf(atrib));
                    nom=lectorxml.nextText();
                    Log.v("etiqueta",nom);

                } else if(etiqueta.compareTo("telefono")==0){
                    String texto = lectorxml.nextText();
                    telf.add(texto);

                }

            }
            if(evento==XmlPullParser.END_TAG){
                String etiqueta = lectorxml.getName();
                if(etiqueta.compareTo("contacto")==0){
                    c = new Contacto(atrib,nom,telf);
                    Log.v("Contacto",c.getNombre()+c.getId());
                    lis.add(c);
                }
            }

            evento = lectorxml.next();
        }
        Log.v("Contacto",lis.toString());
        return lis;
    }

    //**************************************METODOS ESCRIBIR Y LEER (RECUPERAR LISTA INICIAL)********************************************

    public void escribirListaInicial(List<Contacto> x) throws IOException {
        Random r = new Random();
        FileOutputStream fosxml = new FileOutputStream(new File(getExternalFilesDir(null),"listaInicial.xml"));
        XmlSerializer docxml = Xml.newSerializer();
        docxml.setOutput(fosxml, "UTF-8");
        docxml.startDocument(null, Boolean.valueOf(true));
        docxml.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        ArrayList<String> l= new ArrayList<>();
        docxml.startTag(null, "contactos");
        for(int i = 0; i<x.size();i++){
            docxml.startTag(null, "contacto");
            docxml.startTag(null, "nombre");
            docxml.attribute(null, "id", String.valueOf(x.get(i).getId()));
            docxml.text(x.get(i).getNombre().toString());
            docxml.endTag(null, "nombre");
            for(int j=0; j<x.get(i).getListTelf().size(); j++) {
                docxml.startTag(null, "telefono");
                docxml.text(x.get(i).getTelefono(j).toString());
                docxml.endTag(null, "telefono");
            }
            docxml.endTag(null, "contacto");
        }
        docxml.endDocument();
        docxml.flush();
        fosxml.close();
    }

    public List<Contacto> leerListaInicial() throws IOException, XmlPullParserException {
        List<Contacto> lis=new ArrayList();
        Contacto c= null;
        int id=0;
        ArrayList <String> telf= new ArrayList<>();
        String nom="";
        XmlPullParser lectorxml = Xml.newPullParser();
        lectorxml.setInput(new FileInputStream(new File(getExternalFilesDir(null),"listaInicial.xml")),"utf-8");
        int evento = lectorxml.getEventType();
        int atrib=0;
        while (evento != XmlPullParser.END_DOCUMENT){

            if(evento == XmlPullParser.START_TAG){
                String etiqueta = lectorxml.getName();
                Log.v("etiqueta",etiqueta);
                if(etiqueta.compareTo("contacto")==0){
                    telf=new ArrayList<>();
                    c=null;
                    atrib=0;
                    nom="";
                }
                if(etiqueta.compareTo("nombre")==0){
                    Log.v("etiqueta","entra");
                    atrib = Integer.parseInt(lectorxml.getAttributeValue(null, "id"));
                    Log.v("etiqueta", String.valueOf(atrib));
                    nom=lectorxml.nextText();
                    Log.v("etiqueta",nom);

                } else if(etiqueta.compareTo("telefono")==0){
                    String texto = lectorxml.nextText();
                    telf.add(texto);
                }
            }
            if(evento==XmlPullParser.END_TAG){
                String etiqueta = lectorxml.getName();
                if(etiqueta.compareTo("contacto")==0){
                    c = new Contacto(atrib,nom,telf);
                    Log.v("Contacto",c.getNombre()+c.getId());
                    lis.add(c);
                }
            }

            evento = lectorxml.next();
        }
        Log.v("Contacto",lis.toString());
        return lis;
    }
}