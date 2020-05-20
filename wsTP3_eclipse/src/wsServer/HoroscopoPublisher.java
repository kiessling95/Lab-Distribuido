package wsServer;

import javax.xml.ws.Endpoint;

public class HoroscopoPublisher {

	public static void main(String[] args) {
		Endpoint.publish("http://localhost:8000/ws/Horoscopo", new HoroscopoImpl());
	}
}
