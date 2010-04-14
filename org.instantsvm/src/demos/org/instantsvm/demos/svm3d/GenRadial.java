package org.instantsvm.demos.svm3d;

import java.util.ArrayList;
import java.util.List;

import net.masagroup.jzy3d.maths.Coord3d;

public class GenRadial {
	public static List<Coord3d> generate(double[] radius, double height[], int[] n){
		List<Coord3d> rings = new ArrayList<Coord3d>();
		for (int i = 0; i < n.length; i++) {
			double r = radius[i];
			double h = height[i];
			double t = 0;
			double step = 2*Math.PI / n[i];
			while(t<(2*Math.PI)){
				Coord3d c = new Coord3d(t,0,r).cartesian();
				c.z += h;
	        	rings.add( c );
	        	
	        	t += step;
	        }
		}
		return rings;
	}
}
