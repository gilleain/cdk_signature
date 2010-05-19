package annealing;

import java.util.ArrayList;
import java.util.Random;

/**
 * Holds the state for the AnnealingEngine, and fires off 
 * state change events to listeners.
 * 
 * @author maclean
 *
 */
public class TrivialAnnealerAdapter implements AnnealerAdapterI {
	
	public final int NUMBER_RANGE;
	
	private static Random rand = new Random();
	
	private final ArrayList<StateListener> stateListeners;
	
	private int s;
	private int sPrime;
	private int bestState;
	
	private int target; 
	
	public TrivialAnnealerAdapter(int target, int numberRange) {
		this.NUMBER_RANGE = numberRange;
		
		this.target = target;
		this.s = -1;
		this.sPrime = -1;
		this.bestState = s;
		this.stateListeners = new ArrayList<StateListener>();
	}
	
	public int getBestState() {
		return this.bestState;
	}

	public void addStateListener(StateListener listener) {
		this.stateListeners.add(listener);
	}
	
	public void nextState() {
		this.sPrime = pickRandomState(neighbourhood(this.s));
	}
	
	public void initialState() {
		this.s = rand.nextInt(NUMBER_RANGE);
	}
	
	public void accept() {
		this.s = this.sPrime;
		if (cost(this.s) < cost(this.bestState)) {
			this.bestState = this.s;
		}
		this.fireStateEvent(new IntState(s, 1));	// XXX technically, broken (stepIndex)
	}
	
	public void reject() {
		this.fireStateEvent(new IntState(s, 1));	// XXX technically, broken (stepIndex)
	}
	
	public double costDifference() {
		return (double)(cost(this.s) - cost(this.sPrime));
	}
	
	public boolean costDecreasing() {
		return cost(this.sPrime) < cost(this.s);
	}
	
	private int cost(int state) {
		return Math.abs(this.target - state);
	}

	private void fireStateEvent(State state) {
		for (StateListener listener : this.stateListeners) {
			listener.stateChanged(state);
		}
	}

	private int pickRandomState(int[] neighbourhood) {
		return neighbourhood[rand.nextInt(neighbourhood.length)];
	}

	private int[] neighbourhood(int state) {
		int r = 10;	// the 'radius' of the neighbourhood
		int[] neighbours = new int[r * 2];
		int k = 0;
		for (int i = state - r; i < state + r; i++) {
			neighbours[k++] = i;
		}
		return neighbours;
	}
}
