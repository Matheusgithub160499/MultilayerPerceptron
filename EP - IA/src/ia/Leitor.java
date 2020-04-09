package ia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

public class Leitor {
	
	private static List<String[]> inputs;
	
	
	public Leitor() throws IOException {
		
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		Reader reader;
		System.out.println("AND, OR ou XOR?");
		String tipo = bf.readLine();
		
		switch(tipo) {
			case "OR": 
				reader = Files.newBufferedReader(Paths.get("problemOR.csv").toAbsolutePath());
				break;
			case "XOR": 
				reader = Files.newBufferedReader(Paths.get("problemXOR.csv").toAbsolutePath());
				break;
			default:
				reader = Files.newBufferedReader(Paths.get("problemAND.csv").toAbsolutePath());//AND como default
		}
		
	    CSVReader csvReader = new CSVReaderBuilder(reader).build();
	    
	    try {
	    	inputs = csvReader.readAll();
	    }
	    catch (CsvException e) {		
	    	 e.printStackTrace();
	    }
	}
	
	
	public static ArrayList<double[]> leEntrada(){
		
		inputs.get(0)[0] = inputs.get(0)[0].substring(1);
		ArrayList<double[]> entradas = new ArrayList<double[]>();

		for(int i=0; i<inputs.size(); i++) { 
			String[] adicionado = inputs.get(i);
			double[] temp = Arrays.stream(adicionado).mapToDouble(Double::parseDouble).toArray();//String[] to double[]
			temp = ArrayUtils.remove(temp, temp.length-1);//Remove o último elemento do array (seria a saida esperada)
			entradas.add(temp);
		}
		
		return entradas;
	}
	
	public static double[] leSaidaEsperada() {
		
		double[] saidasEsperadas = new double[inputs.size()];

		for(int i=0; i<inputs.size(); i++) {
			saidasEsperadas[i] = Double.valueOf(inputs.get(i)[inputs.get(i).length-1]);
		}
		
		for(int i=0; i<saidasEsperadas.length; i++) {
			System.out.println("Saidas: " + saidasEsperadas[i]);
		}
		
		return saidasEsperadas;
	}
	
}


