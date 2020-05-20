package wsServer;

import javax.xml.ws.Endpoint;

public class PronosticoPublisher {
	
	public static void main(String[] args) {
		Endpoint.publish("http://localhost:7000/ws/Pronostico", new PronosticoImpl());
	}
}
