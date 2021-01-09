package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


public class AdventFileHelper {
    public static List<String> readLines(String fileName) {
        BufferedReader reader;
        List<String> lines = new ArrayList<>();
        try {
            reader = new BufferedReader(new java.io.FileReader(
                    fileName));
            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);
                lines.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public static String readAll(String filePath)
    {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8))
        {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }

    public static List<RawDataSegment> parseRawDataSegments(String rawdata) {
        String[] segArray = rawdata.split("\n\n");
        List<RawDataSegment> segments = new ArrayList<>();
        for (String segment : segArray) segments.add(new RawDataSegment(segment));
        return segments;
    }

    public static class RawDataSegment {
        String rawData;
        public String getRawData() {return rawData;}

        public RawDataSegment(String rawPassportData) {
            this.rawData = rawPassportData;
        }
    }
}
