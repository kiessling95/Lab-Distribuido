package server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;

public class HiloPronostico extends Thread {

    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
    private int idSessio;

    public Servidor_Horoscopo(Socket socket, int id) {
        this.socket = socket;
        this.idSessio = id;

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

            // Comunicacion con el servidor del horoscopo
            String strOutput = process(request);
            System.out.println("Pronostico solicitado por el cliente con idSesion " + this.idSessio);
            System.out.println(strOutput);

            // Devolvemos resultado al cliente
            dos.writeUTF(strOutput);
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
        } catch (IOException ex)) {
            System.err.println(ex.getMessage());
        }

        return response;
    }
}
