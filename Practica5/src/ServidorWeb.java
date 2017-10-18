import java.net.*;
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class ServidorWeb {

    public static final int PUERTO = 8000;
    ServerSocket ss;

    class Manejador extends Thread {

        protected Socket socket;
        protected PrintWriter pw;
        protected BufferedOutputStream bos;
        protected BufferedReader br;
        protected String FileName;
        protected Map<String, String> extensiones;

        public Manejador(Socket _socket) throws Exception {
            this.socket = _socket;
            this.extensiones = new HashMap<String, String>();
            extensiones.put("aac", "audio/aac");
            extensiones.put("abw", "application/x-abiword");
            extensiones.put("arc", "application/octet-stream");
            extensiones.put("avi", "video/x-msvideo");
            extensiones.put("azw", "application/vnd.amazon.ebook");
            extensiones.put("bin", "application/octet-stream");
            extensiones.put("bz", "application/x-bzip");
            extensiones.put("bz2", "application7x-bzip2");
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
            extensiones.put("oga", "audio7ogg");
            extensiones.put("ogv", "video/ogg");
            extensiones.put("ogx", "application/ogg");
            extensiones.put("ppt", "application/vnd.ms-powerpoint");
            extensiones.put("rar", "application/x-rar-compressed");
            extensiones.put("rtf", "application/rtf");
            extensiones.put("sh", "application/x-sh");
            extensiones.put("svg", "image/svg+xml");
            extensiones.put("swf", "application/x-shockwave-flash");
            extensiones.put("tar", "application/x-tar");
            extensiones.put("tif", "image7tiff");
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
            extensiones.put("7z", "application-x-7z-compressed");
            
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
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                bos = new BufferedOutputStream(socket.getOutputStream());
                pw = new PrintWriter(new OutputStreamWriter(bos));
                String line = br.readLine();
                System.out.println("linea:" + line);
                if (line == null) {
                    pw.print("<html><head><title>Servidor WEB");
                    pw.print("</title><body bgcolor=\"#AACCFF\"<br>Linea Vacia</br>");
                    pw.print("</body></html>");
                    socket.close();
                    return;
                }
                System.out.println("\nCliente Conectado desde: " + socket.getInetAddress());
                System.out.println("Por el puerto: " + socket.getPort());
                System.out.println("Datos: " + line + "\r\n\r\n");

                if (!line.contains("?")) {
                    if (line.toUpperCase().startsWith("GET")){
                    getArch(line);
                    System.out.println("FileName: " + FileName);
                    if (FileName.compareTo("") == 0) {
                        SendA("index.htm");
                    } else {
                        SendA(FileName);
                    }
                    System.out.println(FileName);
                    }else if(line.toUpperCase().startsWith("POST")){
                        System.out.println("Soy un post");
                    }else if(line.toUpperCase().startsWith("PUT")){
                        
                    }else if(line.toUpperCase().startsWith("HEAD")){
                        
                    }else if(line.toUpperCase().startsWith("POST")){
                        
                    }
                } else if (line.toUpperCase().startsWith("GET")) {
                    StringTokenizer tokens = new StringTokenizer(line, "?");
                    String req_a = tokens.nextToken();
                    String req = tokens.nextToken();
                    System.out.println("Token1: " + req_a + "\r\n\r\n");
                    System.out.println("Token2: " + req + "\r\n\r\n");
                    pw.println("HTTP/1.0 200 Okay");
                    pw.flush();
                    pw.println();
                    pw.flush();
                    pw.print("<html><head><title>SERVIDOR WEB");
                    pw.flush();
                    pw.print("</title></head><body bgcolor=\"#AACCFF\"><center><h1><br>Parametros Obtenidos..</br></h1>");
                    pw.flush();
                    pw.print("<h3><b>" + req + "</b></h3>");
                    pw.flush();
                    pw.print("</center></body></html>");
                    pw.flush();
                } else {
                    pw.println("HTTP/1.0 501 Not Implemented");
                    pw.println();
                }
                pw.flush();
                bos.flush();
                socket.close();
            } catch (IOException e) {
                System.err.println(e.toString());
            }
        }//run

        public void getArch(String line) {
            int i;
            int f;
            if (line.toUpperCase().startsWith("GET")) {
                i = line.indexOf("/");
                f = line.indexOf(" ", i);
                FileName = line.substring(i + 1, f);
            }
        }

        public void SendA(String fileName, Socket sc) {
            //System.out.println(fileName);
            int fSize = 0;
            byte[] buffer = new byte[4096];
            try {
                DataOutputStream out = new DataOutputStream(sc.getOutputStream());

                //sendHeader();
                FileInputStream f = new FileInputStream(fileName);
                int x = 0;
                while ((x = f.read(buffer)) > 0) {
                    //		System.out.println(x);
                    out.write(buffer, 0, x);
                }
                out.flush();
                f.close();
            } catch (FileNotFoundException e) {
                //msg.printErr("Transaction::sendResponse():1", "El archivo no existe: " + fileName);
            } catch (IOException e) {
                //			System.out.println(e.getMessage());
                //msg.printErr("Transaction::sendResponse():2", "Error en la lectura del archivo: " + fileName);
            }

        }//sendA

        public void SendA(String arg) {
            try {
                int b_leidos = 0;
                BufferedInputStream bis2 = new BufferedInputStream(new FileInputStream(arg));
                byte[] buf = new byte[1024];
                int tam_bloque = 0;
                if (bis2.available() >= 1024) {
                    tam_bloque = 1024;
                } else {
                    bis2.available();
                }

                int tam_archivo = bis2.available();
                /**
                 * ********************************************
                 */
                String sb = "";
                sb = sb + "HTTP/1.0 200 ok\n";
                sb = sb + "Server: Servidor de Juan y Tona Company/1.0 \n";
                sb = sb + "Date: " + new Date() + " \n";
                if (!arg.equals("index.htm")) {
                    String[] partesFileName = arg.split(Pattern.quote("."));
                    String extension = partesFileName[(partesFileName.length - 1)];
                    System.out.println("Extension pedida: " + extension);
                    if (extensiones.containsKey(extension)) {
                        sb = sb + "Content-Type: " + extensiones.get(extension) + " \n";
                    }
                } else {
                    sb = sb + "Content-Type: text-html\n";
                }

                sb = sb + "Content-Length: " + tam_archivo + " \n";
                sb = sb + "\n";
                bos.write(sb.getBytes());
                bos.flush();

                //out.println("HTTP/1.0 200 ok");
                //out.println("Server: Axel Server/1.0");
                //out.println("Date: " + new Date());
                //out.println("Content-Type: text/html");
                //out.println("Content-Length: " + mifichero.length());
                //out.println("\n");
                /**
                 * ********************************************
                 */
                while ((b_leidos = bis2.read(buf, 0, buf.length)) != -1) {
                    System.out.println("leidos: "+b_leidos);
                    bos.write(buf, 0, b_leidos);

                }
                bos.flush();
                bis2.close();

            } catch (Exception e) {
                System.out.println("error" + e.toString());
            }

        } //SendA
    }//inner class

    public ServidorWeb() throws Exception {
        System.out.println("Iniciando Servidor.......");
        this.ss = new ServerSocket(PUERTO);
        System.out.println("Servidor iniciado:---OK");
        System.out.println("Esperando por Cliente....");
        for (;;) {
            Socket accept = ss.accept();
            new Manejador(accept).start();
        }
    }

    public static void main(String[] args) throws Exception {
        ServidorWeb sWEB = new ServidorWeb();
    }

}
