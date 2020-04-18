import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;

import java.io.*;
import java.util.regex.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;


public class ServidorPronostico extends UnicastRemoteObject implements ServiciosPronostico {

    protected ServidorPronostico() throws RemoteException {
        super();
    }

    /**
     * @param args argumentos de la linea de comando
     * @param args[0] una direccion IP (localhost es valido)
     * @param args[1] un numero de puerto (debe tener asociado un servicio RMI Registry)
     */
    public static void main(String[] args) { 
        // Si no se ingresan la cantidad de parametros correctos
        if (args.length < 2) { 
            System.err.println("Uso: ServidorPronostico IPLocal PuertoLocal");
            return;
        }

        try {
            // Se instancian los servicios
            ServiciosPronostico serv = new ServidorPronostico(); 
            
            LocateRegistry.createRegistry(Integer.parseInt(args[1]));
            
            // Se asocia una URL a la IP y puerto de los parametros
            Naming.rebind("rmi://" + args[0] + ":" + args[1] + "/ServidorPronostico",serv); 

        } catch (RemoteException e) {
            System.err.println("Error de comunicacion: " + e.toString());
            System.exit(1); 
        } catch (Exception e) {
            System.err.println("Excepcion en Servidor:");
            e.printStackTrace();
            System.exit(1); 
        }
    }

    /**
     * @param request una fecha de consulta
     * @return una predicción del tiempo acorde a la fecha enviada por parametro
     */
    @Override
    public String consultarPronostico(String request) throws RemoteException {
      
        String respuesta = "Consulta recibida";

        System.out.println("Cliente> peticion [" + request + "]");

        respuesta = process(request); // Se procesa la consulta para obtener la prediccion


        System.out.println("Pronostico> Resultado de peticion");
        System.out.println("Pronostico> \"" + respuesta + "\"");

        return respuesta; // Se devuelve la prediccion
    }

    /**
     * Procesa la peticion del cliente y retorna un resultado.
     * @param request peticion del cliente
     * @return el pronostico para el dia solicitado
     */
    public static String process(String request) {
        String result = "El pronostico del dia: ";
        int value = 0;

        Pattern patronFecha = Pattern.compile("(0?[1-9]|[12][0-9]|3[01])[- /.](0?[1-9]|1[012])[- /.](\\d{2,4})");
        Matcher matcher = patronFecha.matcher(request);
       
        try {
            File file = new File("Pronosticos.xml");
            //an instance of factory that gives a document builder
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //an instance of builder to parse the specified xml file
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("dia");
            matcher.find();

            int day = Integer.parseInt(matcher.group(1));
            int month = Integer.parseInt(matcher.group(2));
            int year = Integer.parseInt(matcher.group(3));

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
            Thread.sleep((long) (Math.random()*(6000-1000)+1000));
        } catch (InterruptedException ex) {
            System.err.println(ex.getMessage());
        }

        return result;
    }
}

