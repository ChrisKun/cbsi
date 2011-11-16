package es.ucm.fdi.isbc.gui;

import java.awt.BorderLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import es.ucm.fdi.isbc.controlador.Controlador;
import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda;

@SuppressWarnings("serial")
public class Gui extends JFrame implements ActionListener, Observer{
	private JButton button;
	private JPanel minipanel;
	private PanelCaract panelCaract;
	private PanelExtrasFinca panelExtrasFinca;
	private PanelExtrasBasico panelExtrasBasico;
	private PanelExtrasOtros panelExtrasOtros;
	private JTabbedPane tabbed;
	private DescripcionVivienda descr;
	private Controlador controlador;
	private MenuBar barraMenus;
	private Menu mArchivo;
	private boolean flag = true;
	
	public Gui(Controlador c){
		super("Recomendador Viviendas");
		
		this.controlador = c;
		
		barraMenus = new MenuBar();
		mArchivo = new Menu( "Archivo" );
		mArchivo.addActionListener(this);
		mArchivo.add(new MenuItem( "Salir"));
		barraMenus.add(mArchivo);
		this.setMenuBar(barraMenus);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		this.setLayout(new BorderLayout());
		tabbed = new JTabbedPane();
		
		panelCaract = new PanelCaract();
		tabbed.addTab("Características", panelCaract);
		
		panelExtrasFinca = new PanelExtrasFinca();
		tabbed.addTab("Extras Finca", panelExtrasFinca);
		
		panelExtrasBasico = new PanelExtrasBasico();
		tabbed.addTab("Extras Básicos", panelExtrasBasico);
		
		panelExtrasOtros = new PanelExtrasOtros();
		tabbed.addTab("Extras Otros", panelExtrasOtros);
		
        this.add(tabbed, BorderLayout.NORTH);
		button = new JButton("OK");
		button.addActionListener(this);
		button.setEnabled(false);
		minipanel = new JPanel();
		minipanel.add(button);
        this.add(minipanel, BorderLayout.CENTER);
        descr = null;
		//Display the window.
        this.pack();
        
	}

	@Override
	public void actionPerformed(ActionEvent e) {
        if (flag && "OK".equals(e.getActionCommand())) {
        	// Obtener datos paneles y crear DescripcionVivienda
        	// TODO: id query?
        	descr = panelCaract.getDescripcionVivienda(-1);
        	descr.setExtrasFinca(panelExtrasFinca.getExtrasFinca(-1));
        	descr.setExtrasBasicos(panelExtrasBasico.getExtrasBasicos(-1));
        	descr.setExtrasOtros(panelExtrasOtros.getExtrasOtros(-1));
        	flag = false;
        	controlador.repite(descr);
        } 
		else if (e.getActionCommand().equals("Salir")){
			controlador.fin();
			System.exit(0); 
		}
	}


	public DescripcionVivienda getDescripcionVivienda() {
		return descr;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// Ya ha terminado de mostrar los resultados
		button.setEnabled(true);
		flag = true;
	}

}
