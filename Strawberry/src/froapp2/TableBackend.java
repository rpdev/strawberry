package froapp2;

import java.util.ArrayList;
import java.util.EnumMap;

import javax.swing.table.DefaultTableModel;

class TableBackend extends DefaultTableModel {
	private static final long serialVersionUID = -758366155994706040L;
	private final ArrayList<EnumMap<Labels, String>> data;
	private final FroApp froApp;

	TableBackend(FroApp froApp, ArrayList<EnumMap<Labels, String>> data) {
		this.froApp = froApp;
		this.data = data;
	}

	EnumMap<Labels, String> getEnumData(int rowIndex){
		return data.get(rowIndex).clone();
	}
	
	void setData(ArrayList<EnumMap<Labels, String>> data){
		this.data.clear();
		this.data.addAll(data);
		fireTableDataChanged();
	}	
	
	void saveUpdateData(EnumMap<Labels, String> data) {
		if(data.containsKey(Labels.ID)){
			System.out.println("ID");
		} else {			
			System.out.println("NO ID");
			froApp.saveData(data);
		}
		// TODO Auto-generated method stub
	}
	
	void deleteItem(int row) {
		froApp.deleteItem(Integer.parseInt(data.get(row).get(Labels.ID)));
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return Labels.values()[columnIndex].cl;
	}

	@Override
	public int getColumnCount() {
		return Labels.values().length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return Labels.getLabels()[columnIndex];
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
		return data.get(rowIndex).get(Labels.values()[columnIndex]);
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub

	}
}
