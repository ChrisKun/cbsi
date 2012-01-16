package es.ucm.fdi.isbc.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda;
import es.ucm.fdi.isbc.viviendas.representacion.RecomendadorVivienda;
import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda.EstadoVivienda;
import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda.TipoVivienda;

/**
 * Panel que contiene las viviendas con diversidad de contenido
 * 
 */

class PanelDiversidad extends JPanel {

	private static final long serialVersionUID = 1L;
//	private Galeria galeria;
	
	private ArrayList<DescripcionVivienda> diversas;
	private Galeria galeria;
	private JLabel label[] = new JLabel[RecomendadorVivienda.NUMDIVERSIDAD];
	
	private final int LENGTH_LABELS = 6;

	public PanelDiversidad(Galeria galeria) {
		setLayout(new GridLayout(2,3));

		this.galeria = galeria;
		diversas = new ArrayList<DescripcionVivienda>();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		dim.setSize(dim.width - 100, dim.height - 100);

		setMinimumSize(new Dimension((int) (dim.width * 0.73),
				(int) (dim.height * 0.78)));
		setSize(new Dimension((int) (dim.width * 0.73),
				(int) (dim.height * 0.78)));
	}

	// Una vez obtenidas las viviendas con diversidad de 3 a 5, meterlas en el
	// panel

	public void setViviendasDiversas(ArrayList<DescripcionVivienda> viviendas) {
		for (int i = 0; i < viviendas.size(); i++){
			diversas.add(viviendas.get(i));
//			idDiversas.add(viviendas.get(i).getId());
			label[i] = new JLabel(/*viviendas.get(i).getUrlFoto()*/);
			this.add(label[i]);
//			galeria.addFoto(vivienda.getUrlFoto(), vivienda.getId());
			DescripcionVivienda dV = diversas.get(i);
			String localización = "Madrid, ";
			String[] loca1 = dV.getLocalizacion().split("/");
			loca1[0] = loca1[loca1.length - 1].replaceAll("-", " ");
			localización += VentanaPpal.transformar(loca1[0].substring(0, 1)
					.toUpperCase() + loca1[0].substring(1));
			String mensaje = "<html>" + "Nombre:  " + dV.getTitulo() + "<br>"
					+ "Tipo:  " + tipoToString(dV.getTipo()) + "<br>"
					+ "Estado:  " + estadoToString(dV.getEstado()) + "<br>"
					+ "Localización:  " + localización + "<br>"
					+ "Superficie:  " + dV.getSuperficie()
					+ "m<sup>2</sup></html>";

			// Añadimos la foto del panel:
			int pos = galeria.addFoto(dV.getUrlFoto(), dV.getId());
			// La cargamos:
			label[i].setToolTipText(mensaje);
			label[i].setIcon(galeria.getFotoOrigPos(pos));
			label[i].setBorder(BorderFactory
					.createBevelBorder(BevelBorder.LOWERED));

			label[i].addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					boolean encontrado = false;
					ArrayList<Integer> idYaVisitadas = VentanaPpal.getIdDescViviendVisitadas();
					for (int j = 0; j < diversas.size() && !encontrado; j++)
						if (e.getSource() == label[j]) {
							if (!idYaVisitadas.contains(diversas.get(j).getId()))
								// La añadimos a visitados por haber pinchado
								// sobre ella. Y no en VentanaDescripcion
								VentanaPpal.addVivienda(diversas.get(j));
								VentanaPpal.actualizarPanel();
								VentanaPpal.lanzarVentanaDescripcion(diversas.get(j));
								encontrado = true;
							//dispose();
						}
				}
			});
			
		}
	}
	
	private String tipoToString(TipoVivienda tipo) {
		switch (tipo) {
		case Atico:
			return "Ático";
		case Plantabaja:
			return "Planta baja";
		case Casaadosada:
			return "Adosado";
		case CasaChalet:
			return "Chalet";
		case Duplex:
			return "Dúplex";
		case Fincarustica:
			return "Finca rústica";
		default:
			return tipo.toString();
		}
	}

	private String estadoToString(EstadoVivienda estado) {
		switch (estado) {
		case Muybien:
			return "Muy bien";
		case Areformar:
			return "A reformar";
		case Casinuevo:
			return "Casi nuevo";
		default:
			return estado.toString();
		}
	}

}
