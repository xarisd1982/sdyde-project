import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
//import java.util.Map;
//import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.SynchronousQueue;
import com.google.gson.JsonObject;
import gnu.io.*;


/**
 * && -> start of command
 * $$ -> end of command
 * @author xarisd
 *
 */
public class TwoWaySerialComm implements Runnable{
	
	private SensorsNode sensorsNode1;
	private SensorsNode sensorsNode2;
	private SensorsNode sensorsNode3;
	private String arduinoDevice = "/dev/ttyACM0";
	private String predictionURI = "http://192.168.1.19:8000";
	private PredictionConnector connector = null;
	private InputStream in;
	private OutputStream out;
	private static final TwoWaySerialComm instance = new TwoWaySerialComm();
	public final SynchronousQueue<String> incomingSynchronousQueue = new SynchronousQueue<String>();
	public final SynchronousQueue<String> outgoingSynchronousQueue = new SynchronousQueue<String>();
//	private Map<Integer, String> incomingCommands = new TreeMap<Integer, String>();
//	private Map<Integer, String> outgoingCommands = new TreeMap<Integer, String>();

	public static TwoWaySerialComm getInstance() {
		if (instance != null) {
			return instance;
		}
		return null;
	}
	
	public SynchronousQueue<String> getIncomingSynchronousQueue() {
		return incomingSynchronousQueue;
	}

	public SynchronousQueue<String> getOutgoingSynchronousQueue() {
		return outgoingSynchronousQueue;
	}
	
	public String getArduinoDevice() {
		return arduinoDevice;
	}

	public void setArduinoDevice(String arduinoDevice) {
		this.arduinoDevice = arduinoDevice;
	}

	public String getPredictionURI() {
		return predictionURI;
	}

	public void setPredictionURI(String predictionURI) {
		this.predictionURI = predictionURI;
	}

	public void connect(String devicePortName) throws Exception {
		CommPortIdentifier portIdentifier = CommPortIdentifier
				.getPortIdentifier(devicePortName);
		if (portIdentifier.isCurrentlyOwned()) {
			System.out.println("Error: Port is currently in use");
		} else {
			int timeout = 2000;
			CommPort commPort = portIdentifier.open(this.getClass().getName(),
					timeout);

			if (commPort instanceof SerialPort) {
				SerialPort serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8,
						SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

				TwoWaySerialComm.getInstance().in = serialPort.getInputStream();
				TwoWaySerialComm.getInstance().out = serialPort.getOutputStream();

				
				Thread serialReaderThread = new Thread(new SerialReader(TwoWaySerialComm.getInstance().in));
				serialReaderThread.start();
				Thread serialWriterThread = new Thread(new SerialWriter(TwoWaySerialComm.getInstance().out));
				serialWriterThread.start();

			} else {
				System.out
						.println("Error: Only serial ports are handled by this example.");
			}
			Thread mainThread = new Thread(TwoWaySerialComm.getInstance());
			mainThread.setDaemon(true);
			mainThread.start();
		}
	}

	private static void createEvent(String data) {
		
	}
	
	private static String sendPredictionRequest(PredictionConnector connector, List<Integer> request) throws ExecutionException, InterruptedException, IOException{
		JsonObject result = connector.doPredictionQuery(request);
		return result.toString();
	}
	
	@Override
	public void run() {
		if (connector == null) {
			connector = new PredictionConnector(TwoWaySerialComm.getInstance().getPredictionURI());
		}
		while (true) {
			try {
				String incomingMessage = "";
				try {
					//					System.out.println("Waiting IncomingSynchronousQueue for a message");
					incomingMessage = TwoWaySerialComm.getInstance().getIncomingSynchronousQueue().take(); // blocks until receives data from arduino.
					//					System.out.println("Got message from IncomingSynchronousQueue: " + incomingMessage);
					System.out.println("#############################");
					System.out.println("Got input: " + incomingMessage);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				updateSensorsNodesStatusses(incomingMessage);
				if (hasStatusChanged()) {
					String temp = "";
					temp = "sending prediction request: {";
					for (Integer arg : createPredictionRequestArgumentsList()) {
						temp = temp.concat(String.valueOf(arg) + ",");
					}
					temp = temp.substring(0, temp.length()-1);
					temp=temp.concat("}");
					System.out.println(temp);
					String prediction = sendPredictionRequest(connector, createPredictionRequestArgumentsList());
					System.out.println("got prediction result:" + prediction);
					sensorsNode1.setPreviousStatus(sensorsNode1);
					sensorsNode2.setPreviousStatus(sensorsNode2);
					sensorsNode3.setPreviousStatus(sensorsNode3);
					TwoWaySerialComm.getInstance().getOutgoingSynchronousQueue().put(prediction);//put httpResponse from prediction to arduino to enable actuators according to prediction done
				} else {
					System.out.println("Status hasn't changed");
					System.out.println("#############################");
				}
			} catch (ExecutionException ee) {
				ee.printStackTrace();
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			} catch (Exception e) {
				System.out.println("problematic input. ignoring it...");
			}
		}
	}
	
	
	
	/**
	 * $$nodeX|lum|temp|motion|fsr|currentVal$$
	 * input example1: $$node1|3|26|0|0|0$$
	 * input example2: $$node1|3|26|0|0|0$$$$node2|3|26|0|0|0$$
	 * input example2: $$node1|3|26|0|0|0$$$$node2|3|26|0|0|0$$$$node3|3|26|0|0|0$$
	 * @param input
	 * @return 
	 */
	private void updateSensorsNodesStatusses(String input) {
		String[] eachNodesInput = input.split("\\$\\$");
		for (int i=0; i < eachNodesInput.length;) {
//			System.out.println("eachNodesInput[" + i + "]: " + eachNodesInput[i]);
			if (eachNodesInput[i].isEmpty()) {
				i++;
			} else if (eachNodesInput[i].contains("node1")) {
				if (this.sensorsNode1==null) {
					this.sensorsNode1 = new SensorsNode(eachNodesInput[i]);
				} else if (!this.sensorsNode1.isStatusEqual(new SensorsNode(eachNodesInput[i]))) {
					this.sensorsNode1.updateStatusFromInputString(eachNodesInput[i]);
				}
				i++;
			} else if (eachNodesInput[i].contains("node2")) {
				if (this.sensorsNode2==null) {
					this.sensorsNode2 = new SensorsNode(eachNodesInput[i]);
				} else if (!this.sensorsNode2.isStatusEqual(new SensorsNode(eachNodesInput[i]))) {
					this.sensorsNode2.updateStatusFromInputString(eachNodesInput[i]);
				}
				i++;
			} else if (eachNodesInput[i].contains("node3")) {
				if (this.sensorsNode3==null) {
					this.sensorsNode3 = new SensorsNode(eachNodesInput[i]);
				} else if (!this.sensorsNode3.isStatusEqual(new SensorsNode(eachNodesInput[i]))) {
					this.sensorsNode3.updateStatusFromInputString(eachNodesInput[i]);
				}
				i++;
			} else {
				i++;
			}
		}
	}
	
	
	private boolean hasStatusChanged(){
		if (
				sensorsNode1!=null && sensorsNode2!=null && sensorsNode3!=null
				&& (sensorsNode1.statusHasChanged() || sensorsNode2.statusHasChanged() || sensorsNode3.statusHasChanged())
			) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 
	 * @return output parameters: node1Temp,node1Lum,node1Presence,node1Current,node2Temp,node2Lum,node2Presence,node2Current,node3Temp,node3Lum,node3Presence,node3Current
	 */
	private List<Integer> createPredictionRequestArgumentsList() {
		List<Integer> predictionQueryArgumentsList = new ArrayList<Integer>();
		predictionQueryArgumentsList.add(Integer.valueOf(this.sensorsNode1.getTemperature()));
		predictionQueryArgumentsList.add(Integer.valueOf(this.sensorsNode1.getLuminosity()));
		predictionQueryArgumentsList.add(Integer.valueOf(this.sensorsNode1.getPresence()));
		predictionQueryArgumentsList.add(Integer.valueOf(this.sensorsNode1.getCurrent()));
		predictionQueryArgumentsList.add(Integer.valueOf(this.sensorsNode2.getTemperature()));
		predictionQueryArgumentsList.add(Integer.valueOf(this.sensorsNode2.getLuminosity()));
		predictionQueryArgumentsList.add(Integer.valueOf(this.sensorsNode2.getPresence()));
		predictionQueryArgumentsList.add(Integer.valueOf(this.sensorsNode2.getCurrent()));
		predictionQueryArgumentsList.add(Integer.valueOf(this.sensorsNode3.getTemperature()));
		predictionQueryArgumentsList.add(Integer.valueOf(this.sensorsNode3.getLuminosity()));
		predictionQueryArgumentsList.add(Integer.valueOf(this.sensorsNode3.getPresence()));
		predictionQueryArgumentsList.add(Integer.valueOf(this.sensorsNode3.getCurrent()));
		return predictionQueryArgumentsList;
	}

	public static class SerialReader implements Runnable {
		InputStream in;
		String input = "";
		/*BufferedReader br = null;
		StringBuilder sb = null;
		String line;*/
		
		public SerialReader(InputStream in) {
			this.in = in;
			try{
				this.in.skip(this.in.available());
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}

		public void run() {
			System.out.println("start reading");
			/*while (true) {
				try{
					sb = new StringBuilder();
					br = new BufferedReader(new InputStreamReader(in));
					while ((line = br.readLine()) != null) {
						sb.append(line);
					}
					line = sb.toString();
					System.out.println("got something: '" + line + "'");
					TwoWaySerialComm.getInstance().getIncomingSynchronousQueue().put(line);
					line="";
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}*/
			String input = "";
			byte[] buffer = new byte[1024];
			int len = -1;
			try {
				while ((len = this.in.read(buffer)) > -1) {
					try {
						input = input.concat(new String(buffer, 0, len));
//						System.out.println("input: " + input);
						if(countMatches(input, "$$") >= 2) {
							input = input.substring(input.indexOf("$$"), input.lastIndexOf("$$")+2);
							String[] eachNodesInput = input.split("\\$\\$");
							if (eachNodesInput.length==0) {
								input="";
								continue;
							}
//							System.out.println("Got input from arduino: '" + input + "'");
							if (input.startsWith("$$node") && input.endsWith("$$") && countMatches(input, "|")>=5) {
								
								TwoWaySerialComm.getInstance().getIncomingSynchronousQueue().put(input);
							}
//							System.out.println("Pass arduino input for processing");
							input = "";
							len=-1;
							buffer = new byte[1024];
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
//					System.out.println("." + input);
//					System.out.print(new String(buffer, 0, len));//for debug reasons. to be commented out
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
//			System.out.println("stop reading");
		}
		
		private int countMatches(String input, String findStr) {
			int lastIndex = 0;
			int count = 0;
			while(lastIndex != -1){
			    lastIndex = input.indexOf(findStr,lastIndex);
			    if(lastIndex != -1){
			        count ++;
			        lastIndex += findStr.length();
			    }
			}
			 return count;
		}
	}

	public static class SerialWriter implements Runnable {
		OutputStream out;

		public SerialWriter(OutputStream out) {
			this.out = out;
		}

		public void run() {
			try {
				int c = 0;
				
				while (true) {
//					System.out.println("ready to write...");
					String messageToWrite = TwoWaySerialComm.getInstance().getOutgoingSynchronousQueue().take();
//					System.out.println("got to write:"); 
					System.out.println("predictionResult:".concat(messageToWrite));
					if (messageToWrite.contains("2.0")) {
						System.out.println("Node1's actuator should be enabled (bedroom)");
					} else if (messageToWrite.contains("3.0")) {
						System.out.println("Node2's actuator should be enabled (guestroom)");
					} else if (messageToWrite.contains("4.0")) {
						System.out.println("Node3's actuator should be enabled (study)");
					}
					System.out.println("#############################");
					String temp = "predictionResult:".concat(messageToWrite);
					for (int i=0; i< temp.length();i++) {
						char character=temp.charAt(i);
						this.out.write(character);
					}
				}
//				while ((c = System.in.read()) > -1) {
//					this.out.write(c);
//				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}