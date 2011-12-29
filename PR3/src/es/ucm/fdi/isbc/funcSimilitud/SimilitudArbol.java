package es.ucm.fdi.isbc.funcSimilitud;

import es.ucm.fdi.isbc.viviendas.representacion.Arbol;
import jcolibri.exception.NoApplicableSimilarityFunctionException;
import jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;

/**
 * No hace falta cargar la base de casos en el preciclo, a cada localización, que es un String (una ruta, o un árbol 
 * de un sólo camino). Puede mirarse la similitud con otro Strig/ruta/árbol para ver la profundidad de las hojas y del
 * padre común.
 * 
 */

public class SimilitudArbol implements LocalSimilarityFunction
{
	/** Atributos **/
	
		Arbol arbol;
	
	/** Constructoras **/
		
		public SimilitudArbol(Arbol arbol)
		{
			this.arbol = arbol;
		}
	
	/** Métodos **/
	
		/* Métodos que implementan a LocalSimilarityFunction */
	
			public double compute(Object caseObject, Object queryObject) throws NoApplicableSimilarityFunctionException
			{
				
				if ((caseObject == null) || (queryObject == null)) return 0;

				if (!(caseObject instanceof java.lang.String))
					throw new NoApplicableSimilarityFunctionException(this.getClass(), queryObject.getClass());

				if (!(queryObject instanceof java.lang.String))
					throw new NoApplicableSimilarityFunctionException(this.getClass(), queryObject.getClass());

				Arbol a1 = new Arbol((String)caseObject);
				Arbol a2 = new Arbol((String)queryObject);

				// Si la queryObject no existe en el árbol de datos saldrá sólo la raíz madrid. En cuyo caso el 
				// nivel del padre común será 0 y la medida de similitud será 0. En otro caso,

				// Hacemos el cast a double a uno de los números para que la división la haga real y no entera ya que
					// los niveles del árbol son números naturales.

				return ((double)(a1.getNivelPadreComun(a2))) / Math.max(a1.getProfundidad(), a2.getProfundidad());

			}			

			// isApplicable a String
			public boolean isApplicable(Object caseObject, Object queryObject)
			{
				if ((caseObject == null) && (queryObject == null))
					return	true;
				else if (caseObject == null)
					return	queryObject instanceof String;
				else if (queryObject == null)
					return	caseObject instanceof String;
				else
					return	(caseObject instanceof String) &&
							(queryObject instanceof String);
			}
}
