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
		// Se Formatea hora operacion ya que el servicio lo regresa solo con 6
		// dijitos sin los puntos y para actualizarlo nececita los 2 puntos
		// pendiente verificar si esto afecta o no
		if (request.length() > 0 && request.length() < 7) {
			String hora, minuto, segundo;
			hora = request.substring(0, 2);
			minuto = request.substring(2, 4);
			segundo = request.substring(4, 6);
			response = hora + ":" + minuto + ":" + segundo;
		}
		return response;
	}
}
