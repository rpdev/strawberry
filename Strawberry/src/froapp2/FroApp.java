
package froapp2;

import java.util.ArrayList;

public class FroApp {
	private final ArrayList<Data> berrys = new ArrayList<>();
	
	private FroApp() {
		
		new Gui(new TableBackend());
		
		Data blue = new Data("Blåbär", 10, 23);
		blue.setSalda(2);
		berrys.add(blue);
		
		Data bear = new Data("Björnbär", 8, 54);
		bear.setSalda(1);
		berrys.add(bear);
		
		printListContent();
	}
  
	private void printListContent(){
//		for(Data d : berrys)
//			System.out.println(d);
		
		for(int i=0; i< berrys.size(); i++){
			Data d = berrys.get(i);
			System.out.println(d.toString());
		}
	}
	
    public static void main(String[] args) {
         new FroApp();
    }
}
    
    
    