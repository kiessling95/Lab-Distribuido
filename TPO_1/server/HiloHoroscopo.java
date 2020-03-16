package server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HiloHoroscopo extends Thread {

    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
    private int idSessio;
    private Pattern patronSigno;

    public HiloHoroscopo(Socket socket, int id) {
        this.socket = socket;
        this.idSessio = id;
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
        try {
            // Esto es lo que el cliente solicita, en este caso un signo zoodiacal
            request = dis.readUTF();

            Matcher escanearSigno = patronSigno.matcher(request);

            if (escanearSigno.find()) {
                String signo = escanearSigno.group();

                // Comunicacion con el servidor del horoscopo
                String strOutput = process(signo);
                System.out.println("Prediccion de " + signo + " solicitada por el cliente con idSesion " + this.idSessio);

                // Devolvemos resultado al cliente
                dos.writeUTF(strOutput);
            }

        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }

        desconectar();
    }

    public String process(String request) {
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
}
