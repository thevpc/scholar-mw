package net.vpc.scholar.hadruplot.libraries.calc3d.anaglyph;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class AnaglyphFactory {
        private int width;
        private int height;
        private int[] intBufferImage1 = null;
        private int[] intBufferImage2 = null;
        private double red = 0.299;
        private double green = 0.587;
        private double blue = 0.114;

        public void setImage1(BufferedImage bImage) {
                width = bImage.getWidth();
                height = bImage.getHeight();
                intBufferImage1 = ((DataBufferInt) bImage.getRaster().getDataBuffer()).getData();
        }

        public void setImage2(BufferedImage bImage) {
                intBufferImage2 = ((DataBufferInt) bImage.getRaster().getDataBuffer()).getData();
        }

        public void CreateAnaglyph(int amode, BufferedImage mergedImage) {
                int[] mergedIntBuffer = ((DataBufferInt) mergedImage.getRaster()
                                .getDataBuffer()).getData();
                switch (amode) {
                case 0: redBlueAnaglyps(width, height, mergedIntBuffer); break;
                case 1: redGreenAnaglyps(width, height, mergedIntBuffer); break;
                case 2: colorAnaglyps(width, height, mergedIntBuffer);  break;
                case 3: optimizedAnaglyps(width, height, mergedIntBuffer); break;
                case 4: noAnaglypsLeft(width, height, mergedIntBuffer); break;
                case 5: noAnaglypsRight(width, height, mergedIntBuffer); break;
                }
        }

        public void redBlueAnaglyps(int width, int height, int[] mergedIntBuffer) {
                int alpha = 0;

                int i = 0;
                int rLeft = 0;
                int gLeft = 0;
                int bLeft = 0;
                int rRight = 0;
                int gRight = 0;
                int bRight = 0;

                int newRedLeft = 0;
                int newBlueRight = 0;

                for (int j = 0; j < height; j++) {
                        for (int k = 0; k < width; k++) {
                                // if(intBufferImage1.length == intBufferImage2.length)
                                {
                                        int intValue1 = intBufferImage1[i];
                                        rLeft = (intValue1 >> 16) & 0xFF;
                                        gLeft = ((intValue1 >> 8) & 0xFF);
                                        bLeft = intValue1 & 0xFF;
                                        newRedLeft = (int) (red * rLeft + green * gLeft + blue
                                                        * bLeft);

                                        int intValue2 = intBufferImage2[i];
                                        rRight = (intValue2 >> 16) & 0xFF;
                                        gRight = ((intValue2 >> 8) & 0xFF);
                                        bRight = intValue2 & 0xFF;
                                        newBlueRight = (int) (red * rRight + green * gRight + blue
                                                        * bRight);

                                        alpha = (intValue2 >> 24) & 0xFF;

                                        mergedIntBuffer[i++] = ((alpha << 24) | (newRedLeft << 16)
                                                        | (0 << 8) | newBlueRight);
                                }
                        }
                }
        }

        public void redGreenAnaglyps(int width, int height, int[] mergedIntBuffer) {
                int alpha = 0;

                int i = 0;
                int rLeft = 0;
                int gLeft = 0;
                int bLeft = 0;
                int rRight = 0;
                int gRight = 0;
                int bRight = 0;

                int newRedLeft = 0;
                int newGreenRight = 0;

                for (int j = 0; j < height; j++) {
                        for (int k = 0; k < width; k++) {
                                if (intBufferImage1.length == intBufferImage2.length) {
                                        int intValue1 = intBufferImage1[i];
                                        rLeft = (intValue1 >> 16) & 0xFF;
                                        gLeft = ((intValue1 >> 8) & 0xFF);
                                        bLeft = intValue1 & 0xFF;
                                        newRedLeft = (int) (red * rLeft + green * gLeft + blue
                                                        * bLeft);

                                        int intValue2 = intBufferImage2[i];
                                        rRight = (intValue2 >> 16) & 0xFF;
                                        gRight = ((intValue2 >> 8) & 0xFF);
                                        bRight = intValue2 & 0xFF;

                                        newGreenRight = (int) (red * rRight + green * gRight + blue
                                                        * bRight);

                                        alpha = (intValue2 >> 24) & 0xFF;

                                        mergedIntBuffer[i++] = ((alpha << 24) | (newRedLeft << 16)
                                                        | (newGreenRight << 8) | 0);
                                }
                        }
                }
        }

        public void colorAnaglyps(int width, int height, int[] mergedIntBuffer) {
                int alpha = 0;

                int i = 0;
                int rLeft = 0;
                int gRight = 0;
                int bRight = 0;

                for (int j = 0; j < height; j++) {
                        for (int k = 0; k < width; k++) {
                                // if(intBufferImage1.length == intBufferImage2.length)
                                {
                                        rLeft = (intBufferImage1[i] >> 16) & 0xFF;

                                        int intValue2 = intBufferImage2[i];
                                        gRight = ((intValue2 >> 8) & 0xFF);
                                        bRight = intValue2 & 0xFF;

                                        alpha = (intValue2 >> 24) & 0xFF;

                                        mergedIntBuffer[i++] = ((alpha << 24) | (rLeft << 16)
                                                        | (gRight << 8) | bRight);
                                }
                        }
                }
        }

        public void optimizedAnaglyps(int width, int height, int[] mergedIntBuffer) {
                int alpha = 0;

                int i = 0;
                int gLeft = 0;
                int bLeft = 0;
                int gRight = 0;
                int bRight = 0;

                int newRedLeft = 0;

                for (int j = 0; j < height; j++) {
                        for (int k = 0; k < width; k++) {
                                // if(intBufferImage1.length == intBufferImage2.length)
                                {
                                        gLeft = ((intBufferImage1[i] >> 8) & 0xFF);
                                        bLeft = intBufferImage1[i] & 0xFF;
                                        newRedLeft = (int) (0.837 * gLeft + 0.163 * bLeft);

                                        int intValue2 = intBufferImage2[i];
                                        gRight = ((intValue2 >> 8) & 0xFF);
                                        bRight = intValue2 & 0xFF;

                                        alpha = (intValue2 >> 24) & 0xFF;

                                        mergedIntBuffer[i++] = ((alpha << 24) | (newRedLeft << 16)
                                                        | (gRight << 8) | bRight);
                                }
                        }
                }
        }

        public void noAnaglypsLeft(int width, int height, int[] mergedIntBuffer) {
                int i = 0;
                for (int j = 0; j < height; j++) {
                        for (int k = 0; k < width; k++) {
                                // if(intBufferImage1.length == intBufferImage2.length)
                                {
                                        mergedIntBuffer[i] = intBufferImage1[i];
                                        i++;
                                }
                        }
                }
        }

        public void noAnaglypsRight(int width, int height, int[] mergedIntBuffer) {
                int i = 0;
                for (int j = 0; j < height; j++) {
                        for (int k = 0; k < width; k++) {
                                // if(intBufferImage1.length == intBufferImage2.length)
                                {
                                        mergedIntBuffer[i] = intBufferImage2[i];
                                        i++;
                                }
                        }
                }
        }
}
