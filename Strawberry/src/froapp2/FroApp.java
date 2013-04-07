
package froapp2;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map.Entry;


public class FroApp {
	private final TableBackend b;
	
	private FroApp() {		
		Database.getInstance().addItem("Blåbär", 10, 2, 10-2, 23);
		Database.getInstance().addItem("Björnbär", 8, 1, 8-1, 54);
		b = new TableBackend(this, Database.getInstance().getAllContent());
		new Gui(b);
		printDatabase();
	}
	
	private void printDatabase(){
		ArrayList<EnumMap<Labels, String>> d = Database.getInstance().getAllContent();
		for(EnumMap<Labels, String> v : d){
			for(Entry<Labels, String> e : v.entrySet()){
				System.out.print(e + " ");
			}
			System.out.println();
		}
	}
	
    public static void main(String[] args) {
         new FroApp();
    }

	void saveData(EnumMap<Labels, String> data) {
		Database.getInstance().addItem(
				data.get(Labels.NAME), 
				Integer.parseInt(data.get(Labels.NUMBER)), 
				Integer.parseInt(data.get(Labels.SOLD)), 
				Integer.parseInt(data.get(Labels.NON_SOLD)), 
				Integer.parseInt(data.get(Labels.PRICE)));	
		printDatabase();
		b.setData(Database.getInstance().getAllContent());
	}

	void deleteItem(int id) {
		Database.getInstance().deleteItem(id);
		printDatabase();
		b.setData(Database.getInstance().getAllContent());
	}
}
    
    
    