package com.example.carmen.aadd1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carmen on 26/10/2015.
 */
public class Contacto implements Serializable, Comparable<Contacto>{

    private long id;
    private String nombre;
    private List<String> telefonos;

    public Contacto() {
        this(0, "0", new ArrayList<String>());

    }

    public Contacto(long id, String nombre, List<String> telefonos) {
        this.id = id;
        this.nombre =nombre;
        this.telefonos= telefonos;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }



    public List<String> getListTelf() {
        return telefonos;
    }

    public void setlistTelf(List<String> listTelf) {
        this.telefonos = listTelf;
    }

    public String getNum() {
        return telefonos.get(0);
    }

    public String getNumP(int pos) {
        return telefonos.get(pos);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contacto contacto = (Contacto) o;

        if (id != contacto.id) return false;
        if (nombre != null ? !nombre.equals(contacto.nombre) : contacto.nombre != null)
            return false;
        return !(telefonos != null ? !telefonos.equals(contacto.telefonos) : contacto.telefonos != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (nombre != null ? nombre.hashCode() : 0);
        result = 31 * result + (telefonos != null ? telefonos.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Contacto contacto) {
        int r = this.nombre.compareTo(contacto.nombre);
        if(r == 0){
            r = (int)(this.id - contacto.id);
        }
        return r;
    }

    public  void setTelefono (int location, String lTelf){
        this.telefonos.set(location, lTelf);
    }

    public String getTelefono(int location) {
        return telefonos.get(location);
    }

    public int size() {
        return telefonos.size();
    }

    public boolean isEmpty() {
        return telefonos.isEmpty();
    }

    @Override
    public String toString() {
        return "Contacto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", lTelf=" + telefonos +
                '}';
    }

    public String getNumeros() {
        String s="";
        for(String a:telefonos)
            s+=a+"\n";
        return s;
    }
}