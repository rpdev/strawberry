
package froapp2;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map.Entry;


public class FroApp {
	
	private FroApp() {
		
		
		Database.getInstance().addItem("Blåbär", 10, 2, 10-2, 23);
		Database.getInstance().addItem("Björnbär", 8, 1, 8-1, 54);
		ArrayList<EnumMap<Labels, String>> d = Database.getInstance().getAllContent();
		for(EnumMap<Labels, String> v : d){
			for(Entry<Labels, String> e : v.entrySet()){
				System.out.print(e + " ");
			}
			System.out.println();
		}
		TableBackend b = new TableBackend(d);
		new Gui(b);
	}
	
    public static void main(String[] args) {
         new FroApp();
    }
}
    
    
    