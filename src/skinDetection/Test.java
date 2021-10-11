package skinDetection;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Test {

    int TestSet;
    String imagePath;
    String[] image;

    double[][][] Skin = new double [256][256][256];
    double threshold = 0.85;
    
    public Test(int TestSet, String imagePath){
        this.imagePath = imagePath;
        this.TestSet = TestSet;
        image = getAllFiles(imagePath);

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

    public void checkImage(int roundNumber) throws IOException {


        for(int i=roundNumber*TestSet; i<(roundNumber*TestSet+TestSet); i++){

            BufferedImage bufferImage = ImageIO.read(new File(image[i]));
            File newImage;
            Color pixel;

            for(int y=0; y < bufferImage.getHeight(); y++){

                for(int x=0; x < bufferImage.getWidth(); x++){

                    pixel = new Color(bufferImage.getRGB(x,y));

                    if(Skin[pixel.getRed()] [pixel.getGreen()] [pixel.getBlue()] > threshold){

                        bufferImage.setRGB(x, y, bufferImage.getRGB(x, y));
                    }

                    else{

                        bufferImage.setRGB(x, y, Color.WHITE.getRGB());
                    }

                }
            }

            newImage = new File("E:\\5th Semester\\Practice\\ibtd\\Output\\"+i+".jpg");
            ImageIO.write(bufferImage,"jpg",newImage);

            System.out.println(newImage.getAbsolutePath());


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
