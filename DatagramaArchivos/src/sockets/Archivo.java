package sockets;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author tona created on 06/09/2017 for DatagramaArchivos.
 */
public class Archivo implements Serializable{
    private String nombre;
    private String ruta;
    private long tam;
    private byte[] datos;
    private int bytesEnviados;
    private String clave;

    public Archivo(String nombre, String ruta, long tam, String clave) {
        this.nombre = nombre;
        this.ruta = ruta;
        this.tam = tam;
        this.clave = clave;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public long getTam() {
        return tam;
    }

    public void setTam(long tam) {
        this.tam = tam;
    }

    public byte[] getDatos() {
        return datos;
    }

    public void setDatos(byte[] datos) {
        this.datos = datos;
    }

    public int getBytesEnviados() {
        return bytesEnviados;
    }

    public void setBytesEnviados(int bytesEnviados) {
        this.bytesEnviados = bytesEnviados;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    @Override
    public String toString() {
        return "Archivo{" + "nombre='" + nombre + '\'' + ", ruta='" + ruta + '\'' + ", tam=" +
                tam + ", datos=" + Arrays
                .toString(
                        datos) + ", bytesEnviados=" + bytesEnviados + ", clave='" + clave + '\'' + '}';
    }
}
