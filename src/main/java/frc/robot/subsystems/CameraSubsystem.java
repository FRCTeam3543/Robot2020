package frc.robot.subsystems;

import org.opencv.core.Mat;
import org.opencv.core.Point;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Robot;
import team3543.vision.CenterDropDetection;

public class CameraSubsystem extends Subsystem {
    Thread m_visionThread = null;

    static final int WIDTH = Config.CAMERA_WIDTH;
    static final int HEIGHT = Config.CAMERA_HEIGHT;

    CenterDropDetection detector = new CenterDropDetection();

    @Override
    protected void initDefaultCommand() {
        // nothing for now
    }


    public void initCameraThread() {

        if (m_visionThread == null) {
            m_visionThread = new Thread(() -> {
                // Get the Axis camera from CameraServer
                // AxisCamera camera = CameraServer.getInstance().addAxisCamera(Config.CAMERA_ADDRESS);
                UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();                
                // Set the resolution
                camera.setResolution(WIDTH, HEIGHT);

                // Get a CvSink. This will capture Mats from the camera
                CvSink cvSink = CameraServer.getInstance().getVideo();
                // Setup a CvSource. This will send images back to the Dashboard
                CvSource outputStream
                        = CameraServer.getInstance().putVideo("HUD", WIDTH, HEIGHT);

                // Mats are very memory expensive. Lets reuse this Mat.
                Mat mat = new Mat();

                // This cannot be 'true'. The program will never exit if it is. This
                // lets the robot stop this thread when restarting robot code or
                // deploying.
                while (!Thread.interrupted()) {
                    // Tell the CvSink to grab a frame from the camera and put it
                    // in the source mat.  If there is an error notify the output.
                    if (cvSink.grabFrame(mat) == 0) {
                        // Send the output the error.
                        outputStream.notifyError(cvSink.getError());
                        // skip the rest of the current iteration
                        continue;
                    }

                    addHUD(mat); // add the heads-up display

                    // Give the output stream a new image to display
                    outputStream.putFrame(mat);
                }
            });
            m_visionThread.setDaemon(true);
            m_visionThread.start();
        }
    }

    public static final int THIN = 1;

    void addHUD(Mat mat) {
        HUD hud = HUD.with(mat);

        hud
                .color(HUD.GREEN)
                .thickness(HUD.THIN)
        // Put a halfway line
                .line(HUD.point(WIDTH/2, 0), HUD.point(WIDTH/2, HEIGHT))
                .line(HUD.point(0, HEIGHT/2), HUD.point(WIDTH, HEIGHT/2))

        // 60px from bottom, 120px x 120px target square
                .rectangle(HUD.point(WIDTH/2 - 60, HEIGHT-20-120),
                                HUD.point(WIDTH/2 + 60, HEIGHT-20));

        // Line sensor HUD
        Robot.lineSensor.updateHUD(mat);
        //Robot.Hatch.updateHUD(mat);

        // Plot the path of the ball, when you have one
    }

    public void stopCameraThread() {
        m_visionThread.interrupt();
    }

    Point point(int x, int y) {
        return new Point(x, y);
    }

    Point point(double x, double y) {
        return point((int)x, (int)y);
    }

}

