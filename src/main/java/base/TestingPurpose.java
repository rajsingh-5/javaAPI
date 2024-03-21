package base;

import java.io.FileNotFoundException;
import java.io.IOException;

public class TestingPurpose {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		String str = ""; // or str = null;
		if (str != null && !str.isEmpty()) {
			System.out.println("null");
		} else {
			System.out.println("not");
		} 
	}

}
