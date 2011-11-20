package es.ucm.fdi.isbc.funcSimilitud;

import es.ucm.fdi.isbc.viviendas.representacion.Coordenada;
import jcolibri.exception.NoApplicableSimilarityFunctionException;
import jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;

public class MyCoordinateSimilarityFunction implements LocalSimilarityFunction {

	@Override
	public double compute(Object caseObject, Object queryObject)
			throws NoApplicableSimilarityFunctionException {
        double a = ((Coordenada)caseObject).getLatitud();
        double b = ((Coordenada)queryObject).getLatitud();
        double c = ((Coordenada)caseObject).getLongitud();
        double d = ((Coordenada)queryObject).getLongitud();
        
        double ca = 0;
        if (Math.max(a, b) != 0)
        	// Cociente de latitudes
        	ca = Math.min(a, b)/Math.max(a, b);
        
        double cb = 0;
        if (Math.max(c, d) != 0)
        	// Cociente de longitudes
        	cb = Math.min(c, d)/Math.max(c, d);
        
        return (ca + cb) / 2;
	}

	@Override
	public boolean isApplicable(Object caseObject, Object queryObject) {
		// TODO Auto-generated method stub
		return false;
	}
	

    



}
