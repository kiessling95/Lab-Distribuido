package server;

import javax.xml.ws.Endpoint;

public class PronosticoPublisher {

    public static void main(String[] args) {
        // Si no se ingresan la cantidad de parametros correctos
        if (args.length < 2) {
            System.err.println("Uso: ServidorPronostico IPLocal PuertoLocal");
            return;
        }
        
        Endpoint.publish("http://"+args[0]+":"+args[1]+"/ws/Pronostico", new ServidorPronostico());
    }
}
