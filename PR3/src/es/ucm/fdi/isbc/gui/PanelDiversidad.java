package es.ucm.fdi.isbc.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import es.ucm.fdi.isbc.controlador.Controlador;
import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda;

/**
 * Panel que contiene las viviendas con diversidad de contenido
 * 
 */

class PanelDiversidad extends JPanel {

	private static final long serialVersionUID = 1L;
	private Galeria galeria;
	
	private ArrayList<DescripcionVivienda> diversas;
	private ArrayList<Integer> idDiversas;
	private int[] imagen;
	
	private final int LENGTH_LABELS = 6;

	public PanelDiversidad(Galeria galeria) {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		this.galeria = galeria;
		imagen = new int[LENGTH_LABELS];

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
		for (DescripcionVivienda vivienda: viviendas){
			diversas.add(vivienda);
			idDiversas.add(vivienda.getId());
			galeria.addFoto(vivienda.getUrlFoto(), vivienda.getId());
		}
	}

}
