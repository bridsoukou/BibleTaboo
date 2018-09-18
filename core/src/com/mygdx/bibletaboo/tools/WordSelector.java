package com.mygdx.bibletaboo.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordSelector {

    private List<String> guessWordsList = new ArrayList<String>();
    private List<Integer> usedBeforeList = new ArrayList<Integer>();

   public WordSelector(){
       FileHandle handle;
       String text;
       String[] words;
       //first100 for real words, testingwords for test
       handle = Gdx.files.internal("data/first100.txt");
       text = handle.readString();
       words = text.split("\n");

      for(int a = 0; a < words.length; a++) {
          guessWordsList.add(words[a].trim());
      }

      usedBeforeList.add(1);
   }

   public int randomizer(){
       Random r = new Random();
       int index = r.nextInt(guessWordsList.size());
       boolean validWord = false;

       while (!validWord) {
           index = r.nextInt(guessWordsList.size());

           if (guessWordsList.get(index).contains("@")/* && !usedBeforeList.contains(index)*/) {
                   validWord = true;
                   usedBeforeList.add(index);
           } else if (usedBeforeList.size() == guessWordsList.size() / 5) { //Bug on this line. NullPointerException
                   usedBeforeList.clear();
                   Gdx.app.log("usedBeforeList", "Cleared");
               }
           }

       return index;
       }

   public  String getGuessWord(int num){
       String theword;
       theword = guessWordsList.get(num).substring(1);
       return theword;
   }

   public  String getTabooWord(int num, int pos){
       if(pos > 0) {
           return guessWordsList.get(num + pos);
       } else {
           Gdx.app.log("Error", "Invalid Number");
       }

       return guessWordsList.get(num + 1);
   }

   public void setUsedBeforeList(List<Integer> list){

       usedBeforeList = list;

    }

   public List<Integer> getUsedBeforeList(){

       return usedBeforeList;

   }

   public void clearUsedBeforeList(){
       if(!usedBeforeList.isEmpty()) {
           usedBeforeList.clear();
       }
   }
}
