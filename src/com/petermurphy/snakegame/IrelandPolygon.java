package com.petermurphy.snakegame;

import java.awt.*;

public class IrelandPolygon {
    private static final double LONG_MIN = -10.5;
    private static final double LONG_MAX = -5.5;
    private static final double LAT_MIN = 51.4;
    private static final double LAT_MAX = 55.5;
    private static final int PANEL_WIDTH = 600;
    private static final int PANEL_HEIGHT = 600;
    private static final int INFO_HEIGHT = 60;

    public static Polygon getIrelandPolygon() {
        // Raw coordinates from https://geojson.io/#map=5.96/53.654/-8.797
        double[][] rawCoords = {
            {-6.458921486446002, 52.168341849309996},
            {-6.072036759210164, 52.9876310278122},
            {-6.317872410564149, 53.98614727814052},
            {-5.458377683910072, 54.35643307189568},
            {-5.999914556396135, 55.18490262071683},
            {-7.410687077252504, 55.3469496543174},
            {-8.299342238152434, 55.14340093362799},
            {-8.768820991825947, 54.63337656843527},
            {-8.400395743181264, 54.48529424278874},
            {-8.677426695322907, 54.2849064707635},
            {-10.110890358903816, 54.29840389356929},
            {-10.12701975987548, 53.388013541055926},
            {-9.484456655543823, 53.21298624052764},
            {-8.959751076230674, 53.26176784726047},
            {-8.959265812260156, 53.12797735638691},
            {-9.315909780867742, 53.1268978561159},
            {-10.04221105213162, 52.315735444521806},
            {-10.422436304928368, 52.17556714264495},
            {-10.156740928206943, 51.63144356410075},
            {-9.18139066759906, 51.572541205263235},
            {-6.458921486446002, 52.168341849309996}
        };

        int[] xCoords = new int[rawCoords.length];
        int[] yCoords = new int[rawCoords.length];

        for (int i = 0; i < rawCoords.length; i++) {
            xCoords[i] = (int) ((rawCoords[i][0] - LONG_MIN) / (LONG_MAX - LONG_MIN) * PANEL_WIDTH);
            yCoords[i] = (int) ((LAT_MAX - rawCoords[i][1]) / (LAT_MAX - LAT_MIN) * PANEL_HEIGHT) + INFO_HEIGHT;
        }

        return new Polygon(xCoords, yCoords, xCoords.length);
    }
}

