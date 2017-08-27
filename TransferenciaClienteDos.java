import javax.swing.JFileChooser;
import java.net.*;
import java.io.*;

public class TransferenciaClienteDos{
	public static void main(String [] args){
		try{
			JFileChooser jf=new JFileChooser();
			//Se habilita la siguiente opcion para seleccionar multiples archivos
			jf.setMultiSelectionEnabled(true);
			jf.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );
			int r = jf.showOpenDialog(null);
			if(r== JFileChooser.APPROVE_OPTION){
				File[] f=jf.getSelectedFiles();
				//Se itera por cada Archivo seleccionado
				for(File file :f){
					System.out.println(file.getName());
					if(file.isDirectory()){
						System.out.println("Es un directorio, sus archivos son: ");
						archivosFolder(file);
					}
					else if(file.isFile()){
						System.out.println("Es un archivo");
						enviarArchivo(file);
					}
					else if(!file.exists()){
						System.out.println("No existe el archivo");
					}
				}

				System.out.println("Archivo enviado...");
			}//if
		}catch(Exception e){
			e.printStackTrace();
		}//catch
	}//main

	public static void archivosFolder(File folder) throws Exception{
		for (File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				archivosFolder(fileEntry);
			} else {
				System.out.println(fileEntry.getName());
				enviarArchivo(fileEntry);
			}
		}
	}

	public static void enviarArchivo(File archivo) throws Exception{
		String nombre = archivo.getName();
		long tam= archivo.length(), enviados=0;
		String ruta=archivo.getAbsolutePath();
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
	}
}