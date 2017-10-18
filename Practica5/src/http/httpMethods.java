package http;
/**
 *
 * @author JuanDanielCR
 */
public interface httpMethods {
    
    /**
     * Metodos HTTP 1.1 ser implementados por un servidor
     * @param httpRequest - Objeto que contiene los parametros de una peticion
     * con la semántica de un request http 1.1
     **/
    public void doGet(HttpRequest httpRequest);
    public void doPost(HttpRequest httpRequest);
    public void doPut(HttpRequest httpRequest);
    public void doHead(HttpRequest httpRequest);
    public void doDelete(HttpRequest httpRequest);
    public void doConnect(HttpRequest httpRequest);
    public void doOptions(HttpRequest httpRequest);
    public void doTrace(HttpRequest httpRequest);
    
}
