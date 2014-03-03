package com.tuneit.jrex.ui;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JTabbedPane;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JSplitPane;
import javax.swing.JTree;

import com.tuneit.jrex.probes.Request;
import java.awt.FlowLayout;
import javax.swing.JTextPane;
import javax.swing.JButton;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JScrollPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JScrollBar;
import java.awt.Canvas;
import javax.swing.JSeparator;
import java.awt.Button;
import java.util.ArrayList;
import java.util.List;

public class RequestViewPanel extends JPanel {
	private Request request;
	private final JLabel lblRequestInfo = new JLabel("Request info");
	
	private RequestGraphPanel panelRequestView;
	
	static final double T_MSEC = 1000000.0;
	
	/**
	 * Create the panel.
	 */
	public RequestViewPanel(Request request) {
		this.request = request;
		
		setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPaneMain = new JSplitPane();
		splitPaneMain.setOrientation(JSplitPane.VERTICAL_SPLIT);
		add(splitPaneMain, BorderLayout.CENTER);
		
		JSplitPane splitPaneTop = new JSplitPane();
		splitPaneMain.setLeftComponent(splitPaneTop);
		
		JPanel panelRight = new JPanel();
		splitPaneTop.setRightComponent(panelRight);
		panelRight.setLayout(new BoxLayout(panelRight, BoxLayout.Y_AXIS));
		lblRequestInfo.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblRequestInfo.setHorizontalAlignment(SwingConstants.LEFT);
		panelRight.add(lblRequestInfo);
		
		JScrollPane scrollPane = new JScrollPane();
		splitPaneTop.setLeftComponent(scrollPane);
		
		final JTree tree = new JTree(generateRequestNode(request));
		scrollPane.setViewportView(tree);
		
		tree.setMinimumSize(new Dimension(256, 192));
		
		JPanel panelBottom = new JPanel();
		splitPaneMain.setRightComponent(panelBottom);
		panelBottom.setLayout(new BorderLayout(0, 0));
		
		JScrollBar scrollBar = new JScrollBar();
		scrollBar.setOrientation(JScrollBar.HORIZONTAL);
		panelBottom.add(scrollBar, BorderLayout.NORTH);
		
		panelRequestView = new RequestGraphPanel(scrollBar, request);
		panelBottom.add(panelRequestView);
		
		JPanel panelRightButtons = new JPanel();
		panelBottom.add(panelRightButtons, BorderLayout.SOUTH);
		panelRightButtons.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JPanel panel = getPanel();
				JTabbedPane tabPane = (JTabbedPane) panel.getParent();
				
				tabPane.remove(panel);
			}
		});
		
		final JLabel lblRequest = new JLabel("");
		panelRightButtons.add(lblRequest);
		panelRightButtons.add(btnClose);
		
		JButton btnZoomIn = new JButton("Zoom in");
		btnZoomIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelRequestView.zoomIn();
			}
		});
		panelRightButtons.add(btnZoomIn);
		
		JButton btnZoomOut = new JButton("Zoom out");
		panelRightButtons.add(btnZoomOut);
		btnZoomOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelRequestView.zoomOut();
			}
		});	
		
		TreeSelectionModel selectionModel = tree.getSelectionModel();
		selectionModel.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				/* Generate request description in text pane */
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)
		                tree.getLastSelectedPathComponent();
				
				setRequestInfo((Request) node.getUserObject());
			}
		});
		
		panelRequestView.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				Request rq = panelRequestView.findRequest(e.getX(), e.getY());
				
				if(rq != null) {
					lblRequest.setText(rq.toString());
				}
			}				
		});
		
		panelRequestView.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Request rq = panelRequestView.findRequest(e.getX(), e.getY());
				
				if(rq != null) {
					setRequestInfo(rq);
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}				
		});
	}

	private DefaultMutableTreeNode generateRequestNode(Request rqRoot) {
		/* Recursive */
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(rqRoot);
		
		for(Request rq : rqRoot.getSubRequests()) {
			node.add(generateRequestNode(rq));
		}
		
		return node;
	}
	
	private void setRequestInfo(Request rq) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("<html>");
		
		sb.append("<h2>");
		sb.append(rq.toString());
		sb.append("</h2> <br />");
		
		sb.append("<b>tid: </b>");
		sb.append(rq.getTid());				
		sb.append("<br />");
		
		sb.append("<b>Start time: </b>");
		sb.append(rq.getStartTime());				
		sb.append("<br />");
		
		sb.append("<b>Total time: </b>");
		sb.append(((double) rq.getTime()) / T_MSEC);				
		sb.append("ms <br />");
		
		sb.append("<h3>Params</h3>");
		
		for(String paramName : rq.getParamNames()) {
			sb.append("<b>");
			sb.append(paramName);
			sb.append(": </b>");
			sb.append(rq.getStringParam(paramName));				
			sb.append("<br />");	
		}
		
		sb.append("</html>");
		
		lblRequestInfo.setText(sb.toString());
	}
	
	RequestViewPanel getPanel() {
		return this;
	}
	
	public class RequestGraphPanel extends JPanel {
		private Request rq;
		private List<RequestRect> rqRectangles;
		
		private JScrollBar scrollBar;
		
		private double pxPerMS = 20;
		
		private long leftTime = 0;		/* left time mark in MS */
		
		private double minPxPerMS = 4;
		private double maxPxPerMS = 100;
		
		private int maxIndent = 1;
		
		static final boolean DEBUG = true;
		
		RequestGraphPanel(JScrollBar scrollBar, Request rq) {
			this.scrollBar = scrollBar;
			this.rq = rq;
			
			this.scrollBar.setMinimum(0);
			this.scrollBar.setMaximum((int) Math.ceil((double) rq.getTime() / T_MSEC));
			
			this.scrollBar.addAdjustmentListener(new AdjustmentListener() {
				public void adjustmentValueChanged(AdjustmentEvent e) {
					leftTime = (long) Math.ceil(((double) e.getValue()) * T_MSEC);
					repaint();
				}				
			});
			
			this.maxIndent = calcMaxIndent(rq);
			
			this.rqRectangles = new ArrayList<RequestRect>();
		}
		
		private int calcMaxIndent(Request rqRoot) {
			int maxIndentChild = 0;
			int indentChild = 0;
			
			for(Request rq : rqRoot.getSubRequests()) {
				indentChild = calcMaxIndent(rq);
				
				if(indentChild > maxIndentChild) {
					maxIndentChild = indentChild;
				}
			}
			
			return maxIndentChild + 1;
		}
		
		public Request findRequest(int x, int y) {
			for(RequestRect rect : rqRectangles) {
				if(rect.isInside(x, y)) {
					return rect.getRequest();
				}
			}
			
			return null;
		}
		
		public void zoomIn() {
			if(pxPerMS < maxPxPerMS) {
				pxPerMS *= 2;
			}
			
			repaint();
		}
		
		public void zoomOut() {
			if(pxPerMS > minPxPerMS) {
				pxPerMS /= 2;
				
				if(pxPerMS < minPxPerMS) {
					pxPerMS = minPxPerMS;
				}
			}
			
			repaint();
		}
		
		public void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        
	        int height = getHeight();	        
	        int width = getWidth();
	        
	        int rectHeight = height / maxIndent;
	        
	        long rightTime = leftTime + (int) (((double) width) / pxPerMS * T_MSEC);
	        
	        this.rqRectangles = new ArrayList<RequestRect>();
	        
	        paintRequest(g, rq, rightTime, 0, rectHeight);
		}
		
		private void paintRequest(Graphics g, Request rqRoot, long rightTime, int indent, int rectHeight) {
			long normStartTime = rqRoot.getStartTime() - this.rq.getStartTime();
			long normEndTime = normStartTime + rqRoot.getTime();
			
			if(normStartTime < rightTime && normEndTime > leftTime) {
				/* Request in view port */
				int startX = 0;
				int endX = getWidth();
				
				if(normStartTime > leftTime) {
					startX = timeToX(normStartTime - leftTime);
				}
				
				if(normEndTime < rightTime) {
					endX = timeToX(normEndTime - leftTime);
				}
				
				RequestRect rect = new RequestRect(rqRoot, startX, endX, indent, rectHeight);				
				rect.paint(g);
				
				this.rqRectangles.add(rect);
			}
			else if(DEBUG) {
				System.out.println(String.format("Request %s [%d;%d] is out of view port [%d;%d]",
						rqRoot.toString(), normStartTime, normEndTime, leftTime, rightTime));
			}
			
			for(Request rq : rqRoot.getSubRequests()) {
				paintRequest(g, rq, rightTime, indent + 1, rectHeight);
			}
		}
		
		private int timeToX(long time) {
			return (int) Math.floor((((double) time) / T_MSEC) * pxPerMS);
		}
		
		public class RequestRect {
			public RequestRect(Request rq, int startX, int endX, int indent,
					int height) {
				this.rq = rq;
				this.startX = startX;
				this.endX = endX;
				this.indent = indent;
				this.height = height;
			}

			private Request rq;
			
			private int startX;
			private int endX;
			
			private int indent;
			private int height;
			
			public void paint(Graphics g) {
				if(DEBUG) {
					System.out.println(String.format("Painting %s at (%d;%d) wh: (%d;%d)", rq.toString(), 
						startX, indent * height, endX - startX, height));
				}
				
				g.drawRect(startX, indent * height, endX - startX, height);
			}
			
			public Request getRequest() {
				return rq;
			}
			
			public boolean isInside(int x, int y) {
				return (x > startX) && (x < endX) &&
					   (y > indent * height) && (y < ((indent + 1) * height));
			}
		}
	}
}
