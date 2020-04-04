package grafovi;

import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import grafovi.Graf.ElemC;
public class Datoteka {

	private File f;
	private Graf g;
	private Scena s;
	
	public Datoteka(Scena ss) {
		g=new Graf(false);
		s= ss;
	}

	public void setF(File f) {
		this.f = f;
	}
	public Graf importuj(File f) {
		if(f.getName().endsWith(".csv")) {
		try {

			Scanner sc = new Scanner(f);
			String pattern = "([^;|$]*)";
			Pattern r = Pattern.compile(pattern);
			while (sc.hasNextLine()){
			    Matcher m = r.matcher(sc.nextLine());
			    Cvor poz = s.racunajPoziciju();

			    m.find();
			    poz.setIme(m.group(1));
			    
				Cvor prvi=null;
			    if (g.dodajCvor(new Cvor(poz.getX(),poz.getY(),m.group(1)))) {
					prvi = poz;
					prvi=g.getPoslCvor(); //Ovo je bitna izmena
			    }
			    else
			    	for (Graf.ElemC i=g.getPrviC(); i!=null; i=i.sled)
						if(i.c.getIme().equals(poz.getIme())) {
							prvi=i.c;
							break;
						}

			    while (m.find( )) {
			       if(m.group(1).equals("")) continue;
			       //if (m.group(1).equals(prvi.getIme())) continue;
			       poz= s.racunajPoziciju();
			       Cvor novi = new Cvor(poz.getX(),poz.getY(),m.group(1));
			       if(!g.dodajCvor(novi)) 
			    	   for(Graf.ElemC pom= g.getPrviC(); pom!=null ; pom=pom.sled) 
			    		   if(pom.c.getIme().equals(novi.getIme())) {
			    			   novi=pom.c;
			    		       break;
			    		   }
			       
			       if(g.daLiJeUsmeren())
			    	   g.postaviGranuUsmeren(new Grana(prvi,novi));
			       else
			    	   g.postaviGranuNeusmeren(new Grana(prvi,novi));
	     	    }    
			}
			
			return g;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		if(f.getName().endsWith(".gml")) {
			 
			 
			 
			 
			 
			 
		}
	    return null;
	}
	public void exportGML() {
		try {
		File f=new File("GML_Format.gml");
		BufferedWriter writer;
		writer = new BufferedWriter(new FileWriter(f));
		
		StringBuffer sb=new StringBuffer("graph\n[\n");
	
		for (Graf.ElemC tek=g.getPrviC();tek!=null;tek=tek.sled) 
			sb.append("\tnode\n\t[\t id " + tek.c.getIme() +"\n\t label \"" + tek.c.getIme() + "\"\n\t]\n");
		
		int i=0;
		for (Graf.ElemG tek=g.getPrvaG();tek!=null;tek=tek.sled,i++)
					sb.append((i == 0 ? "\t edge" : "\tedge") + "\n\t[\n\t source " + tek.g.getIme1() +"\n\t target " + tek.g.getIme2() + "\n\t]\n");

		sb.append("]");
		
			writer.write(sb.toString());
			writer.close();
		} catch (IOException e) {
			System.out.println("Greska");
			e.printStackTrace();
		}
		
	}
	public void exportCSV() {
		
		try {
			File f=new File("CSV_Format.csv");
			BufferedWriter writer = new BufferedWriter(new FileWriter(f));
			StringBuffer sb=new StringBuffer();
			
			for (Graf.ElemC tek=g.getPrviC();tek!=null;tek=tek.sled) {
				sb.append(tek.c.getIme()); if(tek.c.brojGrana()==0) continue;
				for(int i=0;i<tek.c.brojGrana();i++) {
				    sb.append(";"+tek.c.uzmiKomsiju().getIme());
				}
				sb.append("\n");
			}
			
			writer.write(sb.toString());
			writer.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
