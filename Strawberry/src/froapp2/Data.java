package froapp2;

public class Data {
	private static int idCounter = 0;
	private final int id;
	private String name;
	private int antal;
	private int salda;
	private int ejSalda;
	private int pris;
	
	Data(String name, int antal, int pris){
		this.id = idCounter++;
		this.name = name;
		this.antal = antal;
		this.pris = pris;
		ejSalda = antal;
		salda = 0;
	}
	
	int getId(){
		return id;
	}
	
	String getName() {
		return name;
	}

	void setName(String name) {
		this.name = name;
	}

	int getAntal() {
		return antal;
	}

	void setAntal(int antal) {
		this.antal = antal;
	}

	int getSalda() {
		return salda;
	}

	void setSalda(int salda) {
		this.salda = salda;
		ejSalda = antal - salda;
	}

	int getEjSalda() {
		return ejSalda;
	}

	void setEjSalda(int ejSalda) {
		this.ejSalda = ejSalda;
		salda = antal - ejSalda;
	}

	int getPris() {
		return pris;
	}

	void setPris(int pris) {
		this.pris = pris;
	}
	
	@Override
	public String toString(){
		return "Namn:" + name + " antal:" + antal + " sålda:" + salda + " ejsålda:" + ejSalda + " pris:" + pris;
	}
}
