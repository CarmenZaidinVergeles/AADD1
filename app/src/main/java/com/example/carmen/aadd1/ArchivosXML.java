package com.example.carmen.aadd1;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Carmen on 26/10/2015.
 */
public class ArchivosXML extends AppCompatActivity {

    private List <Contacto> lista;

    public void escribir(List<Contacto> x) throws IOException {
        FileOutputStream fosxml = new FileOutputStream(new File(getExternalFilesDir(null),"probando.xml"));
        XmlSerializer docxml = Xml.newSerializer();
        docxml.setOutput(fosxml, "UTF-8");
        docxml.startDocument(null, Boolean.valueOf(true));
        docxml.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        List<String> l= new ArrayList<>();
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
        Contacto c= null;
        int id=0;
        List <String> telf= new ArrayList<>();
        String nom="";
        XmlPullParser lectorxml = Xml.newPullParser();
        lectorxml.setInput(new FileInputStream(new File(getExternalFilesDir(null),"probando.xml")),"utf-8");
        int evento = lectorxml.getEventType();
        while (evento != XmlPullParser.END_DOCUMENT){
            if(evento == XmlPullParser.START_TAG){
                String etiqueta = lectorxml.getName();
                if(etiqueta.compareTo("nombre")==0){
                    String atrib = lectorxml.getAttributeValue(null, "id");
                    String texto = lectorxml.nextText();
                    id= Integer.parseInt(lectorxml.getAttributeValue(null,"id"));
                    nom=lectorxml.nextText();

                } else if(etiqueta.compareTo("telefono")==0){
                    String texto = lectorxml.nextText();
                    telf.add(texto);

                }
                c = new Contacto(id,nom,telf);
            }
            lista.add(c);
            evento = lectorxml.next();
        }
        return lista;
    }
}
