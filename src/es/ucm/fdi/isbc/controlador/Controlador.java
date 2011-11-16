package es.ucm.fdi.isbc.controlador;

import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda;
import es.ucm.fdi.isbc.viviendas.representacion.RecomendadorVivienda;

public class Controlador {
	private RecomendadorVivienda rv;
	
	public Controlador(RecomendadorVivienda recomendador){
		this.rv = recomendador;
	}
	
	public void repite(DescripcionVivienda descr){
		rv.repite(descr);
	}

	public void fin() {
		rv.fin();	
	}
}
