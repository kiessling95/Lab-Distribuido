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
                System.out.println("Nueva conexion entrante: " + clientSocket);

                //Para leer lo que envie el cliente
                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                //para imprimir datos de salida
                PrintStream output = new PrintStream(clientSocket.getOutputStream());
                //se lee peticion del cliente
                String request = input.readLine();
                System.out.println("ServidorCentral> Pidio el pronostico del dia [" + request +  "]");
                //se procesa la peticion y se espera resultado
                String strOutput = process(request);
                //Se imprime en consola "servidor"
                System.out.println("Pronostico> La siguiente informacion sera devuelta");
                System.out.println("Pronostico> \"" + strOutput + "\"");
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
        String result = " El pronostico del dia ";
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
                result += 
                "Temperatura : 15 - 35°C - Cielo despejado - Viento: Noreste 10 km/h - Presion: 1001 hPa";
                break;
            case 1:
                result += 
                "Temperatura : 9 - 20°C - Nublado con tormentas - Viento: Norte 32 km/h Presion: 1003 hPa";
                break;
            case 2:
                result +=
                "Temperatura : 10 - 25°C - Nublado con lluvias aisladas - Viento: Este 40 km/h - Presion: 1003 hPa";
                break;
            case 3:
                result +=
                "Temperatura : 20 - 25,2°C - Tormentas electricas - Viento: Norte 50 km/h - Presion: 1500 hPa";
                break;
            case 4:
                result +=
                "Temperatura : 25,2°C - Se espera Huracan - Viento: Oeste 100 km/h - Presion: 1200 hPa";
                break;
            case 5:
                result +=
                "Temperatura : 15 - 30°C - Cielo despejado, el dia estara hermoso para quedarse en cuarentena - Viento: Sureste 20 km/h - Presion: 1003 hPa";
                break;
            case 6:
                result += 
                "Temperatura : -9 - 10°C - Cielo algo nublado - Viento: Sur 43 km/h - Presion: 950 hPa";
                break;
            case 7:
                result +=
                "Temperatura : 15 - 25°C - Cielo nublado - Viento: Suroeste 32 km/h - Presion: 1003 hPa";
                break;
            case 8:
                result +=
                "Temperatura : 20 - 25,2°C - Chaparrones durante la tarde noche - Viento: Sur 52 km/h - Presión: 1003 hPa";
                break;
            case 9:
                result +=
                "Temperatura : 25 - 41°C - Cielo despejado - Viento: Norte 32 km/h - Presion: 1003 hPa";
                break;
            default:
                result +=
                "Temperatura : 30 - 43°C - Infierno en la tierra, evitar salir - Viento: --  km/h - Presion: 2000 hPa";
        }

        try {
            Thread.sleep(6000);
        } catch (InterruptedException ex) {
            System.err.println(ex.getMessage());
        }

        return result;
    }
}
