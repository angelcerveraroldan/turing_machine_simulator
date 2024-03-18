import machine.TuringMachine;

public class Main {
    public static void main(String[] args) throws Exception {
        TuringMachine add = new TuringMachine(
                8,
                binaryString("pop"),
                "/Users/angelcr/projects/project_ham/turing_machine/src/even_palindromes.yml"
        );

        add.run();
    }

    // Convert the user input into binary, and add underscores as spaces
    public static String binaryString(String s) {
        char[] chars = s.toLowerCase().toCharArray();
        StringBuilder fin = new StringBuilder();

        for (char c : chars) {
            fin.append(Integer.toBinaryString(c)).append("_");
        }

        return fin.toString();
    }
}
