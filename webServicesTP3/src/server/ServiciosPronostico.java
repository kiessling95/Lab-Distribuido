package server;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

/*
Interfaz implementada por ServidorPronostico
*/
@WebService
@SOAPBinding(style = Style.DOCUMENT)
public interface ServiciosPronostico {

    @WebMethod
    public String consultarPronostico(String consulta); 
}
