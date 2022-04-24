package project;

/**
 * This class is used when we want to rotate the direction of an L-System. It takes care of converting angles to radians
 * and setting the sin and cos thetas.
 */
public class RotationManager {
    private int angle;

    private float cosTheta;
    private float sinTheta;

    public RotationManager(int angle) {
        this.angle = angle % 360;
        convertToRadAndSetCosSin();
    }

    private void convertToRadAndSetCosSin() {
        double angleToRadians = angle * (Math.PI / 180);
        cosTheta = (float) Math.cos(angleToRadians);
        sinTheta = (float) Math.sin(angleToRadians);
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle % 360;
        convertToRadAndSetCosSin();
    }

    public float getCosTheta() {
        return cosTheta;
    }

    public float getSinTheta() {
        return sinTheta;
    }
}
