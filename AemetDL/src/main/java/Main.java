import controller.Controller;
import datalake.*;

import java.io.IOException;

public class Main {
    public static String URL = "https://opendata.aemet.es/opendata/api/observacion/convencional/todas";

    public static void main(String[] args) throws IOException {
        DataLake dataLake = new DataLakeFile();
        Controller controller = new Controller();
        controller.run(dataLake, args[0]);
    }
}
