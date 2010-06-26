package org.instantsvm.demos.svm3d;

import java.awt.Rectangle;

import net.masagroup.jzy3d.bridge.ChartLauncher;
import net.masagroup.jzy3d.chart.Chart;
import net.masagroup.jzy3d.colors.Color;
import net.masagroup.jzy3d.colors.ColorMapper;
import net.masagroup.jzy3d.colors.colormaps.ColorMapRainbow;
import net.masagroup.jzy3d.maths.BoundingBox3d;
import net.masagroup.jzy3d.maths.Coord3d;
import net.masagroup.jzy3d.maths.Range;
import net.masagroup.jzy3d.plot3d.builder.Mapper;
import net.masagroup.jzy3d.plot3d.builder.concrete.OrthonormalTesselator;
import net.masagroup.jzy3d.plot3d.builder.concrete.RingInterpolator;
import net.masagroup.jzy3d.plot3d.primitives.Scatter;
import net.masagroup.jzy3d.plot3d.primitives.Shape;

import org.instantsvm.Parameters;
import org.instantsvm.regression.RegressionInputs;
import org.instantsvm.regression.RegressionSVM;
import org.instantsvm.svm3d.editors.RegressionParamsEditor;
import org.instantsvm.svm3d.tesselation.SvmGrid;
import org.instantsvm.svm3d.tesselation.SvmMapper;
import org.instantsvm.svm3d.utils.Conversion;

public abstract class Abstract3dDemo {
	public static void openChart(Chart chart){
		ChartLauncher.openChart( chart, "Svm3D " );
	}
	
	public static void openChart(Chart chart, Parameters params){
		ChartLauncher.openChart( chart, "InstantSVM & Svm3D " );
		ChartLauncher.openPanel( new RegressionParamsEditor(params), new Rectangle(800, 0, 200, 600), "Regression params");
	}
	
	public static void scale(Coord3d[] pts, float xfact, float yfact, float zfact){
		for (int i = 0; i < pts.length; i++) {
			pts[i].x *= xfact;
			pts[i].y *= yfact;
			pts[i].y *= zfact;
		}
	}
		
	/****************** CHART GENERATION ********************/
	
	public static Chart getRegressionChart(Coord3d[] values, Parameters parameters) {
		SvmMapper mapper = new SvmMapper( values, parameters );

		mapper.getSvm().getParameters().print();
		//Array.print( mapper.getOutput() );
		
		return getRegressionChart( mapper, values );
	}
	
	public static Chart getRegressionChart(RegressionSVM svm, RegressionInputs inputs) {
		Coord3d[] values = Conversion.toCoord3d(inputs);
		SvmMapper mapper = new SvmMapper( svm );
		return getRegressionChart( mapper, values );
	}
	
	protected static Chart getRegressionChart(SvmMapper mapper, Coord3d[] values) {
		BoundingBox3d b = Conversion.getBounds(values);
		System.out.println( b );
		
		final Shape s = getRingSurface( mapper, b.getXRange(), b.getYRange(), b.getZRange(), 50 );
		Chart chart = new Chart();
		chart.getScene().getGraph().add(s);
		
		/*s.setFace(new ColorbarFace(s, 
				    chart.getView().getAxe().getLayout().getZTickProvider(), 
				    chart.getView().getAxe().getLayout().getZTickRenderer()));
		s.setFace2dDisplayed(true);*/ 
		loadScatter( chart, values );
		
		return chart;
	}
	
	/****************** 3d OBJECTS GENERATION ********************/
	
	public static Scatter loadScatter(Chart chart, Coord3d[] coords) {
		Scatter scatter = new Scatter(coords, Color.BLACK, 5);
		chart.getScene().getGraph().add(scatter);
		return scatter;
	}
	
	public static Shape getRingSurface(Mapper mapper, Range xrange, Range yrange, Range zrange, int steps){
		ColorMapper cmapper = new ColorMapper(new ColorMapRainbow(), (float)zrange.getMin(), (float)zrange.getMax(), new Color(1,1,1,.5f));
		RingInterpolator tesselator = new RingInterpolator(0, (float)xrange.getMax(), cmapper, new Color(1f,1f, 1f));
		SvmGrid grid = new SvmGrid(xrange, steps, yrange, steps);
		
		Shape surface = (Shape) tesselator.build( grid.apply( mapper ) );
		surface.setFaceDisplayed(true);
		surface.setWireframeDisplayed(true);
		surface.setWireframeColor(Color.BLACK);
		return surface;
	}
	
	public static Shape getSquareSurface(Chart chart, Mapper mapper, Range xrange, Range yrange, int steps){
		Shape surface = (Shape)new OrthonormalTesselator().build( new SvmGrid(xrange, steps, yrange, steps).apply( mapper ) ) ;
		surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1,1,1,.5f)));
		surface.setFaceDisplayed(true);
		surface.setWireframeDisplayed(true);
		surface.setWireframeColor(Color.BLACK);
		return surface;
	}
}
