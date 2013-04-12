package froapp3;

import java.util.ArrayList;
import java.util.Map.Entry;

import javax.swing.table.DefaultTableModel;

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
	
	TableBackend getPriceTable(int id){
		return froapp.getPricesTable(id);
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
		return columnIndex > 0;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		
	}


}
