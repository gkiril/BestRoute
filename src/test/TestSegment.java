package test;

import grid.Segment;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestSegment {
	static Segment s;
	
	@BeforeClass
	public static void BeforeAll(){
		int x = 4;
		int y = 3;
		char l = 'V';
		
		s = new Segment(x,y,l);
	}
	
	@Before
	public void SetUp(){
		
	}
	
	@Test
	public void Test(){
		System.out.print("(" + s.getX() + ", " + s.getY() + ", " + s.getLetter() + ")");
		
		int x = 5;
		int y = 6;
		char l = 'g';
		
		if (Segment.checkLetter(l)){
			s = new Segment(x, y, l);
			System.out.print("(" + s.getX() + ", " + s.getY() + ", " + s.getLetter() + ")");
		}
		else
			System.out.print("\nInapropriate letter!");
		
		x = 7;
		y = 8;
		l = 'H';
		
		if (Segment.checkLetter(l)){
			s = new Segment(x, y, l);
			System.out.print("\n(" + s.getX() + ", " + s.getY() + ", " + s.getLetter() + ")");
		}
	}
	
	@After
	public void AfterTest(){
		
	}
	
	@AfterClass
	public static void AfterAll(){
		
	}
}