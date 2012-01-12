package es.ucm.fdi.isbc.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda;
import es.ucm.fdi.isbc.viviendas.representacion.RecomendadorVivienda;

/**
 * Panel que contiene las viviendas con diversidad de contenido
 * 
 */

class PanelDiversidad extends JPanel {

	private static final long serialVersionUID = 1L;
//	private Galeria galeria;
	
	private ArrayList<DescripcionVivienda> diversas;
	private ArrayList<Integer> idDiversas;
	private int[] imagen;
	private JLabel label[] = new JLabel[RecomendadorVivienda.NUMDIVERSIDAD];
	
	private final int LENGTH_LABELS = 6;

	public PanelDiversidad(/*Galeria galeria*/) {
		setLayout(new GridLayout(2,3));

//		this.galeria = galeria;

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		dim.setSize(dim.width - 100, dim.height - 100);

		setMinimumSize(new Dimension((int) (dim.width * 0.73),
				(int) (dim.height * 0.78)));
		setSize(new Dimension((int) (dim.width * 0.73),
				(int) (dim.height * 0.78)));
	}

	// Una vez obtenidas las viviendas con diversidad de 3 a 5, meterlas en el
	// panel

	public void setViviendasDiversas(ArrayList<DescripcionVivienda> viviendas) {
		for (int i = 0; i < viviendas.size(); i++){
			diversas.add(viviendas.get(i));
			idDiversas.add(viviendas.get(i).getId());
			label[i] = new JLabel(viviendas.get(i).getUrlFoto());
			this.add(label[i]);
//			galeria.addFoto(vivienda.getUrlFoto(), vivienda.getId());
			
		}
	}

}
