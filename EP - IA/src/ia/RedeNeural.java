package ia;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class RedeNeural {
	
	static enum TipoNeuronio {I, H, O};
	private static double taxaAprendizado;
	public static int inputNeuronio;
	public static int hiddenNeuronio;
	private static double[] biasHidden;
	private static double[] biasOutput;
	private static Neuronio[] neuronios;
	public int epocas;
	public static double[] saidasEsperadas;
	public static ArrayList<double[]> entradas;
	

	public void inicializaVariaveis() throws IOException {
		Leitor leitor = new Leitor();

		taxaAprendizado = getRandomNoIntervalo(0.1, 1.0);
		epocas = 1000;
		saidasEsperadas = leitor.leSaidaEsperada();
		entradas = leitor.leEntrada();
	}
	
	public void inicializaNeuronios(int qtdInput, int qtdHidden, int qtdOutuput) {
		neuronios = new Neuronio[qtdInput + qtdHidden + qtdOutuput];
		biasHidden = new double[qtdHidden];
		biasOutput = new double[qtdOutuput];

		inputNeuronio = qtdInput;
		hiddenNeuronio = qtdHidden;
		
		for(int i=0; i < (qtdInput + qtdHidden + qtdOutuput); i++) {
			if (i < qtdInput) {
				neuronios[i] = new Neuronio(TipoNeuronio.I,0);
			} else if (i < (qtdInput + qtdHidden)) {
				neuronios[i] = new Neuronio(TipoNeuronio.H, qtdInput);
			} else {
				neuronios[i] = new Neuronio(TipoNeuronio.O, qtdHidden);
			}
		}

		for (int i = 0; i < biasHidden.length; i++) {
			biasHidden[i] = getRandomNoIntervalo(0.0, 1.0);
		}

		for (int i = 0; i < biasOutput.length; i++) {
			biasOutput[i] = getRandomNoIntervalo(0.0, 1.0);
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
		
		for (int i=0; i<neuronios.length; i++) {
			switch(neuronios[i].getTiponeuronio()) {
				case I:
					neuronios[i].setOutput(input[i]);
					break;
				case H:
					somaPesos = biasHidden[i - inputNeuronio];

					for (int j = 0; j < inputNeuronio; j++) {
						somaPesos += neuronios[i].getPeso()[j] * neuronios[j].getOutput();
					}

					neuronios[i].aplicaFuncaoAtivacao(somaPesos);
					break;
				case O:
					somaPesos = biasOutput[i - (inputNeuronio + hiddenNeuronio)];

					for (int j = inputNeuronio; j < (inputNeuronio + hiddenNeuronio); j++) {
						somaPesos += neuronios[i].getPeso()[j - inputNeuronio] * neuronios[j].getOutput();
					}

					neuronios[i].aplicaFuncaoAtivacao(somaPesos);
					break;
			}
		}
		
		return this;
	}
	
	public RedeNeural backpropError (double valEsperado) {
		// Passo 6 - backpropagation camada de saída.
		for (int i = neuronios.length -1; i >= inputNeuronio + hiddenNeuronio; i--) {
			neuronios[i].setError((valEsperado - neuronios[i].getOutput()) * neuronios[i].calculaDerivada());
			neuronios[i].setEntrada(neuronios[i].getEntrada() + taxaAprendizado * neuronios[i].getError());

			for (int j = 0; j < hiddenNeuronio; j++) {
				neuronios[i].getPeso()[j] = neuronios[i].getPeso()[j] + taxaAprendizado * neuronios[i].getError() * neuronios[j + hiddenNeuronio].getOutput();
			}

			biasOutput[i - (inputNeuronio + hiddenNeuronio)] = biasOutput[i - (inputNeuronio + hiddenNeuronio)] + (taxaAprendizado * neuronios[i].getError());
		}

		// Passo 7 - backpropagation camada escondida.
		for (int i = inputNeuronio; i < inputNeuronio + hiddenNeuronio; i++) {
			double somaErro = 0;

			for (int j = neuronios.length -1; j >= inputNeuronio + hiddenNeuronio; j--) {
				somaErro += neuronios[j].getPeso()[i - inputNeuronio] * neuronios[j].getError();
			}

			neuronios[i].setError(somaErro * neuronios[i].calculaDerivada());
			neuronios[i].setEntrada(neuronios[i].getEntrada() + taxaAprendizado * neuronios[i].getError());

			for (int j = 0; j < inputNeuronio; j++) {
				neuronios[i].getPeso()[j] = neuronios[i].getPeso()[j] + taxaAprendizado * neuronios[i].getError() * neuronios[j].getOutput();
			}

			biasHidden[i - inputNeuronio] = biasHidden[i - inputNeuronio] + (taxaAprendizado * neuronios[i].getError());
		}

		return this;
	}
}
