package it.polito.tdp.food.model;

import java.util.*;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.Condiment;
import it.polito.tdp.food.db.Food;
import it.polito.tdp.food.db.FoodDao;


public class Model {
	private SimpleWeightedGraph<Condiment, DefaultWeightedEdge> grafo;
    private Map <Integer, Condiment> idMapC ;
    private List <Condiment> listaC;
    private Map <Integer, Food> idMapF ;
    private List <Food> listaF;
    FoodDao dao;
    double MAX;
    

public Model () {
	dao = new FoodDao();
}
public void creaGrafo (Double calorie) {
	// creo il grafo
	this.grafo =new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
	idMapC = new HashMap <Integer, Condiment> ();
	listaC = new LinkedList <Condiment>();
	idMapF = new HashMap <Integer, Food> ();
	listaF = new LinkedList <Food>();
	// aggiungo i vertici
	idMapC = dao.listAllCondiment(calorie, idMapC);
	listaC.addAll(idMapC.values());
	Graphs.addAllVertices(grafo, idMapC.values());
	System.out.println("vertici : "+ grafo.vertexSet().size());
	for (Condiment c1 : listaC) {
		for (Condiment c2 : listaC) {
			int cont = dao.getFC (c1.getFood_code(),c2.getFood_code(), idMapC);
			if (!c1.equals(c2) && cont>0 && !grafo.containsEdge(c2, c1)) {
				Graphs.addEdge(grafo, c1, c2, cont);
			}
		}
	}
	System.out.println(grafo.edgeSet().size());


}

public String getOutPut () {
	List <Condiment> lC = new LinkedList <Condiment>();
	lC.addAll(listaC);
	Collections.sort(lC);
	String s = "Condimenti :\n";
	 
	for (Condiment c : lC) {
		/*System.out.println(c.getFood_code());
		int cont =Graphs.neighborListOf(this.grafo, c).size();*/
		int cont = 0;
		List <Integer> lI = new LinkedList<>();
		for (Condiment c1 :Graphs.neighborListOf(this.grafo, c))
		{  
			if (!lI.contains(c1.getFood_code())){
				lI.add(c1.getFood_code());
				cont++;
			}
		}
		s+= c.getCondiment_id()+"___calorie : "+c.getCondiment_calories()+"--"+ "numero di cibi che lo contengono : "+cont+ "\n";
		
		
	}
	return s;
}



public List<Condiment> trovaSequenza(){
	List <Condiment> parziale = new LinkedList <Condiment>();
	List <Condiment> finale = new LinkedList <Condiment>();
	List <Condiment> lista = new LinkedList <Condiment>(grafo.vertexSet());
	MAX=0.0;
	
	cerca (parziale, finale , lista, 0 ,1);
	return finale;
	
}
private void cerca(List<Condiment> parziale, List<Condiment> finale, List<Condiment> lista,double somma,  int livello) {
	    
		if (somma > MAX )
		/*if(tot > MAX)*/ {
			MAX = somma;
			finale.clear();
			finale.addAll(parziale);
		   System.out.println(finale+"\n+++++++++++\n "+MAX+"\n");}
	
	for(Condiment c :lista){

		if (!parziale.contains(c) && isCorretto(parziale,c)) {
			parziale.add(c);
			somma+= c.getCondiment_calories();
			//System.out.println(parziale+"\n");
     		cerca (parziale, finale, lista, somma,  livello++);
			parziale.remove(c);
		}
	}
	
}

private Double totCalorie(List<Condiment> parziale) {
	double tot = 0;
	for (Condiment c : parziale) {
		tot+=c.getCondiment_calories();
	}
	
	//System.out.println(tot+"\n");
	return tot;
}









private boolean isCorretto(List<Condiment> parziale, Condiment condiment) {
	
	if (parziale.size()==0) {
		return true;
	}else if (parziale.size()==1) {
		List <Condiment> vicini = new LinkedList <Condiment>(Graphs.neighborListOf(grafo, condiment));
		if (!vicini.contains(parziale.get(parziale.size()-1))){
			return true;
		}else
			return false;
		
	}else if (parziale.size()>1){
		List <Condiment> vicini = new LinkedList <Condiment>(Graphs.neighborListOf(grafo, condiment));
		if (!vicini.contains(parziale.get(parziale.size()-1)) && vicini.contains(parziale.get(parziale.size()-2) )){
			//System.out.println("corretto");
			return true;
		}else {
			//System.out.println("Non corretto");
			return false;}

	}else return false;
	
}













}