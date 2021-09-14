package kltn.dto;

import java.util.List;

import kltn.util.StatisticalMonth;

public class StatisticalDTO {
	private float general;
	private List<StatisticalMonth> data;
	public StatisticalDTO(float general, List<StatisticalMonth> data) {
		super();
		this.general = general;
		this.data = data;
	}
	public float getGeneral() {
		return general;
	}
	public void setGeneral(float general) {
		this.general = general;
	}
	public List<StatisticalMonth> getData() {
		return data;
	}
	public void setData(List<StatisticalMonth> data) {
		this.data = data;
	}
	
}
