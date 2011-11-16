package es.ucm.fdi.isbc.votacion;

import java.util.Collection;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.extensions.classification.ClassificationSolution;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.method.reuse.classification.SimilarityWeightedVotingMethod;
import es.ucm.fdi.isbc.viviendas.representacion.SolucionVivienda;

public class Clasificacion implements ClassificationSolution{
	SimilarityWeightedVotingMethod votacion;
	
	public Clasificacion(){
		 votacion = new SimilarityWeightedVotingMethod();
	}

	@Override
	public Attribute getIdAttribute() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getClassification() {return null;}
	
	public Integer getPrediccionPrecio(CBRCase query, Collection<RetrievalResult> cases){
		return ((SolucionVivienda)votacion.getPredictedCase(query, cases).getSolution()).getPrecio();
	}

}
