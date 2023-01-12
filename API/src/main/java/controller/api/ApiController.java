package controller.api;

import com.google.gson.Gson;
import controller.command.Command;
import controller.databasecontroller.DataBaseConnection;
import controller.databasecontroller.DataBaseQuery;
import model.TempEvent;
import spark.Request;
import web.HTMLMaker;

import java.io.File;
import java.io.FileInputStream;
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
        getHomeReq();
        getMinReq();
        getMaxReq();
        getMaxHtmlReq();
        getMinHtmlReq();
        notAPageErrorHandler();
    }

    private void getHomeReq() {
        get("v1", (req, res) -> {
            FileInputStream fileInputStream = new FileInputStream("web/home.html");
            return fileInputStream.readAllBytes();
        });
    }

    private void getMinReq() {
        get("/v1/places/with-min-temperature", (req, res) -> {

            try {
                LocalDate from = getFrom(req, "from");
                LocalDate to = getFrom(req, "to");
                return new Gson().toJson(command.minTemperatures(from, to));
            } catch (Exception e) {
                return "{\"error\":\"invalid-dates\"}";
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
                return "{\"error\":\"invalid-dates\"}";
            }
        });
    }

    private void getMinHtmlReq() {
        get("v1/html/places/with-min-temperature", (req, res) -> {

            try {
                LocalDate from = getFrom(req, "from");
                LocalDate to = getFrom(req, "to");
                return HTMLMaker.getInstance().feedHTML(new Gson().toJson(command.maxTemperatures(from, to)), "web/index.html");
            } catch (Exception e) {
                FileInputStream fileInputStream = new FileInputStream("web/error404.html");
                return fileInputStream.readAllBytes();
            }
        });
    }

    private void getMaxHtmlReq() {
        get("v1/html/places/with-max-temperature", (req, res) -> {

            try {
                LocalDate from = getFrom(req, "from");
                LocalDate to = getFrom(req, "to");
                return HTMLMaker.getInstance().feedHTML(new Gson().toJson(command.maxTemperatures(from, to)), "web/index.html");
            } catch (Exception e) {
                FileInputStream fileInputStream = new FileInputStream("web/error404.html");
                return fileInputStream.readAllBytes();
            }
        });
    }

    private void notAPageErrorHandler() {
        get("//*", (req, res) -> {
            FileInputStream fileInputStream = new FileInputStream("web/error404notapage.html");
            return fileInputStream.readAllBytes();
        });
    }

    private LocalDate getFrom(Request req, String param) {
        if (req.queryParams(param) != null)
            return LocalDate.parse(req.queryParams(param), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        else
            return null;
    }


}
