package it.polito.tdp.food.model;

public class TestModel {
	public static void main(String[] args) {
             Model m = new Model();
             m.creaGrafo((double)15);
             System.out.println(m.getOutPut());
             System.out.println( m.trovaSequenza().toString());
    }
	
}
