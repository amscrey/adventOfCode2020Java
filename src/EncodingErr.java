import util.AdventFileHelper;

import java.util.Arrays;
import java.util.List;

public class EncodingErr {
    public static void main(String[] args) {
        long magicNumber = 1492208709;
        List<String> rawLines =
                AdventFileHelper.readLines("/Users/dc24863/dev/supplychain/Advent2/encodingErrData.txt");
        MemorySpace memorySpace = setupUpInitMemSpace(rawLines, 25);
        long invalid = findFirstInvalidEntry(rawLines, memorySpace);
        System.out.println("First Invalid is: " + invalid);
    }

    static MemorySpace setupUpInitMemSpace(List<String> rawLines, int size) {
        MemorySpace memSpace = new MemorySpace(size);
        for (int i=0;i<size;i++) {
            long entry = Long.parseLong(rawLines.get(i));
            memSpace.pushOnAvailableSpace(entry);
        }
        return memSpace;
    }

    static long findFirstInvalidEntry(List<String> rawLines, MemorySpace memSpace) {
        //System.out.println("MemList: " + memSpace);
        for (int i=memSpace.capacity;i<rawLines.size();i++) {
            System.out.println("begin findloop : " + i);
            long thisEntry = Long.parseLong(rawLines.get(i));
            boolean isValid = memSpace.isValid(thisEntry);
            System.out.println("Checking[" + i + "] : " + thisEntry + ", valid?: " + isValid);
            if (!isValid) return thisEntry;
            memSpace.pushOnFull(thisEntry);
        }
        return -1;
    }

    static class MemorySpace {
        long[] memList;
        int currentTop = 0;
        int capacity;

        public MemorySpace(int capacity) {
            this.capacity = capacity;
            memList = new long[capacity];
        }

        void pushOnAvailableSpace(long nextEntry) {
            memList[currentTop] = nextEntry;
            currentTop++;
        }

        void pushOnFull(long nextEntry) {
            for (int i=0;i<memList.length-1;i++) {
                memList[i] = memList[i+1];
            }
            memList[memList.length-1] = nextEntry;
        }

        boolean isValid(long nextEntry) {
            for (int outer=0;outer<capacity;outer++) {
                // add to each other index and compare
                for (int inner=0;inner<capacity;inner++) {
                    long sum = memList[inner] + memList[outer];
                    System.out.println("outer: " + memList[outer] + "+ inner: " + memList[inner] + " = sum: " + sum);
                    if (inner != outer) {
                        if (nextEntry == sum) return true;
                    }
                }
            }
            return false;
        }

        @Override
        public String toString() {
            return "MemorySpace{" +
                    "memList=" + Arrays.toString(memList) +
                    ", currentTop=" + currentTop +
                    ", capacity=" + capacity +
                    '}';
        }
    }
}
