package server;

import java.io.*;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.concurrent.ConcurrentHashMap;

public class ServidorHilo extends Thread {

    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
    private int idSessio;
    private Pattern patronSigno, patronFecha;
    private ConcurrentHashMap<String,String> hm;

    public ServidorHilo(Socket socket, int id, ConcurrentHashMap<String,String> hashmap) {
        this.socket = socket;
        this.idSessio = id;
        this.hm = hashmap;
        this.patronFecha = Pattern.compile("(0?[1-9]|[12][0-9]|3[01])[- /.](0?[1-9]|1[012])[- /.]\\d?\\d?(\\d{2})");
        this.patronSigno = Pattern.compile("aries|tauro|geminis|cancer|leo|virgo|libra|escorpio|sagitario|capricornio|acuario|piscis",
                Pattern.CASE_INSENSITIVE);

        try {
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void desconectar() {
        try {
            socket.close();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @Override
    public void run() {
        String request = "";
        String response = "";
        try {
            // Esto es lo que el cliente solicita
            request = dis.readUTF();

            Matcher escanearSigno = patronSigno.matcher(request);
            Matcher escanearFecha = patronFecha.matcher(request);

            if (escanearSigno.find()) {
                String signo = escanearSigno.group();

                System.out.println("Prediccion de " + signo + " solicitada por el cliente con idSesion=" + this.idSessio);

                if (hm.containsKey(signo)) {
                    response += hm.get(signo);
                    System.out.println("Se accedió al búffer para recuperar la predicción del cliente " + this.idSessio);
                } else {
                    // Comunicacion con el servidor del horoscopo
                    String rpta = procesarSigno(signo);

                    hm.put(signo, rpta);
                    response += rpta;
                }
            }

            if (escanearFecha.find()) {
                String fecha = escanearFecha.group();

                System.out.println("Pronostico del dia " + fecha + " solicitado por el cliente " + this.idSessio);

                if(hm.containsKey(fecha)) {
                    response += hm.get(fecha);
                    System.out.println("Se accedió al búffer para recuperar el pronóstico del cliente " + this.idSessio);
                } else {
                    // Comunicacion con el servidor de pronosticos
                    String rpta = procesarFecha(fecha);

                    hm.put(fecha, rpta);
                    response += procesarFecha(fecha);
                }
            }

            // Devolvemos resultado al cliente
            dos.writeUTF(response);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }

        desconectar();
    }

    public String procesarSigno(String request) {
        final String SERVIDORHOROSCOPO = "localhost";
        final int PORT = 8000;
        String response = "";

        Socket socket;

        try {
            socket = new Socket(SERVIDORHOROSCOPO, PORT);

            //Para leer lo que envie el servidor
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //para imprimir datos del servidor
            PrintStream output = new PrintStream(socket.getOutputStream());

            //manda signo al servidor
            output.println(request);
            //captura respuesta del servidor (una prediccion)
            response = input.readLine();
            //if( st != null ) System.out.println("Servidor> " + st );

            socket.close();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }

        return response;
    }

    public String procesarFecha(String request) {
        final String SERVIDORPRONOSTICO = "localhost";
        final int PORT = 7000;
        String response = "";

        Socket socket;

        try {
            socket = new Socket(SERVIDORPRONOSTICO, PORT);

            //Para leer lo que envie el servidor
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //para imprimir datos del servidor
            PrintStream output = new PrintStream(socket.getOutputStream());

            //manda signo al servidor
            output.println(request);
            //captura respuesta del servidor (una prediccion)
            response = input.readLine();
            //if( st != null ) System.out.println("Servidor> " + st );

            socket.close();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }

        return response;
    }
}
