package es.ucm.fdi.isbc.funcSimilitud;

import es.ucm.fdi.isbc.viviendas.representacion.Coordenada;
import jcolibri.exception.NoApplicableSimilarityFunctionException;
import jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;

public class MyCoordinateSimilarityFunction implements LocalSimilarityFunction {
	
	//Los puntos mas lejanos de la comunidad de madrid
	static double MAXLAT = 41.164722;
	static double MINLAT = 39.884444;
	static double MAXLON = -3.053056;
	static double MINLON = -4.579167;

	@Override
	public double compute(Object caseObject, Object queryObject)
			throws NoApplicableSimilarityFunctionException {
        double p1y = ((Coordenada)caseObject).getLatitud();
        double p2y = ((Coordenada)queryObject).getLatitud();
        double p1x = ((Coordenada)caseObject).getLongitud();
        double p2x = ((Coordenada)queryObject).getLongitud();
        
        //Distancia euclídea:
        double parteX = (p2x-p1x)*(p2x-p1x);
        double parteY = (p2y-p1y)*(p2y-p1y);
        double dist = Math.sqrt(parteX+parteY);
        if(dist < 0.5)
        	return 1-(2*dist);
        else
        	return 0;
        
       /* 
        double ca = 0;
        if (Math.max(a, b) != 0)
        	// Cociente de latitudes
        	ca = Math.min(a, b)/Math.max(a, b);
        
        double cb = 0;
        if (Math.max(c, d) != 0)
        	// Cociente de longitudes
        	cb = Math.min(c, d)/Math.max(c, d);
        
        return (ca + cb) / 2;*/
	}

	@Override
	public boolean isApplicable(Object caseObject, Object queryObject) {
		// TODO Auto-generated method stub
		return false;
	}
	

    



}
