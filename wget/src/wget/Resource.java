package wget;

import java.io.File;

/**
 *
 * @author JuanDanielCR
 */
public class Resource {
    private String URI;
    private String directorio;
    private String nombreArchivo;
    private String contenyType;
    private int tipo;
    private final Mime mime;

    public Resource(String URI, Mime mime, int tipo) {
        this.URI = URI;
        this.tipo = tipo;
        this.mime = mime;
    }

    public String getURI() {
        return URI;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getDirectorio() {
        return directorio;
    }

    public void setDirectorio(String directorio) {
        this.directorio = directorio;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getContenyType() {
        return contenyType;
    }

    public void setContenyType(String contenyType) {
        this.contenyType = contenyType;
    }
    
    
    public String getURI(String sitio){
        String child;
        if(sitio.equals(URI)){
            child = sitio; 
        }else{
            String aux="";
            if(URI.startsWith("/")){
                //Quitar slash para evitar un // en la nueva petición http
                aux = URI.substring(1);
            }
            child = sitio+aux;
        }
        return child;
    }
    
    //Método para analizar el URI y dividirlo en carpetas y archivo
    public void crearDirectorios(String sitio){
        if(URI.equals(sitio)){
            nombreArchivo = "index.html";
        }else{
            String[] carpetas = URI.split("/");
            if(carpetas.length > 2){
                String dir = "/";
                for(int i=1; i<carpetas.length-1;i++){
                    dir += carpetas[i]+File.separator;
                }
                directorio = dir;
                tipo=2;
            }else{
                if(URI.startsWith("/")){
                    URI = URI.substring(1);
                }
                
            }
            if(carpetas.length>0){
                nombreArchivo = carpetas[carpetas.length-1];
            }
            analizarContentType();
        }
        System.out.println("URI: "+URI+" nombre archivo: "+nombreArchivo+" directorio: "+directorio+" content-type: "+contenyType);
    }
    private void analizarContentType(){
        if(!nombreArchivo.contains(".")){
            String[]content = contenyType.split(";");
            String extension = mime.getExtension(content[0]);
            nombreArchivo += "."+extension;
        }
    }
}
