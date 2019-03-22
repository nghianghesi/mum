package common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Application {

	public static void main(String[] args) {
		if(args.length >= 2) {
			String trainingFile = args[0];
			String testfile = args[1];
			
			ArrayList<Sample> trainingSet = Util.readBankData(trainingFile);
			ArrayList<Sample> testingSet = Util.readBankData(testfile);
			List<common.KNN<Sample>> knnRunner = new ArrayList<>(); 
			knnRunner.add(new serial.SerialKNN<Sample>());
			knnRunner.add(new parallel.GroupParallelKNN<Sample>(10));
			knnRunner.add(new parallel.SingleParallelKNN<Sample>());
			
			int k = 1000;
			int testNumber = testingSet.size();
			int countError = 0, countTested = 0;
			long serialTime=-1;
			for(common.KNN<Sample> runner : knnRunner) {
				System.out.println(runner.getName());
				countError = 0;
				countTested = 0;
				Date t = new Date();
				for(int i = 0;i<testNumber && i<testingSet.size();i++) {
					String tag = runner.classify(trainingSet, testingSet.get(i), k);
					countTested++;
					if(!tag.equals(testingSet.get(i).getTag())) {
						countError++;
						//System.out.println("Error " + countError + " per "+ countTested);
					}
				}
				long time= (new Date()).getTime() - t.getTime();
				if(serialTime<0) {
					serialTime=time;
					System.out.println("Elapsed Time: " + time);
				}else {
					System.out.println("Elapsed Time: " + time + " speed up "+ (1.0*serialTime/time));
				}
				System.out.println("Error " + countError + " per "+ countTested);
			}
		}
	}
}
