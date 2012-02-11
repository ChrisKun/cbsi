package es.ucm.fdi.isbc.gui;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import es.ucm.fdi.isbc.viviendas.representacion.ExtrasFinca;

@SuppressWarnings("serial")
public class PanelExtrasFinca extends JPanel{
	
	private JLabel[] labels;
	private JComboBox combos[];
	
	public PanelExtrasFinca(){
		super();
		
		JPanel pDatos = new JPanel();
		GridLayout layout1 = new GridLayout(7,2);
		layout1.setVgap(18);
		pDatos.setLayout(layout1);
		//pDatos.setPreferredSize(new Dimension(500, 700));
		
		labels = new JLabel[7];
		labels[0] = new JLabel("  Ascensor ");
		labels[1]  = new JLabel("  Trastero ");
		labels[2]  = new JLabel("  Energía solar ");
		labels[3]  = new JLabel("  Portería ");
		labels[4]  = new JLabel("  Parking comunitario ");
		labels[5] = new JLabel("  Garaje privado ");
		labels[6] = new JLabel("  Video portero ");
		
		combos = new JComboBox[7];
		String[] options = {"No", "Sí"};
		int i = 0;
		for (JLabel l: labels){
			pDatos.add(l);
			combos[i] = new JComboBox(options);
			pDatos.add(combos[i]);
			i++;
		}
		
		JScrollPane datosView = new JScrollPane(pDatos);

		JScrollPane nullView = new JScrollPane();
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.add(datosView);
		splitPane.add(nullView);
		
		Dimension minimumSize = new Dimension(170, 130);
        datosView.setMinimumSize(minimumSize);
        //arbolView.setMinimumSize(minimumSize);
        splitPane.setDividerLocation(420); 
        splitPane.setDividerSize(0);
        splitPane.setPreferredSize(new Dimension(500, 700));
 
        //Add the split pane to this panel.
        add(splitPane);
	}
	
	public ExtrasFinca getExtrasFinca(int id){
		// TODO: id de extras básicos y demás?
		ExtrasFinca extrasFinca = new ExtrasFinca(id);
		extrasFinca.setAscensor(combos[0].getSelectedItem().equals("Sí"));
		extrasFinca.setTrastero(combos[1].getSelectedItem().equals("Sí"));
		extrasFinca.setEnergiaSolar(combos[2].getSelectedItem().equals("Sí"));
		extrasFinca.setServPorteria(combos[3].getSelectedItem().equals("Sí"));
		extrasFinca.setParkingComunitario(combos[4].getSelectedItem().equals("Sí"));
		extrasFinca.setGarajePrivado(combos[5].getSelectedItem().equals("Sí"));
		extrasFinca.setVideoportero(combos[6].getSelectedItem().equals("Sí"));
		
		return extrasFinca;
	}
	
}
