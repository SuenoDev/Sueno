package org.durmiendo.sueno.math;

import arc.math.Interp;
import arc.math.Mathf;

public interface SInterp {
    Interp recession = a -> a<1 && a>0 ? (Mathf.cos((float) (a*Math.PI*Mathf.degRad)) + 1f) / 2f : a>1 ? 1f : 0f;
}
