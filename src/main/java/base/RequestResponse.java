package base;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RequestResponse {

	public String requestHit(String body) {
		RequestSpecification resquestSpec = RestAssured.given();
		Response response = resquestSpec
				.contentType(ContentType.JSON)
				.baseUri("https://reqres.in/api/users")
				.body(body).post();
		String responseString = response.asString();
		return responseString;
	}
	
	public boolean responseProcessing(XSSFWorkbook workbook, String actualResponse, String expectedResponse) {
		XSSFSheet sheet = workbook.getSheet("ResponseParameter");
		int lastRowNum = sheet.getLastRowNum();
		for(int i = 0; i < lastRowNum;i++) {
			
		}
		return true;
	}
}
