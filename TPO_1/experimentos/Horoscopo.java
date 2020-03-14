import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 * Extrae de un sitio web el horóscopo del día.
 * Utiliza la librería JSOUP.
 *
 * Compilar/Ejecutar de la siguiente manera:
 * javac -cp "jsoup-1.13.1.jar" Horoscopo.java
 * java -cp "jsoup-1.13.1.jar" Horoscopo
 */
public class Horoscopo {

    public static void main(String[] args) {
        System.out.println(process("Leo"));
    }

    /**
     * procesa peticion del cliente y retorna resultado
     * @param request peticion del cliente
     * @return String
     */
    public static String process(String request) {
        Document doc;

        try {

            // obtener página web
            doc = Jsoup.connect("https://www.lecturas.com/horoscopo").get();

            // obtener horóscopo de hoy
            Elements divs = doc.select("div[class=horoscopo-hoy]");

            for (Element nodo : divs) {
                if (request.equalsIgnoreCase(nodo.getElementsByTag("h2").text())) {
                    return nodo.getElementsByAttributeValue("class","txt").text();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}
