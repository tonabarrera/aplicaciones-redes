package practica2;

import java.io.DataInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author JuanDanielCR
 */
public class ServidorBuscaminas {
    public static final byte NIVEL_PRINCIPANTE = 9;
    public static final byte NIVEL_INTERMEDIO = 16;
    public static final byte NIVEL_DIFICIL = 30;
    
    public static final byte MINAS_NIVEL_PRINCIPANTE = 10;
    public static final byte MINAS_NIVEL_INTERMEDIO = 40;
    public static final byte MINAS_NIVEL_DIFICIL = 99;
    
    public static final byte tipoUno = 1;
    public static final byte tipoDos = 2;
    public static final byte tipoTres = 3;
    public static final List<Casilla> tablero = new ArrayList<>();
    public static byte columnas = 0;
    public static byte filas = 0;
    public static byte numeroMinas = 0;
    
    public static void main(String[] args) {
        int puerto = 9988;
        try{
            byte dificultad;
            
            ServerSocket s;
            s = new ServerSocket(puerto);
            s.setReuseAddress(true);
            System.out.println("Buscaminas encendido");
            for(;;){
                System.out.println("Esperando jugador...");
                Socket cliente = s.accept();
                //Configuracion juego
                DataInputStream dis = new DataInputStream(cliente.getInputStream());
                dificultad = dis.readByte();
                if(dificultad==2){
                        columnas = filas = NIVEL_INTERMEDIO;
                        numeroMinas = MINAS_NIVEL_INTERMEDIO;
                }else if(dificultad==3){
                        filas = NIVEL_DIFICIL;
                        columnas = NIVEL_INTERMEDIO;
                        numeroMinas = MINAS_NIVEL_DIFICIL;
                }else{
                        columnas = filas = NIVEL_PRINCIPANTE;
                        numeroMinas = MINAS_NIVEL_PRINCIPANTE;
                }
                //Creación de un tablero para el usuario
                tablero.clear();
                crearTablero();
                //Envío de una tablero
                ObjectOutputStream oos = new ObjectOutputStream(cliente.getOutputStream());
                oos.writeObject(tablero);
                oos.flush();
                //Inicio temporizador
                String tempo = dis.readUTF();

                if(tempo.equals("OK")){
                    Date date = new Date();
                    DateFormat inicio = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    System.out.println(inicio.format(date));
                    
                    String marcador = dis.readUTF();
                    if(marcador.equals("WIN")){
                        System.out.println("Ganó");
                    }else{
                        System.out.println("Perdió");
                    }
                    date = new Date();
                    DateFormat fin = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    System.out.println(fin.format(date)); 
                }
                dis.close();
                oos.close();
                cliente.close();
                System.out.println("\n");
            }
        }catch(Exception e){
            e.toString();
        }
    }
    
    private static void crearTablero(){ 
        Casilla[][]conteo = new Casilla[filas][columnas];
        for(byte i = 0; i < filas;i++){
            for(byte j = 0; j < columnas; j++){
                if(i == 0 || i==(filas-1)){//Tipo 1
                    if(j == 0 || j==(columnas-1)){
                        Casilla aux = new Casilla(i,j,(byte)1);
                        tablero.add(aux);
                        conteo[i][j] = aux;
                    }else{ //Tipo 2
                        Casilla aux = new Casilla(i,j,(byte)2);
                        tablero.add(aux);
                        conteo[i][j] = aux;
                    }
                }else{
                    if(j==0||j==(columnas-1)){ //Tipo 2
                        Casilla aux = new Casilla(i,j,(byte)2);
                        tablero.add(aux);
                        conteo[i][j] = aux;
                    }else{
                        Casilla aux = new Casilla(i,j,(byte)3);
                        tablero.add(aux);
                        conteo[i][j] = aux;
                    }    
                }
            } //for
        }//for
        //LLenado de minas
        byte minasPuestas = 0;
        double ponerMina;
        for(int z = 0; z<tablero.size(); z=z+3 ){
            Casilla casillaActual = tablero.get(z);
            if(minasPuestas < numeroMinas){ //aun faltan minas
                if(z == tablero.size()-1){ //faltan pero recorri el tableo
                   z = 0;
                }
                if(casillaActual.getIsMina()==false){ 
                //Estoy en una casilla sin mina
                    ponerMina = Math.random();
                    if(ponerMina<0.5){ //Random
                        casillaActual.setIsMina(true);
                        minasPuestas++;
                        byte i_aux = casillaActual.getFila();
                        byte j_aux = casillaActual.getColumna();
                        //Se puso una mina, así que avisamos a sus vecinos
                        if(casillaActual.getTipo() == 1){
                            if(casillaActual.getFila()==0){ 
                               if(casillaActual.getColumna()==0){ //superior izquierda
                                   //System.out.println("superior izquierda, i:"+i_aux+"j: "+j_aux);
                                   conteo[0][1].hasMinaVecina();
                                   conteo[1][0].hasMinaVecina();
                                   conteo[1][1].hasMinaVecina();
                               }else if(casillaActual.getColumna()==(columnas-1)){ //superior derecha
                                   //System.out.println("superior derecha " +i_aux+"j: "+j_aux);
                                   conteo[0][(j_aux)-2].hasMinaVecina();
                                   conteo[1][(j_aux)-1].hasMinaVecina();
                                   conteo[1][(j_aux)-2].hasMinaVecina();
                               }
                           }else{ //casillaActual.getFila()==(filas-1)
                               if(casillaActual.getColumna()==0){ //inferior izquierda
                                    // System.out.println("inferior izquierda "+i_aux+"j: "+j_aux);
                                   conteo[(i_aux)-1][1].hasMinaVecina();
                                   conteo[(i_aux)-2][0].hasMinaVecina();
                                   conteo[(i_aux)-2][1].hasMinaVecina();
                               }else{//inferior derecha
                                   //System.out.println("inferior derecha "+i_aux+"j: "+j_aux);
                                   conteo[(i_aux)-1][(j_aux)-2].hasMinaVecina();
                                   conteo[(i_aux)-1][(j_aux)-2].hasMinaVecina();
                                   conteo[(i_aux)-2][(j_aux)-1].hasMinaVecina();
                               }
                           }
                        }else if(casillaActual.getTipo() == 2){
                            if(i_aux==0){//superiores
                                //System.out.println("superiores "+i_aux+"j: "+j_aux);
                                conteo[0][(j_aux)-1].hasMinaVecina();
                                conteo[0][(j_aux)+1].hasMinaVecina();
                                conteo[1][(j_aux)-1].hasMinaVecina();
                                conteo[1][(j_aux)+1].hasMinaVecina();
                                conteo[1][j_aux].hasMinaVecina();
                            }else if(i_aux==(filas-1)){//inferiores
                                //System.out.println("inferiores "+i_aux+"j: "+j_aux);
                                conteo[(filas-1)][(j_aux)-1].hasMinaVecina();
                                conteo[(filas-1)][(j_aux)+1].hasMinaVecina();
                                conteo[(filas-2)][(j_aux)-1].hasMinaVecina();
                                conteo[(filas-2)][(j_aux)+1].hasMinaVecina();
                                conteo[(filas-2)][(j_aux)].hasMinaVecina();
                            }else if(j_aux==0){//izquierda
                                //System.out.println("izquierdas "+i_aux+"j: "+j_aux);
                                conteo[(i_aux)-1][0].hasMinaVecina();
                                conteo[(i_aux)+1][0].hasMinaVecina();
                                conteo[(i_aux)-1][1].hasMinaVecina();
                                conteo[(i_aux)+1][1].hasMinaVecina();
                                conteo[i_aux][1].hasMinaVecina();
                            }else if(j_aux==(columnas-1)){//derecha
                                //System.out.println("derechas "+i_aux+"j: "+j_aux);
                                conteo[(i_aux)-1][j_aux].hasMinaVecina();
                                conteo[(i_aux)+1][j_aux].hasMinaVecina();
                                conteo[(i_aux)-1][(j_aux)-1].hasMinaVecina();
                                conteo[(i_aux)+1][(j_aux)-1].hasMinaVecina();
                                conteo[i_aux][(j_aux)-1].hasMinaVecina();
                            }
                        }else{
                            //System.out.println("centros " +i_aux+"j: "+j_aux);
                            conteo[i_aux][(j_aux)-1].hasMinaVecina();
                            conteo[i_aux][(j_aux)+1].hasMinaVecina();
                            conteo[(i_aux)-1][(j_aux)-1].hasMinaVecina();
                            conteo[(i_aux)-1][j_aux].hasMinaVecina();
                            conteo[(i_aux)-1][(j_aux)+1].hasMinaVecina();
                            conteo[(i_aux)+1][(j_aux)-1].hasMinaVecina();
                            conteo[(i_aux)+1][j_aux].hasMinaVecina();
                            conteo[(i_aux)+1][(j_aux)+1].hasMinaVecina();
                        }
                   }//mina puesta
               }
            }
        }//for minas
        //mostrando tablero
        for(int i=0; i<filas-1;i++){
            System.out.println("");
            for(int j = 0; j < columnas-1; j++){
                Casilla casilla = conteo[i][j];
                if(casilla.getIsMina()==false){
                        System.out.print(" "+casilla.getMinasAlrededor()+" ");
                }else{
                    System.out.print(" * ");
                }
            }
        }
        System.out.println("");
    }
}
