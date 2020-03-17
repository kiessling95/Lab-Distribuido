package cliente;

import java.io.*;
import java.net.Socket;

public class MainCliente {

    private final static int PORT = 10578;
    private final static String SERVER = "localhost";

    public static void main(String[] args) {
        boolean exit = false;   // bandera para controlar ciclo del programa
        Socket socket;          // socket para la comunicacion cliente servidor

        try {
            System.out.println("Cliente> Inicio");
            while( !exit ) {
                socket = new Socket(SERVER, PORT);  // abre socket

                DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                DataInputStream input = new DataInputStream(socket.getInputStream());

                // lee lo que escriba el usuario
                BufferedReader brRequest = new BufferedReader(new InputStreamReader(System.in));

                System.out.print("Cliente> ");
                // captura comando escrito por el usuario
                String request = brRequest.readLine();

                if (request.equals("exit")) {
                    exit = true;
                    System.out.println("Cliente> Fin de programa");
                } else {
                    // manda peticion al servidor
                    output.writeUTF(request);
                    // captura respuesta e imprime
                    String st = input.readUTF();
                    if( st != null ) System.out.println("Servidor> " + st );
                }

                output.close();
                socket.close();
            }
       } catch (IOException ex) {
            System.err.println("Cliente> " + ex.getMessage());
       }
    }
}
