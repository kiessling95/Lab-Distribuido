package wsServer;

import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.*;

//Service Implementation
@WebService(endpointInterface = "wsServer.Servidor")
public class ServidorImpl implements Servidor{

	
	// Servicios del servidor horoscopo
    private static Horoscopo horoscopo;
    // Servicios del servidor pronostico
    private static Pronostico pronostico; 
    // Estructura de Datos utilizada como CACHE
    private static ConcurrentHashMap<String, String> cache; 
    // Patrones fecha y signo
    private Pattern patronSigno, patronFecha;
    
	protected ServidorImpl() {
        super();
 
        try{
            cache = new ConcurrentHashMap<>();
            
            // URLs
            URL urlPronostico= new URL("http://localhost:7000/ws/Pronostico?wsdl");
            URL urlHoroscopoco= new URL("http://localhost:8000/ws/Horoscopo?wsdl");
            
            // Qnames
            QName qnamePronostico = new QName("http://wsServer/", "PronosticoImplService");
            QName qnameHoroscopo = new QName("http://wsServer/", "HoroscopoImplService");
            
            
            // Services
            Service servPronostico = Service.create(urlPronostico, qnamePronostico);
            Service servHoroscopo  = Service.create(urlHoroscopoco, qnameHoroscopo);
            
            // Conexion al servidor pronostico
            pronostico =  servPronostico.getPort(Pronostico.class);
            // Conexion al servidor horoscopo
            horoscopo  =  servHoroscopo.getPort(Horoscopo.class);
            // Patrones 
            this.patronFecha = Pattern.compile("\\b(0?[1-9]|[12][0-9]|3[01])[- /.](0?[1-9]|1[012])[- /.](\\d{2,4})\\b");
            this.patronSigno = Pattern.compile("aries|tauro|geminis|cancer|leo|virgo|libra|escorpio|sagitario|capricornio|acuario|piscis",
                Pattern.CASE_INSENSITIVE);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
	
	
	
	/**
     * @param request consulta un signo y fecha 
     * @return una prediccion del horoscopo y del tiempo en la fecha dada
     */
    @Override
    public String consultar(String request) {
        String respuesta = "Consulta recibida";
        String[] response = new String[2];
        String signo = "";
        String fecha = "";
        String fechaNormalizada = "";
        String signoNormalizada = "";

        System.out.println("Servidor Central> Nueva peticion entrante: [" + request + "]");

        try {
            
            Matcher escanearSigno = patronSigno.matcher(request);
            Matcher escanearFecha = patronFecha.matcher(request);

            if (escanearSigno.find()) {
                signo = escanearSigno.group();             
                // Si la consulta esta en cache, caso contrario realizo consulta
                signoNormalizada = signo.toLowerCase();
                if (cache.containsKey(signoNormalizada)) { 
                    response[0] = (String) cache.get(signoNormalizada);
                    System.out.println("Servidor Central> Se accedio a cache para recuperar la petición de Horoscopo de [" + signoNormalizada + "]");
                } else {
                    response[0] = horoscopo.consultarHoroscopo(signo); // Invocacion remota a horoscopo
                }

            } else {
                response[0] = new String("Servidor Central> No se detecto ningun signo o fue escrito incorrectamente.");
            }

            if (escanearFecha.find()) {
                fecha = escanearFecha.group();
                // Si la consulta esta en cache , caso contrario realizo consulta
                fechaNormalizada = fecha.replaceAll("[^0-9]","");
                if (cache.containsKey(fechaNormalizada)) {
                    response[1] = new String(cache.get(fechaNormalizada));
                    System.out.println("Servidor Central> Se accedio a cache para recuperar la peticion de Pronostico de [" + fecha + "]");
                } else {
                    response[1] = pronostico.consultarPronostico(fecha); // Invocacion remota a pronostico
                }
            } else {
                response[1] = new String("Servidor Central> No se detecto ninguna fecha o fue escrita incorrectamente.");
            }

            respuesta = "HOROSCOPO: " + response[0] + "\n" + "PRONOSTICO: " + response[1];
            System.out.println("Servidor Central> Retornando respuesta para solicitud: [" + signo + "] fecha: [" + fecha + "]");
            
            // Administra cache, si la cache esta "llena" elimina un elemento
            if (cache.size() > 10) { 
                String basura = (String) cache.keys().nextElement();
                cache.remove(basura);
                System.out.println("Servidor Central> Cache llena, elimino consulta : " + basura);
            }

            if (response[0] != null) {
                cache.put(signoNormalizada, response[0]); 
            }

            if (response[1] != null) {
                cache.put(fechaNormalizada, response[1]); 
            }

            
        } catch (Exception e) {
            e.printStackTrace();
        }

        return respuesta;
    }
}
