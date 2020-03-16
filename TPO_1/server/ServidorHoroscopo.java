package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

public class ServidorHoroscopo {

    private final static int PORT = 8000;

    public static void main(String[] args) {

        try {
            System.out.print("Inicializando servidor horoscopo en el puerto " + PORT + "... ");
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
                System.out.println("ServidorCentral> Pidió la predicción del siguiente signo [" + request +  "]");
                //se procesa la peticion y se espera resultado
                String strOutput = process(request);
                //Se imprime en consola "servidor"
                System.out.println("Horóscopo> La siguiente informacion será devuelta");
                System.out.println("Horócopo> \"" + strOutput + "\"");
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

        //frases
        String[] phrases = {"Jornada propicia para romper la rutina. Sorprende a tu pareja con un presente, o una salida sorpresa juntos.",
        "No hagas promesas de amor que no estas dispuesto a cumplir fielmente. No juegues con los sentimientos de los demás.",
        "Si necesitas de una relación mucho más intensa debes decirlo de forma clara. No puedes vivir sin cumplir tus deseos.",
        "Recibirás el toque mágico del afecto. Aprovecha el poder estar con quien deseas en el momento indicado.",
        "Procura dejar la presión de lado en la pareja. Otorga a tu pareja los tiempos que necesite para alcanzar la confianza.",
        "Tu pareja podría ser pura conversación y nada de acción. Presta atención a los detalles de la relación como cumpleaños y fechas importantes.",
        "Iniciarás la jornada de hoy con muy buen animo en pareja. Mantenlo durante el día mediante llamadas o mensajes a ella.",
        "Sufrirás una serie de malentendidos con la familia de tu pareja que pondrá cierta tensión en el aire. No temas hablarlo.",
        "Recibirás un sin fin de propuestas sentimentales. En principio querrás aceptarlas a todas, pero deberás dejar algunas.",
        "Las relaciones amorosas pasan por un buen momento, aparecen nuevas e interesantes personas que te motivan a conseguir ideales.",
        "Nuevas sensaciones te invadirán al encontrarte con un viejo amor del pasado. Jornada propicia para cenas románticas.",
        "No debes caer presa de la presión que pueda ejercer tu entorno acerca de tus sentimientos hacia tu pareja. Se tu mismo."};

        ArrayList<String> phrasesList = new ArrayList<String>();
        Collections.addAll(phrasesList, phrases);

        Collections.shuffle(phrasesList);

        result = phrasesList.get(0);

        return result;
    }
}
