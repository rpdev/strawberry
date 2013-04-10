package froapp3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

class FroApp {
	
	private FroApp(){
		Database d = Database.getInstance();
		d.addBerry("test", 0, 0, 0, 0);
		d.addBerry("test2", 0, 0, 0, 0);
		
		d.updateBerry(1, null, null, 2, 1, 3);
		d.updateBerry(2, null, null, 2, 1, 5);
		
		d.addPrice(100, 1);
		d.addPrice(100, 1);
		d.addPrice(100, 1);
		d.addPrice(555, 1);
		d.addPrice(11, 1);
		d.updatePrice(1, 333, null);
		
		d.addPrice(101, 2);
		d.addPrice(102, 2);
		d.addPrice(100, 2);
		d.addPrice(10, 2);
		
		for(Object[] row : generateBerriesTableData()){
			for(Object cell : row)
				System.out.print(cell + " ");
			System.out.println();
		}
			
	}
	
	private ArrayList<Object[]> generateBerriesTableData(){
		ArrayList<LinkedHashMap<String, Object>> all = Database.getInstance().getAllBerries();
		for(String key : all.get(0).keySet()) // error if list empty
			System.out.print(key + " ");
		System.out.println();
		ArrayList<Object[]> data = new ArrayList<Object[]>(); // data to be used in TableModel
		for(LinkedHashMap<String, Object> map : all){ // loop through every value in array
			Object[] row = new Object[9]; // enummap would be better, trying to keep this part sort of easy
			Iterator<Entry<String, Object>> entrySet = map.entrySet().iterator(); // need iterator for looping
			int i;
			for(i=0; entrySet.hasNext() ;i++) // possible IndexOutOfBoundExecption
				row[i] = entrySet.next().getValue();
			row[i++] = Database.getInstance().getMinPrice((int) row[0]); // min value
			row[i++] = Database.getInstance().getMaxPrice((int) row[0]); // max value
			row[i] = null; // avg
			data.add(row);
		}
		return data;
	}

	public static void main(String[] args) {
		new FroApp();
	}

}
