package com.bansefi.nss.cargoabono.commons;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.bansefi.nss.cargoabono.vo.EntInsertDiarioElect;
import com.bansefi.nss.cargoabono.vo.ResponseService;

public class BD2Connect 
{
	private static final Logger log = LogManager.getLogger(DBServer.class);
	Connection conn=null;
	
	public Boolean dbOpenConnect(String BDStrConn,String BDStrUsr,String BDStrPwd)
	{
		Boolean Status =false;
		try
		{
			DriverManager.registerDriver(new   com.ibm.db2.jcc.DB2Driver());
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
	
	public String Consulta()
	{
		String StrResult ="";
		try
		{
			String Squery ="select 1 as uno from sysibm.sysdummy1";
			Squery="select * from SYSIBM.SYSVERSIONS";
			Squery ="select * from syscat.procedures";
			Squery =" CREATE OR REPLACE PROCEDURE PRC_SAMPLE4() LANGUAGE SQL Begin select 1 as uno from sysibm.sysdummy1 End";
			CallableStatement cStmt = conn.prepareCall(Squery);
			
			cStmt.executeUpdate();
			/*ResultSet rs = cStmt.executeQuery();

			while (rs.next()) 
			 {
					System.out.println("Valor 001  "+ rs.getString(1) +" : "+rs.getString(2));
			 }*/
		}
		catch(Exception ex)
		{
			log.error("Error [dbOpenConnect] : "+ex.getMessage());
			System.out.println("Error : "+ex.getMessage());
		}
		return StrResult;
	}
	
	public ResponseService InsertDiarioElect(EntInsertDiarioElect oInset)
	{
		String Squery ="";
		String SEncab="";
		ResponseService oResult = new ResponseService();
		oResult.setStatus(0);
		try
		{
			SEncab ="CONTRIDA   ,MAS_MENOS_DI ,MODO_TX      ,SGN_CTBLE_DI ,COD_CLOP_SIST,"+
					"NUM_PUESTO ,SITU_TX      ,COD_ERR_1    ,COD_ERR_2    ,COD_ERR_3    ,"+
					"COD_ERR_4  ,COD_ERR_5    ,COD_RESPUESTA,COD_NUMRCO_MONEDA ,COD_NUMRCO_MONEDA1,"+
					"HORA_OPRCN ,HORA_PC       ,HORA_PC_FINAL ,COD_INTERNO_UO ,COD_INTERNO_UO_FSC ,"+
					"COD_NRBE_EN,COD_NRBE_EN_FSC,COD_TX_DI      ,TIPO_SBCLOP    ,FECHA_ANUL     ,"+
					"FECHA_CTBLE,FECHA_OFF      ,FECHA_OPRCN    ,FECHA_PC       ,FECHA_VALOR    ,"+
					"NUM_SEC_ANUL,NUM_SEC_OFF    ,NUM_SEC       ,COD_TX         ,ID_EMPL_ANUL    ,"+
					"ID_EMPL_AUT,ID_EMPL_OFF     ,ID_INTERNO_EMPL_EP,ID_INTERNO_TERM_TN,ID_TERM_ANUL    ,"+
					"ID_TERM_OFF,NUM_SEC_AC      ,IMP_NOMINAL_X   ,IMP_NOMINAL     ,IMP_SDO         ,"+
					"DI_TEXT_ARG_1,DI_TEXT_ARG_2 ,DI_TEXT_ARG_3   ,DI_TEXT_ARG_4   ,DI_TEXT_ARG_5   ,"+
					"CLAVE_ANUL_DI,VALOR_DTLL_TX   ";
			
			Squery="insert into DI_DIARIO_ELECTRON("+SEncab+") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"; //25 parametros
			Squery+="?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
						
			CallableStatement cStmt = conn.prepareCall(Squery);
			
			cStmt.setString(1, oInset.getCONTRIDA());				cStmt.setString(2, oInset.getMAS_MENOS_DI());
			cStmt.setString(3, oInset.getMODO_TX());				cStmt.setString(4, oInset.getSGN_CTBLE_DI());
			cStmt.setString(5, oInset.getCOD_CLOP_SIST());			cStmt.setString(6, oInset.getNUM_PUESTO());
			cStmt.setString(7, oInset.getSITU_TX());				cStmt.setInt(8, Integer.parseInt( oInset.getCOD_ERR_1()));
			cStmt.setInt(9, Integer.parseInt( oInset.getCOD_ERR_2()));cStmt.setInt(10,Integer.parseInt( oInset.getCOD_ERR_3()));
			cStmt.setInt(11,Integer.parseInt( oInset.getCOD_ERR_4()));cStmt.setInt(12,Integer.parseInt( oInset.getCOD_ERR_5()));
		cStmt.setInt(13, Integer.parseInt(oInset.getCOD_RESPUESTA()));cStmt.setString(14, oInset.getCOD_NUMRCO_MONEDA());
			cStmt.setString(15, oInset.getCOD_NUMRCO_MONEDA1());	cStmt.setString(16, oInset.getHORA_OPRCN());
			cStmt.setString(17, oInset.getHORA_PC());				cStmt.setString(18, oInset.getHORA_PC_FINAL());
			cStmt.setString(19, oInset.getCOD_INTERNO_UO());		cStmt.setString(20, oInset.getCOD_INTERNO_UO_FSC());
			cStmt.setString(21, oInset.getCOD_NRBE_EN());			cStmt.setString(22, oInset.getCOD_NRBE_EN_FSC());
			cStmt.setString(23, oInset.getCOD_TX_DI());				cStmt.setString(24, oInset.getTIPO_SBCLOP());
			cStmt.setString(25, oInset.getFECHA_ANUL());			cStmt.setString(26, oInset.getFECHA_CTBLE());
			cStmt.setString(27, oInset.getFECHA_OFF());				cStmt.setString(28, oInset.getFECHA_OPRCN());
			cStmt.setString(29, oInset.getFECHA_PC());				cStmt.setString(30, oInset.getFECHA_VALOR());
	cStmt.setInt(31, Integer.parseInt( oInset.getNUM_SEC_ANUL()));  cStmt.setInt(32, Integer.parseInt( oInset.getNUM_SEC_OFF()));
	cStmt.setInt(33,Integer.parseInt( oInset.getNUM_SEC()));		cStmt.setString(34, oInset.getCOD_TX());
			cStmt.setString(35, oInset.getID_EMPL_ANUL());			cStmt.setString(36, oInset.getID_EMPL_AUT());
			cStmt.setString(37, oInset.getID_EMPL_OFF());			cStmt.setString(38, oInset.getID_INTERNO_EMPL_EP());
			cStmt.setString(39, oInset.getID_INTERNO_TERM_TN());	cStmt.setString(40, oInset.getID_TERM_ANUL());
			cStmt.setString(41, oInset.getID_TERM_OFF());			cStmt.setInt(42,Integer.parseInt( oInset.getNUM_SEC_AC()));
			cStmt.setString(43, oInset.getIMP_NOMINAL_X());			cStmt.setInt(44, Integer.parseInt( oInset.getIMP_NOMINAL()));
	cStmt.setInt(45, Integer.parseInt(oInset.getIMP_SDO()));		cStmt.setString(46, oInset.getDI_TEXT_ARG_1());
			cStmt.setString(47, oInset.getDI_TEXT_ARG_2());			cStmt.setString(48, oInset.getDI_TEXT_ARG_3());
			cStmt.setString(49, oInset.getDI_TEXT_ARG_4());			cStmt.setString(50, oInset.getDI_TEXT_ARG_5());
			cStmt.setString(51, oInset.getCLAVE_ANUL_DI());			cStmt.setString(52, oInset.getVALOR_DTLL_TX());
	
			Integer rs = cStmt.executeUpdate();
			cStmt.close();
			if(rs>0)
			{
				oResult.setStatus(1);
				conn.commit();
			}
		}
		catch(Exception ex)
		{
			oResult.setStatus(-1);
			oResult.setDescripcion(ex.getMessage());
			System.out.println("Error "+ex.getMessage());
		}
		return oResult;
	}
	
}
