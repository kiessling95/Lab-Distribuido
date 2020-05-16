package server;

import javax.xml.ws.Endpoint;

public class HoroscopoPublisher {

    public static void main(String[] args) {
        // Si no se ingresan la cantidad de parametros correctos
        if (args.length < 2) {
            System.err.println("Uso: ServidorHoroscopo IPLocal PuertoLocal");
            return;
        }
        
        Endpoint.publish("http://"+args[0]+":"+args[1]+"/ws/Horoscopo", new ServidorHoroscopo());
    }
}
