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

//Mio
import java.io. * ;
import java.util.Scanner;

import java.io.FileReader;
import java.io.IOException;//signals an exception of some kind has occurred
import java.io.BufferedReader;//Reads text from a character-input stream
//Mio

/**
 * Chapter 9: Predictive Neural Networks
 * 
 * ActualData: Holds values from the sine wave.
 * 
 * @author Jeff Heaton
 * @version 2.1
 */


public class ActualData {
	
	
	public static double datos_clima[] = new double[10000];
	
	public static double sinDEG(final double deg) {
		
	    final double rad = deg * (Math.PI / 180);
		final double result = Math.sin(rad);
		return ((int) (result * 100000.0)) / 100000.0;
		
	}
	
	private final double actual[];
	private final int inputSize;

	private final int outputSize;
	
	public static int segundo_dataset;
	public static double valor_minimo_predicho;
	public static double valor_medio_predicho;
	public static double valor_maximo_predicho;
	

	public ActualData(final int size, final int inputSize, final int outputSize, 
			final double nuevo_valor, final int contador_5, final int size_original, final int cantidad_datos_salida){
		
		
		String sample = ",";
        String mystring;
        try
        {
        	segundo_dataset++;
        	
        	BufferedReader temperatura;
        	
        	if (segundo_dataset == 1) {
        		temperatura = new BufferedReader(new FileReader("_temp_min.csv"));
        	}else {
                        temperatura = new BufferedReader(new FileReader("_temp_max.csv"));  
        	}
        	
            int i = 0;
            
            while ((mystring = temperatura.readLine()) != null)  //Reads a line of text
            {
                String[] clima = mystring.split(sample);//utilized to split the string
                    datos_clima[i] = Double.parseDouble(clima[0])/100;
                i++;	
            }
            
            if (size > size_original) {
                datos_clima[size-1] = nuevo_valor;
            }
        }
        catch (IOException e)//catches exception in the try block
        {
            e.printStackTrace();//Prints this throwable and its backtrace
        }
		
		
		this.actual = new double[size];
		this.inputSize = inputSize;
		this.outputSize = outputSize;


		for (int i = 0; i < size; i++) {
			this.actual[i] = datos_clima[i];
			
			if (i == size_original +1) {

					valor_minimo_predicho = ((int) (actual[size_original]     * 10000.0)) / 100.0;
					valor_maximo_predicho = ((int) (actual[size_original + 1] * 10000.0)) / 100.0;
					
					System.out.println("Valor minimo predicho: - " + valor_minimo_predicho);
					System.out.println("Valor maximo predicho: - " + valor_maximo_predicho);					
			}
		}
		
		
		//Salir cuando se alcance los que se quieren calcular
		if (size > size_original + cantidad_datos_salida-1) {	
			System.out.println("Salir ActualData");
			System.exit(0);
		}
		
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
