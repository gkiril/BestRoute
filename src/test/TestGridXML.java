package test;

import java.util.List;

import grid.Grid;
import grid.Segment;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestGridXML {
	static Grid grid;
	
	//initialization
	@BeforeClass
	public static void BeforeAll(){
		grid = new Grid();
		grid.initializeFromXml("XMLDonnees.xml");
		
		grid.setAlpha(5);
        grid.setFuel(1.3);
        grid.setToll(3);
        grid.setDist(100);
        
        grid.setA(0.0625);
        grid.setB(1.875);
        
        grid.setOptSpeed();
        
        grid.setOptCostHorizontal();
        grid.setOptCostVertical();
        
        grid.setOptTimeHorizontal();
		grid.setOptTimeVertical();
		
		grid.setOptTimeHorizontal();
		grid.setOptTimeVertical();
	}
	
	//printing the initialization
	@Before
	public void Before(){
		//print m and n
		System.out.println("m = " + grid.getM());
		System.out.println("n = " + grid.getN());
		
		//print optimal speed
		System.out.println("Optimal speed: " + grid.getOptSpeed());
		
		//print speed limits on the horizontal segments
		System.out.println("\n The speed limits on the horizontal segments (x/y):");
		for (int i=0; i<grid.getM(); i++){
			for (int j=0; j<=grid.getN(); j++)
				System.out.format("%5d",grid.getSpeedHorizontal(i, j));
			System.out.print("\n");
		}
		
		//printing the vertical speed limits
		System.out.print("\nSpeed limits for the vertical segments (x/y): \n");
		for (int i=0; i<=grid.getM(); i++){
			for (int j=0; j<grid.getN(); j++)
				System.out.format("%5d",grid.getSpeedVertical(i, j));
			System.out.print("\n");
		}
		System.out.print("\n");
		
		//printing the optimal horizontal costs
		System.out.print("Optimal costs for the horizontal segments (x/y): \n");
		for (int i=0; i<grid.getM(); i++){
			for (int j=0; j<=grid.getN(); j++)
				System.out.format("%12f",grid.getOptCostHorizontal(i,j));
			System.out.print("\n");
		}
		
		//printing the optimal vertical costs 
		System.out.print("\nOptimal costs for the vertical segments (x/y): \n");
		for (int i=0; i<=grid.getM(); i++){
			for (int j=0; j<grid.getN(); j++)
				System.out.format("%12f",grid.getOptCostVertical(i, j));
			System.out.print("\n");
		}
		System.out.print("\n");
		
		//printing the optimal time for horizontal segments
		System.out.print("Optimal time for the horizontal segments (x/y): \n");
		for (int i=0; i<grid.getM(); i++){
			for (int j=0; j<=grid.getN(); j++)
				System.out.format("%12f",grid.getOptTimeHorizontal(i,j));
			System.out.print("\n");
		}
				
		//printing the optimal vertical costs 
		System.out.print("\nOptimal time for the vertical segments (x/y): \n");
		for (int i=0; i<=grid.getM(); i++){
			for (int j=0; j<grid.getN(); j++)
				System.out.format("%12f",grid.getOptTimeVertical(i, j));
			System.out.print("\n");
		}
	
	}
	
	@Test
	public void Test(){
		System.out.print("\nnum\n");
		
		//calculate optimal costs
		grid.setOptimalCost(0, 0);
	
		//print the results
		double d[] = grid.getOptDistCost();
		System.out.print("\n\n DISTANCES: \n");
		for(int i=0; i<d.length; i++)
			System.out.format("%12d",i);
		System.out.print("\n");
		for(int i=0; i<d.length; i++)
			System.out.format("%12f",d[i]);
		
		int pred[] = grid.getOptPredCost();
		System.out.print("\n\n PRECEDANTS: \n");
		for(int i=0; i<pred.length; i++)
			System.out.format("%5d",i);
		System.out.print("\n");
		for(int i=0; i<pred.length; i++)
			System.out.format("%5d",pred[i]);
	}
	
	@After
	public void AfterTest(){
		System.out.print("\nnum\n");
		grid.setOptimalTime(0, 0);
		
		double d[] = grid.getOptTime();
		System.out.print("\n\n TIME: \n");
		for(int i=0; i<d.length; i++)
			System.out.format("%12d",i);
		System.out.print("\n");
		for(int i=0; i<d.length; i++)
			System.out.format("%12f",d[i]);
		
		int pred[] = grid.getOptPredTime();
		System.out.print("\n\n PRECEDANTS: \n");
		for(int i=0; i<pred.length; i++)
			System.out.format("%5d",i);
		System.out.print("\n");
		for(int i=0; i<pred.length; i++)
			System.out.format("%5d",pred[i]);
		
		//optimal cost path
		List<Integer> list = grid.getOptimalCostPath(0, 99);

		System.out.println("\n\nOPTIMAL COST PATH: ");
		for (int i=0; i<list.size(); i++)
			System.out.print(list.get(i) + "  ");
		
		//optimal time path
		list = grid.getOptimalTimePath(0,99);
		
		System.out.println("\n\nOPTIMAL TIME PATH: ");
		for (int i=0; i<list.size(); i++)
			System.out.print(list.get(i) + "  ");
		
		//get optimal segments (for cost)
		List<Segment> segmentList = grid.getOptimalCostPathSegments(0, 99);
		System.out.println("\n\nOPTIMAL COST PATH: ");
		
		//the optimal cost path and its time
		double cost = 0.0;
		double time = 0.0;
		double cost2 = 0.0;
		double time2 = 0.0;
		double speed2 = 0.0;
		
		for (int i=0; i<segmentList.size(); i++){
			System.out.print(segmentList.get(i).toString() + "  ");
			if(segmentList.get(i).getLetter() == 'H'){
				cost = cost + grid.getOptCostHorizontal(segmentList.get(i).getX(), segmentList.get(i).getY());
				cost2 = cost2 + segmentList.get(i).getCost();
				speed2 = speed2 + segmentList.get(i).getSpeed();
				time2 = time2 + segmentList.get(i).getTime();
				if (grid.getSpeedHorizontal(segmentList.get(i).getX(), segmentList.get(i).getY()) >= grid.getOptSpeed()){
					time = time + (grid.getDist() / grid.getOptSpeed());
				}
				else
					time = time + (grid.getDist() / grid.getSpeedHorizontal(segmentList.get(i).getX(), segmentList.get(i).getY()));
			}
			else{
				cost = cost + grid.getOptCostVertical(segmentList.get(i).getX(), segmentList.get(i).getY());
				cost2 = cost2 + segmentList.get(i).getCost();
				speed2 = speed2 + segmentList.get(i).getSpeed();
				time2 = time2 + segmentList.get(i).getTime();
				if (grid.getSpeedVertical(segmentList.get(i).getX(), segmentList.get(i).getY()) >= grid.getOptSpeed())
					time = time + (grid.getDist() / grid.getOptSpeed());
				else
					time = time + (grid.getDist() / grid.getSpeedVertical(segmentList.get(i).getX(), segmentList.get(i).getY()));
			}
		}
		System.out.println("\nOPTIMAL COST: " + cost + " euros");
		System.out.println("OPTIMAL COST2: " + cost2);
		System.out.println("TIME NEEDED: " + time + " h");
		System.out.println("TIME2: " + time2);
		System.out.println("SPEED: " + speed2);
		System.out.println("Avarage speed: " + (speed2/(grid.getM()+grid.getN())));
		
		//get optimal segments (for time)
		//the optimal time path and its cost
		cost = 0.0;
		time = 0.0;
	    time2 = 0.0;
		cost2 = 0.0;
		double speed = 0.0;
		
		segmentList = grid.getOptimalTimePathSegments(0, 99);
		System.out.println("\n\nOPTIMAL TIME PATH: ");
		for (int i=0; i<segmentList.size(); i++){
			System.out.print(segmentList.get(i).toString() + "  ");
			if(segmentList.get(i).getLetter() == 'H'){
				time2 = time2 + segmentList.get(i).getTime();
				time = time + grid.getOptTimeHorizontal(segmentList.get(i).getX(), segmentList.get(i).getY());
				cost = cost + (grid.getAlpha()*(grid.getDist()/grid.getSpeedHorizontal(segmentList.get(i).getX(), segmentList.get(i).getY()))) + grid.getFuel()*(grid.getA()*grid.getSpeedHorizontal(segmentList.get(i).getX(), segmentList.get(i).getY()) + grid.getB());
				cost2 = cost2 + segmentList.get(i).getCost();
				speed = speed + segmentList.get(i).getSpeed();
			}
			else{
				time2 = time2 + segmentList.get(i).getTime();
				time = time + grid.getOptTimeVertical(segmentList.get(i).getX(), segmentList.get(i).getY());
				cost = cost + (grid.getAlpha()*(grid.getDist()/grid.getSpeedVertical(segmentList.get(i).getX(), segmentList.get(i).getY()))) + grid.getFuel()*(grid.getA()*grid.getSpeedVertical(segmentList.get(i).getX(), segmentList.get(i).getY()) + grid.getB());
				cost2 = cost2 + segmentList.get(i).getCost();
				speed = speed + segmentList.get(i).getSpeed();
			}
		}
		System.out.println("\nCOST: " + cost + " euros");
		System.out.println("COST2: " + cost2);
		System.out.println("TIME NEEDED: " + time + " h");
		System.out.println("TIME2: " + time2);
		System.out.println("TOTAL SPEED: " + speed);
		System.out.println("Avarage speed: " + (speed/(grid.getM() + grid.getN())) + " km/h");
	}
	
	@AfterClass
	public static void AfterAll(){
		//print horizontal fixed radars
		System.out.print("\n\nHorizontal fixed radars: \n");
		for(int i=0; i<grid.getM(); i++){
			for(int j=0; j<=grid.getN(); j++){
				System.out.print(" " + grid.getHorizontalFixedRadar(i, j) + " ");
			}	
			System.out.print("\n");
		}
			
		//print vertical fixed radars
		System.out.print("\nVertical fixed radars: \n");
		for(int i=0; i<=grid.getM(); i++){
			for(int j=0; j<grid.getN(); j++){
				System.out.print(" " + grid.getVerticalFixedRadar(i, j) + " ");
			}
			System.out.print("\n");
		}
		
		//set and print epsilon
		grid.setEpsilon(0.001);
		System.out.println("EPSILON = " + grid.getEpsilon());
		
		//set horizontal V1, V2, V3
		grid.setHorizontalV();
		
		//print horizontal V1
		System.out.println("\n v1 (horizontal): ");
		for (int i=0; i<grid.getM(); i++){
			for (int j=0; j<=grid.getN(); j++){
				System.out.format("%12f", grid.getHorizontalV1(i, j));
			}
			System.out.print("\n");
		}
		
		//print horizontal V2
		System.out.println("\n v2 (horizontal): ");
		for (int i=0; i<grid.getM(); i++){
			for (int j=0; j<=grid.getN(); j++){
				System.out.format("%12f", grid.getHorizontalV2(i, j));
			}
			System.out.print("\n");
		}
				
		//print horizontal V3
		System.out.println("\n v3 (horizontal): ");
		for (int i=0; i<grid.getM(); i++){
			for (int j=0; j<=grid.getN(); j++){
				System.out.format("%12f", grid.getHorizontalV3(i, j));
			}
			System.out.print("\n");
		}
		
		//set vertical V1, V2, V3
		grid.setVerticalV();
		
		//print vertical V1
		System.out.println("\n v1 (vertical): ");
		for (int i=0; i<=grid.getM(); i++){
			for (int j=0; j<grid.getN(); j++){
				System.out.format("%12f", grid.getVerticalV1(i, j));
			}
			System.out.print("\n");
		}
		
		//print vertical V1
		System.out.println("\n v2 (vertical): ");
		for (int i=0; i<=grid.getM(); i++){
			for (int j=0; j<grid.getN(); j++){
				System.out.format("%12f", grid.getVerticalV2(i, j));	
			}
			System.out.print("\n");				
		}
				
		//print vertical V3
		System.out.println("\n v3 (vertical): ");
		for (int i=0; i<=grid.getM(); i++){
			for (int j=0; j<grid.getN(); j++){
				System.out.format("%12f", grid.getVerticalV3(i, j));	
			}
			System.out.print("\n");				
		}
				
		grid.setMobileRadarNumber(20);
		grid.setExpectation();
		System.out.println("\nNumber of mobile radars: " + grid.getMobileRadarNumber());
		System.out.println("Expectation: " + grid.getExpectation());
				
		//set horizontal c1,c2,c3
		grid.setHorizontalC();
				
		//print horizontal c1
		System.out.println("\n c1(horizontal): ");
		for (int i=0; i<grid.getN(); i++){
			for (int j=0; j<=grid.getM(); j++){
				System.out.format("%12f", grid.getHorizontalC1(i, j));
			}
			System.out.print("\n");
		}
				
		//print horizontal c2
		System.out.println("\n c2(horizontal): ");
		for (int i=0; i<grid.getN(); i++){
			for (int j=0; j<=grid.getM(); j++){
				System.out.format("%12f", grid.getHorizontalC2(i, j));
			}
			System.out.print("\n");
		}
				
		//print horizontal c3
		System.out.println("\n c3(horizontal): ");
		for (int i=0; i<grid.getN(); i++){
			for (int j=0; j<=grid.getM(); j++){
				System.out.format("%12f", grid.getHorizontalC3(i, j));
			}
			System.out.print("\n");
		}
				
		//set vertical c1,c2,c3
		grid.setVerticalC();
				
		//print vertical c1
		System.out.println("\n c1(vertical): ");
		for(int i=0; i<=grid.getN(); i++){
			for(int j=0; j<grid.getM(); j++){
				System.out.format("%12f", grid.getVerticalC1(i, j));
			}
			System.out.print("\n");
		}
				
		//print vertical c2
		System.out.println("\n c2(vertical): ");
		for(int i=0; i<=grid.getN(); i++){
			for(int j=0; j<grid.getM(); j++){
				System.out.format("%12f", grid.getVerticalC2(i, j));
			}
			System.out.print("\n");
		}
				
		//print vertical c3
		System.out.println("\n c3(vertical): ");
		for(int i=0; i<=grid.getN(); i++){
			for(int j=0; j<grid.getM(); j++){
				System.out.format("%12f", grid.getVerticalC3(i, j));
			}
			System.out.print("\n");
		}
				
		//set horizontal T
		grid.setHorizontalT();
		
		//print horizontal t1
		System.out.println("\n t1(horizontal): ");
		for (int i=0; i<grid.getN(); i++){
			for (int j=0; j<=grid.getM(); j++){
				System.out.format("%12f", grid.getHorizontalT1(i, j));
			}
			System.out.print("\n");
		}
				
		//print horizontal t2
		System.out.println("\n t2(horizontal): ");
		for (int i=0; i<grid.getN(); i++){
			for (int j=0; j<=grid.getM(); j++){
				System.out.format("%12f", grid.getHorizontalT2(i, j));
			}
			System.out.print("\n");
		}
				
		//print horizontal t3
		System.out.println("\n t3(horizontal): ");
		for (int i=0; i<grid.getN(); i++){
			for (int j=0; j<=grid.getM(); j++){
				System.out.format("%12f", grid.getHorizontalT3(i, j));
			}
			System.out.print("\n");
		}
				
		//set vertical t
		grid.setVerticalT();
				
		//prob time
		//prob2
		List<Segment> probList = grid.getProbTime(17.780886, 0.8);
	}
}
