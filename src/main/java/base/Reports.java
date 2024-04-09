package base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class Reports {


	public static void main(String[] args) {
		Reports.reportPrepare("rest.html");
	}
	public static void reportPrepare(String filePath) {
	    ExtentSparkReporter htmlReporter = new ExtentSparkReporter(filePath);
        ExtentReports extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        ExtentTest test = extent.createTest("MyTest", "Sample description");
        test.pass("Step details");
        extent.flush();
	}
}
