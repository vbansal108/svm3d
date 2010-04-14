package org.instantsvm.demos.svm3d;

import java.io.IOException;

import libsvm.svm_parameter;
import net.masagroup.jzy3d.maths.Range;

import org.instantsvm.Parameters;
import org.instantsvm.XValResult;
import org.instantsvm.regression.RegressionInputs;
import org.instantsvm.regression.RegressionSVM;
import org.instantsvm.utils.LibSvmIO;

public class RadialRegressionDemo extends Abstract3dDemo{
	public static void main(String[] args) throws IOException {
		RegressionInputs inputs = new RegressionInputs( LibSvmIO.defaultInputPath("radial", true) );
		inputs.scale(1000, 1000, 1);
		
		
		
		Parameters params = getParams();
		
		//RegressionSVM svm = LazySVM.loadOrTrainRegression("radial", params, inputs);
		//RegressionSVM svm = LazySVM.trainAndSaveRegression("radial", params, inputs);
		
		RegressionSVM svm = new RegressionSVM();
		XValResult r = svm.xval(inputs.getX(), inputs.getY(), params, 4, new Range(0,1000), 10, new Range(0,1), 10);
		params.getParam().C = r.bestC;
		params.getParam().gamma = r.bestG;
		
		System.out.println("Maybe reused a trained svm!");
		openChart( getRegressionChart(svm, inputs) );
		
		params.print();
	}
		
	public static Parameters getParams(){
		Parameters params = new Parameters();

		params.getParam().svm_type = svm_parameter.NU_SVR;
		
		params.getParam().eps = 0.001; // kernel width?
		//params.getParam().p = 0.001; // epsilon  error  def=0.1
		//params.getParam().probability = 1;
		params.getParam().gamma = 1;// / values.length;
		params.getParam().C = 100; // more precise but computation longer when increase
		//params.getParam().coef0 = 0.5;
		//params.getParam().degree = 10;
		//params.getParam().gamma = 1;
		//params.getParam().shrinking = 0;
		return params;
	}
}
