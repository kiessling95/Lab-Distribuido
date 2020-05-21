package webservice;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

/*
Interfaz implementada por ServidorHoroscopo
*/
@WebService
@SOAPBinding(style = Style.DOCUMENT)
public interface ServiciosHoroscopo {

    @WebMethod String consultarHoroscopo(String consulta); 
}
