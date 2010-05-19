package annealing;

public interface AnnealerAdapterI {

	public void addStateListener(StateListener listener);

	/**
	 * Generate an initial state (internally).
	 */
	public void initialState();

	/**
	 * Generate a new state internally, and store it for acceptance/rejection.
	 */
	public void nextState();

	/**
	 * @return true if the cost of the next state is 
	 * less than the cost of the current.
	 */
	public boolean costDecreasing();

	/**
	 * Accept the next state.
	 */
	public void accept();

	/**
	 * Reject the next state, go back to current. 
	 */
	public void reject();

	/**
	 * @return the difference between the cost of the current and next states.
	 */
	public double costDifference();
	
}