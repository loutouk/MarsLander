public class NavigationInstructions {

    // instructions[][0] contains thrust instructions (0,1,2,3,4)
    // instructions[][1] contains rotation instructions
    private int[][] instructions;

    public NavigationInstructions(int[][] instructions) {
        this.instructions = instructions;
    }

    public int[] getTerm(int index) {
        return instructions[index];
    }

    public void setTerm(int index, int[] value) {
        instructions[index] = value;
    }

    public int[][] getInstructions() {
        return instructions;
    }

    public int size() {
        return instructions.length;
    }
}
