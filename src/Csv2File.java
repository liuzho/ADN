import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Csv2File {

    private static final String JSON_TEMPLATE = "{" +
            "\"device\":\"%1$s\"," +
            "\"model\": \"%2$s\"," +
            "\"name\": \"%3$s\"" +
            "}";

    public static void main(String[] args) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream("./supported_devices.csv"), StandardCharsets.UTF_16LE));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] split = line.split(",", 4);
                if (split.length != 4) {
                    throw new RuntimeException(line);
                }
                writeToFile(split[2], split[3], split[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(reader);
        }
    }


    private static void writeToFile(String device, String model, String deviceName) throws IOException {
        File folder = new File("./names/" + encode(device.toLowerCase().trim()));
        folder.mkdirs();
        File targetFile = new File(folder, encode(model.toLowerCase().trim()) + ".json");
        Writer writer = null;
        try {
            writer = new FileWriter(targetFile);
            String json = "{" +
                    "\"device\":\"" + encode(device.trim()) + "\"," +
                    "\"model\":\"" + encode(model.trim()) + "\"," +
                    "\"name\":\"" + encode(deviceName.trim()) + "\"" +
                    "}";
            writer.write(json);
        } finally {
            close(writer);
        }
    }

    private static String encode(String source) throws IOException {
        return URLEncoder.encode(source, "utf-8");
    }


    private static void close(AutoCloseable close) {
        if (close != null) {
            try {
                close.close();
            } catch (Throwable ignore) {
            }
        }
    }

}
