import java.rmi.*;
import java.util.Scanner;

public class Cliente {
    
    /**
     * @param args argumentos de la linea de comando
     * @param args[0] un signo del horoscopo
     * @param args[1] una fecha
     */
    public static void main(String[] args) {
        
        if (args.length < 2) {
            System.err.println("Uso: Cliente IPServidor PuertoServidor Horoscopo Fecha");
            return;
        }

        try {
            System.out.println("Buscando Servicios");
            // Se buscan los servicios provistos por el servidor - HARDCODE
            Servicios serv = (Servicios) Naming.lookup("rmi://"+args[0]+":"+args[1]+"/ServidorImplementacion");
            
            while (true) {
                System.out.println("Ingrese peticion: ");
                String entrada = new Scanner(System.in).nextLine();
                if (entrada.equals("exit")) break;
                
                System.out.println("Enviando...");
                // Invocacion remota
                System.out.println("Respuestas: " + serv.consultar(entrada));
            }
        } catch (Exception e) { 
            e.printStackTrace();
            System.out.println("Error con servidores");
            System.out.println("Asegurese que servidores estan funcionando");
        }
    }
}
