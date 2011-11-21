package es.ucm.fdi.isbc.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda;

public class VentanaResult extends JInternalFrame {
	
	private JTextField[] textAreas;
	private JLabel[] labels;
	
	public VentanaResult(){
		super();
		setPreferredSize(new Dimension(500, 600));
		GridLayout layout1 = new GridLayout(12,2);
		layout1.setVgap(18);
		setLayout(layout1);
		
		labels = new JLabel[12];
		labels[0]  = new JLabel("  Tipo vivienda ");
		labels[1]  = new JLabel("  Estado vivienda ");
		labels[2]  = new JLabel("  Superficie ");
		labels[3]  = new JLabel("  Habitaciones ");
		labels[4]  = new JLabel("  Baños ");
		labels[5]  = new JLabel("  Precio medio ");
		labels[6]  = new JLabel("  Precio zona ");
		labels[7]  = new JLabel("  Localizacion ");
		labels[8]  = new JLabel("  Latitud ");
		labels[9]  = new JLabel("  Longitud ");
		labels[10] = new JLabel("  Precio Vivienda -->");
		labels[11] = new JLabel("  Confianza");
		labels[10].setBackground(new Color(0,0,255));
	
		textAreas = new JTextField[12];
		int i = 0;
		for (JLabel l: labels){
			add(l);
			textAreas[i] = new JTextField(20);
			textAreas[i].setText("");
			textAreas[i].setBorder(BorderFactory.createLineBorder(Color.black));
			add(textAreas[i]);
			i++;
		}
	}
	
	public void setDescripcion(DescripcionVivienda descrip){
		
		textAreas[0].setText(descrip.getTipo().toString());
		textAreas[1].setText(descrip.getEstado().toString());
		textAreas[2].setText(String.valueOf(descrip.getSuperficie()));
		textAreas[3].setText(String.valueOf(descrip.getHabitaciones()));
		textAreas[4].setText(String.valueOf(descrip.getBanios()));
		textAreas[5].setText(String.valueOf(descrip.getPrecioMedio()));
		textAreas[6].setText(String.valueOf(descrip.getPrecioZona()));
		textAreas[7].setText(String.valueOf(descrip.getLocalizacion()));
		textAreas[8].setText(String.valueOf(descrip.getCoordenada().getLatitud()));
		textAreas[9].setText(String.valueOf(descrip.getCoordenada().getLongitud()));
		textAreas[10].setText(String.valueOf(descrip.getPrecio()));
		textAreas[11].setText("");
		
	}
	
	public void setSolucion(DescripcionVivienda descrip, int precio, double confianza){
		
		if(descrip.getTipo() != null)
			textAreas[0].setText(descrip.getTipo().toString());
		else textAreas[0].setText("");
		if(descrip.getEstado() != null)
			textAreas[1].setText(descrip.getEstado().toString());
		else textAreas[1].setText("");
		if(descrip.getSuperficie() != null)
			textAreas[2].setText(String.valueOf(descrip.getSuperficie()));
		else textAreas[2].setText("");
		if(descrip.getHabitaciones() != null)
			textAreas[3].setText(String.valueOf(descrip.getHabitaciones()));
		else textAreas[3].setText("");
		if(descrip.getBanios() != null)
			textAreas[4].setText(String.valueOf(descrip.getBanios()));
		else textAreas[4].setText("");
		if(descrip.getPrecioMedio() != null)
			textAreas[5].setText(String.valueOf(descrip.getPrecioMedio()));
		else textAreas[5].setText("");
		if(descrip.getPrecioZona() != null)
			textAreas[6].setText(String.valueOf(descrip.getPrecioZona()));
		else textAreas[6].setText("");
		if(descrip.getLocalizacion() != null)
			textAreas[7].setText(String.valueOf(descrip.getLocalizacion()));
		else textAreas[7].setText("");
		if(descrip.getCoordenada() != null){
			textAreas[8].setText(String.valueOf(descrip.getCoordenada().getLatitud()));
			textAreas[9].setText(String.valueOf(descrip.getCoordenada().getLongitud()));
		}
		else{
			textAreas[8].setText("");
			textAreas[9].setText("");
		}
			
		textAreas[10].setText(String.valueOf(precio));
		textAreas[11].setText(String.valueOf(confianza));
	
	}

}
