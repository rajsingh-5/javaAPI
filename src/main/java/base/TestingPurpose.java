package base;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TestingPurpose {

	public static void main(String[] args) throws JsonMappingException, JsonProcessingException {
//		String body = "{\"email\": \"eve.holt@reqres.in\",\"password\": \"pistol\"}";
		String baseURI = "https://reqres.in/api/users?page=2";
//		String basePath = "api/register";
		RequestSpecification given = RestAssured.given();
		Response response = given.
				contentType(ContentType.JSON).
				baseUri(baseURI).
				get();
		System.out.println(response.asPrettyString());
//		JsonObject jsonObject = Json.createReader(new StringReader(response.asString())).readObject();
//
//        JsonObjectBuilder builder = Json.createObjectBuilder(jsonObject);
//        builder.add("data[0].id", "");
//
//        JsonObject updatedJsonObject = builder.build();
//        System.out.println(updatedJsonObject.toString());
		ObjectMapper objMap = new ObjectMapper();
		JsonNode json = objMap.readTree(response.asString());

		
	}
}
