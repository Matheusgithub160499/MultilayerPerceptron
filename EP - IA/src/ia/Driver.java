package ia;

import java.io.*;

public class Driver {
	
	static int NUM_EPOCAS = 1000;
	public static double[] saidasEsperadas = {0, 0, 0, 1};
	public static double[][] entradas = {{-1, -1},
										{-1, 1},
										{1, -1},
										{1, 1}};
	
	public static void printaResultado(double resultado[]) {
		System.out.println("  Input 1    |    Input 2    |    Esperado    |    Resultado  ");
		System.out.println("--------------------------------------------------------------");
		for(int i=0; i < entradas.length; i++) {
			for(int j=0; j < entradas[0].length; j++) {
				System.out.print("     " + entradas[i][j] + "     |   ");
				
			}
			System.out.print("  " + saidasEsperadas[i] + "       |  " + String.format("%.5f", resultado[i]) + "  \n");
		}
	}
	
	public static void main(String[] args) throws IOException {
		RedeNeural redeNeural = new RedeNeural();
		
		redeNeural.inicializaVariaveis();
		redeNeural.inicializaNeuronios(entradas[0].length, entradas[0].length, 1);
		
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		boolean flag = true;

		while(flag) {
			System.out.println("rodar, treinar ou sair?");
			String comando;
			try {
				comando = bf.readLine();
				switch(comando) {
				
				case "rodar":
					double[] resultado = new double[] {0,0,0,0};
					
					for(int i=0; i < entradas.length; i++) {
						resultado[i] = redeNeural.forwardprop(entradas[i])
									   .getNeuronios()[RedeNeural.inputNeuronio + RedeNeural.hiddenNeuronio]
									   .getOutput();
					};
					printaResultado(resultado);
					break;
					
				case "treinar":
					for(int i=0; i < redeNeural.epocas; i++) {
						System.out.println("[Epoca " +i+"]");
						System.out.println("[Tipo Neuronio, peso 1, peso 2, entrada, output]");
						for(int j=0; j < entradas.length; j++) {
							System.out.println(redeNeural.forwardprop(entradas[j]).backpropError(saidasEsperadas[j]));
						}
					};
					System.out.println("[Acabou o treino!]");
					break;
					
				case "sair":
					flag = false;
					break;	
				}
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.exit(0);
	}
	
	

}
