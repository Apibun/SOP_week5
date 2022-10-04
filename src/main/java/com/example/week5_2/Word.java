package com.example.week5_2;

import java.util.*;

public class Word {
    public ArrayList<String> badWords;
    public ArrayList<String> goodWords;

    public Word(){
        this.badWords = new ArrayList<String>();
        badWords.add("fuck");
        badWords.add("olo");
        this.goodWords = new ArrayList<String>();
        goodWords.add("happy");
        goodWords.add("enjoy");
        goodWords.add("like");
    }
}
