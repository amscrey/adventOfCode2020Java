import util.AdventFileHelper;

import java.util.*;
public class Customs {
    public static void main(String[] args) {
        String rawdata = AdventFileHelper.readAll("/Users/dc24863/dev/supplychain/Advent2/customQuest.txt");
        List<Map<Character,Integer>> groupsAnswers = groupSegmentToUnamiousAnswers(AdventFileHelper.parseRawDataSegments(rawdata));
    }
    static List<Map<Character,Integer>> groupSegmentToUnamiousAnswers(List<AdventFileHelper.RawDataSegment> groupSegs) {
        List<Map<Character,Integer>> listOfAllGroupsAnswerMaps = new ArrayList<>();
        int allGroupAnswerCount = 0; int unamAnswerCountSoFar = 0;
        for (int i=0;i<groupSegs.size();i++) {
            int groupMembers = 1; int unamAnswerCount = 0;
            Map<Character,Integer> groupAnswerMap = new Hashtable();
            String answersString = groupSegs.get(i).getRawData();
            listOfAllGroupsAnswerMaps.add(groupAnswerMap);
            for (int a=0; a<answersString.length();a++) {
                Character questionLetter = answersString.charAt(a);
                if (questionLetter != '\n') { Integer countSoFar = groupAnswerMap.get(questionLetter); if (countSoFar == null) groupAnswerMap.put(questionLetter, 1); else groupAnswerMap.put(questionLetter, countSoFar.intValue() + 1); }
                else {groupMembers++;}
            }
            allGroupAnswerCount = allGroupAnswerCount + groupAnswerMap.size();
            for (Map.Entry<Character,Integer> entry : groupAnswerMap.entrySet()) { if (entry.getValue() >= groupMembers) unamAnswerCount++; }
            unamAnswerCountSoFar = unamAnswerCount + unamAnswerCountSoFar;
            System.out.println("\nGroup " + i + " AnswerMap: " + groupAnswerMap + " map size: " + groupAnswerMap.size() + ", size so far is: " + allGroupAnswerCount + " unamCount: " + unamAnswerCount + " soFar:" + unamAnswerCountSoFar);
        }
        return listOfAllGroupsAnswerMaps;
    }
}
