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
        String[] phrases = {
        "Jornada propicia para romper la rutina. Sorprende a tu pareja con un presente, o una salida sorpresa juntos.",
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
        "No debes caer presa de la presión que pueda ejercer tu entorno acerca de tus sentimientos hacia tu pareja. Se tu mismo.",
        "Vas a superar esto. No durará para siempre. Lo que vives en este momento es temporal. El dolor que sientes, en unos meses se convertirá solo en un recuerdo.",
        "Es verdad, todo apesta en este momento, pero te sorprenderás las grandes cosas que te están esperando. Todo lo que has soñado está a tu alcance. Continúa avanzando hacia esa vida de ensueño que deseas.",
        "La mejor venganza es aprender a amarte a ti misma. No te gastes por demostrarlo a los demás. Toma la vida lo mejor posible ¡no importa si te lo reconocen o no los demás!",
        "Mereces ser feliz, nunca permitas que te digan lo contrario. Tu corazón está lleno de bondad y eso es muy difícil de encontrar. Somos afortunados quienes podemos compartir una tarde contigo, lo mejor sería que tú nos mantengas cerca el resto de tu vida.",
        "Verás que todo tu esfuerzo será recompensado. Estás trabajando muy duro para alcanzar tus metas; aprende a ser paciente. Llegará lo que tanto anhelas",
        "Perder una batalla, no significa perder la guerra. No permitas que ningún fracaso te aleje de tus sueños, Un mal día no significa que toda tu vida apesta. Si te rompen el corazón, no es sinónimo de renunciar al amor",
        "Eres hermosa tanto por dentro como por fuera; mereces una relación sana llena de amor. Si alguien te hace dudar de ello, no vale la pena que desperdicies ni un segundo de tu tiempo en él",
        "Has evolucionado, ya no eres la misma persona que eras ayer. Lograste florecer, así que deja de preocuparte por tus errores del pasado y enfócate en construir tu futuro",
        "Aprende a amarte, deja de mirarte de esa forma poco amorosa. Ni eres una carga para los demás, ni estás llena de tantos defectos como lo piensas. Quiérete mucho",
        "Deja la frustración a un lado, ¡mira todo lo que has avanzado rumbo a tus metas! Siéntete orgullosa de tus logros y deja de criticarte tanto. Lo estás haciendo perfecto",
        "Tu fuerza interior es superior a lo que te imaginas. Mírate desde otra perspectiva para que puedas apreciar tus logros ¡has realizado todo lo que una vez soñaste!",
        "Sal a buscar tus sueños, persigue tu felicidad, yo estoy aquí para apoyarte en todo momento, no tienes porqué sentirte sola, porque voy sosteniendo tu mano, acompañándote en todo el camino"
};

        ArrayList<String> phrasesList = new ArrayList<String>();
        Collections.addAll(phrasesList, phrases);

        Collections.shuffle(phrasesList);

        result = phrasesList.get(0);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            System.err.println(ex.getMessage());
        }

        return result;
    }
}
