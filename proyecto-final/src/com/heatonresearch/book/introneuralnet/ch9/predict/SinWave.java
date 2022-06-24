/**
 * Introduction to Neural Networks with Java, 2nd Edition
 * Copyright 2008 by Heaton Research, Inc. 
 * http://www.heatonresearch.com/books/java-neural-2/
 * 
 * ISBN13: 978-1-60439-008-7  	 
 * ISBN:   1-60439-008-5
 *   
 * This class is released under the:
 * GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/copyleft/lesser.html
 */
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

/**
 * Chapter 9: Predictive Neural Networks
 * 
 * SinWave: Use a neural network to predict the sine wave.
 * 
 * @author Jeff Heaton
 * @version 2.1
 */
public class SinWave {
	public static int ACTUAL_SIZE = 525; //Tama�o del Dataset
	public static int cantidad_datos_salida = 2; //Cantidad de predicciones que debe de hacer
	public static double valor_nuevo = 0;
	public static int contador_5_mandar = 0;
	public static int ACTUAL_SIZE_OG = ACTUAL_SIZE;
	public static int solo_calcular_1_vez = 0;
	public final static int TRAINING_SIZE = 250;
	public final static int INPUT_SIZE = 5;
	public final static int OUTPUT_SIZE = 1;
	public final static int NEURONS_HIDDEN_1 = 7;
	public final static int NEURONS_HIDDEN_2 = 0;
	public final static boolean USE_BACKPROP = true;

	public static void main(final String args[]) {
		final SinWave wave = new SinWave();
		wave.run();
	}

	private ActualData actual;	
	private double input[][];
	private double ideal[][];

	public static int segundo_dataset;
	
	public static double calcular_5[] = new double[1000];
	private int contador_5 = 0;
	private int contador_500 = 0;
	
	
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

		//Comienza en el 5 por el WinWave.Input_size
		for (int i = SinWave.INPUT_SIZE; i < SinWave.ACTUAL_SIZE; i++) {

			
			this.actual.getInputData(i - SinWave.INPUT_SIZE, input);
			this.actual.getOutputData(i - SinWave.INPUT_SIZE, output);

			final StringBuilder str = new StringBuilder();

			for (int j = 0; j < output.length; j++) {
				if (j > 0) {
					str.append(',');
				}
				str.append(output[j]);
			}

			final double predict[] = this.network.computeOutputs(input);

			str.append(",");
			for (int j = 0; j < output.length; j++) {
				if (j > 0) {
					str.append(',');
				}
				str.append(predict[j]);
				
				if (contador_500 == ACTUAL_SIZE - INPUT_SIZE - 1) {//=================================================================================

					calcular_5[contador_5] = predict[0];
					
					SinWave.ACTUAL_SIZE = SinWave.ACTUAL_SIZE + 1;
					SinWave.valor_nuevo = calcular_5[contador_5];

					//Dato calculado
					contador_5++;
					SinWave.contador_5_mandar = contador_5;
					
					
					final SinWave wave = new SinWave();
					wave.run();
					
				}
				
				contador_500++;
				
			}

//			str.append(":Difference=");
			str.append(",");

			final ErrorCalculation error = new ErrorCalculation();
			error.updateError(predict, output);
			str.append(percentFormat.format(error.calculateRMS()));


			if (i > ACTUAL_SIZE_OG - 5) { //Cambiar a -3 para que solo muestre los �ltimos

//				System.out.println(str.toString()); MOSTRAR

			}
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
			
		}

	}

	private void generateActual() {
		this.actual = new ActualData(SinWave.ACTUAL_SIZE, SinWave.INPUT_SIZE,
				SinWave.OUTPUT_SIZE, SinWave.valor_nuevo, SinWave.contador_5_mandar, SinWave.ACTUAL_SIZE_OG, SinWave.cantidad_datos_salida);
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

		//Entrenar
		trainNetworkBackprop();
		
		display();
		
	}


	private void trainNetworkBackprop() {
		
		//Solo calcular 1 vez
		if (solo_calcular_1_vez == 0) {
		
		final Train train = new Backpropagation(this.network, this.input,
				this.ideal, 0.001, 0.1);

		int epoch = 1;

			//C�digo original
		if (segundo_dataset == 0) {
			System.out.println("Entrenar la primera red para predecir el valor minimo");
		}else {
			System.out.println("Entrenar la segunda red para predecir el valor maximo");
		}
			do {
				train.iteration();
				if (epoch % 100 == 0) {
					System.out.println("Iteration #" + epoch + " Error:"
					+ train.getError());				
				}
				epoch++;
			} while ((epoch <= 5000) && (train.getError() > 0.001));
			//C�digo original
			
			segundo_dataset++;
			System.out.println("=====================================================");
		}
	}
}
