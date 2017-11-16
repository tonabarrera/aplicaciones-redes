package wget;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Wget {
    public static Queue<Resource> linksActuales = new LinkedList<>();
    public static ArrayList<String> linksDescargados = new ArrayList<>();
    public static ArrayList<String> linksActualesString = new ArrayList<>();
    public static Mime  mime = new Mime();
    
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        System.out.println("Sitio: ");
        Resource sitio = new Resource(sc.nextLine(), mime,1);
        String base = sitio.getURI();
                
        URL url;
        HttpURLConnection connection;
        linksActuales.add(sitio);
            
        while(!linksActuales.isEmpty()){
            try {    
            //Conexión
                Resource recursoActual = linksActuales.poll();
                linksDescargados.add(recursoActual.getURI());
                url = new URL(recursoActual.getURI(base));
                connection =  (HttpURLConnection)url.openConnection();
            //Guardar  
                if(connection.getResponseCode() != 200){
                    continue;
                }
                recursoActual.setContenyType(connection.getContentType());
                File file;
                recursoActual.crearDirectorios(base);
                //System.out.println("file: "+fileName+" tipo: "+recursoActual.tipo);
                if(recursoActual.getDirectorio()!=null){
                    String fd = "."+recursoActual.getDirectorio()+recursoActual.getNombreArchivo();
                    file = new File(fd);
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }else{
                    file =  new File(recursoActual.getNombreArchivo());
                }
                                
                
                FileOutputStream outputStream = new FileOutputStream(file);
                InputStream is = url.openStream ();
                
                byte[] byteChunk = new byte[4096];
                
                int read = 0;
                while((read = is.read(byteChunk)) > 0 ) {
                    //Analizar
                    analizeLine(new String(byteChunk));
                    outputStream.write(byteChunk, 0, read);
                }
                
                outputStream.close();
                is.close(); 
                
                System.out.println("Descargados : "+linksDescargados.size()+" Faltantes: "+linksActuales.size());
            
            } catch (Exception e) {
            }
        }//while
        System.out.println("Listo");
    }
    
    private static void analizeLine(String paginaHtml){
        Matcher pageMatcher;
        Pattern linkPattern;
        //Links
        linkPattern = Pattern.compile("href=\"(.*?)\"", Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
        pageMatcher = linkPattern.matcher(paginaHtml);
        ArrayList<String> links = new ArrayList<>();
        while(pageMatcher.find()){
            String [] filterHref = pageMatcher.group().split(Pattern.quote("href="));
            String url = filterHref[1];
            links.add(url);
        }        
        for(String linkActual: links){
            analizarLink(linkActual);
        }
        //Source
        linkPattern = Pattern.compile("src=\"(.*?)\"");
        pageMatcher = linkPattern.matcher(paginaHtml);
        links.clear();
        while(pageMatcher.find()){
            String [] filterHref = pageMatcher.group().split(Pattern.quote("src="));
            String url = filterHref[1];
            links.add(url);
        }
        for(String linkActual: links){
            analizarLink(linkActual);
        }
    }
    private static void analizarLink(String link){
        //Validar mismo servidor
        if(!link.contains("http:") && !link.contains("https:") && !link.contains("#") && !link.contains("?")){
            //Limpiando
            link = limpiarLink(link);
            //Descargado
            validarDescargado(link);
        }//contains(http)
    }
    private static String limpiarLink(String url){
        if(url.contains("'")){
            String [] filterComilla = url.split(Pattern.quote("'"));
            url = filterComilla[1];
        }else if(url.contains("\"")){
            String [] filterComilla = url.split(Pattern.quote("\""));
            if(filterComilla.length>0)
                url = filterComilla[1];
        }
        return url;
    }

    private static void validarDescargado(String link){
        //System.out.println("lista: "+linksDescargados.size());
        if(linksDescargados.contains(link) || linksActualesString.contains(link)){                  
                
        }else{
            Resource recurso = new Resource(link,mime, 1);
            linksActuales.add(recurso);
            linksActualesString.add(link);
        }
    }
    
}