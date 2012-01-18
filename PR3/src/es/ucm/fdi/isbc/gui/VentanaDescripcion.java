package es.ucm.fdi.isbc.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

import jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.isbc.controlador.Controlador;
import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda;
import es.ucm.fdi.isbc.viviendas.representacion.ExtrasBasicos;
import es.ucm.fdi.isbc.viviendas.representacion.ExtrasFinca;
import es.ucm.fdi.isbc.viviendas.representacion.ExtrasOtros;

public class VentanaDescripcion extends JDialog implements ItemListener {
	private static final long serialVersionUID = 1L;
	/** Atributos **/

	private JLabel label;
	private JButton[] button;
	private JEditorPane[] editorPane;
	private DescripcionVivienda vivienda;
	private ImageIcon imageIcon;

	private JCheckBox ascensor, trastero, energiaSolar, servPorteria,
			parkingComunitario, garajePrivado, videoPortero;

	private JCheckBox lavadero, internet, microondas, horno, amueblado,
			cocinaOffice, parquet, domotica, armarios, tv, lavadora,
			electrodomesticos, suiteConBanio, puertaBlindada, gresCeramica,
			calefaccion, aireAcondicionado, nevera, patio, balcon,
			zonaDeportiva, zonaComunitaria, terraza, piscinaComunitaria,
			jardinPrivado, zonaInfantil, piscina;

	/** Constructores **/

	public VentanaDescripcion(final DescripcionVivienda VIVIENDA,
			final int INDEX, Galeria galeria) {
		vivienda = VIVIENDA;
		imageIcon = galeria.getFotoOrig(VIVIENDA.getId());
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
		Image image = imageIcon.getImage().getScaledInstance(300, 200,
				Image.SCALE_AREA_AVERAGING);
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

		// JEditorPane en la fila 0, columna 5 y que ocupa 6x5 celdas
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
		localizacion += VentanaPpal.transformar(loca[0].substring(0, 1)
				.toUpperCase() + loca[0].substring(1));

		String texto = "<html><font face = \"Arial\", size = 4>"
				+ "<b>NOMBRE</b>: " + vivienda.getTitulo() + "<br><br>"
				+ "<b>LOCALIZACIÓN</b>: " + localizacion + "<br><br>"
				+ "<b>SUPERFICIE</b>: " + vivienda.getSuperficie()
				+ "m<sup>2<sup><br><br>" + "<b>HABITACIONES</b>: "
				+ vivienda.getHabitaciones() + "habitaciones<br><br>"
				+ "<b>BAÑOS</b>: " + vivienda.getBanios() + "baños<br><br>"
				+ "<b>PRECIO</b>: " + vivienda.getPrecio() + " €<br>";

		// Esto lo pongo así porque si queremos vender una casa no suele quedar
		// bien decirle que le estamos vendiendo
		// una casa más cara que las de la zona al posible cliente.
		if (vivienda.getPrecioMedio() <= vivienda.getPrecioZona())
			texto += "<b>PRECIO MEDIO</u></b>: " + vivienda.getPrecioMedio()
					+ "€<br>" + "<b>PRECIO ZONA</u></b>: "
					+ vivienda.getPrecioZona() + "€</font></html>";
		else
			texto += "<b>PRECIO MEDIO</u></b>: sin datos.<br>"
					+ "<b>PRECIO ZONA</u></b>: sin datos.</font></html>";

		editorPane[0].setContentType("text/html");
		editorPane[0].setOpaque(false);
		editorPane[0].setBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED));
		editorPane[0].setEnabled(false);
		editorPane[0].setDisabledTextColor(Color.black);
		editorPane[0].setText(texto);

		// JEditorPane en la fila 6, columna 0 y que ocupa 4xREMAINDER celdas
		// (REMAINDER es que ocupa el ancho máximo)
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
		editorPane[1].setBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED));
		editorPane[1].setEnabled(false);
		editorPane[1].setDisabledTextColor(Color.black);
		editorPane[1]
				.setText("<html><body align=\"justify\"><font face = \"Arial\", size = 4>"
						+ vivienda.getDescripcion().replaceAll("<br>", " ")
						+ "</font></body></html>");

		// Meter las fincas en la fila 10, columna 0 y que ocupe 5x6 celdas
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 10;
		gridBagConstraints.gridwidth = 6;
		gridBagConstraints.gridheight = 5;
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		panel.add(getPanelFinca(), gridBagConstraints);

		// Meter los otros en la fila 10, columna 10 y que ocupa 5x6 celdas
		gridBagConstraints.gridx = 10;
		gridBagConstraints.gridy = 10;
		gridBagConstraints.gridwidth = 6;
		gridBagConstraints.gridheight = 5;
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		panel.add(getPanelOtros(), gridBagConstraints);

		// Meter los básicos en la fila 0, columna 10 y que ocupe 6x3 celdas
		gridBagConstraints.gridx = 10;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.gridheight = 6;
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.anchor = GridBagConstraints.CENTER;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		panel.add(getPanelBasicos(), gridBagConstraints);

		// Hacerlo visible
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setMinimumSize(new Dimension(dim.width - 150, dim.height - 150));
		setLocation((dim.width - getWidth()) / 2,
				(dim.height - getHeight()) / 2);

		add(panel);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		// VentanaPpal.disposeResult();
		button[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(VentanaDescripcion.this,
						"Contacto con el vendedor: " + "699 999 999\n"
						/* + "URL: "+ VIVIENDA.getUrl() */, "Comprando",
						JOptionPane.INFORMATION_MESSAGE);

			}
		});
		button[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CBRQuery query = new CBRQuery();
				query.setDescription(VIVIENDA);
				Controlador.getInstance().moreLikeThis(query,
						VentanaPpal.getIdDescViviendVisitadas());
				dispose();

			}
		});

		setVisible(true);
	}

	/** Métodos **/

	/* Getters */

	public DescripcionVivienda getVivienda() {
		return vivienda;
	}

	public ImageIcon getImageIcon() {
		return imageIcon;
	}

	/* Setters */

	public void setVivienda(DescripcionVivienda vivienda) {
		this.vivienda = vivienda;
	}

	public void setImageIcon(ImageIcon imageIcon) {
		this.imageIcon = imageIcon;
	}

	/* Métodos que implementan ItemListener */

	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == ascensor)
			ascensor.setSelected(vivienda.getExtrasFinca().isAscensor());
		else if (e.getSource() == trastero)
			trastero.setSelected(vivienda.getExtrasFinca().isTrastero());
		else if (e.getSource() == energiaSolar)
			energiaSolar
					.setSelected(vivienda.getExtrasFinca().isEnergiaSolar());
		else if (e.getSource() == servPorteria)
			servPorteria
					.setSelected(vivienda.getExtrasFinca().isServPorteria());
		else if (e.getSource() == parkingComunitario)
			parkingComunitario.setSelected(vivienda.getExtrasFinca()
					.isServPorteria());
		else if (e.getSource() == garajePrivado)
			garajePrivado.setSelected(vivienda.getExtrasFinca()
					.isGarajePrivado());
		else if (e.getSource() == videoPortero)
			videoPortero
					.setSelected(vivienda.getExtrasFinca().isVideoportero());
		else if (e.getSource() == lavadero)
			lavadero.setSelected(vivienda.getExtrasBasicos().isLavadero());
		else if (e.getSource() == internet)
			internet.setSelected(vivienda.getExtrasBasicos().isInternet());
		else if (e.getSource() == microondas)
			microondas.setSelected(vivienda.getExtrasBasicos().isMicroondas());
		else if (e.getSource() == horno)
			horno.setSelected(vivienda.getExtrasBasicos().isHorno());
		else if (e.getSource() == amueblado)
			amueblado.setSelected(vivienda.getExtrasBasicos().isAmueblado());
		else if (e.getSource() == cocinaOffice)
			cocinaOffice.setSelected(vivienda.getExtrasBasicos()
					.isCocinaOffice());
		else if (e.getSource() == parquet)
			parquet.setSelected(vivienda.getExtrasBasicos().isParquet());
		else if (e.getSource() == domotica)
			domotica.setSelected(vivienda.getExtrasBasicos().isDomotica());
		else if (e.getSource() == armarios)
			armarios.setSelected(vivienda.getExtrasBasicos().isArmarios());
		else if (e.getSource() == tv)
			tv.setSelected(vivienda.getExtrasBasicos().isTv());
		else if (e.getSource() == lavadora)
			lavadora.setSelected(vivienda.getExtrasBasicos().isLavadora());
		else if (e.getSource() == electrodomesticos)
			electrodomesticos.setSelected(vivienda.getExtrasBasicos()
					.isElectrodomesticos());
		else if (e.getSource() == suiteConBanio)
			suiteConBanio.setSelected(vivienda.getExtrasBasicos()
					.isSuiteConBanio());
		else if (e.getSource() == puertaBlindada)
			puertaBlindada.setSelected(vivienda.getExtrasBasicos()
					.isPuertaBlindada());
		else if (e.getSource() == gresCeramica)
			gresCeramica.setSelected(vivienda.getExtrasBasicos()
					.isGresCeramica());
		else if (e.getSource() == calefaccion)
			calefaccion
					.setSelected(vivienda.getExtrasBasicos().isCalefaccion());
		else if (e.getSource() == aireAcondicionado)
			aireAcondicionado.setSelected(vivienda.getExtrasBasicos()
					.isAireAcondicionado());
		else if (e.getSource() == nevera)
			nevera.setSelected(vivienda.getExtrasBasicos().isNevera());
		else if (e.getSource() == patio)
			patio.setSelected(vivienda.getExtrasOtros().isPatio());
		else if (e.getSource() == balcon)
			balcon.setSelected(vivienda.getExtrasOtros().isBalcon());
		else if (e.getSource() == zonaDeportiva)
			zonaDeportiva.setSelected(vivienda.getExtrasOtros()
					.isZonaDeportiva());
		else if (e.getSource() == zonaComunitaria)
			zonaComunitaria.setSelected(vivienda.getExtrasOtros()
					.isZonaComunitaria());
		else if (e.getSource() == terraza)
			terraza.setSelected(vivienda.getExtrasOtros().isTerraza());
		else if (e.getSource() == piscinaComunitaria)
			piscinaComunitaria.setSelected(vivienda.getExtrasOtros()
					.isPiscinaComunitaria());
		else if (e.getSource() == jardinPrivado)
			jardinPrivado.setSelected(vivienda.getExtrasOtros()
					.isJardinPrivado());
		else if (e.getSource() == zonaInfantil)
			zonaInfantil
					.setSelected(vivienda.getExtrasOtros().isZonaInfantil());
		else if (e.getSource() == piscina)
			piscina.setSelected(vivienda.getExtrasOtros().isPiscina());
	}

	/* Otros métodos */

	/* AUXILIARES */

	private JPanel getPanelFinca() {
		JPanel panel = new JPanel(new GridLayout(3, 3));

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

	private JPanel getPanelBasicos() {
		JPanel panel = new JPanel(new GridLayout(6, 3));

		lavadero = new JCheckBox("Lavedero");
		internet = new JCheckBox("Internet");
		microondas = new JCheckBox("Microondas");
		horno = new JCheckBox("Horno");
		amueblado = new JCheckBox("Amueblado");
		cocinaOffice = new JCheckBox("Cocina-Office");
		parquet = new JCheckBox("Parquet");
		domotica = new JCheckBox("Domótica");
		armarios = new JCheckBox("Armarios");
		tv = new JCheckBox("Televisión");
		lavadora = new JCheckBox("Lavadora");
		electrodomesticos = new JCheckBox("Electrodomésticos");
		suiteConBanio = new JCheckBox("Suite con baño");
		puertaBlindada = new JCheckBox("Puerta blindada");
		gresCeramica = new JCheckBox("Cerámica gres");
		calefaccion = new JCheckBox("Calefacción");
		aireAcondicionado = new JCheckBox("Aire acondicionado");
		nevera = new JCheckBox("Nevera");

		ExtrasBasicos eB = vivienda.getExtrasBasicos();
		lavadero.setSelected(eB.isLavadero());
		internet.setSelected(eB.isInternet());
		microondas.setSelected(eB.isMicroondas());
		horno.setSelected(eB.isHorno());
		amueblado.setSelected(eB.isAmueblado());
		cocinaOffice.setSelected(eB.isCocinaOffice());
		parquet.setSelected(eB.isParquet());
		domotica.setSelected(eB.isDomotica());
		armarios.setSelected(eB.isArmarios());
		tv.setSelected(eB.isTv());
		lavadora.setSelected(eB.isLavadora());
		electrodomesticos.setSelected(eB.isElectrodomesticos());
		suiteConBanio.setSelected(eB.isSuiteConBanio());
		puertaBlindada.setSelected(eB.isPuertaBlindada());
		gresCeramica.setSelected(eB.isGresCeramica());
		calefaccion.setSelected(eB.isCalefaccion());
		aireAcondicionado.setSelected(eB.isAireAcondicionado());
		nevera.setSelected(eB.isNevera());

		lavadero.addItemListener(this);
		internet.addItemListener(this);
		microondas.addItemListener(this);
		horno.addItemListener(this);
		amueblado.addItemListener(this);
		cocinaOffice.addItemListener(this);
		parquet.addItemListener(this);
		domotica.addItemListener(this);
		armarios.addItemListener(this);
		tv.addItemListener(this);
		lavadora.addItemListener(this);
		electrodomesticos.addItemListener(this);
		suiteConBanio.addItemListener(this);
		puertaBlindada.addItemListener(this);
		gresCeramica.addItemListener(this);
		calefaccion.addItemListener(this);
		aireAcondicionado.addItemListener(this);
		nevera.addItemListener(this);

		panel.add(lavadero);
		panel.add(internet);
		panel.add(microondas);
		panel.add(horno);
		panel.add(amueblado);
		panel.add(cocinaOffice);
		panel.add(parquet);
		panel.add(domotica);
		panel.add(armarios);
		panel.add(tv);
		panel.add(lavadora);
		panel.add(electrodomesticos);
		panel.add(suiteConBanio);
		panel.add(puertaBlindada);
		panel.add(gresCeramica);
		panel.add(calefaccion);
		panel.add(aireAcondicionado);
		panel.add(nevera);

		return panel;
	}

	private JPanel getPanelOtros() {
		JPanel panel = new JPanel(new GridLayout(3, 3));

		patio = new JCheckBox("Patio");
		balcon = new JCheckBox("Balcón");
		zonaDeportiva = new JCheckBox("Zona deportiva");
		zonaComunitaria = new JCheckBox("Zona comunitaria");
		terraza = new JCheckBox("Terraza");
		piscinaComunitaria = new JCheckBox("Piscina comunitaria");
		jardinPrivado = new JCheckBox("Jardín privado");
		zonaInfantil = new JCheckBox("Zona infantil");
		piscina = new JCheckBox("Piscina");
		ExtrasOtros eO = vivienda.getExtrasOtros();
		patio.setSelected(eO.isPatio());
		balcon.setSelected(eO.isBalcon());
		zonaDeportiva.setSelected(eO.isZonaDeportiva());
		zonaComunitaria.setSelected(eO.isZonaComunitaria());
		terraza.setSelected(eO.isTerraza());
		piscinaComunitaria.setSelected(eO.isPiscinaComunitaria());
		jardinPrivado.setSelected(eO.isJardinPrivado());
		zonaInfantil.setSelected(eO.isZonaInfantil());
		piscina.setSelected(eO.isPiscina());

		patio.addItemListener(this);
		balcon.addItemListener(this);
		zonaDeportiva.addItemListener(this);
		zonaComunitaria.addItemListener(this);
		terraza.addItemListener(this);
		piscinaComunitaria.addItemListener(this);
		jardinPrivado.addItemListener(this);
		zonaInfantil.addItemListener(this);
		piscina.addItemListener(this);

		panel.add(patio);
		panel.add(balcon);
		panel.add(zonaDeportiva);
		panel.add(zonaComunitaria);
		panel.add(terraza);
		panel.add(piscinaComunitaria);
		panel.add(jardinPrivado);
		panel.add(zonaInfantil);
		panel.add(piscina);

		return panel;
	}

}
