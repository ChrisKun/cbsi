package es.ucm.fdi.isbc.gui;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import es.ucm.fdi.isbc.viviendas.representacion.ExtrasOtros;

@SuppressWarnings("serial")
public class PanelExtrasOtros extends JPanel{

	private JLabel[] labels;
	private JComboBox combos[];
	
	public PanelExtrasOtros(){
		super();
		
		JPanel pDatos = new JPanel();
		GridLayout layout1 = new GridLayout(9,2);
		layout1.setVgap(18);
		pDatos.setLayout(layout1);

		labels = new JLabel[9];
		labels[0] = new JLabel("  Patio ");
		labels[1]  = new JLabel("  Balcón ");
		labels[2]  = new JLabel("  Zona deportiva ");
		labels[3]  = new JLabel("  Zona comunitaria ");
		labels[4]  = new JLabel("  Terraza ");
		labels[5] = new JLabel("  Piscina comunitaria ");
		labels[6] = new JLabel("  Jardín privado ");
		labels[7] = new JLabel("  Zona infantil ");
		labels[8]  = new JLabel("  Piscina ");
		
		combos = new JComboBox[9];
		String[] options = {"No", "Sí"};
		int i = 0;
		for (JLabel l: labels){
			pDatos.add(l);
			combos[i] = new JComboBox(options);
			pDatos.add(combos[i]);
			i++;
		}
		

		JScrollPane nullView = new JScrollPane();
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.add(pDatos);
		splitPane.add(nullView);
		
		Dimension minimumSize = new Dimension(170, 130);
        pDatos.setMinimumSize(minimumSize);
        //arbolView.setMinimumSize(minimumSize);
        splitPane.setDividerLocation(420); 
        splitPane.setDividerSize(0);
        splitPane.setPreferredSize(new Dimension(500, 700));
 
        //Add the split pane to this panel.
        add(splitPane);
	}
	
	public ExtrasOtros getExtrasOtros(int id){
		// TODO: id de extras básicos y demás?
		ExtrasOtros extrasOtros = new ExtrasOtros(id);
		extrasOtros.setPatio(combos[0].getSelectedItem().equals("Sí"));
		extrasOtros.setBalcon(combos[1].getSelectedItem().equals("Sí"));
		extrasOtros.setZonaDeportiva(combos[2].getSelectedItem().equals("Sí"));
		extrasOtros.setZonaComunitaria(combos[3].getSelectedItem().equals("Sí"));
		extrasOtros.setTerraza(combos[4].getSelectedItem().equals("Sí"));
		extrasOtros.setPiscinaComunitaria(combos[5].getSelectedItem().equals("Sí"));
		extrasOtros.setJardinPrivado(combos[6].getSelectedItem().equals("Sí"));
		extrasOtros.setZonaInfantil(combos[7].getSelectedItem().equals("Sí"));
		extrasOtros.setPiscina(combos[8].getSelectedItem().equals("Sí"));
		
		return extrasOtros;
	}
}
