import java.io.BufferedReader;
import java.math.BigInteger;
import java.util.List;

public class ExCounter {
    public static void main(String[] args) {
        BufferedReader reader;
        List<String> lines = PasswordValidator.LineReader.readLines("/Users/dc24863/dev/supplychain/Advent2/trees.txt");
        RowData lastRowData = runSlopeSim(lines, 1, 1);
        BigInteger slope11Hits = BigInteger.valueOf(lastRowData.getAssessment().sumOfHitsSoFar);
        System.out.println("\nLast Row for Slope 1/1: --" + lastRowData + "\n");
        lastRowData = runSlopeSim(lines, 3, 1);
        BigInteger slope31Hits = BigInteger.valueOf(lastRowData.getAssessment().sumOfHitsSoFar);
        System.out.println("\nLast Row for Slope 3/1: --" + lastRowData + "\n");
        lastRowData = runSlopeSim(lines, 5, 1);
        BigInteger slope51Hits = BigInteger.valueOf(lastRowData.getAssessment().sumOfHitsSoFar);
        System.out.println("\nLast Row for Slope 5/1: --" + lastRowData + "\n");
        lastRowData = runSlopeSim(lines, 7, 1);
        BigInteger slope71Hits = BigInteger.valueOf(lastRowData.getAssessment().sumOfHitsSoFar);
        System.out.println("\nLast Row for Slope 7/1: --" + lastRowData + "\n");
        lastRowData = runSlopeSim(lines, 1, 2);
        BigInteger slope12Hits = BigInteger.valueOf(lastRowData.getAssessment().sumOfHitsSoFar);
        System.out.println("\nLast Row for Slope 1/2: --" + lastRowData + "\n");
        BigInteger product = slope11Hits.multiply(slope31Hits).multiply(slope51Hits).multiply(slope71Hits).multiply(slope12Hits);
        System.out.println("\nProduct is: " + product + "\n");
    }

    static RowData runSlopeSim(List<String> lines, int horizontalOffset, int vertOffset) {
        BasicLinesData linesData = new BasicLinesData(lines);
        int rowIndex = 0;
        RowData rd0 = new RowData(rowIndex, 0, 0, lines.get(0), 0);
        rowIndex = rowIndex + vertOffset;
        int lastVirtCol = 0;
        int hitCount = rd0.getAssessment().getSumOfHitsSoFar();
        RowData lastRowData = null;
        while (rowIndex < linesData.rowCount) {
            // get row index
            String thisLine = lines.get(rowIndex);
            // get this rows virtual col index
            int virtColIndex = getNextRowsVirtualColumnIndex(lastVirtCol, horizontalOffset);
            int realColIndex = getRealColumnIndex(virtColIndex, linesData.getColCount());
            // build rowdata
            lastRowData = new RowData(rowIndex, realColIndex, virtColIndex, thisLine, hitCount);
            //System.out.println(lastRowData);
            // prepare for next iter
            lastVirtCol = virtColIndex;
            hitCount = lastRowData.getAssessment().getSumOfHitsSoFar();
            rowIndex = rowIndex + vertOffset;
        }
        return lastRowData;
    }

    static int getNextRowsVirtualColumnIndex(int previousVirtualColIndex, int offset) {
      return previousVirtualColIndex + offset;
    }

    static int getRealColumnIndex(int virtualColIndex, int rowSize) {
        int realColumnIndex = virtualColIndex;
        if (virtualColIndex >= rowSize) realColumnIndex = virtualColIndex % rowSize;
        return realColumnIndex;
    }

    public static class BasicLinesData {
        int rowCount;
        int colCount;

        public BasicLinesData(List<String> lines) {
            this.rowCount = lines.size();
            this.colCount = lines.get(0).length();
        }

        public int getRowCount() {
            return rowCount;
        }

        public int getColCount() {
            return colCount;
        }
    }


    public static class RowData {
        private int rowIndex;
        private int trueColIndex;
        private int virtualColIndex;
        private String rowData;
        private RowAssessment assessment;

        public RowData(int rowIndex, int trueColIndex, int virtualColIndex, String rowData, int hitsBefore) {
            this.rowIndex = rowIndex;
            this.trueColIndex = trueColIndex;
            this.virtualColIndex = virtualColIndex;
            this.rowData = rowData;
            boolean hit = false;
            if (rowData.charAt(trueColIndex) == '#') {
                hit = true;
                hitsBefore++;
            }
            this.assessment = new RowAssessment(rowIndex, hit, hitsBefore);
        }

        @Override
        public String toString() {
            return "RowData{" +
                    "rowIndex=" + rowIndex +
                    ", trueColIndex=" + trueColIndex +
                    ", virtualColIndex=" + virtualColIndex +
                    ", rowData='" + rowData + '\'' +
                    ", assessment=" + assessment +
                    '}';
        }

        public RowAssessment getAssessment() {
            return assessment;
        }

        public int getRowIndex() {
            return rowIndex;
        }

        public int getTrueColIndex() {
            return trueColIndex;
        }

        public int getVirtualColIndex() {
            return virtualColIndex;
        }

        public String getRowData() {
            return rowData;
        }
    }

    public static class RowAssessment {
        private int rowIndex;
        private boolean hit;
        private int sumOfHitsSoFar;

        @Override
        public String toString() {
            return "RowAssessment{" +
                    "rowIndex=" + rowIndex +
                    ", hit=" + hit +
                    ", sumOfHitsSoFar=" + sumOfHitsSoFar +
                    '}';
        }

        public RowAssessment(int rowIndex, boolean hit, int sumOfHitsSoFar) {
            this.rowIndex = rowIndex;
            this.hit = hit;
            this.sumOfHitsSoFar = sumOfHitsSoFar;
        }

        public int getRowIndex() {
            return rowIndex;
        }

        public boolean isHit() {
            return hit;
        }

        public int getSumOfHitsSoFar() {
            return sumOfHitsSoFar;
        }
    }
}
