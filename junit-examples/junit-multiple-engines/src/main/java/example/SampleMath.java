package example;

public final class SampleMath {

	public int add(int left, int right) {
		return left + right;
	}

	public int clamp(int value, int min, int max) {
		if (value < min) {
			return min;
		}
		if (value > max) {
			return max;
		}
		return value;
	}
}
