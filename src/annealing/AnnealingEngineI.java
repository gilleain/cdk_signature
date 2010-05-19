package annealing;

public interface AnnealingEngineI {

	public void addTemperatureListener(TemperatureListener listener);

	public AnnealerAdapterI run();
	
	public void setAnnealerAdapter(AnnealerAdapterI adapter);

}