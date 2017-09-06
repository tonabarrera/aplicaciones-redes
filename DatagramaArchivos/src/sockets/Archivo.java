package sockets;

import java.io.Serializable;

/**
 * @author tona created on 06/09/2017 for DatagramaArchivos.
 */
public class Archivo implements Serializable{
    private String nombre;
    private String ruta;
    private long tam;
    private byte[] datos;
    private int bytesEnviados;

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
}
