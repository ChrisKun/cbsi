package funcSimilitud;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import jcolibri.exception.NoApplicableSimilarityFunctionException;
import jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;

public class MyTreeSimilarityFunction implements LocalSimilarityFunction{
	private JTree tree;
	
	public MyTreeSimilarityFunction(JTree tree) {
		this.tree = tree;
	}

	@Override
	public double compute(Object caseObject, Object queryObject)
			throws NoApplicableSimilarityFunctionException {
		String caseLocalizacion = (String) caseObject;
		String queryLocalizacion = (String) queryObject;
		String[] caseSplit = {};
		String barrio = "",	zona = "",	calle = "";
		caseSplit = caseLocalizacion.split("/");
		switch (caseSplit.length) {
		// s[0] es caca
		case 2:
			zona = caseSplit[1];
			break;
		case 3:
			zona = caseSplit[1];
			barrio = caseSplit[2];
			break;
		case 4:
			zona = caseSplit[1];
			barrio = caseSplit[2];
			calle = caseSplit[3];
			break;
		}
		DefaultMutableTreeNode top = (DefaultMutableTreeNode)tree.getModel().getRoot();
		DefaultMutableTreeNode hijo = new DefaultMutableTreeNode();
		DefaultMutableTreeNode nieto = new DefaultMutableTreeNode();
		DefaultMutableTreeNode bisnieto = new DefaultMutableTreeNode();
		//buscamos en zonas
		boolean found = false; int i = 0;
		while (!found && i < top.getChildCount()) {
			hijo = (DefaultMutableTreeNode) top.getChildAt(i);
			// Si no está la zona, el barrio y la calle tampoco
			found = hijo.getUserObject().equals(zona);
			// Si tenemos barrio en el caso, comprobamos barrios
			if (!found && caseSplit.length > 2){ 	
		
				boolean encontrado = false; int j = 0;				
				while (!encontrado && j < hijo.getChildCount()) {
					nieto = (DefaultMutableTreeNode) hijo.getChildAt(j);
					encontrado = nieto.getUserObject().equals(barrio);
					// Si no se ha encontrado comprobamos calles, si tenemos calles en el caso
					if (!encontrado && caseSplit.length > 3){
						
						boolean foundF = false; int k = 0;						
						while (!foundF	&& k < nieto.getChildCount()) {
							bisnieto = (DefaultMutableTreeNode) nieto.getChildAt(k);
							foundF = bisnieto.getUserObject().equals(calle);
							k++;
						}
						if (!foundF) {
							// añadir calle al barrio indicado

						}
					}
					else{
						
					}
					j++;
					
				}
			}
			else{
				// Si se ha encontrado en el primer nivel del árbol (zonas)
			}
			i++;
		} 

		return 0;
	}

	@Override
	public boolean isApplicable(Object caseObject, Object queryObject) {
		// TODO Auto-generated method stub
		return false;
	}

}
