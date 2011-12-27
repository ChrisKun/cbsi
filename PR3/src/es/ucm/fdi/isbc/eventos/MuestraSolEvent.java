package es.ucm.fdi.isbc.eventos;

import java.util.ArrayList;

import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda;

public class MuestraSolEvent {
	private ArrayList<DescripcionVivienda> descrs;


	public MuestraSolEvent(ArrayList<DescripcionVivienda> descrs) {
		this.descrs = new ArrayList<DescripcionVivienda>(descrs);
	}


	public ArrayList<DescripcionVivienda> getDescrs() {
		return descrs;
	}

}
