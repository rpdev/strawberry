package froapp3;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableRowSorter;

import froapp3.Database.Berries;

class Gui extends JFrame {
	private static final long serialVersionUID = 800976508415127289L;

	Gui(final TableBackend tableModel){
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		
		final JTable table = new JTable(tableModel);
		
		JButton add = new JButton("Lägg till");
		add.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						new AddBerry(tableModel);
					}
				});
			}
		});
		JButton show = new JButton("Visa");
		show.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						int row = table.getSelectedRow();
						if(row > -1){
							int id = (int) tableModel.getValueAt(row, Berries.ID.ordinal());
							String title = (String) tableModel.getValueAt(row, Berries.NAME.ordinal());
							new PriceGui(tableModel.getPriceTable(id), title);
						}
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
	
	private class AddBerry extends JFrame{
		private static final long serialVersionUID = 5580159001584032476L;
		private final EnumMap<Berries, JTextField> fields = new EnumMap<>(Berries.class);
		
		private AddBerry(TableBackend tableModel){
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			this.setLayout(new GridLayout(0,2));
			this.setTitle("Nytt bär");
			this.setLocationRelativeTo(null);
			Berries[] values = Berries.values();
			for(int i=1;i<values.length;i++){ // skip ID, auto generated
				this.add(new JLabel(values[i].getName()));
				JTextField field = new JTextField();
				fields.put(values[i], field);
				this.add(field);
			}
			JButton save = new JButton("Spara");
			JButton abort = new JButton("Avbryt");
			abort.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					EventQueue.invokeLater(new Runnable() {
						
						@Override
						public void run() {
							dispose();
						}
					});
				}
			});
			this.add(save);
			this.add(abort);
			this.pack();
			this.setVisible(true);
		}
	}

	private class PriceGui extends JFrame{
		private static final long serialVersionUID = -1224970052450584412L;

		private PriceGui(TableBackend tableModel, String title){
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			this.setTitle(title);
			this.setLayout(new BorderLayout());
			this.setLocationRelativeTo(Gui.this);
			
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
