package project;

public class RotationManager {
    private int angle;

    private float cosTheta;
    private float sinTheta;

    public RotationManager(int angle) {
        this.angle = angle;
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
        this.angle = angle;
        convertToRadAndSetCosSin();
    }

    public float getCosTheta() {
        return cosTheta;
    }

    public float getSinTheta() {
        return sinTheta;
    }
}
