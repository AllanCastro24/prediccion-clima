package com.heatonresearch.book.introneuralnet.ch9.predict;

import java.text.NumberFormat;

import com.heatonresearch.book.introneuralnet.neural.activation.ActivationFunction;
import com.heatonresearch.book.introneuralnet.neural.activation.ActivationTANH;
import com.heatonresearch.book.introneuralnet.neural.feedforward.FeedforwardLayer;
import com.heatonresearch.book.introneuralnet.neural.feedforward.FeedforwardNetwork;
import com.heatonresearch.book.introneuralnet.neural.feedforward.train.Train;
import com.heatonresearch.book.introneuralnet.neural.feedforward.train.anneal.NeuralSimulatedAnnealing;
import com.heatonresearch.book.introneuralnet.neural.feedforward.train.backpropagation.Backpropagation;
import com.heatonresearch.book.introneuralnet.neural.util.ErrorCalculation;

public class SinWave {
	//Variables de configuración del programa
        public final static int ACTUAL_SIZE = 525; //Cantidad de datos
        public final static int TRAINING_SIZE = 250; //Cantidad de entrenamiento
        public final static int INPUT_SIZE = 5; //Cantidad de neuronas de entrada
        public final static int OUTPUT_SIZE = 1; //Cantidad de neuronas de salida
        public final static int NEURONS_HIDDEN_1 = 7; //Cantidad de neuronas ocultas
        public final static int NEURONS_HIDDEN_2 = 0;
        public final static boolean USE_BACKPROP = true; //Entrenar con backpropagation
        public static int cantidad_datos_salida = 2; //Cantidad de predicciones que necesitamos
        public static double valor_nuevo = 0; //el valor a precedir se guarda acá
        public static int contador_5_mandar = 0; 
        public static int ACTUAL_SIZE_OG = ACTUAL_SIZE; //Valor actual original
        public static int solo_calcular_1_vez = 0;
        int segundo_dataset = 0;

	public static void main(final String args[]) {
		final SinWave wave = new SinWave();
		wave.run();
	}

	private ActualData actual;
	private double input[][];

	private double ideal[][];

	private FeedforwardNetwork network;

	public void createNetwork() {
		final ActivationFunction threshold = new ActivationTANH();
		this.network = new FeedforwardNetwork();
		this.network.addLayer(new FeedforwardLayer(threshold, INPUT_SIZE));
		this.network.addLayer(new FeedforwardLayer(threshold,
				SinWave.NEURONS_HIDDEN_1));
		if (SinWave.NEURONS_HIDDEN_2 > 0) {
			this.network.addLayer(new FeedforwardLayer(threshold,
					SinWave.NEURONS_HIDDEN_2));
		}
		this.network.addLayer(new FeedforwardLayer(threshold, OUTPUT_SIZE));

		this.network.reset();
	}

	private void display() {
		final NumberFormat percentFormat = NumberFormat.getPercentInstance();
		percentFormat.setMinimumFractionDigits(2);
		final double input[] = new double[SinWave.INPUT_SIZE];
		final double output[] = new double[SinWave.OUTPUT_SIZE];

		for (int i = SinWave.INPUT_SIZE; i < SinWave.ACTUAL_SIZE; i++) {
			this.actual.getInputData(i - SinWave.INPUT_SIZE, input);
			this.actual.getOutputData(i - SinWave.INPUT_SIZE, output);

			final StringBuilder str = new StringBuilder();
			str.append(i);
			str.append(":Actual=");
			for (int j = 0; j < output.length; j++) {
				if (j > 0) {
					str.append(',');
				}
				str.append(output[j]);
			}

			final double predict[] = this.network.computeOutputs(input);

			str.append(":Predicted=");
			for (int j = 0; j < output.length; j++) {
				if (j > 0) {
					str.append(',');
				}
				str.append(predict[j]);
			}

			str.append(":Difference=");

			final ErrorCalculation error = new ErrorCalculation();
			error.updateError(predict, output);
			str.append(percentFormat.format(error.calculateRMS()));

			System.out.println(str.toString());
		}
	}

	public void displayTraining() {
		for (int i = 0; i < this.input.length; i++) {
			final StringBuilder str = new StringBuilder();
			for (int j = 0; j < this.input[0].length; j++) {
				if (j > 0) {
					str.append(',');
				}
				str.append(this.input[i][j]);
			}
			str.append("=>");
			for (int j = 0; j < this.ideal[0].length; j++) {
				if (j > 0) {
					str.append(',');
				}
				str.append(this.ideal[i][j]);
			}
			System.out.println(str.toString());
		}

	}

	private void generateActual() {
		this.actual = new ActualData(SinWave.ACTUAL_SIZE, SinWave.INPUT_SIZE, SinWave.OUTPUT_SIZE, SinWave.valor_nuevo, SinWave.contador_5_mandar, SinWave.ACTUAL_SIZE_OG, SinWave.cantidad_datos_salida);
	}

	private void generateTrainingSets() {
		this.input = new double[TRAINING_SIZE][INPUT_SIZE];
		this.ideal = new double[TRAINING_SIZE][OUTPUT_SIZE];

		for (int i = 0; i < TRAINING_SIZE; i++) {
			this.actual.getInputData(i, this.input[i]);
			this.actual.getOutputData(i, this.ideal[i]);
		}
	}

	public void run() {
		generateActual();
		createNetwork();
		generateTrainingSets();

                trainNetworkBackprop(); //Entrenamiento
		
		display();

	}

	private void trainNetworkBackprop() {
            if(solo_calcular_1_vez == 0){
                final Train train = new Backpropagation(this.network, this.input,
				this.ideal, 0.001, 0.1);

		int epoch = 1;
                

                if(segundo_dataset == 0){
                    System.out.println("Entrenar la primera red para predecir el valor minimo");
                }else{
                    System.out.println("Entrenar la segunda red para predecir el valor maximo");
                }
		do {
			train.iteration();
                        if(epoch % 100 == 0){
                            System.out.println("Iteración #" + epoch + " Error:"
                                + train.getError());
			}
                        epoch++;
		} while ((epoch < 50000) && (train.getError() > 0.00001));
                
                segundo_dataset++;
                System.out.println("---------------------------------------------------------------");
            }
	}
}
