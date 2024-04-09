package utils;

import org.testng.ITestListener;
import org.testng.ITestResult;

public class ExtentListener implements ITestListener {

	public void onTestSuccess(ITestResult result) {
		System.out.println("Test Succsess"+result.getName());
		// TODO Auto-generated method stub
	}
	public void onTestStart(ITestResult result) {
		System.out.println("Test Started"+result.getName());
	}

}
