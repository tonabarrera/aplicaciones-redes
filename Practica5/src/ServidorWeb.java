import http.HttpRequest;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class ServidorWeb {

    public static final int PUERTO = 8000;
    ServerSocket ss;
    public static 

    class Manejador extends Thread implements http.httpMethods{

        protected Socket socket;
        protected BufferedInputStream request;
        protected BufferedOutputStream bos;
        protected PrintWriter response;
        protected String FileName;
        protected Map<String, String> extensiones;

        public Manejador(Socket _socket) throws Exception {
            socket = _socket;
            request = new BufferedInputStream(new DataInputStream(socket.getInputStream()));
            bos = new BufferedOutputStream(socket.getOutputStream());
            response = new PrintWriter(new OutputStreamWriter(bos));
            extensiones = new HashMap<String, String>();
            extensiones.put("aac", "audio/aac");
            extensiones.put("abw", "application/x-abiword");
            extensiones.put("arc", "application/octet-stream");
            extensiones.put("avi", "video/x-msvideo");
            extensiones.put("azw", "application/vnd.amazon.ebook");
            extensiones.put("bin", "application/octet-stream");
            extensiones.put("bz", "application/x-bzip");
            extensiones.put("bz2", "application/x-bzip2");
            extensiones.put("csh", "application/x-csh");
            extensiones.put("css", "text/css");
            extensiones.put("csv", "text/csv");
            extensiones.put("epub", "application/epub+zip");
            extensiones.put("ico", "image/x-icon");
            extensiones.put("ics", "text/calendar");
            extensiones.put("jar", "application/java-archive");
            extensiones.put("js", "application/javascript");
            extensiones.put("json", "application/json");
            extensiones.put("mid", "audio/midi");
            extensiones.put("midi", "audio/midi");
            extensiones.put("mpkg", "application/vnd.apple.installer+xml");
            extensiones.put("odp", "application/vnd.oasis.opendocument.presentation");
            extensiones.put("ods", "application/vnd.oasis.opendocument.spreadsheet");
            extensiones.put("odt", "application/vnd.oasis.opendocument.text");
            extensiones.put("oga", "audio/ogg");
            extensiones.put("ogv", "video/ogg");
            extensiones.put("ogx", "application/ogg");
            extensiones.put("ppt", "application/vnd.ms-powerpoint");
            extensiones.put("rar", "application/x-rar-compressed");
            extensiones.put("rtf", "application/rtf");
            extensiones.put("sh", "application/x-sh");
            extensiones.put("svg", "image/svg+xml");
            extensiones.put("swf", "application/x-shockwave-flash");
            extensiones.put("tar", "application/x-tar");
            extensiones.put("tif", "image/tiff");
            extensiones.put("tiff", "image/tiff");
            extensiones.put("ttf", "font/ttf");
            extensiones.put("vsd", "application/vnd.visio");
            extensiones.put("wav", "audio/x-wav");
            extensiones.put("weba", "audio/webm");
            extensiones.put("webm", "video/webm");
            extensiones.put("webp", "image/webp");
            extensiones.put("woff", "font/woff");
            extensiones.put("woff2", "font/woff2");
            extensiones.put("xhtml", "application/xhtml+xml");
            extensiones.put("xls", "application/vnd.ms-excel");
            extensiones.put("xml", "application/xml");
            extensiones.put("xul", "application/vnd.mozilla.xul+xml");
            extensiones.put("zip", "application/zip");
            extensiones.put("3gp", "video/3gpp");
            extensiones.put("3g2", "video/3gpp2");
            extensiones.put("7z", "application/x-7z-compressed");

            extensiones.put("jpg", "image/jpeg");
            extensiones.put("jpge", "image/jpeg");
            extensiones.put("doc", "application/msword");
            extensiones.put("gif", "image/gif");
            extensiones.put("mpeg", "video/mpeg");
            extensiones.put("html", "text/html");
            extensiones.put("htm", "text/html");
            extensiones.put("pdf", "application/pdf");
        }

        @Override
        public void run() {
            try {
                //Configure maximum request size in this array
                byte b[] = new byte[1024];
                request.read(b);
                String lineRequest = new String(b);
                System.out.println("\nCliente Conectado desde: " + socket.getInetAddress());
                System.out.println("Por el puerto: " + socket.getPort());
                System.out.println("Request: " + lineRequest );

                if (lineRequest == null) {
                    response.print("<html><head><title>Servidor WEB");
                    response.print("</title><body bgcolor=\"#AACCFF\"<br>Linea Vacia</br>");
                    response.print("</body></html>");
                    socket.close();
                }else{
                     HttpRequest httpRequest = new HttpRequest(lineRequest);
                    //GET
                    if(lineRequest.toUpperCase().startsWith("GET")){
                        doGet(httpRequest);      
                    //HEAD
                    }else if(lineRequest.toUpperCase().startsWith("HEAD")){
                        doHead(httpRequest);
                    //POST
                    }else if(lineRequest.toUpperCase().startsWith("POST")){
                        doPost(httpRequest);
                    }
                    response.flush();
                    bos.flush();
                    socket.close();
                }
                
                /*

                } else if (line.toUpperCase().startsWith("GET")) {
                    StringTokenizer tokens = new StringTokenizer(line, "?");
                    String req_a = tokens.nextToken();
                    String req = tokens.nextToken();
                    System.out.println("Token1: " + req_a + "\r\n\r\n");
                    System.out.println("Token2: " + req + "\r\n\r\n");
                    response.println("HTTP/1.0 200 Okay");
                    response.flush();
                    response.println();
                    response.flush();
                    response.print("<html><head><title>SERVIDOR WEB");
                    response.flush();
                    response.print("</title></head><body bgcolor=\"#AACCFF\"><center><h1><br>Parametros Obtenidos..</br></h1>");
                    response.flush();
                    response.print("<h3><b>" + req + "</b></h3>");
                    response.flush();
                    response.print("</center></body></html>");
                    response.flush();
                } else {
                    response.println("HTTP/1.0 501 Not Implemented");
                    response.println();
                }*/
            } catch (IOException e) {
                System.err.println(e.toString());
            }
        }//run

        public String getURI(String line) {
            System.out.println("linea: "+line);
            if(line.equals("/")){
                return "index.htm";
            }
            int i = line.indexOf("/");
            return line.substring(i+1, line.length());
        }
        
        @Override

        public void doGet(HttpRequest httpRequest) {
            //Validaciones de URI 
            String URI = getURI(httpRequest.getValue("GET"));
            String headerResponse = "";
            BufferedInputStream bis2;
            int contentSize = 0;
            try {
                File f = new File(URI);
                //HTTP 202 - Accepted for processing, but not completed
                if(!f.exists() || f.isDirectory()) { 
                    headerResponse = headerResponse + "HTTP/1.0 202\n";
                    headerResponse = headerResponse + "Server: Servidor de Juan y Tona 1.0 \n";
                    headerResponse = headerResponse + "Date: " + new Date() + " \n";
                    headerResponse = headerResponse + "Content-Length: " + contentSize + " \n";
                    headerResponse = headerResponse + "\n";
                    bos.write(headerResponse.getBytes());
                    bos.flush();
                    return;
                }else{
                    bis2 = new BufferedInputStream(new FileInputStream(URI));
                    bis2.available();
                    contentSize = bis2.available();
                    headerResponse = headerResponse + "HTTP/1.0 200 OK\n";
                }
                //HTTP 200 Accepted and processing
                headerResponse = headerResponse + "Server: Servidor de Juan y Tona 1.0 \n";
                headerResponse = headerResponse + "Date: " + new Date() + " \n";
                headerResponse = headerResponse + "Content-Length: " + contentSize + " \n";
                String[] partesFileName = URI.split(Pattern.quote("."));
                //Content-type
                if (extensiones.containsKey(partesFileName[(partesFileName.length - 1)])) {
                    headerResponse = headerResponse + "Content-Type: " + 
                    extensiones.get(partesFileName[(partesFileName.length - 1)]) + " \n";
                }
                //Accept-endoding
                String encondings = httpRequest.getValue("Accept-Encoding");
                if(!encondings.equals("-1")){
                    if(encondings.contains("gzip")){
                        headerResponse = headerResponse + "Content-Encoding: gzip \n";
                    }else if(encondings.contains("deflate")){
                        headerResponse = headerResponse + "Content-Encoding: deflate \n";
                    }
                }
                //esta linea completa un http response
                headerResponse = headerResponse + "\n";
                System.out.println("headder: "+headerResponse);
                bos.write(headerResponse.getBytes());
                bos.flush();
                
                byte[] buf = new byte[1024];
                int b_leidos = 0;
                while ((b_leidos = bis2.read(buf, 0, buf.length)) != -1) {
                    bos.write(buf, 0, b_leidos);
                }
                bos.flush();
                bis2.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void doPost(HttpRequest httpRequest) {
            String URI = getURI(httpRequest.getValue("POST"));
            System.out.println("post: "+URI);
            
        }

        @Override
        public void doPut(HttpRequest httpRequest) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void doHead(HttpRequest httpRequest) {
            try{
                String URI = getURI(httpRequest.getValue("HEAD"));
                String headerResponse = "";
                BufferedInputStream bis2;
                File f = new File(URI);
                int contentSize = 0;
                //HTTP 202 - Accepted for processing, but not completed
                if(!f.exists() || f.isDirectory()) { 
                    headerResponse = headerResponse + "HTTP/1.0 202 NOT FOUND\n";
                    headerResponse = headerResponse + "Server: Servidor de Juan y Tona 1.0 \n";
                    headerResponse = headerResponse + "Date: " + new Date() + " \n";
                    headerResponse = headerResponse + "Content-Type: text/html \n";
                    headerResponse = headerResponse + "Content-Length: " + contentSize + " \n";
                    headerResponse = headerResponse + "\n";
                    bos.write(headerResponse.getBytes());
                    bos.flush();
                    return;
                }else{
                    bis2 = new BufferedInputStream(new FileInputStream(URI));
                    bis2.available();
                    contentSize = bis2.available();
                    headerResponse = headerResponse + "HTTP/1.0 200 OK\n";
                }
                //HTTP 200 Accepted and processing
                headerResponse = headerResponse + "Server: Servidor de Juan y Tona 1.0 \n";
                headerResponse = headerResponse + "Date: " + new Date() + " \n";
                headerResponse = headerResponse + "Content-Length: " + contentSize + " \n";
                String[] partesFileName = URI.split(Pattern.quote("."));
                                
                if (extensiones.containsKey(partesFileName[(partesFileName.length - 1)])) {
                    headerResponse = headerResponse + "Content-Type: " + 
                    extensiones.get(partesFileName[(partesFileName.length - 1)]) + " \n";
                }
                //esta linea completa un http response
                headerResponse = headerResponse + "\n";
                bos.write(headerResponse.getBytes());
                bos.flush();
            }catch(Exception e){
                
            }
        }

        @Override
        public void doDelete(HttpRequest httpRequest) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void doConnect(HttpRequest httpRequest) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void doOptions(HttpRequest httpRequest) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void doTrace(HttpRequest httpRequest) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        
    }//inner class

    public ServidorWeb() throws Exception {
        this.ss = new ServerSocket(PUERTO);
        System.out.println("Servidor iniciado");
        System.out.println(ss.getInetAddress());
        for (;;) {
            System.out.println("Esperando por Cliente....");
            Socket accept = ss.accept();
            new Manejador(accept).start();
        }
    }

    public static void main(String[] args) throws Exception {
        ServidorWeb sWEB = new ServidorWeb();
    }

}
