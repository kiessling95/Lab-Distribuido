import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Scanner;

/**
 * Captura el signo y la fecha ingresados por el usuario utilizando expresiones regulares.
 * No importa la ubicación del signo, ni los símbolos para separar las fechas.
 */
public class Regex {

    public static void main(String[] args) {
        Scanner myObj = new Scanner(System.in);
        String TEXTO_ENTRADA = myObj.nextLine();;


        Pattern patronFecha = Pattern.compile("(0?[1-9]|[12][0-9]|3[01])[- /.](0?[1-9]|1[012])[- /.](19|20\\d\\d)");
        Pattern patronSigno = Pattern.compile("[aA]ries|[tT]auro|[gG].minis|[cC]\\p{L}ncer|[lL]eo|[vV]irgo]|[lL]ibra|[eE]scorpio|[sS]agitario|[cC]apricornio|[aA]cuario|[pP]iscis");

        Matcher matcher = patronFecha.matcher(TEXTO_ENTRADA);
        Matcher matcher2 = patronSigno.matcher(TEXTO_ENTRADA);

        String nuevo = Normalizer.normalize(TEXTO_ENTRADA, Form.NFD)
                .replaceAll("^\\p{ASCII}]", "");

        System.out.println(nuevo);

        if (matcher.find()) {
            System.out.println(matcher.group());
        }

        if (matcher2.find()) {
            System.out.println(matcher2.group());
        }
    }
}