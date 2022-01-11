package lama.show;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;


class GrayImage {
    //获取灰度图 get pic to gray
    Image getGray(Image image) {
        PixelReader pixelReader = image.getPixelReader();
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        WritableImage grayImage = new WritableImage(width, height);
        PixelWriter pixelWriterGray = grayImage.getPixelWriter();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Color color = pixelReader.getColor(i, j);
                color = color.grayscale();
                color = color.invert();
                pixelWriterGray.setColor(i, j, color);
            }
        }

        return grayImage;
    }


}