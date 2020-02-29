package frc.robot.subsystems;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

/**
 * Heads-up display
 *
 * These are abstractions for updating our display, so we don't have OpenCV imports everywhere
 */
public class HUD {

    public static final int WIDTH = 320;
    public static final int HEIGHT = 240;

    public static final Scalar YELLOW = new Scalar(255,255,200);
    public static final Scalar WHITE = new Scalar(255,255,255);
    public static final Scalar GREEN = new Scalar(0,255,0);
    public static final Scalar RED = new Scalar(0,0,255);
    public static final Scalar BLACK = new Scalar(0,0,0);

    public static final int THIN = 1;
    public static final int THICK = 3;

    private final Mat mat;
    private Scalar color = WHITE;
    private int thickness = THIN;

    private HUD(Mat mat) {
        this.mat = mat;
    }

    public static HUD with(Mat mat) {
        return new HUD(mat);
    }

    public HUD sub() {
        return HUD.with(mat);
    }

    public HUD thickness(int t) {
        this.thickness = t;
        return this;
    }

    public HUD color(Scalar s) {
        this.color = s;
        return this;
    }

    public HUD line(Point from, Point to) {
        Imgproc.line(mat, from, to, color, thickness);
        return this;
    }

    public HUD rectangle(Point topLeft, Point bottomRight) {
        Imgproc.rectangle(mat, topLeft, bottomRight, color, thickness);
        return this;
    }

    public HUD circle(Point center, double radius) {
        Imgproc.circle(mat, center, (int)radius, color, thickness);
        return this;
    }

    public HUD crosshair(Point center, double size) { // size is length of a cross
        int s2 = (int)(size / 2);
        return this.line(point(center.x - s2, center.y), point(center.x + s2, center.y))
                    .line(point(center.x, center.y - s2), point(center.x, center.y + s2));
    }

    public HUD add(Provider p) {
        p.updateHUD(this.sub());
        return this;
    }

    public static Point point(int x, int y) {
        return new Point(x, y);
    }

    public static Point relPoint(double percentX, double percentY) {
        return point(percentX * WIDTH, percentY * HEIGHT);
    }

    public static Point point(double x, double y) {
        return point((int)x, (int)y);
    }

    public interface Provider {
        void updateHUD(HUD hud);
    }

}
