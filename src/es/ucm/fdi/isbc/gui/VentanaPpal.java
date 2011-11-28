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
import javax.swing.JTree;

import es.ucm.fdi.isbc.controlador.Controlador;
import es.ucm.fdi.isbc.eventos.MuestraSolEvent;


public class VentanaPpal extends JFrame implements ActionListener, Observer{
	private static final long serialVersionUID = 1L;
	private MenuBar barraMenus;
	private Menu mTasador, modoEval;
	private MenuItem modoNormal, leaveOneOut, holdOut, nFold;
	private boolean flag = false;
	private Gui g;
	private VentanaResult vResult;
	private JTree localizaciones;

	public VentanaPpal(){
		super("Tasador");
		barraMenus = inicializaMenus();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(550,700));
		int x=(int) (Toolkit.getDefaultToolkit().getScreenSize().width/2-this.getPreferredSize().width/2);
		int y=(int) (Toolkit.getDefaultToolkit().getScreenSize().height/2-this.getPreferredSize().height/2);
		setLocation(x, y);
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
		modoEval = new Menu("Evaluación");
		modoEval.addActionListener(this);
		
		leaveOneOut = new MenuItem("Leave One Out");
		holdOut = new MenuItem("Hold Out");
		nFold = new MenuItem("N-Fold");
		modoEval.add(leaveOneOut);
		modoEval.add(holdOut);
		modoEval.add(nFold);
		
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
				g = new Gui(localizaciones);
				g.setVisible(true);
				this.add(g);
			}
			else{
				g.setVisible(true);
			}
			if (vResult != null)
				vResult.setVisible(false);
		}
		else if (flag && arg.getActionCommand().equals("Leave One Out")){
			Controlador.getInstance().repite(null, 0);
			if(g != null)
				g.setVisible(false);
		}
		else if (flag && arg.getActionCommand().equals("Hold Out")){
			Controlador.getInstance().repite(null, 1);
			if(g != null)
				g.setVisible(false);
		}
		else if (flag && arg.getActionCommand().equals("N-Fold")){
			Controlador.getInstance().repite(null, 2);
			if(g != null)
				g.setVisible(false);
		}
		else if (arg.getActionCommand().equals("Salir"))
			System.exit(0); 
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof MuestraSolEvent){
			if(g != null)
				g.setVisible(false);
			if(vResult == null){
				vResult = new VentanaResult();
				add(vResult);
			}
			
			vResult.setSolucion(((MuestraSolEvent)arg).getDescr(), ((MuestraSolEvent)arg).getPrecioPrediccion(), ((MuestraSolEvent)arg).getConfianzaPrediccion());
			vResult.setVisible(true);
			//Update de poder hacer nueva consulta:
		}
		// Ya ha terminado de hacer el inicio
		else{
			if (flag){
				modoEval.setEnabled(false);
				modoNormal.setEnabled(false);
				flag = false;
			}
			else{
				modoEval.setEnabled(true);
				modoNormal.setEnabled(true);
				flag = true;
			}
		}
	}
	
	public void setLocalizaciones(JTree localzn){
		localizaciones = localzn;
	}

}
