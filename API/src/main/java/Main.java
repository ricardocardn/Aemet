import controller.api.ApiController;
import controller.command.Command;
import controller.command.CommandSQLite;
import controller.databasecontroller.DataBaseConnector;
import controller.databasecontroller.DataBaseQuery;
import controller.databasecontroller.StandardQuery;

public class Main {
    public static void main(String[] args) {
        StandardQuery dataBaseQuery = new DataBaseQuery(new DataBaseConnector("temp.db"));
        Command command = new CommandSQLite(dataBaseQuery);
        ApiController apiController = new ApiController(command);
        apiController.run();
    }
}
