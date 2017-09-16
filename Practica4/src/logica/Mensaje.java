package logica;

public class Mensaje {
    private static final int ICONO = 0;
    private static final int IMAGEN = 1;
    private String usuario;
    private String mensaje;
    private String imagen;
    private boolean tieneImagen = false;
    private static final String ANGER = ClassLoader.getSystemResource("resources/anger.png").toString();
    private static final String HEART = ClassLoader.getSystemResource("resources/heart.png").toString();
    private static final String POOP = ClassLoader.getSystemResource("resources/poop.png").toString();
    private static final String SADNESS = ClassLoader.getSystemResource("resources/sadness.png").toString();
    private static final String SMILE = ClassLoader.getSystemResource("resources/smile.png").toString();

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

    public boolean isTieneImagen() {
        return tieneImagen;
    }

    public void setTieneImagen(boolean tieneImagen) {
        this.tieneImagen = tieneImagen;
    }

    @Override
    public String toString() {
        return "Mensaje{" +
                "usuario='" + usuario + '\'' +
                ", mensaje='" + mensaje + '\'' +
                ", imagen='" + imagen + '\'' +
                ", tieneImagen=" + tieneImagen +
                '}';
    }

    public String construirMensaje() {
        String msj = "<div><b>" + this.usuario + ":</b><span>";
        String temporal = mensaje;
        System.out.println();

        temporal = temporal.replace(">=|", obtenerEtiquetaImagen(ANGER, ICONO));
        temporal = temporal.replace("<3", obtenerEtiquetaImagen(HEART, ICONO));
        temporal = temporal.replace(":poop:", obtenerEtiquetaImagen(POOP, ICONO));
        temporal = temporal.replace("='(", obtenerEtiquetaImagen(SADNESS, ICONO));
        temporal = temporal.replace("=)", obtenerEtiquetaImagen(SMILE, ICONO));
        if (this.tieneImagen)
            msj += temporal + "</span><p>" + obtenerEtiquetaImagen(this.imagen, IMAGEN) + "</p></div>";
        else
            msj += temporal + "</span></div>";
        return msj;
    }

    private CharSequence obtenerEtiquetaImagen(String img, int tipo) {
        if (tipo == ICONO)
            return "<img src=\"" + img +"\" width=\"15\" height=\"15\"/>";
        else
            return "<img src=\"file:" + img +"\" width=\"250\" height=\"250\"/>";
    }
}
