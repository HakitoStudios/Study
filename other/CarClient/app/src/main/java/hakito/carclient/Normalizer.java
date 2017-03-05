package hakito.carclient;

public class Normalizer {

    private int min;
    private int max;

    public Normalizer(int min, int max) {
        this.max = max;
        this.min = min;
    }

    public double normalize(double val) {
        double v = val / 2 + 0.5;
        return (int) (min + v * (max - min));
    }
}
