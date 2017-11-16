package wget;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author JuanDanielCR
 */
public class Mime {
    //extensiones - Lista con todos las extensiones permitidas en el servidor
    public static Map<String, String> extensiones;
    
    public Mime(){
        extensiones = new HashMap<>();
        extensiones.put("audio/aac","aac");
        extensiones.put("application/x-abiword","abw");
        extensiones.put("application/octet-stream","arc");
        extensiones.put("video/x-msvideo","avi");
        extensiones.put("application/vnd.amazon.ebook","azw");
        extensiones.put("application/octet-stream","bin");
        extensiones.put("application/x-bzip","bz");
        extensiones.put("application/x-bzip2","bz2");
        extensiones.put("application/x-csh","csh");
        extensiones.put("text/css","css");
        extensiones.put("text/csv","csv");
        extensiones.put("application/epub+zip","epub");
        extensiones.put("image/x-icon","ico");
        extensiones.put("text/calendar","ics");
        extensiones.put("application/java-archive","jar");
        extensiones.put("application/javascript","js");
        extensiones.put("application/json","json");
        extensiones.put("audio/midi","mid");
        extensiones.put("audio/midi","midi");
        extensiones.put("application/vnd.apple.installer+xml","mpkg");
        extensiones.put("application/vnd.oasis.opendocument.presentation","odp");
        extensiones.put("application/vnd.oasis.opendocument.spreadsheet","ods");
        extensiones.put("application/vnd.oasis.opendocument.text","odt");
        extensiones.put("audio/ogg","oga");
        extensiones.put("video/ogg","ogv");
        extensiones.put("application/ogg","ogx");
        extensiones.put("application/vnd.ms-powerpoint","ppt");
        extensiones.put("application/x-rar-compressed","rar");
        extensiones.put("application/rtf","rtf");
        extensiones.put("application/x-sh","sh");
        extensiones.put("image/svg+xml","svg");
        extensiones.put("application/x-shockwave-flash","swf");
        extensiones.put("application/x-tar","tar");
        extensiones.put("image/tiff","tif");
        extensiones.put("image/tiff","tiff" );
        extensiones.put("font/ttf","ttf");
        extensiones.put("application/vnd.visio","vsd");
        extensiones.put("audio/x-wav","wav");
        extensiones.put("audio/webm","weba");
        extensiones.put("video/webm","webm");
        extensiones.put("image/webp","webp");
        extensiones.put("font/woff","woff");
        extensiones.put("font/woff2","woff2");
        extensiones.put("application/xhtml+xml","xhtml");
        extensiones.put("application/vnd.ms-excel","xls");
        extensiones.put("application/xml","xml");
        extensiones.put("application/vnd.mozilla.xul+xml","xul");
        extensiones.put("application/zip","zip");
        extensiones.put("video/3gpp","3gp");
        extensiones.put("video/3gpp2","3g2");
        extensiones.put("application/x-7z-compressed","7z");

        extensiones.put("image/jpeg","jpg");
        extensiones.put("image/jpeg","jpge");
        extensiones.put("application/msword","doc");
        extensiones.put("image/gif","gif");
        extensiones.put("video/mpeg","mpeg");
        extensiones.put("text/html","html");
        extensiones.put("application/pdf","pdf");
    }
    public String getExtension(String contentType){
        String extension;
        if(extensiones.containsKey(contentType)){
            extension = extensiones.get(contentType);
        }else{
            extension = "html";
        }
        return extension;
    }
}
