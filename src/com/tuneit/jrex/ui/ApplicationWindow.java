package com.tuneit.jrex.ui;

import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ButtonGroup;

import com.tuneit.jrex.expressions.ExprFactory;
import com.tuneit.jrex.probes.ProbeManager;
import com.tuneit.jrex.probes.Request;

import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class ApplicationWindow {

	private JFrame frmJrex;
	private ButtonGroup rdbtnGroup;
	private JMenuItem mntmGenerateTraceScript;
	private JMenuItem mntmLoadTraceFile;
	private JTabbedPane tabbedPane;
	private JRadioButtonMenuItem rdbtnmntmSystemtap;
	private JRadioButtonMenuItem rdbtnmntmDtrace;
	
	private GenerateScriptDialog dlgGenerateScript;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ApplicationWindow window = new ApplicationWindow();
					window.frmJrex.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ApplicationWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmJrex = new JFrame();
		frmJrex.setTitle("J-REX");
		frmJrex.setBounds(100, 100, 632, 561);
		frmJrex.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		/* FIXME: change to JAR path */
		ImageIcon iconJrex = new ImageIcon("../icon.png");
		frmJrex.setIconImage(iconJrex.getImage());
		
		rdbtnGroup = new ButtonGroup();
		
		JMenuBar menuBar = new JMenuBar();
		frmJrex.setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("File");
		menuBar.add(mnNewMenu);
		
		rdbtnmntmDtrace = new JRadioButtonMenuItem("DTrace");
		rdbtnGroup.add(rdbtnmntmDtrace);
		mnNewMenu.add(rdbtnmntmDtrace);		
		rdbtnmntmDtrace.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				mntmGenerateTraceScript.setEnabled(true);
				mntmLoadTraceFile.setEnabled(true);
			}
		});
		
		rdbtnmntmSystemtap = new JRadioButtonMenuItem("SystemTap");
		rdbtnGroup.add(rdbtnmntmSystemtap);
		mnNewMenu.add(rdbtnmntmSystemtap);
		rdbtnmntmSystemtap.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				mntmGenerateTraceScript.setEnabled(true);
				mntmLoadTraceFile.setEnabled(true);
			}
		});
		
		JSeparator separator = new JSeparator();
		mnNewMenu.add(separator);
		
		mntmGenerateTraceScript = new JMenuItem("Generate trace script");
		mntmGenerateTraceScript.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dlgGenerateScript = new GenerateScriptDialog();
				dlgGenerateScript.setTool(getInstrumentingTool());
				
				dlgGenerateScript.setVisible(true);
			}
		});
		mntmGenerateTraceScript.setEnabled(false);
		mnNewMenu.add(mntmGenerateTraceScript);
		
		mntmLoadTraceFile = new JMenuItem("Load trace file");
		mntmLoadTraceFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fc = new JFileChooser();	
				
				int returnVal = fc.showOpenDialog(frmJrex);
				
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					File f = fc.getSelectedFile();
					loadTraceFile(f);
				}
			}
		});
		mntmLoadTraceFile.setEnabled(false);
		mnNewMenu.add(mntmLoadTraceFile);
		
		JSeparator separator_1 = new JSeparator();
		mnNewMenu.add(separator_1);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mnNewMenu.add(mntmExit);
		
		JMenuItem mntmFile = new JMenuItem("About...");
		mntmFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AboutDialog dlgAbout = new AboutDialog();
				dlgAbout.setVisible(true);
			}
		});
		mntmFile.setHorizontalAlignment(SwingConstants.RIGHT);
		menuBar.add(mntmFile);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmJrex.getContentPane().add(tabbedPane, BorderLayout.CENTER);
	}

	private int getInstrumentingTool() {
		if(rdbtnmntmDtrace.isSelected()) {
			return ExprFactory.DTRACE;
		}
		else if(rdbtnmntmSystemtap.isSelected()) {
			return ExprFactory.SYSTEMTAP;
		}
		
		return ExprFactory.NOTOOL;
	}
	
	private void loadTraceFile(File f) {
		ExprFactory ef = new ExprFactory(getInstrumentingTool());
		ProbeManager pm = new ProbeManager(ef);
		
		try {
			FileReader r = new FileReader(f);
			List<Request> requests = pm.parseLog(r);
			
			RequestListPanel panelRequestList = new RequestListPanel(requests);
			
			tabbedPane.addTab(f.getName(), panelRequestList);
		} 
		catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(frmJrex, "Trace file not found: " + e1.toString(),
											"Error!", JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
			return;
		} 
		catch(IOException e2) {
			JOptionPane.showMessageDialog(frmJrex, "IO error while opening trace file: " + e2.toString(),
					"Error!", JOptionPane.ERROR_MESSAGE);
			
			e2.printStackTrace();
			return;
		}
	}
}
