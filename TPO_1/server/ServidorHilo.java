package server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServidorHilo extends Thread {

    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
    private int idSessio;
    private Pattern patronSigno, patronFecha;

    public ServidorHilo(Socket socket, int id) {
        this.socket = socket;
        this.idSessio = id;
        this.patronFecha = Pattern.compile("(0?[1-9]|[12][0-9]|3[01])[- /.](0?[1-9]|1[012])[- /.](19\\d\\d|20\\d\\d)");
        this.patronSigno = Pattern.compile("[aA]ries|[tT]auro|[gG].minis|[cC].ncer|[lL]eo|[vV]irgo]|[lL]ibra|[eE]scorpio|[sS]agitario|[cC]apricornio|[aA]cuario|[pP]iscis");

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
            // Esto es lo que el cliente solicita, en este caso un signo zoodiacal
            request = dis.readUTF();

            Matcher escanearSigno = patronSigno.matcher(request);
            Matcher escanearFecha = patronFecha.matcher(request);

            if (escanearSigno.find()) {
                String signo = escanearSigno.group();

                // Comunicacion con el servidor del horoscopo
                response += procesarSigno(signo);
                System.out.println("Prediccion de " + signo + " solicitada por el cliente con idSesion " + this.idSessio);
            }

            if (escanearFecha.find()) {
                String fecha = escanearFecha.group();

                // Comunicacion con el servidor de pronosticos
                response += procesarFecha(fecha);
                System.out.println("Pronostico del dia " + fecha + " solicitada por el cliente con idSesion " + this.idSessio);
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
