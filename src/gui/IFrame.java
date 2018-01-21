package gui;

import java.awt.Component;

public interface IFrame {
	public Component getFrame();
	public Component[] getComponents();
	public void setStatusText(String txt);
	public void setVisible(boolean visible);
}
