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
    private String ipServidorHoroscopo, ipServidorPronostico;

    public ServidorHilo(Socket socket, int id, ConcurrentHashMap<String,String> hashmap, String ipSH, String ipSP) {
        this.socket = socket;
        this.idSessio = id;
        this.hm = hashmap;
        this.patronFecha = Pattern.compile("\\b(0?[1-9]|[12][0-9]|3[01])[- /.](0?[1-9]|1[012])[- /.](\\d{2,4})\\b");
        this.patronSigno = Pattern.compile("aries|tauro|geminis|cancer|leo|virgo|libra|escorpio|sagitario|capricornio|acuario|piscis",
                Pattern.CASE_INSENSITIVE);
        this.ipServidorHoroscopo = ipSH;
        this.ipServidorPronostico = ipSP;

        try {
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @Override
    public void run() {
        String request = "";
        String fecha = "";
        String fechaNormalizada = "";
        String signoNormalizada = "";
        String signo = "";
        String[] response = new String[2];
        FutureTask<String> solicitarHoroscopo = new FutureTask<String>(new Peticion ("", "", 0));
        FutureTask<String> solicitarPronostico = new FutureTask<String>(new Peticion ("", "", 0));
        ExecutorService executor = Executors.newFixedThreadPool(2);

        try {
            // Esto es lo que el cliente solicita
            request = dis.readUTF();

            if (request.equals("exit")) {
                dos.writeUTF("Se despide del cliente");
                socket.close();

                return;
            }

            Matcher escanearSigno = patronSigno.matcher(request);
            Matcher escanearFecha = patronFecha.matcher(request);

            if (escanearSigno.find()) {
                signo = escanearSigno.group();

                System.out.println("Prediccion del signo [" + signo + "] solicitada por el cliente " + this.idSessio);
                solicitarHoroscopo = new FutureTask<String>(new Peticion(signo, ipServidorHoroscopo, 8000));

                // si esta en cache , caso contrario lo solicita al server 
                signoNormalizada = signo.toLowerCase();
                if (hm.containsKey(signoNormalizada)) {
                    response[0] = new String(hm.get(signoNormalizada));
                    System.out.println("Se accedio a cache para recuperar la prediccion del cliente " + this.idSessio);
                } else {
                    // Comunicacion con el servidor del horoscopo
                    executor.submit(solicitarHoroscopo);
                }
            } else {
                response[0] = new String("No se detecto ningun signo o fue escrito incorrectamente.");
            }

            if (escanearFecha.find()) {
                fecha = escanearFecha.group();

                System.out.println("Pronostico del dia [" + fecha + "] solicitado por el cliente " + this.idSessio);
                solicitarPronostico = new FutureTask<String>(new Peticion(fecha, ipServidorPronostico, 7000));

                // si esta en cache la extraigo, sino la solicito
                fechaNormalizada = fecha.replaceAll("[^0-9]","");
                if (hm.containsKey(fechaNormalizada)) {
                    response[1] = new String(hm.get(fechaNormalizada));
                    System.out.println("Se accedio a cache para recuperar el pronostico del cliente " + this.idSessio);
                } else {
                    // Comunicacion con el servidor de pronosticos
                    executor.submit(solicitarPronostico);
                }
            } else {
                response[1] = new String("No se detecto ninguna fecha o fue escrita incorrectamente.");
            }

            // Los "futuros" fueron a buscar los datos, a continuacion se los almacena pero
            // si la matriz ya fue generada significa que ya estaban el buffer o hubo un error!

            if (response[0] == null) {
                response[0] = new String(solicitarHoroscopo.get());
                hm.put(signoNormalizada, response[0]);
            }

            if (response[1] == null) {
                response[1] = new String(solicitarPronostico.get());
                hm.put(fechaNormalizada, response[1]);
            }

            // Devolvemos resultado al cliente
            dos.writeUTF(response[0] + "\n" + response[1]);
            executor.shutdown();
            socket.close();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
}
