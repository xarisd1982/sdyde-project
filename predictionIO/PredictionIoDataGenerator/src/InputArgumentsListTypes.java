import java.util.ArrayList;
import java.util.List;

/**
 * 	arg1->temperature
	arg2->light
	arg3->presence
	arg4->light switch
	
	plan1->act1->off / act2->off / act3->off
	plan2->act1->on / act2->off / act3->off
	plan3->act1->off / act2->on / act3->off
	plan4->act1->off / act2->off / act3->on
	plan5->act1->on / act2->on / act3->off
	plan6->act1->on / act2->off / act3->on
	plan7->act1->off / act2->on / act3->on
	plan8->act1->on / act2->on / act3->on
 */
public class InputArgumentsListTypes {
	private static final InputArgumentsListTypes instance = new InputArgumentsListTypes();
	private List<PioInputArgument> randomInputList = new ArrayList<PioInputArgument>();
	private List<PioInputArgument> room1presenceInputList = new ArrayList<PioInputArgument>();
	private List<PioInputArgument> room2presenceInputList = new ArrayList<PioInputArgument>();
	private List<PioInputArgument> room3presenceInputList = new ArrayList<PioInputArgument>();
	
	
	static {
		initRandomInputList();
		initRoom1presenceInputList();
		initRoom2presenceInputList();
		initRoom3presenceInputList();
	}
	
	private InputArgumentsListTypes(){
	}
	
	public static InputArgumentsListTypes getInstance() {
		return instance;
	}

	public List<PioInputArgument> getRandomInputList() {
		return randomInputList;
	}
	public List<PioInputArgument> getRoom1presenceInputList() {
		return room1presenceInputList;
	}
	public List<PioInputArgument> getRoom2presenceInputList() {
		return room2presenceInputList;
	}
	public List<PioInputArgument> getRoom3presenceInputList() {
		return room3presenceInputList;
	}
	
	private static void initRandomInputList() {
		PioInputArgument node1temp = new PioInputArgument("attr1", (int)5, (int)40);
		instance.randomInputList.add(node1temp);
		PioInputArgument node1light = new PioInputArgument("attr2", (int)0, (int)2000);
		instance.randomInputList.add(node1light);
		PioInputArgument node1presence = new PioInputArgument("attr3", (int)0, (int)1);
		instance.randomInputList.add(node1presence);
		PioInputArgument node1switch = new PioInputArgument("attr4", (int)0, (int)1);
		instance.randomInputList.add(node1switch);
		PioInputArgument node2temp = new PioInputArgument("attr5", (int)5, (int)40);
		instance.randomInputList.add(node2temp);
		PioInputArgument node2light = new PioInputArgument("attr6", (int)0, (int)2000);
		instance.randomInputList.add(node2light);
		PioInputArgument node2presence = new PioInputArgument("attr7", (int)0, (int)1);
		instance.randomInputList.add(node2presence);
		PioInputArgument node2switch = new PioInputArgument("attr8", (int)0, (int)1);
		instance.randomInputList.add(node2switch);
		PioInputArgument node3temp = new PioInputArgument("attr9", (int)5, (int)40);
		instance.randomInputList.add(node3temp);
		PioInputArgument node3light = new PioInputArgument("attr10", (int)0, (int)2000);
		instance.randomInputList.add(node3light);
		PioInputArgument node3presence = new PioInputArgument("attr11", (int)0, (int)1);
		instance.randomInputList.add(node3presence);
		PioInputArgument node3switch = new PioInputArgument("attr12", (int)0, (int)1);
		instance.randomInputList.add(node3switch);
		PioInputArgument plan = new PioInputArgument("plan", (int)1, (int)8);
		instance.randomInputList.add(plan);
	}
	
	private static void initRoom1presenceInputList() {
		PioInputArgument node1temp = new PioInputArgument("attr1", (int)20, (int)28);
		instance.room1presenceInputList.add(node1temp);
		PioInputArgument node1light = new PioInputArgument("attr2", (int)800, (int)1000);
		instance.room1presenceInputList.add(node1light);
		PioInputArgument node1presence = new PioInputArgument("attr3", (int)1, (int)1);
		instance.room1presenceInputList.add(node1presence);
		PioInputArgument node1switch = new PioInputArgument("attr4", (int)1, (int)1);
		instance.room1presenceInputList.add(node1switch);
		PioInputArgument node2temp = new PioInputArgument("attr5", (int)20, (int)28);
		instance.room1presenceInputList.add(node2temp);
		PioInputArgument node2light = new PioInputArgument("attr6", (int)0, (int)1000);
		instance.room1presenceInputList.add(node2light);
		PioInputArgument node2presence = new PioInputArgument("attr7", (int)0, (int)0);
		instance.room1presenceInputList.add(node2presence);
		PioInputArgument node2switch = new PioInputArgument("attr8", (int)0, (int)0);
		instance.room1presenceInputList.add(node2switch);
		PioInputArgument node3temp = new PioInputArgument("attr9", (int)20, (int)28);
		instance.room1presenceInputList.add(node3temp);
		PioInputArgument node3light = new PioInputArgument("attr10", (int)0, (int)1000);
		instance.room1presenceInputList.add(node3light);
		PioInputArgument node3presence = new PioInputArgument("attr11", (int)0, (int)0);
		instance.room1presenceInputList.add(node3presence);
		PioInputArgument node3switch = new PioInputArgument("attr12", (int)0, (int)0);
		instance.room1presenceInputList.add(node3switch);
		PioInputArgument plan = new PioInputArgument("plan", (int)2, (int)2);
		instance.room1presenceInputList.add(plan);
	}
	
	private static void initRoom2presenceInputList() {
		PioInputArgument node1temp = new PioInputArgument("attr1", (int)20, (int)28);
		instance.room2presenceInputList.add(node1temp);
		PioInputArgument node1light = new PioInputArgument("attr2", (int)0, (int)1000);
		instance.room2presenceInputList.add(node1light);
		PioInputArgument node1presence = new PioInputArgument("attr3", (int)0, (int)0);
		instance.room2presenceInputList.add(node1presence);
		PioInputArgument node1switch = new PioInputArgument("attr4", (int)0, (int)0);
		instance.room2presenceInputList.add(node1switch);
		PioInputArgument node2temp = new PioInputArgument("attr5", (int)20, (int)28);
		instance.room2presenceInputList.add(node2temp);
		PioInputArgument node2light = new PioInputArgument("attr6", (int)800, (int)1000);
		instance.room2presenceInputList.add(node2light);
		PioInputArgument node2presence = new PioInputArgument("attr7", (int)1, (int)1);
		instance.room2presenceInputList.add(node2presence);
		PioInputArgument node2switch = new PioInputArgument("attr8", (int)1, (int)1);
		instance.room2presenceInputList.add(node2switch);
		PioInputArgument node3temp = new PioInputArgument("attr9", (int)20, (int)28);
		instance.room2presenceInputList.add(node3temp);
		PioInputArgument node3light = new PioInputArgument("attr10", (int)0, (int)1000);
		instance.room2presenceInputList.add(node3light);
		PioInputArgument node3presence = new PioInputArgument("attr11", (int)0, (int)0);
		instance.room2presenceInputList.add(node3presence);
		PioInputArgument node3switch = new PioInputArgument("attr12", (int)0, (int)0);
		instance.room2presenceInputList.add(node3switch);
		PioInputArgument plan = new PioInputArgument("plan", (int)3, (int)3);
		instance.room2presenceInputList.add(plan);
	}
	
	private static void initRoom3presenceInputList() {
		PioInputArgument node1temp = new PioInputArgument("attr1", (int)20, (int)28);
		instance.room3presenceInputList.add(node1temp);
		PioInputArgument node1light = new PioInputArgument("attr2", (int)0, (int)1000);
		instance.room3presenceInputList.add(node1light);
		PioInputArgument node1presence = new PioInputArgument("attr3", (int)0, (int)0);
		instance.room3presenceInputList.add(node1presence);
		PioInputArgument node1switch = new PioInputArgument("attr4", (int)0, (int)0);
		instance.room3presenceInputList.add(node1switch);
		PioInputArgument node2temp = new PioInputArgument("attr5", (int)20, (int)28);
		instance.room3presenceInputList.add(node2temp);
		PioInputArgument node2light = new PioInputArgument("attr6", (int)0, (int)1000);
		instance.room3presenceInputList.add(node2light);
		PioInputArgument node2presence = new PioInputArgument("attr7", (int)0, (int)0);
		instance.room3presenceInputList.add(node2presence);
		PioInputArgument node2switch = new PioInputArgument("attr8", (int)0, (int)0);
		instance.room3presenceInputList.add(node2switch);
		PioInputArgument node3temp = new PioInputArgument("attr9", (int)20, (int)28);
		instance.room3presenceInputList.add(node3temp);
		PioInputArgument node3light = new PioInputArgument("attr10", (int)800, (int)1000);
		instance.room3presenceInputList.add(node3light);
		PioInputArgument node3presence = new PioInputArgument("attr11", (int)1, (int)1);
		instance.room3presenceInputList.add(node3presence);
		PioInputArgument node3switch = new PioInputArgument("attr12", (int)1, (int)1);
		instance.room3presenceInputList.add(node3switch);
		PioInputArgument plan = new PioInputArgument("plan", (int)4, (int)4);
		instance.room3presenceInputList.add(plan);
	}
	
}
