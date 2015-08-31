
public class SensorsNode {
	String nodeName;
	Integer luminosity;
	Integer temperature;
	Integer presence;
	Integer current;
	SensorsNode previousStatus;

	public SensorsNode(String nodeName, Integer luminosity, Integer temperature, Integer presence, Integer current) {
		this.nodeName = nodeName;
		this.luminosity = luminosity;
		this.temperature = temperature;
		this.presence = presence;
		this.current = current;
	}
	
	/**
	 * node1|luminosity|temperature|IR-presence|FSR|Current
	 * @param inputString
	 */
	public SensorsNode(String inputString) {
		updateStatusFromInputString(inputString);
		this.previousStatus = null;
	}
	
	/**
	 * change node's status from input string. Put oldStatus to previousStatus.
	 * @param inputString
	 */
	public void updateStatusFromInputString(String inputString) {
		this.previousStatus=new SensorsNode(this.nodeName, this.luminosity, this.temperature, this.presence, this.current);
		String[] values = inputString.split("\\|");
		this.nodeName = values[0];
		this.luminosity = Integer.valueOf(values[1]);
		this.temperature = Integer.valueOf(values[2]);
		this.presence = (Integer.valueOf(values[3]) + Integer.valueOf(values[4]) == 0)?0:1;
		this.current = (Integer.valueOf(values[5])==0)?0:1;
	}
	
	public String toString() {
		String result = this.nodeName.concat("|").concat(this.luminosity.toString()).concat("|").concat(this.temperature.toString()).concat("|")
						.concat(this.presence.toString()).concat("|").concat(this.current.toString());
		return result;
	}
	
	/**
	 * method to determine whether whether the status of the current node is considered changed compared to another's node.
	 * @return
	 */
	public boolean isStatusEqual(SensorsNode anotherSensorsNode) {
		boolean result = false;
		if ( anotherSensorsNode.getNodeName().equals(this.getNodeName())
				&& Math.abs((((double)anotherSensorsNode.getLuminosity()-(double)this.getLuminosity())*(double)100)/(double)anotherSensorsNode.getLuminosity()) < (double)10
				&& Math.abs((anotherSensorsNode.getLuminosity() - this.getLuminosity())) < 50
				&& Math.abs((((double)anotherSensorsNode.getTemperature()-(double)this.getTemperature())*(double)100)/(double)anotherSensorsNode.getTemperature()) < (double)10
				&& Math.abs((anotherSensorsNode.getTemperature() - this.getTemperature())) < 2
				&& anotherSensorsNode.getPresence()==this.getPresence()
				&& anotherSensorsNode.getCurrent()==this.getCurrent()
			) {
			result = true;
		}
		return result;
	}
	
	/**
	 * method to determine whether whether the status of the current node is considered changed. If previous status is null status is considered changed in any case
	 * @return
	 */
	public boolean statusHasChanged() {
		if (this.getPreviousStatus() != null) {
			return !isStatusEqual(this.getPreviousStatus());
		} else {
			return true;
		}
	}
	
	public Integer getLuminosity() {
		return luminosity;
	}
	public void setLuminosity(Integer luminosity) {
		this.luminosity = luminosity;
	}
	public Integer getTemperature() {
		return temperature;
	}
	public void setTemperature(Integer temperature) {
		this.temperature = temperature;
	}
	public Integer getPresence() {
		return presence;
	}
	public void setPresence(Integer presence) {
		this.presence = presence;
	}
	public Integer getCurrent() {
		return current;
	}
	public void setCurrent(Integer current) {
		this.current = current;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public SensorsNode getPreviousStatus() {
		return previousStatus;
	}
	public void setPreviousStatus(SensorsNode previousStatus) {
		this.previousStatus = previousStatus;
	}
}
