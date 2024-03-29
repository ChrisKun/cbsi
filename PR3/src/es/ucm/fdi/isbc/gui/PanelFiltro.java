package es.ucm.fdi.isbc.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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
import es.ucm.fdi.isbc.viviendas.representacion.RecomendadorVivienda;

/**
 * Panel que implementa el filtro de la consulta.
 * 
 */
class PanelFiltro extends Container implements KeyListener {

	/** Atributos **/

	private static final long serialVersionUID = 1L;

	private JComboBox tipoVivienda, estadoVivienda;
	private JLabel tipo, estado, localiz, metros;
	private JTextField localizacion, m;
	private JButton button;
	private TipoVivienda type = TipoVivienda.Piso;
	private EstadoVivienda state = EstadoVivienda.Muybien;

	private DescripcionVivienda descr = null;

	/** Constructores **/

	public PanelFiltro(VentanaPpal v) {
		this.setLayout(new GridLayout(5, 1));

		tipo = new JLabel("Tipo de Vivienda");
		estado = new JLabel("Estado vivienda");
		localiz = new JLabel("Localizaci�n");
		metros = new JLabel("<html>Superficie (m<sup>2</sup>)</html>");

		// insertar tipos de vivienda
		tipoVivienda = new JComboBox();
		tipoVivienda.addItem("Adosado");
		tipoVivienda.addItem("Apartamento");
		tipoVivienda.addItem("�tico");
		tipoVivienda.addItem("Chalet");
		tipoVivienda.addItem("D�plex");
		tipoVivienda.addItem("Estudio");
		tipoVivienda.addItem("Finca r�stica");
		tipoVivienda.addItem("Loft");
		tipoVivienda.addItem("Piso");
		tipoVivienda.addItem("Planta baja");
		tipoVivienda.setSelectedItem("Piso");
		tipoVivienda.addKeyListener(this);

		// insertar estados de vivienda
		estadoVivienda = new JComboBox();
		estadoVivienda.addItem("Muy bien");
		estadoVivienda.addItem("Casi nuevo");
		estadoVivienda.addItem("Reformado");
		estadoVivienda.addItem("Bien");
		estadoVivienda.addItem("A reformar");
		estadoVivienda.addKeyListener(this);

		localizacion = new JTextField(20);
		localizacion.addKeyListener(this);
		m = new JTextField(20);
		m.addKeyListener(this);

		JLabel[] label = new JLabel[10];
		for (int i = 0; i < 10; i++)
			label[i] = new JLabel();

		JPanel panel = new JPanel(new GridLayout(9, 2, 10, 0));

		panel.add(label[0]);
		panel.add(label[1]);
		panel.add(tipo);
		panel.add(tipoVivienda);
		panel.add(label[2]);
		panel.add(label[3]);
		panel.add(estado);
		panel.add(estadoVivienda);
		panel.add(label[4]);
		panel.add(label[5]);
		panel.add(localiz);
		panel.add(localizacion);
		panel.add(label[6]);
		panel.add(label[7]);
		panel.add(metros);
		panel.add(m);
		panel.add(label[8]);
		panel.add(label[9]);

		button = new JButton("OK");
		button.setEnabled(false);
		button.addKeyListener(this);
		JPanel minipanel = new JPanel();
		minipanel.add(button);

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		dim.setSize(dim.width - 100, dim.height - 100);

		setMinimumSize(new Dimension((int) (dim.width * 0.25), dim.height));
		setSize(new Dimension((int) (dim.width * 0.25), dim.height));

		this.add(panel);
		this.add(minipanel);

		tipoVivienda.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				String seleccionado = (String) tipoVivienda.getSelectedItem();
				if (seleccionado.equals("Adosado"))
					type = TipoVivienda.Casaadosada;
				else if (seleccionado.equals("Apartamento"))
					type = TipoVivienda.Apartamento;
				else if (seleccionado.equals("�tico"))
					type = TipoVivienda.Atico;
				else if (seleccionado.equals("Chalet"))
					type = TipoVivienda.CasaChalet;
				else if (seleccionado.equals("D�plex"))
					type = TipoVivienda.Duplex;
				else if (seleccionado.equals("Estudio"))
					type = TipoVivienda.Estudio;
				else if (seleccionado.equals("Finca r�stica"))
					type = TipoVivienda.Fincarustica;
				else if (seleccionado.equals("Loft"))
					type = TipoVivienda.Loft;
				else if (seleccionado.equals("Piso"))
					type = TipoVivienda.Piso;
				else
					type = TipoVivienda.Plantabaja;
			}
		});

		estadoVivienda.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				String seleccionado = (String) estadoVivienda.getSelectedItem();
				if (seleccionado.equals("Muy bien"))
					state = EstadoVivienda.Muybien;
				else if (seleccionado.equals("Casi nuevo"))
					state = EstadoVivienda.Casinuevo;
				else if (seleccionado.equals("Reformado"))
					state = EstadoVivienda.Reformado;
				else if (seleccionado.equals("Bien"))
					state = EstadoVivienda.Bien;
				else
					state = EstadoVivienda.Areformar;
			}
		});

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Runnable runnable = new Runnable() {
					public void run() {
						accionDelBoton();
					}
				};
				Thread hilo = new Thread(runnable);
				hilo.start();
			}
		});
	}

	/* M�todos que implementan la interfaz KeyListener */

	public void keyPressed(KeyEvent e) {
		;
	}

	public void keyReleased(KeyEvent e) {
		if (e.getSource() == button || e.getSource() == tipo
				|| e.getSource() == estadoVivienda
				|| e.getSource() == localizacion || e.getSource() == m) {
			if (e.getKeyCode() == 10) {
				Runnable runnable = new Runnable() {
					public void run() {
						accionDelBoton();
					}
				};
				Thread hilo = new Thread(runnable);
				hilo.start();
			}
		}
	}

	public void keyTyped(KeyEvent e) {
		;
	}

	/* AUXILIARES */

	public void enableButton(boolean b) {
		button.setEnabled(b);
	}

	private void accionDelBoton() {
		String sLocalizacion = localizacion.getText().toLowerCase()
				.replaceAll(" ", "-");
		if (sLocalizacion != null && !sLocalizacion.isEmpty()
				&& !sLocalizacion.contains("/"))
			sLocalizacion = RecomendadorVivienda.tree.getPath(sLocalizacion)
					.rutaToString();
		else if (sLocalizacion == null || sLocalizacion.isEmpty())
			sLocalizacion = null;

		descr = new DescripcionVivienda(-1);
		descr.setTipo(type);
		descr.setEstado(state);
		descr.setLocalizacion(sLocalizacion);
		String entero = m.getText();
		if (VentanaPpal.enteroEsCorrecto(entero)) {
			if (!entero.isEmpty())
				descr.setSuperficie(Integer.valueOf(entero));
			Controlador.getInstance().repite(descr);
		} else
			JOptionPane.showMessageDialog(null, "Superficie incorrecta",
					"ERROR", JOptionPane.ERROR_MESSAGE);
	}

}
