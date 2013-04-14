package froapp3;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map.Entry;

import froapp3.Database.Berries;
import froapp3.Database.DatabaseKeys;
import froapp3.Database.Prices;

class FroApp {
	private final TableBackend berries;
	
	private FroApp(){
		new Gui(berries = new TableBackend(this, Berries.class, createNameTypeList(Berries.class), generateBerriesTableData()));
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
			row[i] = Database.getInstance().getAvgPrice((int) row[0]); // avg value
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

	void addBerry(EnumMap<Berries, Object> values) {
		if(values.size() == Berries.values().length - 1){
			if(Database.getInstance().addBerry(values))
				berries.setValues(generateBerriesTableData()); // possible improvement, only add new row not all
		}else
			System.err.println("Missing values for adding berry");
	}

	void deleteBerry(int id) {
		if(Database.getInstance().deleteBerryItem(id))
			berries.setValues(generateBerriesTableData()); // possible improvement, only remove one row, skip updating all
	}

	void addPrice(int price, int berryId, TableBackend tableModel) {
		if(Database.getInstance().addPrice(price, berryId)){
			berries.setValues(generateBerriesTableData());
			tableModel.setValues(generatePricesTableData(berryId));
		}
	}

	void deletePrice(int berryId, int priceId, TableBackend tableModel) {
		if(Database.getInstance().deletePriceItem(priceId)){
			berries.setValues(generateBerriesTableData());
			tableModel.setValues(generatePricesTableData(berryId));
		}
	}

	void updateBerry(int id, Berries berry, Object aValue) {
		// possible better with enummap, not a nice solution...
		switch(berry){
		case NAME: 
			if(Database.getInstance().updateBerry(id, (String) aValue, null, null, null, null))
				berries.setValues(generateBerriesTableData());
			break;
		case NUMBER: 
			if(Database.getInstance().updateBerry(id, null, (Integer) aValue, null, null, null))
				berries.setValues(generateBerriesTableData());
			break;
		case SOLD: 
			if(Database.getInstance().updateBerry(id, null, null, (Integer) aValue, null, null))
				berries.setValues(generateBerriesTableData());
			break;
		case NON_SOLD: 
			if(Database.getInstance().updateBerry(id, null, null, null, (Integer) aValue, null))
				berries.setValues(generateBerriesTableData());
			break;
		case PRICE: 
			if(Database.getInstance().updateBerry(id, null, null, null, null, (Integer) aValue))
				berries.setValues(generateBerriesTableData());
			break;
		default: System.err.println("Can't change " + berry);
			break;
		}
	}

	void updatePrice(int id, int price, int berryId, TableBackend tableModel) {
		if(Database.getInstance().updatePrice(id, price)){
			berries.setValues(generateBerriesTableData());
			tableModel.setValues(generatePricesTableData(berryId));
		}
	}

}
