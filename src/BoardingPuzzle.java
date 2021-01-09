import util.AdventFileHelper;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BoardingPuzzle {

    static List<Boolean> test1 = Stream.of(Boolean.TRUE,Boolean.FALSE, Boolean.TRUE)
            .collect(Collectors.toList());

    public static void main(String[] args) {
        BufferedReader reader;
        List<String> lines = AdventFileHelper.readLines("/Users/dc24863/dev/supplychain/Advent2/boardingPasses.txt");
        long t = sumBinaryEncoding(test1);
        System.out.println("t: " + t);
        List<BoardingPass> passes = hydratePassesAndRecordCritData(lines);
        buildSeatStatusList(passes);

    }

    static List<BoardingPass> hydratePassesAndRecordCritData(List<String> lines) {
        List<BoardingPass> passes = new ArrayList<>();
        long highestSeatNumberSoFar = 0;
        for (String line : lines) {
            BoardingPass pass = new BoardingPass(line);
            passes.add(pass);
            if (highestSeatNumberSoFar < pass.calculateSeatNumber())
                highestSeatNumberSoFar = pass.calculateSeatNumber();
            System.out.println("pass: " + pass + ", highest seat so far: " + highestSeatNumberSoFar);
        }
        return passes;
    }

    static long sumBinaryEncoding(List<Boolean> binNumberAsList) {
        long total = 0;
        for (int i=0;i<binNumberAsList.size();i++) {
            long valueAccordingToPlace = 0;
            if (binNumberAsList.get(i)) valueAccordingToPlace = (long) Math.pow(2, (double) i);
            total = total + valueAccordingToPlace;
        }
        return total;
    }

    // indexes as in String.substring
    static List<Boolean> getBinEncodingFromRaw(int begIndex, int endIndex, String rawEncoding, char valuedMark) {
        List<Boolean> binEncoding = new ArrayList<>();
        for (int i=endIndex-1;i>begIndex-1; i--) {
            char thisMark = rawEncoding.charAt(i);
            Boolean positionValued = new Boolean(thisMark == valuedMark);
            binEncoding.add(positionValued);
        }
        return binEncoding;
    }

    static void buildSeatStatusList(List<BoardingPass> passes) {
        // build empty list ONLY for those seat numbers that exist in terms of our calculation
        Map<Long,SeatStatus> seatStatusMap = new HashMap<>();
        for (long r=0;r<=127;r++) {
            for (long c=0;c<=7;c++)
                seatStatusMap.put(
                        BoardingPass.calculateSeatNumber(r,c),
                        new SeatStatus(BoardingPass.calculateSeatNumber(r,c), false, null));
        }
        // populate passes present
        for (BoardingPass pass : passes) {
            seatStatusMap.put(pass.calculateSeatNumber(), new SeatStatus(pass.calculateSeatNumber(), true, pass));
        }
        long possibleMatchingSeat = 0;
        long previousToLastFoundEmpty = -2;
        long lastFoundEmpty = -1;
        for (long sid=0;sid<=801;sid++) {
            SeatStatus seatStatus = seatStatusMap.get(sid);
            if (seatStatus != null) {
                if (!seatStatus.occupied) {
                    previousToLastFoundEmpty = lastFoundEmpty;
                    lastFoundEmpty = sid;
                    System.out.println("Empty Found: " + lastFoundEmpty);
                } else {
                    System.out.println("NON Empty Found: " + sid);
                    if ((lastFoundEmpty == sid - 1) && (lastFoundEmpty > previousToLastFoundEmpty + 1)) {
                        possibleMatchingSeat = lastFoundEmpty;
                        break;
                    }
                }
            }
        }
        System.out.println("Possible Seat is: " + possibleMatchingSeat + ", lastFoundEmpty: " + lastFoundEmpty);
    }



    public static class BoardingPass {
        long row;
        long column;

        public BoardingPass(String rawPassLine) {
            List<Boolean> rowEncoding = getBinEncodingFromRaw(0, 7, rawPassLine, 'B');
            row = sumBinaryEncoding(rowEncoding);;
            List<Boolean> columnEncoding = getBinEncodingFromRaw(7, 10, rawPassLine, 'R');
            column = sumBinaryEncoding(columnEncoding);
        }

        public long calculateSeatNumber() {
            return calculateSeatNumber(row, column);
        }

        public static long calculateSeatNumber(long row, long column) {
            return row * 8 + column;
        }

        @Override
        public String toString() {
            return "BoardingPass{" +
                    "row=" + row +
                    ", column=" + column +
                    ", seatNumber=" + calculateSeatNumber() +
                    '}';
        }
    }

    static class SeatStatus {
        long seatId;
        boolean occupied;
        BoardingPass boardingPass;

        public SeatStatus(long seatId, boolean occupied, BoardingPass boardingPass) {
            this.seatId = seatId;
            this.occupied = occupied;
            this.boardingPass = boardingPass;
        }
    }
}
