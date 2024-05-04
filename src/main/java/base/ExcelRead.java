package base;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import base.JsonMatch;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class ExcelRead {
	public XSSFWorkbook workbook;
	public FileInputStream fis;
	public XSSFSheet sheet;
	public int startIndex = 1;
	static ExtentTest test;
	static ExtentReports extent;
	boolean requestSame;
	boolean value;
	String newRequestBody;
	String actualResponse;
	String expectedResponse;

	public static void main(String[] args) throws IOException {
		ExcelRead excelRead = new ExcelRead();
		String fileName = "Book1";
		String dateTime = excelRead.currentDateTime();
		ExtentSparkReporter htmlReporter = new ExtentSparkReporter(
				fileName + "_" + dateTime.replace("-", "").replace(":", "") + ".html");
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		excelRead.readExcel(fileName + ".xlsx");
		extent.flush();
	}

	public void readExcel(String fileName) throws IOException {
		RequestResponse run = new RequestResponse();
		fis = new FileInputStream(fileName);
		workbook = new XSSFWorkbook(fis);
		sheet = workbook.getSheet("ResponseParameter");
		/* Reading URL From ResponseParameter Sheet */
		String url = getValue(sheet, 1, 2);
		/* Changing sheet to RequestResponse to get request and response body */
		sheet = workbook.getSheet("RequestResponse");
		int lastRowNum = sheet.getLastRowNum();
		for (int i = 1; i < lastRowNum + 1; i++) {
			test = extent.createTest("Test Data " + i);
			String requestBody = getValue(sheet, i, 0);
			String status = getValue(sheet, i, 6);
			if (status.toLowerCase().equals("postive") || status.toLowerCase().contains("po")) {
				newRequestBody = run.requestBody(workbook, requestBody);
				/* Calling request hit */
				actualResponse = run.requestHit(newRequestBody.trim(), url);
				/* Fetching value of exceptedResponse column from excel */
				expectedResponse = getValue(sheet, i, 1);
				/* Matching the jsonTree with all the parameter and setting value as null */
				value = run.responseProcessing(workbook, actualResponse, expectedResponse);
				requestSame = resultMatching(newRequestBody, requestBody);
			} else if (status.toLowerCase().equals("negative") || status.toLowerCase().contains("ne")) {
				newRequestBody = requestBody;
				/* Calling request hit */
				actualResponse = run.requestHit(newRequestBody.trim(), url);
				/* Fetching value of exceptedResponse column from excel */
				expectedResponse = getValue(sheet, i, 1);
				/* Matching the jsonTree with all the parameter and setting value as null */
				value = resultMatching(actualResponse, expectedResponse);
				requestSame = resultMatching(newRequestBody, requestBody);
			}
			if (!requestSame) {
				test.pass(MarkupHelper.createCodeBlock(" Old Request \n" + requestBody,
						" New request \n " + newRequestBody));
				setValue(sheet, i, 2, newRequestBody);
			}
			setValue(sheet, i, 3, actualResponse);
			String pass;
			if (value) {
				test.pass(MarkupHelper.createCodeBlock("Expected_Response \n" + expectedResponse,
						" Actual_Response \n" + actualResponse));
				pass = "Pass";
			} else {
				test.fail(" Expected_Response " + expectedResponse + "\n Actual_Response " + actualResponse);
				pass = "Fail";
			}
			setValue(sheet, i, 7, pass);
		}
		FileOutputStream fos = new FileOutputStream("Book1.xlsx");
		workbook.write(fos);
		fos.close();
	}

	public String getValue(XSSFSheet sheet, int row, int cell) {
		XSSFRow row2 = sheet.getRow(row);
		if (row2 != null) {
			XSSFCell cell2 = row2.getCell(cell);
			if (cell2 != null) {
				return cell2.getStringCellValue();
			}
		}
		return "";
	}

	public void setValue(XSSFSheet sheet, int row, int cell, String value) {
		Row row2 = sheet.getRow(row);
		Cell cell2 = row2.createCell(cell);
		cell2.setCellValue(value);
	}

	public boolean resultMatching(String json1, String json2) throws JsonMappingException, JsonProcessingException {
		JsonMatch match = new JsonMatch();
		boolean value = match.jsonMatcher(json1, json2);
		return value;
	}

	public String currentDateTime() {
		LocalDateTime currentDateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String dateTime = currentDateTime.format(formatter);
		return dateTime;
	}

}
