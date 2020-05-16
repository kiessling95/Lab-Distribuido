package cliente;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.util.Scanner;

public class Cliente {

    public static void main(String[] args) throws MalformedURLException {
        
        if (args.length < 2) {
            System.err.println("Uso: Cliente IPServidor PuertoServidor");
            return;
        }

        String ipServidor     = args[0];
        String puertoServidor = args[1];
        URL url = new URL("http://"+ipServidor+":"+puertoServidor+"/ws/Servidor?wsdl");
        
        QName qname = new QName("http://server/", "ServiciosImplService");

        Service service = Service.create(url, qname);
        Servicios serv  = service.getPort(Servicios.class);


        while (true) {
            System.out.println("\nIngrese peticion o escriba exit para salir: ");
            String entrada = new Scanner(System.in).nextLine();
            if (entrada.equals("exit")) break;

            System.out.println("Enviando...");
            // Invocacion remota
            System.out.println("Respuestas: \n" + serv.consultar(entrada));
        }
    }
}
