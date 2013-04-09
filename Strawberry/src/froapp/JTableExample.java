package froapp;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * A Simple example how JTable works together with TableModel, to help
 * the friend get an understanding of this.
 */
public class JTableExample extends JFrame{
	private static final long serialVersionUID = 2706584734976094158L;

	public static void main(String[] args){
		new JTableExample();
	}

	private JTableExample(){
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		final TableBackend model = new TableBackend();
		model.addData(new String[]{"R","0000"});
		model.addData(new String[]{"R","0000"});
		model.addData(new String[]{"R","0000"});
		JTable table = new JTable(model);
		
		this.add(new JScrollPane(table), BorderLayout.CENTER);
		this.pack();
		this.setVisible(true);
		
		JButton knapp = new JButton("Knapp");
		knapp.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				model.addData(new String[]{"D","0000"});
			}
		});
		this.add(knapp, BorderLayout.SOUTH);
	}
	
	private class TableBackend extends DefaultTableModel{
		private static final long serialVersionUID = 6974327696827037769L;
		private final String[] headers = {"Name","Number"};
		private final ArrayList<Object[]> data = new ArrayList<>();
		
		
		private void addData(Object[] ob){
			data.add(ob);
			fireTableDataChanged();
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return String.class;
		}

		@Override
		public int getColumnCount() {
			return headers.length;
		}

		@Override
		public String getColumnName(int column) {
			return headers[column];
		}

		@Override
		public int getRowCount() {
			if(data != null)
				return data.size();
			else
				return 0;
		}

		@Override
		public Object getValueAt(int row, int column) {
			return data.get(row)[column];
		}
		
	}
}
