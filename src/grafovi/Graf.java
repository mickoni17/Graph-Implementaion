package grafovi;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Graf implements Cloneable {
	protected class ElemC implements Cloneable{
		Cvor c;
		ElemC sled;
		public ElemC(Cvor cc) {
			c=cc; 
		}
	}
	protected class ElemG implements Cloneable{
		Grana g;
		ElemG sled;
		public ElemG(Grana gg) {
			g=gg; 
		}
		
	}
	private ElemC prviC, poslC;
	private ElemG prvaG, poslG;
	private boolean usmeren;
	private int brCvorova, brGrana;
	
	public Graf(boolean us) {
		usmeren=us;
	}
    @Override
	public Graf clone() {
    	Graf noviGraf;
		try {
			noviGraf = (Graf)super.clone();
			noviGraf.prviC = noviGraf.poslC = null;
			noviGraf.prvaG = noviGraf.poslG = null;
			for (ElemC tek = prviC ; tek!=null ; tek=tek.sled)
				if (noviGraf.prviC == null) 
					noviGraf.prviC = noviGraf.poslC = new ElemC(tek.c);
				else 
					noviGraf.poslC.sled = noviGraf.poslC = new ElemC(tek.c);
			
			for (ElemG tek = prvaG ; tek!=null ; tek=tek.sled)
				if (noviGraf.prvaG == null) 
					noviGraf.prvaG = noviGraf.poslG = new ElemG(tek.g);
				else 
					noviGraf.poslG.sled = noviGraf.poslG = new ElemG(tek.g);
				
			
			return noviGraf;
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return null;
	}
	public boolean daLiJeUsmeren() {
		return usmeren;
	}
	
	public boolean dodajCvor(Cvor c) {
		for (ElemC i=prviC; i!=null; i=i.sled)
			if(i.c.getIme().equals(c.getIme()))
				return false;
	
		if(prviC==null) {
			prviC=poslC=new ElemC(c);
		}
		else {
			poslC.sled=poslC=new ElemC(c);
		}
		brCvorova++;
		return true;
	}
	public boolean postaviGranuUsmeren(Grana g) {
		for (ElemG i=prvaG; i!=null; i=i.sled)
			if(i.g.getIme1().equals(g.getIme1()) && i.g.getIme2().equals(g.getIme2()))
				return false;
		
		if(prvaG==null) {
			prvaG=poslG=new ElemG(g);
		}
		else {
			poslG.sled=poslG=new ElemG(g);
		}
		brGrana++;
		
		//Izmeniti i napraviti 2 skupa za usmerene i neumserene
		for(ElemC tek=prviC;tek!=null;tek=tek.sled) 
			if(!tek.c.daLiIma(g) && (tek.c.getIme().equals(g.getIme1()) || tek.c.getIme().equals(g.getIme2())))
				tek.c.dodajGranu(g);
		
		return true;

    }
	public boolean postaviGranuNeusmeren(Grana g) {
		if (g.getIme1()==null || g.getIme2()==null)
			return false;
		//for (ElemG i=prvaG; i!=null; i=i.sled)
			//if((i.g.getIme1().equals(g.getIme1()) && i.g.getIme2().equals(g.getIme2())) || (i.g.getIme1().equals(g.getIme2()) && i.g.getIme2().equals(g.getIme1())) )
				//return false;
		//OVAKO RADI 
		if(prvaG==null) 
			prvaG=poslG=new ElemG(g);
		else 
			poslG.sled=poslG=new ElemG(g);
		brGrana++;

		for(ElemC tek=prviC;tek!=null;tek=tek.sled) 
			if(!tek.c.daLiIma(g) && (tek.c.getIme().equals(g.getIme1()) || tek.c.getIme().equals(g.getIme2())))
				tek.c.dodajGranu(g);
		
		return true;
	}
	public void crtaj(Graphics g,Scena s) {
		ElemG tek2 = prvaG;
		Graphics2D g2= (Graphics2D) g;
		if(prvaG!=null) {
			while (tek2!=null) {
				g2.setColor(tek2.g.getBoja());
				g2.setStroke(new BasicStroke(1+tek2.g.getDebljina()));
				g2.drawLine(tek2.g.getC1().getX()+tek2.g.getC1().getSvojR()/2, tek2.g.getC1().getY()+tek2.g.getC1().getSvojR()/2, tek2.g.getC2().getX()+tek2.g.getC2().getSvojR()/2, tek2.g.getC2().getY()+tek2.g.getC2().getSvojR()/2);
				tek2=tek2.sled;
			}
			tek2=prvaG;
			//Iscrtavanje ljubicastih preko crnih
			while (tek2!=null) {
				if(tek2.g.getBoja()==Color.MAGENTA) {
				g2.setColor(tek2.g.getBoja());
				g2.setStroke(new BasicStroke(1+tek2.g.getDebljina()));
				g2.drawLine(tek2.g.getC1().getX()+tek2.g.getC1().getSvojR()/2, tek2.g.getC1().getY()+tek2.g.getC1().getSvojR()/2, tek2.g.getC2().getX()+tek2.g.getC2().getSvojR()/2, tek2.g.getC2().getY()+tek2.g.getC2().getSvojR()/2);
				}
				tek2=tek2.sled;
			}
		}
		ElemC tek=prviC;
		if(tek!=null)
			while(tek!=null) {
				g.setColor(Color.BLACK);
				g.fillOval(tek.c.getX()-2-tek.c.okolniKrug()/2,tek.c.getY()-2-tek.c.okolniKrug()/2, tek.c.getSvojR()+4+tek.c.okolniKrug(), tek.c.getSvojR()+4+tek.c.okolniKrug());
				g.setColor(tek.c.getBoja());
				g.fillOval(tek.c.getX(),tek.c.getY(), tek.c.getSvojR(), tek.c.getSvojR());
				//g.setColor(Color.BLACK);
				//g.setFont(Cvor.getFont());
				//g.drawString(tek.c.getIme(), tek.c.getX()+Cvor.getR()/2-Cvor.getFontSize()/10-(tek.c.getIme().length()-1)*5, tek.c.getY()+Cvor.getR()/2+Cvor.getFontSize()/3);	
				tek = tek.sled;
			}

	}
	public boolean brisiCvor(String ime) {
		ElemC tek=prviC, dlt;
		
		if(prviC!=null) {
			if (prviC.c.getIme().equals(ime)) {
				dlt=prviC;
			    prviC=prviC.sled;
			
			    while (prvaG!=null && (prvaG.g.getC1().getIme().equals(ime) || prvaG.g.getC2().getIme().equals(ime))) {
			    	ElemG dlt2 = prvaG;
			    	prvaG=prvaG.sled;
			    	brGrana--;
			    	dlt2=null;
			    }
			    ElemG pom = prvaG;
			    while (pom!=null) 
			    	if (pom.g.getC1().getIme().equals(ime) || pom.g.getC2().getIme().equals(ime)) {
			    		ElemG dlt2 = pom;
			    		pom= pom.sled;
			    		brGrana--;
			    		dlt2=null;
			    	}
			    	else
			    		pom=pom.sled;
			    
			    dlt=null;
			    brCvorova--;
			    return true;
			}
			else
				while (tek.sled!=null) {
					if(tek.sled.c.getIme().equals(ime)) {
					dlt=tek.sled;
					tek.sled=tek.sled.sled;
					
					while (prvaG!=null && (prvaG.g.getC1().getIme().equals(ime) || prvaG.g.getC2().getIme().equals(ime))) {
				    	ElemG dlt2 = prvaG;
				    	prvaG=prvaG.sled;
				    	brGrana--;
				    	dlt2=null;
				    }
				    ElemG pom = prvaG;
				    while (pom!=null) 
				    	if (pom.g.getC1().getIme().equals(ime) || pom.g.getC2().getIme().equals(ime)) {
				    		ElemG dlt2 = pom;
				    		pom= pom.sled;
				    		brGrana--;
				    		dlt2=null;
				    	}
				    	else
				    		pom=pom.sled;
					
					dlt=null;
					brCvorova--;
					return true;
				    }
				tek=tek.sled;
				}
		}
		return false;
		
	}
	public boolean brisiGranu(Grana g) {
	    String ime= g.getIme1(), ime2=g.getIme2(); 	
	
		for(ElemC tek=prviC;tek!=null;tek=tek.sled) 
			if(tek.c.daLiIma(g))
				tek.c.brisiGranu(g);
		
		ElemG pom=prvaG;
		if (pom!=null) {
			if(!usmeren) {
				if (prvaG.g.getC1().getIme().equals(ime) || prvaG.g.getC2().getIme().equals(ime)) {
					ElemG dlt=prvaG;
					prvaG=prvaG.sled;
					if(prvaG==null) poslG=null;
					dlt=null;
					brGrana--;
					return true;
				}
				else 
				while (pom.sled!=null) {
				if (pom.sled.g.getC1().getIme().equals(ime) || pom.sled.g.getC2().getIme().equals(ime)) {
					ElemG dlt=pom.sled;
					pom.sled=pom.sled.sled;
					if(pom.sled.sled==null) poslG=pom;
					dlt=null;
					brGrana--;
					return true;
				}
				pom=pom.sled;
				}
				
			}
			else {
				if (prvaG.g.getC1().getIme().equals(ime) && prvaG.g.getC2().getIme().equals(ime2)) {
					ElemG dlt=prvaG;
					prvaG=prvaG.sled;
					if(prvaG==null) poslG=null;
					dlt=null;
					brGrana--;
					return true;
				}
				else 
				while (pom!=null) {
				if (pom.g.getC1().getIme().equals(ime) && pom.g.getC2().getIme().equals(ime2)) {
					ElemG dlt=pom.sled;
					pom.sled=pom.sled.sled;
					if(pom.sled.sled==null) poslG=pom;
					dlt=null;
					brGrana--;
					return true;
				}
				pom=pom.sled;
				}
			}
		}
		
		return false;
	}
	public boolean postojiGranaNeusmerena(String c1,String c2) {
		for(ElemG tek= prvaG; tek!=null ; tek=tek.sled)
			if((tek.g.getIme1().equals(c1) && tek.g.getIme2().equals(c2))||(tek.g.getIme1().equals(c2) && tek.g.getIme2().equals(c1)))
				return true;
		return false;
	}
	
	public void obojiPut(int dst,Cvor []prethodnici,String c1) {
		int it=dst;
		int n=brCvorova;
		while(!prethodnici[it].getIme().equals(c1)) {
		    for(ElemC pom=prviC;pom!=null;pom=pom.sled)
		    	if(pom.c.getIme().equals(prethodnici[it].getIme())) {
		    		pom.c.setSvojaBoja(Color.MAGENTA);
		    		System.out.println("Tuc");
		    		System.out.println(pom.c.getIme());
		    		for (Graf.ElemG tek=getPrvaG();tek!=null;tek=tek.sled)
		    			if((tek.g.getIme1().equals(prethodnici[it].getIme()) && tek.g.getIme2().equals(pom.c.getIme())) || (tek.g.getIme2().equals(prethodnici[it].getIme()) && tek.g.getIme1().equals(pom.c.getIme()))) {
		    				tek.g.setBoja(Color.MAGENTA);
		    				tek.g.setDebljina(3);
		    				System.out.println("Muc");
		    			}
		    		int it2=0;
		    		for(ElemC pom2=prviC;pom2!=null;pom2=pom2.sled,it2++)
		    			if(pom2.c.getIme().equals(pom.c.getIme())) 
		    				break;
		    			
		    		prethodnici[it]=prethodnici[it2];
		    		break;
		    	}
			
			
		}	
	}
	
	public int getBrCvorova() {
		return brCvorova;
	}
	public int getBrGrana() {
		return brGrana;
	}
	protected ElemC getPrviC() {
		return prviC;
	}
	protected ElemG getPrvaG() {
		return prvaG;
	}
	protected Cvor getPoslCvor() {
	    return poslC.c;	
	}
}
