package tano;

import javax.jws.WebService;

//Service Implementation
@WebService(endpointInterface = "tano.Calc")
public class CalcImpl implements Calc {

    // aca van los metodos
    //    protected CalculadoraImp() { super(); }

    @Override
    public int suma(int a, int b) {
        return a + b;
    }

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
}

