package gui;

import java.awt.Component;
import java.util.Observable;

public interface IFrame {
	public Component getFrame();
	public Component[] getComponents();
	public void setStatusText(String txt);
	public void setVisible(boolean visible);
	public void update(Observable o, Object arg);
}
