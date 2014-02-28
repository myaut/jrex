package com.tuneit.jrex.ui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import java.awt.Canvas;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JSeparator;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SpringLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class AboutDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();

	private final String jrexDescription = 
			"<html><strong>Java Request Extractor</strong> is a tool that allows " +
			"to use DTrace or SystemTap for instrumenting Glassfish " +
			"application server</html>";
	
	private final String jrexHref = "<html><a href=\"http://www.tune-it.ru/\">www.tune-it.ru</a></html>";
	private final String jrexTuneITUri = "http://www.tune-it.ru/";
	
	private JLabel lblJavaRequestExtractor;
	private JPanel panel;
	private JLabel lblCopyright;
	private JLabel lblAuthorSergeyKlyaus;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			AboutDialog dialog = new AboutDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public AboutDialog() {
		/* FIXME: change to JAR path */		
		setTitle("About");
		setBounds(100, 100, 450, 360);
		
		/* FIXME: change to JAR path */
		ImageIcon iconJrex = new ImageIcon("../icon.png");
		setIconImage(iconJrex.getImage());
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		SpringLayout sl_contentPanel = new SpringLayout();
		contentPanel.setLayout(sl_contentPanel);
		{
			panel = new LogoPanel();
			sl_contentPanel.putConstraint(SpringLayout.NORTH, panel, 10, SpringLayout.NORTH, contentPanel);
			sl_contentPanel.putConstraint(SpringLayout.WEST, panel, 5, SpringLayout.WEST, contentPanel);
			sl_contentPanel.putConstraint(SpringLayout.SOUTH, panel, 102, SpringLayout.NORTH, contentPanel);
			sl_contentPanel.putConstraint(SpringLayout.EAST, panel, 437, SpringLayout.WEST, contentPanel);
			panel.setBorder(null);
			contentPanel.add(panel);
			panel.setLayout(new BorderLayout(0, 0));
			{
				JLabel lblJrex = new JLabel("J-REX");
				lblJrex.setFont(new Font("Dialog", Font.BOLD, 36));
				panel.add(lblJrex, BorderLayout.EAST);
			}
		}
		{
			lblJavaRequestExtractor = new JLabel(jrexDescription);
			sl_contentPanel.putConstraint(SpringLayout.WEST, lblJavaRequestExtractor, 5, SpringLayout.WEST, contentPanel);
			sl_contentPanel.putConstraint(SpringLayout.EAST, lblJavaRequestExtractor, -102, SpringLayout.EAST, contentPanel);
			contentPanel.add(lblJavaRequestExtractor);
		}
		{
			lblCopyright = new JLabel("© Tune-IT 2013-2014");
			sl_contentPanel.putConstraint(SpringLayout.WEST, lblCopyright, 5, SpringLayout.WEST, contentPanel);
			contentPanel.add(lblCopyright);
		}
		{
			lblAuthorSergeyKlyaus = new JLabel("Author: Sergey Klyaus");
			sl_contentPanel.putConstraint(SpringLayout.WEST, lblAuthorSergeyKlyaus, 5, SpringLayout.WEST, contentPanel);
			sl_contentPanel.putConstraint(SpringLayout.SOUTH, lblAuthorSergeyKlyaus, -5, SpringLayout.NORTH, lblCopyright);
			sl_contentPanel.putConstraint(SpringLayout.SOUTH, lblJavaRequestExtractor, -28, SpringLayout.NORTH, lblAuthorSergeyKlyaus);
			contentPanel.add(lblAuthorSergeyKlyaus);
		}
		{
			JLabel lblwwwtuneitru = new JLabel(jrexHref);
			lblwwwtuneitru.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					try {
						Desktop.getDesktop().browse(new URI(jrexTuneITUri));
					}
					catch (URISyntaxException e1) {
                        return;
					}
					catch(IOException e2) {
						return;
					}
					catch(UnsupportedOperationException e3) {
						/* Linux + KDE? */
						String command = "xdg-open " + jrexTuneITUri;
						
						try {
							Process p = Runtime.getRuntime().exec(command);
							/* FIXME: Waiting is bad here */
							p.waitFor();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
				}
			});
			sl_contentPanel.putConstraint(SpringLayout.WEST, lblwwwtuneitru, 5, SpringLayout.WEST, contentPanel);
			sl_contentPanel.putConstraint(SpringLayout.EAST, lblwwwtuneitru, -102, SpringLayout.EAST, contentPanel);
			sl_contentPanel.putConstraint(SpringLayout.SOUTH, lblCopyright, -6, SpringLayout.NORTH, lblwwwtuneitru);
			sl_contentPanel.putConstraint(SpringLayout.NORTH, lblwwwtuneitru, 258, SpringLayout.NORTH, contentPanel);
			contentPanel.add(lblwwwtuneitru);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						getDialog().setVisible(false);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}

	@SuppressWarnings("serial")
	public class LogoPanel extends JPanel {
		private Image logo = null;
		
		public LogoPanel() {
			super();
			
			try {
				/* FIXME: change to JAR path */
				logo = ImageIO.read(new File("../tuneit.png"));
			} catch (IOException e) {
			  e.printStackTrace();
			}
		}
		
		public void paintComponent(Graphics g) {
	        super.paintComponent(g);       

	        if(logo != null) {
	        	g.drawImage(logo, 
						0, 0, logo.getWidth(null), logo.getHeight(null), 
						0, 0, logo.getWidth(null), logo.getHeight(null), 
						null);
	        }
	    }  
	}
	
	public AboutDialog getDialog() {
		return this;
	}
}
