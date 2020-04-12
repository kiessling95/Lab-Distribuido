import java.rmi.*;

public class Cliente {
    
    /**
     * @param args argumentos de la linea de comando
     * @param args[0] un signo del horoscopo
     * @param args[1] una fecha
     */

    public static void main(String[] args) {

        try {
            System.out.println("Buscando Servicios");
            // Se buscan los servicios provsitos por el servidor - HARDCODE
            Servicios serv = (Servicios) Naming.lookup("rmi://localhost:54321/ServidorImplementacion"); 
            System.out.println("Enviado...");
            // Invocacion remota
            System.out.println("Respuestas: " + serv.consultar(args[0] + " " + args[1])); 
          
        } catch (Exception e) { 
            e.printStackTrace();
            System.out.println("Error con servidores");
            System.out.println("Asegurese que servidores estan funcionando");
        }

    }
}
