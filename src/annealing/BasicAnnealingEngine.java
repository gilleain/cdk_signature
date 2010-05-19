package annealing;

import java.util.ArrayList;
import java.util.Random;

public class BasicAnnealingEngine implements AnnealingEngineI {
	
	private static Random rand = new Random();
	
	private final ArrayList<TemperatureListener> temperatureListeners; 
	
	private int evalSMax;
	private double initialTemperature;
	private double coolingRate;
	private double roundLength;
	
	private AnnealerAdapterI annealerAdapter;
	
	
	public BasicAnnealingEngine(AnnealerAdapterI annealerAdapter, 
			int evalSMax, double initialT, double coolingRate, double roundLength) {
		this.annealerAdapter = annealerAdapter;
		this.evalSMax = evalSMax;
		this.initialTemperature = initialT;
		this.coolingRate = coolingRate;
		this.roundLength = roundLength;
		
		this.temperatureListeners = new ArrayList<TemperatureListener>();
	}

	public void addTemperatureListener(TemperatureListener listener) {
		this.temperatureListeners.add(listener);
	}

	public AnnealerAdapterI run() {
		annealerAdapter.initialState();
		double t = this.initialTemperature;
		
		for (int i = 1; i < evalSMax; i++) {
			annealerAdapter.nextState();
			// the normal annealing process
			if (annealerAdapter.costDecreasing()) {
				annealerAdapter.accept();
			} else {
				double r = rand.nextDouble();
				if (r < Math.pow(Math.E, (annealerAdapter.costDifference() / t))) {
					annealerAdapter.accept();
				} else {
					annealerAdapter.reject();
				}
			}
			t = this.initialTemperature 
					* Math.pow(coolingRate, 
							Math.floor(i / roundLength));
			fireTemperatureEvent(t);
		}
		return annealerAdapter;
	}
	
	private void fireTemperatureEvent(double t) {
		for (TemperatureListener listener : this.temperatureListeners) {
			listener.temperatureChange(t);
		}
	}
	
	public void setAnnealerAdapter(AnnealerAdapterI adapter) {
		this.annealerAdapter = adapter;
	}

}
