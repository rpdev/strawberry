package froapp2;

enum Labels{
	ID("ID", Integer.class, "INTEGER PRIMARY KEY"),
	NAME("Name", String.class, "TEXT"),
	NUMBER("Antal", Integer.class, "INTEGER"),
	SOLD("Sålda", Integer.class, "INTEGER"),
	NON_SOLD("Ej Sålda", Integer.class, "INTEGER"),
	PRICE("Pris", Integer.class, "INTEGER"),
	LOWEST("Lägsta", Integer.class, "INTEGER"),
	HIGEST("Högsta", Integer.class, "INTEGER"),
	AVG("Medel", Integer.class, "INTEGER");
	
	final String label, databaseType;
	final Class<?> cl;
	
	private Labels(String label, Class<?> cl, String databaseType){
		this.label = label;
		this.cl = cl;
		this.databaseType = databaseType;
	}
		
	static String[] getLabels(){
		Labels[] labels = values();
		String[] stringLabels = new String[labels.length];
		for(int i=0;i<labels.length;i++)
			stringLabels[i] = labels[i].label;
		return stringLabels;
	}
}
