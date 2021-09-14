package kltn.util;


public class StatisticalMonth {
	private int time;
	private float total;
	public StatisticalMonth(int time, float total) {
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
	public float getTotal() {
		return total;
	}
	public void setTotal(float total) {
		this.total = total;
	}
	
}
