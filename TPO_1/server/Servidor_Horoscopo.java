import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.*;
public class Servidor_Horoscopo extends Thread {
    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
    private int idSessio;
    public Servidor_Horoscopo(Socket socket, int id) {
        this.socket = socket;
        this.idSessio = id;
        try {
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(Servidor_Horoscopo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void desconnectar() {
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(Servidor_Horoscopo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    public void run() {
        String request = "";
        try {
            // Aca deberiamos leer la request del horoscopo
            request = dis.readUTF();
    
                //se procesa la peticion y se espera resultado
                String strOutput = process(request);  
                System.out.println(strOutput+"Horoscopo solicitado por el cliente con idSesion "+this.idSessio+" saluda");
                dos.writeUTF("adios");
            
        } catch (IOException ex) {
            Logger.getLogger(Servidor_Horoscopo.class.getName()).log(Level.SEVERE, null, ex);
        }
        desconnectar();
    }

    /**
     * procesa peticion del cliente y retorna resultado
     * @param request peticion del cliente
     * @return String
     */
    public static String process(String request){

        String result="";        

        String[] Horoscopo = {""};
        ArrayList<String> horoscopoList = new ArrayList<String>();
        Collections.addAll(horoscopoList, Horoscopo);            

        switch(request){
            case "Aries":
                break;
            case "Tauro":
                break;
            case "Geminis":
                break;
            case "Cancer":
                break;
            case "Leo":
                break;
            case "Virgo":
                break;
            case "Libra":
                break;
            case "Escorpio":
                break;
            case "Sagitario":
                break;
            case "Capricornio":
                break;
            case "Acuario":
                break;
            case "Piscis":
                break;
            case "Exit":                
                result = "bye";
                break;
            default:
                result = "La peticion no se puede resolver.";
                break;
        }
        return result;
    }
}
