import com.google.gson.Gson;
import controller.Controller;
import controller.dataextractor.DataExtractor;
import controller.dataextractor.DataLakeDataExtractor;
import controller.databasecontroller.DataBaseConnection;
import controller.databasecontroller.DataBaseDDL;
import controller.databasecontroller.DataBaseQuery;
import model.TempEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static void main(String[] args) {
        DateTimeFormatter fr = DateTimeFormatter.ofPattern("yyyyMMdd");

        DataExtractor dataExtractor = new DataLakeDataExtractor();
        DataBaseConnection dataBaseConnection = new DataBaseConnection("temp.db");
        DataBaseDDL dataBaseDDL = new DataBaseDDL(dataBaseConnection);
        DataBaseQuery dataBaseQuery = new DataBaseQuery(dataBaseConnection);

        dataBaseDDL.createTables();

        Controller controller = new Controller(dataExtractor, dataBaseDDL, dataBaseQuery);

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                LocalDate date = LocalDate.now();
                controller.run(date);
            }
        };

        timer.schedule(timerTask, 0, 60*60*1000);
    }
}
