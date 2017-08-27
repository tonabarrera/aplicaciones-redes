import javax.swing.JFileChooser;
import java.net.*;
import java.io.*;

public class TransferenciaServidor{
	public static void main(String [] args){
		try{
			int puerto =9999,n;
			ServerSocket s= new ServerSocket(puerto);
			/*
			investigar mas esto, hay un diagrama relacionado en el cuaderno (18-08-2017)
			esta relacionado con el proposito de utilizar de nuevo la direccion y su puerto despues de que termina un proceso
			s.setReuseAddress(true);
			*/
			/*
			si quieres especificar una ruta
			String path="c:\\...\archivos\";
			*/
			System.out.println("Servicio iniciado");
			for(;;){
				Socket cl=s.accept();
				System.out.println("Cliente conectado desde: "+cl.getInetAddress()+":"+cl.getPort());
				DataInputStream d=new DataInputStream(cl.getInputStream());
				String nombre=d.readUTF();
				long tam=d.readLong();
				DataOutputStream a=new DataOutputStream(new FileOutputStream(nombre));//si quieres especificar la ruta el argumento de seria FileOutputStream(path +nombre);
				long recibidos=0;
				while(recibidos<tam){
					byte[] b=new byte[1500];
					n=d.read(b);
					a.write(b,0,n);
					a.flush();
					recibidos=recibidos+n;
				}//while
				System.out.println("Archivo recibido");
				a.close();
				d.close();
				cl.close();
			}//for
		}catch(Exception e){
			e.printStackTrace();
		}//catch
	}//main
}