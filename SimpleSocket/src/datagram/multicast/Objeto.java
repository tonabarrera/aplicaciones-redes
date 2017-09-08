package datagram.multicast;

import java.io.Serializable;

public class Objeto implements Serializable{
    private int numero;
    private String cadena;
    private boolean boleano;

    public Objeto(int numero, String cadena, boolean boleano) {
        this.numero = numero;
        this.cadena = cadena;
        this.boleano = boleano;
    }

    @Override
    public String toString() {
        return "Objeto{" +
                "numero=" + numero +
                ", cadena='" + cadena + '\'' +
                ", boleano=" + boleano +
                '}';
    }
}
