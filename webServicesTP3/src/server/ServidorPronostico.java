package server;

import javax.jws.WebService;

import java.io.*;
import java.util.regex.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

@WebService(endpointInterface = "server.ServiciosPronostico")
public class ServidorPronostico implements ServiciosPronostico {

    @Override
    public String consultarPronostico(String request) {
		String respuesta = "Consulta recibida"; 

		System.out.println("Pronostico> Nueva peticion entrante: [" + request + "]");

		//Se procesa la peticion y se espera resultado
		respuesta = process(request); 

		System.out.println("Pronostico> Resultado de peticion: [" + request + "] es: " + respuesta);

		return respuesta;
    }

    /**
     * Procesa la peticion del cliente y retorna un resultado.
     * @param request peticion del cliente
     * @return el pronostico para el dia solicitado
     */
    public static String process(String request) {
        String result = "";
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

                result = "Temperatura: " + elemento.getElementsByTagName("temperatura").item(0).getTextContent() +
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

