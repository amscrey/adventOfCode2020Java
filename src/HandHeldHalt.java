import util.AdventFileHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandHeldHalt {

    public static void main(String[] args) {
        List<String> rawLines =
                AdventFileHelper.readLines("/Users/dc24863/dev/supplychain/Advent2/handHeldInstructions.txt");
        Map<Long, Instruction> instructionMap = parseOrderedInstructions(rawLines);
        Assembler ass = new Assembler(instructionMap);
        RunResult res = ass.runInstructionToTerm();
        System.out.println("Terminating RunReplaceResult : " + res);
        RunResult thisTry = tryReplacingInstruction(ass, INSTRUCTION_TYPE.nop, INSTRUCTION_TYPE.jmp);
        System.out.println("First Final Terminating RunReplaceResult : " + thisTry);
        if (thisTry == null) thisTry = tryReplacingInstruction(ass, INSTRUCTION_TYPE.jmp, INSTRUCTION_TYPE.nop);
        else return;
        System.out.println("Final Final Terminating RunReplaceResult : " + thisTry);
    }

    static Map<Long, Instruction> parseOrderedInstructions(List<String> rawLines) {
        Map<Long, Instruction> orderedMap = new HashMap<>();
        for (int i=0;i<rawLines.size();i++) {
            Instruction instr = parseInstruction(rawLines.get(i));
            orderedMap.put((long)i, instr);
        }
        return orderedMap;
    }

    static Instruction parseInstruction(String line) {
        INSTRUCTION_TYPE op = INSTRUCTION_TYPE.valueOf(line.substring(0,3));
        long negPos = 1;
        if (line.charAt(3) == '-') negPos = -1;
        long quantity = Long.parseLong(line.substring(4));
        return new Instruction(op, negPos*quantity);
    }

    static RunResult tryReplacingInstruction(
            Assembler oldAss, INSTRUCTION_TYPE toReplace, INSTRUCTION_TYPE replaceWith) {
        long nextIndexToCheck = 0;
        while (true) {
            long indexToReplace = oldAss.findNextMatch(nextIndexToCheck, toReplace);
            if (indexToReplace == -1) return null;
            Map<Long, Instruction> instructionsCopied = new HashMap<>(oldAss.instructions);
            Instruction replaced = instructionsCopied.get(indexToReplace);
            Instruction replacement = new Instruction(replaceWith, replaced.quantity);
            instructionsCopied.put(indexToReplace, replacement);
            Assembler newAss = new Assembler(instructionsCopied);
            RunResult res = newAss.runInstructionToTerm();
            if (res.ranUntilEnd) return res;
            nextIndexToCheck = indexToReplace + 1;
        }
    }

    static RunResult tryReplacingJMP(Assembler oldAss) {
        long nextIndexToCheck = 0;
        while (true) {
            long indexToReplace = oldAss.findNextMatch(nextIndexToCheck, INSTRUCTION_TYPE.jmp);
            if (indexToReplace == -1) return null;
            Map<Long, Instruction> instructionsCopied = new HashMap<>(oldAss.instructions);
            Instruction replaced = instructionsCopied.get(indexToReplace);
            Instruction replacement = new Instruction(INSTRUCTION_TYPE.nop, replaced.quantity);
            instructionsCopied.put(indexToReplace, replacement);
            Assembler newAss = new Assembler(instructionsCopied);
            RunResult res = newAss.runInstructionToTerm();
            if (res.ranUntilEnd) return res;
            nextIndexToCheck = indexToReplace + 1;
        }
    }

    static enum INSTRUCTION_TYPE {nop, acc, jmp}

    static class Instruction {
        private INSTRUCTION_TYPE operation;
        private long quantity;

        public Instruction(INSTRUCTION_TYPE operation, long quantity) {
            this.operation = operation;
            this.quantity = quantity;
        }

        @Override
        public String toString() {
            return "Instruction{" +
                    "operation='" + operation + '\'' +
                    ", quantity=" + quantity +
                    '}';
        }
    }

    static class Assembler {
        Map<Long, Instruction> instructions;
        private long accumulator;
        Map<Long, Long> instructionsRun;

        public Assembler(Map<Long, Instruction> instructions) {
            this.instructions = instructions;
            accumulator = 0;
            instructionsRun = new HashMap<>();
        }

        public RunResult runInstructionToTerm() {
            long thisIndex = 0;
            while (instructionsRun.get(thisIndex) == null) {
                System.out.println("Running: line " + thisIndex + " -> " + instructions.get(thisIndex));
                instructionsRun.put(thisIndex, thisIndex);
                long nextIndex = runInstruction(thisIndex);
                if (thisIndex == instructions.size()-1) {
                    return new RunResult(true, accumulator);
                }
                thisIndex = nextIndex;
            }
            return new RunResult(false, accumulator);
        }


        //returns next line
        public long runInstruction(long index) {
            long nextIndex = index + 1;
            Instruction instr = instructions.get(index);
            switch (instr.operation) {
                case acc:
                    accumulator = accumulator + instr.quantity;
                    break;
                case jmp:
                    nextIndex = index + instr.quantity;
                    break;
                case nop:
                    break;
            }
            System.out.println("Next Index will be: " + nextIndex);

            return nextIndex;
        }

        public long findNextMatch(long startIndex, INSTRUCTION_TYPE opType) {
            for (long index=startIndex; index<instructions.size(); index++) {
                if (opType.equals(instructions.get(index).operation)) {
                    return index;
                }
            }
            return -1;
        }
    }

    static class RunResult {
        boolean ranUntilEnd;
        long replacedIndex;
        long accumulatorValue;

        public RunResult(boolean ranUntilEnd, long accumulatorValue) {
            this.ranUntilEnd = ranUntilEnd;
            this.accumulatorValue = accumulatorValue;
        }

        @Override
        public String toString() {
            return "RunReplaceResult{" +
                    "ranUntilEnd=" + ranUntilEnd +
                    ", accumulatorValue=" + accumulatorValue +
                    '}';
        }
    }


}
