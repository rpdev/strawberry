
package froapp;



public class Data {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAntal() {
        return antal;
    }

    public void setAntal(int antal) {
        this.antal = antal;
    }

    public int getSalda() {
        return salda;
    }

    public int getEjsalda() {
        return ejsalda;
    }

    public void setEjsalda(int ejsalda) {
        this.ejsalda = ejsalda;
    }

    public int getPris() {
        return pris;
    }

    public void setPris(int pris) {
        this.pris = pris;
    }

    public void setSalda(int salda) {
        this.salda = salda;
    }
    
    public String getAllinfo(){
    return (getName() + getAntal() + getSalda() + getEjsalda() + getPris());
    
    };
    
    public String name;
    public int antal;
    public int salda;
    public int ejsalda;
    public int pris;
    
    
    
}
