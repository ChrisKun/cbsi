package gui;

import java.text.DecimalFormat;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

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
	private static final String  MAIN_LANDF="de.javasoft.plaf.synthetica.SyntheticaOrangeMetallicLookAndFeel";
	//private static final String  MAIN_LANDF="de.javasoft.plaf.synthetica.SyntheticaStandardLookAndFeel";
	//private static final String  MAIN_LANDF="de.javasoft.plaf.synthetica.SyntheticaBlackEyeLookAndFeel";
	
	public static void main(String[] args) {
		// configuracion de formato de salida de los logs
		Logger log = Logger.getLogger("");
		for (Handler h : log.getHandlers())
			log.removeHandler(h);
		log = crearLog("CP");
		
		try {
			UIManager.setLookAndFeel(MAIN_LANDF);
			new Interfaz();
		} catch (ClassNotFoundException e) {
			log.severe(e.getMessage());
		} catch (InstantiationException e) {
			log.severe(e.getMessage());
		} catch (IllegalAccessException e) {
			log.severe(e.getMessage());
		} catch (UnsupportedLookAndFeelException e) {
			log.severe(e.getMessage());
		}
		
	}
	
	private static Logger crearLog(String nombre) {
		Logger log;
		log = Logger.getLogger(nombre);
		log.addHandler(new ConsoleHandler());
		log.getHandlers()[0].setFormatter(new SimpleFormatter() {
			DecimalFormat fmt = new DecimalFormat("000");
			long previousMillis = System.currentTimeMillis();
			int stanza = 0;

			@Override
			public synchronized String format(LogRecord record) {
				String s = (record.getMillis() - previousMillis > 500) ? "\n *** "
						+ (++stanza) + " *** \n\n"
						: "";
				previousMillis = record.getMillis();
				return s + fmt.format(previousMillis % 1000) + " "
						+ record.getLevel() + " -- " + record.getMessage()
						+ "\n";
			}
		});
		/* configuracion del nivel de salida 'por defecto' de los logs
		   opciones: FINEST, FINER, FINE, INFO, CONFIG, WARNING, SEVERE
		   (es posible configurar los logs individuales a niveles distintos)*/
		log.getHandlers()[0].setLevel(Level.ALL);
		log.setLevel(Level.ALL);
		return log;
	}
	
}
