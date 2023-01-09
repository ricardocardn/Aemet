import com.google.gson.Gson;
import databasecontroller.DataBaseConnection;
import databasecontroller.DataBaseQuery;
import model.TempEvent;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import static spark.Spark.*;

public class ApiController {
    private static DateTimeFormatter ft = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static DataBaseQuery dataBaseQuery = new DataBaseQuery(new DataBaseConnection("temp.db"));

    public static void main(String args[]) {
        port(8086);
        get("v1/min", (req, res) -> {

            try {
                TempEvent result = dataBaseQuery.getTempEvent("MinTemp", LocalDate.now().format(ft));
                return new Gson().toJson(result);
            } catch (Exception e) {
                return e.getMessage();
            }
        });
    }
}
