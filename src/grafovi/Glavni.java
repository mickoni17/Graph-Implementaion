package grafovi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;  

public class Glavni extends JFrame implements KeyListener {

	private boolean tasZ,tasCtrl,tasY;
	private JMenuBar meni;
	private JMenuItem[] meniOpcije=new JMenuItem[16];
	private Graf graf=new Graf(false);
	private JPanel opcije = new JPanel(new GridLayout(4,1)), info= new JPanel(new GridLayout(3,1));
	private JLabel labele[]=new JLabel[24];
	private JTextField[] tekst=new JTextField[16];
	private JButton[] dugmad = new JButton[13];
	private JFileChooser fc = new JFileChooser();
	private File f;
	private DodajCvorGranu d1 = new DodajCvorGranu(this);
	private BrisiCvorGranu d2 = new BrisiCvorGranu(this);
	private PromeniIme d3 = new PromeniIme(this);
	private CheckboxGroup grupa = new CheckboxGroup();
	private Checkbox[] izborBoja = new Checkbox[8];
	private PromeniBoju d4 = new PromeniBoju(this);
	private PromeniVelicinu d5 = new PromeniVelicinu(this);
	private PromeniFont d6 = new PromeniFont(this);
	private ShPathAlg d7 = new ShPathAlg(this);
	private ExpAlg d8 = new ExpAlg(this);
	private ContAlg d9 = new ContAlg(this);
	private Export d10=new Export(this);
    private Scena scena=new Scena(graf,labele,info);
    private UndoRedo stanja = new UndoRedo(graf.clone());
    private Datoteka datot;
	private Algoritmi alg;
	
	public Glavni(){
		
		super("Program");
		setSize(1000,800);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		praviMenu();	
		praviInfo();
		
		setJMenuBar(meni);
		add(scena,BorderLayout.CENTER);
		add(info,BorderLayout.SOUTH);
		addKeyListener(this);
	}

	public static void main(String[]args) {
		new Glavni();
	}
	
	public void praviInfo() {
		labele[8] = new JLabel((graf.daLiJeUsmeren()?"Directed":"Undirected")+" Graph");
		labele[9] = new JLabel("Number of nodes: "+graf.getBrCvorova());
		labele[10] = new JLabel("Number of edges: "+graf.getBrGrana());
		for (int i=8; i<11; i++) {
			labele[i].setFont(new Font("Serif",Font.BOLD,20));
			labele[i].setAlignmentX(JLabel.CENTER);
		}
		JPanel ploca1=new JPanel();
		ploca1.add(labele[8]);
		info.add(ploca1,BorderLayout.NORTH);
		ploca1=new JPanel();
		ploca1.add(labele[9]);
		info.add(ploca1,BorderLayout.CENTER);
		ploca1=new JPanel();
		ploca1.add(labele[10]);
		info.add(ploca1,BorderLayout.SOUTH);
		info.setPreferredSize(new Dimension(500,150));
	}
	public void praviMenu() {
		
		UIManager.put("PopupMenu.border", BorderFactory.createLineBorder(Color.black, 1));
		meni=new JMenuBar();
		
		OsluskivacMeni os = new OsluskivacMeni();
		
		JMenu meni1=new JMenu("File");
		JMenu meni2=new JMenu("Edit");
		JMenu meni3=new JMenu("Algorithm");
		
		meni1.add(meniOpcije[0]=new JMenuItem("Import"));
		meni1.add(meniOpcije[1]=new JMenuItem("Export"));
		meni1.add(meniOpcije[10]=new JMenuItem("Export Image"));
		meni2.add(meniOpcije[2]= new JMenuItem("Add"));
		meni2.add(meniOpcije[3]= new JMenuItem("Remove"));
		meni2.add(meniOpcije[4]= new JMenuItem("Edit Node Name"));
		meni2.add(meniOpcije[5]= new JMenuItem("Edit Color"));
		meni2.add(meniOpcije[6]= new JMenuItem("Edit Size"));
		meni2.add(meniOpcije[7]= new JMenuItem("Edit Font"));
		meni2.add(meniOpcije[8]= new JMenuItem("Undo (Crtl + Z)"));
		meni2.add(meniOpcije[9]= new JMenuItem("Redo (Ctrl + Y)"));
		meni3.add(meniOpcije[11]= new JMenuItem("Expansion"));
		meni3.add(meniOpcije[12]= new JMenuItem("Contraction"));
		meni3.add(meniOpcije[13]= new JMenuItem("Force Atlas"));
		meni3.add(meniOpcije[14]= new JMenuItem("Shortest Path"));
		
		for (int i=0;i<15;i++) 
			meniOpcije[i].addActionListener(os);
	
		meni.add(meni1); meni.add(meni2); meni.add(meni3);
	}
	
	private class OsluskivacMeni implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String komanda = e.getActionCommand();
			if (komanda.equals("Import")) birajFile();
			if (komanda.equals("Add")) d1.setVisible(true);
			if (komanda.equals("Remove")) d2.setVisible(true);
			if (komanda.equals("Edit Node Name")) d3.setVisible(true);
			if (komanda.equals("Edit Color")) d4.setVisible(true);
			if (komanda.equals("Edit Size")) d5.setVisible(true);
			if (komanda.equals("Edit Font")) d6.setVisible(true);
			if (komanda.equals("Undo (Crtl + Z)")) if(stanja.mozeUndo()) { graf = stanja.undo(); scena.setGraf(graf); scena.repaint(); }
			if (komanda.equals("Redo (Ctrl + Y)")) if(stanja.mozeRedo()) { graf = stanja.redo(); scena.setGraf(graf); scena.repaint(); }
			if (komanda.equals("Shortest Path")) d7.setVisible(true);
			if (komanda.equals("Contraction")) d9.setVisible(true);
			if (komanda.equals("Expansion")) d8.setVisible(true);
			if (komanda.equals("Force Atlas"))  {alg=new Algoritmi(graf,scena);alg.ForceAtlas();}
			if (komanda.equals("Export")) d10.setVisible(true);
			if (komanda.equals("Export Image")) JOptionPane.showMessageDialog(Glavni.this,"Option not yet created");
		}
	}
	private class Export extends JDialog{
		public Export(Frame roditelj) {
			super(Glavni.this,"Adding",false);
			setSize(180,120);
			setResizable(false);
			
		    JPanel p= new JPanel();
		    p.add(dugmad[11] = new JButton("CSV"),BorderLayout.WEST);
		    p.add(dugmad[12] = new JButton("GML"),BorderLayout.EAST);
		    
		    add(p);
		    
		    AkcijaCSV osCSV = new AkcijaCSV(); dugmad[11].addActionListener(osCSV);
		    AkcijaGML osGML = new AkcijaGML(); dugmad[12].addActionListener(osGML);
		}
	}
	
	private class DodajCvorGranu extends JDialog {
		public DodajCvorGranu(Frame roditelj) {
			super(Glavni.this,"Adding",false);
			setSize(280,220);
			setResizable(false);
			
			JPanel pom=new JPanel(new GridLayout(4,1)), pom1=new JPanel();;
			
			pom.add(labele[0]=(new JLabel("Node")));
			labele[0].setFont(new Font("Serif",Font.BOLD,20));
			labele[0].setHorizontalAlignment(JLabel.CENTER);
			
			pom1.add(tekst[0]= new JTextField(), BorderLayout.WEST);
			pom1.add(dugmad[0] = new JButton("Add"),BorderLayout.EAST);
			tekst[0].setPreferredSize(new Dimension(100,30));
			dugmad[0].setPreferredSize(new Dimension(60,30));
			pom.add(pom1);
			
			DodajCvor osDC = new DodajCvor(); dugmad[0].addActionListener(osDC);
			
			pom.add(labele[1]=(new JLabel("Edge")));
			labele[1].setFont(new Font("Serif",Font.BOLD,20));
			labele[1].setHorizontalAlignment(JLabel.CENTER);
			
			pom1=new JPanel();
			pom1.add(tekst[1]= new JTextField(), BorderLayout.WEST);
			pom1.add(tekst[2]= new JTextField(), BorderLayout.CENTER);
			pom1.add(dugmad[1] = new JButton("Add"),BorderLayout.EAST);
			tekst[1].setPreferredSize(new Dimension(80,30));
			tekst[2].setPreferredSize(new Dimension(80,30));
			dugmad[1].setPreferredSize(new Dimension(60,30));
			pom.add(pom1);
			
			DodajGranu osDG = new DodajGranu(); dugmad[1].addActionListener(osDG);
			
			add(pom);
		}
	}
	private class BrisiCvorGranu extends JDialog { // UREDITI
		public BrisiCvorGranu(Frame roditelj) {
			super(Glavni.this,"Removing",false);
			setSize(280,220);
			setResizable(false);
			
			JPanel pom=new JPanel(new GridLayout(4,1)), pom1=new JPanel();;
			
			pom.add(labele[2]=(new JLabel("Node")));
			labele[2].setFont(new Font("Serif",Font.BOLD,20));
			labele[2].setHorizontalAlignment(JLabel.CENTER);
			
			pom1.add(tekst[3]= new JTextField(), BorderLayout.WEST);
			pom1.add(dugmad[2] = new JButton("Remove"),BorderLayout.EAST);
			tekst[3].setPreferredSize(new Dimension(100,30));
			dugmad[2].setPreferredSize(new Dimension(80,30));
			pom.add(pom1);
			
			BrisiCvor osBC = new BrisiCvor(); dugmad[2].addActionListener(osBC);
			
			pom.add(labele[3]=(new JLabel("Edge")));
			labele[3].setFont(new Font("Serif",Font.BOLD,20));
			labele[3].setHorizontalAlignment(JLabel.CENTER);
			
			pom1=new JPanel();
			pom1.add(tekst[4]= new JTextField(), BorderLayout.WEST);
			pom1.add(tekst[5]= new JTextField(), BorderLayout.CENTER);
			pom1.add(dugmad[3] = new JButton("Remove"),BorderLayout.EAST);
			tekst[4].setPreferredSize(new Dimension(80,30));
			tekst[5].setPreferredSize(new Dimension(80,30));
			dugmad[3].setPreferredSize(new Dimension(80,30));
			pom.add(pom1);
			
			BrisiGranu osBG = new BrisiGranu(); dugmad[3].addActionListener(osBG);
			
			add(pom);
		}
	}
	private class PromeniIme extends JDialog {
		public PromeniIme(Frame roditelj) {
			super(Glavni.this,"Node Name",false);
			setSize(360,165);
			setResizable(false);
			
			JPanel ploca1= new JPanel();
			labele[4]=new JLabel("Current node name");
			tekst[6]= new JTextField();
			labele[4].setFont(new Font("Serif",0,18));
			tekst[6].setPreferredSize(new Dimension(80,30));
			
			ploca1.add(labele[4],BorderLayout.WEST);
			ploca1.add(tekst[6],BorderLayout.EAST);
			add(ploca1,BorderLayout.NORTH);
			
			JPanel ploca2=new JPanel();
			ploca1=new JPanel();
			labele[5]=new JLabel("New node name");
			tekst[7]= new JTextField();
			labele[5].setFont(new Font("Serif",0,18));
			tekst[7].setPreferredSize(new Dimension(80,30));
			
			ploca1.add(labele[5],BorderLayout.WEST);
			ploca1.add(tekst[7],BorderLayout.EAST);
			add(ploca1,BorderLayout.CENTER);
			
			ploca1=new JPanel();
			ploca1.setPreferredSize(new Dimension(10,40));
			dugmad[4]=new JButton("Change");
			ploca2.add(ploca1,BorderLayout.WEST);
			ploca2.add(dugmad[4],BorderLayout.CENTER);
			add(ploca2,BorderLayout.SOUTH);
			
			AkcijaIme1 osAI1 = new AkcijaIme1(); dugmad[4].addActionListener(osAI1);
			
		}
	}
	private class PromeniBoju extends JDialog {
		public PromeniBoju(Frame roditelj) {
			super(Glavni.this,"Coloring",false);
			setBounds(Glavni.this.getWidth()/2,Glavni.this.getHeight()/2,100,300);
			setResizable(false);
			
			JPanel ploca = new JPanel(new GridLayout(8,2));
			izborBoja[0]= new Checkbox(null, grupa, true); izborBoja[1]= new Checkbox(null, grupa, false);
			izborBoja[2]= new Checkbox(null, grupa, false);izborBoja[3]= new Checkbox(null, grupa, false);
			izborBoja[4]= new Checkbox(null, grupa, false);izborBoja[5]= new Checkbox(null, grupa, false);
			izborBoja[6]= new Checkbox(null, grupa, false);izborBoja[7]= new Checkbox(null, grupa, false);
	        JPanel ploca1 = new JPanel(); ploca1.setBackground(Color.RED);ploca.add(ploca1); ploca.add(izborBoja[0]);
	        ploca1 = new JPanel(); ploca1.setBackground(Color.GREEN);ploca.add(ploca1); ploca.add(izborBoja[1]);
	        ploca1 = new JPanel(); ploca1.setBackground(Color.BLUE);ploca.add(ploca1); ploca.add(izborBoja[2]);
	        ploca1 = new JPanel(); ploca1.setBackground(Color.YELLOW);ploca.add(ploca1); ploca.add(izborBoja[3]);
	        ploca1 = new JPanel(); ploca1.setBackground(Color.ORANGE);ploca.add(ploca1); ploca.add(izborBoja[4]);
	        ploca1 = new JPanel(); ploca1.setBackground(Color.WHITE);ploca.add(ploca1);ploca.add(izborBoja[5]);
	        ploca1 = new JPanel(); ploca1.setBackground(Color.PINK);ploca.add(ploca1);ploca.add(izborBoja[6]);
	        ploca1 = new JPanel(); ploca1.setBackground(Color.GRAY);ploca.add(ploca1);ploca.add(izborBoja[7]);
	        add(ploca,BorderLayout.CENTER);
			ploca1=new JPanel();
			ploca1.add(dugmad[5]=new JButton("Apply"));
			
			add(ploca1,BorderLayout.SOUTH );
			AkcijaBoja osAB=new AkcijaBoja(); dugmad[5].addActionListener(osAB);
		}
	}
	private class PromeniVelicinu extends JDialog {
		public PromeniVelicinu(Frame roditelj) {
			super(Glavni.this,"Set Size",false);
			setBounds(Glavni.this.getWidth()/2,Glavni.this.getHeight()/2,260,120);
			setResizable(false);
			
			JPanel ploca = new JPanel();
			labele[6]= new JLabel("Enter node size");
			tekst[8]=new JTextField();
			ploca.add(labele[6],BorderLayout.WEST);
			ploca.add(tekst[8],BorderLayout.EAST);
			labele[6].setFont(new Font("Serif",0,18));
			tekst[8].setPreferredSize(new Dimension(80,30));
			add(ploca,BorderLayout.CENTER);
			
			JPanel ploca1 = new JPanel();
			ploca1.add(dugmad[6]=new JButton("Apply"));
			add(ploca1,BorderLayout.SOUTH);
			
			AkcijaVelicina osAV =new AkcijaVelicina(); dugmad[6].addActionListener(osAV);
		}
	}
	private class PromeniFont extends JDialog {
		public PromeniFont(Frame roditelj) {
			super(Glavni.this,"Set Font",false);
			setBounds(Glavni.this.getWidth()/2,Glavni.this.getHeight()/2,260,120);
			setResizable(false);
			
			JPanel ploca = new JPanel();
			labele[7]= new JLabel("Enter font size");
			tekst[9]=new JTextField();
			ploca.add(labele[7],BorderLayout.WEST);
			ploca.add(tekst[9],BorderLayout.EAST);
			labele[7].setFont(new Font("Serif",0,18));
			tekst[9].setPreferredSize(new Dimension(80,30));
			add(ploca,BorderLayout.CENTER);
			
			JPanel ploca1 = new JPanel();
			ploca1.add(dugmad[7]=new JButton("Apply"));
			add(ploca1,BorderLayout.SOUTH);
			
			AkcijaFont osAF =new AkcijaFont(); dugmad[7].addActionListener(osAF);
		}
	}
	
	private class ShPathAlg extends JDialog{
		public ShPathAlg(JFrame roditelj) {
			super(Glavni.this,"Shortest Path",false);
			setBounds(Glavni.this.getWidth()/2,Glavni.this.getHeight()/2,260,220);
			setResizable(false);
			
			JPanel p1 = new JPanel();
			p1.add(labele[15]=new JLabel("Source Node:"),BorderLayout.WEST);
			p1.add(tekst[10]=new JTextField(),BorderLayout.EAST);
			labele[15].setFont(new Font("Serif",0,18));
			tekst[10].setPreferredSize(new Dimension(80,30));
			
			JPanel p2=new JPanel();
			p2.add(labele[16]=new JLabel("Destination Node:"),BorderLayout.WEST);
			p2.add(tekst[11]=new JTextField(),BorderLayout.EAST);
			labele[16].setFont(new Font("Serif",0,18));
			tekst[11].setPreferredSize(new Dimension(80,30));
			
			JPanel p3=new JPanel();
			p3.add(dugmad[8]=new JButton("Show"));
			
			JPanel p4=new JPanel();
			p4.add(labele[17]=new JLabel());
			labele[17].setFont(new Font("Serif",0,18));
			
			JPanel osnova=new JPanel(new GridLayout(4,1));
			osnova.add(p1);
			osnova.add(p2);
			osnova.add(p4);
			osnova.add(p3);
			add(osnova);
			
			AkcijaSP osSP= new AkcijaSP(); dugmad[8].addActionListener(osSP);
			
			addWindowListener(new WindowAdapter() 
			{
			  public void windowClosing(WindowEvent e)
			  {
				  System.out.println("Ej");
					for (Graf.ElemG tek=graf.getPrvaG();tek!=null;tek=tek.sled) {
		    				tek.g.setBoja(Color.BLACK);
		    				tek.g.setDebljina(1);
		    			}
					scena.repaint();
			  }

			});
		}

		
	}

    private class ExpAlg extends JDialog{
		public ExpAlg(JFrame roditelj) {
			super(Glavni.this,"Expansion",false);
			setBounds(Glavni.this.getWidth()/2,Glavni.this.getHeight()/2,260,150);
			setResizable(false);
			
			JPanel p1 = new JPanel();
			p1.add(labele[18]=new JLabel("Expansion number (>1):"),BorderLayout.WEST);
			p1.add(tekst[12]=new JTextField(),BorderLayout.EAST);
			labele[18].setFont(new Font("Serif",0,18));
			tekst[12].setPreferredSize(new Dimension(80,30));
			
			JPanel p2=new JPanel();
			p2.add(dugmad[9]=new JButton("Expand"));
			
			JPanel osnova=new JPanel(new GridLayout(2,1));
			osnova.add(p1);
			osnova.add(p2);
			add(osnova);
			
			AkcijaExp osE= new AkcijaExp(); dugmad[9].addActionListener(osE);
		}
	}
    private class ContAlg extends JDialog{
	    public ContAlg(JFrame roditelj) {
	    	super(Glavni.this,"Contraction",false);
			setBounds(Glavni.this.getWidth()/2,Glavni.this.getHeight()/2,260,150);
			setResizable(false);
			
			JPanel p1 = new JPanel();
			p1.add(labele[19]=new JLabel("Contraction number (<1):"),BorderLayout.WEST);
			p1.add(tekst[13]=new JTextField(),BorderLayout.EAST);
			labele[19].setFont(new Font("Serif",0,18));
			tekst[13].setPreferredSize(new Dimension(80,30));
			
			JPanel p2=new JPanel();
			p2.add(dugmad[10]=new JButton("Contract"));
			
			JPanel osnova=new JPanel(new GridLayout(2,1));
			osnova.add(p1);
			osnova.add(p2);
			add(osnova);
			
			AkcijaCont osC= new AkcijaCont(); dugmad[10].addActionListener(osC);
	    }
    }
	
	private class DodajCvor implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			Cvor poz = scena.racunajPoziciju();
			if(tekst[0].getText().equals("")) return;
			graf.dodajCvor(new Cvor(poz.getX(),poz.getY(),tekst[0].getText()));
			
			scena.repaint();
			labele[9].setText("Number of nodes: "+graf.getBrCvorova());
			stanja.dodaj(graf);
		}
	}
	private class DodajGranu implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			
			Graf.ElemC pom = graf.getPrviC();
			Cvor c1=null,c2=null;
			while (pom!=null) {
				if(pom.c.getIme().equals(tekst[1].getText()))
					c1=pom.c;
				if(pom.c.getIme().equals(tekst[2].getText()))
					c2=pom.c;
				pom = pom.sled;
			}
			if (c1!=null && c2!=null) {
			    if(graf.daLiJeUsmeren())
				    graf.postaviGranuUsmeren(new Grana(c1,c2));
			    else
				    graf.postaviGranuNeusmeren(new Grana(c1,c2));
			    
			    scena.repaint();
			    labele[10].setText("Number of edges: "+graf.getBrGrana());
			    stanja.dodaj(graf);
			}
			else
				JOptionPane.showMessageDialog(Glavni.this, "Invalid edgde");
		}
	}
	private class BrisiCvor implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			if (graf.brisiCvor(tekst[3].getText())==false) {
				JOptionPane.showMessageDialog(Glavni.this, "That node does not exist!");
				return;
			}
			scena.repaint();
			labele[9].setText("Number of nodes: "+graf.getBrCvorova());
			labele[10].setText("Number of edges: "+graf.getBrGrana());
			stanja.dodaj(graf);
		}
	}
	private class BrisiGranu implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			
			Grana dlt=null;
			//Ovo je slucaj samo za neusmeren graf
			for(Graf.ElemG tek=graf.getPrvaG();tek!=null;tek=tek.sled)
				if((tek.g.getIme1().equals(tekst[4].getText())&&tek.g.getIme2().equals(tekst[5].getText())) || (tek.g.getIme1().equals(tekst[5].getText())&&tek.g.getIme2().equals(tekst[4].getText()))) {
					dlt=tek.g;
					break;
				}
			        
			if (graf.brisiGranu(dlt)==false) {
				JOptionPane.showMessageDialog(Glavni.this, "That edge does not exist!");
				return;
			}
			scena.repaint();
		    labele[10].setText("Number of edges: "+graf.getBrGrana());
		    stanja.dodaj(graf);
		}
	}
	private class AkcijaIme1 implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			Graf.ElemC pom = graf.getPrviC();
			if (tekst[6].getText().equals("")||tekst[7].getText().equals("")) {
				JOptionPane.showMessageDialog(Glavni.this, "Please enter node name");
			    return;
			}
			while(pom!=null && !pom.c.getIme().equals(tekst[6].getText()))
				pom=pom.sled;

			if (pom==null) {
				JOptionPane.showMessageDialog(Glavni.this, "Such node does not exist");
				return;
			}
			Graf.ElemC pom2=graf.getPrviC();
				
			while(pom2!=null) {
			if(pom2.c.getIme().equals(tekst[7].getText())){
				JOptionPane.showMessageDialog(Glavni.this, "Such node already exists");
				return;
			}
			pom2=pom2.sled;
			}
			
			pom.c.setIme(tekst[7].getText());
			scena.repaint();	
			stanja.dodaj(graf);
		}
	}
	private class AkcijaBoja implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			Color c;
			if (izborBoja[0].getState()==true) c=Color.RED;
			else if (izborBoja[1].getState()==true) c=Color.GREEN;
			else if (izborBoja[2].getState()==true) c=Color.BLUE;
			else if (izborBoja[3].getState()==true) c=Color.YELLOW;
			else if (izborBoja[4].getState()==true) c=Color.ORANGE;
			else if (izborBoja[5].getState()==true) c=Color.WHITE;
			else if (izborBoja[6].getState()==true) c=Color.PINK;
			else  c=Color.GRAY;
            Cvor.setBoja(c);
			scena.repaint();
			stanja.dodaj(graf);
		}
	}
	private class AkcijaVelicina implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			Cvor.setR(Integer.parseInt(tekst[8].getText()));
			scena.repaint();
			stanja.dodaj(graf);
		}
	}
	private class AkcijaFont implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
            Cvor.setFont(new Font("Serif",Font.BOLD,Integer.parseInt(tekst[9].getText())));
            Cvor.setFontSize(Integer.parseInt(tekst[9].getText()));
			scena.repaint();
			stanja.dodaj(graf);
		}
	}
	private class AkcijaSP implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			String s1=tekst[10].getText();
		    String s2=tekst[11].getText();
		    int i=0;
			for(Graf.ElemC pom= graf.getPrviC(); pom!=null ; pom=pom.sled)
			    if(pom.c.getIme().equals(s1)||pom.c.getIme().equals(s2))
			    	i++;
			if(i!=2) {
				JOptionPane.showMessageDialog(Glavni.this,"Node(s) not found!");
				return;
			}
			labele[17].setText("Calculating");
			alg=new Algoritmi(graf,scena);
			labele[17].setText("Path length: "+alg.ShortestPath(s1, s2));
			scena.repaint();
		}
		
	}
	private class AkcijaExp implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			String s1=tekst[12].getText();
			double i;
			if((i=Double.parseDouble(s1))<=1) {
				JOptionPane.showMessageDialog(Glavni.this,"Incorrect number!","Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			alg=new Algoritmi(graf,scena);
			alg.Expand(i);
		}
		
	}
	private class AkcijaCont implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			String s1=tekst[13].getText();
			double i;
			if((i=Double.parseDouble(s1))>=1) {
				JOptionPane.showMessageDialog(Glavni.this,"Incorrect number!","Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			alg=new Algoritmi(graf,scena);
			alg.Contract(i);
		}
		
	}
	private class AkcijaCSV implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			datot.exportCSV();
		}
		
	}
	private class AkcijaGML implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			datot.exportGML();
		}
		
	}
	
	public void birajFile() {
		FileNameExtensionFilter filter = new FileNameExtensionFilter("GML & CSV","gml","csv");
		fc.setFileFilter(filter);
        int returnVal = fc.showOpenDialog(null);
        f=fc.getSelectedFile();
        datot=new Datoteka(scena);
        graf=datot.importuj(f);
        scena.setGraf(graf);
        scena.repaint();
        stanja.dodaj(graf);
	}

	
	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
        case KeyEvent.VK_CONTROL: tasCtrl = true; break;
        case KeyEvent.VK_Z: tasZ = true; break;
        case KeyEvent.VK_Y: tasY = true; break;
		}
		if(tasCtrl&&tasZ) {
			 graf = stanja.undo(); scena.setGraf(graf); scena.repaint();
		}
		if(tasCtrl&&tasY) {
			 graf = stanja.redo(); scena.setGraf(graf); scena.repaint();
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()) {
        case KeyEvent.VK_CONTROL: tasCtrl= false; break;
        case KeyEvent.VK_Z: tasZ = false; break;
        case KeyEvent.VK_Y: tasY = false; break;
	}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
	
}

