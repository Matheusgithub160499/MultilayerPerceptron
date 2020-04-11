package ia;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class RedeNeural {
	
	static enum TipoNeuronio {I, H, O};
	private static double taxaAprendizado;
	public static int inputNeuronio;
	public static int hiddenNeuronio;
	private static double[] bias = new double[2];
	private static Neuronio[] neuronios;
	public int epocas;
	public static double[] saidasEsperadas;
	public static ArrayList<double[]> entradas;
	

	public void inicializaVariaveis() throws IOException {
		Leitor leitor = new Leitor();
		
		bias[0] = getRandomNoIntervalo(0.0, 1.0);
		bias[1] = getRandomNoIntervalo(0.0, 1.0);
		taxaAprendizado = getRandomNoIntervalo(0.1, 1.0);
		epocas = 1000;
		saidasEsperadas = leitor.leSaidaEsperada();
		entradas = leitor.leEntrada();
	}
	
	public void inicializaNeuronios(int qtdInput, int qtdHidden, int qtdOutuput) {
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
	
	public RedeNeural forwardprop (double input[]) {
		double somaPesos = 0;
		//double somaEntrada = 0;
		
		
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
		neuronios[4].getPeso()[0] = neuronios[4].getPeso()[0] + taxaAprendizado * neuronios[4].getError() * neuronios[2].getOutput();
		neuronios[4].getPeso()[1] = neuronios[4].getPeso()[1] + taxaAprendizado * neuronios[4].getError() * neuronios[3].getOutput();
		
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
}
