package es.ucm.fdi.isbc.gui;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 *  Panel que contiene las viviendas con diversidad de contenido
 *
 */

public class PanelDiversidad extends JPanel{
	private static final long serialVersionUID = 1L;
	
	public PanelDiversidad(){
		BoxLayout box = new BoxLayout(this, BoxLayout.X_AXIS);
		setLayout(box);
		setMinimumSize(new Dimension(100, 200));
	}
	
	// TODO una vez obtenidas las viviendas con diversidad de 3 a 5, meterlas en el panel
	public void setViviendasDiversas(){
		
	}

}
