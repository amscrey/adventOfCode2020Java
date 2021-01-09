import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class PasswordValidator {

    public static void main(String[] args) {
        //BufferedReader reader;
        List<String> lines = LineReader.readLines("/Users/dc24863/dev/supplychain/Advent2/passwords.txt");
        int index = 0, valid1Count = 0, valid2Count = 0;
        Iterator<String> iter = lines.listIterator();
        while (iter.hasNext()) {
            index++;
            Line line = parseLine(iter.next());
            boolean valid1 = validateScheme1(line);
            boolean valid2 = validateScheme2(line);
            if (valid1) valid1Count++;
            if (valid2) valid2Count++;
            System.out.println("Index: " + index + " line: " + line +
                    " --- is Valid 1 / 2: " + valid1 + " / " + valid2 +
                    "; valid 1 / 2 count: " + valid1Count + " / " + valid2Count);
        }

    }

    static Line parseLine(String line) {
        StringTokenizer st = new StringTokenizer(line);
        String range = st.nextToken();
        String characterAndColon = st.nextToken();
        String psswd = st.nextToken();
        return new Line(range, characterAndColon.substring(0,1), psswd);
    }

    static boolean validateScheme1(Line line) {
        StringTokenizer st = new StringTokenizer(line.getPassword(), line.getCharacter());
        long count = line.getPassword().chars().filter(ch -> ch == line.getCharacter().charAt(0)).count();
        // System.out.println("char count " + count);
        FloorAndCeiling fAndC = line.getFloorAndCeiling();
        if ((fAndC.getFloor() <= count) && (fAndC.getCeiling() >= count)) return true;
        return false;
    }

    static boolean validateScheme2(Line line) {
        FloorAndCeiling fAndC = line.getFloorAndCeiling();
        int pos1Index = fAndC.getFloor() -1;
        int pos2Index = fAndC.getCeiling() -1;
        char position1Val = line.getPassword().charAt(pos1Index);
        char position2Val = line.getPassword().charAt(pos2Index);
        // System.out.println("position1Val: " + position1Val + ", position2Val: " + position2Val);
        if ((position1Val == line.getCharacter().charAt(0)) ^
                (position2Val == line.getCharacter().charAt(0))) return true;
        return false;
    }

    public static class Line {
        private String range;
        private String character;
        private String password;

        public Line(String range, String character, String password) {
            this.range = range;
            this.character = character;
            this.password = password;
        }

        public String getRange() {
            return range;
        }

        public FloorAndCeiling getFloorAndCeiling() {
            StringTokenizer st = new StringTokenizer(range, "-");
            int floor = Integer.parseInt(st.nextToken());
            int ceiling = Integer.parseInt(st.nextToken());
            return new FloorAndCeiling(floor, ceiling);
        }

        public String getCharacter() {
            return character;
        }

        public String getPassword() {
            return password;
        }

        @Override
        public String toString() {
            return "Line{" +
                    "range='" + range + '\'' +
                    ", character='" + character + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }
    }

    public static class FloorAndCeiling {
        int floor;
        int ceiling;

        public FloorAndCeiling(int floor, int ceiling) {
            this.floor = floor;
            this.ceiling = ceiling;
        }

        public int getFloor() {
            return floor;
        }

        public int getCeiling() {
            return ceiling;
        }
    }


    public static class LineReader {
        public static List<String> readLines(String fileName) {
            BufferedReader reader;
            List<String> lines = new ArrayList<>();
            try {
                reader = new BufferedReader(new FileReader(
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
    }
}
