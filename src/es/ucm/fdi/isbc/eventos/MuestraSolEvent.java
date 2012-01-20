package es.ucm.fdi.isbc.eventos;

import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda;

public class MuestraSolEvent {
	private DescripcionVivienda descr;
	private int precioPrediccion;
	private double confianzaPrediccion;

	public MuestraSolEvent(DescripcionVivienda description,int precio_prediccion, double confianza_prediccion) {
		descr = description;
		precioPrediccion = precio_prediccion;
		confianzaPrediccion = confianza_prediccion;
	}

	public DescripcionVivienda getDescr() {
		return descr;
	}

	public int getPrecioPrediccion() {
		return precioPrediccion;
	}

	public double getConfianzaPrediccion() {
		return confianzaPrediccion;
	}

}
