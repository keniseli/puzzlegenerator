package puzzlegenerator_service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

public class SomeClass {

	@Test
	public void test() {
		File file = new File("C:/temp/file.txt");
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			String message = String.format(
					"An exception occurred while writing to the file [%s].",
					file.getAbsolutePath());
			throw new RuntimeException(message, e);
		}
	}
}
