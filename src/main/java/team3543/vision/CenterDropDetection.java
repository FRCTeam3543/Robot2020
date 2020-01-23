package team3543.vision;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.opencv.core.Mat;

import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import team3543.vision.GripPipeline1.Line;

/**
* GripPipeline class.
*
* @author GRIP
*/
public class CenterDropDetection {
	static final Logger LOGGER = Logger.getLogger(CenterDropDetection.class.getSimpleName());

	// Stores the pipeline.  Created once so we can re-use
	final GripPipeline1 pipeline;

	boolean detected = false;

	public CenterDropDetection()
	{
		pipeline = new GripPipeline1();
	}

	// ToDo - how do we detect?
	public DetectionResult detectOld(Mat image)
	{
		DetectionResult result = new DetectionResult();
		// process the image
		pipeline.process(image);

		// first, see if we have at least two lines
		if (pipeline.filterLinesOutput().size() < 2) {
			LOGGER.info("Did not find 2 lines: "+pipeline.filterLinesOutput().size());
			return result;
		}
		else if (pipeline.filterLinesOutput().size() > 30) {
			LOGGER.info("Too many lines");
			return result;
		}

		// next, go through all the lines and make sure the y1 coord is the uppermost one
		// Note: inverted cartesian - X=0,Y=0 is top left, and then find the two that
		// have the right ratio of closer to farther points
		List<Line> lineList = new ArrayList<>();
		lineList
		// add all the lines, making sure they are drawn top-to-bottom
		.addAll(pipeline.filterLinesOutput().stream().map(line -> {
			// ensure y2 is larger than y1
			return (line.y2 < line.y1)
				? new Line(line.x2, line.y2, line.x1, line.y1)
				: line;
		}).collect(Collectors.toList()));

		// now compute the attributes of the pairs
		// This is probably expensive.
		Pair pair = new Pair();
		for (Line line1 : lineList) {
			for (Line line2 : lineList) {
				if (line1 == line2) continue;
				pair.set(line1, line2);
				if (pair.matches) {
					result.lines = pair;
					result.detected = true;
					return result;
				}
			}
		}

		result.detected = true;
		return result;
	}

	static class BoundingBox {
		final Point topLeft;
		final Point bottomRight;

		BoundingBox(Point topLeft, Point bottomRight) {
			this.topLeft = topLeft;
			this.bottomRight = bottomRight;
		}

		BoundingBox(double x1, double y1, double x2, double y2) {
			this(new Point(x1, y1), new Point(x2, y2));
		}

		boolean contains(Line line) {
			return topLeft.x <= Math.min(line.x1, line.x2)
					&& topLeft.y <= Math.min(line.y1, line.y2)
					&& bottomRight.x >= Math.max(line.x1, line.x2)
					&& bottomRight.y >= Math.max(line.y1, line.y2);
		}
	}


	/**
	 * New detectionResult model.
	 *
	 * Here we just look for lines inside the required *approximate* bounding boxes for the tape
	 *
	 * @param image
	 * @return
	 */
	public DetectionResult detect(Mat image)
	{
		DetectionResult result = new DetectionResult();
		// process the image
		// TODO get bounding boxes
		pipeline.process(image);

		// first, see if we have at least two lines
		if (pipeline.filterLinesOutput().size() < 2) {
			LOGGER.info("Did not find 2 lines: "+pipeline.filterLinesOutput().size());
			return result;
		}
		else if (pipeline.filterLinesOutput().size() > 30) {
			LOGGER.info("Too many lines");
			return result;
		}

		// next, go through all the lines and make sure the y1 coord is the uppermost one
		// Note: inverted cartesian - X=0,Y=0 is top left, and then find the two that
		// have the right ratio of closer to farther points
		List<Line> lineList = new ArrayList<>();
		for (Line line : pipeline.filterLinesOutput()) {

		}
//		lineList
//				// add all the lines, making sure they are drawn top-to-bottom
//				.addAll(pipeline.filterLinesOutput().stream().map(line -> {
//					// ensure y2 is larger than y1
//					return (line.y2 < line.y1)
//							? new Line(line.x2, line.y2, line.x1, line.y1)
//							: line;
//				}).collect(Collectors.toList()));

		// now compute the attributes of the pairs
		// This is probably expensive.
//		Pair pair = new Pair();
//		for (Line line1 : lineList) {
//			for (Line line2 : lineList) {
//				if (line1 == line2) continue;
//				pair.set(line1, line2);
//				if (pair.matches) {
//					result.lines = pair;
//					result.detected = true;
//					return result;
//				}
//			}
//		}

		result.detected = true;
		return result;
	}

	public static class Pair
	{
		static double MIN_DIST = 20; // px
		static double TARGET_RATIO = 0.731;
		static double TARGET_RATIO_TOLERANCE = 0.05;	// percent
		static double Y_DIFF_TOLERANCE = 10; // pixels

		public Line left = new Line(0,0,1000,1000);
		public Line right = new Line(0, 0, -1000, -1000);
		boolean matches = false;
		double topDist = 1000000;
		double topYDiff = 90;
		double bottomYDiff = 90;
		double bottomDist = 1000000;

		Pair() {

		}
		Pair(Line leftLine, Line rightLine)
		{
			set(left, right);
		}

		void set(Line line1, Line line2) {
			// whichever has the leftmost left x1
			if (line1.x1 > line2.x1) {
				this.left = line2;
				this.right = line1;
			}
			else {
				this.left = line1;
				this.right = line2;
			}
			Line topDiff = new Line(0,0,line2.x1 - line1.x1, line2.y1 - line1.y1);
			Line bottomDiff = new Line(0,0, line2.x2 - line1.x2, line2.y2 - line1.y2);
			this.topYDiff = Math.abs(line2.y1 - line1.y1);
			this.topDist = topDiff.length();
			this.bottomYDiff = Math.abs(line2.y2 - line1.y2);
			this.bottomDist = bottomDiff.length();

			// does the ratio of top to bottom distance match
			// first, if they are too close together, forget it
			matches = false;
			if (topDist > MIN_DIST && bottomDist > MIN_DIST) {
				// ok, we can bother to check
				if (Math.abs(topDist/bottomDist - TARGET_RATIO) < TARGET_RATIO_TOLERANCE) {
					// distance ratio matches, let's make sure the y offset for the top
					// and bottom points is within tolerance (i.e. the lines are level)
					if (topYDiff <= Y_DIFF_TOLERANCE && bottomYDiff <= Y_DIFF_TOLERANCE) {
						matches = true;
					}
				}
			}
		}
	}

	static final Scalar WHITE = new Scalar(255,255,255);
	static final int THICK = 5;

	public static class DetectionResult
	{
		public boolean detected = false;
		public Pair lines = null;

		public void plot(Mat mat) {
			Imgproc.line(mat, new Point(lines.left.x1, lines.left.y1), new Point(lines.left.x2, lines.left.y2), WHITE, THICK);
			Imgproc.line(mat, new Point(lines.right.x1, lines.right.y1), new Point(lines.right.x2, lines.right.y2), WHITE, THICK);
		}
	}

}

