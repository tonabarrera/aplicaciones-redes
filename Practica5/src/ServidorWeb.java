import ReglasNegocio.Resource;
import com.sun.corba.se.spi.orbutil.threadpool.ThreadPool;
import http.HttpRequest;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class ServidorWeb {
    /**
     * Constantes generales para el Servidor
     */
    public static final int PUERTO = 8000;
    //mantenimiento
    public static byte estado_servidor = 1; 
    //max_payload - tamaño maximo para el payload de una response, 1500 para no mandar imagen
    public static long max_payload = 150000000; 
    //extensiones - Lista con todos las extensiones permitidas en el servidor
    public static Map<String, String> extensiones;
    //resources - Lista con todos los recursos que actualmente se encuentran en el servidor
    public static Map<String, Resource> resources;
    //Instancia del servidor
    ServerSocket ss;
    
    /**
     * Clase manejadora para cada conexion
     */
    public static class Manejador extends Thread implements http.httpMethods{
        
        /**
         * Constantes para cada conexión
         */
        protected Socket socket;
        //Entrada del socket
        protected BufferedInputStream request;
        //Entrada de un archivo
        protected BufferedInputStream bis2;  
        //Salida del socket
        protected BufferedOutputStream bos;
        //Wrapper de salida para el socket
        protected PrintWriter response;
       
        //header - permite guardar el valor que traiga un header en la peticion recibida
        protected String header;
        //msj - mensaje a imprimir después del estatus en el header http
        protected String msj;
        //estatusCode - codigo http devuelto en el response del http
        protected int estatusCode;
        //payload - establece si el response tendrá o no un cuerpo en el response además de los headers
        protected boolean payload;
        //headerResponse - guarda la los headers de un response
        protected String headerResponse;
        //resource - Recurso sobre el que se ejecutan los métodos http, incluye URI
        protected Resource resource;
        //URI - Ruta del recuros solicitado
        protected String URI;
        //contentSize - 
        protected int contentSize;
        //params
        protected  Map<String, String> params;
        
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
            //System.out.println("Request: " + lineRequest );
            return lineRequest;
        }
        
        @Override
        public void run() {
            try {
                boolean resourceFound = true;
                String lineRequest = imprimirPeticion();
                lineRequest = lineRequest+"\n";
                //Fragmenta la petición recibida en pares, llave - valor
                HttpRequest httpRequest = new HttpRequest(lineRequest);
                            
                headerResponse = headerResponse + "HTTP/1.0 -estatusCode- \n";
                headerResponse = headerResponse + "Server: Servidor de Juan y Tona 1.0 \n";
                headerResponse = headerResponse + "Date: " + new Date() + " \n";
                headerResponse = headerResponse + "Content-Length: -contentSize- \n";

                //GET
                if(lineRequest.toUpperCase().startsWith("GET")){
                    URI = getURI(httpRequest.getValue("GET"));
                    resource = resources.get(URI);
                    StringTokenizer tokens = new StringTokenizer(URI, "?");
                    if(resource != null){
                        if(ReglasNegocio.ReglaMetodos.isPermitido("GET", resource)){
                            doGet(httpRequest);
                        }else{
                            estatusCode = 405;
                            msj="Método GET no permitido sobre la URI";
                            enviarHeader(estatusCode);
                        }
                    }else if(tokens.hasMoreTokens()){
                        String req = tokens.nextToken();
                        req = tokens.nextToken();
                        recibirParametros(req);
                    }else{
                        resourceFound = false;
                    }
                //HEAD
                }else if(lineRequest.toUpperCase().startsWith("HEAD")){
                    URI = getURI(httpRequest.getValue("HEAD"));
                    resource = resources.get(URI);
                    if(resource != null){
                        if(ReglasNegocio.ReglaMetodos.isPermitido("HEAD", resource)){
                            doHead(httpRequest);
                        }else{
                            estatusCode = 405;
                            msj="Método HEAD no permitido sobre la URI";
                            enviarHeader(estatusCode);
                        } 
                    }else{
                        resourceFound = false;
                    }
                //POST
                }else if(lineRequest.toUpperCase().startsWith("POST")){
                    URI = getURI(httpRequest.getValue("POST"));
                    resource = new Resource(URI);
                    resource.setAuthorization(ReglasNegocio.ReglaAutorizacion.requireAuthorization(resource));
                    resource.setMethods_allowed(ReglasNegocio.ReglaMetodos.metodosPermitidos(resource));
                    resources.put(URI, resource);
                    
                    if(ReglasNegocio.ReglaMetodos.isPermitido("POST", resource)){
                        doPost(httpRequest);
                    }else{
                        estatusCode = 405;
                        msj="Método POST no permitido sobre la URI";
                        enviarHeader(estatusCode);
                    }   
                    
                //NO METHOD FOUND 501
                }else{
                    response.println("HTTP/1.0 501 Not Implemented");
                    response.println();
                }
                
                //NO URI FOUND
                if(resourceFound == false){
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
                }
                response.flush();
                bos.flush();
                //keep-alive: evita que el socket se cierre
                socket.close();
                /*Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        System.out.println("timer");
                    }
                  }, 2*60*1000);*/
                
            } catch (IOException e) {
                System.err.println(e.toString());
            }
        }//run
        private String getURI(String line) {
           // System.out.println("linea: "+line);
            if(line.equals("/")){
                return "index.htm";
            }
            int i = line.indexOf("/");
            return line.substring(i+1, line.length());
        }
        
        private void enviarRecurso(BufferedInputStream bis2) throws IOException{
            byte[] buf = new byte[1024];
            int b_leidos;
            while ((b_leidos = bis2.read(buf, 0, buf.length)) != -1) {
                bos.write(buf, 0, b_leidos);
            }
            bos.flush();
        }
        
        private void enviarHeader(int contentSize) throws IOException{
            String mensaje = Integer.toString(estatusCode)+" "+msj;
            //System.out.println("mensaje: "+mensaje);
            headerResponse = headerResponse + "\n";
            headerResponse = headerResponse.replace("-estatusCode-", mensaje);
            headerResponse = headerResponse.replace("-contentSize-", Integer.toString(contentSize));
            bos.write(headerResponse.getBytes());
            bos.flush();
        }
        
        private void contentType(String [] partesFileName){
            if(!header.equals("-1") && (header.contains(extensiones.get(partesFileName[(partesFileName.length - 1)])) || header.contains("*/*"))){
                headerResponse = headerResponse + "Content-Type: " + 
                extensiones.get(partesFileName[(partesFileName.length - 1)]) + " \n";
            }else{
               //content type incorrecto
                System.out.println("no conozco ese content-type");
            }
        }
        private void language(){
            //ingles
            if(header.contains("en-US") || header.contains("en")){
                headerResponse = headerResponse + "Content-Language: en \n";
            }else{
                headerResponse = headerResponse + "Content-Language: es \n";
            }
        }
        private void recibirParametros(String req){
            response.println("HTTP/1.0 200 Okay");
            response.flush();
            response.println();
            response.flush();
            response.print("<html><head><title>SERVIDOR WEB"
                    + "</title></head><body bgcolor=\"#AACCFF\"><center><h1"
                    + "><br>Parametros Obtenidos..</br></h1>"
                    + "<h3><b>" + req + "</b></h3> </center></body></html>");
            response.flush();
        }
        private Map<String,String> params(String params){
            Map<String, String> parametros = new HashMap<String, String>();
            String listaParamteros[] = params.split(Pattern.quote("&"));
            for(String parametroActual: listaParamteros){
                String aux[] = parametroActual.split(Pattern.quote("="));
                if(aux[1].length()==0){
                    aux[1]="";
                }
                parametros.put(aux[0], aux[1]);
            }
            return parametros;
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
                    contentType(partesFileName);
                }else{
                    estatusCode = 400;
                    msj="Encabezado incorrecto";
                }              
            //Accept-Language -> Content-Language
                header = httpRequest.getValue("Accept-Language");
                if(!header.equals("-1")){
                    language();
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
                //Implement 505: poner enviarHeader(), enviarRecurso() pero usaria otro try catch
            }
        }

        @Override
        public void doPost(HttpRequest httpRequest) {
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(URI, "UTF-8");
                params = params(httpRequest.getValue("params"));
                for (Map.Entry<String, String> entry : params.entrySet()){
                    writer.println(entry.getKey() + "/" + entry.getValue());
                }
                writer.close();
                msj = "Moved Permanently";
                estatusCode = 303;
                headerResponse = headerResponse + "Location: post.html \n";
                enviarHeader(contentSize);
                
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ServidorWeb.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(ServidorWeb.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ServidorWeb.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }

        @Override
        public void doPut(HttpRequest httpRequest) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void doHead(HttpRequest httpRequest) {
            try{
                File f = new File(URI);
                contentSize = 0;
                payload = false;
                //HTTP 202 - Accepted for processing, but not completed
                if(!f.exists() || f.isDirectory()) { 
                    estatusCode = 202;
                    msj = "FOUND BUT INCOMPLETE";
                }else{
                    //HTTP 200 Accepted and processing
                    bis2 = new BufferedInputStream(new FileInputStream(URI));
                    bis2.available();
                    contentSize = bis2.available();
                    estatusCode = 200;
                    msj = "OK";
                }
                //Accept-Language
                header = httpRequest.getValue("Accept-Language");
                if(!header.equals("-1")){
                    language();
                }
                //Send header
                enviarHeader(contentSize);
                //Validate HEAD doesnt contains payload
                if(payload==true){
                    enviarRecurso(bis2);
                }
            }catch(Exception e){
                //Implement 505
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
        ExecutorService connectionPool = Executors.newFixedThreadPool(10);
        for (;;) {
            System.out.println("Esperando por Cliente....");
            Socket accept = ss.accept();
            connectionPool.submit(new Manejador(accept));
            System.out.println("Threads en pool: "+((ThreadPoolExecutor)connectionPool).getPoolSize());
        }
    }

    public static void main(String[] args) throws Exception {
        ServidorWeb sWEB = new ServidorWeb();
    }

}