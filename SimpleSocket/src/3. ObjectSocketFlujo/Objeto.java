package object;

import java.io.Serializable;

/**
 * @author tona created on 19/08/2017 for SimpleSocket.
 */
public class Objeto implements Serializable{
    String nombre;
    int edad;
    float sueldo; //transient evita que se envie
    public Objeto(String nombre, int edad, float sueldo) {
        this.nombre = nombre;
        this.edad = edad;
        this.sueldo = sueldo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getEdad() {
        return edad;
    }

    public float getSueldo() {
        return sueldo;
    }
}
