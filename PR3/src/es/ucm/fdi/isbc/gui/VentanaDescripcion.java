package es.ucm.fdi.isbc.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda;
import es.ucm.fdi.isbc.viviendas.representacion.ExtrasFinca;

public class VentanaDescripcion extends JDialog implements ItemListener
{

	/** Atributos **/

		/* Estáticos */

			private static final long serialVersionUID = 1L;
			private static JLabel label;
			private static JButton[] button;
			private static JEditorPane[] editorPane;
			private static DescripcionVivienda vivienda;
			private static ImageIcon imageIcon;
			
			JCheckBox ascensor;
			JCheckBox trastero;
			JCheckBox energiaSolar;
			JCheckBox servPorteria;
			JCheckBox parkingComunitario;
			JCheckBox garajePrivado;
			JCheckBox videoPortero;

	/** Constructores **/

		public VentanaDescripcion(DescripcionVivienda vivienda, ImageIcon imageIcon)
		{
			VentanaDescripcion.vivienda = vivienda;
			VentanaDescripcion.imageIcon = imageIcon;
			setTitle(vivienda.getTitulo());
			setModalityType(DEFAULT_MODALITY_TYPE);
			
			JPanel panel = new JPanel(new GridBagLayout());
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			button = new JButton[2];
			editorPane = new JEditorPane[2];

			// JLabel en la fila 0, columna 0 y que ocupa 4x5 celdas.
			label = new JLabel();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.gridwidth = 5;
			gridBagConstraints.gridheight = 4;
			gridBagConstraints.insets = new Insets(5, 5, 0, 5);
			gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
			Image image = imageIcon.getImage().getScaledInstance(300, 200, Image.SCALE_AREA_AVERAGING);
			label.setIcon(new ImageIcon(image));
			label.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			panel.add(label, gridBagConstraints);

			// JButton en la fila 5, columna 0 y que ocupa 1x1 celdas.
			button[0] = new JButton("Comprar");
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 5;
			gridBagConstraints.gridwidth = 1;
			gridBagConstraints.gridheight = 1;
			gridBagConstraints.insets = new Insets(5, 5, 2, 5);
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			panel.add(button[0], gridBagConstraints);

			// JButton en la fila 5, columna 1 y que ocupa 1x1 celdas
			button[1] = new JButton("Más como ésta");
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 5;
			gridBagConstraints.gridwidth = 1;
			gridBagConstraints.gridheight = 1;
			gridBagConstraints.insets = new Insets(5, 2, 2, 5);
			gridBagConstraints.anchor = GridBagConstraints.EAST;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			panel.add(button[1], gridBagConstraints);

			// JEditorPane en la fila 0, columna 6 y que ocupa 6x5 celdas
			editorPane[0] = new JEditorPane();
			gridBagConstraints.gridx = 5;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.gridwidth = 5;
			gridBagConstraints.gridheight = 6;
			gridBagConstraints.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints.anchor = GridBagConstraints.CENTER;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			panel.add(editorPane[0], gridBagConstraints);

			String localizacion = "Madrid, ";
			String[] loca = vivienda.getLocalizacion().split("/");
			loca[0] = loca[loca.length - 1].replaceAll("-", " ");
			localizacion += VentanaPpal.transformar(loca[0].substring(0, 1).toUpperCase() + loca[0].substring(1));

			String texto = "<html><font face = \"Arial\", size = 4>" +
							"<b>NOMBRE</b>: " + vivienda.getTitulo() + "<br><br>" +
							"<b>LOCALIZACIÓN</b>: " + localizacion + "<br><br>" +
							"<b>SUPERFICIE</b>: " + vivienda.getSuperficie() + "m<sup>2<sup><br><br>" +
							"<b>HABITACIONES</b>: " + vivienda.getHabitaciones() + "habitaciones<br><br>" +
							"<b>BAÑOS</b>: " + vivienda.getBanios() + "baños<br><br>" +
							"<b>PRECIO</b>: " + vivienda.getPrecio() + " €<br>";

			// Esto lo pongo así porque si queremos vender una casa no suele quedar bien decirle que le estamos vendiendo
				// una casa más cara que las de la zona al posible cliente.
			if (vivienda.getPrecioMedio() <= vivienda.getPrecioZona())
				texto += "<b>PRECIO MEDIO</u></b>: " + vivienda.getPrecioMedio() + "€<br>" +
							"<b>PRECIO ZONA</u></b>: " + vivienda.getPrecioZona() + "€</font></html>";
			else texto += "<b>PRECIO MEDIO</u></b>: sin datos.<br>" + "<b>PRECIO ZONA</u></b>: sin datos.</font></html>";

			editorPane[0].setContentType("text/html");
			editorPane[0].setOpaque(false);
			editorPane[0].setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			editorPane[0].setEnabled(false);
			editorPane[0].setDisabledTextColor(Color.black);
			editorPane[0].setText(texto);

			// JEditorPane en la fila 6, columna 0 y que ocupa 3x5 celdas
			editorPane[1] = new JEditorPane();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 6;
			gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
			gridBagConstraints.gridheight = 4;
			gridBagConstraints.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints.anchor = GridBagConstraints.CENTER;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			panel.add(editorPane[1], gridBagConstraints);

			editorPane[1].setContentType("text/html");
			editorPane[1].setOpaque(false);
			editorPane[1].setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			editorPane[1].setEnabled(false);
			editorPane[1].setDisabledTextColor(Color.black);
			editorPane[1].setText("<html><font face = \"Arial\", size = 4>" + vivienda.getDescripcion() + "</html>");

			// Meter las fincas en la fila 10, columna 0 y que ocupe 3x3 celdas
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 10;
			gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
			gridBagConstraints.gridheight = 5;
			gridBagConstraints.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			panel.add(getPanelFinca(), gridBagConstraints);

			// Hacerlo visible
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			setMinimumSize(new Dimension(700, 550));
			setLocation((dim.width - getWidth()) / 2, (dim.height - getHeight()) / 2);
			
			//pack();
			add(panel);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setVisible(true);
		}

	/** Métodos **/

		/* Getters */

			public DescripcionVivienda getVivienda()
			{
				return vivienda;
			}
			
			public ImageIcon getImageIcon()
			{
				return imageIcon;
			}

		/* Setters */

			public void setVivienda(DescripcionVivienda vivienda)
			{
				VentanaDescripcion.vivienda = vivienda;
			}
			
			public void setImageIcon(ImageIcon imageIcon)
			{
				VentanaDescripcion.imageIcon = imageIcon;
			}

		/* Métodos que implementan ItemListener */
			
			public void itemStateChanged(ItemEvent e)
			{
				if (e.getSource() == ascensor)
					ascensor.setSelected(vivienda.getExtrasFinca().isAscensor());
				else if (e.getSource() == trastero)
					trastero.setSelected(vivienda.getExtrasFinca().isTrastero());
				else if (e.getSource() == energiaSolar)
					energiaSolar.setSelected(vivienda.getExtrasFinca().isEnergiaSolar());
				else if (e.getSource() == servPorteria)
					servPorteria.setSelected(vivienda.getExtrasFinca().isServPorteria());
				else if (e.getSource() == parkingComunitario)
					parkingComunitario.setSelected(vivienda.getExtrasFinca().isServPorteria());
				else if (e.getSource() == garajePrivado)
					garajePrivado.setSelected(vivienda.getExtrasFinca().isGarajePrivado());
				else if (e.getSource() == videoPortero)
					videoPortero.setSelected(vivienda.getExtrasFinca().isVideoportero());
			}
			
		/* Otros métodos */

			public void actualizarVentanaDescripcion()
			{
				setTitle(vivienda.getTitulo());
				
				label.setIcon(VentanaPpal.redimensionarImageIcon(imageIcon.getImage(), 300, 200));
				
				String localizacion = "Madrid, ";
				String[] loca = vivienda.getLocalizacion().split("/");
				loca[0] = loca[loca.length - 1].replaceAll("-", " ");
				localizacion += VentanaPpal.transformar(loca[0].substring(0, 1).toUpperCase() + loca[0].substring(1));

				String texto = "<html><font face = \"Arial\", size = 4>" +
								"<b>NOMBRE</b>: " + vivienda.getTitulo() + "<br><br>" +
								"<b>LOCALIZACIÓN</b>: " + localizacion + "<br><br>" +
								"<b>SUPERFICIE</b>: " + vivienda.getSuperficie() + "m<sup>2<sup><br><br>" +
								"<b>HABITACIONES</b>: " + vivienda.getHabitaciones() + "habitaciones<br><br>" +
								"<b>BAÑOS</b>: " + vivienda.getBanios() + "baños<br><br>" +
								"<b>PRECIO</b>: " + vivienda.getPrecio() + " €<br>";

				if (vivienda.getPrecioMedio() <= vivienda.getPrecioZona())
					texto += "<b>PRECIO MEDIO</u></b>: " + vivienda.getPrecioMedio() + "€<br>" +
								"<b>PRECIO ZONA</u></b>: " + vivienda.getPrecioZona() + "€</font></html>";
				else texto += "<b>PRECIO MEDIO</u></b>: sin datos.<br>" + "<b>PRECIO ZONA</u></b>: sin datos.</font></html>";

				editorPane[0].setText(texto);
				editorPane[1].setText("<html><font face = \"Arial\", size = 4>" + vivienda.getDescripcion() + "</html>");
			}

		/* AUXILIARES */

			private JPanel getPanelFinca()
			{
				JPanel panel = new JPanel();
				panel.setLayout(new GridLayout(3, 3));

				ascensor = new JCheckBox("Ascensor");
				trastero = new JCheckBox("Trastero");
				energiaSolar = new JCheckBox("Energía solar");
				servPorteria = new JCheckBox("Servicio de portería");
				parkingComunitario = new JCheckBox("Parking comunitario");
				garajePrivado = new JCheckBox("Garaje privado");
				videoPortero = new JCheckBox("Vídeo-Portero");

				ExtrasFinca eF = vivienda.getExtrasFinca();
				ascensor.setSelected(eF.isAscensor());
				trastero.setSelected(eF.isTrastero());
				energiaSolar.setSelected(eF.isEnergiaSolar());
				servPorteria.setSelected(eF.isServPorteria());
				parkingComunitario.setSelected(eF.isParkingComunitario());
				garajePrivado.setSelected(eF.isGarajePrivado());
				videoPortero.setSelected(eF.isVideoportero());

				ascensor.setEnabled(true);
				trastero.setEnabled(true);
				energiaSolar.setEnabled(true);
				servPorteria.setEnabled(true);
				parkingComunitario.setEnabled(true);
				garajePrivado.setEnabled(true);
				videoPortero.setEnabled(true);
				
				ascensor.addItemListener(this);
				trastero.addItemListener(this);
				energiaSolar.addItemListener(this);
				servPorteria.addItemListener(this);
				parkingComunitario.addItemListener(this);
				garajePrivado.addItemListener(this);
				videoPortero.addItemListener(this);

				panel.add(ascensor);
				panel.add(trastero);
				panel.add(energiaSolar);
				panel.add(servPorteria);
				panel.add(parkingComunitario);
				panel.add(garajePrivado);
				panel.add(videoPortero);

				return panel;
			}
			
			/**
			 * Hay que hacer lo mismo que en getPanelFinca
			 * @return panelBasicos
			 */
			@SuppressWarnings("unused")
			private JPanel getPanelBasicos()
			{
				return null;
			}
			
			/**
			 * Hay que hacer lo mismo que en getPanelFinca
			 * @return panelOtros
			 */
			@SuppressWarnings("unused")
			private JPanel getPanelOtros()
			{
				return null;
			}

}
