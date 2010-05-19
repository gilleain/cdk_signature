package annealing;

public class IntState implements State {

	public final int stepIndex;
	public final int i;
	
	public IntState(int i, int stepIndex) {
		this.i = i;
		this.stepIndex = stepIndex;
	}
	
	public int getStep() {
		return this.stepIndex;
	}
	
}
