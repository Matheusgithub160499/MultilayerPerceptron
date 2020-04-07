package ia;

public class Neuronio {
	
	private RedeNeural.TipoNeuronio tipoNeuronio;
	private double entrada;
	private double peso[];
	private double output;
	private double error;
	
	//Construtor da Classe - Atribui o tipo do neuronio (Entrada, Escondido ou Saida)
	public Neuronio(RedeNeural.TipoNeuronio tiponeuronio, int qtdPesos) {
		this.tipoNeuronio = tiponeuronio;
		this.inicializaPesos(qtdPesos);
	}
	
	//Getters e Setters para os atributos do neuronio
	public RedeNeural.TipoNeuronio getTiponeuronio() {
		return tipoNeuronio;
	}

	public double getEntrada() {
		return entrada;
	}

	public void setEntrada(double entrada) {
		this.entrada = entrada;
	}

	public double[] getPeso() {
		return peso;
	}

	public double getOutput() {
		return output;
	}

	public void setOutput(double output) {
		this.output = output;
	}

	public double getError() {
		return error;
	}

	public void setError(double error) {
		this.error = error;
	}
	
	private void inicializaPesos(int qtdPesos) {
		
		for(int i=0; i < qtdPesos; i++) {
			this.peso[i] = getRandomRange(0.1, 1); 			
		}
	}
	
	public double getRandomRange(double min, double max) {
		
		double x = (Math.random()*((max-min)+1))+min;
		return x;
		
	}
	
	public String toString() {
		String retornaValor = "";
		if(tipoNeuronio == RedeNeural.TipoNeuronio.I) retornaValor = "(" + tipoNeuronio + ": " + String.format("%.2f", output) + ")";
		else retornaValor = "(" + tipoNeuronio + ", " + String.format("%.2f", peso[0]) + ", " + String.format("%.2f", peso[1]) +
							", " + String.format("%.2f", entrada) + ", " + String.format("%.5f", output) + ")";
		
		return retornaValor;
	}
	
	//Aplica a funcao de ativacao - Sigmoide
	public void aplicaFuncaoAtivacao(double somaPesos) {
		output = 1 / (1 + Math.exp(-1 * somaPesos));
	}
	
	//Calcula a derivada da funcao de ativacao
	public double calculaDerivada() {
		return output * (1 - output);
	}
	
}
