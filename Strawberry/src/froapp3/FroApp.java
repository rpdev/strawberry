package froapp3;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map.Entry;

import froapp3.Database.Berries;
import froapp3.Database.DatabaseKeys;
import froapp3.Database.Prices;

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
		
		new Gui(new TableBackend(this, Berries.class, createNameTypeList(Berries.class), generateBerriesTableData()));
	}
	
	TableBackend getPricesTable(int id){
		return new TableBackend(this, Prices.class, createNameTypeList(Prices.class), generatePricesTableData(id));
	}
	
	private ArrayList<Entry<String, Class<?>>> createNameTypeList(Class<? extends Enum<? extends DatabaseKeys>> t){
		ArrayList<Entry<String, Class<?>>> types = new ArrayList<>();
		for(Enum<? extends DatabaseKeys> e : t.getEnumConstants()){
			DatabaseKeys l = (DatabaseKeys) e;
			Entry<String, Class<?>> entry = new AbstractMap.SimpleEntry<String, Class<?>>(l.getName(), l.getJavaClass());
			types.add(entry);
		}
		if(t == Berries.class){
			types.add(new AbstractMap.SimpleEntry<String, Class<?>>("Lägsta", Integer.class));
			types.add(new AbstractMap.SimpleEntry<String, Class<?>>("Högsta", Integer.class));
			types.add(new AbstractMap.SimpleEntry<String, Class<?>>("Medel", Integer.class));
		}
		return types;
	}
	
	private ArrayList<Object[]> generateBerriesTableData(){
		ArrayList<EnumMap<Berries, Object>> allRows = Database.getInstance().getAllBerries();
		ArrayList<Object[]> data = new ArrayList<Object[]>(); // data to be used in TableModel
		for(EnumMap<Berries, Object> map : allRows){ // loop through every value in array
			Object[] row = new Object[9];
			Berries[] berries = Berries.values();
			int i;
			for(i=0; i<berries.length ;i++)
				row[i] = map.get(berries[i]);
			row[i++] = Database.getInstance().getMinPrice((int) row[0]); // min value
			row[i++] = Database.getInstance().getMaxPrice((int) row[0]); // max value
			row[i] = null; // avg
			data.add(row);
		}
		return data;
	}
	
	private ArrayList<Object[]> generatePricesTableData(int id){
		ArrayList<EnumMap<Prices, Object>> allRows = Database.getInstance().getAllPrices(id);
		ArrayList<Object[]> data = new ArrayList<Object[]>(); // data to be used in TableModel
		Prices[] prices = Prices.values();
		for(EnumMap<Prices, Object> map : allRows){ // loop through every value in array
			Object[] row = new Object[3];
			int i;
			for(i=0; i< prices.length ;i++)
				row[i] = map.get(prices[i]);
			data.add(row);
		}
		return data;
	}

	public static void main(String[] args) {
		new FroApp();
	}

}
