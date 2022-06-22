
package com.heatonresearch.book.introneuralnet.ch9.predict;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ActualData {
	public static double sinDEG(final double deg) {
		final double rad = deg * (Math.PI / 180);
		final double result = Math.sin(rad);
		return ((int) (result * 100000.0)) / 100000.0;
	}

	private final double actual[];
	private final int inputSize;
        private final double datos_clima[] ;
        //Double[] datos_clima = new Double[1000];
        
	private final int outputSize;
        private int segundo_dataset = 0;
	public ActualData(final int size, final int inputSize, final int outputSize, final double nuevo_valor, final int contador_5, final int size_original, final int cantidad_datos_salida) {
                //Codigo anterior NO MOVER
                this.actual = new double[size];
		this.inputSize = inputSize;
		this.outputSize = outputSize;
                String sample = ",";
                String mystring;
                
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
            
                for(int i = 0; i < size; i++){
                    this.actual[i] = datos_clima[i];
                    
                    if(i == size_original +1){
                        double valor_minimo_predicho = ((int) (actual[size_original]*10000.0))/100.0;
                        double valor_maximo_predicho = ((int) (actual[size_original]*10000.0))/100.0;
                        
                        System.out.println("Valor máximo: " + valor_maximo_predicho);
                        System.out.println("Valor minimo: " + valor_minimo_predicho);
                    }
                }
                
                //Salimos del actual data
                if (size > size_original + cantidad_datos_salida-1){
                    System.out.println("Salimos del actualData");
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
