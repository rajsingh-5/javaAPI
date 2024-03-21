package base;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.github.javafaker.Faker;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RequestResponse {
	ExcelRead excelRead = new ExcelRead();
	Faker faker = new Faker();
	public String requestHit(String body, String url) {
		RequestSpecification resquestSpec = RestAssured.given();
		Response response = resquestSpec.contentType(ContentType.JSON).baseUri(url).body(body).post();
		String responseString = response.asString();
		return responseString;
	}

	public boolean responseProcessing(XSSFWorkbook workbook, String actualResponse, String expectedResponse)
			throws JsonMappingException, JsonProcessingException {
		XSSFSheet sheet = workbook.getSheet("ResponseParameter");
		int lastRowNum = sheet.getLastRowNum();
		String newExceptedResponse = "";
		String newActualResponse = "";
		DocumentContext aResponse = JsonPath.parse(actualResponse);
		DocumentContext eResponse = JsonPath.parse(expectedResponse);
		for (int i = 1; i < lastRowNum + 1; i++) {
			String jsonPath = excelRead.getValue(sheet, i, 0);
			if (jsonPath != null && !jsonPath.isEmpty()) {
				String dataType = excelRead.getValue(sheet, i, 1);
				newActualResponse = jsonValueChange(aResponse, jsonPath);
				newExceptedResponse = jsonValueChange(eResponse, jsonPath, dataType);
			}
		}
		if (newExceptedResponse != null && !newExceptedResponse.isEmpty()) {
			boolean matchResponse = excelRead.resultMatching(newExceptedResponse, newActualResponse);
			return matchResponse;
		} else {
			boolean matchResponse = excelRead.resultMatching(expectedResponse, actualResponse);
			return matchResponse;
		}
	}

	public String jsonValueChange(DocumentContext response, String path, String dataType) {
		String pathValue = response.read(path);
		boolean matchValue = pathValue.matches(dataType);
		if(matchValue){
//			ExcelRead.test.pass("Value on path " + path + " is same as regex " + matchValue);
			response.set(path, "");
			return response.jsonString();
		}
		else {
			ExcelRead.test.fail("Value of path <b>" + path + "</b> is not same as regex ");
//			response.set(path, "");
			return response.jsonString();
		}
	}

	public String jsonValueChange(DocumentContext response, String path) {
//		String pathValue = response.read(path);
//		boolean matchValue = pathValue.matches(dataType);
//		System.out.println("Value on path "+path+" is same as "+matchValue);
		response.set(path, "");
		return response.jsonString();
	}

	public String requestBody(XSSFWorkbook workbook, String requestBody) {
		XSSFSheet sheet = workbook.getSheet("RequestParameter");
		String newRequestBody=null;
		DocumentContext request = JsonPath.parse(requestBody);
		int lastRowNum = sheet.getLastRowNum();
		for (int i = 1; i < lastRowNum + 1; i++) {
			String jsonPath = excelRead.getValue(sheet, i, 0);
			if (jsonPath != null && !jsonPath.isEmpty()) {
				String dataType = excelRead.getValue(sheet, i, 1);
				newRequestBody = jsonValueRequest(request, jsonPath, dataType);
			}
		}
		if (newRequestBody != null && !newRequestBody.isEmpty()) {
			return newRequestBody;
		} else {
			return requestBody;
		}
	}
	
	public String jsonValueRequest(DocumentContext request, String path, String dataType) {
		String value = faker.regexify(dataType);
		request.set(path, value);
		return request.jsonString();
	}
}
