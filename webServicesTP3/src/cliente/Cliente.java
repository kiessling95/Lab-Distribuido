package cliente;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.util.Scanner;

import server.Servidor;

public class Cliente {

    public static void main(String[] args) throws MalformedURLException {
        
        if (args.length < 2) {
            System.err.println("Uso: Cliente IPServidor PuertoServidor");
            return;
        }

        String ipServidor     = args[0];
        String puertoServidor = args[1];
        URL url = new URL("http://"+ipServidor+":"+puertoServidor+"/ws/Servidor?wsdl");
        
        QName qname = new QName("http://server/", "ServidorImplService");

        QName portname = new QName("http://server/","ServidorImplPort");

        Service service = Service.create(url, qname);
        Servidor serv  = service.getPort(portname,Servidor.class);


        while (true) {
            System.out.println("\n Ingrese peticion o escriba exit para salir: ");
            String entrada = new Scanner(System.in).nextLine();
            if (entrada.equals("exit")) break;

            System.out.println("Enviando...");
            // Invocacion remota
            System.out.println("Respuestas: \n" + serv.consultar(entrada));
        }
    }
}
