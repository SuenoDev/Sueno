package org.durmiendo.sueno.math;

public class Maths {

    public static boolean isAngleInSector(float slen, float scenter, float angle) {
        slen = (slen + 360) % 360;
        scenter = (scenter + 360) % 360;
        angle = (angle + 360) % 360;

        float startAngle = scenter - slen / 2;
        float endAngle = scenter + slen / 2;

        if (startAngle <= endAngle) {
            return startAngle <= angle && angle <= endAngle;
        } else {
            return (startAngle <= angle && angle <= 360f) || (0f <= angle && angle <= endAngle);
        }
    }
}
