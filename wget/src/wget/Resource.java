package wget;

/**
 *
 * @author JuanDanielCR
 */
public class Resource {
    String URI;
    int tipo;

    public Resource(String URI,int tipo) {
        this.URI = URI;
        this.tipo = tipo;
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
    
    public String getChild(String uri){
        String child;
        if(uri.equals(URI)){
            child = URI;
        }else{
            if(URI.startsWith("/")){
                URI = URI.substring(1);
                if(!URI.contains(".")){
                    URI = URI+".html";
                }
            }
            child = uri+URI;
        }
        //System.out.println("URI: "+child);
        return child;
    }
    public String isIndex(String uri){
        String index;
        if(URI.equals(uri)){
            index = "index.html";
        }else{
            //TODO:DEJAR solo los folders
            if(URI.startsWith("/") && URI.contains(".html")){
                int fin = URI.indexOf(".");
                URI = URI.substring(0,fin);
            }
            index = URI;
        }
        return index;
    }
    public String getHtml(String uri){
        String index;
        if(URI.equals(uri)){
            index = "index.html";
        }else{
            if(URI.startsWith("/") && URI.contains(".html")){
                int begin = URI.indexOf(".");
                URI = URI.substring(begin,URI.length()-1);
            }
            index = URI;
        }
        return index;
    }
}
