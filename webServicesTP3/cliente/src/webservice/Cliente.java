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
        String[] signos = {"Aries", "Tauro", "Geminis", "Cancer", "Leo", "Virgo", "Libra", "Escorpio",
                "Sagitario", "Capricornio", "Acuario", "Piscis"};

        JTextField ipServidor = new JTextField("localhost");
        JSpinner puertoServidor = new JSpinner(new SpinnerNumberModel(9000, 6000, 9000, 1));
        JComboBox<String> signo = new JComboBox<>(signos);
        JTextField fecha = new JTextField("fecha");
        //JLabel res = new JLabel("Respuesta");
        //JLabel resFecha = new JLabel("Respuesta Fecha");
        JButton b = new JButton("Consultar");
        JTextArea resSigno = new JTextArea(2, 20);
        JTextArea resFecha = new JTextArea(2, 20);

        resSigno.setText("Respuesta Signo...");
        resFecha.setText("Respuesta Fecha...");
        
        resSigno.setBorder(BorderFactory.createLineBorder(Color.RED));
        resFecha.setBorder(BorderFactory.createLineBorder(Color.GREEN));
        

        f.add(ipServidor);
        f.add(puertoServidor);
        f.add(signo);
        f.add(fecha);
        //f.add(resFecha,BorderLayout.CENTER);
        

        f.setLayout(new GridLayout(6,1));
        f.setSize(300,500);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(resSigno, BorderLayout.CENTER);
        f.getContentPane().add(resFecha, BorderLayout.CENTER);
        f.setSize(100,200);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        
        f.add(b);

        b.addActionListener(e -> {
            resSigno.setText("Procesando...");
            resFecha.setText("Procesando...");
            URL url = null;
            try {
                url = new URL("http://"+ipServidor.getText()+":"+puertoServidor.getValue()+"/ws/Servidor?wsdl");
            } catch (MalformedURLException malformedURLException) {
                malformedURLException.printStackTrace();
            }

            QName qname = new QName("http://webservice/", "ServiciosServidorImplService");

            Service service = Service.create(url, qname);
            ServiciosServidor serv  = service.getPort(ServiciosServidor.class);

            String[] respuesta = serv.consultar(signo.getSelectedItem() + fecha.getText());

            resSigno.setWrapStyleWord(true);
            resSigno.setLineWrap(true);
            resSigno.setOpaque(false);
            resSigno.setEditable(false);
            resSigno.setFocusable(false);
            resSigno.setBackground(UIManager.getColor("Label.background"));
            resSigno.setFont(UIManager.getFont("Label.font"));
            resSigno.setBorder(UIManager.getBorder("Label.border"));
            
            resFecha.setWrapStyleWord(true);
            resFecha.setLineWrap(true);
            resFecha.setOpaque(false);
            resFecha.setEditable(false);
            resFecha.setFocusable(false);
            resFecha.setBackground(UIManager.getColor("Label.background"));
            resFecha.setFont(UIManager.getFont("Label.font"));
            resFecha.setBorder(UIManager.getBorder("Label.border"));
            
           
            
            SwingUtilities.invokeLater(() -> resSigno.setText("HOROSCOPO :\n"+respuesta[0]));
            
            SwingUtilities.invokeLater(() -> resFecha.setText("PRONOSTICO :\n"+respuesta[1]));
            
            resSigno.setBorder(BorderFactory.createLineBorder(Color.RED));
            resFecha.setBorder(BorderFactory.createLineBorder(Color.GREEN));
            
        });
    }
}
