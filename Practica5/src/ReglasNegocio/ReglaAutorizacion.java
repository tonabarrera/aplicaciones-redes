package ReglasNegocio;
/**
 *
 * @author JuanDanielCR
 */
public class ReglaAutorizacion {
    
    /**
     * @return - El método retorna si el recurso con la URI asignada requiere o no Autorizacion
     * @param resource - Representa la ruta donde se encuentra el archivo al cual
     * se le aplicarán las acciones siguientes para saber si se requiere o no una autorización
     */
    public static boolean requireAuthorization(Resource resource){
        /*Cualquier archivo XML que deba ser activado necesitará de previa autorización,
        este parametro puede cambiar por eso se usa una Regla de negocio especial*/
        return resource.getUri().contains("xml");
    }
}
