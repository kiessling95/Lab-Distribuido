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
            System.err.println("Uso: Cliente IPServidor PuertoServidor");
            return;
        }

        try {
            System.out.print("Buscando servicios...");
            // Se buscan los servicios provistos por el servidor
            Servicios serv = (Servicios) Naming.lookup("rmi://"+args[0]+":"+args[1]+"/ServidorImplementacion");
            System.out.println("\t\t[OK]");

            while (true) {
                System.out.println("Ingrese peticion o escriba exit para salir: ");
                String entrada = new Scanner(System.in).nextLine();
                if (entrada.equals("exit")) break;
                
                System.out.println("Enviando...");
                // Invocacion remota
                System.out.println("Respuestas: \n" + serv.consultar(entrada));
            }
        } catch (ConnectException e) {
            System.err.println("\t\t[Error]");
            System.err.println("Asegurese que los servidores esten funcionando y/o que los parametros esten bien escritos.");
        } catch (NotBoundException e) {
            System.err.println("\t\t[Error]");
            System.err.println("El servicio especificado no esta asociado a ningun registro.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
