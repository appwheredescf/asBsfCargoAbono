package com.bansefi.nss.cargoabono.commons;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.bansefi.nss.cargoabono.vo.Errores;
import com.google.gson.Gson;

public final class Util<T> {
	/*
	 * Metodo utilitario para convertir un json a un objeto.
	 */
	public Object jsonToObject(T objectRes, String json, ArrayList<String> nodos) throws ParseException {
		Gson gson = new Gson();
		JSONParser parser = new JSONParser();
		Object objRes = parser.parse(json);
		JSONObject jsonObject = (JSONObject) objRes;
		for (String nodo : nodos) {
			jsonObject = (JSONObject) jsonObject.get(nodo);
		}
		String jsonResponse = jsonObject.toJSONString();
		//System.out.println(jsonResponse);
		try {
			return gson.fromJson(jsonResponse, ((T) objectRes).getClass());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * Metodo utilitario para convertir un objeto a un json.
	 */
	public String objectToJson(Object object) {
		Gson gson = new Gson();
		return gson.toJson(object);
	}

	/*
	 * Metodo utilitario para realizar llamada REST por el metodo POST
	 */
	public String callRestPost(Object obj, String uri) {
		String output = "";
		try {
			String input = objectToJson(obj);
			URL restServiceURL = new URL(uri);
			HttpURLConnection httpConnection = (HttpURLConnection) restServiceURL.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Content-Type", "application/json");
			OutputStream outputStream = httpConnection.getOutputStream();
			outputStream.write(input.getBytes());
			outputStream.flush();
			if (httpConnection.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + httpConnection.getResponseCode());
			}
			BufferedReader responseBuffer = new BufferedReader(
					new InputStreamReader((httpConnection.getInputStream()), "UTF8"));
			String outputline;
			while ((outputline = responseBuffer.readLine()) != null) {
				output += outputline;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return output;
	}

	/*
	 * Metodo utilitario para limpiar lista de errores
	 */
	public String limpiarErrores(ArrayList<Errores> request) {
		String response = "";
		for (int i = 0; i < request.size(); i++) {
			if (request.get(i).getTEXT_CODE().equals("0000")) {
				request.remove(i);
				i--;
			}
		}
		for (int j = 0; j < request.size(); j++) {
			if (!request.get(j).getTEXT_CODE().equals("0000")) {
				if (j != request.size() - 1) {
					response = response + request.get(j).getTEXT_CODE().trim() + "|"
							+ request.get(j).getTEXT_ARG1().trim() + ",";
				} else {
					response = response + request.get(j).getTEXT_CODE().trim() + "|"
							+ request.get(j).getTEXT_ARG1().trim() + ".";
				}
			}
		}
		return response;
	}

	/*
	 * Metodo utilitario para dar formato a la hora
	 */
	public String formatoHora(String request) {
		String response = "";
		if (request.length() > 0 && request.length() < 7) {
			String hora, minuto, segundo;
			hora = request.substring(0, 2);
			minuto = request.substring(2, 4);
			segundo = request.substring(4, 6);
			response = hora + ":" + minuto + ":" + segundo;
		}
		return response;
	}
	
	/*
	 *Metodo para identificar tipo operacion contra intervencion 
	 */
	public String concInterv(String request){
		String response = "";
		switch(request){
			case "990002": response = "ABONOS VARIOS " + request;
			break;
			case "030005": response = "DOC. NO COMPENSADO. " + request;
			break;
			case "990001": response = "ADEUDOS VARIOS " + request;
			break;
			case "030001": response = "PAGO CHEQUE " + request;
			break;
			case "220054": response = "RECIBOS VARIOS " + request;
			break;
		}
		return response;
	}
	
	/*
	 *Metodo para realizar recorrimiento de bits con formaro dd/mm/aaaa 
	 */
	public static char[] getCharArrayfromFechaLocal(String fechaTCB){
        String [] lista=fechaTCB.split("/");
        int y=Integer.parseInt(lista[2]);
        int m=Integer.parseInt(lista[1]);
        int d=Integer.parseInt(lista[0]);
        int value=YMD2int(y,m,d);
        char[] listaChar=new char[4];
        listaChar[0] = ((char)(value >> 24 & 0xFF));
        listaChar[1] = ((char)(value >> 16 & 0xFF));
        listaChar[2] = ((char)(value >> 8 & 0xFF));
        listaChar[3] = ((char)(value >> 0 & 0xFF));
        return listaChar;
    }
	
	public static char[] getCharArrayfromHora(String hora){
        String [] lista=hora.split(":");
        int hour=Integer.parseInt(lista[0]);
        int min=Integer.parseInt(lista[1]);
        int sec=Integer.parseInt(lista[2]);
        int ms=0;
        int value=HMSM2int(hour,min,sec,ms);
        char[] listaChar=new char[4];

        listaChar[0] = ((char)(value >> 24 & 0xFF));
        listaChar[1] = ((char)(value >> 16 & 0xFF));
        listaChar[2] = ((char)(value >> 8 & 0xFF));
        listaChar[3] = ((char)(value >> 0 & 0xFF));
        return listaChar;
    }
	
	public static  int YMD2int(int y, int m, int d){
        if (m < 3){
            y += 399;
            m += 9;
        }else{
            y += 400;
            m -= 3;
        }
        return y * 365 + y / 4 - y / 100 + y / 400 + (153 * m + 2) / 5 + d - 146037;
    }
	
	static final int HMSM2int(int hour, int min, int sec, int ms)
    {
        return hour * 3600000 + min * 60000 + sec * 1000 + ms;
    }
}
