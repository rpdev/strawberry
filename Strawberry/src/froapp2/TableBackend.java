package froapp2;

import java.util.ArrayList;
import java.util.EnumMap;

import javax.swing.table.DefaultTableModel;

class TableBackend extends DefaultTableModel {
	private static final long serialVersionUID = -758366155994706040L;
	private final ArrayList<EnumMap<Labels, String>> data;

	TableBackend(ArrayList<EnumMap<Labels, String>> data) {
		// TODO Auto-generated constructor stub
		this.data = data;
		System.out.println(data.size());
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
