package com.mygdx.bibletaboo.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class FileWriter {

    //        Some stuff I might include in here eventually
    //        x.replace('\r', '\u0000');

     private FileHandle file;
     private String fileContents;


    public FileWriter(String path){

        file = Gdx.files.local(path);
        fileContents = file.readString();
    }

    public String changeValue(String value, String newvalue){
        String modifiedFile = fileContents.replaceFirst(value, newvalue);
        file.writeString(modifiedFile, false);
        Gdx.app.log("FileWriter", "Value Changed");
        return modifiedFile;
    }
}
