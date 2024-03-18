package machine;

import parser.Data;

import java.util.HashMap;

public class TuringMachine {
    Character[] tape;
    int head = 0;
    int state;

    protected HashMap<InstructionKey, InstructionValue> updateFunction;

    /*
     * @throws FileNotFoundException if the file with the tm instructions could not be found
     * @throws RuntimeException if there is an error parsing instructions, or if an instruction is not fond
     * */
    public TuringMachine(
            int initialState,
            String input,
            String path
    ) throws Exception {
        updateFunction = Data.loadYaml(path);

        tape = new Character[input.length()];

        // Fill the tape
        for (int i = 0; i < input.length(); i++) {
            tape[i] = input.charAt(i);
        }

        // Set the initial state
        this.state = initialState;
    }


    public void run() throws InterruptedException {
        step();
        display();

        // Make it a little easier to see
        Thread.sleep(100);

        if (state != -1) run();
    }

    void move(Movement m) {
        if (m == Movement.Right) head++;
        else if (m == Movement.Left) head--;

        if (head >= tape.length) extendTape(Movement.Right);
        if (head < 0) extendTape(Movement.Left);
    }

    void extendTape(Movement m) {
        Character[] newTape = new Character[tape.length + 10];
        if (m == Movement.Left) {
            System.arraycopy(tape, 0, newTape, 10, tape.length);
            head += 10;
        } else System.arraycopy(tape, 0, newTape, 0, tape.length);

        tape = newTape;
    }

    void step() {
        // Read the content
        Character content = tape[head];

        // Generate the key
        InstructionKey k = new InstructionKey(state, content);
        InstructionValue v = updateFunction.get(k);

        if (v == null) {
            throw new RuntimeException("No instruction found for (content: " + content + ", state: " + state + ")");
        }

        // Update the turing machine using the instructions
        tape[head] = v.mutate;
        state = v.newState;
        move(v.movement);
    }

    void display() {
        String ANSI_RED = "\u001B[31m";
        String ANSI_BLUE = "\u001B[34m";
        String ANSI_YELLOW = "\u001B[33m";
        String ANSI_RESET = "\u001B[0m";

        // Change this to show more tape
        int diff = 50;
        int len = tape.length;

        // If the tape is too long, it will only print where the head is pointing plus 50 more boxes per side
        for (int i = head - diff; i <= head + diff; i++) {
            if (i >= 0 && i < len) {
                String tapeStr;
                if (tape[i] == null) tapeStr = "#";
                else tapeStr = tape[i] + "";

                // Make the head red, the numbers blue, and spaces yellow
                if (i == head) tapeStr = ANSI_RED + tapeStr;
                else if (tapeStr.equals("0") || tapeStr.equals("1")) tapeStr = ANSI_BLUE + tapeStr;
                else if (tapeStr.equals("_")) tapeStr = ANSI_YELLOW + tapeStr;

                // Ansi reset will bring the colors back to normal
                System.out.print(tapeStr + ANSI_RESET + " | ");
            }
        }

        System.out.println();
    }
}

