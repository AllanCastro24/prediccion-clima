
package proyecto.pkgfinal;
package com.heatonresearch.book.introneuralnet.ch9.predict;

import com.heatonresearch.book.introneuralnet.ch9.predict.SinWave;
import java.text.NumberFormat;
import com.heatonresearch.book.introneuralnet.neural.activation.ActivationFunction;
import com.heatonresearch.book.introneuralnet.neural.activation.ActivationTANH;
import com.heatonresearch.book.introneuralnet.neural.feedforward.FeedforwardLayer;
import com.heatonresearch.book.introneuralnet.neural.feedforward.FeedforwardNetwork;
import com.heatonresearch.book.introneuralnet.neural.feedforward.train.Train;
import com.heatonresearch.book.introneuralnet.neural.feedforward.train.anneal.NeuralSimulatedAnnealing;
import com.heatonresearch.book.introneuralnet.neural.feedforward.train.backpropagation.Backpropagation;
import com.heatonresearch.book.introneuralnet.neural.util.ErrorCalculation;

public class ProyectoFinal {
    //Variables de configuración del programa
    public final static int ACTUAL_SIZE = 500;
    public final static int TRAINING_SIZE = 250;
    public final static int INPUT_SIZE = 5;
    public final static int OUTPUT_SIZE = 1;
    public final static int NEURONS_HIDDEN_1 = 7;
    public final static int NEURONS_HIDDEN_2 = 0;
    public final static boolean USE_BACKPROP = true;
    public static int cantidad_datos_salida = 2; //Cantidad de predicciones que necesitamos
    public static double valor_nuevo = 0;
    public static int contador_5_mandar = 0;
    public static int ACTUAL_SIZE_OG = ACTUAL_SIZE;
    public static int solo_calcular_1_vez = 0;
    
    public static void main(String[] args) {
        //El programa inicia en el archivo sinwave
        //final ProyectoFinal wave = new ProyectoFinal();
        //wave.run();
    }
   
    public void run() {
		generateActual(); //Generamos el valor actual
		createNetwork(); //Creamos la red neuronal
		generateTrainingSets(); //Generamos los datos de entrenamiento
		
                trainNetworkBackprop(); //Entrenamos con backpropagation
		
		display();

    }

    private void generateActual() {
        System.out.println("Holis estoy en generateActual");
    }

    private void createNetwork() {
        System.out.println("Holis estoy en CreateNetwork");
    }

    private void generateTrainingSets() {
        System.out.println("Holis estoy en generateTrainigSets");
    }

    private void trainNetworkBackprop() {
        System.out.println("Holis estoy en TrainNetworkBackprop");
    }

    private void display() {
        System.out.println("Holis estoy en Display");
    }
}