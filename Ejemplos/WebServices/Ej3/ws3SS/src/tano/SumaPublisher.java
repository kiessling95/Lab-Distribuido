package tano;

import javax.xml.ws.Endpoint;

//Endpoint publisher
public class SumaPublisher {

    public static void main(String[] args) {
        Endpoint.publish("http://localhost:7780/ws/Sumador", new SumaImpl());
    }
}
