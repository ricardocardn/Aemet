import controller.Controller;
import controller.dataextractor.DataExtractor;
import controller.dataextractor.DataLakeDataExtractor;
import controller.databasecontroller.DataBaseConnector;
import controller.databasecontroller.DataBaseDDL;
import controller.databasecontroller.DataBaseQuery;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        DataExtractor dataExtractor = new DataLakeDataExtractor();
        DataBaseConnector dataBaseConnection = new DataBaseConnector("temp.db");
        DataBaseDDL dataBaseDDL = new DataBaseDDL(dataBaseConnection);
        DataBaseQuery dataBaseQuery = new DataBaseQuery(dataBaseConnection);

        Controller controller = new Controller(dataExtractor, dataBaseDDL, dataBaseQuery);
        controller.run(LocalDate.now());
    }
}
