import io.prediction.EngineClient;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;

public class PredictionConnector {
	private EngineClient engineClient;
	
	public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
		PredictionConnector predictionConnector = new PredictionConnector("http://192.168.1.19:8000");
		
		
		JsonObject result = predictionConnector.doPredictionQuery(Arrays.asList(23, 158, 0, 0, 21, 860, 1, 1, 26, 361, 0, 0));
		Logger.getLogger("TestLogger").info("result1: " + result.toString());
		predictionConnector = new PredictionConnector("http://192.168.1.19:8000");
		
		result = predictionConnector.doPredictionQuery(Arrays.asList(23, 158, 0, 0, 21, 860, 1, 1, 26, 361, 0, 0));
		Logger.getRootLogger().info("result2: " + result.toString());
		
//		EngineClient engineClient = new EngineClient("http://192.168.110.128:8000");
//
//		JsonObject response = engineClient.sendQuery(ImmutableMap
//				.<String, Object> of("features", ImmutableList.of(23, 158, 0, 0, 21, 860, 1, 1, 26, 361, 0, 0)));
//		
//		PredictionConnector predictionConnector = new PredictionConnector();
//		System.out.println(predictionConnector.doPredictionQuery("http://192.168.110.128:8000", new Integer[] {23, 158, 0, 0, 21, 860, 1, 1, 26, 361, 0, 0}));
//		System.out.println(predictionConnector.doPredictionQuery("http://192.168.110.128:8000", Arrays.asList(23, 158, 0, 0, 21, 860, 1, 1, 26, 361, 0, 0)));
	}
	
	public PredictionConnector (String uri) {
		if (engineClient==null) {
			engineClient = new EngineClient(uri, 1000, 1000, 60000);
		}
	}
	
	public JsonObject doPredictionQuery(List<Integer> arguments) throws ExecutionException, InterruptedException, IOException {
		
//		List<Object> list = new ArrayList<Object>();
//		list.add(23);list.add(158);list.add(0);list.add(0);list.add(21);list.add(860);list.add(1);list.add(1);list.add(26);list.add(361);list.add(0);list.add(0);
//		list.add(new Integer("23"));list.add(new Integer("158"));list.add(new Integer("0"));list.add(new Integer("0"));list.add(new Integer("21"));
//		list.add(new Integer("860"));list.add(new Integer("1"));list.add(new Integer("1"));list.add(new Integer("26"));list.add(new Integer("361"));
//		list.add(new Integer("0"));list.add(new Integer("0"));
		
		JsonObject response = engineClient.sendQuery(ImmutableMap
				.<String, Object> of("features", ImmutableList.copyOf(arguments)));
		
		return response;
	}
	
	public void createPredictionEvent() {
		/*EventClient client = new EventClient(<ACCESS KEY>, <URL OF EVENTSERVER>);

		// set the 4 properties for a user
		Event event = new Event()
		    .event("$set")
		    .entityType("user")
		    .entityId(<USER ID>)
		    .properties(ImmutableMap.<String, Object>of(
		        "attr0", <VALUE OF ATTR0>,
		        "attr1", <VALUE OF ATTR1>,
		        "attr2", <VALUE OF ATTR2>,
		        "plan", <VALUE OF PLAN>
		    ));
		client.createEvent(event);*/
	}
}
//curl -H "Content-Type: application/json" -d '{ "features": [23, 158, 0, 0, 21, 860, 1, 1, 26, 361, 0, 0] }' http://192.168.110.128:8000/queries.json