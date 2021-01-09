import util.AdventFileHelper;

import java.util.List;

public class Bags {
    public static void main(String[] args) {
        List<String> rawLines =
                AdventFileHelper.readLines("/Users/dc24863/dev/supplychain/Advent2/bags.txt");

        List<BagContentsDefinition> bcds = loadDefs(rawLines);
        String bagType = "shiny gold";
        long possibleGoldBagContainerCount = countPossibleContainers(bagType, bcds);
    }

    static List<BagContentsDefinition> loadDefs(java.util.List<String> rawLines) {
        /*
        for (String line: rawLines) {
            String[] lineSegments = line.split("bags contain ");
            String container = lineSegments[0];
            String contentElementsRawNoPeriod = lineSegments[1].substring(0,contentElementsRawNoPeriod.length-1);
            String[] contentElementsRaw = contentElementsRawNoPeriod.split(", ");
            List<BagCollection> contentElements = new ArrayList<>();
            for (String elem: contentElementsRaw) {
                String[] numAndBags = elem.split("^[1-9] ");
                if (numAndBags > 1) {

                }
                else {// must be "no other bags"

                }
            }



        }
        "bags contain";
        ","

*/
        return null;
    }

    static long countPossibleContainers(String bagType, List<BagContentsDefinition> bcds) {
        // FIND IMMEDIATE CONTAINERS
        // loop
        // for each immediate recurse until
        long count = 0;
        for (BagContentsDefinition bcd: bcds) {
            BagCollection match = bcd.getValidMatchingBagContents(bcd.bagType);
            if (match != null) {
                count ++;
                count = count + countPossibleContainers(match.bagType, bcds);
            }
        }
        return count;
    }


    static class BagContentsDefinition {
        String bagType;
        List<BagCollection> validContents;

        BagCollection getValidMatchingBagContents(String bagType) {
            for (BagCollection bc: validContents) {
                if (bagType.equals(bc.bagType)) return bc;
            }
            return null;
        }
    }

    static class BagCollection {
        String bagType;
        int number;
    }
}
