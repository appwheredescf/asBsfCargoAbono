package com.bansefi.nss.cargoabono.process;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONObject;

import com.bansefi.nss.cargoabono.commons.UtilJson;
import com.bansefi.nss.cargoabono.consInter.CargoAbonoDS;
import com.bansefi.nss.cargoabono.vo.EntDescImpr;
import com.bansefi.nss.cargoabono.vo.ResponseService;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONArray;

public class CDatosSucursales {
	private static final Logger log = LogManager.getLogger(CDatosSucursales.class);
	private UtilJson utilJson = UtilJson.getInstance();
	public ResponseService EncriptarDescr(String input,String Strurl){
		ResponseService oResult= new ResponseService();
		String Salida ="";
		try
		{
			Salida = getSalida(input,Strurl);
			if(Salida.trim().length()>0)
			{
				try
				{
					JSONObject json = new JSONObject(Salida);

					oResult.setDescripcion( json.getString("respuesta"));
					oResult.setTXT_ARG1(json.getString("error"));
					oResult.setStatus(Integer.parseInt(json.getString("codRet")));
	
				}
				catch(Exception ex)
				{
					log.error("No responde [EncriptarDescr] "+ ex.getMessage());
					oResult.setStatus(-1);
					oResult.setDescripcion(ex.getMessage());
					//log.error("Error [Encriptar]"+ex.getMessage());
				}

			}
			else
			{
				log.error("No responde [EncriptarDescr] "+Strurl +" input "+ input);
			}
		}catch(Exception ex)
		{
			log.error("No responde [EncriptarDescr] "+Strurl +" input "+ input+ "Err:  "+ex.getMessage());
		}
		return oResult;
	}

	public static String getSalida(String input,String StrUrl)
	{
		String StrResp ="";
				try
				{
					URL restServiceURL = new URL(StrUrl);
					HttpURLConnection httpConnection = (HttpURLConnection) restServiceURL.openConnection();
					
					httpConnection.setDoOutput(true);
					httpConnection.setRequestMethod("POST");
					httpConnection.setRequestProperty("Content-Type", "application/json");
					OutputStream outputStream = httpConnection.getOutputStream();
					outputStream.write(input.getBytes());
					outputStream.flush();
					
					if (httpConnection.getResponseCode() != 200) 
					{
						throw new RuntimeException("Failed : HTTP error code : " + httpConnection.getResponseCode());
					}
					BufferedReader responseBuffer = new BufferedReader(new InputStreamReader((httpConnection.getInputStream()), "UTF8"));
					String output;
					while ((output = responseBuffer.readLine()) != null) {
						StrResp += output;
					}
					httpConnection.disconnect();		
				}
				catch(Exception ex)
				{
					log.error("No responde [getSalida] "+StrUrl +" input "+ input);
					System.out.println("Error Msg "+ input +" Url : "+StrUrl);
					StrResp="";		
				}
				return StrResp;
	}
	
	public EntDescImpr SerealObjImp(String StrCad)
	{
		EntDescImpr res= new EntDescImpr(); 
		try
		{
			JSONObject jsonSalida = new JSONObject(StrCad);
		
			ArrayList<String> nodos = new ArrayList<String>();
	        res = (EntDescImpr) utilJson.jsonToObject(res, StrCad, nodos);
			
		}
		catch(Exception ex)
		{
			System.out.println("Error Msg "+ ex.getMessage());
			log.error("No responde [SerealObjImp] "+StrCad );
		}
		return res;
	}
}
