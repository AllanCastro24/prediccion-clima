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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Chapter 9: Predictive Neural Networks
 * 
 * ActualData: Holds values from the sine wave.
 * 
 * @author Jeff Heaton
 * @version 2.1
 */
public class ActualData {
	public static double sinDEG(final double deg) {
		final double rad = deg * (Math.PI / 180);
		final double result = Math.sin(rad);
		return ((int) (result * 100000.0)) / 100000.0;
	}

	private final double actual[];
	private final int inputSize;

	private final int outputSize;
        private int segundo_dataset;
	public ActualData(final int size, final int inputSize, final int outputSize, final double nuevo_valor, final int contador_5, final int size_original, final int cantidad_datos_salida) {
                String sample = ",";
                String mystring;
                Double[] datos_clima = null;
                try
                {
                    segundo_dataset++;
                    
                    BufferedReader temperatura; //Leer archivo
                    //Los archivos deben ir en la raiz del proyecto
                    if(segundo_dataset == 1){ //Tempeartura minima
                        temperatura = new BufferedReader(new FileReader("temp_min.csv"));
                    }
                    else{ //Temperatura maxima
                        temperatura = new BufferedReader(new FileReader("temp_max.csv"));
                    }
                    
                    int i = 0;
                    
                    while ((mystring = temperatura.readLine()) != null){ //Leemos linea por linea el archivo
                        String[] clima = mystring.split(sample); //Separamos por comas
                        datos_clima[i] = Double.parseDouble(clima[0])/100;
                        i++;
                    }
                    
                    if(size > size_original){
                        datos_clima[size-1] = nuevo_valor;
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            
                //Codigo anterior NO MOVER
                this.actual = new double[size];
		this.inputSize = inputSize;
		this.outputSize = outputSize;
	}

	public void getInputData(final int offset, final double target[]) {
		for (int i = 0; i < this.inputSize; i++) {
			target[i] = this.actual[offset + i];
		}
	}

	public void getOutputData(final int offset, final double target[]) {
		for (int i = 0; i < this.outputSize; i++) {
			target[i] = this.actual[offset + this.inputSize + i];
		}
	}
	

}
