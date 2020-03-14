import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Captura el signo y la fecha ingresados por el usuario utilizando expresiones regulares.
 * No importa la ubicación del signo, ni los símbolos para separar las fechas.
 */
public class Regex {
    public static final String TEXTO_ENTRADA= "Dame el horóscopo de cancer del 12-12-2020";

    public static void main(String[] args) {
        Pattern patronFecha = Pattern.compile("(0?[1-9]|[12][0-9]|3[01])[- /.](0?[1-9]|1[012])[- /.](19|20\\d\\d)");
        Pattern patronSigno = Pattern.compile("[aA]ries|[tT]auro|[gG].minis|[cC].ncer|[lL]eo|[vV]irgo]|[lL]ibra|[eE]scorpio|[sS]agitario|[cC]apricornio|[aA]cuario|[pP]iscis");

        Matcher matcher = patronFecha.matcher(TEXTO_ENTRADA);
        Matcher matcher2 = patronSigno.matcher(TEXTO_ENTRADA);

        if (matcher.find()) {
            System.out.println(matcher.group());
        }

        if (matcher2.find()) {
            System.out.println(matcher2.group());
        }
    }
}