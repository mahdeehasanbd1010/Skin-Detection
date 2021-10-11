package skinDetection;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Calculation {

    double threshold = 0.85;
    double[][][] Skin = new double[256][256][256];

    int tp=0, tn=0, fp=0, fn=0;
    double Accuracy,F_Measure;

    public Calculation(int roundNumber,int TestSet) throws IOException {

        readFromTextFile(roundNumber);

        String[] maskImage = getAllFiles("E:\\5th Semester\\Practice\\ibtd\\Mask");
        String[] outputImage = getAllFiles("E:\\5th Semester\\Practice\\ibtd\\Output");


        for(int i=roundNumber*TestSet; i<(roundNumber*TestSet+TestSet); i++){

            BufferedImage bufferedImage,maskBufferedImage;

            Color pixel,maskPixel;

            bufferedImage = ImageIO.read(new File(outputImage[i]));
            maskBufferedImage = ImageIO.read(new File(maskImage[i]));

            for(int y=0; y<maskBufferedImage.getHeight() && y<bufferedImage.getHeight(); y++){

                for(int x=0; x<maskBufferedImage.getWidth() && x<bufferedImage.getWidth(); x++){

                    pixel = new Color(bufferedImage.getRGB(x,y));
                    maskPixel = new Color(maskBufferedImage.getRGB(x,y));

                    if(maskPixel.getRed()>240 && maskPixel.getGreen()>240 && maskPixel.getBlue()>240){

                        if(Skin[pixel.getRed()][pixel.getGreen()][pixel.getBlue()]<=threshold) tn++;
                        else fn++;

                    }

                    else {

                        if(Skin[pixel.getRed()][pixel.getGreen()][pixel.getBlue()]>threshold) tp++;
                        else fp++;
                    }


                }


            }

        }

        //System.out.println("Accuracy : " + calculateAccuracy());

        Accuracy = calculateAccuracy();
        F_Measure = calculateF_Measure();

    }

    public double calculateF_Measure(){

        return 2*(calculateRecall()*calculatePrecision())/(calculateRecall()+calculatePrecision());
    }

    public double calculatePrecision(){

        return (double)tp/(double)(tp+fp);
    }

    public double calculateRecall (){

        return (double)tp/(double)(tp+fn);
    }

    public double calculateAccuracy(){

        return (double)(tp+tn)/(double)(tp+fp+tn+fn);
    }

    public double getAccuracy(){

        return Accuracy;
    }

    public double getF_Measure(){

        return F_Measure;
    }


    public void readFromTextFile(int roundNumber) throws IOException {

        int i=0,j=0,k=0;
        String[] lineOfData;
        String line;

        BufferedReader fileReader = new BufferedReader(
                new FileReader("E:\\5th Semester\\Practice\\ibtd\\Data\\data"+roundNumber+".txt"));

        while((line=fileReader.readLine())!=null){

            lineOfData=line.split(",");

            for(k=0;k<256;k++){

                //System.out.println("gsdf:"+Double.parseDouble(lineOfData[k]));

                Skin[i][j][k]=Double.parseDouble(lineOfData[k]);

            }

            if(k==256){

                j++;
                if(j==256){
                    j=0;
                    i++;
                }
            }

        }


    }



    public String[] getAllFiles(String folderPath){

        File[] files = new File(folderPath).listFiles();
        ArrayList<String> filesPathList = new ArrayList<String>();

        for(File file: files){
            filesPathList.add(file.getAbsolutePath());
        }

        String filesPath[] = new String[filesPathList.size()];
        filesPathList.toArray(filesPath);
        return filesPath;
    }
}
