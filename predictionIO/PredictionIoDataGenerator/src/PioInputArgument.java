public class PioInputArgument {

	private Integer minVal;
	private Integer maxVal;
	private String name;
	private Integer value;

	public PioInputArgument() {
	}
	
	public PioInputArgument(String name, Integer minVal, Integer maxVal) {
		this.name = name;
		this.minVal = minVal;
		this.maxVal = maxVal;
	}
	
	public PioInputArgument(String name, Integer value, Integer minVal, Integer maxVal) {
		this.name = name;
		this.value = value;
		this.minVal = minVal;
		this.maxVal = maxVal;
	}
	
	public Integer getMinVal() {
		return minVal;
	}
	public void setMinVal(Integer minVal) {
		this.minVal = minVal;
	}
	public Integer getMaxVal() {
		return maxVal;
	}
	public void setMaxVal(Integer maxVal) {
		this.maxVal = maxVal;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
}