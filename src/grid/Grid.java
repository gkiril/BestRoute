package grid;

import exceptions.MnZeroException;

import java.io.File;
import java.io.IOException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 *
 * @author Gashteovski
 */
public class Grid {
    //la vitese initial pour horizontal et vertical
    private int [][] speedHorizontal;
    private int [][] speedVertical;
    
    //la cout optimal pour horizontal et vertical
    private double [][] optCostHorizontal;
    private double [][] optCostVertical;
    
    //le temps optimal pour horizontal et vertical
    private double [][] optTimeHorizontal;
    private double [][] optTimeVertical;
    
    //la matrice 'num' qui va aider pour le mapping
    private int [][] num;
    
    //la vitese optimal
    private double optSpeed;
    
    /* la taille de quadrillage (m x n)
       m - la taille de l'axe x
       n - la taille de l'axe y
    */
    private int m;
    private int n;
    
    //la longeur de segments (en km)
    private double dist;
    
    /* les counts 
     * les coefficients a et b pour la consumation
     * fuel - le cout du carburant
     * toll - le cout pour la peage
     * alpha
     */
    private double a;
    private double b;
    private double fuel;
    private double toll;
    private double alpha;
    
    private double [] optDistCost;  // distances optimales pour la cout	
    private int [] optPredCost; //precedants optimals pour la cout
	
    private double [] optTime; // temps optimal
    private int [] optPredTime; // precedants de temps optimal
	
    private List<Integer> predList; // une list avec precedants
	
    private int fixedRadarNumber; // le nombre de radars fixe
    private int mobileRadarNumber; // le nombre de radars mobile
    private double expectation; // l'esperance
	
    private int [][] horizontalFixedRadars; // les radars fixes sur les segments horizontaux
    private int [][] verticalFixedRadars; // les radars fixes sur les segments verticaux
	
    private double [][][] horizontalV; // V1, V2, V3 - horizontal
    private double [][][] verticalV;   // V1, V2, V3 - vertical
	
    private double [][][] horizontalC;  // c1, c2, c3 - horizontal
    private double [][][] verticalC;    // c1, c2, c3 - vertical
	
    private double [][][] horizontalT; // t1, t2, t3 - horizontal
    private double [][][] verticalT;   // t1, t2, t3 - vertical
 	
    private double epsilon;
	
    private double minProbTime;
    private double minProbCost;
	
    public Grid(){
    }
    
    //constructeur avec valeurs d'entree seulement pour m et n
    public Grid(int M, int N){
    	
    	this.m = M;
    	this.n = N;
    	
    	speedHorizontal = new int [m][n+1];
    	speedVertical = new int [m+1][n];
    	
    	optCostHorizontal = new double [m][n+1];
    	optCostVertical = new double [m+1][n];
    	
    	optTimeHorizontal = new double [m][n+1];
    	optTimeVertical = new double [m+1][n];
    	
    	horizontalFixedRadars = new int[m][n+1];
    	this.setNoHorizontalFixedRadars();
    	
    	verticalFixedRadars = new int[m+1][n];
    	this.setNoVerticalFixedRadars();
    	
    	horizontalV = new double [m][n+1][4];
    	verticalV = new double [m+1][n][4];
    	
    	horizontalC = new double [m][n+1][4];
    	verticalC = new double [m+1][n][4];
    	
    	horizontalT = new double [m][n+1][4];
    	verticalT = new double [m+1][n][4];
    	
    	num = new int [m+1][n+1];
    	this.setNum();
    	this.setPredList();
    }

    //constructeur pour tous les valeurs sauf les valeurs optimales
    public Grid(int[][] speedHorizontal, int[][] speedVertical, int m, int n, int dist, double a, double b, double fuel, double toll) {
        this.speedHorizontal = speedHorizontal;
        this.speedVertical = speedVertical;
        this.m = m;
        this.n = n;
        this.dist = dist;
        this.a = a;
        this.b = b;
        this.fuel = fuel;
        this.toll = toll;
        this.num = new int [m+1][n+1];
        
        this.setOptSpeed();
        this.setOptCostHorizontal();
        this.setOptCostVertical();
        this.setNum();
        this.setPredList();
    }
    
    public boolean initializeFromXml(String file){
    	try{
            org.jdom.Document document = null;
            Element racine;
    	
            SAXBuilder sxb = new SAXBuilder();
            document = sxb.build(new File(file));
            	  
            racine = document.getRootElement();//recuperation de la racine du document xml
	      
            /*recuperation de la taille de l'axe des abscisses , et de l'axe des ordonnées*/
	      
            Element taille = racine.getChild("taille");
	    
            m  = Integer.parseInt(taille.getAttributeValue("abscisse"));
            n  = Integer.parseInt(taille.getAttributeValue("ordonnee"));
            
            if (m==0 && n==0)
                throw new MnZeroException();
		
            this.speedHorizontal = new int[m][n+1];
            this.speedVertical = new int[m+1][n];
		
            this.horizontalFixedRadars = new int[m][n+1];
            this.verticalFixedRadars = new int[m+1][n];
	    
            optCostHorizontal = new double [m][n+1];
            optCostVertical = new double [m+1][n];
    	
            optTimeHorizontal = new double [m][n+1];
            optTimeVertical = new double [m+1][n];
    	
            horizontalV = new double [m][n+1][4];
            verticalV = new double [m+1][n][4];
    	
            horizontalC = new double [m][n+1][4];
            verticalC = new double [m+1][n][4];
    	
            horizontalT = new double [m][n+1][4];
            verticalT = new double [m+1][n][4];
    	
            num = new int [m+1][n+1];
            this.setNum();
            this.setPredList();
	    
            int j,z=0;
		
            /*On parcourt l'ensemble des fils de l'element "horizontals". Chacun de ces fils
             est un element "horizontal" correspondant а une ligne horizontal de la grille.
             On parcourt l'ensemble des fils "segment" de chacun de ces element.
             Chaque element "segment" a le nombre de radar et la limitation de vitesse comme attributs.
             On initialise ainsi speedHorizontal et radarHorizontal*/
		
            @SuppressWarnings("rawtypes")
			Iterator i = racine.getChild("horizontals").getChildren().iterator();
            while(i.hasNext()) {	
                Element courant = (Element)i.next();
                @SuppressWarnings("rawtypes")
				Iterator ii = courant.getChildren().iterator();
                j=0;
                
                while(ii.hasNext()){
                    Element courant2 = (Element)ii.next();
                    int lim = Integer.parseInt(courant2.getAttributeValue("limVitesse"));
                    int nbRadar = Integer.parseInt(courant2.getAttributeValue("nbRadar"));
                    this.speedHorizontal[j][z]=lim;
                    this.horizontalFixedRadars[j][z]=nbRadar;
                    j++;
                }
		
                z++;
            }
		
            /*	On parcourt l'ensemble des fils de l'element "verticals". Chacun de ces fils
              	est un element "vertical" correspondant а une ligne vertical de la grille.
	      		On parcourt l'ensemble des fils "segment" de chacun de ces element.
	      		Chaque element "segment" a le nombre de radar et la limitation de vitesse comme attributs.
              	On initialise ainsi speedVertical et radarVertical*/
		
            i = racine.getChild("verticals").getChildren().iterator();
            j=0;
            
            while(i.hasNext()){	
                Element courant = (Element)i.next();
                @SuppressWarnings("rawtypes")
				Iterator ii = courant.getChildren().iterator();
                z=0;
                while(ii.hasNext()){
                    Element courant2 = (Element)ii.next();
                    int lim = Integer.parseInt(courant2.getAttributeValue("limVitesse"));
                    int nbRadar = Integer.parseInt(courant2.getAttributeValue("nbRadar"));
                    this.speedVertical[j][z]=lim;
                    this.verticalFixedRadars[j][z]=nbRadar;
                    z++;
                }	
                j++;	
            }
            return true;
        }
        
        catch (java.lang.NegativeArraySizeException e){
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame,
                "Vous devez entrer des nombres entiers positifs pour abscisse ou ordonnée",
                "Mauvaises données d'entrée",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        catch (java.lang.NumberFormatException e){
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame,
                "Vous devez entrer des nombres entiers positifs pour abscisse ou ordonnée",
                "Mauvaises données d'entrée",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        catch (MnZeroException e){
            return false;
        }
                
        catch(Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String msg = sw.toString();
            msg = msg.substring(0, 500) + " ...";
            
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame,
                msg,
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean initializeFromXls(String fichier) throws BiffException, IOException{
	
    	try{	
            /*On crée un objet workbook permettant de recuperer les données du fichier excel 
		 	passé en paramètre*/	
            Workbook workbook = Workbook.getWorkbook(new File(fichier));  
		
            /*On recupere ensuite la premiere feuille de calcul*/
            Sheet sheet = workbook.getSheet(0); 
		
            /*On recupere la taille de l'axe des ordonnées*/
            Cell t = sheet.getCell(0,2); 				
            n = Integer.parseInt(t.getContents());		
		
            /*On recupere la taille de l'axe des abscisse*/
            m =0;	
	    
            while(sheet.getCell(m+1,2).getType()!=CellType.EMPTY){	
                m++;	
            }
            
            if (m==0 && n==0)
                throw new MnZeroException();
		
            speedHorizontal = new int[m][n+1];
            speedVertical = new int[m+1][n];
            horizontalFixedRadars = new int[m][n+1];
            verticalFixedRadars = new int[m+1][n];
		
            this.speedHorizontal = new int[m][n+1];
            this.speedVertical = new int[m+1][n];
		
            this.horizontalFixedRadars = new int[m][n+1];
            this.verticalFixedRadars = new int[m+1][n];
	    
            optCostHorizontal = new double [m][n+1];
            optCostVertical = new double [m+1][n];
    	
            optTimeHorizontal = new double [m][n+1];
            optTimeVertical = new double [m+1][n];
    	
            horizontalV = new double [m][n+1][4];
            verticalV = new double [m+1][n][4];
    	
            horizontalC = new double [m][n+1][4];
            verticalC = new double [m+1][n][4];
    	
            horizontalT = new double [m][n+1][4];
            verticalT = new double [m+1][n][4];
    	
            num = new int [m+1][n+1];
            this.setNum();
            this.setPredList();
		
            System.out.println(m+" "+n);
		
		
            /*On initialise speedHorizontal et radarHorizontal */
		
            for(int i = 0 ;i<n+1;i++){
            	for(int j = 0;j<m;j++){					
                    speedHorizontal[j][i] = Integer.parseInt(sheet.getCell(j+1,n+2-i).getContents());
				
                    int z=1;
                    int nbRadar=0;
                    
                    while(z<sheet.getColumns() &&(sheet.getCell(z,2*n+15).getType())!=CellType.EMPTY){			
                        String s = sheet.getCell(z,2*n+15).getContents();
                        s = s.replaceAll(" ", "");
                        if(String.valueOf(s.charAt(1)).equals(Integer.toString(j)) && String.valueOf(s.charAt(3)).equals(Integer.toString(i)) && s.charAt(5)=='H'){
                        	nbRadar++;
                        }
			
                        z++;
                    }
		
                    z=1;
                    while(z<sheet.getColumns() &&(sheet.getCell(z,2*n+14).getType())!=CellType.EMPTY){			
                        String s = sheet.getCell(z,2*n+14).getContents();
                        s = s.replaceAll(" ", "");
                        if(String.valueOf(s.charAt(1)).equals(Integer.toString(j)) && String.valueOf(s.charAt(3)).equals(Integer.toString(i)) && s.charAt(5)=='H'){
                        	nbRadar++;
                        }
                        z++;
                    }
				
                    horizontalFixedRadars[j][i] = nbRadar;					
                }				
            }
			
            /*On initialise speedVertical et radarVertical */
		
            for(int i = 0; i<m+1; i++){
            	for(int j = 0; j<n; j++){
                    speedVertical[i][j] = Integer.parseInt(sheet.getCell(i+1,2*n+8-j).getContents());
				
                    int z=1;
                    int nbRadar=0;
                    
                    while(z<sheet.getColumns() &&(sheet.getCell(z,2*n+15).getType())!=CellType.EMPTY){			
                        String s = sheet.getCell(z,2*n+15).getContents();
                        s = s.replaceAll(" ", "");
                        if(String.valueOf(s.charAt(1)).equals(Integer.toString(i)) && String.valueOf(s.charAt(3)).equals(Integer.toString(j)) && s.charAt(5)=='V'){
                            nbRadar++;
                        }
			
                        z++;
                    }
				
                    z=1;
                    while(z<sheet.getColumns() &&(sheet.getCell(z,2*n+14).getType())!=CellType.EMPTY){			
                        String s = sheet.getCell(z,2*n+14).getContents();
                        s = s.replaceAll(" ", "");
                        if(String.valueOf(s.charAt(1)).equals(Integer.toString(i)) && String.valueOf(s.charAt(3)).equals(Integer.toString(j)) && s.charAt(5)=='V'){
                            nbRadar++;		
                        }
				
                        z++;
                    }
				
                    verticalFixedRadars[i][j] = nbRadar;				
                }	
            }
                
            return true;
        }
        
        catch (java.lang.NegativeArraySizeException e){
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame,
                "Vous devez entrer des nombres entiers positifs pour abscisse ou ordonnée",
                "Mauvaises données d'entrée",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        catch (java.lang.NumberFormatException e){
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame,
                "Vous devez entrer des nombres entiers positifs pour abscisse ou ordonnée",
                "Mauvaises données d'entrée",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        catch (MnZeroException e){
            return false;
        }
        
        catch(Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String msg = sw.toString();
            msg = msg.substring(0, 500) + " ...";
            
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame,
                msg,
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
	
    public void setA(double a) {
        this.a = a;
    }

    public void setB(double b) {
        this.b = b;
    }

    public void setDist(double dist) {
        this.dist = dist;
    }

    public void setFuel(double fuel) {
        this.fuel = fuel;
    }

    public void setM(int m) {
        this.m = m;
    }

    public void setN(int n) {
        this.n = n;
    }
    
    public void setOptSpeed(double opt){
        this.optSpeed = opt;
    }
    
    public void setOptSpeed(){
        optSpeed = Math.sqrt((alpha*dist)/(fuel*a));
    }

    public void setOptCostHorizontal(double[][] optCostHorizontal) {
        this.optCostHorizontal = optCostHorizontal;
    }
    
    public void setOptCostHorizontal(){
        
        optCostHorizontal = new double[m][n+1];
        double c1 [][] = new double [m][n+1];
        double c2 [][] = new double [m][n+1];
        
        for (int i=0; i<m; i++)
            for (int j=0; j<=n; j++){
                if(speedHorizontal[i][j]>=optSpeed){
                    c1[i][j] = (alpha*dist)/optSpeed;
                    c2[i][j] = fuel*(a*optSpeed + b);
                    if(speedHorizontal[i][j]==130)
                        optCostHorizontal[i][j] = c1[i][j] + c2[i][j] + toll;
                    else
                        optCostHorizontal[i][j] = c1[i][j] + c2[i][j];
                }
                else{
                    c1[i][j] = (alpha*dist)/speedHorizontal[i][j];
                    c2[i][j] = fuel*(a*speedHorizontal[i][j] + b);
                    if(speedHorizontal[i][j]==130)
                    	optCostHorizontal[i][j] = c1[i][j] + c2[i][j] + toll;
                    else
                    	optCostHorizontal[i][j] = c1[i][j] + c2[i][j];
                }
            }
    }

    public void setOptCostVertical(double[][] optCostVertical) {
        this.optCostVertical = optCostVertical;
    }
    
    public void setOptCostVertical(){
        optCostVertical = new double[m+1][n];
        double c1 [][] = new double [m+1][n];
        double c2 [][] = new double [m+1][n];
        
        for (int i=0; i<=m; i++)
            for (int j=0; j<n; j++){
                if(speedVertical[i][j]>=optSpeed){
                    c1[i][j] = (alpha*dist)/optSpeed;
                    c2[i][j] = fuel*(a*optSpeed + b);
                    if(speedVertical[i][j]==130)
                        optCostVertical[i][j] = c1[i][j] + c2[i][j] + toll;
                    else
                        optCostVertical[i][j] = c1[i][j] + c2[i][j];
                }
                else{
                    c1[i][j] = (alpha*dist)/speedVertical[i][j];
                    c2[i][j] = fuel*(a*speedVertical[i][j] + b);
                    if(speedVertical[i][j]==130)
                    	optCostVertical[i][j] = c1[i][j] + c2[i][j] + toll;
                    else
                    	optCostVertical[i][j] = c1[i][j] + c2[i][j];
                }
            }
    }
    
    public void setOptTimeHorizontal(double time[][]){
    	this.optTimeHorizontal = time;
    }
    
    public void setOptTimeHorizontal(){
    	for (int i=0; i<m; i++)
            for (int j=0; j<=n; j++)
            	optTimeHorizontal[i][j] = dist/speedHorizontal[i][j];
    }
    
    public void setOptTimeVertical(double time[][]){
    	this.optTimeVertical = time;
    }
    
    public void setOptTimeVertical(){
    	for (int i=0; i<=m; i++)
            for (int j=0; j<n; j++)
            	optTimeVertical[i][j] = dist/speedVertical[i][j];
    }

    public void setSpeedHorizontal(int[][] speedHorizontal) {
        this.speedHorizontal = speedHorizontal;
    }
    
    public void setSpeedHorizontal(int i, int j, int value){
        this.speedHorizontal[i][j] = value;
    }

    public void setSpeedVertical(int[][] speedVertical) {
        this.speedVertical = speedVertical;
    }
    
    public void setSpeedVertical(int i, int j, int value){
        this.speedVertical[i][j] = value;
    }

    public void setToll(double toll) {
        this.toll = toll;
    }
    
    public void setAlpha(double alpha){
        this.alpha = alpha;
    }
    
    public void setNum(){
    	for (int i=0; i<=m; i++)
    		for (int j=0; j<=n; j++)
    			num[i][j] = j*(m+1) + i;
    }
    
    public void setOptimalCost(int x, int y){
    	//x, y - coordonees de la point de debut (dijkstra)
    	final int MAX_VALUE = Integer.MAX_VALUE; // infinite
    	final int NO_PARENT = -1; // une constant qui dire que la point n'a pas des parents
    	
    	int s = y*(m+1) + x; // la point de debut
    	int numSize = num[m][n]; // la taille de la matrice num (le dernier element)
    	
    	System.out.print("\nm = " + m);
    	System.out.print("\nn = " + n + "\n");
    	    	
    	for (int i=0; i<=m; i++){
    		for(int j=0; j<=n; j++)
    			System.out.format("%5d", num[i][j]);
    		System.out.print("\n");
    	}
    	
    	boolean [] T = new boolean [numSize+1]; // flag
    	optDistCost = new double [numSize+1];  // distance
    	optPredCost = new int [numSize+1]; // precedants
    	
    	for (int i=0; i<=numSize; i++){
    		optDistCost[i] = MAX_VALUE;
    		optPredCost[i] = NO_PARENT;
    	}
    		
    	//initialization pour le voisin de droite (s'il existe)
    	if((s % (m+1)) != m){
    		optDistCost[s+1] = optCostHorizontal[x][y];
    		optPredCost[s+1] = s;
    	}
    	
    	//initialization pour le voisin de gauche (s'il existe)
    	if((s-1) >= 0){
    		optDistCost[s-1] = optCostHorizontal[x-1][y];
    		optPredCost[s-1] = s;
    	}
    	
    	//initialisation pour le voisin de haut (s'il existe)
    	if((s+m+1)/(m+1) <= n){
    		optDistCost[s+m+1] = optCostVertical[x][y];
    		optPredCost[s+m+1] = s;
    	}
    	
    	//initialisation pour le voisin de bas (s'il existe)
    	if((s-(m+1)) >= 0){
    		optDistCost[s-(m+1)] = optCostVertical[x][y-1];
    		optPredCost[s-(m+1)] = s;
    	}
    	
		System.out.print("\n");
		for (int i=0; i<optDistCost.length; i++){
			System.out.print(" " + optDistCost[i] + " ");
			if(i%m==0)
				System.out.print("\n");
		}
		
    	//initialisation de flag
    	System.out.println("\ns=" + s);
    	for (int i=0; i<T.length; i++)
			T[i] = true;
		T[s] = false;
		optPredCost[s] = NO_PARENT;
		
		System.out.print("\npred = " );
		for(int i=0; i<optPredCost.length; i++)
			System.out.print(" " + optPredCost[i] + " ");
    	
		System.out.print("\nT = ");
		for (int i=0; i<T.length; i++)
			if(T[i] == true)
				System.out.print(" 1 ");
			else
				System.out.print(" 0 ");
		
		while(true){
			int j = NO_PARENT;
			double di = MAX_VALUE;
			
			for(int i=0; i<T.length; i++)
				if(T[i] && (optDistCost[i] < di)){
					di = optDistCost[i];
					j = i;
				}
			
			if(NO_PARENT == j) break;
			T[j] = false;
			
			int xj = -1;
			int yj = -1;
			
			for (int i=0; i<=m; i++)
				for (int k= 0; k<=n; k++)
					if (num[i][k] == j){
						xj = i;
						yj = k;
					}
			
			int i;
			
			//pour le voisin de droite
			if((xj+1) <= m){
				i = num[xj+1][yj];
				if(T[i]){
					if(optDistCost[i] > optDistCost[j] + optCostHorizontal[xj][yj]){
						optDistCost[i] = optDistCost[j] +optCostHorizontal[xj][yj];
						optPredCost[i] = j;
					}
				}
			}
			
			//pour le voisin de gauche
			if((xj-1) > -1){
				i = num[xj-1][yj];
				if(T[i]){
					if(optDistCost[i] > optDistCost[j] + optCostHorizontal[xj-1][yj]){
						optDistCost[i] = optDistCost[j] +optCostHorizontal[xj-1][yj];
						optPredCost[i] = j;
					}
				}
			}
			
			//pour le voisin de haut
			if((yj+1) <= n){
				i = num[xj][yj+1];
				if(T[i]){
					if(optDistCost[i] > optDistCost[j] + optCostVertical[xj][yj]){
						optDistCost[i] = optDistCost[j] +optCostVertical[xj][yj];
						optPredCost[i] = j;
					}
				}
			}
			
			//pour le voisin de bas
			if((yj-1) > -1){
				i = num[xj][yj-1];
				if(T[i]){
					if(optDistCost[i] > optDistCost[j] + optCostVertical[xj][yj-1]){
						optDistCost[i] = optDistCost[j] +optCostVertical[xj][yj-1];
						optPredCost[i] = j;
					}
				}
			}
		}
    }
    
    public void setOptimalTime(int x, int y){
    	//x, y - coordonees de la point de debut
    	final int MAX_VALUE = Integer.MAX_VALUE; // infinite
    	final int NO_PARENT = -1; // une constant qui dire que la point n'a pas des parents
    	
    	int s = y*(m+1) + x; // la point de debut
    	int numSize = num[m][n]; // la taille de la matrice num (le dernier element)
    	
    	System.out.print("\nm = " + m);
    	System.out.print("\nn = " + n + "\n");
    	    	
    	for (int i=0; i<=m; i++){
    		for(int j=0; j<=n; j++)
    			System.out.format("%5d", num[i][j]);
    		System.out.print("\n");
    	}
    	
    	boolean [] T = new boolean [numSize+1]; // flag
    	optTime = new double [numSize+1];  // distance
    	optPredTime = new int [numSize+1]; // precedants
    	
    	for (int i=0; i<=numSize; i++){
    		optTime[i] = MAX_VALUE;
    		optPredTime[i] = NO_PARENT;
    	}
    		
    	//initialization pour le voisin de droite (s'il existe)
    	if((s % (m+1)) != m){
    		optTime[s+1] = optTimeHorizontal[x][y];
    		optPredTime[s+1] = s;
    	}
    	
    	//initialization pour le voisin de gauche (s'il existe)
    	if((s-1) >= 0){
    		optTime[s-1] = optTimeHorizontal[x-1][y];
    		optPredTime[s-1] = s;
    	}
    	
    	//initialisation pour le voisin de haut (s'il existe)
    	if((s+m+1)/(m+1) <= n){
    		optTime[s+m+1] = optTimeVertical[x][y];
    		optPredTime[s+m+1] = s;
    	}
    	
    	//initialisation pour le voisin de bas (s'il existe)
    	if((s-(m+1)) >= 0){
    		optTime[s-(m+1)] = optTimeVertical[x][y-1];
    		optPredTime[s-(m+1)] = s;
    	}
    	
		System.out.print("\n");
		for (int i=0; i<optTime.length; i++){
			System.out.print(" " + optTime[i] + " ");
			if(i%m==0)
				System.out.print("\n");
		}
		
    	//initialisation de flag
    	System.out.println("\ns=" + s);
    	for (int i=0; i<T.length; i++)
			T[i] = true;
		T[s] = false;
		optTime[s] = NO_PARENT;
		
		System.out.print("\npred = " );
		for(int i=0; i<optTime.length; i++)
			System.out.print(" " + optTime[i] + " ");
    	
		System.out.print("\nT = ");
		for (int i=0; i<T.length; i++)
			if(T[i] == true)
				System.out.print(" 1 ");
			else
				System.out.print(" 0 ");
				
		while(true){
			int j = NO_PARENT;
			double di = MAX_VALUE;
			
			for(int i=0; i<T.length; i++)
				if(T[i] && (optTime[i] < di)){
					di = optTime[i];
					j = i;
				}
			
			if(NO_PARENT == j) break;
			T[j] = false;
			
			int xj = -1;
			int yj = -1;
			
			for (int i=0; i<=m; i++)
				for (int k= 0; k<=n; k++)
					if (num[i][k] == j){
						xj = i;
						yj = k;
					}
			
			int i;
			
			//pour le voisin de droite
			if((xj+1) <= m){
				i = num[xj+1][yj];
				if(T[i]){
					if(optTime[i] > optTime[j] + optTimeHorizontal[xj][yj]){
						optTime[i] = optTime[j] +optTimeHorizontal[xj][yj];
						optPredTime[i] = j;
					}
				}
			}
			
			//pour le voisin de gauche
			if((xj-1) > -1){
				i = num[xj-1][yj];
				if(T[i]){
					if(optTime[i] > optTime[j] + optTimeHorizontal[xj-1][yj]){
						optTime[i] = optTime[j] +optTimeHorizontal[xj-1][yj];
						optPredTime[i] = j;
					}
				}
			}
			
			//pour le voisin de haut
			if((yj+1) <= n){
				i = num[xj][yj+1];
				if(T[i]){
					if(optTime[i] > optTime[j] + optTimeVertical[xj][yj]){
						optTime[i] = optTime[j] +optTimeVertical[xj][yj];
						optPredTime[i] = j;
					}
				}
			}
			
			//pour le voisin de bas
			if((yj-1) > -1){
				i = num[xj][yj-1];
				if(T[i]){
					if(optTime[i] > optTime[j] + optTimeVertical[xj][yj-1]){
						optTime[i] = optTime[j] +optTimeVertical[xj][yj-1];
						optPredTime[i] = j;
					}
				}
			}
		}
    }
    
    private void setOptimalCostPath(int s, int j){
		if(optPredCost[j] != s) setOptimalCostPath(s, optPredCost[j]);
		predList.add(j);
	}
    
    private void setOptimalTimePath(int s, int j){
    	if(optPredTime[j] != s) setOptimalTimePath(s, optPredTime[j]);
		predList.add(j);
    }
    
    private void setPredList(){
    	predList = new ArrayList<Integer>();
    }
    
    public void setFixedRadarNumber(int radar){
    	this.fixedRadarNumber = radar;
    }
    
    public void setMobileRadarNumber(int radar){
    	this.mobileRadarNumber = radar;
    }
    
    public void setExpectation(){
    	this.expectation = (double) this.mobileRadarNumber / ((m+1)*n + m*(n+1));
    }
    
    public void setNoHorizontalFixedRadars(){
    	for(int i=0; i<m; i++)
    		for(int j=0; j<=n; j++)
    			horizontalFixedRadars[i][j] = 0;
    }
    
    public void setHorizontalFixedRadars(int radars[][]){
    	this.horizontalFixedRadars = radars;
    }
    
    public void setHorizontalFixedRadar(int i, int j, int radar){
    	this.horizontalFixedRadars[i][j] = radar;
    }
    
    public void setNoVerticalFixedRadars(){
    	for(int i=0; i<=m; i++)
    		for(int j=0; j<n; j++)
    			verticalFixedRadars[i][j] = 0;
    }
    
    public void setVerticalFixedRadars(int radars[][]){
    	this.verticalFixedRadars = radars;
    }
    
    public void setVerticalFixedRadar(int i, int j, int radar){
    	this.verticalFixedRadars[i][j] = radar;
    }
    
    public void setEpsilon(double e){
    	this.epsilon = e;
    }
    
    public void setHorizontalV(){
    	for (int i=0; i<m; i++){
    		for (int j=0; j<=n; j++){
    			this.horizontalV[i][j][0] = this.speedHorizontal[i][j] * 1.1;
    			this.horizontalV[i][j][1] = this.speedHorizontal[i][j] * 1.5;
    			this.horizontalV[i][j][2] = this.speedHorizontal[i][j] * 2; // this.speedHorizontal[i][j] * 2
    			this.horizontalV[i][j][3] = this.optSpeed;
    		}
    	}
    }
    
    public void setHorizontalC(){
    	for (int i=0; i<m; i++){
    		for (int j=0; j<=n; j++){
    			//setting c1
    			this.horizontalC[i][j][0] = (horizontalFixedRadars[i][j] + this.expectation)*0 + (alpha*(dist/horizontalV[i][j][0]) + fuel*(a*horizontalV[i][j][0]+b));
    			
    			//setting c2
    			this.horizontalC[i][j][1] = (horizontalFixedRadars[i][j] + this.expectation)*70 + (alpha*(dist/horizontalV[i][j][1]) + fuel*(a*horizontalV[i][j][1]+b));
    	
    			//setting c3
    			this.horizontalC[i][j][2] = (horizontalFixedRadars[i][j] + this.expectation)*180 + (alpha*(dist/horizontalV[i][j][2]) + fuel*(a*horizontalV[i][j][2]+b));
    		
    			//c4
    			if (this.horizontalV[i][j][3] > this.speedHorizontal[i][j] * 1.5)
    				this.horizontalC[i][j][3] = (horizontalFixedRadars[i][j] + this.expectation) * 180 + (alpha*(dist/horizontalV[i][j][3]) + fuel*(a*horizontalV[i][j][3]+b));
    			else 
    				if (this.horizontalV[i][j][3] > 1.1 * this.speedHorizontal[i][j])
    					this.horizontalC[i][j][3] = (horizontalFixedRadars[i][j] + this.expectation)*70 + (alpha*(dist/horizontalV[i][j][3]) + fuel*(a*horizontalV[i][j][3]+b));
    				else
    					this.horizontalC[i][j][3] = (alpha*(dist/horizontalV[i][j][3]) + fuel*(a*horizontalV[i][j][3]+b));
    		}
    	}
    }
    
    public void setHorizontalT(){
    	for (int i=0; i<m; i++){
    		for (int j=0; j<=n; j++){
    			for (int k=0; k<4; k++){
    				this.horizontalT[i][j][k] = dist/horizontalV[i][j][k];	
    			}
    		}	
    	}
    }
    
    public void setVerticalV(){
    	for(int i=0; i<=m; i++){
    		for(int j=0; j<n; j++){
    			this.verticalV[i][j][0] = this.speedVertical[i][j] * 1.1;
    			this.verticalV[i][j][1] = this.speedVertical[i][j] * 1.5;
    			this.verticalV[i][j][2] = this.speedVertical[i][j] * 2;
    			this.verticalV[i][j][3] = this.optSpeed;
    		}
    	}
    }
    
    public void setVerticalC(){
    	for (int i=0; i<=m; i++){
    		for (int j=0; j<n; j++){
    			
    			//setting c1
    			this.verticalC[i][j][0] = (this.verticalFixedRadars[i][j] + this.expectation)*0 + (alpha*(dist/verticalV[i][j][0]) + fuel*(a*verticalV[i][j][0]+b));
    			
    			//setting c2
    			this.verticalC[i][j][1] = (this.verticalFixedRadars[i][j] + this.expectation)*70 + (alpha*(dist/verticalV[i][j][1]) + fuel*(a*verticalV[i][j][1]+b));
    				
    			//setting c3
    			this.verticalC[i][j][2] = (this.verticalFixedRadars[i][j] + this.expectation)*180 + (alpha*(dist/verticalV[i][j][2]) + fuel*(a*verticalV[i][j][2]+b));
    		
    			//c4
    			if (this.verticalV[i][j][3] > this.speedVertical[i][j] * 1.5)
    				this.verticalC[i][j][3] = (verticalFixedRadars[i][j] + this.expectation) * 180 + (alpha*(dist/verticalV[i][j][3]) + fuel*(a*verticalV[i][j][3]+b));
    			else 
    				if (this.verticalV[i][j][3] > 1.1 * this.speedVertical[i][j])
    					this.verticalC[i][j][3] = (verticalFixedRadars[i][j] + this.expectation)*70 + (alpha*(dist/verticalV[i][j][3]) + fuel*(a*verticalV[i][j][3]+b));
    				else
    					this.verticalC[i][j][3] = (alpha*(dist/verticalV[i][j][3]) + fuel*(a*verticalV[i][j][3]+b));
    		}
    	}
    }
    
    public void setVerticalT(){
    	for (int i=0; i<=m; i++){
    		for (int j=0; j<n; j++){
    			for (int k=0; k<4; k++){
    				this.verticalT[i][j][k] = dist/verticalV[i][j][k];	
    			}
    		}	
    	}
    }
    
    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getDist() {
        return dist;
    }

    public double getFuel() {
        return fuel;
    }

    public int getM() {
        return m;
    }

    public int getN() {
        return n;
    }

    public double[][] getOptCostHorizontal() {
        return optCostHorizontal;
    }
    
    public double getOptCostHorizontal(int i, int j){
        return optCostHorizontal[i][j];
    }

    public double[][] getOptCostVertical() {
        return optCostVertical;
    }
    
    public double getOptCostVertical(int i, int j){
    	return optCostVertical[i][j];
    }

    public int[][] getSpeedHorizontal() {
        return speedHorizontal;
    }
    
    public int getSpeedHorizontal(int i, int j){
        return speedHorizontal[i][j];
    }

    public int[][] getSpeedVertical() {
        return speedVertical;
    }
    
    public int getSpeedVertical(int i, int j){
        return speedVertical[i][j];
    }
    
    public double[][] getOptTimeHorizontal(){
    	return optTimeHorizontal;
    }
    
    public double getOptTimeHorizontal(int i, int j){
    	return optTimeHorizontal[i][j];
    }
    
    public double[][] getOptTimeVertical(){
    	return optTimeVertical;
    }
    
    public double getOptTimeVertical(int i, int j){
    	return optTimeVertical[i][j];
    }

    public double getToll() {
        return toll;
    }
    
    public double getAlpha(){
        return alpha;
    }
    
    public double getOptSpeed(){
        return optSpeed;
    }
    
    public int[][] getNum(){
    	return num;
    }
    
    public int getNum(int i, int j){
    	return num[i][j];
    }
    
    public double getOptDistCost(int i){
    	return optDistCost[i];
    }
    
    public double [] getOptDistCost(){
    	return optDistCost;
    }
    
    public int getOptPredCost(int i){
    	return optPredCost[i];
    }
    
    public int[] getOptPredCost(){
    	return optPredCost;
    }
    
    public double [] getOptTime(){
    	return optTime;
    }
    
    public double getOptTime(int i){
    	return optTime[i];
    }
    
    public int [] getOptPredTime(){
    	return optPredTime;
    }
    
    public int getFixedRadarNumber(){
    	return fixedRadarNumber;
    }
    
    public int getMobileRadarNumber(){
    	return mobileRadarNumber;
    }
    
    public int getOptPredTime(int i){
    	return optPredTime[i];
    }
    
    public int [][] getHorizontalFixedRadars(){
    	return this.horizontalFixedRadars;
    }
    
    public int getHorizontalFixedRadar(int i, int j){
    	return this.horizontalFixedRadars[i][j];
    }
    
    public int [][] getVerticalFixedRadars(){
    	return this.verticalFixedRadars;
    }
    
    public int getVerticalFixedRadar(int i, int j){
    	return this.verticalFixedRadars[i][j];
    }
    
    public double getEpsilon(){
    	return this.epsilon;
    }
    
    public double[][][] getHorizontalV(){
    	return this.horizontalV;
    }
    
    public double getHorizontalV1(int i, int j){
    	return this.horizontalV[i][j][0];
    }
    
    public double getHorizontalV2(int i, int j){
    	return this.horizontalV[i][j][1];
    }
    
    public double getHorizontalV3(int i, int j){
    	return this.horizontalV[i][j][2];
    }
    
    public double getHorizontalV(int i, int j, int k){
    	return this.horizontalV[i][j][k];
    }
    
    public double[][][] getHorizontalC(){
    	return this.horizontalC;
    }
    
    public double getHorizontalC1(int i, int j){
    	return this.horizontalC[i][j][0];
    }
    
    public double getHorizontalC2(int i, int j){
    	return this.horizontalC[i][j][1];
    }
    
    public double getHorizontalC3(int i, int j){
    	return this.horizontalC[i][j][2];
    }
    
    public double getHorizontalC(int i, int j, int k){
    	return this.horizontalC[i][j][k];
    }
    
    public double[][][] getHorizontalT(){
    	return this.horizontalT;
    }
    
    public double getHorizontalT1(int i, int j){
    	return this.horizontalT[i][j][0];
    }
    
    public double getHorizontalT2(int i, int j){
    	return this.horizontalT[i][j][1];
    }
    
    public double getHorizontalT3(int i, int j){
    	return this.horizontalT[i][j][2];
    }
    
    public double getHorizontalT(int i, int j, int k){
    	return this.horizontalT[i][j][k];
    }
    
    public double [][][] getVerticalC(){
    	return this.verticalC;
    }
    
    public double getVerticalC1(int i, int j){
    	return this.verticalC[i][j][0];
    }
    
    public double getVerticalC2(int i, int j){
    	return this.verticalC[i][j][1];
    }
    
    public double getVerticalC3(int i, int j){
    	return this.verticalC[i][j][2];
    }
    
    public double getVerticalC(int i, int j, int k){
    	return this.verticalC[i][j][k];
    }
    
    public double getVerticalT1(int i, int j){
    	return this.verticalT[i][j][0];
    }
    
    public double getVerticalT2(int i, int j){
    	return this.verticalT[i][j][1];
    }
    
    public double getVerticalT3(int i, int j){
    	return this.verticalT[i][j][2];
    }
    
    public double getVerticalT(int i, int j, int k){
    	return this.verticalT[i][j][k];
    }
    
    public double [][][] getVerticalV(){
    	return this.verticalV;
    }
    
    public double getVerticalV1(int i, int j){
    	return this.verticalV[i][j][0];
    }
    
    public double getVerticalV2(int i, int j){
    	return this.verticalV[i][j][1];
    }
    
    public double getVerticalV3(int i, int j){
    	return this.verticalV[i][j][2];
    }
    
    public double getVerticalV(int i, int j, int k){
    	return this.verticalV[i][j][k];
    }
    
    public List<Integer> getOptimalCostPath(int s, int j){
    	this.setPredList();
    	predList.add(s);
    	this.setOptimalCostPath(s, j);
    	return predList;
    }
    
    public List<Integer> getOptimalTimePath(int s, int j){
    	this.setPredList();
    	predList.add(s);
    	this.setOptimalTimePath(s, j);
    	return predList;
    }
    
    public List<Segment> getOptimalCostPathSegments(int s, int j){
    	List<Segment> segmentList = new ArrayList<Segment>();
    	List<Integer> optPath = this.getOptimalCostPath(s, j);  
    	
    	for(int i=0; i<optPath.size()-1; i++){
    		int xi = -1;
    		int yi = -1;
    		
    		for (int l=0; l<=m; l++)
    			for (int k= 0; k<=n; k++)
    				if (num[l][k] == optPath.get(i)){
    					xi = l;
    					yi = k;
    					break;
    				}
    		
    		if((optPath.get(i)+1) == (optPath.get(i + 1))){
    			Segment segment = new Segment(xi, yi,'H');
    			segment.setCost(this.optCostHorizontal[xi][yi]);
    			
    			double speed;
    			if(this.speedHorizontal[xi][yi] >= this.optSpeed)
    				speed = this.optSpeed;
    			else
    				speed = speedHorizontal[xi][yi];
    			segment.setSpeed(speed);
    			
    			segment.setTime(this.dist/speed);
    			segmentList.add(segment);
    		}
    		
    		else{
    			Segment segment = new Segment(xi, yi,'V');
    			segment.setCost(this.optCostVertical[xi][yi]);
    			
    			double speed;
    			if(this.speedVertical[xi][yi] >= this.optSpeed)
    				speed = this.optSpeed;
    			else
    				speed = speedVertical[xi][yi];
    			segment.setSpeed(speed);
    			
    			segment.setTime(this.dist/speed);
    			segmentList.add(segment);
    		}	
    	}
    	
    	return segmentList;
    }
    
    public List<Segment> getOptimalTimePathSegments(int s, int j){
    	List<Segment> segmentList = new ArrayList<Segment>();
    	List<Integer> optPath = this.getOptimalTimePath(s, j);  
    	
    	for(int i=0; i<optPath.size()-1; i++){
    		int xi = -1;
    		int yi = -1;
    		
    		for (int l=0; l<=m; l++)
    			for (int k= 0; k<=n; k++)
    				if (num[l][k] == optPath.get(i)){
    					xi = l;
    					yi = k;
    					break;
    				}
    		
    		if((optPath.get(i)+1) == (optPath.get(i + 1))){
    			Segment segment = new Segment(xi, yi,'H');
    			segment.setTime(this.optTimeHorizontal[xi][yi]);
    			double cost = this.alpha*(this.dist/this.speedHorizontal[xi][yi]) + this.fuel*(this.a*this.speedHorizontal[xi][yi]+this.b);
    			segment.setCost(cost);
    			segment.setSpeed(this.speedHorizontal[xi][yi]);
    			segmentList.add(segment);
    		}
    		
    		else{
    			Segment segment = new Segment(xi, yi,'V');
    			segment.setTime(this.optTimeVertical[xi][yi]);
    			double cost = this.alpha*(this.dist/this.speedVertical[xi][yi]) + this.fuel*(this.a*this.speedVertical[xi][yi]+this.b);
    			segment.setCost(cost);
    			segment.setSpeed(this.speedVertical[xi][yi]);
    			segmentList.add(segment);
    		}	
    	}
    	
    	for (int i=0; i<segmentList.size(); i++){
    		if (segmentList.get(i).getSpeed()==130){
    			double cost = segmentList.get(i).getCost() + this.toll;
    			segmentList.get(i).setCost(cost);
    		}
    	}
    	
    	return segmentList;
    }
    
    public List<Segment> getProbTime(double T0, double coef){
    	LinkedList<Label> labelList = new LinkedList<Label>();
    	
		@SuppressWarnings("unchecked")
		LinkedList<Label> matrix [][] = new LinkedList[m+1][n+1];
    	for(int i=0; i<=m; i++)
    		for(int j=0; j<=n; j++)
    			matrix[i][j] = new LinkedList<Label>();
    	
    	Label label = new Label();
    	
    	labelList.add(label);
    	matrix[0][0].add(label);
    	
    	while(!labelList.isEmpty()){
    		label = labelList.getFirst();
    		System.out.print("\n" + label.getX() + " " + label.getY() + " " + label.getCost() + " " + label.getTime() + "\n");
    		
    		for(int i=0; i<=m; i++){
    			for(int j=0; j<=n; j++){
    				System.out.print(matrix[i][j].size() + "  ");
    			}
    			System.out.print("\n");
    		}
    		
    		int x = label.getX();
    		int y = label.getY();
    		double cost = label.getCost();
    		double time = label.getTime();
    		
    		if((x+1) <= m){
    			for (int i=0; i<4; i++){
    	    		double tollCost = 0;
    				if(this.getSpeedHorizontal(x, y) == 130)
    					tollCost = this.toll;
    	    		
    				Label newLabel = new Label(x+1, y, cost + tollCost + this.getHorizontalC(x, y, i), time + this.getHorizontalT(x, y, i), this.getHorizontalV(x, y, i),  label);
    	    		//newLabel.setSpeed(this.getHorizontalV(x, y, i));
        			if(!isDominated(newLabel, x+1, y, matrix)){
        				labelList.add(newLabel);
        				matrix[x+1][y].add(newLabel);
        			}
        			
    			}
   			}
    		
    		
    		if ( ((x-1) >= 0) && ((x+1)<=m) ){
    			for (int i=0; i<4; i++){
    				double tollCost = 0;
    				if(this.getSpeedHorizontal(x-1, y) == 130)
    					tollCost = this.toll;
    				
    				Label newLabel = new Label(x-1, y, cost + tollCost + this.getHorizontalC(x-1, y, i), time + this.getHorizontalT(x-1, y, i), this.getHorizontalV(x, y, i), label);
    	    		//newLabel.setSpeed(this.getHorizontalV(x, y, i));
    				if(!isDominated(newLabel, x-1, y, matrix)){
    					labelList.add(newLabel);
    					matrix[x-1][y].add(newLabel);
    				}
    			}
    		}
    		
    		if((y+1) <= n){
    			for(int i=0; i<4; i++){
    				double tollCost = 0;
    				if(this.getSpeedVertical(x, y) == 130)
    					tollCost = this.toll;
    				
    				Label newLabel = new Label(x, y+1, cost + tollCost + this.getVerticalC(x, y, i), time + this.getVerticalT(x, y, i), this.getVerticalV(x, y, i), label);
    	    		//newLabel.setSpeed(this.getVerticalV(x, y, i));
    				if(!isDominated(newLabel, x, y+1, matrix)){
    					labelList.add(newLabel);
    					matrix[x][y+1].add(newLabel);
    				}
    			}
    		}
    		
    		
    		if( ((y-1) >= 0) && ((y+1) <= n)){
    			for(int i=0; i<4; i++){
    				double tollCost = 0;
    				if(this.getSpeedVertical(x, y-1) == 130)
    					tollCost = this.toll;
    				
    				Label newLabel = new Label(x, y-1, cost + tollCost + this.getVerticalC(x, y-1, i), time + this.getVerticalT(x, y-1, i), this.getVerticalV(x, y, i), label);
    				//newLabel.setSpeed(this.getVerticalV(x, y, i));
    				if(!isDominated(newLabel, x, y-1, matrix)){
    					labelList.add(newLabel);
    					matrix[x][y-1].add(newLabel);
    				}
    			}
    		}
    		
   			labelList.removeFirst();
   			//System.out.print("\nEXIT");
    	}
    
    	System.out.println("\n\nTEST ");
    	
    	System.out.println("\nSIZE: " + matrix[m][n].size());
    	System.out.println("\nFIRST: " + matrix[m][n].getFirst().getX() + " " + matrix[m][n].getFirst().getY() + " " +
    			matrix[m][n].getFirst().getCost() + " " + matrix[m][n].getFirst().getTime());
    	
    	int counter = 0;
    	this.minProbCost = Double.MAX_VALUE;
    	this.minProbTime = 0;
    	Label minLabel = new Label();
    	for (int i=0; i<matrix[m][n].size(); i++){
    		if (matrix[m][n].get(i).getTime() <= (T0*coef)){ // change the constant!!!
    			counter++;
    			if (matrix[m][n].get(i).getCost() < this.minProbCost){
    				this.minProbCost = matrix[m][n].get(i).getCost();
    				minLabel = matrix[m][n].get(i);
    				this.minProbTime = matrix[m][n].get(i).getTime();
    			}
    		}
    	}
    	
    	List<Label> minLabelList = new ArrayList<Label>();
    	
    	System.out.println("\n COUNTER: " + counter);
    	System.out.println("\n MIN COST: " + this.minProbCost);
    	System.out.println("\n MIN TIME: " + this.minProbTime);
    	System.out.println("\n MIN LABEL: ");
    	
    	counter = 0;
    	for (int i=0; i<matrix[m][n].size(); i++){
    		if(matrix[m][n].get(i).getTime() == this.minProbTime)
    			counter ++;
    	}
    	
    	System.out.println("\nNUMBER OF MINIMAL PATHS: " + counter);
    	
    	do{
    		minLabelList.add(minLabel);
    		System.out.print("(" + minLabel.getX() + ", " + minLabel.getY() + "); ");
    		minLabel = minLabel.getPred();
    	} while (minLabel != null);	
    	
    	List<Segment> minLabelSegment = new ArrayList<Segment>();
    	System.out.println("\n LABEL LIST: ");
    	
    	for(int i=minLabelList.size()-1; i>=1; i--){
    		System.out.print("(" + minLabelList.get(i).getX() + ", " + minLabelList.get(i).getY() + "); ");
    		
    		if((minLabelList.get(i).getX()+1) == (minLabelList.get(i-1).getX())){
    			Segment segment = new Segment(minLabelList.get(i).getX(), minLabelList.get(i).getY(), 'H');
    			segment.setCost(minLabelList.get(i-1).getCost());
    			segment.setTime(minLabelList.get(i-1).getTime());
    			segment.setSpeed(minLabelList.get(i-1).getSpeed());
    			minLabelSegment.add(segment);
    		}
    		
    		else{
    			Segment segment = new Segment(minLabelList.get(i).getX(), minLabelList.get(i).getY(), 'V');
    			segment.setCost(minLabelList.get(i-1).getCost());
    			segment.setTime(minLabelList.get(i-1).getTime());
    			segment.setSpeed(minLabelList.get(i-1).getSpeed());
    			minLabelSegment.add(segment);
    		}
    	}
    	
    	for (int i=minLabelSegment.size()-1; i>=1; i--){
    		minLabelSegment.get(i).setCost(minLabelSegment.get(i).getCost() - minLabelSegment.get(i-1).getCost());
    		minLabelSegment.get(i).setTime(minLabelSegment.get(i).getTime() - minLabelSegment.get(i-1).getTime());
    	}
    	
    	System.out.println("\nSEGMENT LIST: ");
    	for(int i=0; i<minLabelSegment.size(); i++){
    		System.out.print("(" + minLabelSegment.get(i).getX() + ", " + minLabelSegment.get(i).getY() + ", " + minLabelSegment.get(i).getLetter() + ") -  "
    							 + (int) minLabelSegment.get(i).getSpeed() + " km/h    " 
    							 + minLabelSegment.get(i).getCost() + " €    " 
    							 + minLabelSegment.get(i).getTime() +" h \n" );
    	}
    	
    	return minLabelSegment;
    }
    
    public boolean isDominated(Label label, int x, int y, LinkedList<Label> matrix [][]){
    	
    	Label currentLabel = new Label();
    	
    	if(!matrix[x][y].isEmpty()){
    		Iterator<Label> l = matrix [x][y].iterator();	
    		
    		do{
    			currentLabel=l.next();
    			
    			if ((currentLabel.getCost() <= label.getCost()) && 
    					(currentLabel.getTime() <= label.getTime())){
    				return true;
    			}
    			
    			else{
    				if((label.getCost() <= currentLabel.getCost()) &&
    						(label.getTime() <= currentLabel.getTime())){
    					matrix[x][y].remove(currentLabel);
    					return false;
    				}
    			}
    			
    		} while(l.hasNext());
    	}
    	
    	return false;
    }
    
    public double getExpectation(){
    	return this.expectation;
    }
    
    public double getMinProbTime(){
        return this.minProbTime;
    }
    
    public double getMinProbCost(){
        return this.minProbCost;
    }
}