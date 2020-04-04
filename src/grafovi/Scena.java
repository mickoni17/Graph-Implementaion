package grafovi;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import grafovi.Graf.ElemC;

public class Scena extends Canvas implements MouseListener, MouseMotionListener,MouseWheelListener, KeyListener{
	
	
	private Graf graf;
	private JLabel[] labele;
	private JScrollPane sp = new JScrollPane();
	private int pretX=0,pretY=0;
	private boolean drziCtrl=false, ctrl=false, klik=false;
	private static double brZoom=1.;
	
	public Scena(Graf g,JLabel[] l,JPanel p) {
		setBackground(Color.WHITE);
		graf=g;
		labele=l;
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		addMouseListener(this);
		addKeyListener(this);
	}
	@Override
	public void paint(Graphics g) {
		graf.crtaj(g,this);
	}
	
	public void setGraf(Graf g) {
		graf=g;
	}
	public Cvor racunajPoziciju(){
		boolean sece=false;
		int x=0, y=0;
		for (int i = 0 ; i<20 ; i++) {
			x=(int)(Math.random()*this.getWidth());
			y=(int)(Math.random()*this.getHeight());
			if (x<3) x+=3;
			if (x>this.getWidth()-Cvor.getR()) x-=Cvor.getR();
			if (y<3) y+=3;
			if (y>this.getHeight()-Cvor.getR()) y-=Cvor.getR();
			for (Graf.ElemC tek = graf.getPrviC() ; tek!=null ; tek=tek.sled)
			if((new Rectangle(x,y,Cvor.getR(),Cvor.getR()).intersects(new Rectangle(tek.c.getX(),tek.c.getY(),Cvor.getR(),Cvor.getR())))) {
				sece=true;
				break;
			}
			if (sece==false)
				break;
			sece=false;
		}
		return new Cvor(x,y);
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		
		int rot = e.getWheelRotation();
		
		if (rot<0) {
			if(brZoom<8)
			    brZoom+=0.1;
		}
		else {
			if(brZoom>0.2)
				brZoom-=0.1;
		}
		
		if(brZoom>0.2 && brZoom <8) {
		double umnozak;
		if(brZoom==1.) umnozak=1.;
		else if(rot<0) umnozak=1.1;
		else umnozak=0.9;
		
		for(Graf.ElemC pom= graf.getPrviC(); pom!=null ; pom=pom.sled) {
			pom.c.setX((brZoom==1.?pom.c.getInicX():((int)(pom.c.getX()*umnozak))));
			pom.c.setY((brZoom==1.?pom.c.getInicY():((int)(pom.c.getY()*umnozak))));
		}	
			
		for(Graf.ElemC pom= graf.getPrviC(); pom!=null ; pom=pom.sled) 
			pom.c.setSvojR((int)(Cvor.getR()*brZoom));
		}
		repaint();
	}
	public static double getBrZoom() {
		return brZoom;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
   
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		
	}
	@Override
	public void mouseDragged(MouseEvent d) {
		
		//Inicijalizacija pretX i pretY
		if (klik==false) {
			klik=true;
			pretX=d.getX();pretY=d.getY();
		}
		
		boolean uslov=false;
		
		if (!d.isMetaDown() && !d.isAltDown())
		for(Graf.ElemC pom= graf.getPrviC(); pom!=null ; pom=pom.sled) 
			if(new Rectangle(d.getX(),d.getY(),2,2).intersects(new Rectangle(pom.c.getX(),pom.c.getY(),pom.c.getSvojR(),pom.c.getSvojR()))) {
			    uslov=true;
			    break;
			}
		if(uslov)
		for(Graf.ElemC pom= graf.getPrviC(); pom!=null ; pom=pom.sled)
			if (pom.c.isKliknut()) {
				if(pretX-d.getX()!=0)
				pom.c.setX(pom.c.getX()-(pretX-d.getX()));
				if(pretY-d.getY()!=0)
				pom.c.setY(pom.c.getY()-(pretY-d.getY()));
		
			    pom.c.setInicX((int)(pom.c.getX()/brZoom));
				pom.c.setInicY((int)(pom.c.getY()/brZoom));
				
			}
		
		
		if (d.isMetaDown()) {
			for(Graf.ElemC pom= graf.getPrviC(); pom!=null ; pom=pom.sled) {
				//pom.c.setX(pom.c.getX()+(pretX-d.getX()<0?-2:2));
				//pom.c.setY(pom.c.getY()+(pretY-d.getY()<0?-2:2));
				if(pretX-d.getX()!=0)
				pom.c.setX(pom.c.getX()+(pretX-d.getX()));
				if(pretY-d.getY()!=0)
				pom.c.setY(pom.c.getY()+(pretY-d.getY()));
			
			}
		}
		
		pretX=d.getX();pretY=d.getY();
		repaint();
		
	}
	@Override
	public void mouseMoved(MouseEvent d) {
		for(Graf.ElemC pom= graf.getPrviC(); pom!=null ; pom=pom.sled) 
			if(new Rectangle(d.getX(),d.getY(),2,2).intersects(new Rectangle(pom.c.getX(),pom.c.getY(),pom.c.getSvojR(),pom.c.getSvojR()))){
				labele[8].setText("Node "+ pom.c.getIme());
				if (!graf.daLiJeUsmeren()) {
				    StringBuilder s=new StringBuilder("Number of edges: "); int n=0;
				    for(Graf.ElemG tek = graf.getPrvaG(); tek!=null ; tek=tek.sled)
					    if(tek.g.getC1().getIme().equals(pom.c.getIme()) || tek.g.getC2().getIme().equals(pom.c.getIme())) n++;
				    s.append(""+n);
				    if (n!=0 && n<20) {
				    	s.append(" [ ");
				    for(Graf.ElemG tek = graf.getPrvaG(); tek!=null ; tek=tek.sled)
				    if(tek.g.getC1().getIme().equals(pom.c.getIme()) || tek.g.getC2().getIme().equals(pom.c.getIme()))
				    	s.append(pom.c.getIme()+"-"+(tek.g.getC1().getIme().equals(pom.c.getIme())?tek.g.getC2().getIme():tek.g.getC1().getIme())+" ");
				    s.append("]");
				    }
				    labele[9].setText(s.toString());
					labele[10].setText("");
					break;
				}
				else {
					StringBuilder s=new StringBuilder("Number of outgoing edges: "); int n=0;
				    for(Graf.ElemG tek = graf.getPrvaG(); tek!=null ; tek=tek.sled)
					    if(tek.g.getC1().getIme().equals(pom.c.getIme())) n++;
				    s.append(""+n);
				    if (n!=0) {
				    	s.append(" [ ");
				    for(Graf.ElemG tek = graf.getPrvaG(); tek!=null ; tek=tek.sled)
				    if(tek.g.getC1().getIme().equals(pom.c.getIme()))
				    	s.append(pom.c.getIme()+"-"+(tek.g.getC1().getIme().equals(pom.c.getIme())?tek.g.getC2().getIme():tek.g.getC1().getIme())+" ");
				    s.append("]");
				    }
				    labele[9].setText(s.toString());
				    s=new StringBuilder("Number of entering edges: "); n=0;
				    for(Graf.ElemG tek = graf.getPrvaG(); tek!=null ; tek=tek.sled)
					    if(tek.g.getC2().getIme().equals(pom.c.getIme())) n++;
				    s.append(""+n);
				    if (n!=0) {
				    	s.append(" [ ");
				    for(Graf.ElemG tek = graf.getPrvaG(); tek!=null ; tek=tek.sled)
				    if(tek.g.getC2().getIme().equals(pom.c.getIme()))
				    	s.append((tek.g.getC1().getIme().equals(pom.c.getIme())?tek.g.getC2().getIme():tek.g.getC1().getIme())+"-"+pom.c.getIme()+" ");
				    s.append("]");
				    }
				    labele[10].setText(s.toString());
				    break;
				}
			}
			else {
		        labele[8].setText((graf.daLiJeUsmeren()?"Directed":"Undirected")+" Graph");
		        labele[9].setText("Number of nodes: "+graf.getBrCvorova());
		        labele[10].setText("Number of edges: "+graf.getBrGrana());
			}
	}
	@Override
	public void mousePressed(MouseEvent d) {

		boolean uslov=false;
		for(Graf.ElemC pom= graf.getPrviC(); pom!=null ; pom=pom.sled)
			if(new Rectangle(d.getX(),d.getY(),2,2).intersects(new Rectangle(pom.c.getX(),pom.c.getY(),pom.c.getSvojR(),pom.c.getSvojR()))) {
				uslov = true;
			}
		if(uslov==false) {
			for(Graf.ElemC pom= graf.getPrviC(); pom!=null ; pom=pom.sled)
				pom.c.setKliknut0();
			ctrl=false;
		}
		else {
		    if(ctrl) {
		    for(Graf.ElemC pom= graf.getPrviC(); pom!=null ; pom=pom.sled)
			    if(new Rectangle(d.getX(),d.getY(),2,2).intersects(new Rectangle(pom.c.getX(),pom.c.getY(),pom.c.getSvojR(),pom.c.getSvojR()))) {	
				    if(pom.c.isKliknut()==false && drziCtrl==false) {
				    	for(Graf.ElemC tek= graf.getPrviC(); tek!=null ; tek=tek.sled) {
				    		tek.c.setKliknut0();
				    	    ctrl=false;
				    	}
				    }
			    	pom.c.setKliknut1();
			    }
		    }
		    else {
			    for(Graf.ElemC pom= graf.getPrviC(); pom!=null ; pom=pom.sled)
				    pom.c.setKliknut0();
			    for(Graf.ElemC pom= graf.getPrviC(); pom!=null ; pom=pom.sled)
				    if(new Rectangle(d.getX(),d.getY(),2,2).intersects(new Rectangle(pom.c.getX(),pom.c.getY(),pom.c.getSvojR(),pom.c.getSvojR()))) 	
				        pom.c.setKliknut1();
		    }
		}
		repaint();
	}
	@Override
	public void mouseReleased(MouseEvent e) {
	    klik=false;
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode()==KeyEvent.VK_CONTROL) {
			ctrl=true;
			drziCtrl=true;
		}
		if (e.getKeyCode()==KeyEvent.VK_A && drziCtrl) {
			for(Graf.ElemC pom= graf.getPrviC(); pom!=null ; pom=pom.sled)
			    pom.c.setKliknut1();
			repaint();
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode()==KeyEvent.VK_CONTROL) 
	        drziCtrl=false;
	}
	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	
}
