package ia;

import java.io.*;
import java.util.stream.IntStream;

public class Driver {
	
	static int NUM_EPOCAS = 1000;
	static double DADOS_TREINO[][][] = new double[][][] {{{-1,-1},{0}},
														{{-1,1},{0}},
														{{1,-1},{0}},
														{{1,1},{1}}};
														
	
	public static void printaResultado(double resultado[]) {
		System.out.println("  Input 1    |    Input 2    |    Esperado    |    Resultado  ");
		System.out.println("--------------------------------------------------------------");
		IntStream.range(0, DADOS_TREINO.length).forEach(i -> {
			IntStream.range(0, DADOS_TREINO[0][0].length).forEach(j -> System.out.print("     " + DADOS_TREINO[i][0][j] + "     |   "));
			System.out.print("  " + DADOS_TREINO[i][1][0] + "       |  " + String.format("%.5f", resultado[i]) + "  \n");
		});
															
	}		
	
	public static void main(String[] args) throws IOException {
		RedeNeural redeNeural = new RedeNeural();
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		boolean flag = true;

		while(flag) {
			System.out.println("rodar, treinar ou sair?");
			String comando = bf.readLine();
			switch(comando) {
				case "rodar":
					double[] resultado = new double[] {0,0,0,0};
					IntStream.range(0, DADOS_TREINO.length).forEach(i -> {
						resultado[i] = redeNeural.forwardprop(DADOS_TREINO[i][0])
												.getNeuronios()[RedeNeural.INPUT_NEURONIO + RedeNeural.HIDDEN_NEURONIO]
												.getOutput();
						
					});
					printaResultado(resultado);
					break;
				case "treinar":
					IntStream.range(0, NUM_EPOCAS).forEach(i -> {
						System.out.println("[Epoca " +i+"]");
						System.out.println("[Tipo Neuronio, peso 1, peso 2, entrada, output]");
						IntStream.range(0, DADOS_TREINO.length).forEach(j -> 
							System.out.println(redeNeural.forwardprop(Driver.DADOS_TREINO[j][0])
															.backpropError(Driver.DADOS_TREINO[j][1][0])));
					});
					System.out.println("[Acabou o treino!]");
					break;
				case "sair":
					flag = false;
					break;	
			}
				
		}
		System.exit(0);
	}
	
	

}
