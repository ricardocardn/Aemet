import controller.api.ApiController;
import controller.command.Command;
import controller.command.CommandSQLite;
import controller.databasecontroller.DataBaseConnection;
import controller.databasecontroller.DataBaseQuery;

public class Main {
    public static void main(String[] args) {
        DataBaseQuery dataBaseQuery = new DataBaseQuery(new DataBaseConnection("temp.db"));
        Command command = new CommandSQLite(dataBaseQuery);
        ApiController apiController = new ApiController(command);
        apiController.run();
    }
}
