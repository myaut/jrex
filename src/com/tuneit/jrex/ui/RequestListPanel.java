package com.tuneit.jrex.ui;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import com.tuneit.jrex.probes.Request;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;

public class RequestListPanel extends JPanel {
	private JTable requestTable;
	
	class RequestTableModel extends AbstractTableModel {
		final String[] requestTableColumns = {"RQID", "TID", "METHOD", "URI", "STATUS", "TIME (ms)"};
		
		static final int RQT_COL_RQID = 0;
		static final int RQT_COL_TID = 1;
		static final int RQT_COL_METHOD = 2;
		static final int RQT_COL_URI = 3;
		static final int RQT_COL_STATUS = 4;
		static final int RQT_COL_TIME = 5;
		
		static final int RQT_REQUEST = 6;	/* Special column that keeps link back to 
		 									   requests list. Not shown anyway */
		
		static final int RQT_COLUMN_COUNT = 6;
		
		static final double T_MSEC = 1000000.0;
		
		private List<Request> requests = null;
		private Object[][] data = null;
		
		public int getColumnCount() {
	        return RQT_COLUMN_COUNT;
	    }
		
		public String getColumnName(int col) {
	        return requestTableColumns[col];
	    }
		
		public int getRowCount() {
			if(data != null)
				return data.length;
			
	        return 0;
	    }

	    public Object getValueAt(int row, int col) {
	    	if(data != null)
	    		return data[row][col];
	    	
	        return "";
	    }

	    public Class getColumnClass(int col) {
	    	switch(col) {
	    	case RQT_COL_RQID:
	    	case RQT_COL_STATUS:	    		
	    		return Integer.class;
	    	case RQT_COL_TID:	    		
	    		return Long.class;
	    	case RQT_COL_METHOD:	    	
	    	case RQT_COL_URI:	    		
	        	return String.class;
	    	case RQT_COL_TIME:
	    		return Double.class;
	    	}
	    	
	    	return Object.class;
	    }
	    
	    /* ---------------- */
	    
	    void setRequestList(List<Request> requests) {
			this.requests = requests;
			
			this.data = new Object[requests.size()][RQT_COLUMN_COUNT + 1];
			
			int i = 0;
			for(Request rq : requests) {
				/*
				if(!rq.getGroup().equals("glassfish"))
					continue;
				*/
				
				this.data[i][RQT_COL_RQID] = new Integer(i);
				this.data[i][RQT_COL_TID] = new Long(rq.getTid());
				this.data[i][RQT_COL_METHOD] = rq.getStringParam("method");
				this.data[i][RQT_COL_URI] = rq.getStringParam("uri");
				this.data[i][RQT_COL_STATUS] = new Long(rq.getLongParam("status"));
				
				this.data[i][RQT_COL_TIME] = new Double(((double) rq.getTime()) / T_MSEC);
				
				this.data[i][RQT_REQUEST] = rq;
				
				++i;
			}
		}
	    
	    Request getRequest(int rqId) {
	    	return (Request) this.data[rqId][RQT_REQUEST];
	    }
	}
	
	private RequestTableModel requestTableModel;
	
	/**
	 * Create the panel.
	 */
	public RequestListPanel(List<Request> requests) {
		setLayout(new BorderLayout(0, 0));
		
		JPanel panelButtons = new JPanel();
		add(panelButtons, BorderLayout.SOUTH);
		panelButtons.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JPanel panel = getPanel();
				JTabbedPane tabPane = (JTabbedPane) panel.getParent();
				
				tabPane.remove(panel);
			}
		});
		
		final JButton btnViewRequest = new JButton("View request");
		btnViewRequest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JPanel panel = getPanel();
				JTabbedPane tabPane = (JTabbedPane) panel.getParent();
				
				int[] selectedRows = requestTable.getSelectedRows();				
				for(int selectedRow : selectedRows) {
					Request rq = requestTableModel.getRequest(selectedRow);
					RequestViewPanel rqViewPanel = new RequestViewPanel(rq);
					
					tabPane.addTab(Integer.toString(selectedRow), rqViewPanel);					
					tabPane.setSelectedComponent(rqViewPanel);
				}
			}
		});
		btnViewRequest.setEnabled(false);
		panelButtons.add(btnViewRequest);
		panelButtons.add(btnClose);
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		requestTableModel = new RequestTableModel();
		requestTableModel.setRequestList(requests);
		
		requestTable = new JTable(requestTableModel);
		scrollPane.setViewportView(requestTable);	
		
		/* Set preferred width for columns */
		setColumnWidthRange(RequestTableModel.RQT_COL_RQID, 40, 64);
		setColumnWidthRange(RequestTableModel.RQT_COL_TID, 64, 96);
		setColumnWidthRange(RequestTableModel.RQT_COL_STATUS, 64, 96);
		setColumnWidthRange(RequestTableModel.RQT_COL_METHOD, 64, 96);
		setColumnWidthRange(RequestTableModel.RQT_COL_TIME, 96, 128);
		requestTable.getColumnModel().getColumn(RequestTableModel.RQT_COL_URI).setMinWidth(256);
		
		/* Enable selection */
		ListSelectionModel selectionModel = requestTable.getSelectionModel();
		selectionModel.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
		        int[] selectedRows = requestTable.getSelectedRows();		        
		        btnViewRequest.setEnabled(selectedRows.length != 0);
			}
		});
	}

	void setColumnWidthRange(int col, int minWidth, int maxWidth) {
		TableColumn tableColumn = requestTable.getColumnModel().getColumn(col);
		
		tableColumn.setMinWidth(minWidth);
		tableColumn.setMaxWidth(maxWidth);
	}
	
	RequestListPanel getPanel() {
		return this;
	}
}
