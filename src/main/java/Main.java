import org.json.simple.parser.ParseException;

import java.io.IOException;

public class Main {
    public static final String WEB_SITE = "https://onlinesim.ru/price-list-data?type=receive";
    public static void main(String[] args) {

        Parser.startParsing(WEB_SITE);
    }
}
