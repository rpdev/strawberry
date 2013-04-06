package froapp;

import java.util.ArrayList;

public class Arr {

    ArrayList<Data> arr = new ArrayList<Data>();
    Data data = new Data();

    public void addToList(Data nummer) {
        arr.add(nummer);
    }

    // Retunera ett tal
    public int antal() {
        return arr.size();
    }

    ;
    
    // Retunera objektet Data
    public Data getFromList(int i) {
        return arr.get(i);
    }

    // Ta bort en index i arrayen
    public void delFromList(int i) {
        arr.remove(i);
    }

    public String getInfo() {
        return ("Hej" + data.getName());
    }

    ;
    
    public void VisaList() {

        for (int i = 0; i < antal(); i++) {

            Data ut = getFromList(i);
            System.out.println(ut.getAllinfo());
        };



    }

    public String VisaAllInfo() {
        String visa = "";
        for (int i = 0; i < antal(); i++) {
            Data g = getFromList(i);
            visa += g.getAllinfo();
        }
        return visa;
    }
;


}