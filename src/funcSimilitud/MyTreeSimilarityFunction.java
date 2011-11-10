package funcSimilitud;

import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import jcolibri.exception.NoApplicableSimilarityFunctionException;
import jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;

public class MyTreeSimilarityFunction implements LocalSimilarityFunction {
	private JTree tree;

	public MyTreeSimilarityFunction(JTree tree) {
		this.tree = tree;
	}

	public TreePath findByName(JTree tree, String name) {
		TreeNode root = (TreeNode) tree.getModel().getRoot();
		return find(tree, new TreePath(root), name, 0, true);
	}

	private TreePath find(JTree tree, TreePath parent, Object node2Comp, int depth, boolean byName) {
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		Object o = node;

		// If by name, convert node to a string
		if (byName) {
			o = o.toString();
		}

		// If equal, go down the branch
		if (!o.equals(node2Comp)) {

			// Traverse children
			if (node.getChildCount() >= 0) {
				for (Enumeration e = node.children(); e.hasMoreElements();) {
					TreeNode n = (TreeNode) e.nextElement();
					// Añadimos el hijo a la path, para buscar luego con el.
					TreePath path = parent.pathByAddingChild(n); 
					TreePath result = find(tree, path, node2Comp, depth + 1, byName);
					// Found a match
					if (result != null) {
						return result;
					}
				}
			}
		} else {
			return parent;
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
		TreePath path = this.findByName(tree, (String)queryObject);
		System.out.println(path.toString());
		// Obtenemos el path del caso a comparar
		
		// Comparamos los paths para obtener la profundidad del nodo común
		
		// Calculamos similitud a devolver
		return 0;
	}

}
