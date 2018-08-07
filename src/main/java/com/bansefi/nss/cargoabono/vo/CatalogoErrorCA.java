package com.bansefi.nss.cargoabono.vo;

import java.util.ArrayList;

import lombok.Data;

@Data
public class CatalogoErrorCA {
	private ArrayList<ErrorCA> PSV_ERROR_V;
	private ArrayList<Errores> STD_TRN_MSJ_PARM_V;
}
