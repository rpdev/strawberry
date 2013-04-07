package froapp2;

enum Labels{
	ID("ID", Integer.class),
	NAME("Name", String.class),
	TOTAL("Antal", Integer.class),
	SOLD("Sålda", Integer.class),
	NON_SOLD("Ej Sålda", Integer.class),
	PRICE("Pris", Integer.class),
	LOWEST("Lägsta", Integer.class),
	HIGEST("Högsta", Integer.class),
	AVG("Medel", Integer.class);
	
	final String label;
	final Class<?> cl;
	
	private Labels(String label, Class<?> cl){
		this.label = label;
		this.cl = cl;
	}
		
	static String[] getLabels(){
		Labels[] labels = values();
		String[] stringLabels = new String[labels.length];
		for(int i=0;i<labels.length;i++)
			stringLabels[i] = labels[i].label;
		return stringLabels;
	}
}
