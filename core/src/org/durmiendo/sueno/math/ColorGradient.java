package org.durmiendo.sueno.math;

import arc.graphics.Color;

public class ColorGradient {
    public static Color[] gradient(float value, Color... colors) {
        int numColors = colors.length;

        // Если передан только один цвет, возвращаем его без изменений
        if (numColors == 1) {
            return colors;
        }

        // Если передано два или более цвета, создаем массивы для хранения цветов
        Color[] color1 = new Color[numColors - 1];
        Color[] color2 = new Color[numColors - 1];

        // Заполняем массивы color1 и color2 значениями переданных цветов
        for (int i = 0; i < numColors - 1; i++) {
            color1[i] = colors[i];
            color2[i] = colors[i + 1];
        }

        int[] red = new int[numColors - 1];
        int[] green = new int[numColors - 1];
        int[] blue = new int[numColors - 1];

        // Рассчитываем значения red, green и blue для каждой пары цветов
        for (int i = 0; i < numColors - 1; i++) {
            red[i] = (int) (color1[i].r * (1 - value) + color2[i].r * value);
            green[i] = (int) (color1[i].g * (1 - value) + color2[i].g * value);
            blue[i] = (int) (color1[i].b * (1 - value) + color2[i].b * value);
        }

        // Создаем и возвращаем массив градиентных цветов
        Color[] gradientColors = new Color[numColors - 1];
        for (int i = 0; i < numColors - 1; i++) {
            gradientColors[i] = new Color(red[i], green[i], blue[i]);
        }
        return gradientColors;
    }
}