package server;

import java.rmi.*;

/*
Interfaz implementada por ServidorImplementacion
*/

public interface Servicios extends Remote {
    // Metodo utilizado para consultar un signo y pronostico
    public String consultar(String consulta) throws RemoteException; 
}
