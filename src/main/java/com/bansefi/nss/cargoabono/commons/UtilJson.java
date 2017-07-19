package com.bansefi.nss.cargoabono.commons;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;


public final class UtilJson<T> 
{

	private static final Logger log = LogManager.getLogger(UtilJson.class);
	/*
	 * Se define variable del mismo tipo que la clase para implementación de patrón singleton.
	 */
	private static UtilJson util;

	/*
	 * Constructor privado para construir patrón singleton.
	 */
	private UtilJson() {
	}

	/*
	 * Método estatico que devuelve instancia del tipo de clase para patrón singleton.
	 */
	public static UtilJson getInstance() {
		if (util == null)
			util = new UtilJson();
		return util;
	}

	/*
	 * Método utilitario para convertir un json a un objeto.
	 */
	public Object jsonToObject(T objectRes, String json, ArrayList<String> nodos) throws ParseException {
		try {
			Gson gson = new Gson();
			JSONParser parser = new JSONParser();
			Object objRes = parser.parse(json);
			JSONObject jsonObject = (JSONObject) objRes;
			for (String nodo : nodos) {
				jsonObject = (JSONObject) jsonObject.get(nodo);
			}
			String jsonResponse = jsonObject.toJSONString();
			return gson.fromJson(jsonResponse, ((T) objectRes).getClass());
		} catch (Exception e) {
			// TODO: handle exception
			log.error("\nError en el metodo jsonToObject(T objectRes, String json, ArrayList<String> nodos)" + "\nParametros de entrada ( " + objectRes.getClass() + ", " + json + ", "
					+ nodos.toString() + " )" + "\nException Message: " + e.getMessage());
			return null;
		}
	}

	/*
	 * Método utilitario para convertir un objeto a un json.
	 */
	public String objectToJson(T object) {
		Gson gson = new Gson();
		return gson.toJson(object);
	}

	/*
	 * Método utilitario para realizar llamada REST por el método POST
	 */
	public String callRestPost(String input, String uri) {
		//UtilJson util = UtilJson.getInstance();
		String output = "";
		try {

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
			BufferedReader responseBuffer = new BufferedReader(new InputStreamReader((httpConnection.getInputStream()), "UTF8"));
			String outputline;
			while ((outputline = responseBuffer.readLine()) != null) {
				output += outputline;
			}
			httpConnection.disconnect();
		} 
		catch (Exception ex) 
		{
			log.error("callRestPost Entrada - " + input+" [ uri ]"+uri+" [error :]"+ex.getMessage());
			ex.printStackTrace();
		}
		return output;
	}

}
