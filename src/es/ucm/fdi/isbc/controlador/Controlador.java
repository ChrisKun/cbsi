package es.ucm.fdi.isbc.controlador;

import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda;
import es.ucm.fdi.isbc.viviendas.representacion.RecomendadorVivienda;

public class Controlador {
	private RecomendadorVivienda rv;
	private static Controlador controlador;
	
	public static Controlador getInstance(){
		if (controlador == null){
			controlador = new Controlador();
			// By default
		}
		return controlador;
	}
	
	public Controlador(){}
	
	public void setRecomendadorVivienda(RecomendadorVivienda recomendador){
		rv = recomendador;
	}
	
	public Controlador(RecomendadorVivienda recomendador){
		this.rv = recomendador;
	}
	
	public void repite(DescripcionVivienda descr){
		if (descr == null) rv.setEvaluacionSistema(true);
		else rv.setEvaluacionSistema(false);
		rv.repite(descr);
	}
	
	public void setEvaluacionSistema(boolean b){
		rv.setEvaluacionSistema(b);
	}

	public void fin() {
		rv.fin();	
	}

//	public void iniciaNormal() {
//		rv.iniciaNormal();
//	}

}
