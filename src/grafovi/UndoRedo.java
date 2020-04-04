package grafovi;

public class UndoRedo {

	private class Elem{
		Graf g;
		Elem next,prev;
		public Elem(Graf g) {
			this.g=g;
		}
	}
	private Elem prvi,posl,tek;
	private int n=0;
	
	public UndoRedo(Graf g) {
		tek = prvi = posl = new Elem(g); n=1;
	}
	
	public void dodaj(Graf gg) {
		Graf g= gg.clone();
		if(n<=10) {
			if(tek.prev==null) {
			Elem novi = new Elem(g);
		    novi.next=prvi;
		    prvi.prev=novi;
		    prvi=novi;
		    tek=prvi;
			n++;
			}
			else {
				int p=0; prvi=tek;
				while(prvi.prev!=null) {
					p++;
					prvi=prvi.prev;
				} //Prvi se u ovom slucaju koristi kao pomocni
				n-=p;
				
				tek.prev=new Elem(g);
				prvi=tek.prev;
				tek=prvi;
			}
		}
		else {
			Elem novi = new Elem(g);
		    novi.next=prvi;
		    prvi.prev=novi;
		    prvi=novi;
		    posl=posl.prev;
		    posl.next=null;
		}
	}
	public Graf redo() {
		if (tek.prev!=null) {
			tek=tek.prev;
			return tek.g;
		}
		else 
			return null;
		
	}
	public Graf undo() {
		if (tek.next!=null) {
			tek=tek.next;
			return tek.g;
		}
		else
			return null;
	}
	public boolean mozeUndo() {
		if (tek.next!=null)
			return true;
		return false;
	}
	public boolean mozeRedo() {
		if (tek.prev!=null)
			return true;
		return false;
	}
}
