package es.ucm.fdi.isbc.controlador;

import java.util.ArrayList;
import java.util.Collection;

import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda;
import es.ucm.fdi.isbc.viviendas.representacion.RecomendadorVivienda;

public class Controlador {
	private RecomendadorVivienda rv;
	private static Controlador controlador;

	/**
	 * Patrón Singleton (un único controlador por recomendador)
	 */
	public static Controlador getInstance() {
		if (controlador == null) {
			controlador = new Controlador();
		}
		return controlador;
	}

	public Controlador() {}

	public void setRecomendadorVivienda(RecomendadorVivienda recomendador) {
		rv = recomendador;
	}

	public Controlador(RecomendadorVivienda recomendador) {
		this.rv = recomendador;
	}

	/**
	 * Pone en la CBRQuery la DescripcionVivienda pasada por parámetro e intenta
	 * ejecutar el cycle del RecomendadorVivienda.
	 * 
	 * @param DescripcionVivienda
	 *            descr
	 */
	public void repite(DescripcionVivienda descr) {
		rv.repite(descr);
	}

	public void fin() {
		rv.fin();
	}

	public CBRCaseBase getCaseBase() {
		return rv.getCaseBase();
	}

	public Collection<CBRCase> getCases() {
		return rv.getCases();
	}

	public NNConfig getSimCongfig() {
		return rv.getSimCongfig();
	}

	public void moreLikeThis(ArrayList<Integer> idDescViviendVisitadas) {
		rv.moreLikeThis(idDescViviendVisitadas);
	}

}
