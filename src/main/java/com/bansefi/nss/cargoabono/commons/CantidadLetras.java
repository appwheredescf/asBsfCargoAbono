package com.bansefi.nss.cargoabono.commons;

import java.text.DecimalFormat;
import java.util.regex.Pattern;
/**
 * @web http://jc-mouse.blogspot.com/
 * @author Mouse
 */
public class CantidadLetras {

    private static final String[] UNIDADES = {"", "un ", "dos ", "tres ", "cuatro ", "cinco ", "seis ", "siete ", "ocho ", "nueve "};
    private static final String[] DECENAS = {"diez ", "once ", "doce ", "trece ", "catorce ", "quince ", "dieciseis ",
        "diecisiete ", "dieciocho ", "diecinueve", "veinte ", "treinta ", "cuarenta ",
        "cincuenta ", "sesenta ", "setenta ", "ochenta ", "noventa "};
    private static final String[] CENTENAS = {"", "ciento ", "doscientos ", "trescientos ", "cuatrocientos ", "quinientos ", "seiscientos ",
        "setecientos ", "ochocientos ", "novecientos "};

   public CantidadLetras() {
   }

   public static String FormatoNumero(String StrImporte)
   {
	   String StrReturn="";
	   try
	   {
		   String Cad="";
		   String[] cantidades = StrImporte.split("\\.");
			StringBuilder sb = new StringBuilder(cantidades[0]).append(".");
			for (int i = 1, len = sb.length(); i < len; i++) {
			    if (i % 4 == 0) {
			        sb.insert(len = sb.length() - i, ',');
			        len = sb.length();
			    }
			}
			
			Cad =sb.toString();
			
			if(cantidades.length>1)
				 Cad =sb.toString()+cantidades[1];
			else
				 Cad =sb.toString()+"00";
			StrReturn=Cad;
	   }
	   catch(Exception ex)
	   {
		   StrReturn= StrImporte;
	   }
	   return "$ "+StrReturn;
   }
    public static String Convertir(String numero, boolean mayusculas) {
        String literal = "";
        String parte_decimal = "";
        numero = numero.replaceAll(",", "");
        numero = numero.replaceAll("$", "");
        numero = String.format ("%.2f", Double.parseDouble(numero));
        //si el numero utiliza (.) en lugar de (,) -> se reemplaza
        numero = numero.replace(".", ",");
        //si el numero no tiene parte decimal, se le agrega ,00
        if(numero.indexOf(",")==-1){
            numero = numero + ",00";
        }
        //se valida formato de entrada -> 0,00 y 999 999 999,00
        if (Pattern.matches("\\d{1,12},\\d{1,2}", numero)) {
            //se divide el numero 0000000,00 -> entero y decimal
            String Num[] = numero.split(",");            
            //de da formato al numero decimal
            parte_decimal = Num[1] + "/100 M.N.";
            //se convierte el numero a literal
            if (Long.parseLong(Num[0]) == 0) {//si el valor es cero
                literal = "cero ";
            } else if(Long.parseLong(Num[0]) > 999999999){
            	if(Num[0].substring(0, Num[0].length() -9).length() == 1){
            		literal =getUnidades(Num[0].substring(0, Num[0].length() -9)) + "MIL " + getMillones(Num[0].substring(Num[0].length() -9));
            	}else if(Num[0].substring(0, Num[0].length() -9).length() == 2){
            		literal =getDecenas(Num[0].substring(0, Num[0].length() -9)) + "MIL " + getMillones(Num[0].substring(Num[0].length() -9));
            	}else if(Num[0].substring(0, Num[0].length() -9).length() == 3){
            		literal =getCentenas(Num[0].substring(0, Num[0].length() -9)) + "MIL " + getMillones(Num[0].substring(Num[0].length() -9));
            	}
            } else if (Long.parseLong(Num[0]) > 999999) {//si es millon
                literal = getMillones(Num[0]);
            } else if (Long.parseLong(Num[0]) > 999) {//si es miles
                literal = getMiles(Num[0]);
            } else if (Long.parseLong(Num[0]) > 99) {//si es centena
                literal = getCentenas(Num[0]);
            } else if (Long.parseLong(Num[0]) > 9) {//si es decena
                literal = getDecenas(Num[0]);
            } else {//sino unidades -> 9
                literal = getUnidades(Num[0]);
            }
            //devuelve el resultado en mayusculas o minusculas
            if (mayusculas) {
                return (literal + (literal.trim().equals("un") ? "peso " : "pesos ") + parte_decimal).toUpperCase();
            } else {
                return (literal + (literal.trim().equals("un") ? "peso " : "pesos ") + parte_decimal);
            }
        } else {//error, no se puede convertir
            return literal = null;
        }
    }

    /* funciones para convertir los numeros a literales */

    private static String getUnidades(String numero) {// 1 - 9
        //si tuviera algun 0 antes se lo quita -> 09 = 9 o 009=9
        String num = numero.substring(numero.length() - 1);
        return UNIDADES[Integer.parseInt(num)];
    }

    private static String getDecenas(String num) {// 99                        
        int n = Integer.parseInt(num);
        if (n < 10) {//para casos como -> 01 - 09
            return getUnidades(num);
        } else if (n > 19) {//para 20...99
            String u = getUnidades(num);
            if (u.equals("")) { //para 20,30,40,50,60,70,80,90
                return DECENAS[Integer.parseInt(num.substring(0, 1)) + 8];
            } else {
            	if(num.equals("20"))
            		return DECENAS[Integer.parseInt(num.substring(0, 1)) + 8] + "y " + u;
            	else
            		if(Integer.parseInt(num)<30)
            			return "veinti" + "" + u;
            		else
            			return DECENAS[Integer.parseInt(num.substring(0, 1)) + 8] + "y " + u;
            			
            }
        } else {//numeros entre 11 y 19
            return DECENAS[n - 10];
        }
    }

    private static String getCentenas(String num) {// 999 o 099
        if( Integer.parseInt(num)>99 ){//es centena
            if (Integer.parseInt(num) == 100) {//caso especial
                return " cien ";
            } else {
                 return CENTENAS[Integer.parseInt(num.substring(0, 1))] + getDecenas(num.substring(1));
            } 
        }else{//por Ej. 099 
            //se quita el 0 antes de convertir a decenas
            return getDecenas(Integer.parseInt(num)+"");            
        }        
    }

    private static String getMiles(String numero) {// 999 999
        //obtiene las centenas
        String c = numero.substring(numero.length() - 3);
        //obtiene los miles
        String m = numero.substring(0, numero.length() - 3);
        String n="";
        //se comprueba que miles tenga valor entero
        if (Integer.parseInt(m) > 0) {
        	Integer numLen =m.length();  
        	if(numLen >=2)
        	{
        		Integer nNum =Integer.parseInt(m);// m.substring(m.length()-2));
        		if (nNum==1){
        			m =Integer.toString(  Integer.parseInt(m) -1); 
        		}
        		n = getCentenas(m);
        		return n + "mil " + getCentenas(c);
        	}
        	else
        	{
        		n = getCentenas(m);    
                if (Integer.parseInt(m)==1) 
                return  "mil " + getCentenas(c);
                else
                return n + "mil " + getCentenas(c);	
        	}
            
        } else {
            return "" + getCentenas(c);
        }

    }

    private static String getMillones(String numero) { //000 000 000
        //se obtiene los miles
        String miles = numero.substring(numero.length() - 6);
        //se obtiene los millones
        String millon = numero.substring(0, numero.length() - 6);
        String n = "";
        if(millon.length() == 1 && millon.equals("1")){
        	if(numero.equals("1000000")){
        		n = getCentenas(millon) + "millon de ";
        	}else{
        		n = getCentenas(millon) + "millon ";
        	}
        }else{
        	switch(numero){
        		case "2000000":
        		case "3000000":
        		case "4000000":
        		case "5000000":
        		case "6000000":
        		case "7000000":
        		case "8000000":
        		case "9000000":
        		case "10000000":
        			n = getUnidades(millon) + "millones de ";
        			break;
        		default:
        			n = direcciona(millon) + "millones ";
        			//n = getUnidades(millon) + "millones ";
        			break;
        	}
            
        }
        return n + getMiles(miles);        
    }
    
    public static boolean isNumeric(String str)
    {
    	return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }
    
    public static String direcciona(String millones){
    	String response = "";
    	int letNum = Integer.parseInt(millones);
    	String numLet = Integer.toString(letNum); 
    	int tamanio = numLet.length(); 
    	switch(tamanio){
    	case 1:
    		response = getUnidades(millones);
    		break;
    	case 2:
    		response = getDecenas(millones);
    		break;
    	case 3:
    		response = getCentenas(millones);
    		break;
    	case 4:
    		response = getMiles(millones);
    		break;
    	}
    	return response;
    }
    
    private static String getMilesMillones(String numero) {// 999 999
        //obtiene las centenas
        String c = numero.substring(numero.length() - 3);
        //obtiene los miles
        String m = numero.substring(0, numero.length() - 3);
        String n="";
        //se comprueba que miles tenga valor entero
        if (Integer.parseInt(m) > 0) {
        	Integer numLen =m.length();  
        	if(numLen >=2)
        	{
        		Integer nNum =Integer.parseInt(m);// m.substring(m.length()-2));
        		if (nNum==1){
        			m =Integer.toString(  Integer.parseInt(m) -1); 
        		}
        		n = getCentenas(m);
        		return n + "mil " + getCentenas(c);
        	}
        	else
        	{
        		n = getCentenas(m);    
                if (Integer.parseInt(m)==1) 
                return  "mil " + getCentenas(c);
                else
                return n + "mil " + getCentenas(c);	
        	}
            
        } else {
            return "" + getCentenas(c);
        }

    }

}