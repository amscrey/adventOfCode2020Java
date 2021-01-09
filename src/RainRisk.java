import util.AdventFileHelper;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class RainRisk {
    public static void main(String[] args) {
        List<String> rawData =
                AdventFileHelper.readLines("/Users/dc24863/dev/supplychain/Advent2/ferryInstructions.txt");
        List<RawInstruction> rawInstructions = RawInstruction.createValidRawInstructions(rawData);
        System.out.println("Done reading Raw!\n\n");
        ProcessLog pLog = ProcessLog.processAsPart1(CardinalDirection.E, new Cartesian(0,0), rawInstructions);
        System.out.println("Done Processing: " + pLog);
        System.out.println("Distance: " + pLog.getTaxiCabDistance());
    }


    static class Cartesian {
        int x;
        int y;

        public Cartesian(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Cartesian matrixSum(Cartesian vector) {
            return new Cartesian(this.x + vector.x, this.y + vector.y);
        }

        public int getTaxiCabDistance(Cartesian vector) {
            int x = abs(this.x + vector.x);
            int y = abs(this.y + vector.y);
            return x+y;
        }

        @Override
        public String toString() {
            return "Cartesian{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }


    static class Pivot {
        RelativeDirection direction;
        int quarterTurns;

        public Pivot(RelativeDirection direction, int quarterTurns) {
            this.direction = direction;
            this.quarterTurns = quarterTurns;
        }

        @Override
        public String toString() {
            return "Pivot{" +
                    "direction=" + direction +
                    ", quarterTurns=" + quarterTurns +
                    '}';
        }
    }


    public enum RelativeDirection {
        R, L;

        public static RelativeDirection fromChar(char c) {
            RelativeDirection direction = RelativeDirection.valueOf(String.valueOf(c));
            return direction;
        }
    }

    public enum CardinalDirection {
        N, E, S, W;

        public CardinalDirection rotate(Pivot pivot) {
            CardinalDirection facing = this;
            for (int i=0;i<pivot.quarterTurns;i++) {
                facing = facing.rotateQuarterTurn(pivot.direction);
            }
            return facing;
        }

        CardinalDirection rotateQuarterTurn(RelativeDirection direction) throws RuntimeException {
            int ordinalValue = this.ordinal();
            if (RelativeDirection.R.equals(direction)) {ordinalValue++;}
            else {ordinalValue--;}
            if (ordinalValue == values().length) ordinalValue = 0;
            if (ordinalValue == -1) ordinalValue = values().length -1;
            return values()[ordinalValue];
        }

        public static CardinalDirection fromChar(char c) {
            CardinalDirection direction = CardinalDirection.valueOf(String.valueOf(c));
            return direction;
        }
    }

    static class RawInstruction {
        char letter;
        int number;

        public static List<RawInstruction> createValidRawInstructions(List<String> rawData) {
            List<RawInstruction> rawInstructions = new ArrayList<>();
            for (String datum : rawData) {
                char letter = datum.charAt(0);
                int number = Integer.parseInt(datum.substring(1));
                rawInstructions.add(new RawInstruction(letter, number));
            }
            return rawInstructions;
        }


        // Throws Runtime Exception
        public static Cartesian interpretAsCartesian(RawInstruction instruction, CardinalDirection facing) {
            int quantity = instruction.number;
            CardinalDirection dir = null;
            // try to interpret as a literal cardinal direction
            try {
                dir = CardinalDirection.fromChar(instruction.letter);
            } catch (Exception e) {
                // if instruction letter is F we can interpret direction
                if (instruction.letter == 'F') dir = facing;
                else throw new RuntimeException("Cannot interpret instruction as Cartesian: " + instruction);
            }
            switch (dir) {
                case N: return new Cartesian(0, quantity);
                case E: return new Cartesian(quantity, 0);
                case S: return new Cartesian(0, -1 * quantity);
                case W: return new Cartesian(-1 * quantity, 0);
            }
            throw new RuntimeException("Cannot interpret instruction as Cartesian: " + instruction);
        }

        // Throws Runtime Exception
        public static Pivot interpretAsValidPivot(RawInstruction instruction) {
            if (instruction.number % 90 != 0)
                throw new RuntimeException("Invalid Pivot - angle not a a 90 degree increment: " + instruction.number);
            int quarterTurns = instruction.number / 90;
            RelativeDirection direction = RelativeDirection.fromChar(instruction.letter);
            Pivot interpretedPivot = new Pivot(direction,quarterTurns);
            return interpretedPivot;
        }

        public RawInstruction(char letter, int number) {
            this.letter = letter;
            this.number = number;
        }

        @Override
        public String toString() {
            return "RawInstruction{" +
                    "letter=" + letter +
                    ", number=" + number +
                    '}';
        }
    }

    static class InterpretedInstruction {
        Pivot pivot;
        Cartesian vectorChange;
        Integer forwardQuantity;

        public InterpretedInstruction(Pivot pivot, Cartesian vectorChange, Integer forwardQuantity) {
            this.pivot = pivot;
            this.vectorChange = vectorChange;
            this.forwardQuantity = forwardQuantity;
        }
    }

    public static interface ProcessStep {
        public Cartesian getOriginalShipPosition();
        public Cartesian getResultingShipPosition();
        public String toString();
    }

    static class Part1ProcessStep implements ProcessStep {
        Cartesian originalShipPosition;
        CardinalDirection originalShipFacing;
        RawInstruction rawInstruction;
        InterpretedInstruction interpretedInstruction;
        Cartesian resultingShipPosition;
        CardinalDirection resultingShipFacing;

        public Cartesian getOriginalShipPosition() { return originalShipPosition; }
        public Cartesian getResultingShipPosition() { return resultingShipPosition; }

        public Part1ProcessStep(Cartesian originalShipPosition, CardinalDirection originalShipFacing,
                                RawInstruction rawInstruction, InterpretedInstruction interpretedInstruction,
                                Cartesian resultingShipPosition, CardinalDirection resultingShipFacing) {
            this.originalShipPosition = originalShipPosition;
            this.originalShipFacing = originalShipFacing;
            this.rawInstruction = rawInstruction;
            this.interpretedInstruction = interpretedInstruction;
            this.resultingShipPosition = resultingShipPosition;
            this.resultingShipFacing = resultingShipFacing;
        }

        @Override
        public String toString() {
            return "Part1ProcessStep{" +
                    "originalShipPosition=" + originalShipPosition +
                    ", originalShipFacing=" + originalShipFacing +
                    ", rawInstruction=" + rawInstruction +
                    ", interpretedInstruction=" + interpretedInstruction +
                    ", resultingShipPosition=" + resultingShipPosition +
                    ", resultingShipFacing=" + resultingShipFacing +
                    '}';
        }
    }

    static class Part2ProcessStep implements ProcessStep {
        Cartesian originalShipPosition;
        Cartesian originalWayPointPosition;
        CardinalDirection originalShipFacing;
        RawInstruction rawInstruction;
        InterpretedInstruction interpretedInstruction;
        Cartesian resultingShipPosition;
        Cartesian resultingWaypointPosition;
        CardinalDirection resultingShipFacing;

        public Cartesian getOriginalShipPosition() { return originalShipPosition; }
        public Cartesian getResultingShipPosition() { return resultingShipPosition; }

        public Part2ProcessStep(Cartesian originalShipPosition, Cartesian originalWayPointPosition,
                                CardinalDirection originalShipFacing, RawInstruction rawInstruction,
                                InterpretedInstruction interpretedInstruction, Cartesian resultingShipPosition,
                                Cartesian resultingWaypointPosition, CardinalDirection resultingShipFacing) {
            this.originalShipPosition = originalShipPosition;
            this.originalWayPointPosition = originalWayPointPosition;
            this.originalShipFacing = originalShipFacing;
            this.rawInstruction = rawInstruction;
            this.interpretedInstruction = interpretedInstruction;
            this.resultingShipPosition = resultingShipPosition;
            this.resultingWaypointPosition = resultingWaypointPosition;
            this.resultingShipFacing = resultingShipFacing;
        }

        @Override
        public String toString() {
            return "Part2ProcessStep{" +
                    "originalShipPosition=" + originalShipPosition +
                    ", originalWayPointPosition=" + originalWayPointPosition +
                    ", originalShipFacing=" + originalShipFacing +
                    ", rawInstruction=" + rawInstruction +
                    ", interpretedInstruction=" + interpretedInstruction +
                    ", resultingShipPosition=" + resultingShipPosition +
                    ", resultingWaypointPosition=" + resultingWaypointPosition +
                    ", resultingShipFacing=" + resultingShipFacing +
                    '}';
        }
    }

    static class ProcessLog {
        List<ProcessStep> steps;

        public ProcessLog(List<ProcessStep> steps) {
            this.steps = steps;
        }

        @Override
        public String toString() {
            return "ProcessLog{" +
                    "steps=" + steps +
                    '}';
        }

        public int getTaxiCabDistance() {
            ProcessStep firstStep = steps.get(0);
            ProcessStep finalStep = steps.get(steps.size()-1);
            return firstStep.getOriginalShipPosition().getTaxiCabDistance(finalStep.getResultingShipPosition());
        }

        static ProcessLog processAsPart1(CardinalDirection initFacing, Cartesian initPosition, List<RawInstruction> rawInstructionList) {
            List<ProcessStep> steps = new ArrayList<>();
            CardinalDirection oldFacing = initFacing;
            Cartesian oldPosition = initPosition;
            for (RawInstruction rInstruction : rawInstructionList) {
                if (('L' == rInstruction.letter) || ('R' == rInstruction.letter)) {
                    Pivot interpretedPivot = RawInstruction.interpretAsValidPivot(rInstruction);
                    InterpretedInstruction iInstruction = new InterpretedInstruction(interpretedPivot, null, null);
                    CardinalDirection newFacing = oldFacing.rotate(interpretedPivot);
                    steps.add(new Part1ProcessStep(oldPosition, oldFacing, rInstruction, iInstruction, oldPosition, newFacing));
                    oldFacing = newFacing;
                } else {
                    Cartesian interpretedVector = RawInstruction.interpretAsCartesian(rInstruction, oldFacing);
                    InterpretedInstruction iInstruction = new InterpretedInstruction(null, interpretedVector, null);
                    Cartesian newPosition = oldPosition.matrixSum(interpretedVector);
                    steps.add(new Part1ProcessStep(oldPosition, oldFacing, rInstruction, iInstruction, newPosition, oldFacing));
                    oldPosition = newPosition;
                }
            }
            return new ProcessLog(steps);
        }

        static ProcessLog processAsPart2(CardinalDirection initFacing, Cartesian initPosition, Cartesian initWaypointLocation, List<RawInstruction> rawInstructionList) {
            List<ProcessStep> steps = new ArrayList<>();
            CardinalDirection oldFacing = initFacing;
            Cartesian oldPosition = initPosition;
            Cartesian oldWaypointPosition = initWaypointLocation;
            for (RawInstruction rInstruction : rawInstructionList) {
                if (('L' == rInstruction.letter) || ('R' == rInstruction.letter)) {
                    Pivot interpretedPivot = RawInstruction.interpretAsValidPivot(rInstruction);
                    InterpretedInstruction iInstruction = new InterpretedInstruction(interpretedPivot, null, null);
                    Cartesian newWaypointPosition = rotateWaypoint();
                    steps.add(new Part2ProcessStep(oldPosition, oldWaypointPosition, initFacing, rInstruction, iInstruction, oldPosition, newWaypointPosition, initFacing));
                    oldWaypointPosition = newWaypointPosition;
                } else {
                    Cartesian interpretedVector = RawInstruction.interpretAsCartesian(rInstruction, oldFacing);
                    InterpretedInstruction iInstruction = new InterpretedInstruction(null, interpretedVector, null);
                    Cartesian newPosition = oldPosition.matrixSum(interpretedVector);
                    steps.add(new Part2ProcessStep(oldPosition, oldWaypointPosition, initFacing, rInstruction, iInstruction, newPosition, oldWaypointPosition, initFacing));
                    oldPosition = newPosition;
                }
            }
            return new ProcessLog(steps);
        }

        // TODO this really needs to be in another class
        static Cartesian rotateWaypoint() {
            return null;
        }
    }
}
