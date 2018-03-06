package tests;

import static org.junit.Assert.*;

import java.io.File;

import javax.swing.Timer;

import org.junit.Before;
import org.junit.Test;

import core.AdvancedMetricsAnalyser;
import core.IStorage;
import core.SimpleMetricsAnalyser;
import core.Storage;
import gui.Frame;
import gui.IFrame;
import util.FrameDetails;
import util.StatusType;
import util.TableRow;

/**
 * A Collection of JUnit tests to validate the functionality of the application
 * 
 * @author Daniel Hristov (2018)
 */
public class UnitTests {
	private IStorage storage;
	private IFrame frame;
	private FrameDetails fd;
	Timer t;
	
	@Before
	public void init(){
		storage = new Storage();
		fd = new FrameDetails("Test Frame", 500, 500, false);
		frame = new Frame(fd, storage);
		frame.setVisible(true);
	}
	
	@Test
	public void testInitialisation(){
		FrameDetails fd_ = null;
		fd_ = new FrameDetails("Test Frame", 500, 500, false);
		assertNotNull(fd_);

		IStorage storage_ = new Storage();
		assertNotNull(storage_);


		IFrame frame_ = new Frame(fd_, storage_);
		assertNotNull(frame_);
		assertNotNull(frame_.getComponents());
	}

	@Test
	public void testFileLoad() {
		//There should be no file after initialisation
		assertNull(storage.getFile());
		//A valid file - should return true when loaded
		assertTrue(storage.setFile(new File("src/tests/Small.ifcxml")));
		assertNotNull(storage.getFile());
		assertEquals(storage.getFile(), new File("src/tests/Small.ifcxml"));
		
		//An invalid file - should return false when loaded.
		assertFalse(storage.setFile(new File("src/test/003.jpg")));
	}
	
	@Test
	public void testStorageStatus(){
		//There should be no status after initialisation
		assertNull(storage.getStatus());
		storage.setStatus("testing", StatusType.NORMAL);
		assertEquals(storage.getStatus(), "testing");
	}
	
	@Test
	public void testStorageAnalysers(){
		//When initialised storage has a default, simple, analyser
		assertTrue(storage.getAnalyser() instanceof SimpleMetricsAnalyser);
		
		storage.setAnalyser(new AdvancedMetricsAnalyser());
		assertTrue(storage.getAnalyser() instanceof AdvancedMetricsAnalyser);
		
	}
	
	@Test
	public void testTableData(){
		//The table should start with 0 rows (no data)
		assertEquals(storage.getTableModel().getRowCount(), 0);
		TableRow tr1 = new TableRow();
		tr1.setMetric("test1");
		tr1.setValue("test1");
		tr1.setMoreInfo("test1");
		storage.addTableRow(tr1);
		assertEquals(storage.getTableModel().getRowCount(), 1);
		assertEquals(storage.getTableRow(0).getMetric(), tr1.getMetric());
		assertEquals(storage.getTableRow(0).getValue(), tr1.getValue());
		assertEquals(storage.getTableRow(0).getMoreInfo(), tr1.getMoreInfo());
		tr1.setMetric("test2");
		tr1.setValue("test2");
		tr1.setMoreInfo("test2");
		storage.addTableRow(tr1);
		assertEquals(storage.getTableModel().getRowCount(), 2);
		assertEquals(storage.getTableModel().getValueAt(0, 0), "test1");
		assertEquals(storage.getTableModel().getValueAt(1, 0), "test2");
	}
	
	@Test
	public void testSimpleAnalysis(){
		storage.setFile(new File("src/tests/Small.ifcxml"));
		storage.setStartAnalysis();
	}
	
	@Test
	public void testAdvancedAnalysis(){
		storage.setFile(new File("src/tests/Small.ifcxml"));
		storage.setAnalyser(new AdvancedMetricsAnalyser());
		storage.setStartAnalysis();
	}

}
