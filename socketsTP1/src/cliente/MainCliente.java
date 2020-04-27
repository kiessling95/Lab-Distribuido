package cliente;

import java.io.*;
import java.net.Socket;

public class MainCliente 

    public static void main(String[] args) {
        boolean exit = false;   // bandera para controlar ciclo del programa
        Socket socket;          // socket para la comunicacion cliente servidor

        // Por default 
        String servidor = "localhost";
        int PORT=10578;

        if (args.length != 0) {
            // cargo ip server parametro 
            servidor = args[0];
            // cargo puserto server parametro 
            PORT = Integer.parseInt(args[1]);
        }

        try {
            while( !exit ) {
                System.out.println("Cliente inicia nueva sesion");
                // direccion server y port por consola
                socket = new Socket(servidor, PORT); // abre socket

                DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                DataInputStream input = new DataInputStream(socket.getInputStream());

                // lee lo que escriba el usuario
                BufferedReader brRequest = new BufferedReader(new InputStreamReader(System.in));

                System.out.print("Cliente> ");
                // captura comando escrito por el usuario
                String request = brRequest.readLine();

                if (request.equalsIgnoreCase("exit")) {
                    exit = true;
                    System.out.println("Cliente finaliza sesion");
                }

                // manda peticion al servidor
                output.writeUTF(request);
                // captura respuesta e imprime
                String st = input.readUTF();
                if( st != null ) System.out.println("Servidor> " + st );

                output.close();
                socket.close();
            }
       } catch (IOException ex) {
            System.err.println(ex.getMessage());
       }
    }
}
