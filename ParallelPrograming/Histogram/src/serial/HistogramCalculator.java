package serial;

public class HistogramCalculator extends common.HistogramCalculator{

	private int max;
	public HistogramCalculator(int max) {
		this.max = max;
	}
	@Override
	public String getName() {
		return "Serial";
	}

	@Override
	public int[] calculate(Integer[][] image) {
		int[] finalhistogram = new int[max];
		for(int i=0;i<image.length;i++) {
			for(int j=0;j<image[i].length;j++) {
				finalhistogram[image[i][j]]++;
			}
		}
		return finalhistogram;
	}

}