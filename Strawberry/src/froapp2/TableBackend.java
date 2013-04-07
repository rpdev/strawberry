package froapp2;

import javax.swing.table.DefaultTableModel;

class TableBackend extends DefaultTableModel {
	private static final long serialVersionUID = -758366155994706040L;

	public TableBackend() {
		// TODO Auto-generated constructor stub
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
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
