import ReglasNegocio.Resource;
import http.HttpRequest;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class ServidorWeb {
    /**
     * Constantes generales para el Servidor
     */
    public static final int PUERTO = 8000;
    //mantenimiento
    public static byte estado_servidor = 1; 
    //max_payload - tamaño maximo para el payload de una response, 1500 para no mandar imagen
    public static long max_payload = 150000; 
    //extensiones - Lista con todos las extensiones permitidas en el servidor
    public static Map<String, String> extensiones;
    //resources - Lista con todos los recursos que actualmente se encuentran en el servidor
    public static Map<String, Resource> resources;
    ServerSocket ss;
    
    /**
     * Clase manejadora para cada conexion
     */
    public static class Manejador extends Thread implements http.httpMethods{
        
        /**
         * Constantes para cada conexión
         */
        protected Socket socket;
        protected BufferedInputStream request;
        protected BufferedInputStream bis2;  
        protected BufferedOutputStream bos;
        protected PrintWriter response;
       
        //header - permite guardar el valor que traiga un header en la peticion recibida
        protected String header;
        //msj - mensaje a imprimir después del estatus
        protected String msj;
        //estatusCode - codigo http devuelto en el response del http
        protected int estatusCode;
        //payload - establece si el response tendrá o no un cuerpo en el response además de los headers
        protected boolean payload;
        //headerResponse - guarda la los headers de un response
        protected String headerResponse;
        //resource - Recurso actual sobre el que se ejecutan los métodos http
        protected Resource resource;
        //URI - Ruta del recuros solicitado
        protected String URI;
        //contentSize - 
        protected int contentSize;
        
        public Manejador(Socket _socket) throws Exception {
            //TODO: 429 Too many requests
            //TODO: 503 Service unavailable
            socket = _socket;
            request = new BufferedInputStream(new DataInputStream(socket.getInputStream()));
            bos = new BufferedOutputStream(socket.getOutputStream());
            response = new PrintWriter(new OutputStreamWriter(bos));
            estatusCode = 200;
            payload = true;
            msj = "OK";
        }

        private String imprimirPeticion() throws IOException{
            byte b[] = new byte[1024];
            request.read(b);
            String lineRequest = new String(b);
            System.out.println("\nCliente Conectado desde: " + socket.getInetAddress());
            System.out.println("Por el puerto: " + socket.getPort());
            System.out.println("Request: " + lineRequest );
            return lineRequest;
        }
        
        @Override
        public void run() {
            try {
                String lineRequest = imprimirPeticion();
                HttpRequest httpRequest = new HttpRequest(lineRequest);
                            
                headerResponse = headerResponse + "HTTP/1.0 -estatusCode- \n";
                headerResponse = headerResponse + "Server: Servidor de Juan y Tona 1.0 \n";
                headerResponse = headerResponse + "Date: " + new Date() + " \n";
                headerResponse = headerResponse + "Content-Length: -contentSize- \n";
                
                 
                URI = getURI(httpRequest.getValue("GET"));
                resource = resources.get(URI);
                
                //Validaciones de URI 
                if(resource==null) { 
                    estatusCode = 404;
                    msj = "Not Found";
                    URI = "404.html";
                    //Pagina 404
                    bis2 = new BufferedInputStream(new FileInputStream(URI));
                    bis2.available();
                    contentSize = bis2.available();
                    //Enviando respuesta
                    enviarHeader(contentSize);
                    enviarRecurso(bis2);
                }else{
                    //GET
                    if(lineRequest.toUpperCase().startsWith("GET")){
                        if(ReglasNegocio.ReglaMetodos.isPermitido("GET", resource)){
                            doGet(httpRequest);
                        }else{
                            estatusCode = 405;
                            msj="Método GET no permitido sobre la URI";
                            enviarHeader(estatusCode);
                        }                       
                    //HEAD
                    }else if(lineRequest.toUpperCase().startsWith("HEAD")){
                        if(ReglasNegocio.ReglaMetodos.isPermitido("GET", resource)){
                            doHead(httpRequest);
                        }else{
                            estatusCode = 405;
                            msj="Método GET no permitido sobre la URI";
                            enviarHeader(estatusCode);
                        } 
                    //POST
                    }else if(lineRequest.toUpperCase().startsWith("POST")){
                        if(ReglasNegocio.ReglaMetodos.isPermitido("GET", resource)){
                            doPost(httpRequest);
                        }else{
                            estatusCode = 405;
                            msj="Método POST no permitido sobre la URI";
                            enviarHeader(estatusCode);
                        }         
                    //NOT METHOD FOUND 501
                    }else{
                        response.println("HTTP/1.0 501 Not Implemented");
                        response.println();
                    }
                }

                response.flush();
                bos.flush();
                //keep-alive
                socket.close();
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
        
        public void enviarRecurso(BufferedInputStream bis2) throws IOException{
            byte[] buf = new byte[1024];
            int b_leidos;
            while ((b_leidos = bis2.read(buf, 0, buf.length)) != -1) {
                bos.write(buf, 0, b_leidos);
            }
            bos.flush();
        }
        
        public void enviarHeader(int contentSize) throws IOException{
            headerResponse = headerResponse + "\n";
            headerResponse = headerResponse.replace("-estatusCode-", Integer.toString(estatusCode)+msj);
            headerResponse = headerResponse.replace("-contentSize-", Integer.toString(contentSize));
            bos.write(headerResponse.getBytes());
            bos.flush();
        }
        
        @Override
        public void doGet(HttpRequest httpRequest) {
            try {
            //HTTP 200 Accepted and processing
            bis2 = new BufferedInputStream(new FileInputStream(URI));
            bis2.available();
            contentSize = bis2.available();
    
            //Payload -> Size   
                if(contentSize > max_payload){
                    payload = false;
                    estatusCode = 413;
                    msj = "El payload excede el size de: "+max_payload;
                }

                String[] partesFileName = URI.split(Pattern.quote("."));
                
                header = httpRequest.getValue("Accept");
            //Accept -> Content-type           
                if (extensiones.containsKey(partesFileName[(partesFileName.length - 1)])) {
                    if(!header.equals("-1") && (header.contains(extensiones.get(partesFileName[(partesFileName.length - 1)])) || header.contains("*/*"))){
                        headerResponse = headerResponse + "Content-Type: " + 
                        extensiones.get(partesFileName[(partesFileName.length - 1)]) + " \n";
                    }else{
                       //content type incorrecto
                        System.out.println("no conozco ese content-type");
                    }
                }else{
                    estatusCode = 400;
                    msj="Encabezado incorrecto";
                }              
            //Accept-Language -> Content-Language
                header = httpRequest.getValue("Accept-Language");
                if(!header.equals("-1")){
                    //ingles
                    if(header.contains("en-US") || header.contains("en")){
                        headerResponse = headerResponse + "Content-Language: en \n";
                    }else{
                        headerResponse = headerResponse + "Content-Language: es \n";
                    }
                }else{
                    estatusCode = 400;
                    msj="Encabezado incorrecto";
                }
            //Connection -> Connection
                header = httpRequest.getValue("Connection");
                if(!header.equals("-1")){
                    if(header.contains("keep-alive")){
                        headerResponse = headerResponse + "Connection: keep-alive \n";
                    }else if(header.contains("close")){
                        headerResponse = headerResponse + "Connection: close \n"; 
                    }
                }else{
                    headerResponse = headerResponse + "Connection: close \n";
                    estatusCode = 400;
                    msj="Encabezado incorrecto";
                }  
            //Authorization
                if(resource.isAuthorization()){ //se necesita Autorización
                    header = httpRequest.getValue("Authorization");
                    if(!header.equals("-1")){ //check credentials
                        if(header.contains("Basic")){
                            payload = true;
                        }else{
                            payload = false;
                            estatusCode = 403;
                            msj = "Forbidden";
                        }
                    }else{
                        headerResponse = headerResponse + "WWW-Authenticate: Basic realm=\"Manda tus credenciales\"  \n";
                        msj = "Autenticate";
                        estatusCode = 401; //send credentials
                    }
                }
                enviarHeader(contentSize);
                if(payload==true){
                    enviarRecurso(bis2);
                }
            } catch (IOException e) {
                //Implement 505
            }
        }

        @Override
        public void doPost(HttpRequest httpRequest) {
            
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
                
                //Content-type
                if (extensiones.containsKey(partesFileName[(partesFileName.length - 1)])) {
                    headerResponse = headerResponse + "Content-Type: " + 
                    extensiones.get(partesFileName[(partesFileName.length - 1)]) + " \n";
                }
                //Accept-Language
                String language = httpRequest.getValue("Accept-Language");
                if(!language.equals("-1")){
                    if(language.contains("en-US") || language.contains("en")){
                        headerResponse = headerResponse + "Content-Language: en \n";
                    }
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
        
        resources = new HashMap<String, Resource>();
        /*Implementar rutina para permisos*/
        File publico = new File(".");
        for(File archivo: publico.listFiles()){
            Resource resource = new Resource(archivo.getName());
            //Aplicación Reglas de Negocio
            resource.setAuthorization(ReglasNegocio.ReglaAutorizacion.requireAuthorization(resource));
            resource.setMethods_allowed(ReglasNegocio.ReglaMetodos.metodosPermitidos(resource));

            resources.put(archivo.getName(), resource);
        }
            
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