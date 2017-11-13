package wget;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Pattern;

public class Wget {
    public static Queue<String> linksActuales = new LinkedList<>();
    public static ArrayList<String> linksDescargados = new ArrayList<>();
    public static String sitio;
    public static void main(String[] args) {
        
        URL url;
        try {
            sitio = "http://marey.esy.es";
            linksActuales.add(sitio);
            while(!linksActuales.isEmpty()){
                // get URL content
                String urlActual = linksActuales.poll();
                System.out.println("Obteniendo: "+urlActual);
                if(urlActual.equals(sitio)){
                    url = new URL(urlActual);
                }else{
                    url = new URL(sitio+"/"+urlActual);
                }
                
                linksDescargados.add(urlActual);
                
                URLConnection conn = url.openConnection();

                // open the stream and put it into BufferedReader
                BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

                String inputLine;

                //save to this filename
                String fileName;
                if(urlActual.equals(sitio)){
                    fileName = "index.html";
                }else{
                    fileName=urlActual;
                }
                 
                File file = new File(fileName);

                if (!file.exists()) {
                        file.createNewFile();
                }

                //use FileWriter to write file
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);

                StringBuilder contenido = new StringBuilder();

                while ((inputLine = br.readLine()) != null) {
                    //System.out.println(inputLine);
                    analizeLine(inputLine);
                    bw.write(inputLine);
                }

                bw.close();
                br.close();

                System.out.println("Descargados : "+linksDescargados.size());
                System.out.println("Falta : "+linksActuales.size());
            }//while
        } catch (MalformedURLException e) {
                System.out.println("Bad url");
        } catch (IOException e) {
                System.out.println("IO Excpetion");
                e.printStackTrace();
        }
    }
    private static void analizeLine(String linea){
        if(linea.contains("href=")){
            String [] filterHref = linea.split(Pattern.quote("href="));
            String url = filterHref[1];
            if(url.contains("'")){
                String [] filterComilla = url.split(Pattern.quote("'"));
                url = filterComilla[1];
            }else if(url.contains("\"")){
                String [] filterComilla = url.split(Pattern.quote("\""));
                url = filterComilla[1];
            }else{
                url = filterHref[1];
            }
            
            
            
            if(!url.contains("http")&&!url.contains("https")){
                if(url.contains("/")){
                String []folder = url.split(Pattern.quote("/"));
                File theDir = new File(folder[0]);
                // if the directory does not exist, create it
                if (!theDir.exists()) {
                    try{
                        theDir.mkdir();
                    } 
                    catch(SecurityException se){
                        //handle it
                    }        
                }
            }
                for(String aux: linksDescargados){
                    if(!aux.equals(url) && !url.contains("#")){
                        linksActuales.add(url);
                        System.out.println("params h: "+url);
                    }
                }
                
            }       
        }else if(linea.contains("src=")){
            String [] filterHref = linea.split(Pattern.quote("src="));
            String url = filterHref[1];
            if(url.contains("'")){
                String [] filterComilla = url.split(Pattern.quote("'"));
                url = filterComilla[1];
            }else if(url.contains("\"")){
                String [] filterComilla = url.split(Pattern.quote("\""));
                url = filterComilla[1];
            }else{
                url = filterHref[1];
            }
            if(!url.contains("http")&&!url.contains("https") && url.contains("/")){
                String []folder = url.split(Pattern.quote("/"));
                File theDir = new File(folder[0]);
                // if the directory does not exist, create it
                if (!theDir.exists()) {
                    try{
                        theDir.mkdir();
                    } 
                    catch(SecurityException se){
                        //handle it
                    }        
                }

            }
            for(String aux: linksDescargados){
                if(!aux.equals(url) && !url.contains("parentNode")&&!url.contains("https")){
                    linksActuales.add(url);
                    System.out.println("params s: "+url);
                }
            }
        }
    }
}