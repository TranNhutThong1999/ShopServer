package kltn.dto;

import java.util.List;

import kltn.util.StatisticalMonth;

public class StatisticalDTO {
	private double general;
	private List<StatisticalMonth> data;
	public StatisticalDTO(double general, List<StatisticalMonth> data) {
		super();
		this.general = general;
		this.data = data;
	}
	public double getGeneral() {
		return general;
	}
	public void setGeneral(double general) {
		this.general = general;
	}
	public List<StatisticalMonth> getData() {
		return data;
	}
	public void setData(List<StatisticalMonth> data) {
		this.data = data;
	}
	
}
