import java.io.FileWriter;
import java.io.IOException;

import com.opencsv.CSVWriter;

public class CSVService {

    public static void writeToCsv(String text) {
        String csv = "data.csv";
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(csv, true));
            String[] record = (text + ";").split(";");
            writer.writeNext(record);

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
