package com.tuneit.jrex.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;

import com.tuneit.jrex.expressions.ExprFactory;
import com.tuneit.jrex.probes.ProbeManager;

public class GenerateScriptDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	
	private int tool = ExprFactory.NOTOOL;
	
	private JCheckBox chckbxGlassfish = new JCheckBox("Glassfish");
	private JCheckBox chckbxScheduler = new JCheckBox("OS Scheduler");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			GenerateScriptDialog dialog = new GenerateScriptDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public GenerateScriptDialog() {
		setTitle("Generate script");
		setBounds(100, 100, 315, 314);
		
		/* FIXME: change to JAR path */
		ImageIcon iconJrex = new ImageIcon("../icon.png");
		setIconImage(iconJrex.getImage());
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		{
			chckbxGlassfish.setEnabled(false);
			chckbxGlassfish.setSelected(true);
			contentPanel.add(chckbxGlassfish);
		}
		{
			chckbxScheduler.setSelected(true);
			contentPanel.add(chckbxScheduler);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				final JFileChooser fc = new JFileChooser();				
				
				JButton okButton = new JButton("Save");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(getTool() == ExprFactory.SYSTEMTAP) {
							fc.setFileFilter(new GenerateScriptDialog.SystemTapFileFilter());
							fc.setSelectedFile(new File("jrex.stp"));
						}
						else if(getTool() == ExprFactory.DTRACE) {
							fc.setFileFilter(new GenerateScriptDialog.DTraceFileFilter());
							fc.setSelectedFile(new File("jrex.d"));
						}
						
						int returnVal = fc.showSaveDialog(getDialog());
						
						if(returnVal == JFileChooser.APPROVE_OPTION) {
							File f = fc.getSelectedFile();
							
							if(f.exists()) {
								int doRewrite = 
									JOptionPane.showConfirmDialog(getDialog(),
								        "File '" + f.getAbsolutePath() + "' already exists! Rewrite?", 
								        "File already exists!", JOptionPane.YES_NO_OPTION);
								
								if(doRewrite == JOptionPane.NO_OPTION) {
									return;
								}
							}
							
							saveFile(f);
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	public GenerateScriptDialog getDialog() {
		return this;
	}
	
	public int getTool() {
		return tool;
	}

	public void setTool(int tool) {
		this.tool = tool;
	}
	
	private void saveFile(File f) {
		ExprFactory ef = new ExprFactory(getTool());
		ProbeManager pm = new ProbeManager(ef);
		ArrayList<String> groupsList = new ArrayList<String>();
		
		/* Let's process checkboxes */
		if(chckbxGlassfish.isSelected()) {
			groupsList.add("glassfish");
		}
		if(chckbxScheduler.isSelected()) {
			groupsList.add("ivcs");
			groupsList.add("vcs");
		}
		
		/* Write code */
		try {
			String groups[] = groupsList.toArray(new String[groupsList.size()]);
			PrintStream script = new PrintStream(f);
			
			pm.writeCode(script, groups);
		} 
		catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(this, "Error while opening script:" + e1.toString(),
											"Error!", JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
			return;
		} 
			
		setVisible(false);
	}

	public class SystemTapFileFilter extends FileFilter {
		@Override
		public boolean accept(File f) {
			if (f.isDirectory()) {
	            return true;
	        }
			
			return f.getName().endsWith(".stp");
		}

		@Override
		public String getDescription() {
			return "SystemTap scripts (*.stp)";
		}
		
	}
	
	public class DTraceFileFilter extends FileFilter {
		@Override
		public boolean accept(File f) {
			if (f.isDirectory()) {
	            return true;
	        }
			
			return f.getName().endsWith(".d");
		}

		@Override
		public String getDescription() {
			return "DTrace scripts (*.d)";
		}	
	}
}
