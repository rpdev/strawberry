package froapp3;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableRowSorter;

class Gui extends JFrame {
	private static final long serialVersionUID = 800976508415127289L;

	Gui(final TableBackend tableModel){
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		
		final JTable table = new JTable(tableModel);
		
		JButton add = new JButton("Lägg till");
		JButton show = new JButton("Visa");
		show.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						int row = table.getSelectedRow();
						if(row > -1)
							new PriceGui(tableModel.getPriceTable((int) tableModel.getValueAt(row, 0)), "Title");
					}
				});
			}
		});
		JButton remove = new JButton("Radera");
		
		JPanel buttons = new JPanel(new FlowLayout());
		buttons.add(add);
		buttons.add(show);
		buttons.add(remove);
		this.add(buttons, BorderLayout.NORTH);
		
		
		TableRowSorter<TableBackend> sorter = new TableRowSorter<>(tableModel);
		table.setRowSorter(sorter);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.add(new JScrollPane(table), BorderLayout.CENTER);
		this.pack();
		this.setVisible(true);
	}

	private class PriceGui extends JFrame{
		private static final long serialVersionUID = -1224970052450584412L;

		private PriceGui(TableBackend tableModel, String title){
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			this.setTitle(title);
			this.setLayout(new BorderLayout());
			
			JButton add = new JButton("Lägg till");
			JButton remove = new JButton("Radera");
			
			JPanel buttons = new JPanel(new FlowLayout());
			buttons.add(add);
			buttons.add(remove);
			this.add(buttons, BorderLayout.NORTH);
			
			JTable table = new JTable(tableModel);
			TableRowSorter<TableBackend> sorter = new TableRowSorter<>(tableModel);
			table.setRowSorter(sorter);
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			this.add(new JScrollPane(table), BorderLayout.CENTER);
			this.pack();
			this.setVisible(true);
		}
	}
}
