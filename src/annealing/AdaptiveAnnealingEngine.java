package annealing;

import java.util.ArrayList;
import java.util.Random;

/**
 * An annealing engine implementation based on the paper by 
 * Vincent A. Cicirello (On the Design of an 
 * Adaptive Simulated Annealing Algorithm). 
 * 
 * @author maclean
 *
 */
public class AdaptiveAnnealingEngine implements AnnealingEngineI {
	
	private final Random rand = new Random();
	
	private int evalSMax;
	private final ArrayList<TemperatureListener> temperatureListeners; 
	
	private AnnealerAdapterI aa;
	
	public AdaptiveAnnealingEngine(AnnealerAdapterI annealerAdapter, int evalSMax) {
		this.aa = annealerAdapter;
		this.evalSMax = evalSMax;
		this.temperatureListeners = new ArrayList<TemperatureListener>();
	}
	
	public void addTemperatureListener(TemperatureListener listener) {
		this.temperatureListeners.add(listener);
	}
	
	/* 
	 * This implementation is near-identical to the pseudocode of Figure 2.
	 * 
	 * @see generator.AnnealingEngineI#run()
	 * 
	 */
	public AnnealerAdapterI run() {
		aa.initialState();
		double t = 0.5;
		double acceptRate = 0.5;
		
		for (int i = 1; i < evalSMax; i++) {
			aa.nextState();
			// the normal annealing process
			if (aa.costDecreasing()) {
				aa.accept();
				acceptRate = (1.0/500.0) * (499.0 * (acceptRate + 1));
			} else {
				double r = rand.nextDouble();
				if (r < Math.pow(Math.E, (aa.costDifference() / t))) {
					aa.accept();
					acceptRate = (1.0/500.0) * (499.0 * (acceptRate + 1));
				} else {
					aa.reject();
					acceptRate = (1.0/500.0) * (499.0 * (acceptRate));
				}
			}
			
			// calculate the lambda rate
			double lamRate = 0.0;
			if (i/evalSMax < 0.15) {
				lamRate = 0.44 + 0.56 * Math.pow(560, -i/evalSMax/0.15); 
			} else if (i/evalSMax >= 0.15 && i/evalSMax < 0.65) {
				lamRate = 0.44;
			} else if (0.65 <= i/evalSMax) {
				lamRate = 0.44 * Math.pow(440, -(i/evalSMax - 0.65)/0.15);
			}
			
//			System.out.println("lam=" + lamRate + "acceptRate=" + acceptRate);
			
			// use the lambda to adjust the temperature 
			if (acceptRate > lamRate) {
				t = 0.999 * t;
			} else {
				t = t / 0.999;
			}
			fireTemperatureEvent(t);
		}
		return aa;
	}
	
	private void fireTemperatureEvent(double t) {
		for (TemperatureListener listener : this.temperatureListeners) {
			listener.temperatureChange(t);
		}
	}

	public void setAnnealerAdapter(AnnealerAdapterI adapter) {
		this.aa = adapter;
	}

}
