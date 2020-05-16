package tano;

import javax.jws.WebService;

//Service Implementation
@WebService(endpointInterface = "tano.Suma")
public class SumaImpl implements Suma {

// aca van los metodos
//    protected CalculadoraImp() { super(); }

    @Override
    public int sumare(int a, int b) {
        return a + b;
    }
}
