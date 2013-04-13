package froapp3;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map.Entry;

import javax.swing.table.DefaultTableModel;

import froapp3.Database.Berries;
import froapp3.Database.DatabaseKeys;
import froapp3.Database.Prices;

class TableBackend extends DefaultTableModel {
	private static final long serialVersionUID = -6184127963011865836L;
	private final ArrayList<Object[]> data;
	private final Class<? extends Enum<? extends DatabaseKeys>> type;
	private final ArrayList<Entry<String, Class<?>>> classTypes;
	private final FroApp froapp;

	TableBackend(FroApp froapp, Class<? extends Enum<? extends DatabaseKeys>> type, ArrayList<Entry<String, Class<?>>> classTypes, ArrayList<Object[]> data){
		this.froapp = froapp;
		this.data = data;
		this.type = type;
		this.classTypes = classTypes;
	}
	
	void addBerry(EnumMap<Berries, Object> values) {
		froapp.addBerry(values);
	}
	
	void addPrice(int price, int berryId, TableBackend tableModel) {
		froapp.addPrice(price, berryId, tableModel);
	}

	void deleteBerry(int row) {
		froapp.deleteBerry((int) data.get(row)[0]);
	}

	void deletePrice(int row, TableBackend tableModel) {
		Object[] d = data.get(row);
		// id, price, berryID
		froapp.deletePrice((int) d[2], (int) d[0], tableModel);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return classTypes.get(columnIndex).getValue();
	}

	@Override
	public int getColumnCount() {
		return type == Prices.class ? classTypes.size() - 1 : classTypes.size();
	}

	@Override
	public String getColumnName(int columnIndex) {
		return classTypes.get(columnIndex).getKey();
	}

	TableBackend getPriceTable(int id){
		return froapp.getPricesTable(id);
	}

	@Override
	public int getRowCount() {
		if(data == null)
			return 0;
		else
			return data.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return data.get(rowIndex)[columnIndex];
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if(columnIndex == 0)
			return false;
		else if(type == Berries.class && columnIndex > Berries.PRICE.ordinal())
			return false;
		else
			return true;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if(type == Berries.class){
			froapp.updateBerry((int) data.get(rowIndex)[0], Berries.values()[columnIndex], aValue);
		} else { // Prices
			froapp.updatePrice((int) data.get(rowIndex)[0], (int) aValue, (int) data.get(rowIndex)[2], this);
		}
	}

	void setValues(ArrayList<Object[]> data) {
		/*
		 * Could cause problem, improvement would be to synchronize data
		 * to avoid access while chancing data.
		 */
		this.data.clear();
		this.data.addAll(data);
		fireTableDataChanged();
	}
}
