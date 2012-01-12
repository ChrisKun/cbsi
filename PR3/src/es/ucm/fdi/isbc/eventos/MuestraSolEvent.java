package es.ucm.fdi.isbc.eventos;

import java.util.ArrayList;

import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda;

public class MuestraSolEvent {
	private ArrayList<DescripcionVivienda> descrs;
	private String type;


	public MuestraSolEvent(ArrayList<DescripcionVivienda> descrs, String t) {
		this.descrs = new ArrayList<DescripcionVivienda>(descrs);
		type = t;
	}


	public ArrayList<DescripcionVivienda> getDescrs() {
		return descrs;
	}
	
	public String getType() {
		return type;
	}

}
