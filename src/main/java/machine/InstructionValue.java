package machine;

public class InstructionValue {
    Character mutate;
    int newState;
    Movement movement;

    public InstructionValue(Character mutate, int newState, Movement movement) {
        this.mutate = mutate;
        this.newState = newState;
        this.movement = movement;
    }
}

