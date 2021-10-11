package skinDetection;

import java.io.IOException;
import java.util.Scanner;


public class Main {


    public static void main(String[] args) throws IOException {

        int TotalNumberOfImage=555;

        Scanner cin = new Scanner(System.in);
        int Fold;

        System.out.print("Enter K-fold number : ");
        Fold = cin.nextInt();

        double[] AccuracyOfRounds = new double[Fold];
        double[] F_MeasureOfRounds = new double[Fold];

        String imagePath = "E:\\5th Semester\\Practice\\ibtd\\Image";
        String maskImagePath = "E:\\5th Semester\\Practice\\ibtd\\Mask";
        Training training = new Training(TotalNumberOfImage/Fold, imagePath, maskImagePath);

        Test test = new Test(TotalNumberOfImage/Fold, imagePath);

        for(int i=0; i<Fold; i++){



            System.out.println("Round number : " + (i+1));
            //training.trained(i);

            test.readFromTextFile(i);
            test.checkImage(i);
            Calculation calculation = new Calculation(i, TotalNumberOfImage/Fold);

            AccuracyOfRounds[i] = calculation.getAccuracy();
            F_MeasureOfRounds[i] = calculation.getF_Measure();

        }

        double Sum=0,Sum1=0;

        for(int i=0; i<Fold; i++){

            System.out.println("Accuracy of round " + (i+1)+ " : " + AccuracyOfRounds[i]*100 + "%");
            Sum+=AccuracyOfRounds[i];
            System.out.println("F-Measure of round " + (i+1)+ " : " + F_MeasureOfRounds[i]);
            Sum1+=F_MeasureOfRounds[i];

        }

        System.out.println("Average accuracy : " + (Sum/Fold)*100 + "%");
        System.out.println("Average f-measure : " + (Sum1/Fold));


    }
}










