package com.ensah.utils;

import java.util.Random;

public class GeneratePassword {
    private String mdp;

    public GeneratePassword(int N) {
        int tab[] = new int[N];
        int y;
        int i = 0;
        while (i < N) {
            Random random = new Random();
            int x = 0;
            while (true) {
                x = random.nextInt(10);
                if (x != 0) break;
            }
            tab[i] = x;
            i += 1;
        }
        StringBuilder str = new StringBuilder();
        for (int h = 0; h < tab.length; h++) {
            str.append(tab[h]);
        }
        String fStr = str.toString();
        this.mdp = fStr;
    }

    public String getMdp() {
        return mdp;
    }
}