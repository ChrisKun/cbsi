package es.ucm.fdi.isbc.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import es.ucm.fdi.isbc.controlador.Controlador;
import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda;
import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda.EstadoVivienda;
import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda.TipoVivienda;

/**
 *  Panel que implementa el filtro de la consulta.
 *
 */

public class PanelFiltro extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private JComboBox tipoVivienda, estadoVivienda;
	private JLabel tipo, estado, localiz, metros;
	private JTextField localizacion, m;
	private JButton button;
	private JPanel[] panel;
	private DescripcionVivienda descr = null;
	
	public PanelFiltro(){
		this.setLayout(new GridLayout(5, 1));
		tipo = new JLabel("Tipo de Vivienda");
		estado = new JLabel("Estado vivienda");
		localiz = new JLabel("Localización");
		metros = new JLabel("Metros cuadrados");
		
		tipoVivienda = new JComboBox(TipoVivienda.values());
		// TODO insertar tipos de vivienda
		estadoVivienda = new JComboBox(EstadoVivienda.values());
		// TODO insertar estados de vivienda
		localizacion = new JTextField(20);
		m = new JTextField(20);
		
		panel = new JPanel[4];
		for (int i = 0; i < panel.length; i++){
			panel[i] = new JPanel();
		}
		
		panel[0].add(tipo); 
		panel[0].add(tipoVivienda);
		panel[1].add(estado); panel[1].add(estadoVivienda);
		panel[2].add(localiz); panel[2].add(localizacion);
		panel[3].add(metros); panel[3].add(m);
		
		button = new JButton("OK");
		button.addActionListener(this);
		button.setEnabled(false);
		JPanel minipanel = new JPanel();
		minipanel.add(button);
		
		for (JPanel p: panel){
			this.add(p);
		}
		this.add(minipanel);
		
		this.setMinimumSize(new Dimension(200, 175));
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand().equals("OK")){
			descr = new DescripcionVivienda(-1);
			descr.setTipo((TipoVivienda) tipoVivienda.getSelectedItem());
			descr.setEstado((EstadoVivienda) estadoVivienda.getSelectedItem());
			descr.setLocalizacion(localizacion.getText());
			try{
				descr.setSuperficie(Integer.parseInt(m.getText()));
				Controlador.getInstance().repite(descr);
			} catch (Exception e){
				JOptionPane.showMessageDialog(null, "Superficie incorrecta", "ERROR", JOptionPane.ERROR_MESSAGE); 
			}
		}
	}

	public void enableButton(boolean b) {
		button.setEnabled(b);
	}


}
