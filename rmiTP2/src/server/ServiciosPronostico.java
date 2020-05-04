package server;

import java.rmi.*;

/*
Interfaz implementada por ServidorPronostico
*/

public interface ServiciosPronostico extends Remote {
    // Metodo utilizado para consultar el pronostico
    public String consultarPronostico(String consulta) throws RemoteException; 
}
