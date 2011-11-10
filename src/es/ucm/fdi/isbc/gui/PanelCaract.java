package es.ucm.fdi.isbc.gui;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda.EstadoVivienda;
import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda.TipoVivienda;

@SuppressWarnings("serial")
public class PanelCaract extends JPanel{
	private JTextArea[] textAreas;
	private JLabel[] labels;
	private JComboBox tipoVivienda, estadoVivienda;
	
	public PanelCaract(){
		super();
		setLayout(new GridLayout(8,2));
		JLabel tipoLabel = new JLabel("Tipo vivienda ");
		JLabel estadoLabel = new JLabel("Estado vivienda ");
		tipoVivienda = new JComboBox(TipoVivienda.values());
		estadoVivienda = new JComboBox(EstadoVivienda.values());
		add(tipoLabel); add(tipoVivienda);
		add(estadoLabel); add(estadoVivienda);
		labels = new JLabel[6];
		labels[0] = new JLabel("Superficie ");
		labels[1]  = new JLabel("Habitaciones ");
		labels[2]  = new JLabel("Baños ");
		labels[3]  = new JLabel("Precio medio ");
		labels[4]  = new JLabel("Precio zona ");
		labels[5] = new JLabel("Localización ");
		
		textAreas = new JTextArea[6];
		int i = 0;
		for (JLabel l: labels){
			add(l);
			textAreas[i] = new JTextArea(1,20);
			textAreas[i].setBorder(BorderFactory.createLineBorder(Color.black));
			add(textAreas[i]);
			i++;
		} 
		
	}
}
