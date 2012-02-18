package org.mutoss.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.lang.reflect.Method;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.af.commons.Localizer;
import org.af.commons.tools.OSTools;
import org.af.commons.widgets.validate.IntegerTextField;
import org.af.commons.widgets.validate.RealTextField;
import org.mutoss.config.Configuration;

public class CreateGraphGUI extends JFrame implements WindowListener, ActionListener, ChangeListener {
	
	Configuration conf = Configuration.getInstance();
	
	/**
	 * Constructor - you will not need to change things here
	 */
	public CreateGraphGUI() {		
		super("QTD (Quantitative Trial Design) GUI Version 1.1");
		//setIconImage((new ImageIcon(getClass().getResource("/org/mutoss/gui/graph/images/rjavaicon64.png"))).getImage());
		
		/* This Errorhandling should be uncommented for testing versions that should report errors:
		 if (!LoggingSystem.alreadyInitiated()) {
			LoggingSystem.init(
					"/org/mutoss/gui/commons-logging.properties",
					true,
					false,
					new ApplicationLog());
			ErrorHandler.init("rohmeyer@small-projects.de", "http://www.algorithm-forge.com/report/bugreport.php", true, true, ErrorDialogSGTK.class);
		}*/
		
		Locale.setDefault(Locale.ENGLISH);
		RControl.getRControl(true);
		Localizer.getInstance().addResourceBundle("org.mutoss.gui.ResourceBundle");		
		
		makeContent();
		pack();

		Dimension dim = getToolkit().getScreenSize();
		Rectangle bounds = getBounds();
		setLocation((dim.width - bounds.width) / 2, (dim.height - bounds.height) / 2);

		addWindowListener(this);
		
		//loadDefaults();
		
		setVisible(true);
	}

	/** Two methods to set and save the default values */
	private String getValue(String key, String def) { return conf.getProperty(key, def); }
	private void setValue(String key, String value) { conf.setProperty(key, value); }

	public String[] designs = new String[] {"anderson", "andersonPreece", "archdeacon", "atkinson3t", "atkinson4t", 
			"atkinson5t", "balaam3t", "balaam4t", "balaam5t", "balaam6t", 
			"bateJones5t", "bateJones8t", "berenblut3t", "berenblut4t", "berenblut5t", 
			"blaisdellRaghavarao6t", "blaisdellRaghavarao8t", "blaisdellRaghavarao9t", 
			"davisHall6ta", "davisHall6tb", "davisHall6tc", "davisHall7ta", 
			"davisHall7tb", "davisHall7tc", "davisHall8ta", "davisHall8tb", 
			"davisHall8tc", "davisHall9ta", "davisHall9tb", "davisHall9tc"};

	private void makeContent() {
		JTabbedPane tabbedPane = new JTabbedPane();

		//JPanel topPanel = getTopPanel();

		//JPanel panelcont = getContPanel();
		//tabbedPane.addTab("Continuous endpoint", panelcont);

		//JPanel panelbinary = getBinPanel();
		//tabbedPane.addTab("Binary endpoint", panelbinary);
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx=0; c.gridy=0; c.gridwidth = 1; c.gridheight = 1; c.ipadx=5; c.ipady=5; c.weightx=1; c.weighty=0;
		
		getContentPane().setLayout(new GridBagLayout());		
		
		//getContentPane().add(topPanel, c);
		c.gridy++;		 
		c.weighty=1;
		getContentPane().add(tabbedPane, c);	
		
		c.gridy++;		 
		c.weighty=0;
		//getContentPane().add(getButtonPane(), c);	
	}
	
	/**
	 * This function handles events from buttons, check boxes and combo boxes. 
	 */
	public void actionPerformed(ActionEvent e) {
	
	}
	
	/**
	 * This function builds the OC.bin/OC.ct function call.
	 * @param e
	 */
	private void callOCFunction(ActionEvent e) {
		String s = "";
		try {
					
		} catch (Exception exc) {
			JOptionPane.showMessageDialog(this, "An error ocurred with the following command:\n"+s+"\n\nError message:\n"+exc.getMessage(), "Error in calculation", JOptionPane.ERROR_MESSAGE);			
		}
	}

	/**
	 * Shows a file from a package
	 * @param s String like 
	 */
	public void showFile(String s) {
		File f = new File(RControl.getR().eval("system.file(\""+s+"\", package=\"crossover\")").asRChar().getData()[0]);
		if (!f.exists()) {
			throw new RuntimeException("This is strange. The file \""+s+"\" could not be found.");
		} else {
			try {	
				Method main = Class.forName("java.awt.Desktop").getDeclaredMethod("getDesktop");
				Object obj = main.invoke(new Object[0]);
				Method second = obj.getClass().getDeclaredMethod("open", new Class[] { File.class }); 
				second.invoke(obj, f);
			} catch (Exception exc) {			
				try {
					if (OSTools.isWindows()) {
						Process p;							
						p = Runtime.getRuntime().exec("rundll32 " +
								"url.dll,FileProtocolHandler " + f.getAbsolutePath());
						p.waitFor();
					} else {
						JOptionPane.showMessageDialog(this, "Please open and read the following file:\n"+f.getAbsolutePath(), "Could not find appropriate viewer", JOptionPane.WARNING_MESSAGE);
					}
				} catch (Exception e1) {					
					e1.printStackTrace();
					JOptionPane.showMessageDialog(this, "Please open and read the following file:\n"+f.getAbsolutePath(), "Could not find appropriate viewer", JOptionPane.WARNING_MESSAGE);
				}

			}
		}
	}

	/**
	 * WindowListener methods - the first one closes the R console if  
	 */
	public void windowClosing(WindowEvent e) {
		RControl.getR().eval("q(save=\"no\")");
	}
	public void windowActivated(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}
	

	public void stateChanged(ChangeEvent e) {}

	/**
	 * Method to call from R
	 */
	public static void startGUI() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new CreateGraphGUI();
			}
		});		
	}	
	
	/**
	 * Just for me for the development
	 * @param args
	 */
	public static void main(String[] args) {
		RControl.getR().eval(".setenv <- if (exists(\"Sys.setenv\")) Sys.setenv else Sys.putenv");
		RControl.getR().eval(".setenv(\"JAVAGD_CLASS_NAME\"=\"org/mutoss/gui/JavaGD\")");
		new CreateGraphGUI();
	}

}
