package webservice;

import javax.xml.ws.Endpoint;
import javax.swing.*;
import java.awt.*;

public class ServidorPublisher {

    public static void main(String[] args) {
        JFrame f = new JFrame("Servidor Central");

        JTextField localIP = new JTextField("localhost");
        JSpinner localPort = new JSpinner(new SpinnerNumberModel(9000, 6000, 9000, 1));

        JTextField ipH = new JTextField("localhost");
        JSpinner pH    = new JSpinner(new SpinnerNumberModel(7000, 6000, 9000, 1));

        JTextField ipP = new JTextField("localhost");
        JSpinner pP    = new JSpinner(new SpinnerNumberModel(8000, 6000, 9000, 1));

        JButton b = new JButton("Publish EndPoint");

        f.add(localIP);
        f.add(localPort);
        f.add(ipH);
        f.add(pH);
        f.add(ipP);
        f.add(pP);
        f.add(b);

        f.setLayout(new GridLayout(7,1));
        f.setSize(300,500);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        b.addActionListener(e -> {
            String ipLocal       = localIP.getText();
            int puertoLocal      = (Integer) localPort.getValue();
            String ipHoroscopo   = ipH.getText();
            int puertoHoroscopo  = (Integer) pH.getValue();
            String ipPronostico  = ipP.getText();
            int puertoPronostico = (Integer) pP.getValue();

            Endpoint.publish("http://" + ipLocal + ":"
                    + puertoLocal + "/ws/Servidor", new ServiciosServidorImpl(ipHoroscopo, puertoHoroscopo, ipPronostico, puertoPronostico));
            b.setEnabled(false);
        });
    }
}
