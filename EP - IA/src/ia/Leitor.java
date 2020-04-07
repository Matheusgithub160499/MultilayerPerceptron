package ia;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Leitor {
	
	
	public void leitura() throws IOException {

		 Reader reader = Files.newBufferedReader(Paths.get("problemXOR.csv").toAbsolutePath());
	     CSVReader csvReader = new CSVReaderBuilder(reader).build();

	     List<String[]> inputs;
	     
	     try {
	    	inputs = csvReader.readAll();
	    	
			for (String[] input : inputs) {
				System.out.println("Input 1 : " + input[0] +
                            	" - Input 2 : " + input[1] +
                            	" - Result : " + input[2]);
			}
		            
	     } 
	     
	     catch (CsvException e) {		
	    	 e.printStackTrace();
	     }
	}
}
