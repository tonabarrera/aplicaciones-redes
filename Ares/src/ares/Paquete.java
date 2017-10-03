package ares;

import java.io.Serializable;

/**
 *
 * @author JuanDanielCR
 */
public class Paquete implements Serializable{
    private int tipo;
    private int numero;
    private byte[] bytes;
    private String nombreArchivo;
    private String nombreCarpeta;

    public Paquete() {
        super();
    }
    
    public Paquete(int tipo) {
        this.tipo = tipo;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getNombreCarpeta() {
        return nombreCarpeta;
    }

    public void setNombreCarpeta(String nombreCarpeta) {
        this.nombreCarpeta = nombreCarpeta;
    }
    
    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Paquete{" + "tipo=" + tipo + ", numero=" + numero + ", bytes=" + bytes + ", nombreArchivo=" + nombreArchivo + ", nombreCarpeta=" + nombreCarpeta + '}';
    }
    
}
