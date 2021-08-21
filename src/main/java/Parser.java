import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class Parser {
    private static final String FILE_FIRST_PATH = "src/main/resources/priceFirst.json";
    private static final String FILE_SECOND_PATH = "src/main/resources/priceSecond.json";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static Map<String, Map<String, String>> countryPrice = new HashMap<>();

    //Запуск парсинга
    public static void startParsing(String urlPage) {
        try {
            System.out.println("Parsing......");
            parsePage(urlPage);
            urlParser();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Parsing done!");

    }

    //c GET запроса достаем json с прайсом по странам, пишем в файл
    private static void parsePage(String urlPage) throws IOException {
        PrintWriter writer = new PrintWriter(FILE_FIRST_PATH);
        URL url = new URL(urlPage);
        InputStream inputStream = url.openStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            result.append(line);
        }
        writer.write(result.toString());
        writer.flush();
        writer.close();
    }

    //Парсим json, достаем нужную информацию, сохраняем в Map<Название страны, Map<Название сервиса, цена>>
    private static void urlParser() throws IOException, ParseException {

        FileReader reader = new FileReader(FILE_FIRST_PATH);

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
        Map<String, String> text = (Map<String, String>) jsonObject.get("text");
        Map<String, String> textRus = new HashMap<>();
        for (String key : text.keySet()) {
            String newKey = key.replaceAll("[A-Za-z_]", "");
            textRus.put(newKey, text.get(key));
        }
        Map<String, Map<String, String>> list = (Map<String, Map<String, String>>) jsonObject.get("list");
        for (String key : textRus.keySet()) {
            countryPrice.put(textRus.get(key), list.get(key));
            jsonCreate();
        }
    }

    // Создаем новый json c прайсом по странам
    private static void jsonCreate() throws IOException {
        try (FileWriter file = new FileWriter(FILE_SECOND_PATH)) {
            file.write(GSON.toJson(countryPrice));
        }


    }

}
