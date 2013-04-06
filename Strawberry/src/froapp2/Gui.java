package froapp2;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

class Gui extends JFrame {
	private static final long serialVersionUID = -6029743826442546600L;

	Gui(){
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
		
		this.pack();
		this.setVisible(true);
	}
	
	
	private class DisplayItem extends JFrame{
		private static final long serialVersionUID = -8466864965303634622L;
		private final String[] labels = {"ID","Namn","Antal","Sålda","Ej Sålda","Pris","Lägsta","Högsta","Medel"};
		private final JTextField[] fields = new JTextField[labels.length];
		{
//			Arrays.fill(fields, new JTextField());
			for(int i=0;i<fields.length;i++)
				fields[i] = new JTextField();
			fields[0].setEditable(false); // ID
		}
		
		private DisplayItem(){
			this.setLayout(new GridLayout(0, 2));
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			
			for(int i=0;i<labels.length;i++){
				JLabel l = new JLabel(labels[i]);
				this.add(l);
				this.add(fields[i]);
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
