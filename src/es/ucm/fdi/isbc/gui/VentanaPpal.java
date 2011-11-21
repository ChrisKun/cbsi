package es.ucm.fdi.isbc.gui;

import java.awt.Dimension;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

import es.ucm.fdi.isbc.controlador.Controlador;
import es.ucm.fdi.isbc.viviendas.representacion.DescripcionVivienda;


public class VentanaPpal extends JFrame implements ActionListener, Observer{
	private static final long serialVersionUID = 1L;
	private MenuBar barraMenus;
	private Menu mTasador;
	private MenuItem modoEval, modoNormal;
	private boolean flag = false;
	private Gui g;
	private VentanaResult vResult;
	

	public VentanaPpal(){
		super("Tasador");
		barraMenus = inicializaMenus();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(550,865));
		setLocation(200, 100);
		this.setResizable(false);
		setMenuBar(barraMenus);
		pack();
	}

	/**
	 * Devuelve el menú de la ventana
	 * @return El menú de la ventana
	 */
	private MenuBar inicializaMenus(){
		MenuBar barra = new MenuBar();
		mTasador = new Menu("Modo");
		mTasador.addActionListener(this);
		modoNormal = new MenuItem("Normal");
		mTasador.add(modoNormal);
		modoNormal.setEnabled(false);
		modoEval = new MenuItem("Evaluación");
		mTasador.add(modoEval);
		modoEval.setEnabled(false);
		mTasador.addSeparator();
		mTasador.add(new MenuItem("Salir"));
		barra.add(mTasador);
		return barra;
	}

	/**
	 * Acción a realizar después de la pulsación de un botón
	 */
	public void actionPerformed(ActionEvent arg) {
		if (arg.getActionCommand().equals("Normal")){
			if(g == null){
				g = new Gui();
				g.setVisible(true);
				this.add(g);
			}
			else{
				g.setVisible(true);
			}
			if(vResult != null)
				vResult.setVisible(false);
		}
		else if (flag && arg.getActionCommand().equals("Evaluación")){
			Controlador.getInstance().repite(null);
			if(g != null)
				g.setVisible(false);
		}
		else if (arg.getActionCommand().equals("Salir"))
			System.exit(0); 
	}

	@Override
	public void update(Observable o, Object arg) {
		// Ya ha terminado de hacer el inicio
		modoEval.setEnabled(true);
		modoNormal.setEnabled(true);
		flag = true;
	}
	
	public void muestraSol(DescripcionVivienda descrip, int precio, double confianza){
		if(g != null)
			g.setVisible(false);
		if(vResult == null){
			vResult = new VentanaResult();
			add(vResult);
		}
		vResult.setSolucion(descrip, precio, confianza);
		vResult.setVisible(true);
		//Update de poder hacer nueva consulta:
	}

}
