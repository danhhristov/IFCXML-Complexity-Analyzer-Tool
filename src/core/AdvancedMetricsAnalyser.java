package core;

import java.io.File;
import java.util.List;
import java.util.Observer;

import org.w3c.dom.Node;

public class AdvancedMetricsAnalyser extends AbstractAnalyser{

	@Override
	public void analyse(File f) {
		List<Node> nodes = getNodes(f);
		
	}

	@Override
	public void addObserver(Observer o) {
		
	}

	@Override
	public String getFileStats() {
		return null;
	}

	@Override
	public int getStatus() {
		return 0;
	}

}
