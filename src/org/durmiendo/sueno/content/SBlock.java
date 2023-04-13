package org.durmiendo.sueno.content;

import org.durmiendo.sueno.content.distr.Test;
import org.durmiendo.sueno.extend.SuenoBlock;

public class SBlock {
    public static SuenoBlock test;
    public static void load() {
        test = new Test("test") {
            {
                this.health = 300;
                this.sizeX = 1;
                this.sizeY = 2;
            }
        };
    }
}
