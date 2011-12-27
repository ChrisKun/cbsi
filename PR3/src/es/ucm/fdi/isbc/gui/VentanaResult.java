package es.ucm.fdi.isbc.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda;

public class VentanaResult extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel p;

	public VentanaResult(){
		super("Viviendas");
		
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		p = new JPanel();
		
        JScrollPane scrollPane = new JScrollPane(p);
        this.add(scrollPane);
		
		setPreferredSize(new Dimension(800,600));
		int x=(int) (Toolkit.getDefaultToolkit().getScreenSize().width/2-this.getPreferredSize().width/2);
		int y=(int) (Toolkit.getDefaultToolkit().getScreenSize().height/2-this.getPreferredSize().height/2);
		setLocation(x, y);
		
		this.setSize(new Dimension(800,600));
	}
	
	/**
	 * Cuando hayamos obtenido el resultado con la lista de viviendas similares..
	 * @param descrs
	 */
	public void setResultado(ArrayList<DescripcionVivienda> descrs){
		p.setLayout(new GridLayout(descrs.size(), 1));
		// Paneles de viviendas
		JPanel panel[] = new JPanel[descrs.size()];
		// Etiquetas viviendas
		JLabel label[] = new JLabel[descrs.size()];
		JLabel imagen[] = new JLabel[descrs.size()];
		
		for (int i = 0; i < descrs.size(); i++){
			panel[i] = new JPanel();
			imagen[i] = new JLabel(new ImageIcon(descrs.get(i).getUrlFoto()));
			label[i] = new JLabel(descrs.get(i).getTitulo() + 
					"\n" + descrs.get(i).getLocalizacion() +
					"\n" + descrs.get(i).getDescripcion());
			panel[i].add(imagen[i]); 
			panel[i].add(label[i]);
			p.add(panel[i]);
		}
		this.add(p);
		this.setVisible(true);
		
	}

}
