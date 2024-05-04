package base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonMatch {

	public boolean jsonMatcher(String json1, String json2) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node1 = mapper.readTree(json1);
		JsonNode node2 = mapper.readTree(json2);
		boolean matches = node1.equals(node2);
		return matches;
	}
}
