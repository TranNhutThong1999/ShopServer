package kltn.util;


public class StatisticalMonth {
	private int time;
	private double total;
	public StatisticalMonth(int time, double total) {
		super();
		this.time = time;
		this.total = total;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	
}
