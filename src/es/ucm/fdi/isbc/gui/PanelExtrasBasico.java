package es.ucm.fdi.isbc.gui;

import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PanelExtrasBasico extends JPanel{
	
	private JLabel[] labels;
	private JComboBox combos[];
	
	public PanelExtrasBasico(){
		super();
		setLayout(new GridLayout(18,2));
		
		labels = new JLabel[18];
		labels[0] = new JLabel("Lavadero ");
		labels[1]  = new JLabel("Internet ");
		labels[2]  = new JLabel("Microondas ");
		labels[3]  = new JLabel("Horno ");
		labels[4]  = new JLabel("Amueblado ");
		labels[5] = new JLabel("Cocina Office ");
		labels[6] = new JLabel("Parquet ");
		labels[7] = new JLabel("Domótica ");
		labels[8]  = new JLabel("Armarios ");
		labels[9]  = new JLabel("Televisión ");
		labels[10]  = new JLabel("Lavadora ");
		labels[11]  = new JLabel("Electrodomésticos ");
		labels[12] = new JLabel("Suite con baño ");
		labels[13] = new JLabel("Puerta blindada ");
		labels[14] = new JLabel("Gres cerámica ");
		labels[15]  = new JLabel("Calefacción ");
		labels[16]  = new JLabel("Aire acondicionado ");
		labels[17]  = new JLabel("Nevera ");
		
		combos = new JComboBox[18];
		String[] options = {"No", "Sí"};
		int i = 0;
		for (JLabel l: labels){
			add(l);
			combos[i] = new JComboBox(options);
			add(combos[i]);
			i++;
		}
	}

}
