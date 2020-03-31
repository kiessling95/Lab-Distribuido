package server;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServidorHoroscopo {

    private final static int PORT = 8000;

    public static void main(String[] args) {

        try {
            //Socket de servidor para esperar peticiones de la red
            ServerSocket serverSocket = new ServerSocket(PORT);
            String ip = InetAddress.getLocalHost().getHostAddress();
            System.out.println("Inicializando servidor horoscopo en el puerto " + PORT + " con IP " + ip + "\t[OK]");

            //Socket de cliente, en este caso el cliente sera el ServidorCentral
            Socket clientSocket;
            while(true) {
                // En espera de conexion, si existe la acepta
                clientSocket = serverSocket.accept();
                System.out.println("Nueva conexion entrante: " + clientSocket);

                //Para leer lo que envie el cliente
                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                //para imprimir datos de salida
                PrintStream output = new PrintStream(clientSocket.getOutputStream());
                //se lee peticion del cliente
                String request = input.readLine();
                System.out.println("ServidorCentral> Pidio la prediccion del siguiente signo [" + request +  "]");
                //se procesa la peticion y se espera resultado
                String strOutput = process(request);
                //Se imprime en consola "servidor"
                System.out.println("Horoscopo> La siguiente informacion sera devuelta");
                System.out.println("Horocopo> \"" + strOutput + "\"");
                //se imprime en cliente
                output.flush();//vacia contenido
                output.println(strOutput);
                //cierra conexion
                clientSocket.close();
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    /**
     * procesa peticion del cliente y retorna resultado
     * @param request peticion del cliente
     * @return String
     */
    public static String process(String request) {
        String result = "";
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;

        try {
            try {
                archivo = new File ("Horoscopo.txt");
                System.out.println(archivo.getAbsolutePath());
                fr = new FileReader(archivo);
                br = new BufferedReader(fr);
                
                // Lectura del fichero

                String linea;
                ArrayList<String> phrasesList = new ArrayList<String>();
                while((linea=br.readLine())!=null){
                    phrasesList.add(linea);
                }
                Collections.addAll(phrasesList);

                Collections.shuffle(phrasesList);

                result = phrasesList.get(0);

            } catch (Exception e) {
                e.printStackTrace();
                //TODO: handle exception
            }finally{
                // En el finally cerramos el fichero, para asegurarnos
                // que se cierra tanto si todo va bien como si salta 
                // una excepcion.
                try{                    
                   if( null != fr ){   
                      fr.close();     
                   }                  
                }catch (Exception e2){ 
                   e2.printStackTrace();
                }
             }
            
            
            
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            System.err.println(ex.getMessage());
        }

        return result;
    }
}
