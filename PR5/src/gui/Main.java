package gui;

import java.util.logging.Handler;
import java.util.logging.Logger;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


/**
 * Practica 5 de ISBC: Introduccion a las ontologias.
 * 
 * @author Gloria Esther Pozuelo Fernandez
 * @author Vladimir Georgiev Mikovski
 * @author Marco Gallardo Casu
 *
 */
public class Main {
//	private static final String  MAIN_LANDF="de.javasoft.plaf.synthetica.SyntheticaOrangeMetallicLookAndFeel";
//	private static final String  MAIN_LANDF="de.javasoft.plaf.synthetica.SyntheticaStandardLookAndFeel";
//	private static final String  MAIN_LANDF="de.javasoft.plaf.synthetica.SyntheticaBlackEyeLookAndFeel";
	
	public static void main(String[] args) {
		// configuracion de formato de salida de los logs
		/*Logger log = Logger.getLogger("CP");
		for (Handler h : log.getHandlers())
			log.removeHandler(h);
		
		try {
			UIManager.setLookAndFeel(MAIN_LANDF);*/
			new Interfaz();
		/*} catch (ClassNotFoundException e) {
			log.severe(e.getMessage());
		} catch (InstantiationException e) {
			log.severe(e.getMessage());
		} catch (IllegalAccessException e) {
			log.severe(e.getMessage());
		} catch (UnsupportedLookAndFeelException e) {
			log.severe(e.getMessage());
		}*/
		
	}
	
}
