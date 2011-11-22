package es.ucm.fdi.isbc.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;

import es.ucm.fdi.isbc.controlador.Controlador;
import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda;

@SuppressWarnings("serial")
public class Gui extends JInternalFrame implements ActionListener, Observer{
	private JButton button;
	private JPanel minipanel, panel;
	private PanelCaract panelCaract;
	private PanelExtrasFinca panelExtrasFinca;
	private PanelExtrasBasico panelExtrasBasico;
	private PanelExtrasOtros panelExtrasOtros;
	private JTabbedPane tabbed;
	private DescripcionVivienda descr;
//	private MenuBar barraMenus;
//	private Menu mArchivo;
	private boolean flag = true;
	
	public Gui(JTree localizaciones){
		super("Recomendador Viviendas");
		
		/*barraMenus = new MenuBar();
		mArchivo = new Menu( "Archivo" );
		mArchivo.addActionListener(this);
		mArchivo.add(new MenuItem( "Salir"));
		barraMenus.add(mArchivo);
		this.setMenuBar(barraMenus);*/
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		panel = new JPanel(new BorderLayout());
		tabbed = new JTabbedPane();
		
		panelCaract = new PanelCaract(localizaciones);
		tabbed.addTab("Características", panelCaract);
		
		panelExtrasFinca = new PanelExtrasFinca();
		tabbed.addTab("Extras Finca", panelExtrasFinca);
		
		panelExtrasBasico = new PanelExtrasBasico();
		tabbed.addTab("Extras Básicos", panelExtrasBasico);
		
		panelExtrasOtros = new PanelExtrasOtros();
		tabbed.addTab("Extras Otros", panelExtrasOtros);
		
		panel.add(tabbed,BorderLayout.NORTH);
		button = new JButton("OK");
		button.addActionListener(this);
		minipanel = new JPanel();
		minipanel.add(button);
		panel.add(minipanel, BorderLayout.CENTER);
        descr = null;
        JScrollPane scrollPane = new JScrollPane(panel);
        this.add(scrollPane);
		//Display the window.
        this.pack();
        
	}

	@Override
	public void actionPerformed(ActionEvent e) {
        if (flag && "OK".equals(e.getActionCommand())) {
        	// Obtener datos paneles y crear DescripcionVivienda
        	descr = panelCaract.getDescripcionVivienda(-1);
        	descr.setExtrasFinca(panelExtrasFinca.getExtrasFinca(-1));
        	descr.setExtrasBasicos(panelExtrasBasico.getExtrasBasicos(-1));
        	descr.setExtrasOtros(panelExtrasOtros.getExtrasOtros(-1));
        	Controlador.getInstance().repite(descr,-1);
        } 
	}


	public DescripcionVivienda getDescripcionVivienda() {
		return descr;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// Ya ha terminado de hacer el inicio
		button.setEnabled(true);
		flag = true;
	}

}
