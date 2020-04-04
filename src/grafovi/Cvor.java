package grafovi;

import java.awt.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Cvor {
	private int x,y,inicX,inicY;
	private String ime;
	private boolean kliknut;
	private int svojR=r;
	private Color svojaBoja=boja;
	
	private static int r=40;
	private static Color boja=Color.RED;
	private static int fontSize=18;
	private static Font font= new Font("Serif",Font.BOLD,fontSize);
	
	private Set<Grana> grane;
	private int it=0;
	
	//Konstruktori
	public Cvor(int x, int y, String ime) {
		inicX=x;
		inicY=y;
		this.ime=ime;
		this.x=(int)(inicX*Scena.getBrZoom());
		this.y=(int)(inicY*Scena.getBrZoom());
		svojR=(int)(r*Scena.getBrZoom());
		grane=new HashSet<Grana>();
	}
	
	public Color getSvojaBoja() {
		return svojaBoja;
	}
	public int brojGrana() {
		return grane.size();
	}
    public void dodajGranu(Grana g) {
    	grane.add(g);
    }
    public void brisiGranu(Grana g) {
    	grane.remove(g);
    }
    public Cvor uzmiKomsiju() {
    	Iterator<Grana> iter = grane.iterator();
    	int j=0;
    	it++;
    	Grana g=null;
    	while(iter.hasNext()) {
    		g=iter.next();
    		if(++j<it)
    			break;
    	}
    	if(g.getIme1().equals(ime)) {
    		return g.getC2();
    	}
    	else {
    		return g.getC1();
    	}
    }
    public boolean daLiIma(Grana g) {
    	if (grane!=null && grane.size()>0)
    	return grane.contains(g);
    	else
    		return false;
    }
	public void setSvojaBoja(Color svojaBoja) {
		this.svojaBoja = svojaBoja;
	}

	public void setInicX(int i) {
		inicX=i;
		//x=(int)(i*Scena.getBrZoom());
	}
	public void setInicY(int i) {
		inicY=i;
		//y=(int)(i*Scena.getBrZoom());
	}
	public int getInicX() {
		return inicX;
	}
	public int getInicY() {
		return inicY;
	}
	
	public int getSvojR() {
		return svojR;
	}

	public void setSvojR(int svojR) {
		this.svojR = svojR;
	}
	public void setZoomR(int z) {
		svojR=z*r;
	}


	public int okolniKrug() {
		if(kliknut) return 4;
		else return 0;
	}
	public Cvor(int x, int y) {
		this(x, y, null);
	}
	public Cvor() {
		this(0,0,null);
	}
	public Cvor(String ime) {
		this(0,0,ime);
	}
	
	//Getters & Setters
	
	
	
	public int getX() {
		return x;
	}
	public boolean isKliknut() {
		return kliknut;
	}

	public void setKliknut1() {
		this.kliknut = true;
	}
	public void setKliknut0() {
		this.kliknut = false;
	}

	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	public static int getR() {
		return r;
	}
	public static void setR(int rr) {
		r = rr;
	}
	public static Color getBoja() {
		return boja;
	}
	public static void setBoja(Color boj) {
		boja = boj;
	}
	public static Font getFont() {
		return font;
	}
	public static void setFont(Font fon) {
		font = fon;
	}

	public static int getFontSize() {
		return fontSize;
	}

	public static void setFontSize(int fontSize) {
		Cvor.fontSize = fontSize;
	}

	

}
