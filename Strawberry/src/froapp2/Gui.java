package froapp2;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

class Gui extends JFrame {
	private static final long serialVersionUID = -6029743826442546600L;

	Gui(TableBackend tableModel){
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		
		JButton add = new JButton("Lägg Till");
		add.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DisplayItem item = new DisplayItem();
			}
		});
		
		JButton show = new JButton("Visa");
		JButton edit = new JButton("Ändra");
		JButton remove = new JButton("Ta bort");
		JPanel buttons = new JPanel(new FlowLayout());
		buttons.add(add);
		buttons.add(show);
		buttons.add(edit);
		buttons.add(remove);
		this.add(buttons, BorderLayout.NORTH);
		
		JTable table = new JTable(tableModel);
		this.add(new JScrollPane(table), BorderLayout.CENTER);
		
		this.pack();
		this.setVisible(true);
	}
	
	
	private static class DisplayItem extends JFrame{
		private static final long serialVersionUID = -8466864965303634622L;
		private final EnumMap<Labels, JTextField> fields = new EnumMap<>(Labels.class);
		{
			for(Labels key : Labels.values())
				fields.put(key, new JTextField());
			
			fields.get(Labels.ID).setEditable(false);
		}
		
		private DisplayItem(){
			this.setLayout(new GridLayout(0, 2));
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			
			for(Entry<Labels, JTextField> e : fields.entrySet()){
				JLabel label = new JLabel(e.getKey().label);
				this.add(label);
				this.add(e.getValue());
			}
			
			JButton save = new JButton("Spara");
			JButton abort = new JButton("Avbryt");
			abort.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					dispose();
				}
			});
			this.add(save);
			this.add(abort);
			
			this.pack();
			this.setLocationRelativeTo(null);
			this.setVisible(true);
		}
		
		private DisplayItem(Data data){
			this();
		}
	}
}
