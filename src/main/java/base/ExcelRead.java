package base;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class ExcelRead {
	public XSSFWorkbook workbook;
	public FileInputStream fis;
	public XSSFSheet sheet;
	public int startIndex = 1;

	public static void main(String[] args) throws IOException {
		ExcelRead excelRead = new ExcelRead();
		String fileName = "Book1.xlsx";
		excelRead.readExcel(fileName);
		
	}

	public void readExcel(String fileName) throws IOException {
		RequestResponse run = new RequestResponse();
		fis = new FileInputStream(fileName);
		workbook = new XSSFWorkbook(fis);
		sheet = workbook.getSheet("ResponseParameter");
		String url = getValue(sheet, 1,2);
		sheet = workbook.getSheet("ResquestResponse");
		
		int lastRowNum = sheet.getLastRowNum();
		for (int i = 1; i < lastRowNum + 1; i++) {
			String request = getValue(sheet, i, 0);
			/* Calling request hit */
			String actualResponse = run.requestHit(request.trim(), url);
			/* Fetching value of exceptedResponse column from excel */
			String expectedResponse = getValue(sheet, i, 1);
			/* Matching the jsonTree with all the parameter and setting value as null */
			boolean value = run.responseProcessing(workbook, actualResponse, expectedResponse);
			setValue(sheet, i, 2, actualResponse);
			String pass;
			if (value) {
				pass = "Pass";
			} else {
				pass = "Fail";
			}
			setValue(sheet, i, 3, pass);
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

	
	
}
