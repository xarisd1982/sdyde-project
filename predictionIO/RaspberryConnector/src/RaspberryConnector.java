import java.util.ArrayList;
import java.util.List;


public class RaspberryConnector {
	
	public static void main(String[] args) {
		List<Integer> list = new ArrayList<Integer>();
		String input = "node1|158|23|0|0|0#node2|860|21|0|1|6#node3|361|26|0|0|0";
		for (String nodeMsg : input.split("#")) {
			String[] eachNodesInputs = nodeMsg.split("\\|");
			list.add(Integer.valueOf(eachNodesInputs[1]));
			list.add(Integer.valueOf(eachNodesInputs[2]));
			if (eachNodesInputs[3].equals("1") || eachNodesInputs[4].equals("1")) {
				list.add(new Integer(1));
			} else {
				list.add(new Integer(0));
			}
			if (!eachNodesInputs[5].equals("0")) {
				list.add(new Integer(1));
			} else {
				list.add(new Integer(0));
			}
		}
		System.out.println();
	}
}
