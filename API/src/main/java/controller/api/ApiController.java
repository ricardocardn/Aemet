package controller.api;

import com.google.gson.Gson;
import controller.command.Command;
import controller.databasecontroller.DataBaseConnection;
import controller.databasecontroller.DataBaseQuery;
import model.TempEvent;
import spark.Request;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static spark.Spark.*;

public class ApiController {
    private final DateTimeFormatter ft = DateTimeFormatter.ofPattern("yyyyMMdd");
    private final Command command;

    public ApiController(Command command) {
        this.command = command;
    }

    public void run() {
        port(8086);
        getMinReq();
        getMaxReq();
    }

    private void getMinReq() {
        get("v1/places/with-min-temperature", (req, res) -> {

            try {
                LocalDate from = getFrom(req, "from");
                LocalDate to = getFrom(req, "to");
                return new Gson().toJson(command.minTemperatures(from, to));
            } catch (Exception e) {
                return "No data available for specified dates";
            }
        });
    }

    private void getMaxReq() {
        get("v1/places/with-max-temperature", (req, res) -> {

            try {
                LocalDate from = getFrom(req, "from");
                LocalDate to = getFrom(req, "to");
                return new Gson().toJson(command.maxTemperatures(from, to));
            } catch (Exception e) {
                return "No data available for specified dates";
            }
        });
    }

    private LocalDate getFrom(Request req, String param) {
        if (req.queryParams(param) != null)
            return LocalDate.parse(req.queryParams(param), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        else
            return null;
    }
}
