package server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HiloPronostico extends Thread {

    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
    private int idSessio;
    private Pattern patronFecha;

    public HiloPronostico(Socket socket, int id) {
        this.socket = socket;
        this.idSessio = id;
        this.patronFecha = Pattern.compile("(0?[1-9]|[12][0-9]|3[01])[- /.](0?[1-9]|1[012])[- /.](19\\d\\d|20\\d\\d)");

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
        try {
            // Esto es lo que el cliente solicita, en este caso un signo zoodiacal
            request = dis.readUTF();

            Matcher escanearFecha = patronFecha.matcher(request);

            if (escanearFecha.find()) {
                String fecha = escanearFecha.group();

                // Comunicacion con el servidor de pronosticos
                String strOutput = process(fecha);
                System.out.println("Pronostico del dia " + fecha + " solicitada por el cliente con idSesion " + this.idSessio);

                // Devolvemos resultado al cliente
                dos.writeUTF(strOutput);
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }

        desconectar();
    }

    public String process(String request) {
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
