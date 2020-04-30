import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;

import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.*;


public class ServidorImplementacion extends UnicastRemoteObject implements Servicios {

    // Servicios del servidor horoscopo
    private static ServiciosHoroscopo horoscopo; 
    // Servicios del servidor pronostico
    private static ServiciosPronostico pronostico; 
    // Estructura de Datos utilizada como CACHE
    private static ConcurrentHashMap<String, String> cache; 
    // Patrones fecha y signo
    private Pattern patronSigno, patronFecha;

    protected ServidorImplementacion(String ipH, int pH, String ipP, int pP) throws RemoteException {
        super();
 
        try{
            cache = new ConcurrentHashMap<>();
            // Conexion al servidor pronostico
            pronostico =  (ServiciosPronostico) Naming.lookup("rmi://"+ipP+":"+pP+"/ServidorPronostico");
            // Conexion al servidor horoscopo
            horoscopo  =  (ServiciosHoroscopo) Naming.lookup("rmi://"+ipH+":"+pH+"/ServidorHoroscopo");
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
    public String consultar(String request) throws RemoteException {
        String respuesta = "Consulta recibida";
        String[] response = new String[2];
        String signo = "";
        String fecha = "";
        String fechaNormalizada = "";
        String signoNormalizada = "";

        System.out.println("Cliente> peticion [" + request + "]");

        try {
            
            Matcher escanearSigno = patronSigno.matcher(request);
            Matcher escanearFecha = patronFecha.matcher(request);

            if (escanearSigno.find()) {
                signo = escanearSigno.group();             
                // Si la consulta esta en cache, caso contrario realizo consulta
                signoNormalizada = signo.toLowerCase();
                if (cache.containsKey(signoNormalizada)) { 
                    response[0] = (String) cache.get(signoNormalizada);
                    System.out.println("Se accedio a cache para recuperar la petición de Horoscopo del cliente");
                } else {
                    response[0] = horoscopo.consultarHoroscopo(signo); // Invocacion remota a horoscopo
                }

            } else {
                response[0] = new String("No se detecto ningun signo o fue escrito incorrectamente.");
            }
            
            if (escanearFecha.find()) {
                fecha = escanearFecha.group();
                // Si la consulta esta en cache , caso contrario realizo consulta
                fechaNormalizada = fecha.replaceAll("[^0-9]","");
                if (cache.containsKey(fechaNormalizada)) {
                    response[1] = new String(cache.get(fechaNormalizada));
                    System.out.println("Se accedio a cache para recuperar la peticion de Pronostico del cliente");
                } else {
                    response[1] = pronostico.consultarPronostico(fecha); // Invocacion remota a pronostico
                }
            }
              else {
                response[1] = new String("No se detecto ningun signo o fue escrito incorrectamente.");
            }
    
            respuesta = " SIGNO: "+response[0] + "\n"+" HORÓSCOPO:" + response[1];
            System.out.println("Central> Resultado de peticion");
            System.out.println("Central> \"" + respuesta + "\"");
            
            // Administra cache, si la cache esta "llena" elimina un elemento
            if (cache.size() > 10) { 
                String basura = (String) cache.keys().nextElement();
                cache.remove(basura);
                System.out.println("Cache llena, elimino consulta : "+ basura);
            }

            if (response[0] == null) {
                cache.put(signoNormalizada, response[0]); 
            }

            if (response[1] == null) {
                cache.put(fechaNormalizada, response[1]); 
            }

            
        } catch (Exception e) {
            e.printStackTrace();
        }

        return respuesta;
    }
}


