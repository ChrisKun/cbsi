package es.ucm.fdi.isbc.gui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class PanelVisitados extends JPanel
{
	
	private static final long serialVersionUID = 1L;
	
	public PanelVisitados()
	{
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		//setMinimumSize(new Dimension(500, 100));
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		dim.setSize(dim.width - 100, dim.height - 100);
		
		setMinimumSize(new Dimension((int) (dim.width * 0.73), (int) (dim.height * 0.12)));
		setSize(new Dimension((int) (dim.width * 0.73), (int) (dim.height * 0.12)));
	}
	
}
