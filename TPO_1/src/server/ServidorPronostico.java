package server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class ServidorPronostico {

    private final static int PORT = 7000;

    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            String ip = InetAddress.getLocalHost().getHostAddress();
            System.out.println("Inicializando servidor pronostico en el puerto " + PORT + " con IP " + ip + "\t[OK]");

            //Socket de cliente
            Socket clientSocket;
            while(true) {
                // en espera de conexion, si existe la acepta
                clientSocket = serverSocket.accept();
                System.out.println("Nueva conexion entrante: " + clientSocket);

                //Para leer lo que envie el cliente
                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                //para imprimir datos de salida
                PrintStream output = new PrintStream(clientSocket.getOutputStream());
                //se lee peticion del cliente
                String request = input.readLine();
                System.out.println("ServidorCentral> Pidio el pronostico del dia [" + request +  "]");
                //se procesa la peticion y se espera resultado
                String strOutput = process(request);
                //Se imprime en consola "servidor"
                System.out.println("Pronostico> La siguiente informacion sera devuelta");
                System.out.println("Pronostico> \"" + strOutput + "\"");
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
        String result = "El pronostico del dia: ";
        int value = 0;

        Pattern patronFecha = Pattern.compile("(0?[1-9]|[12][0-9]|3[01])[- /.](0?[1-9]|1[012])[- /.](19\\d\\d|20\\d\\d)");
        Matcher matcher = patronFecha.matcher(request);

        int day = Integer.parseInt(matcher.group(1));
        int month = Integer.parseInt(matcher.group(2));
        int year = Integer.parseInt(matcher.group(3));

        try {
            File file = new File("Pronosticos.xml");
            //an instance of factory that gives a document builder
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //an instance of builder to parse the specified xml file
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("dia");
            value = (day + month + year) % nodeList.getLength();

            Node node = nodeList.item(value);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elemento = (Element) node;

                result += "Temperatura: " + elemento.getElementsByTagName("temperatura").item(0).getTextContent() +
                        " - Viento: " + elemento.getElementsByTagName("viento").item(0).getTextContent() +
                        " - Presion: " + elemento.getElementsByTagName("presion").item(0).getTextContent() +
                        " - Humedad: "+ elemento.getElementsByTagName("humedad").item(0).getTextContent() +
                        " - Notas: "+ elemento.getElementsByTagName("notas").item(0).getTextContent();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(6000);
        } catch (InterruptedException ex) {
            System.err.println(ex.getMessage());
        }

        return result;
    }
}
