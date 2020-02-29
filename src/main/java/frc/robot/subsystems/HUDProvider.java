package frc.robot.subsystems;

import org.opencv.core.Mat;

/**
 * Interface for classes that provide heads-up display
 */
public interface HUDProvider {
    void updateHUD(Mat mat);
}
