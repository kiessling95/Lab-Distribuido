package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServidorPronostico {

    private final static int PORT = 7000;

    public static void main(String[] args) {

        try {
            System.out.println("Inicializando servidor pronostico en el puerto " + PORT + " ... ");
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("\t[OK]");

            //Socket de cliente
            Socket clientSocket;
            while(true) {
                // en espera de conexion, si existe la acepta
                clientSocket = serverSocket.accept();
                System.out.println("Nueva conexión entrante: " + clientSocket);

                //Para leer lo que envie el cliente
                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                //para imprimir datos de salida
                PrintStream output = new PrintStream(clientSocket.getOutputStream());
                //se lee peticion del cliente
                String request = input.readLine();
                System.out.println("Cliente> He pedido el pronostico de [" + request +  "]");
                //se procesa la peticion y se espera resultado
                String strOutput = process(request);
                //Se imprime en consola "servidor"
                System.out.println("Servidor Horscopo> La siguiente informacion sera devuelta al cliente");
                System.out.println("Servidor Horsocopo> \"" + strOutput + "\"");
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
        int value = 0;

        Pattern patronFecha = Pattern.compile("(0?[1-9]|[12][0-9]|3[01])[- /.](0?[1-9]|1[012])[- /.](19\\d\\d|20\\d\\d)");
        Matcher matcher = patronFecha.matcher(request);

        if (matcher.find()) {
            int day = Integer.parseInt(matcher.group(1));
            int month = Integer.parseInt(matcher.group(2));
            int year = Integer.parseInt(matcher.group(3));

            value = (day + month + year) % 10;
        }

        switch (value) {
            case 0:
                result = "soleado";
                break;
            case 1:
                result = "ventoso";
                break;
            case 2:
                result = "lluvias";
                break;
            case 3:
                result = "nevadas";
                break;
            case 4:
                result = "granizo";
                break;
            case 5:
                result = "huracanes";
                break;
            case 6:
                result = "nublado";
                break;
            case 7:
                result = "posibles lluvias";
                break;
            case 8:
                result = "tormentas electricas";
                break;
            case 9:
                result = "niebla";
                break;
            default:
                result = "indeterminado";
        }

        return result;
    }
}
