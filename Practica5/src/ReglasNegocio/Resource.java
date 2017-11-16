package ReglasNegocio;

import java.util.List;

/**
 *
 * @author JuanDanielCR
 */
public class Resource {
    private String Uri;
    private boolean authorization;
    private List<String> methods_allowed;
    /**
     * @param Uri Ruta donde se buscará el recurso solicitado
     * @param authorization Nivel de autorización del archivo encontrado en la URI
     * @param methods_allowed Metodos http que podrán aplicarse al Recurso
     **/
    public Resource(String Uri, boolean authorization, List<String> methods_allowed) {
        this.Uri = Uri;
        this.authorization = authorization;
        this.methods_allowed = methods_allowed;
    }

    public Resource(String Uri) {
        this.Uri = Uri;
    }
    
    public String getUri() {
        return Uri;
    }

    public void setUri(String Uri) {
        this.Uri = Uri;
    }

    public List<String> getMethods_allowed() {
        return methods_allowed;
    }

    public void setMethods_allowed(List<String> methods_allowed) {
        this.methods_allowed = methods_allowed;
    }

    public boolean isAuthorization() {
        return authorization;
    }

    public void setAuthorization(boolean authorization) {
        this.authorization = authorization;
    }
    
    @Override
    public String toString() {
        return "Resource{" + "Uri=" + Uri + ", authorization=" + authorization + ", methods_allowed=" + methods_allowed + '}';
    }
    
    
}
