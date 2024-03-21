package base;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RequestResponse {
	ExcelRead excelRead = new ExcelRead();

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
				newExceptedResponse = jsonValueChange(eResponse, jsonPath);
				newActualResponse = jsonValueChange(aResponse, jsonPath);
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

	public String jsonValueChange(DocumentContext response, String path) {
		response.set(path, "");
		return response.jsonString();
	}
}
