package tano;

import javax.xml.ws.Endpoint;

//Endpoint publisher
public class CalcPublisher {

    public static void main(String[] args) {
        Endpoint.publish("http://localhost:7779/ws/Calculadora", new CalcImpl());
    }
}
