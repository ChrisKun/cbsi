package es.ucm.fdi.isbc.gui;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;


public class PanelVisitados extends JPanel{
	private static final long serialVersionUID = 1L;
	
	public PanelVisitados(){
		BoxLayout box = new BoxLayout(this, BoxLayout.X_AXIS);
		setLayout(box);
		setMinimumSize(new Dimension(100, 87));
	}
	
}
