package grafovi;

import java.awt.Color;

public class Grana {
	private Cvor c1,c2;
	private String ime1, ime2;
	private int debljina;
	private Color boja;
	
	public Grana(Cvor c11,Cvor c12) {
		c1=c11;
		c2=c12;
		ime1=c1.getIme();
		ime2=c2.getIme();
		debljina=1;
		boja=Color.BLACK;
	}

	public int getDebljina() {
		return debljina;
	}

	public void setDebljina(int debljina) {
		this.debljina = debljina;
	}

	public Color getBoja() {
		return boja;
	}

	public void setBoja(Color boja) {
		this.boja = boja;
	}

	public Cvor getC1() {
		return c1;
	}
	public Cvor getC2() {
		return c2;
	}
	public String getIme1() {
		return ime1;
	}
	public String getIme2() {
		return ime2;
	}
	
}
