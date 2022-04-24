package project;

/**
 * A state represents the current coordinates of an L-System line, their direction and length.
 * This class is used by the [ and ] operators of the L-System, pushing and popping states into a stack.
 * @author Vyacheslav Novak
 */
public class State {
    private final float x1;
    private final float y1;
    private final float x2;
    private final float y2;
    private final int angle;
    private final float length;

    public State(float x1, float y1, float x2, float y2, int angle, float length) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.angle = angle;
        this.length = length;
    }

    public float getX1() {
        return x1;
    }

    public float getY1() {
        return y1;
    }

    public float getX2() {
        return x2;
    }

    public float getY2() {
        return y2;
    }

    public int getAngle() {
        return angle;
    }

    public float getLength() {
        return length;
    }
}
