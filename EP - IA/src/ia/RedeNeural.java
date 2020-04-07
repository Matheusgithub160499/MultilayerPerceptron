package ia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.IntStream;

public class RedeNeural {
	
	static enum TipoNeuronio {I, H, O};
	private static double taxaAprendizado;
	private static int inputNeuronio;
	private static int hiddenNeuronio;
	private static double[] bias = new double[2];
	private static Neuronio[] neuronios;
	public static int epocas;
	public static double[] saidasEsperadas = {0, 0, 0, 1};
	public static double[][] entradas = {{-1, -1},
										{-1, 1},
										{1, -1},
										{1, 1}
	};
	
	/*
	public RedeNeural() {
		IntStream.range(0, INPUT_NEURONIO).forEach(i -> neuronios[i] = new Neuronio(TipoNeuronio.I));
		IntStream.range(INPUT_NEURONIO, INPUT_NEURONIO + HIDDEN_NEURONIO).forEach(i -> neuronios[i] = new Neuronio(TipoNeuronio.H));
		neuronios[INPUT_NEURONIO + HIDDEN_NEURONIO] = new Neuronio(TipoNeuronio.O);
	}
	*/
	private static void inicializaVariaveis() {
		bias[0] = getRandomNoIntervalo(0.0, 1.0);
		bias[1] = getRandomNoIntervalo(0.0, 1.0);
		taxaAprendizado = getRandomNoIntervalo(0.1, 1.0);
		epocas = 1000;
	}
	
	private static void inicializaNeuronios(int qtdInput, int qtdHidden, int qtdOutuput) {
		neuronios = new Neuronio[qtdInput + qtdHidden + qtdOutuput];
		inputNeuronio = qtdInput;
		hiddenNeuronio = qtdHidden;
		
		for(int i=0; i < (qtdInput + qtdHidden + qtdOutuput); i++) {
			if (i < qtdInput) {
				neuronios[i] = new Neuronio(TipoNeuronio.I,0);
			} else if (i < (qtdInput + qtdHidden)) {
				neuronios[i] = new Neuronio(TipoNeuronio.H, qtdHidden);
			} else {
				neuronios[i] = new Neuronio(TipoNeuronio.O, qtdHidden);
			}
		}
		
	}
	
	public Neuronio[] getNeuronios() {
		return neuronios;
	}
	
	@Override
	public String toString() {
		return Arrays.toString(neuronios);
	}
	
	private static double getRandomNoIntervalo(double min, double max){
		   double x = (Math.random()*((max-min)+1))+min;
		   return x;
	}
	
	public static RedeNeural forwardprop (double input[]) {
		double somaPesos = 0;
		
		for (int i=0; i<neuronios.length; i++) {
			
			switch(neuronios[i].getTiponeuronio()) {
				case I:
					neuronios[i].setOutput(input[i]);
					break;
				case H:
					somaPesos = neuronios[i].getEntrada() +
								neuronios[i].getPeso()[0] * neuronios[0].getOutput() +
								neuronios[i].getPeso()[1] * neuronios[1].getOutput();
					neuronios[i].aplicaFuncaoAtivacao(somaPesos);
					break;
				case O:
					somaPesos = neuronios[i].getEntrada() +
								neuronios[i].getPeso()[0] * neuronios[2].getOutput() +
								neuronios[i].getPeso()[1] * neuronios[3].getOutput();
					neuronios[i].aplicaFuncaoAtivacao(somaPesos);
					break;
			}
		}
		
		return this;
	}
	
	public RedeNeural backpropError (double valEsperado) {
		neuronios[4].setError((valEsperado - neuronios[4].getOutput()) * neuronios[4].calculaDerivada());
		neuronios[4].setEntrada(neuronios[4].getEntrada() + taxaAprendizado * neuronios[4].getError());
		neuronios[4].getPeso()[0] = neuronios[4].getPeso()[0] + taxaAprendizado * neuronios[4].getError() * neuronios[0].getOutput();
		neuronios[4].getPeso()[1] = neuronios[4].getPeso()[1] + taxaAprendizado * neuronios[4].getError() * neuronios[1].getOutput();
		
		neuronios[3].setError(neuronios[4].getPeso()[1] * neuronios[4].getError() * neuronios[3].calculaDerivada());
		neuronios[3].setEntrada(neuronios[3].getEntrada() + taxaAprendizado * neuronios[3].getError());
		neuronios[3].getPeso()[0] = neuronios[3].getPeso()[0] + taxaAprendizado * neuronios[3].getError() * neuronios[0].getOutput();
		neuronios[3].getPeso()[1] = neuronios[3].getPeso()[1] + taxaAprendizado * neuronios[3].getError() * neuronios[1].getOutput();
		
		neuronios[2].setError(neuronios[4].getPeso()[0] * neuronios[4].getError() * neuronios[2].calculaDerivada());
		neuronios[2].setEntrada(neuronios[2].getEntrada() + taxaAprendizado * neuronios[2].getError());
		neuronios[2].getPeso()[0] = neuronios[2].getPeso()[0] + taxaAprendizado * neuronios[2].getError() * neuronios[0].getOutput();
		neuronios[2].getPeso()[1] = neuronios[2].getPeso()[1] + taxaAprendizado * neuronios[2].getError() * neuronios[1].getOutput();
		return this;
	}
	
	public static void printaResultado(double resultado[]) {
		System.out.println("  Input 1    |    Input 2    |    Esperado    |    Resultado  ");
		System.out.println("--------------------------------------------------------------");
		for(int i=0; i < entradas.length; i++) {
			for(int j=0; j < entradas[0].length; j++) {
				System.out.print("     " + entradas[i][j] + "     |   ");
				System.out.print("  " + saidasEsperadas[i] + "       |  " + String.format("%.5f", resultado[i]) + "  \n");
			}
		}
		/*
		IntStream.range(0, DADOS_TREINO.length).forEach(i -> {
			IntStream.range(0, DADOS_TREINO[0][0].length).forEach(j -> System.out.print("     " + DADOS_TREINO[i][0][j] + "     |   "));
			System.out.print("  " + DADOS_TREINO[i][1][0] + "       |  " + String.format("%.5f", resultado[i]) + "  \n");
		});
		*/
															
	}
	
	public static void main(String[] args) {

		inicializaVariaveis();
		inicializaNeuronios(entradas[0].length, entradas[0].length, 1);
		
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
						resultado[i] = forwardprop(entradas[i])
									   .getNeuronios()[inputNeuronio + hiddenNeuronio]
									   .getOutput();
					};
					printaResultado(resultado);
					break;
					
				case "treinar":
					for(int i=0; i < epocas; i++) {
						System.out.println("[Epoca " +i+"]");
						System.out.println("[Tipo Neuronio, peso 1, peso 2, entrada, output]");
						for(int j=0; j < epocas; j++) {
							System.out.println(forwardprop(entradas[j]).backpropError(saidasEsperadas[j]));
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
