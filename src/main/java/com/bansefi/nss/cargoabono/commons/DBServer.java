package com.bansefi.nss.cargoabono.commons;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.bansefi.nss.cargoabono.vo.EntPendienteCargAbono;
import com.bansefi.nss.cargoabono.vo.EntSucursal;
import com.bansefi.nss.cargoabono.vo.ResponseServiceObject;

public class DBServer 
{
	private static final Logger log = LogManager.getLogger(DBServer.class);
	
	Connection conn=null;
	public Boolean dbOpenConnect(String BDStrConn,String BDStrUsr,String BDStrPwd)
	{
		Boolean Status =false;
		try
		{
			DriverManager.registerDriver(new   net.sourceforge.jtds.jdbc.Driver());
             conn = DriverManager.getConnection(
            		BDStrConn, BDStrUsr, BDStrPwd);
             Status = true;
		}
		catch(Exception ex)
		{
			Status =false;
			log.error("Error [dbOpenConnect] : "+ex.getMessage());
		}
		return Status;
	}
	
	public EntPendienteCargAbono dbConsultaPendientes(String Entidad,String Sucursal,String Terminal,String Empleado)
	{
		EntPendienteCargAbono oEnt = new EntPendienteCargAbono();
		try
		{
			CallableStatement cStmt = conn.prepareCall("{call spBsfProcesoPendienteCargoAbono(?, ?, ?, ?)}");
			cStmt.setString(1, Entidad);
			cStmt.setString(2, Sucursal);
			cStmt.setString(3, Terminal);
			cStmt.setString(4, Empleado);
			
			ResultSet rs = cStmt.executeQuery();

			while (rs.next()) 
			 {
				 oEnt.setEntidad(rs.getString("entidad"));
				 oEnt.setTipoOp(rs.getString("tipoOp"));
				 oEnt.setFechaValor(rs.getString("fechaValor"));
				 oEnt.setCajaInt(rs.getString("cajaInt"));
				 oEnt.setFechaOper(rs.getString("fechaOperacion"));
				 oEnt.setSucursal(rs.getString("sucursal"));
				 oEnt.setIdTerminal(rs.getString("idTerminal"));
				 oEnt.setIdEmpleado(rs.getString("idEmpleado"));
				 oEnt.setHoraOper(rs.getString("horaOp"));
				 oEnt.setFechaContable(rs.getString("fechaContable"));
				 oEnt.setDateProcess(rs.getString("dateProceso"));
				 oEnt.setDataTrans(rs.getString("dataTrans"));
				 oEnt.setDateCambioStatus(rs.getString("dateCambioStatus"));
				 
				 oEnt.setStatusPendiente(Integer.parseInt(rs.getString("StatusPendiente")));
				 oEnt.setIdMovimiento(Integer.parseInt(rs.getString("idMovimiento")));
				 oEnt.setFolioTrans(Integer.parseInt(rs.getString("folioTrans")));
				 oEnt.setStatus(1);
		  	 }
		}
		catch(Exception ex)
		{
			oEnt.setStatus(-1);
			log.error("Error [dbConsultaPendientes] : "+ex.getMessage());
		}
		return oEnt;
	}
	
}
