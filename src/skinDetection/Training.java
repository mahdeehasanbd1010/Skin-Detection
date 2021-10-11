package skinDetection;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Training {

    int TotalNumberOfImage=555;
    int TrainingSet;
    double[][][] Skin = new double[256][256][256];
    double[][][] NotSkin = new double[256][256][256];
    int SumOfSkin,SumOfNotSkin;

    String imagePath;
    String maskImagePath;

    String[] image;
    String[] maskImage;

    public void initialization(){

        for(int i=0;i<256;i++){
            for(int j=0;j<256;j++){
                for(int k=0;k<256;k++){

                    Skin[i][j][k]=0;
                    NotSkin[i][j][k]=0;
                }
            }
        }
    }

    public Training (int TrainingSet, String imagePath, String maskImagePath){

        this.TrainingSet = TrainingSet;
        this.imagePath = imagePath;
        this.maskImagePath = maskImagePath;

        image = getAllFiles(imagePath);
        maskImage = getAllFiles(maskImagePath);

    }

    public void trained(int roundNumber) throws IOException {

        initialization();

        for(int imageNumber=0; imageNumber < TotalNumberOfImage; imageNumber++){

            if((roundNumber*TrainingSet <= imageNumber) && ((roundNumber*TrainingSet+TrainingSet-1) >= imageNumber)){

                imageNumber = roundNumber*TrainingSet+TrainingSet;
                if(imageNumber >= TotalNumberOfImage) break;
            }

            System.out.println(image[imageNumber]);

            BufferedImage  bufferedImage,maskBufferedImage;
            bufferedImage = ImageIO.read(new File(image[imageNumber]));
            maskBufferedImage = ImageIO.read(new File(maskImage[imageNumber]));

            Color imagePixel, maskPixel;

            for(int y=0; y<bufferedImage.getHeight(); y++){

                for(int x=0; x<bufferedImage.getWidth(); x++){

                    imagePixel = new Color(bufferedImage.getRGB(x,y));
                    maskPixel = new Color(maskBufferedImage.getRGB(x,y));

                    if(maskPixel.getRed()>240 && maskPixel.getGreen()>240&& maskPixel.getBlue()>240){

                        NotSkin[imagePixel.getRed()][imagePixel.getGreen()][imagePixel.getBlue()]++;
                    }

                    else {

                        Skin[imagePixel.getRed()][imagePixel.getGreen()][imagePixel.getBlue()]++;
                    }


                }
            }

        }

        calculateSum();

        writeToTextFile(roundNumber);

    }

    public void calculateSum(){

        for(int i=0; i<256; i++){

            for(int j=0; j<256; j++){

                for(int k=0; k<256; k++){

                    SumOfSkin+=Skin[i][j][k];
                    SumOfNotSkin+=NotSkin[i][j][k];

                }
            }
        }

        System.out.println("NS: " + SumOfNotSkin + " S: " + SumOfSkin + " total: " +(SumOfSkin+SumOfNotSkin));
    }


    public void writeToTextFile(int roundNumber) throws IOException {

        FileWriter fileWriter = new FileWriter("E:\\5th Semester\\Practice\\ibtd\\Data\\data"+roundNumber+".txt");

        for(int i=0; i<256; i++){

            for(int j=0; j<256; j++){

                for(int k=0; k<256; k++){

                    Skin[i][j][k]/=SumOfSkin;
                    NotSkin[i][j][k]/=SumOfNotSkin;

                    if(Skin[i][j][k]==0 && NotSkin[i][j][k]==0) Skin[i][j][k]=0;

                    else if(Skin[i][j][k]!=0 && NotSkin[i][j][k]==0) Skin[i][j][k]=10;

                    else Skin[i][j][k]/=NotSkin[i][j][k];

                    //System.out.println(Skin[i][j][k]);

                    if(k==255) fileWriter.write(Skin[i][j][k]+"\n");

                    else fileWriter.write(Skin[i][j][k]+",");

                }
            }
        }
        fileWriter.flush();
        fileWriter.close();


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

    public double[][][] getSkin() {

        return Skin;
    }
}
