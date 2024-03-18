package machine;

import java.util.Objects;

public class InstructionKey {
    public int state;
    public Character content;

    public InstructionKey(int state, Character content) {
        this.state = state;
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InstructionKey that = (InstructionKey) o;
        return state == that.state && content == that.content;
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, content);
    }
}

