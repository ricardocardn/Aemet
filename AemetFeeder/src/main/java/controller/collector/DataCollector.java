package controller.collector;

import java.io.IOException;
import java.util.List;

public interface DataCollector {
    List<Object> getCurrentObjects(String apiKey) throws IOException;
}
