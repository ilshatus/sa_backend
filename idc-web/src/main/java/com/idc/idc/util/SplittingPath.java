package com.idc.idc.util;

import java.util.ArrayList;

public class SplittingPath {
    private static final Double MIN = 0.000009999999996068709;

    public static Double[][] split(Double[][] points){
        ArrayList<Double[]> splitted_points =  new ArrayList<>();
        for(int i = 1; i < points.length; i++){
            Double distance = distance(points[i-1], points[i]);
            if (distance > MIN){
                int numOfPoints = (int) (distance/MIN);
                for (int j = 0; j < numOfPoints + 1; j++){
                    Double[] point = { points[i-1][0] + (points[i][0] - points[i-1][0]) * ((double) j/numOfPoints), points[i-1][1] + (points[i][1] - points[i-1][1]) * ((double) j/numOfPoints)};
                    splitted_points.add(point);
                }
            } else {
                splitted_points.add(points[i-1]);
            }
        }
        Double[][] result = new Double[splitted_points.size()][splitted_points.get(0).length];
        for (int i = 0; i < splitted_points.size(); i++) {
            result[i] = splitted_points.get(i);
        }
        return result;
    }

    private static Double distance(Double[] a, Double[] b){
        return Math.sqrt((a[0] - b[0])*(a[0] - b[0]) + (a[1] - b[1])*(a[1] - b[1]));
    }
}

