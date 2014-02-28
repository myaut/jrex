package com.tuneit.jrex.ui;

import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.SwingConstants;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ButtonGroup;

import com.tuneit.jrex.expressions.ExprFactory;

public class ApplicationWindow {

	private JFrame frmJrex;
	private ButtonGroup rdbtnGroup;
	private JMenuItem mntmGenerateTraceScript;
	
	GenerateScriptDialog dlgGenerateScript;

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
		
		final JRadioButtonMenuItem rdbtnmntmDtrace = new JRadioButtonMenuItem("DTrace");
		rdbtnGroup.add(rdbtnmntmDtrace);
		mnNewMenu.add(rdbtnmntmDtrace);		
		rdbtnmntmDtrace.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				mntmGenerateTraceScript.setEnabled(true);
			}
		});
		
		final JRadioButtonMenuItem rdbtnmntmSystemtap = new JRadioButtonMenuItem("SystemTap");
		rdbtnGroup.add(rdbtnmntmSystemtap);
		mnNewMenu.add(rdbtnmntmSystemtap);
		rdbtnmntmSystemtap.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				mntmGenerateTraceScript.setEnabled(true);
			}
		});
		
		JSeparator separator = new JSeparator();
		mnNewMenu.add(separator);
		
		mntmGenerateTraceScript = new JMenuItem("Generate trace script");
		mntmGenerateTraceScript.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dlgGenerateScript = new GenerateScriptDialog();
				
				if(rdbtnmntmDtrace.isSelected()) {
					dlgGenerateScript.setTool(ExprFactory.DTRACE);
				}
				else if(rdbtnmntmSystemtap.isSelected()) {
					dlgGenerateScript.setTool(ExprFactory.SYSTEMTAP);
				}
				
				dlgGenerateScript.setVisible(true);
			}
		});
		mntmGenerateTraceScript.setEnabled(false);
		mnNewMenu.add(mntmGenerateTraceScript);
		
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
	}

}
