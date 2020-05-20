package wsCliente;
import wsServer.Servidor;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import java.util.Scanner;

public class Cliente {

    public static void main(String[] args) throws MalformedURLException {
    	
    	URL url= new URL("http://localhost:7779/ws/Servidor?wsdl");
        
        // Qnames
        QName qname = new QName("http://wsServer/", "ServidorImplService");
        
        QName portname = new QName("http://wsServer/","ServidorImplPort");

        Service service = Service.create(url, qname);
        
        Servidor servicio = service.getPort(portname,Servidor.class);




        while (true) {
            System.out.println("\nIngrese peticion o escriba exit para salir: ");
            String entrada = new Scanner(System.in).nextLine();
            if (entrada.equals("exit")) break;

            System.out.println("Enviando...");
            // Invocacion remota
            System.out.println("Respuestas: \n" + servicio.consultar(entrada));
        }
    }
}
