package grafovi;

import java.util.PriorityQueue;
import java.util.Set;
import java.util.SortedMap;
import java.util.List;
import java.util.Map;
import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;

public class Algoritmi {

	Graf g;
	Scena s;
	static final double PI = 3.14;
	
	public Algoritmi(Graf gg,Scena ss) {
	    g=gg;
	    s=ss;

	}

	int minDistance(int dist[], boolean sptSet[])
	{
	   // Initialize min value
	   int min = Integer.MAX_VALUE, min_index=0;
	  
	   for (int v = 0; v < g.getBrCvorova(); v++)
	     if (sptSet[v] == false && dist[v] <= min) {
	         min = dist[v]; min_index = v;
	     }
	   return min_index;
	}
	
	public int ShortestPath(String c1,String c2) {
		int graph[][] = new int[g.getBrCvorova()][g.getBrCvorova()];
		int i=0,j=0;
		
		Cvor cvorovi[]=new Cvor[g.getBrCvorova()];	
		for(Graf.ElemC pom1= g.getPrviC(); pom1!=null ; pom1=pom1.sled,i++)
			cvorovi[i]=pom1.c;
		int n=g.getBrCvorova();
		
		for(i=0;i<n;i++) {
			for(j=0;j<n;j++) 
		        if(g.postojiGranaNeusmerena(cvorovi[i].getIme(), cvorovi[j].getIme()))
			        graph[i][j]=1;
		        else
		        	graph[i][j]=0;
		    j=0;
	    }
		
		if(!g.daLiJeUsmeren()) {
			int src=0;
			for(Graf.ElemC pom1= g.getPrviC(); pom1!=null ; pom1=pom1.sled,src++)
			    if(pom1.c.getIme().equals(c1))
			    	break;
	
			int dist[]=new int[g.getBrCvorova()]; 

            boolean sptSet[]=new boolean[g.getBrCvorova()]; 
            
            Cvor prethodnici[]=new Cvor[n];

            for (i = 0; i < g.getBrCvorova(); i++) {
            dist[i] = Integer.MAX_VALUE; sptSet[i] = false; prethodnici[i]=null;
            }
            
            dist[src] = 0; 
            
			
            for (int count = 0; count < g.getBrCvorova()-1; count++)
            {
              // Pick the minimum distance vertex from the set of vertices not
              // yet processed. u is always equal to src in the first iteration.
              int u = minDistance(dist, sptSet);
  
              // Mark the picked vertex as processed
              sptSet[u] = true;
         
              // Update dist value of the adjacent vertices of the picked vertex.
              for (int v = 0; v < g.getBrCvorova(); v++)
         
                // Update dist[v] only if is not in sptSet, there is an edge from 
                // u to v, and total weight of path from src to  v through u is 
                // smaller than current value of dist[v]
                if (sptSet[v]==false && graph[u][v]!=0 && dist[u] != Integer.MAX_VALUE && dist[u]+graph[u][v] < dist[v]) {
                   dist[v] = dist[u] + graph[u][v];
                   prethodnici[v]=cvorovi[u];
                }
            }
            //Bojenje grafa i vracanje rezultata
            int dst=0;
            for(i=0;i<n;i++,dst++)
            	if(cvorovi[i].getIme().equals(c2)) {
            		int it=dst;
            		Cvor ja=cvorovi[dst];
            		while(!prethodnici[it].getIme().equals(c1)) {
            		    for(int it2=0;it2<n;it2++)
            		    	if(cvorovi[it2].getIme().equals(prethodnici[it].getIme())) {
            		    		System.out.println(cvorovi[it2].getIme());
            		    		for (Graf.ElemG tek=g.getPrvaG();tek!=null;tek=tek.sled)
            		    			if((tek.g.getIme1().equals(cvorovi[it2].getIme()) && tek.g.getIme2().equals(ja.getIme()))|| (tek.g.getIme2().equals(cvorovi[it2].getIme()) && tek.g.getIme1().equals(ja.getIme()))) {
            		    				tek.g.setBoja(Color.MAGENTA);
            		    				tek.g.setDebljina(4);
            		    			}
            		    		ja=cvorovi[it2];
            		    		it=it2;
            		    		break;
            		    	}
            			
            			
            		}	
            		for (Graf.ElemG tek=g.getPrvaG();tek!=null;tek=tek.sled)
		    			if((tek.g.getIme1().equals(prethodnici[it].getIme()) && tek.g.getIme2().equals(ja.getIme()))|| (tek.g.getIme2().equals(prethodnici[it].getIme()) && tek.g.getIme1().equals(ja.getIme()))) {
		    				tek.g.setBoja(Color.MAGENTA);
		    				tek.g.setDebljina(4);
		    			}
            		s.repaint();
            		return dist[dst];
            	}
		}
		
        return 1;
	}

    public void Expand(double i) {
    	int xc=s.getWidth()/2, yc=s.getHeight()/2;
    	
    	for(Graf.ElemC tek=g.getPrviC();tek!=null;tek=tek.sled) {
    		tek.c.setX((int)(tek.c.getX()+(1-i)*(xc-tek.c.getX())));
    		tek.c.setY((int)(tek.c.getY()+(1-i)*(yc-tek.c.getY())));
    	}
    	
    	s.repaint();
    }
    public void Contract(double i) {
    	int xc=s.getWidth()/2, yc=s.getHeight()/2;
    	
    	for(Graf.ElemC tek=g.getPrviC();tek!=null;tek=tek.sled) {
    		tek.c.setX((int)(tek.c.getX()+(xc-tek.c.getX())*(1-i)));
    		tek.c.setY((int)(tek.c.getY()+(yc-tek.c.getY())*(1-i)));
    	}
    	
    	s.repaint();
    }
    
	public void ForceAtlas() {
		int i=0,n=g.getBrCvorova();
		
		Cvor cvorovi[]=new Cvor[n];	
			
		for(Graf.ElemC pom1= g.getPrviC(); pom1!=null ; pom1=pom1.sled,i++)
			cvorovi[i]=pom1.c;
		
		for(i=0;i<n-1;i++) {
			Cvor max=cvorovi[i];
			int pos=i;
			for(int j=i+1;j<n;j++)
				if(cvorovi[j].brojGrana()>max.brojGrana()) {
					max=cvorovi[j];
					pos=j;
				}
			cvorovi[pos]=cvorovi[i];
			cvorovi[i]=max;
		}
		
		//Koliko cvorova ima maks broja grana koje cemo smestiti u centar
		int maks=cvorovi[0].brojGrana();
		int koliko=0; i=0;
		while (cvorovi[i].brojGrana()==maks) {
			koliko++;
			i++;
		}
		i=0;
	
		//Sortiranje po pripadnosti cvorova
		Set<Cvor> skup = new HashSet<Cvor>();
		int pre=0,posle=koliko;
		while(posle<n) {
			//Punjenje skupa cvorova roditelja
			for(int j=pre;j<posle;j++) {
				for (int k =0; k< cvorovi[j].brojGrana();k++) {
				    skup.add(cvorovi[j].uzmiKomsiju());
				}
			}
			int brojZaBreak=cvorovi[posle].brojGrana(); //Sortiranje dela sa istim brojem grana
			int posleposle=posle;
			while(cvorovi[posleposle].brojGrana()>=brojZaBreak) {
				posleposle++;
				if(posleposle==n) break;
			}
			i=posle;
			for(int j=posle ; i<posleposle; i++) { 
				if(!skup.contains(cvorovi[j]) && cvorovi[j].brojGrana()>=brojZaBreak) {
					int k=j+1;
					while(cvorovi[k].brojGrana()>=brojZaBreak) {//Guranje cvora na kraj svog dela ukoliko ne pripada
						Cvor zam = cvorovi[k-1];
						cvorovi[k-1]=cvorovi[k];
						cvorovi[k]=zam;
						k++;
						if (k==n) break;
					}
				}
				else {
					j++;
					if(j==n) break;
					if (cvorovi[j].brojGrana()<brojZaBreak) {
						break;
					}
				}
			}
			pre = posle;
			posle=posleposle;
		}

		int pKruga = (int)( Cvor.getR()*Cvor.getR()*PI*s.getBrZoom()/4 );
		final int lDefault= (int)((koliko+1)*Cvor.getR()*s.getBrZoom());
		int xc=s.getWidth()/2,yc=s.getHeight()/2;
		int l2=lDefault, l1=l2;
		boolean sece=false;
		
		//Smestanje u centralnu kocku
		for (i = 0; i < koliko ; i++) {
			int rx=0,ry=0;
		    for (int k=0; k<20 ; k++) { //Random puta generisati
		    	rx = (int) (Math.random()*l2 + xc-l2/2 ); 
			    ry = (int) (Math.random()*l2 + yc-l2/2 ); 
			 
			    for (int j=0;j<i;j++)
					if((new Rectangle(rx,ry,(int)(Cvor.getR()*Scena.getBrZoom()),(int)(Cvor.getR()*Scena.getBrZoom())).intersects(new Rectangle(cvorovi[j].getX(),cvorovi[j].getY(),(int)(Cvor.getR()*Scena.getBrZoom()),(int)(Cvor.getR()*Scena.getBrZoom()))))) {
						sece=true;
						break;
					}
					if (sece==false) {
						break;
					}
					sece=false;
		    }
		    cvorovi[i].setInicX((int)(rx/Scena.getBrZoom()));
			cvorovi[i].setInicY((int)(ry/Scena.getBrZoom()));
		}
		//Centar je generisan
		
		for (i=koliko ; i<n ; i++ ) { //Uraditi za svaki preostali cvor
			l2=l1;
			l1=l2+lDefault*2;
			int pKrofne = l1*l1 - l2*l2;
			int nKrofne=pKrofne/pKruga/20;
			for (int j = 0 ; j< nKrofne && i<n; j++, i++) { //Koliko cvorova smestiti u krofnu
				int rx=0,ry=0;
			    for (int k=0; k<20 ; k++) { //Random puta generisati
			    	rx=(int)(Math.random()*l1 +xc-l1/2);
			    	ry=(int)(Math.random()*l1 +yc-l1/2);
			    	while(new Rectangle(xc-l2/2,yc-l2/2,l2,l2).intersects(new Rectangle(rx,ry,(int)(Cvor.getR()*Scena.getBrZoom()),(int)(Cvor.getR()*Scena.getBrZoom())))){
			    		rx=(int)(Math.random()*l1 +xc-l1/2);
				    	ry=(int)(Math.random()*l1 +yc-l1/2);
			    	}
			    	
			    	/*int rx1 = (int) (Math.random()*(l1/2-l2/2) + xc-l1/2 ); int rx2 = (int) (Math.random()*(l1/2-l2/2) + xc+l2/2 );	
				    int ry1 = (int) (Math.random()*(l1/2-l2/2) + yc-l1/2 ); int ry2 = (int) (Math.random()*(l1/2-l2/2) + yc+l1/2 );
				    rx = ((int)(Math.random()*2)==1?rx1:rx2);ry = ((int)(Math.random()*2)==1?ry1:ry2);
				    */
			    	for (int y=0;y<i;y++)
						if((new Rectangle(rx,ry,(int)(Cvor.getR()*Scena.getBrZoom()),(int)(Cvor.getR()*Scena.getBrZoom())).intersects(new Rectangle(cvorovi[y].getX(),cvorovi[y].getY(),(int)(Cvor.getR()*Scena.getBrZoom()),(int)(Cvor.getR()*Scena.getBrZoom()))))) {
							sece=true;
							break;
						}
						if (sece==false)
							break;
			    	sece=false;
						
			    }
			    cvorovi[i].setInicX((int)(rx/s.getBrZoom()));
				cvorovi[i].setInicY((int)(ry/s.getBrZoom()));
			}	
			
		}
		System.out.println("Tu sam");
	    s.repaint();
	}
}


