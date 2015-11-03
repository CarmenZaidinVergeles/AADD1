package com.example.carmen.aadd1;

import java.util.Comparator;

/**
 * Created by ivan on 10/18/2015.
 */
public class OrdenaNombresAsc implements Comparator<Contacto>{
    @Override
    public int compare(Contacto c1, Contacto c2) {
    if(c1.getNombre().toString().compareToIgnoreCase(c2.getNombre())>0)
        return 1;
    if(c1.getNombre().toString().compareToIgnoreCase(c2.getNombre())<0)
        return -1;
    return 0;
    }
}
