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
	public static int cantidad_datos_salida = 2; //Cantidad de predicciones que debe de hacer (Temperatura maxima y minima)
	public static double valor_nuevo = 0; //Aqui se va a guardar el valor que se va a predecir
	public static int contador_5_mandar = 0; //Se mandan 5 temperaturas para predecir una nueva
	public static int ACTUAL_SIZE_OG = ACTUAL_SIZE;
	public static int solo_calcular_1_vez = 0;
	public final static int TRAINING_SIZE = 250; // Con cuantos datos quieres entrenar
	public final static int INPUT_SIZE = 5; //Neuronas de entrada
	public final static int OUTPUT_SIZE = 1; //Neuronas de salida
	public final static int NEURONS_HIDDEN_1 = 7; //Neuronas ocultas
	public final static int NEURONS_HIDDEN_2 = 0;
	public final static boolean USE_BACKPROP = true; //Se indica que debe entrenar con back propagation

	public static void main(final String args[]) {
            //Se crea el objeto wave y se inicia el programa (El programa inicia en el método run()
		final SinWave wave = new SinWave();
		wave.run();
	}

        //Objeto de tipo ActualData para usar las funciones que tenga el ActualData.java
	private ActualData actual;
        //Arreglo para guardar las temperaturas y el valor ideal de cada predicción
	private double input[][];
	private double ideal[][];
        //Variable entera para identificar cuando usar el dataset de temp_max y cuando el de temp_min
	public static int segundo_dataset;
	
	public static double calcular_5[] = new double[1000];
	private int contador_5 = 0;
	private int contador_500 = 0;
	
	//Se crea la red de tipo feedForward
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

			str.append(",");

			final ErrorCalculation error = new ErrorCalculation();
			error.updateError(predict, output);
			str.append(percentFormat.format(error.calculateRMS()));


			if (i > ACTUAL_SIZE_OG - 5) { //Cambiar a -3 para que solo muestre los ultimos


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
		//Generar el actual, crear la red y los datos de entrenamiento
		generateActual();
		createNetwork();
		generateTrainingSets();
		//Entrenar
		trainNetworkBackprop();
		//Mostrar datos en pantalla
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
				if (epoch % 100 == 0) { //Se pone %100 para que muestre de 100 en 100 las epocas
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
