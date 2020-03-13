import java.io.*;
import java.net.*;
import java.util.logging.*;
public class Servidor_Central {
    public static void main(final String args[]) throws IOException {
        ServerSocket ss;
        System.out.print("Inicializando servidor... ");
        try {
            ss = new ServerSocket(10578);
            System.out.println("\t[OK]");
            int idSession = 0;
            while (true) {
                Socket socket;
                socket = ss.accept();
                System.out.println("Nueva conexión entrante: " + socket);
                ((Servidor_Pronostico) new Servidor_Pronostico(socket, idSession)).start();
                idSession++;
            }
        } catch (final IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}