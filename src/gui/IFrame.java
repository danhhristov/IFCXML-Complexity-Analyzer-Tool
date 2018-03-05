package gui;

import java.awt.Component;
import java.util.Observable;

/**
 * An interface for the Frame (GUI) Class.
 * 
 * @author Daniel Hristov (2018)
 */
public interface IFrame {
	public Component getFrame();

	public Component[] getComponents();

	public void setVisible(boolean visible);

	public void update(Observable o, Object arg);
	
	public void lockFrame();
	
	public void unlockFrame();
}
