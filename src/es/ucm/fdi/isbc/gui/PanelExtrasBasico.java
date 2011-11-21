package es.ucm.fdi.isbc.gui;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import es.ucm.fdi.isbc.viviendas.representacion.ExtrasBasicos;

@SuppressWarnings("serial")
public class PanelExtrasBasico extends JPanel{
	
	private JLabel[] labels;
	private JComboBox combos[];
	
	public PanelExtrasBasico(){
		super();
		
		JPanel pDatos = new JPanel();
		GridLayout layout1 = new GridLayout(18,2);
		layout1.setVgap(18);
		pDatos.setLayout(layout1);
		pDatos.setPreferredSize(new Dimension(500, 700));
		
		labels = new JLabel[18];
		labels[0] = new JLabel("  Lavadero");
		labels[1]  = new JLabel("  Internet");
		labels[2]  = new JLabel("  Microondas");
		labels[3]  = new JLabel("  Horno ");
		labels[4]  = new JLabel("  Amueblado ");
		labels[5] = new JLabel("  Cocina Office ");
		labels[6] = new JLabel("  Parquet ");
		labels[7] = new JLabel("  Domótica ");
		labels[8]  = new JLabel("  Armarios ");
		labels[9]  = new JLabel("  Televisión ");
		labels[10]  = new JLabel("  Lavadora ");
		labels[11]  = new JLabel("  Electrodomésticos ");
		labels[12] = new JLabel("  Suite con baño ");
		labels[13] = new JLabel("  Puerta blindada ");
		labels[14] = new JLabel("  Gres cerámica ");
		labels[15]  = new JLabel("  Calefacción ");
		labels[16]  = new JLabel("  Aire acondicionado ");
		labels[17]  = new JLabel("  Nevera ");
		
		combos = new JComboBox[18];
		String[] options = {"No", "Sí"};
		int i = 0;
		for (JLabel l: labels){
			pDatos.add(l);
			combos[i] = new JComboBox(options);
			pDatos.add(combos[i]);
			i++;
		}
		
		JScrollPane datosView = new JScrollPane(pDatos);
        add(datosView);
		
	}
	
	public ExtrasBasicos getExtrasBasicos(int id){
		// TODO: id de extras básicos y demás?
		ExtrasBasicos extrasBasicos = new ExtrasBasicos(id);
		extrasBasicos.setLavadero(combos[0].getSelectedItem().equals("Sí"));
		extrasBasicos.setInternet(combos[1].getSelectedItem().equals("Sí"));
		extrasBasicos.setMicroondas(combos[2].getSelectedItem().equals("Sí"));
		extrasBasicos.setHorno(combos[3].getSelectedItem().equals("Sí"));
		extrasBasicos.setAmueblado(combos[4].getSelectedItem().equals("Sí"));
		extrasBasicos.setCocinaOffice(combos[5].getSelectedItem().equals("Sí"));
		extrasBasicos.setParquet(combos[6].getSelectedItem().equals("Sí"));
		extrasBasicos.setDomotica(combos[7].getSelectedItem().equals("Sí"));
		extrasBasicos.setArmarios(combos[8].getSelectedItem().equals("Sí"));
		extrasBasicos.setTv(combos[9].getSelectedItem().equals("Sí"));
		extrasBasicos.setLavadora(combos[10].getSelectedItem().equals("Sí"));
		extrasBasicos.setElectrodomesticos(combos[11].getSelectedItem().equals("Sí"));
		extrasBasicos.setSuiteConBanio(combos[12].getSelectedItem().equals("Sí"));
		extrasBasicos.setPuertaBlindada(combos[13].getSelectedItem().equals("Sí"));
		extrasBasicos.setGresCeramica(combos[14].getSelectedItem().equals("Sí"));
		extrasBasicos.setCalefaccion(combos[15].getSelectedItem().equals("Sí"));
		extrasBasicos.setAireAcondicionado(combos[16].getSelectedItem().equals("Sí"));
		extrasBasicos.setNevera(combos[17].getSelectedItem().equals("Sí"));
		
		return extrasBasicos;
	}

}
