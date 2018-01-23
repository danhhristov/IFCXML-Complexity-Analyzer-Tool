package util;

/**
 * FrameDetails is a Data Container for data that goes together when creating a
 * Frame Object. It is used to improve readability and reduce the complexity of
 * passing multiple arguments in method calls.
 * 
 * @author dhris
 *
 */
public class FrameDetails {
	public String title;
	public int width;
	public int height;
	public boolean resizable;

	public FrameDetails(String title, int width, int height, boolean resizable) {
		this.title = title;
		this.width = width;
		this.height = height;
		this.resizable = resizable;
	}
}
