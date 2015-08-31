
public class MyProgram {
	public static void main(String[] args) {
		try {
			if (args.length == 0) {
				TwoWaySerialComm.getInstance().connect(TwoWaySerialComm.getInstance().getArduinoDevice());
			} else if (args.length == 1) {
				TwoWaySerialComm.getInstance().setArduinoDevice(args[0]);
				TwoWaySerialComm.getInstance().connect(TwoWaySerialComm.getInstance().getArduinoDevice());
			} else if (args.length == 2) {
				TwoWaySerialComm.getInstance().setArduinoDevice(args[0]);
				TwoWaySerialComm.getInstance().setPredictionURI(args[1]);
				TwoWaySerialComm.getInstance().connect(TwoWaySerialComm.getInstance().getArduinoDevice());
			} else {
				System.out.println("USAGE: java MyProgram [arduinoDevice] [predictionURI]");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
