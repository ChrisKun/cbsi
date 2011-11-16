package es.ucm.fdi.isbc.funcSimilitud;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import jcolibri.exception.NoApplicableSimilarityFunctionException;


public class PruebaMyTreeSimilarity {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("Madrid");
		JTree tree = new JTree(top);
		top.add(new DefaultMutableTreeNode("BarrioA"));
		top.add(new DefaultMutableTreeNode("BarrioB"));
		top.add(new DefaultMutableTreeNode("BarrioC"));
		((DefaultMutableTreeNode)top.getChildAt(0)).add(new DefaultMutableTreeNode("ZonaA"));
		((DefaultMutableTreeNode)top.getChildAt(0)).add(new DefaultMutableTreeNode("ZonaB"));
		((DefaultMutableTreeNode)top.getChildAt(1)).add(new DefaultMutableTreeNode("ZonaC"));
		((DefaultMutableTreeNode)top.getChildAt(2)).add(new DefaultMutableTreeNode("ZonaD"));
		DefaultMutableTreeNode hijoA = (DefaultMutableTreeNode)top.getChildAt(0).getChildAt(0);
		DefaultMutableTreeNode hijoB = (DefaultMutableTreeNode)top.getChildAt(1).getChildAt(0);
		hijoA.add(new DefaultMutableTreeNode("CalleA"));
		hijoA.add(new DefaultMutableTreeNode("CalleB"));
		hijoB.add(new DefaultMutableTreeNode("CalleC"));
		hijoB.add(new DefaultMutableTreeNode("CalleD"));
		MyTreeSimilarityFunction simFunc = new MyTreeSimilarityFunction(tree);
		String buscado = "CalleC";
		double value = 0;
		try {
			value = simFunc.compute("Madrid/BarrioB/ZonaC/CalleD", buscado);
		} catch (NoApplicableSimilarityFunctionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(Double.toString(value));
		//String s = a.buscaNodo("CalleB");
		//TreePath path = a.findByName(tree, new String[]{"CalleB"});
	}

}
