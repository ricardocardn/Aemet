import controller.Controller;
import controller.datalake.DataLake;
import controller.datalake.DataLakeFile;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class Main {
    public static String URL = "https://opendata.aemet.es/opendata/api/observacion/convencional/todas";

    public static void main(String @NotNull [] args) {
        DataLake dataLake = new DataLakeFile();
        Controller controller = new Controller();
        controller.run(dataLake, args[0]);
    }
}
