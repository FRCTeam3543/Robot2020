package frc.robot.subsystems;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;

public class BallCamera {
    public BallCamera() {
        CameraServer.getInstance().startAutomaticCapture();
    }

    public boolean checkForBall() {
        final CvSink sink = CameraServer.getInstance().getVideo();
        Mat image = new Mat();
        if (sink.grabFrame(image) == 0) { // 640 by 480
            Robot.LOGGER.info("Image fetch failed: "+sink.getError());
            return false;
        }
        // now we resize the image to 40 x 30
        // Ball if the middle 50% is more than N% bright

        // first, resize the image to 40 x 30
        image = resizeImage(image);
        // now, grayscale the image
        image = grayscale(image);

        // now get the pixels in the middle.  Rows from 10 to 20,
        // cols from 10 to 30.
        int totalBrightPixels = 0;
        final double threshold = 128;
        final double areaThreshold = 75.0;

        double val;
        double maxVal = 0.0;
        int minRow = 2;
        int maxRow = 15;
        int minCol = 13;
        int maxCol = 27;

        for (int row = minRow; row < maxRow; row++) {
            for (int col = minCol; col < maxCol; col++) {
                // should be one channel
                val = image.get(row, col)[0];
                if (val > threshold)
                    totalBrightPixels++;
                maxVal = Math.max(val, maxVal);
            }
        }
        final double area = (maxRow - minRow) * (maxCol - minCol);
        final double percentage = (double) totalBrightPixels / area * 100;

        SmartDashboard.putNumber("Target area", area);
        SmartDashboard.putNumber("Total bright pixels", totalBrightPixels);
        SmartDashboard.putNumber("Ball percentage", percentage);
        SmartDashboard.putNumber("Max pixel val", maxVal);

        final boolean ret = percentage > areaThreshold;
        SmartDashboard.putBoolean("Ball detected", ret);

        return ret;
    }

    private Mat resizeImage(final Mat input) {
        final Mat output = new Mat();
        Imgproc.resize(input, output, new Size(40, 30), 0.0, 0.0, Imgproc.INTER_CUBIC);
        return output;
    }

    private Mat grayscale(final Mat input) {
        final Mat output = new Mat();
		switch (input.channels()) {
			case 1:
				// If the input is already one channel, it's already desaturated
				input.copyTo(output);
				break;
			case 3:
				Imgproc.cvtColor(input, output, Imgproc.COLOR_BGR2GRAY);
				break;
			case 4:
				Imgproc.cvtColor(input, output, Imgproc.COLOR_BGRA2GRAY);
				break;
			default:
				throw new IllegalArgumentException("Input to desaturate must have 1, 3, or 4 channels");
        }
        return output;
	}

    private Mat hsvThreshold(Mat input) {
        Mat out = new Mat();
        double[] hue = {0.0, 163.1918505942275};
		double[] sat = {0.0, 255.0};
		double[] val = {190.3327338129496, 255.0};

        Imgproc.cvtColor(input, out, Imgproc.COLOR_BGR2HSV);
        Core.inRange(out, new Scalar(hue[0], sat[0], val[0]), new Scalar(hue[1], sat[1], val[1]), out);
        return out;
    }

}
