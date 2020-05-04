package server;

import java.rmi.*;

/*
Interfaz implementada por ServidorHoroscopo
*/

public interface ServiciosHoroscopo extends Remote {
    // Metodo utilizado para consultar el horoscopo
    public String consultarHoroscopo(String consulta) throws RemoteException; 
}
