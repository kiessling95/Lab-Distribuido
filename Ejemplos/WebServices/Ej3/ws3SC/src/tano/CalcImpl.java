package tano;

import javax.jws.WebService;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;

//Service Implementation
@WebService(endpointInterface = "tano.Calc")
public class CalcImpl implements Calc {

// aca van los metodos
//    protected CalculadoraImp() { super(); }


    @Override
    public int resta(int a, int b) {
        return a - b;
    }

    @Override
    public int div(int a, int b) {
        return a / b;
    }

    @Override
    public int mul(int a, int b) {
        return a * b;
    }

    @Override
    public int suma(int a, int b) throws MalformedURLException {
        int res = 0;
        URL url = new URL("http://localhost:7780/ws/Sumador?wsdl");
        QName qname = new QName("http://tano/", "SumaImplService");
        Service service = Service.create(url, qname);
        Suma sum = service.getPort(Suma.class);
        res = sum.sumare(a, b);
        return res;
    }
}
