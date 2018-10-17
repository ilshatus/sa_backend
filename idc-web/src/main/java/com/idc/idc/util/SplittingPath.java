package com.idc.idc.util;

public class SplittingPath {


//    public  setAnimatedRoute = (leg) => {
//        return leg.steps.reduce((res, current) => res.concat(current.path.reduce((res, current) => res.concat(getPointBetween(res[res.length - 1], current), current), [])), []);
//    };
//
//    int min = 100000;
//
//    final double MIN = 0.000009999999996068709;
//
//
//    export const getPointBetween = (a, b) => {
//        if (!a) return b;
//
//    const dist = math.distance([a.lat(), a.lng()], [b.lat(), b.lng()]);
//    const ratio = parseInt(dist / MIN, 10);
//
//    const res = new Array(ratio + 1);
//        res[0] = a;
//
//        for (let i = 1; i < ratio; i++) {
//            res[i] = new window.google.maps.LatLng(res[i - 1].lat() + (b.lat() - res[i - 1].lat()) / (ratio - i + 1),
//                    res[i - 1].lng() + (b.lng() - res[i - 1].lng()) / (ratio - i + 1))
//        }
//
//        res[ratio] = b;
//
//        return res
//    };
}
