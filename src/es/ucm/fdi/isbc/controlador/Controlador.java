package es.ucm.fdi.isbc.controlador;

import es.ucm.fdi.isbc.gui.VentanaPpal;
import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda;
import es.ucm.fdi.isbc.viviendas.representacion.RecomendadorVivienda;

public class Controlador {
	private RecomendadorVivienda rv;
	private VentanaPpal v;
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
	
	public void setVentanaPpal(VentanaPpal v){
		this.v = v;
	}
	
	public Controlador(RecomendadorVivienda recomendador){
		this.rv = recomendador;
	}
	
	public void repite(DescripcionVivienda descr){
		rv.repite(descr);
	}

	public void fin() {
		rv.fin();	
	}

//	public void iniciaNormal() {
//		rv.iniciaNormal();
//	}

	public void muestraSolucion(DescripcionVivienda description, Integer precio_prediccion, double confianza_prediccion) {		
		v.muestraSol(description, precio_prediccion, confianza_prediccion);		
	}

}
