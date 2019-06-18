
package com.bansefi.nss.cargoabono.ws;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

import com.bansefi.nss.cargoabono.commons.FuncionesEncryption;
import com.bansefi.nss.cargoabono.commons.Util;
import com.bansefi.nss.cargoabono.process.CargoAbono;
import com.bansefi.nss.cargoabono.properties.EndpointProperties;
import com.bansefi.nss.cargoabono.vo.ReqInsertDiarioDTO;


public class Main {

    public static void main(String[] args) throws Exception {


        ServiciosCargoAbono oc = new ServiciosCargoAbono();
        String resp1 = oc.CargoAbonoIntervencion("E1660401", "Bansef47", "0166", "0001", "E1660401", "12000124", "0070673264", "A", "1111-11-11", "100.00", "Ordenante", "2019-05-30", "13:20:00", "I", "JOSE HERNANDEZ", "CUENTAHORRO",  "DVI        ", "DVI        ", "99", "0002", "06f2f9e3-f41d-dc40-54f7-bfaaa0d8b25dz");
        System.out.println("Cargo Intervencion -->" + resp1);
    }
}
