package annealing;

import org.openscience.cdk.interfaces.IMolecule;

public class MoleculeState implements State {
	
	public enum Acceptance { ACCEPT, REJECT };
	
	public final IMolecule molecule;
	
	public final Acceptance acceptance;
	
	public final int stepIndex;
	
	public MoleculeState(IMolecule molecule, Acceptance acceptance, int stepIndex) {
		this.molecule = molecule;
		this.acceptance = acceptance;
		this.stepIndex = stepIndex;
	}
	
	public int getStep() {
		return this.stepIndex;
	}

}
