package engine;

import java.util.ArrayList;
import java.util.List;

public abstract class ExtractionEngine {
	private List<ExtractionEngine> engines = new ArrayList<ExtractionEngine>();
	public abstract void intializeDatabases();
	public abstract void initializeSourceAgent();
	public abstract void initializeTargetAgent();
	public abstract void insertsIntoTarget();
	public List<ExtractionEngine> getEngines() {
		return engines;
	}
	public void setEngines(List<ExtractionEngine> engines) {
		this.engines = engines;
	}
	
	public void iterates(){
		for(ExtractionEngine engine : engines){
			engine.insertsIntoTarget();
		}
	}
}
