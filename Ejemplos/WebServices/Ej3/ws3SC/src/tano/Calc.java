package tano;

import java.net.MalformedURLException;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

//Service Endpoint Interface
@WebService
@SOAPBinding(style = Style.DOCUMENT)
public interface Calc {

// van las definiciones de los metodos

    @WebMethod
    int suma(int a, int b) throws MalformedURLException;

    @WebMethod
    int resta(int a, int b);

    @WebMethod
    int div(int a, int b);

    @WebMethod
    int mul(int a, int b);

} 


