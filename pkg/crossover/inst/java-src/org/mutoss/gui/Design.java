package org.mutoss.gui;

import org.mutoss.config.Configuration;

public class Design {
	
	String title;
	String reference;
	String signature;
	int t, s, p;
	String design;
	Double efficiencyAdj = null;
	Double efficiencyUnadj = null;
	String result = null;
	String rName = null;
	String uniqueName = null;
	
	/**
	 * Constuctor of design object
	 * @param title Title for Design. For example "FLETCHER FACTORIAL 2 x 2 DESIGN 3".
	 * @param rName
	 */
	public Design(String title, String rName) {
		this.title = title;		
		this.rName = rName;
		saveDesign2R();
		this.t = RControl.getR().eval("length(levels(as.factor("+uniqueName+")))").asRInteger().getData()[0];		
		int[] dim = RControl.getR().eval("dim("+uniqueName+")").asRInteger().getData();
		p = dim[0];
		s = dim[1];		
		design = RControl.getR().eval("paste(capture.output(dput("+uniqueName+")), collapse=\"\")").asRChar().getData()[0];
		saveDesign2R();
	}
	
	/**
	 * Contructor for crossover design objects. 
	 * @param title
	 * @param rName
	 * @param reference
	 * @param signature
	 * @param t
	 * @param s
	 * @param p
	 * @param design
	 */
	public Design(String title, String rName, String reference, String signature, int t, int s, int p, String design) {
		this.title = title;
		this.rName = rName;
		this.reference = reference;
		this.signature = signature;
		this.t = t;
		this.s = s;
		this.p = p;
		this.design = design;
		double[] eff = RControl.getR().eval("crossover:::getEff("+(rName==null?design:rName)+")").asRNumeric().getData();
		efficiencyUnadj = eff[0];
		efficiencyAdj = eff[1];		
		saveDesign2R();
	}
	
	public String saveDesign2R() {		
		uniqueName = "CODesign."+RControl.getR().eval("digest::digest(getDesign("+rName+"))").asRChar().getData()[0];
		RControl.getR().eval(uniqueName+"<- getDesign("+rName+")");
		return uniqueName;
	}	
	
	public void setRName(String name) {
		//if (RControl.getR().eval("crossover:::isRName("+name+")").asRLogical().getData()[0]) {}
		rName = RControl.getR().eval("make.names("+name+")").asRChar().getData()[0];		
	}
	
	public String getRSignature() {
		String rSignature = "p = "+p+", n = "+s+", t = "+t;
		//if (signature.startsWith(rSignature)) return "same";
		return rSignature;
	}
	
	public String toString() {
		return title+" ("+signature+")";
	}
	
	public String getRDesign() {
		return design;
	}
	
	public String getTextDesign() {
		if (result != null) return result;
		result = RControl.getR().eval("paste(capture.output(print("+(rName==null?design:rName)+")), collapse=\"\\n\")").asRChar().getData()[0];
		/*if (efficiency == null) {
			RControl.getR().eval("design.out<-design.efficiency(design,nseq,ntrt,nper,nrep)");
		}*/
		return result;
	}

	public String getHTMLTable() {		
		String result = RControl.getR().eval("crossover:::getTable("+(rName==null?design:rName)+", type=\""+Configuration.getInstance().getProperty("outputF", "HTML")+"\")").asRChar().getData()[0];
		return result;
	}
}
