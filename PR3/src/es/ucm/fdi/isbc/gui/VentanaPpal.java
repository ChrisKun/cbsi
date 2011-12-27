package es.ucm.fdi.isbc.gui;

import java.awt.Dimension;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JSplitPane;

import es.ucm.fdi.isbc.eventos.MuestraSolEvent;

public class VentanaPpal extends JFrame implements Observer, ActionListener{
	private static final long serialVersionUID = 1L;
	
	private PanelVisitados panelVisitados;
	private PanelFiltro panelFiltro;
	private PanelDiversidad panelDiversidad;
	private MenuBar barraMenus;
	private Menu mArchivo;
	private MenuItem salir;
	private JSplitPane horizontal, vertical;
	
	private VentanaResult vResult;
	
	private boolean flag = false;
	
	public VentanaPpal(){
		super("Recomendador Viviendas");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		barraMenus = inicializaMenus();
		this.setMenuBar(barraMenus);
		
		panelVisitados = new PanelVisitados();
		panelFiltro = new PanelFiltro();
		panelDiversidad = new PanelDiversidad();
		
		vertical = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelDiversidad, panelVisitados);
		horizontal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelFiltro, vertical);
		
		vertical.setMinimumSize(new Dimension(450, 450));
		horizontal.setMinimumSize(new Dimension(800, 600));
		
		setPreferredSize(new Dimension(550,700));
		int x=(int) (Toolkit.getDefaultToolkit().getScreenSize().width/2-this.getPreferredSize().width/2);
		int y=(int) (Toolkit.getDefaultToolkit().getScreenSize().height/2-this.getPreferredSize().height/2);
		setLocation(x, y);

		this.setContentPane(horizontal);
		this.setSize(800, 600);	
	}
	
	/**
	 * Devuelve el menú de la ventana
	 * @return El menú de la ventana
	 */
	private MenuBar inicializaMenus(){
		MenuBar barra = new MenuBar();
		mArchivo = new Menu("Archivo");

		salir = new MenuItem("Salir");
		salir.addActionListener(this);
		
		mArchivo.add(salir);
		
		barra.add(mArchivo);
		return barra;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof MuestraSolEvent){	
			if (vResult == null){
				vResult = new VentanaResult();
			}
			vResult.setResultado(((MuestraSolEvent)arg).getDescrs());
		}
		else{
			if (flag){
				panelFiltro.enableButton(false);
				flag = false;
			}
			else{
				panelFiltro.enableButton(true);
				flag = true;
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg) {
		if (arg.getActionCommand().equals("Salir"))
			System.exit(0); 
	}

}
