import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


public class PioDataGenerator {

	private Integer numberOfUsers;
	private Integer numberOfEventsPerUser;
	private PioDataObject pioDataObject;

	public static void main (String[] args) throws ParseException {
		PioDataGenerator pdg = new PioDataGenerator();
		pdg.init();
		for (int i=1; i <= pdg.getNumberOfUsers(); i++){
			pdg.pioDataObject.setUserId(i);
			for (int j=0; j < pdg.getNumberOfEventsPerUser(); j++) {
				Random random = new Random();//random.nextInt(max - min + 1) + min
				for (PioInputArgument currentPioInputArgument : pdg.pioDataObject.getInputList()) {
					currentPioInputArgument.setValue(random.nextInt(currentPioInputArgument.getMaxVal()-currentPioInputArgument.getMinVal()+1)+currentPioInputArgument.getMinVal());
				}
				pdg.pioDataObject.setEventTime(new Date(pdg.pioDataObject.getEventTime().getTime() + (long)30*60*1000));
				String output = "curl -i -X POST " + pdg.getPioDataObject().getUrl() + "/events.json?accessKey="
						+ pdg.getPioDataObject().getAccessKey() + " -H \"Content-Type: application/json\" -d"
						+ " '{\"event\" : \"$set\",\"entityType\" : \"user\", \"entityId\" : "
						+ pdg.getPioDataObject().getUserId() + ", \"properties\" : { "
						+ "\"" + pdg.getPioDataObject().getInputList().get(0).getName()+ "\" : "
						+ pdg.getPioDataObject().getInputList().get(0).getValue() 
						+ ", \"" + pdg.getPioDataObject().getInputList().get(1).getName()+ "\" : "
						+ pdg.getPioDataObject().getInputList().get(1).getValue()
						+ ", \"" + pdg.getPioDataObject().getInputList().get(2).getName()+ "\" : "
						+ pdg.getPioDataObject().getInputList().get(2).getValue() 
						+ ", \"" + pdg.getPioDataObject().getInputList().get(3).getName()+ "\" : "
						+ pdg.getPioDataObject().getInputList().get(3).getValue()
						+ ", \"" + pdg.getPioDataObject().getInputList().get(4).getName()+ "\" : "
						+ pdg.getPioDataObject().getInputList().get(4).getValue()
						+ ", \"" + pdg.getPioDataObject().getInputList().get(5).getName()+ "\" : "
						+ pdg.getPioDataObject().getInputList().get(5).getValue()
						+ ", \"" + pdg.getPioDataObject().getInputList().get(6).getName()+ "\" : "
						+ pdg.getPioDataObject().getInputList().get(6).getValue()
						+ ", \"" + pdg.getPioDataObject().getInputList().get(7).getName()+ "\" : "
						+ pdg.getPioDataObject().getInputList().get(7).getValue()
						+ ", \"" + pdg.getPioDataObject().getInputList().get(8).getName()+ "\" : "
						+ pdg.getPioDataObject().getInputList().get(8).getValue()
						+ ", \"" + pdg.getPioDataObject().getInputList().get(9).getName()+ "\" : "
						+ pdg.getPioDataObject().getInputList().get(9).getValue()
						+ ", \"" + pdg.getPioDataObject().getInputList().get(10).getName()+ "\" : "
						+ pdg.getPioDataObject().getInputList().get(10).getValue()
						+ ", \"" + pdg.getPioDataObject().getInputList().get(11).getName()+ "\" : "
						+ pdg.getPioDataObject().getInputList().get(11).getValue()
						+ ", \"" + pdg.getPioDataObject().getInputList().get(12).getName()+ "\" : "
						+ pdg.getPioDataObject().getInputList().get(12).getValue()
						+ " } \"eventTime\" : \"" + pdg.getPioDataObject().getFormattedEventTime() + "\" }'";
				System.out.println(output);
				//curl -i -X POST 127.0.0.1:7070/events.json?
				//accessKey=jJHs8HyUn8CxMEmmCC5LY8MnYtXpdpNQpfW37S6l20J59QNTCFSR18maDdUTApHG
				// -H "Content-Type: application/json" -d '{"event" : "$set",
				// "entityType" : "user", "entityId" : 1, 
				// "properties" : { "attr0" : 60, "attr1" : 8, "attr2" : 1, "plan" : 4 } 
				// "eventTime" : "2015-02-01T07:00:00.000Z" }'
			}
		}
	}
	
	private void init() throws ParseException {
		this.pioDataObject = new PioDataObject();
//		UBUNTU
//		this.pioDataObject.setAccessKey("jJHs8HyUn8CxMEmmCC5LY8MnYtXpdpNQpfW37S6l20J59QNTCFSR18maDdUTApHG");
//		RASPBERRY
//		this.pioDataObject.setAccessKey("TER7TzvtOvEh7r2DmRpmkwwxHjq9EgIQrq4bujbQMyUPAKH8LFxh1VRvsY2pUe2s");
		this.pioDataObject.setAccessKey("Lb4WGSFvoIIpwZPv46niKrT5Gv6YYNtp2mmLyx1U2GaF7QgFnMqSUWc9mcg5Xe2X");
		this.pioDataObject.setUrl("127.0.0.1:7070");
		this.pioDataObject.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
		this.pioDataObject.setEventTime(this.pioDataObject.getDateFormat().parse("2015-02-01T07:00:00.000Z"));
		this.setNumberOfUsers(20);
		this.setNumberOfEventsPerUser(30);
//		this.pioDataObject.setInputList(InputArgumentsListTypes.getInstance().getRandomInputList());
//		this.pioDataObject.setInputList(InputArgumentsListTypes.getInstance().getRoom1presenceInputList());
//		this.pioDataObject.setInputList(InputArgumentsListTypes.getInstance().getRoom2presenceInputList());
		this.pioDataObject.setInputList(InputArgumentsListTypes.getInstance().getRoom3presenceInputList());
	}
	
	public Integer getNumberOfUsers() {
		return numberOfUsers;
	}
	public void setNumberOfUsers(Integer numberOfUsers) {
		this.numberOfUsers = numberOfUsers;
	}
	public Integer getNumberOfEventsPerUser() {
		return numberOfEventsPerUser;
	}
	public void setNumberOfEventsPerUser(Integer numberOfEventsPerUser) {
		this.numberOfEventsPerUser = numberOfEventsPerUser;
	}
	public PioDataObject getPioDataObject() {
		return pioDataObject;
	}
	public void setPioDataObject(PioDataObject pioDataObject) {
		this.pioDataObject = pioDataObject;
	}
}
