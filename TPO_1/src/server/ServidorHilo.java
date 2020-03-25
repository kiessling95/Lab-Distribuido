package server;

import java.io.*;
import java.net.*;
import java.util.regex.*;
import java.util.concurrent.*;

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
        String fecha = "";
        String signo = "";
        String[] response = new String[2];
        FutureTask<String> solicitarHoroscopo = new FutureTask<String>(new Peticion ("", "", 0));
        FutureTask<String> solicitarPronostico = new FutureTask<String>(new Peticion ("", "", 0));
        ExecutorService executor = Executors.newFixedThreadPool(2);

        try {
            // Esto es lo que el cliente solicita
            request = dis.readUTF();

            Matcher escanearSigno = patronSigno.matcher(request);
            Matcher escanearFecha = patronFecha.matcher(request);

            if (escanearSigno.find()) {
                signo = escanearSigno.group();

                System.out.println("Prediccion del signo [" + signo + "] solicitada por el cliente con idSesion=" + this.idSessio);
                solicitarHoroscopo = new FutureTask<String>(new Peticion(signo, "localhost", 8000));

                if (hm.containsKey(signo)) {
                    response[0] = new String(hm.get(signo));
                    System.out.println("Se accedió al búffer para recuperar la predicción del cliente " + this.idSessio);
                } else {
                    // Comunicacion con el servidor del horoscopo
                    executor.submit(solicitarHoroscopo);
                }
            } else {
                response[0] = new String("");
            }

            if (escanearFecha.find()) {
                fecha = escanearFecha.group();

                System.out.println("Pronostico del dia [" + fecha + "] solicitado por el cliente " + this.idSessio);
                solicitarPronostico = new FutureTask<String>(new Peticion(fecha, "localhost", 7000));

                if(hm.containsKey(fecha)) {
                    response[1] = new String(hm.get(fecha));
                    System.out.println("Se accedió al búffer para recuperar el pronóstico del cliente " + this.idSessio);
                } else {
                    // Comunicacion con el servidor de pronosticos
                    executor.submit(solicitarPronostico);
                }
            } else {
                response[1] = new String("");
            }

            if (response[0] == null) {
                response[0] = new String(solicitarHoroscopo.get());
                hm.put(signo, response[0]);
            }

            if (response[1] == null) {
                response[1] = new String(solicitarPronostico.get());
                hm.put(fecha, response[1]);
            }

            // Devolvemos resultado al cliente
            executor.shutdown();
            dos.writeUTF(response[0] + response[1]);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

        desconectar();
    }
}
