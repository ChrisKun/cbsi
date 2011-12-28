package es.ucm.fdi.isbc.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

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

public class PanelFiltro extends JPanel
{
	
	private static final long serialVersionUID = 1L;
	
	private static JComboBox tipoVivienda, estadoVivienda;
	private static JLabel tipo, estado, localiz, metros;
	private static JTextField localizacion, m;
	private static JButton button;
	private static JPanel panel;
	private static TipoVivienda type = TipoVivienda.Casaadosada;
	private static EstadoVivienda state = EstadoVivienda.Muybien;
	
	private DescripcionVivienda descr = null;
	
	public PanelFiltro()
	{
		this.setLayout(new GridLayout(5, 1));

		tipo = new JLabel("Tipo de Vivienda");
		estado = new JLabel("Estado vivienda");
		localiz = new JLabel("Localización");
		metros = new JLabel("<html>Superficie (m<sup>2</sup>)</html>");
		
		// insertar tipos de vivienda
		tipoVivienda = new JComboBox();
		tipoVivienda.addItem("Adosado");
		tipoVivienda.addItem("Apartamento");
		tipoVivienda.addItem("Ático");
		tipoVivienda.addItem("Chalet");
		tipoVivienda.addItem("Dúplex");
		tipoVivienda.addItem("Estudio");
		tipoVivienda.addItem("Finca rústica");
		tipoVivienda.addItem("Loft");
		tipoVivienda.addItem("Piso");
		tipoVivienda.addItem("Planta baja");
		
		// insertar estados de vivienda
		estadoVivienda = new JComboBox();
		estadoVivienda.addItem("Muy bien");
		estadoVivienda.addItem("Casi nuevo");
		estadoVivienda.addItem("Reformado");
		estadoVivienda.addItem("Bien");
		estadoVivienda.addItem("A reformar");
		
		localizacion = new JTextField(20);
		m = new JTextField(20);
		
		JLabel[] label = new JLabel[10];
		for (int i = 0; i < 10; i++)
			label[i] = new JLabel();
		
		panel = new JPanel();
		panel.setLayout(new GridLayout(9, 2, 10, 0));
		
		panel.add(label[0]);	panel.add(label[1]);
		panel.add(tipo);		panel.add(tipoVivienda);
		panel.add(label[2]);	panel.add(label[3]);
		panel.add(estado);		panel.add(estadoVivienda);
		panel.add(label[4]);	panel.add(label[5]);
		panel.add(localiz);		panel.add(localizacion);
		panel.add(label[6]);	panel.add(label[7]);
		panel.add(metros);		panel.add(m);
		panel.add(label[8]);	panel.add(label[9]);		
		
		button = new JButton("OK");
		button.setEnabled(false);
		JPanel minipanel = new JPanel();
		minipanel.add(button);
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		dim.setSize(dim.width - 100, dim.height - 100);
		
		setMinimumSize(new Dimension((int) (dim.width * 0.25), dim.height));
		setSize(new Dimension((int) (dim.width * 0.25), dim.height));
		
		this.add(panel);
		this.add(minipanel);

		tipoVivienda.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent arg0)
			{
				String seleccionado = (String) tipoVivienda.getSelectedItem();
				if (seleccionado.equals("Adosado")) type = TipoVivienda.Casaadosada;
				else if (seleccionado.equals("Apartamento")) type = TipoVivienda.Apartamento;
				else if (seleccionado.equals("Ático")) type = TipoVivienda.Atico;
				else if (seleccionado.equals("Chalet")) type = TipoVivienda.CasaChalet;
				else if (seleccionado.equals("Dúplex")) type = TipoVivienda.Duplex;
				else if (seleccionado.equals("Estudio")) type = TipoVivienda.Estudio;
				else if (seleccionado.equals("Finca rústica")) type = TipoVivienda.Fincarustica;
				else if (seleccionado.equals("Loft")) type = TipoVivienda.Loft;
				else if (seleccionado.equals("Piso")) type = TipoVivienda.Piso;
				else type = TipoVivienda.Plantabaja;
			}
		}
		);
		
		estadoVivienda.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				String seleccionado = (String) estadoVivienda.getSelectedItem();
				if (seleccionado.equals("Muy bien")) state = EstadoVivienda.Muybien;
				else if (seleccionado.equals("Casi nuevo")) state = EstadoVivienda.Casinuevo;
				else if (seleccionado.equals("Reformado")) state = EstadoVivienda.Reformado;
				else if (seleccionado.equals("Bien")) state = EstadoVivienda.Bien;
				else state = EstadoVivienda.Areformar;
			}
		}
		);
		
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				descr = new DescripcionVivienda(-1);
				descr.setTipo(type);
				descr.setEstado(state);
				descr.setLocalizacion(localizacion.getText());
				String entero = m.getText();
				if (enteroEsCorrecto(entero)) {
					if (!entero.isEmpty()) descr.setSuperficie(Integer.valueOf(entero));
					Controlador.getInstance().repite(descr);
				}
				else JOptionPane.showMessageDialog(null, "Superficie incorrecta", "ERROR", JOptionPane.ERROR_MESSAGE); 
			}
		}
		);
	}

	/* AUXILIARES */
	
		public void enableButton(boolean b)
		{
			button.setEnabled(b);
		}
	
		private boolean enteroEsCorrecto(String entero)
		{
			/** Para que el entero sea correcto tiene que tener formato de Integer y ser mayor o igual
			 * que cero ya que la superficie, el número de habitaciones y de baños no puede ser negativo.
			 * Si la cadena es vacía devolvemos true;
			 */
			
			if (entero.isEmpty()) return true;
			try {
				Integer ent = Integer.valueOf(entero);
				return ent >= 0;
			}
			catch (NumberFormatException e) {
				return false;
			}
		}

}
