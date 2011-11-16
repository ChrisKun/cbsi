package es.ucm.fdi.isbc.funcSimilitud;

import javax.swing.JTree;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import jcolibri.cbrcore.CBRCase;
import jcolibri.exception.NoApplicableSimilarityFunctionException;
import jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;

public class MyTreeSimilarityFunction implements LocalSimilarityFunction {
	private JTree tree;

	public MyTreeSimilarityFunction(JTree tree) {
		this.tree = tree;
	}

	public String findByName(JTree tree, String name) {
		TreeNode root = (TreeNode) tree.getModel().getRoot();
		String pathRoot = root.toString();
		return find(root, pathRoot, name, 0, true);
	}

	private String find(TreeNode parentNode, String parentPath, Object node2Comp, int depth, boolean byName) {
		Object o = parentNode;

		// Si es by name, entonces comparamos los strings del nodo
		if (byName) {
			o = o.toString();
		}

		// Si no coinciden, habrá que buscar en los hijos de este nodo.
		if (!o.equals(node2Comp)) {

			// Buscamos en todos los hijos. Recursión en profundidad
			if (!parentNode.isLeaf() && parentNode.getChildCount() >= 0) {
				for (int n = 0; n < parentNode.getChildCount(); n++) {
					TreeNode childNode =  parentNode.getChildAt(n);
					// Añadimos el hijo a la path, para buscar luego con el.
					String childPath = parentPath+"/"+childNode.toString(); 
					String result = find(childNode, childPath, node2Comp, depth + 1, byName);
	                // Hemos encontrado ya el nodo?
	                if (result != null) {
	                    return result;
	                }
				}
			}
			
		} 
		else { // Coinciden, luego devuelvo la path construida.
			return parentPath;
		}

		// No match at this branch
		return null;
	}

	@Override
	public boolean isApplicable(Object caseObject, Object queryObject) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double compute(Object caseObject, Object queryObject) throws NoApplicableSimilarityFunctionException {
		// Obtenemos el path de la query
		String pathQuery = (String) queryObject;
		System.out.println(pathQuery);
		String[] sQuery, sCase;
		sQuery = pathQuery.split("/");
		pathQuery = this.findByName(tree, sQuery[sQuery.length-1]); //Buscamos solo con el último dato.
		System.out.println(pathQuery);
		//Volvemos a separarlo pero ahora con la path entera
		sQuery = pathQuery.split("/");
		
		// Obtenemos el path del caso a comparar
		String pathCase = (String) caseObject;
		sCase = pathCase.split("/");
		
		// Comparamos los paths para obtener la profundidad del nodo común
		//Sabemos que sCase[0] es un valor corrupto y el de sQuery es madrid. Asi que comparamos con sCase[1] == sQuery[1] pero este no hace falta.
		double profCoQ = 0.0; //Profundidad (Case v Query)
		boolean coincidencia = false;
		int i = Math.min(sCase.length, sQuery.length) - 1;
		while ( i > 0 && !coincidencia){
			coincidencia = sCase[i].equalsIgnoreCase(sQuery[i]);
			i--;
		}
		if(coincidencia)
			profCoQ = i + 1;
		else
			profCoQ = 0;
		
		// Calculamos similitud a devolver
		double maxprof = Math.max(sCase.length, sQuery.length);
		double result = profCoQ/maxprof;
		return result;
	}

}
