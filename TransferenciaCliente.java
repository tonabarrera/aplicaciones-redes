import javax.swing.JFileChooser;
import java.net.*;
import java.io.*;

public class TransferenciaCliente{
	public static void main(String [] args){
		try{
			JFileChooser jf=new JFileChooser();
			/*si quieres enviar multiples archivos
			jf.setMultiSelectionEnabled(true);
			*/
			int r = jf.showOpenDialog(null);
			if(r== JFileChooser.APPROVE_OPTION){
				File f=jf.getSelectedFile();//si seleccionaste multiples archivos, es con getSelectedFiles()

				/*si se seleccionan multiples archivos vas a tener que poner el bloque de codigo donde se envia el archivo en un metodo y llamar el metodo tabtas veces como archivos tengas*/

				//Falta validacion para saber si es un archivo lo seleccionado
				/*ej
				if (f.isDirectory())
				*/
				String nombre = f.getName();
				long tam= f.length(), enviados=0;
				String ruta=f.getAbsolutePath();
				//Podria seleccionarse una direccion IP para el servidor, aqui estamos fijandola en 127.0.0.1
				String host="127.0.0.1";
				int pto=9999,n,porcentaje=0;
				Socket cl=new Socket(host,pto);
				System.out.println("Conexion establecida, comienza envio del archivo: "+ruta);
				DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
				DataInputStream dis=new DataInputStream(new FileInputStream(ruta));
				dos.writeUTF(nombre);
				dos.flush();
				dos.writeLong(tam);
				dos.flush();
				while(enviados<tam){
					byte[] b=new byte[1500];//creamos un buffer con bytes fijos para segmentar el archivo en pedazos de ese tamaño
					n=dis.read(b);
					dos.write(b,0,n);//lee la cantidad de bytes que haya leido, delimitando al tamaño fijado
					dos.flush();
					enviados=enviados+n;
					porcentaje=(int)((enviados*100)/tam);//regla de 3 con la que se calcula el porcentaje envido
					System.out.print("\rSe ha transmitido el"+porcentaje+"%");
				}//while
				System.out.println("Archivo enviado...");
			}//if
		}catch(Exception e){
			e.printStackTrace();
		}//catch
	}//main
}