package parser;


import machine.InstructionKey;
import machine.InstructionValue;
import machine.Movement;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/*
* This part of the code is kinda messy, but we decided to leave it as is
* since parsing yaml has nothing to do with our project.
* */
public class Data {

    public static HashMap<InstructionKey, InstructionValue> loadYaml(String path) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(path);
        Yaml yaml = new Yaml();


        Map<String, Map<String, Object>> data = yaml.load(inputStream);

        HashMap<InstructionKey, InstructionValue> map = new HashMap<>();

        data.forEach((k, v) -> {
            // Make the InstructionKey
            InstructionKey ik = keyStringToInstructionKey(k);
            InstructionValue iv;

            try {
                iv = valueMapToInstructionValue(ik, v);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            map.put(ik, iv);
        });

        return map;
    }

    private static InstructionKey keyStringToInstructionKey(String str) {
        Character content;
        int state;

        // The instruction key string looks as follows:
        // (<character>, <integer>)
        String[] keys = str
                // Remove first and last characters, in other words, remove the brackets
                .substring(1, str.length() - 1)
                // Now remove white space
                .replace(" ", "")
                // and separate by the comma
                .split(",");

        // Handle the character / null
        if (Objects.equals(keys[0], "null")) content = null;
        else content = keys[0].charAt(0);

        // Handle the state
        state = Integer.parseInt(keys[1]);

        return new InstructionKey(state, content);
    }

    private static InstructionValue valueMapToInstructionValue(
            InstructionKey key,
            Map<String, Object> h
    ) {
        // If the key mutate is found, then save mutate as that new character, otherise set it to the original one
        // meaning that this field is optional
        Character mutate;
        if (h.containsKey("mutate")) {
            String mutateOrNull = (String) h.get("mutate");
            if (Objects.equals(mutateOrNull, "null")) mutate = null;
            else mutate = mutateOrNull.charAt(0);
        } else mutate = key.content;

        // Similarly, if the newState key is not found, that means that the user wants the state to stay as it is
        int state;
        if (h.containsKey("newState")) state = (int) h.get("newState");
        else state = key.state;

        Movement movement;
        // By default, we do not move
        if (!h.containsKey("move")) movement = Movement.Stay;
        else {
            // Get the string and turn it into lower case
            String strMovement = ((String) h.get("move")).toLowerCase();

            // We are only interested in the first letter, that way this will accept left, Left, l, L
            if (strMovement.charAt(0) == 'r') movement = Movement.Right;
            else if (strMovement.charAt(0) == 'l') movement = Movement.Left;
            else if (strMovement.charAt(0) == 's') movement = Movement.Stay;
            // If something not starting with l, r, or s is found, then there must be a typo in the instruction set
            else throw new RuntimeException("Could not parse movement " + strMovement);
        }

        // Now we can generate the instruction value
        return new InstructionValue(mutate, state, movement);
    }
}

