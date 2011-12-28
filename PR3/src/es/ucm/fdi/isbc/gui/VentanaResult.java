package es.ucm.fdi.isbc.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.plaf.BorderUIResource;

import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda;

public class VentanaResult extends JFrame
{
	private static final long serialVersionUID = 1L;
	private static JPanel p;
	private static Dimension dim;

	public VentanaResult()
	{
		
		super("Viviendas");
		
		p = new JPanel();
		
        JScrollPane scrollPane = new JScrollPane(p);
        this.add(scrollPane);
		
		dim = Toolkit.getDefaultToolkit().getScreenSize();
		setPreferredSize(new Dimension(800, 600));
		setSize(dim.width - 150, dim.height - 150);
		setLocation((dim.width - getWidth()) / 2, (dim.height - getHeight()) / 2);
		
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
	}

	/**
	 * Cuando hayamos obtenido el resultado con la lista de viviendas similares..
	 * @param descrs
	 */

	public void setResultado(ArrayList<DescripcionVivienda> descrs)
	{
		p.setLayout(new GridLayout(descrs.size(), 2, 20, 20));
		
		for (int i = 0; i < descrs.size(); i++) {
			
			String localización = "Madrid, ";
			String[] loca = descrs.get(i).getLocalizacion().split("/");
			loca[0] = loca[loca.length - 1].replaceAll("-", " ");
			localización += loca[0].substring(0, 1).toUpperCase() + loca[0].substring(1);
			System.out.println(localización);
			
			String descr =	"<html><p align=\"justify\"><b><u>NOMBRE</u></b>: " + descrs.get(i).getTitulo() + "<br>" +
					"<b><u>LOCALIZACIÓN</u></b>: " + localización + "<br>" +
					"<b><u>PRECIO</u></b>: " + descrs.get(i).getPrecio() + " €<br>" +
					"<b><u>DESCRIPCIÓN</u></b>: " + descrs.get(i).getDescripcion() + "</p></html>";			
			
			JLabel imagen = new JLabel(" ");
			JLabel label = new JLabel(descr);
			
			try {
				imagen.setIcon(new ImageIcon(new URL(descrs.get(i).getUrlFoto())));
				imagen.setBorder(BorderUIResource.getEtchedBorderUIResource());
				imagen.setMinimumSize(new Dimension(50, 50));
				imagen.setMaximumSize(new Dimension(50, 50));
			}
			catch (MalformedURLException e) {
				System.out.println("Error");
				e.printStackTrace();
			}

			p.add(imagen);
			p.add(label);
			
		}
		
		this.add(p);
		this.setVisible(true);
	}

}
