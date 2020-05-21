package webservice;

import javax.xml.ws.Endpoint;
import javax.swing.*;
import java.awt.*;

public class PronosticoPublisher {

    public static void main(String[] args) {
        JFrame f = new JFrame("Servidor Pronostico");

        JTextField localIP = new JTextField("localhost");
        JSpinner localPort = new JSpinner(new SpinnerNumberModel(8000, 6000, 9000, 1));
        JButton b = new JButton("Publish EndPoint");

        f.add(localIP);
        f.add(localPort);
        f.add(b);

        f.setLayout(new GridLayout(3,1));
        f.setSize(300,200);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        b.addActionListener(e -> {
            Endpoint.publish("http://" + localIP.getText() + ":"
                    + localPort.getValue() + "/ws/Pronostico", new PronosticoImpl());
            b.setEnabled(false);
        });
    }
}
