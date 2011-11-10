package es.ucm.fdi.isbc.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class Gui extends JFrame implements ActionListener{
	private JButton button;
	private JPanel minipanel;
	private PanelCaract panelCaract;
	private PanelExtrasFinca panelExtrasFinca;
	private PanelExtrasBasico panelExtrasBasico;
	private PanelExtrasOtros panelExtrasOtros;
	private JTabbedPane tabbed;
	
	public Gui(){
		super("Recomendador Viviendas");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		tabbed = new JTabbedPane();
		
		panelCaract = new PanelCaract();
		tabbed.addTab("Características", panelCaract);
		
		panelExtrasFinca = new PanelExtrasFinca();
		tabbed.addTab("Extras Finca", panelExtrasFinca);
		
		panelExtrasBasico = new PanelExtrasBasico();
		tabbed.addTab("Extras Básicos", panelExtrasBasico);
		
		panelExtrasOtros = new PanelExtrasOtros();
		tabbed.addTab("Extras Básicos", panelExtrasOtros);
		
        this.add(tabbed, BorderLayout.NORTH);
		button = new JButton("OK");
		button.addActionListener(this);
		minipanel = new JPanel();
		minipanel.add(button);
        this.add(minipanel, BorderLayout.CENTER);
		//Display the window.
        this.pack();
        this.setVisible(true);
	}


	public static void main(String[] args) {
    	Gui g = new Gui();
    }

	@Override
	public void actionPerformed(ActionEvent e) {

	}

}
