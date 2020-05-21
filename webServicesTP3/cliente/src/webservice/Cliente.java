package webservice;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.swing.*;
import java.awt.*;

public class Cliente {

    public static void main(String[] args) {

        JFrame f = new JFrame("Cliente");

        JTextField ipServidor = new JTextField("localhost");
        JSpinner puertoServidor = new JSpinner(new SpinnerNumberModel(9000, 6000, 9000, 1));
        JTextField signo = new JTextField("signo");
        JTextField fecha = new JTextField("fecha");
        JLabel res = new JLabel("Respuesta");
        JButton b = new JButton("Consultar");

        f.add(ipServidor);
        f.add(puertoServidor);
        f.add(signo);
        f.add(fecha);
        f.add(res);
        f.add(b);

        f.setLayout(new GridLayout(6,1));
        f.setSize(300,500);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        b.addActionListener(e -> {
            URL url = null;
            try {
                url = new URL("http://"+ipServidor.getText()+":"+puertoServidor.getValue()+"/ws/Servidor?wsdl");
            } catch (MalformedURLException malformedURLException) {
                malformedURLException.printStackTrace();
            }

            QName qname = new QName("http://webservice/", "ServiciosServidorImplService");

            Service service = Service.create(url, qname);
            ServiciosServidor serv  = service.getPort(ServiciosServidor.class);

            String respuesta = serv.consultar(signo.getText() + fecha.getText());

            SwingUtilities.invokeLater(() -> res.setText(respuesta));
        });
    }
}
