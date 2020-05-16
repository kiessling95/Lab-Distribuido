package tano;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

public class CalcClient {
    public static void main(String[] args) throws Exception {
        URL url = new URL("http://localhost:7779/ws/Calculadora?wsdl");

        //1st argument service URI, refer to wsdl document above
        //2nd argument is service name, refer to wsdl document above
        QName qname = new QName("http://tano/", "CalcImplService");


        Service service = Service.create(url, qname);
        Calc calc = service.getPort(Calc.class);

        System.out.print("2 + 3 = ");
        System.out.println(calc.suma(2, 3));
        System.out.print("7 - 3 = ");
        System.out.println(calc.resta(7, 3));
        System.out.print("9 / 3 = ");
        System.out.println(calc.div(9, 3));
        System.out.print("2 * 3 = ");
        System.out.println(calc.mul(2, 3));

    }

}


