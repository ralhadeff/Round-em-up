package com.raphael.ecosystem;

public class Support {

	public static int safeDivide(int a, int b, int def) {
		if (b != 0)
			return (a / b);
		return def;
	}

	public static float safeDivide(float a, float b, float def) {
		if (b != 0)
			return (a / b);
		return def;
	}

	public static int getVectorAngle(int startX, int startY, int endX, int endY) {
		// returns an angle from 0 and 360. -1 means vector 0.

		int angle;

		if (startY == endY) {
			if (startX == endX)
				return -1;
			else
				return (startX > endX) ? 270 : 90;
		}

		if (startY > endY) {
			angle = (int) Math.toDegrees(Math.atan((float) (endX - startX)
					/ (float) (startY - endY)));
		} else {
			angle = (int) Math.toDegrees(Math.atan((float) (endX - startX)
					/ (float) (startY - endY)));
			angle += 180;
		}
		if (angle < 0)
			angle += 360;

		return angle;
	}

	public static int getVectorLength(int startX, int startY, int endX, int endY) {

		return (int) Math.sqrt((startX - endX) * (startX - endX)
				+ (startY - endY) * (startY - endY));

	}

	public static boolean angleIsWithin(float destinationDirection, float direction, int delta) {

		if ((destinationDirection >= direction)
				&& ((destinationDirection - direction <= delta) || (destinationDirection - direction >= 360 - delta)))
			return true;
		if ((destinationDirection < direction)
				&& ((direction - destinationDirection <= delta) || (direction - destinationDirection >= 360 - delta)))
			return true;
		return false;
	}

	public static float rotateAngle(float source, float target, int desiredDelta) {
		float targetHelper = target - source;
		if (targetHelper < 0)
			targetHelper += 360;
		if (targetHelper <= 180)
			if (source + desiredDelta < 360)
				return (source + desiredDelta);
			else
				return (source + desiredDelta - 360);
		else if (source - desiredDelta >= 0)
			return (source - desiredDelta);
		else
			return (source - desiredDelta + 360);
	}

}
