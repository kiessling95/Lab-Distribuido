package wsServer;

import javax.xml.ws.Endpoint;

public class ServidorPublisher {

	public static void main(String[] args) {
		   Endpoint.publish("http://localhost:7779/ws/Servidor", new ServidorImpl());
	}
}
