import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PioDataObject {

	private String url;
	private String accessKey;
	private Integer userId;
	private List<PioInputArgument> inputList = new ArrayList<PioInputArgument>();
	private Date eventTime;	//2015-02-01T07:00:00.000Z
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAccessKey() {
		return accessKey;
	}
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public List<PioInputArgument> getInputList() {
		return inputList;
	}
	public void setInputList(List<PioInputArgument> inputList) {
		this.inputList = inputList;
	}
	public String getFormattedEventTime() {
		return getDateFormat().format(getEventTime());
	}
	public Date getEventTime() {
		return eventTime;
	}
	public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
	}
	public SimpleDateFormat getDateFormat() {
		return dateFormat;
	}
	public void setDateFormat(SimpleDateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}
	
}
