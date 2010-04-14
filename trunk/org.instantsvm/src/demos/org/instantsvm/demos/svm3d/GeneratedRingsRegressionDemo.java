package org.instantsvm.demos.svm3d;

import java.io.IOException;

import libsvm.svm_parameter;

import net.masagroup.jzy3d.maths.Array;
import net.masagroup.jzy3d.maths.Coord3d;
import net.masagroup.jzy3d.maths.Range;

import org.instantsvm.Parameters;
import org.instantsvm.XValResult;
import org.instantsvm.regression.RegressionInputs;
import org.instantsvm.regression.RegressionParameters;
import org.instantsvm.regression.RegressionSVM;
import org.instantsvm.svm3d.utils.Conversion;

public class GeneratedRingsRegressionDemo extends Abstract3dDemo {
	public static void main(String[] args) throws IOException {
		RegressionInputs inputs = Conversion.toRegressionInputs( getInputs() );
		Parameters params = getParams();
		
		RegressionSVM svm = new RegressionSVM();
		XValResult r = svm.xval(inputs.getX(), inputs.getY(), params, 3, new Range(0, 10000), 6, new Range(0, 5), 6);	
		System.out.println("Best parameters:" + r);
		Array.print(r.errors);
		
		params.getParam().C = r.bestC;
		params.getParam().gamma = r.bestG;
		svm.train(inputs, params);
		
		System.out.println("Maybe reused a trained svm!");
		openChart( getRegressionChart(svm, inputs) );
	}

	public static Coord3d[] getInputs() {
		double[] radius = {0.2,0.5, 0.8};
		double[] height = {0,0.5, -0.5};
		int n[] = {10,10,10};
		return Conversion.toArray(GenRadial.generate(radius, height, n));
	}

	public static Parameters getParams() {
		Parameters params = new RegressionParameters(0.01, 100, 0.5);

		if (false) {
			params.getParam().svm_type = svm_parameter.EPSILON_SVR;
			params.getParam().eps = 0.01;
			params.getParam().C = 1000;
			params.getParam().probability = 1;
			params.getParam().gamma = 0.5;

			// params.getParam().coef0 = 0.5;
			// params.getParam().degree = 10;
			// params.getParam().p = 0.00001;
			// params.getParam().gamma = 1;
			// params.getParam().shrinking = 0;
		}
		return params;
	}
}
