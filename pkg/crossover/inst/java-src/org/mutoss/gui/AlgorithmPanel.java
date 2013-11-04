package org.mutoss.gui;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.af.commons.widgets.ComponentTitledBorder;
import org.af.commons.widgets.HTMLPaneWithButtons;
import org.mutoss.gui.infinite.InfiniteRunningDialog;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class AlgorithmPanel extends JPanel implements ActionListener, ChangeListener {
	
	JTextField jtTitle = new JTextField();
	JTextField jtReference = new JTextField();
	JTextField jtN1 = new JTextField("5000", 6);	

	JButton ok = new JButton("Ready");
	JButton jbCompute = new JButton("Compute Design");
	public HTMLPaneWithButtons jta;
	JLabel label = new JLabel();
	JPanel ntPanel = null;
	JPanel weightsPanel = null;
	JPanel effPanel = null;
	List<JTextField> weightsV = new Vector<JTextField>();
	List<JTextField> effV = new Vector<JTextField>();
	List<JTextField> nV = new Vector<JTextField>();
	CellConstraints cc = new CellConstraints();
	JRadioButton jbBalanceNothing = new JRadioButton("No balancing restrictions");
	JRadioButton jbBalanceSequences = new JRadioButton("Balance treatments in regard to sequences (may decrease efficiency)");
	JRadioButton jbBalancePeriods = new JRadioButton("Balance treatments in regard to periods (may decrease efficiency)");
	CrossoverGUI gui;	
	JButton exportR = new JButton("Export to R");
	JButton showAlgoPerformance = new JButton("Search algorithm plot");
	JCheckBox useCatalogueDesigns = new JCheckBox("Use designs from catalogue as starting point");
	JComboBox jcbContrasts = new JComboBox(new String[] {"All pair comparisons"} );
	JComboBox jCBMixed = new JComboBox(new String[] {"Fixed subject effects model", "Random subject effects model"});
	JComboBox jcbCorrelation = new JComboBox(new String[] {"Independence", "Autocorrelated Error", "User defined"});
	JCheckBox fixedNumber = new JCheckBox("Specify exact number of treatment assignments:");
	JLabel jlMixed;
	JLabel jlCor;
	JTextField jtWithinSubjectRho;
	//JTabbedPane jTabAlgo = new jTabAlgo;
	
	public AlgorithmPanel(CrossoverGUI gui) {
		this.gui = gui;
		gui.spinnerT.addChangeListener(this);
		gui.spinnerP.addChangeListener(this);
		String cols = "5dlu, fill:min:grow, 5dlu, fill:min:grow";
        String rows = "5dlu, fill:pref:grow, 5dlu";
        
        FormLayout layout = new FormLayout(cols, rows);
        layout.setColumnGroups(new int[][]{ {2, 4} });

        setLayout(layout);
        CellConstraints cc = new CellConstraints();        
		
		int row = 2;
    	
    	jta = new HTMLPaneWithButtons();
		jta.setFont(new Font("Monospaced", Font.PLAIN, 12));
		//jta.setLineWrap(false);		
		jta.setMargin(new Insets(4,4,4,4));
		jta.setEditable(false);		
		
		add(new JScrollPane(getLeftSidePanel()), cc.xy(2, row));
		add((getRightSidePanel()), cc.xy(4, row));
		
		
	}
	
	JSpinner spinnerS;
	JPanel lsPanel;

	public JPanel getRightSidePanel() {
		JPanel panel = new JPanel();
		
		String cols = "5dlu, pref, 5dlu, fill:min:grow, 5dlu";
        String rows = "pref, 5dlu, fill:min:grow, 5dlu, pref";
        
        panel.setLayout(new FormLayout(cols, rows));
        CellConstraints cc = new CellConstraints();
		
		int row = 1;
    	
		panel.add(new JLabel("Created Design"), cc.xy(2, row));
		
		row += 2;
		
		panel.add(new JScrollPane(jta), cc.xyw(2, row, 3));
		
		row += 2;
		
		panel.add(exportR, cc.xy(2, row));
		exportR.setEnabled(false);
		exportR.addActionListener(this);		
		panel.add(showAlgoPerformance, cc.xy(4, row));
		showAlgoPerformance.setEnabled(false);
		showAlgoPerformance.addActionListener(this);
		
		return panel;
	}
    
	
	public JPanel getLeftSidePanel() {
		lsPanel = new JPanel();
		String cols = "5dlu, pref, 5dlu, fill:min:grow, 5dlu";
        String rows = "5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu";
        
        lsPanel.setLayout(new FormLayout(cols, rows));
		
		int row = 2;
    	

        lsPanel.add(jCBMixed, cc.xy(4, row));
        jCBMixed.setSelectedIndex(0);
        jCBMixed.addActionListener(this);
        //jCBMixed.setEnabled(false);
        
		row+=2;  
		
        jlCor = new JLabel("Correlation structure");
        //jlCor.setEnabled(false);
        //jcbCorrelation.setEnabled(false);
		lsPanel.add(jlCor, cc.xy(2, row));
		lsPanel.add(jcbCorrelation, cc.xy(4, row));

        row+=2;
        
        jlMixed = new JLabel("Within-subject correlation coefficient");
        jlMixed.setEnabled(false);
        jtWithinSubjectRho = new JTextField("0.5");
        jtWithinSubjectRho.setEnabled(false);
		lsPanel.add(jlMixed, cc.xy(2, row));
		lsPanel.add(jtWithinSubjectRho, cc.xy(4, row));
		
        row+=2;  
        
    	spinnerS = new JSpinner(new SpinnerNumberModel(5, 1, 100, 1)); 
    	spinnerS.addChangeListener(this);
        
        lsPanel.add(new JLabel("Number of sequences:"), cc.xy(2, row));
        lsPanel.add(spinnerS, cc.xy(4, row));
        
        row+=2;
        
        rowN = row; 
        createTreatmentNumberPanel();
        
        row+=2;  
        
        ButtonGroup group = new ButtonGroup();
        group.add(jbBalanceNothing);
        group.add(jbBalanceSequences);
        group.add(jbBalancePeriods);
        jbBalanceNothing.setSelected(true);
              
        lsPanel.add(jbBalanceNothing, cc.xyw(2, row, 3));
        row+=2;
        lsPanel.add(jbBalanceSequences, cc.xyw(2, row, 3));
        row+=2;
        lsPanel.add(jbBalancePeriods, cc.xyw(2, row, 3));        
        row+=2;
        
        //rowTNP = row;        
        //createWeightsPanel();
        lsPanel.add(new JLabel("Contrasts:"), cc.xy(2, row));
        lsPanel.add(jcbContrasts, cc.xy(4, row));

        row+=2;    
        
        rowEff = row;        
        createEffPanel();
        
        row+=2;        
        
        useCatalogueDesigns.setSelected(true);
        lsPanel.add(useCatalogueDesigns, cc.xyw(2, row, 3));
        
        row+=2;   
        
        lsPanel.add(new JLabel("Number of steps per run:"), cc.xy(2, row));
        lsPanel.add(jtN1, cc.xy(4, row));
        
        row+=2;        
        
        lsPanel.add(jbCompute, cc.xyw(2, row, 3));
        jbCompute.addActionListener(this);
        
        return lsPanel;
	}
	
	int rowTNP, rowN, rowEff;
	
	private void createWeightsPanel() {		
		if (weightsPanel!=null) {
			lsPanel.remove(weightsPanel);
		}

		weightsPanel = new JPanel();
		GridBagConstraints cbcWeights = new GridBagConstraints();
		cbcWeights.fill = GridBagConstraints.BOTH;	
		cbcWeights.gridx=0; cbcWeights.gridy=0;
		cbcWeights.gridwidth = 1; cbcWeights.gridheight = 1;
		cbcWeights.ipadx=5; cbcWeights.ipady=5;
		cbcWeights.weightx=1; cbcWeights.weighty=1;
		weightsPanel.setBorder(BorderFactory.createTitledBorder("Contrast Weights [will be redesigned and work only under R in the moment]"));
		//
		//weightsPanel.setEnabled(false);
		weightsPanel.setLayout(new GridBagLayout());
		
		List<String> labels = new Vector<String>();
        
		int s = Integer.parseInt(gui.spinnerT.getModel().getValue().toString());
		for (int i=1; i<s; i++) {
        	for (int j=1; j<=s; j++) {
        		labels.add(i+"-"+j);
        	}        	
        }  

		weightsV.clear();
		for (int i=0;i<labels.size();i++) {        		
			weightsV.add(new JTextField("1.0", 6));
			weightsPanel.add(new JLabel(labels.get(i)), cbcWeights);
			cbcWeights.gridx++;
			weightsPanel.add(weightsV.get(i), cbcWeights);	
			if ((i+1)%3!=0) {
				cbcWeights.gridx++;
			} else {
				cbcWeights.gridx=0;cbcWeights.gridy++;
			}
		}		
		for (Component c : weightsPanel.getComponents()) c.setEnabled(false);
		//lsPanel.add(weightsPanel, cc.xyw(2, rowTNP, 3));
		lsPanel.revalidate();
		lsPanel.repaint();
	}
	
	public void createEffPanel() {		
		if (effPanel!=null) {
			lsPanel.remove(effPanel);
		}

		effPanel = new JPanel();
		GridBagConstraints effWeights = new GridBagConstraints();
		effWeights.fill = GridBagConstraints.BOTH;	
		effWeights.gridx=0; effWeights.gridy=0;
		effWeights.gridwidth = 1; effWeights.gridheight = 1;
		effWeights.ipadx=5; effWeights.ipady=5;
		effWeights.weightx=1; effWeights.weighty=1;
		effPanel.setBorder(BorderFactory.createTitledBorder("Weights:"));
		
		//effPanel.setEnabled(false);
		effPanel.setLayout(new GridBagLayout());
		
		List<String> labels = new Vector<String>();
        
		int s = gui.jCBmodel.getSelectedIndex();
		for (String p : CrossoverGUI.parameters[s]) {        	
        	labels.add(p);        	
        }  

		effV.clear();
		for (int i=0;i<labels.size();i++) {        		
			effV.add(new JTextField("1.0", 6));
			effPanel.add(new JLabel(labels.get(i)), effWeights);
			effWeights.gridx++;
			effPanel.add(effV.get(i), effWeights);	
			if ((i+1)%3!=0) {
				effWeights.gridx++;
			} else {
				effWeights.gridx=0;effWeights.gridy++;
			}
		}		
		//for (Component c : effPanel.getComponents()) c.setEnabled(false);
		lsPanel.add(effPanel, cc.xyw(2, rowEff, 3));
		lsPanel.revalidate();
		lsPanel.repaint();
	}
	
	private void createTreatmentNumberPanel() {
		if (ntPanel!=null) {
			lsPanel.remove(ntPanel);
		}

		ntPanel = new JPanel();
		GridBagConstraints cbcWeights = new GridBagConstraints();
		cbcWeights.fill = GridBagConstraints.BOTH;	
		cbcWeights.gridx=0; cbcWeights.gridy=0;
		cbcWeights.gridwidth = 1; cbcWeights.gridheight = 1;
		cbcWeights.ipadx=5; cbcWeights.ipady=5;
		cbcWeights.weightx=1; cbcWeights.weighty=1;
		ntPanel.setBorder(new ComponentTitledBorder(fixedNumber, ntPanel, BorderFactory.createTitledBorder("Weights:")));
		//ntPanel.setBorder(BorderFactory.createTitledBorder("Number of treatment assignments"));

		ntPanel.setLayout(new GridBagLayout());
		
		List<String> labels = new Vector<String>();
        
		int v = Integer.parseInt(gui.spinnerT.getModel().getValue().toString());
		int s = Integer.parseInt(spinnerS.getModel().getValue().toString());
		int p = Integer.parseInt(gui.spinnerP.getModel().getValue().toString());
		
		for (int i=1; i<=v; i++) {        	
        	labels.add("Treatment "+i+":");        	        	
        }      
        
		nV.clear();
		for (int i=0;i<labels.size();i++) {        		
			nV.add(new JTextField(""+((s*p)/v+((i<(s*p)%v)?1:0)), 6));
			ntPanel.add(new JLabel(labels.get(i)), cbcWeights);
			cbcWeights.gridx++;
			ntPanel.add(nV.get(i), cbcWeights);	
			if ((i+1)%3!=0) {
				cbcWeights.gridx++;
			} else {
				cbcWeights.gridx=0;cbcWeights.gridy++;
			}
		}		
		lsPanel.add(ntPanel, cc.xyw(2, rowN, 3));
		lsPanel.revalidate();
		lsPanel.repaint();
	}
	
	private String getVRep() {
		String vrep = "c(";
		for (int i=0; i<nV.size(); i++) {   
			vrep += nV.get(i).getText();
			if (i!=nV.size()-1) vrep += ",";
		}
		return vrep+")";
	}
	
	private String getContrast() {
		return "";
	}
	
	public void addActionListener(ActionListener al) {
		ok.addActionListener(al);
	}
	
	public Design getDesign() {
		return null;
	}
	
	String command = "";
	String models = "";

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == jbCompute) {
			gui.glassPane.start();
			
			command = "s="+spinnerS.getModel().getValue().toString()
					+", "+gui.getParameters()
					+", model=\""+gui.jCBmodel.getSelectedItem()+"\""
					//+", eff.factor="+1
					+", v.rep="+getVRep()
					+", balance.s="+(jbBalanceSequences.isSelected()?"TRUE":"FALSE")
					+", balance.p="+(jbBalancePeriods.isSelected()?"TRUE":"FALSE")
					+(gui.jCBmodel.getSelectedIndex()==4?", model.param=list(placebos="+gui.jtfParam.getText()+")":"")
					+(gui.jCBmodel.getSelectedIndex()==7?", model.param=list(ppp="+gui.jtfParam.getText()+")":"")
					+", verbose=FALSE"
					+", n=c("+jtN1.getText()+", 5)"
					;
			models = (useCatalogueDesigns.isSelected()?", start.designs=\"catalog\"":"");
			
			InfiniteRunningDialog ird = new InfiniteRunningDialog(gui, command, models);
		} else if (e.getSource()==showAlgoPerformance) {			
			RControl.getR().eval("JavaGD(\"Search algorithm performance\")");
			//RControl.getR().eval("png(filename=\""+getTmpFile()+"\")");
			RControl.getR().eval("print(plot(.COresult))");
			//RControl.getR().eval("dev.off()");
		} else if (e.getSource()==exportR) {
			VariableNameDialog vd = new VariableNameDialog(gui, "design");			
			RControl.getR().eval(vd.getName()+" <- getDesign(.COresult)");
		} else if (e.getSource()==jCBMixed) {
			boolean mixed = jCBMixed.getSelectedIndex()==1;
			jlMixed.setEnabled(mixed);
	        jtWithinSubjectRho.setEnabled(mixed);
		}
	}

	private String getTmpFile() {
		String file = System.getProperty("java.io.tmpdir")+"/searchplot"+(new Date()).getTime()+".png";
		System.out.println(file);
		return file;
	}


	public void stateChanged(ChangeEvent e) {
		createWeightsPanel();
		createTreatmentNumberPanel();
	}


	public void searchResultReady(String table) {
		exportR.setEnabled(true);						
		showAlgoPerformance.setEnabled(true);
		jta.clear();
		jta.appendHTML(table);
		String command2 = "paste(capture.output(general.carryover(.COresult)),collapse=\"\\n\")";
		jta.appendParagraph("<pre>"+RControl.getR().eval(command2).asRChar().getData()[0]+"</pre>");		
		jta.appendParagraph("Random seed: TODO");
		jta.appendParagraph("R Code: <pre>"+command+"</pre>");
		jta.setCaretPosition(0);		
	}

}
