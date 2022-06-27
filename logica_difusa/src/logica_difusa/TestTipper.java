package logica_difusa;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.Gpr;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;
import java.util.Scanner;
/**
 * Test parsing an FCL file
 * @author pcingola@users.sourceforge.net
 */
public class TestTipper {

	public static void main(String[] args) throws Exception {
		
            System.out.println("=========================================================================");
	    System.out.println("||||| Introduzca las temperaturas esperadas para obtener un consejo |||||");
	    System.out.println("=========================================================================");

	    double temp_min = -100;
	    double temp_max = -100;
		

	    while (temp_min <= -16 || temp_min > 30.9 || temp_max < 9.1 || temp_max > 50.9) {
	    
	    Scanner myObj = new Scanner(System.in);
	    
	    System.out.println("Temperatura minima: ");
	    String input_texto = myObj.nextLine(); 
	    temp_min = Double.parseDouble(input_texto); 
	    
	    System.out.println("Temperatura maxima: ");
	    input_texto = myObj.nextLine(); 
	    temp_max = Double.parseDouble(input_texto); 
		
	    	
		    if (temp_min <= -16 || temp_min > 30.9 || temp_max < 9.1 || temp_max > 50.9) {

		    	System.out.println("===========================================");
		    	System.out.println("===========================================");
		    	System.out.println("-------------------------------------------");
				
		    	System.out.println("Ingrese valores entre los rangos permitidos");
				//System.exit(0);
		    }
		
	    }
	    
	    
                // Cargar archivo clima.fcl (Matlab)
 		String fileName = "clima.fcl";
 		FIS fis = FIS.load(fileName, true);
 		if (fis == null) { // Error while loading?
 			System.err.println("No se puede cargar archivo: '" + fileName + "'");
 			return;
 		}

 		// Mostrar reglas
 		FunctionBlock functionBlock = fis.getFunctionBlock(null);
 		JFuzzyChart.get().chart(functionBlock);
	    
		// Entradas
		functionBlock.setVariable("temp_minima", temp_min);
		functionBlock.setVariable("temp_maxima", temp_max);

		// Aqui hace la evaluación
		functionBlock.evaluate();

		// Mostrar la salida de la variable en una gráfica
		Variable consejo = functionBlock.getVariable("consejo");
		JFuzzyChart.get().chart(consejo, consejo.getDefuzzifier(), true);

		System.out.println("Consejo:" + functionBlock.getVariable("consejo").getValue());
		
		double consejo_resultado = functionBlock.getVariable("consejo").getValue();
		
		//IFs para consejos
		if (consejo_resultado <= 0) {
			System.out.println("Hara bastante frio, procura no salir al menos que sea necesario y abrigarte bien");
		} else if (consejo_resultado > 0 && consejo_resultado <= 5) {
			System.out.println("Hara mucho frio, procura abrigarte bien y si sales camina por el sol");
		} else if (consejo_resultado > 5 && consejo_resultado <= 10) {
			System.out.println("Hara frio considerable, procura mantenerte abrigado");
		} else if (consejo_resultado > 10 && consejo_resultado <= 15) {
			System.out.println("Hara frio, no olvides abrigarte un poco");
		} else if (consejo_resultado > 15 && consejo_resultado <= 20) {
			System.out.println("Estara fresco, si eres propenso al frio abrigate");
		} else if (consejo_resultado > 20 && consejo_resultado <= 25) {
			System.out.println("El clima esta agradable, sal y disfrutalo! :D");
		} else if (consejo_resultado > 25 && consejo_resultado <= 30) {
			System.out.println("El clima esta agradable pero se siente un poco de calor, no te pongas ropa innecesaria");
		} else if (consejo_resultado > 30 && consejo_resultado <= 35) {
			System.out.println("Hara calor, si puedes lleva ropa deportiva o similar que deje que el aire pase por tu cuerpo para enfriarte");
		} else if (consejo_resultado > 35 && consejo_resultado <= 39) {
			System.out.println("Hara bastante calor, procura llevar agua para no deshidratarte");
		} else if (consejo_resultado > 39 && consejo_resultado <= 42) {
			System.out.println("Hara muchisimo calor, no salgas al menos que sea necesario y camina por la sombra, ponte bloqueador");
		} else if (consejo_resultado > 42) {
			System.out.println("El calor esta insoportable, podria resultar dañino el salir a la calle, especialmente para los niños y adultos"
					+ " mayores ya que son mas propensos a la insolaci�n, ponte bloqueador");
		}
		
		
	}
}
