package server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;

public class ServidorPronostico {

    private final static int PORT = 7000;

    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            String ip = InetAddress.getLocalHost().getHostAddress();
            System.out.println("Inicializando servidor pronostico en el puerto " + PORT + " con IP " + ip + "\t[OK]");

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
                System.out.println("ServidorCentral> Pidió el pronóstico del día [" + request +  "]");
                //se procesa la peticion y se espera resultado
                String strOutput = process(request);
                //Se imprime en consola "servidor"
                System.out.println("Pronóstico> La siguiente información será devuelta");
                System.out.println("Pronóstico> \"" + strOutput + "\"");
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
        String result = " El día ";
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
                result += "será soleado.";
                break;
            case 1:
                result += "será ventoso.";
                break;
            case 2:
                result += "será lluvioso.";
                break;
            case 3:
                result += "tendrá nevadas.";
                break;
            case 4:
                result += "tendrá granizo.";
                break;
            case 5:
                result += "tendrá huracanes.";
                break;
            case 6:
                result += "estará nublado.";
                break;
            case 7:
                result += "tendrá posibles lluvias.";
                break;
            case 8:
                result += "tendrá tormentas electricas.";
                break;
            case 9:
                result += "tendrá nieblas.";
                break;
            default:
                result += "será indeterminado.";
        }

        try {
            Thread.sleep(6000);
        } catch (InterruptedException ex) {
            System.err.println(ex.getMessage());
        }

        return result;
    }
}
