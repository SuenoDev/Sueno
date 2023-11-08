package org.durmiendo.sueno.math;

import arc.math.Interp;
import arc.math.Mathf;

public interface SInterp {
    Interp recession = a -> a<1 && a>0 ? (Mathf.cos(a*Mathf.PI) + 1f) / 2f : a>1 ? 1f : 0f;
    Interp arecession = a -> 1f-recession.apply(a);
    Interp wave = a -> a<1 && a>0 ? (Mathf.cos(-a*Mathf.PI/2f) + 1f) / 2f : 0f;
}
