package logica;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

public class Mensaje implements Serializable {
    public static final int ICONO = 0;
    public static final int IMAGEN = 1;
    public static final int ANUNCIO = 2;
    public static final int LISTA_CONECTADOS = 3;
    private String destinatario;
    private String usuario;
    private String mensaje;
    private String imagen;
    private int tipoMensaje = 0;
    private ArrayList<String> conectados;

    // Esto se deberia de cambiar por la direccion del localhost
    public static final URL ANGER = ClassLoader.getSystemResource("resources/anger.png");
    public static final URL HEART = ClassLoader.getSystemResource("resources/heart.png");
    public static final URL POOP = ClassLoader.getSystemResource("resources/poop.png");
    public static final URL SADNESS = ClassLoader.getSystemResource("resources/sadness.png");
    public static final URL SMILE = ClassLoader.getSystemResource("resources/smile.png");

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getTipoMensaje() {
        return tipoMensaje;
    }

    public void setTipoMensaje(int tipoMensaje) {
        this.tipoMensaje = tipoMensaje;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public ArrayList<String> getConectados() {
        return conectados;
    }

    public void setConectados(ArrayList<String> conectados) {
        this.conectados = conectados;
    }

    @Override
    public String toString() {
        return "Mensaje{" +
                "destinatario='" + destinatario + '\'' +
                ", usuario='" + usuario + '\'' +
                ", mensaje='" + mensaje + '\'' +
                ", imagen='" + imagen + '\'' +
                ", tipoMensaje=" + tipoMensaje +
                ", conectados=" + conectados +
                '}';
    }

    public String construirMensaje() {
        String msj;
        if (this.tipoMensaje == ANUNCIO) {
            return this.mensaje;
        } else {
            msj = "<div><b>" + this.usuario + ":</b><span>";
            String temporal = mensaje;

            temporal = temporal.replace(">=|", obtenerEtiquetaImagen(ANGER.toString(), ICONO));
            temporal = temporal.replace("<3", obtenerEtiquetaImagen(HEART.toString(), ICONO));
            temporal = temporal.replace(":poop:", obtenerEtiquetaImagen(POOP.toString(), ICONO));
            temporal = temporal.replace("='(", obtenerEtiquetaImagen(SADNESS.toString(), ICONO));
            temporal = temporal.replace("=)", obtenerEtiquetaImagen(SMILE.toString(), ICONO));
            if (this.tipoMensaje == IMAGEN)
                msj += temporal + "</span><p>" + obtenerEtiquetaImagen(this.imagen, IMAGEN) + "</p></div>";
            else
                msj += temporal + "</span></div>";
        }

        return msj;
    }

    private CharSequence obtenerEtiquetaImagen(String img, int tipo) {
        if (tipo == ICONO)
            return "<img src=\"" + img + "\" width=\"20\" height=\"20\"/>";
        else
            return "<img src=\"file:" + img + "\" width=\"250\" height=\"250\"/>";
    }
}
