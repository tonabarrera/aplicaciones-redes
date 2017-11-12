package ReglasNegocio;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JuanDanielCR
 */
public class ReglaMetodos {
    /**
     * @param resource - Recurso al cual se le agregarán los metodos http que puede 
     * usar
     * @return - Lista con nombres de los métodos HTTP que son permitidos para el
     * recurso actual según la definición de la regla de negocio
     */
    public static List<String> metodosPermitidos(Resource resource){
        List<String> metodos = new ArrayList();
        //All general-purpose servers MUST support the methods GET and HEAD
        metodos.add("GET");
        metodos.add("HEAD");
        //DELETE: El metodo DELETE será aplicable para los archivos con extension .png
        if(resource.getUri().contains("png")){
            metodos.add("DELETE");
        }
        //PUT, POST: Solo se podrán subir y actualizar archivos .txt
        if(resource.getUri().contains("txt")){
            metodos.add("POST");
            metodos.add("PUT");
        }
        return metodos;
    }
    
    /**
     * @param metodo - Método que se desea usar en el recurso recibido
     * @param resource - REcurso recibido sobre el cual se desea aplicar una accion http
     * @return - Retorna si el método es recibido puede ser aplicado al recurso recibido
     */
    public static boolean isPermitido(String metodo, Resource resource){
        return resource.getMethods_allowed().contains(metodo);
    }
    
}
