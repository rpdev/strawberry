
package froapp;

import java.util.ArrayList;

public class FroApp {

  
    public static void main(String[] args) {
    
  
         
         
         
         Data skicka = new Data();
         Data skicka2 = new Data();
         Arr list = new Arr();
         
         // 0
         skicka.setName("Blåbär");
         skicka.setAntal(10);
         skicka.setSalda(2);
         skicka.setEjsalda(3);
         skicka.setPris(23);
         list.arr.add(skicka);
         
         // 1
         skicka2.setName("Björnbär");
         skicka2.setAntal(8);
         skicka2.setSalda(1);
         skicka2.setEjsalda(3);
         skicka.setPris(54);
         list.arr.add(skicka2);
         
         
         
         // list.delFromList(1);
         
         
         list.VisaAllInfo();
         
         /*
         int i;
         for(i=0;i<list.antal();i++){
         Data ut = list.getFromList(i);
         
         System.out.println(
                 "ID: "+ i + 
                 "\nNamn: " + ut.getName()+ 
                 "\nAntal påsar: " + ut.antal + 
                 "\nSålda: " + ut.salda + 
                 "\nEj Sålda: " + ut.ejsalda +
         "\nPris: " + ut.pris + "\n______________"); 
         * */
             
         
       
         }
         
    }
    
    
    