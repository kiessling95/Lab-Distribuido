package server;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServidorHoroscopo {

    private final static int PORT = 8000;

    public static void main(String[] args) {

        try {
            //Socket de servidor para esperar peticiones de la red
            ServerSocket serverSocket = new ServerSocket(PORT);
            String ip = InetAddress.getLocalHost().getHostAddress();
            System.out.println("Inicializando servidor horoscopo en el puerto " + PORT + " con IP " + ip + "\t[OK]");

            //Socket de cliente, en este caso el cliente sera el ServidorCentral
            Socket clientSocket;
            while(true) {
                // En espera de conexion, si existe la acepta
                clientSocket = serverSocket.accept();
                System.out.println("Nueva conexion entrante: " + clientSocket);

                //Para leer lo que envie el cliente
                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                //para imprimir datos de salida
                PrintStream output = new PrintStream(clientSocket.getOutputStream());
                //se lee peticion del cliente
                String request = input.readLine();
                System.out.println("ServidorCentral> Pidio la prediccion del siguiente signo [" + request +  "]");
                //se procesa la peticion y se espera resultado
                String strOutput = process(request);
                //Se imprime en consola "servidor"
                System.out.println("Horoscopo> La siguiente informacion sera devuelta");
                System.out.println("Horocopo> \"" + strOutput + "\"");
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
        "No hagas promesas de amor que no estas dispuesto a cumplir fielmente. No juegues con los sentimientos de los demas.",
        "Si necesitas de una relacion mucho mas intensa debes decirlo de forma clara. No puedes vivir sin cumplir tus deseos.",
        "Recibiras el toque magico del afecto. Aprovecha el poder estar con quien deseas en el momento indicado.",
        "Procura dejar la presión de lado en la pareja. Otorga a tu pareja los tiempos que necesite para alcanzar la confianza.",
        "Tu pareja podria ser pura conversacion y nada de accion. Presta atencion a los detalles de la relacion como cumpleanios y fechas importantes.",
        "Iniciaras la jornada de hoy con muy buen animo en pareja. Mantenlo durante el dia mediante llamadas o mensajes a ella.",
        "Sufriras una serie de malentendidos con la familia de tu pareja que pondra cierta tension en el aire. No temas hablarlo.",
        "Recibiras un sin fin de propuestas sentimentales. En principio querras aceptarlas a todas, pero deberas dejar algunas.",
        "Las relaciones amorosas pasan por un buen momento, aparecen nuevas e interesantes personas que te motivan a conseguir ideales.",
        "Nuevas sensaciones te invadiran al encontrarte con un viejo amor del pasado. Jornada propicia para cenas romanticas.",
        "No debes caer presa de la presion que pueda ejercer tu entorno acerca de tus sentimientos hacia tu pareja. Se tu mismo.",
        "Vas a superar esto. No durara para siempre. Lo que vives en este momento es temporal. El dolor que sientes, en unos meses se convertira solo en un recuerdo.",
        "Es verdad, todo apesta en este momento, pero te sorprenderas las grandes cosas que te estan esperando. Todo lo que has soniado esta a tu alcance. Continua avanzando hacia esa vida de ensuenio que deseas.",
        "La mejor venganza es aprender a amarte a ti misma. No te gastes por demostrarlo a los demas. Toma la vida lo mejor posible, no importa si te lo reconocen o no los demas!",
        "Mereces ser feliz, nunca permitas que te digan lo contrario. Tu corazon está lleno de bondad y eso es muy dificil de encontrar. Somos afortunados quienes podemos compartir una tarde contigo, lo mejor seria que tu nos mantengas cerca el resto de tu vida.",
        "Veras que todo tu esfuerzo sera recompensado. Estas trabajando muy duro para alcanzar tus metas; aprende a ser paciente. Llegara lo que tanto anhelas",
        "Perder una batalla, no significa perder la guerra. No permitas que ningun fracaso te aleje de tus suenios, Un mal dia no significa que toda tu vida apesta. Si te rompen el corazon, no es sinonimo de renunciar al amor",
        "Eres hermosa tanto por dentro como por fuera; mereces una relacion sana llena de amor. Si alguien te hace dudar de ello, no vale la pena que desperdicies ni un segundo de tu tiempo en el",
        "Has evolucionado, ya no eres la misma persona que eras ayer. Lograste florecer, asi que deja de preocuparte por tus errores del pasado y enfocate en construir tu futuro",
        "Aprende a amarte, deja de mirarte de esa forma poco amorosa. Ni eres una carga para los demas, ni estas llena de tantos defectos como lo piensas. Quierete mucho",
        "Deja la frustracion a un lado, mira todo lo que has avanzado rumbo a tus metas! Sientete orgullosa de tus logros y deja de criticarte tanto. Lo estas haciendo perfecto",
        "Tu fuerza interior es superior a lo que te imaginas. Mirate desde otra perspectiva para que puedas apreciar tus logros, has realizado todo lo que una vez soniaste!",
        "Sal a buscar tus suenios, persigue tu felicidad, yo estoy aqui para apoyarte en todo momento, no tienes por que sentirte sola, porque voy sosteniendo tu mano, acompaniandote en todo el camino"
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
