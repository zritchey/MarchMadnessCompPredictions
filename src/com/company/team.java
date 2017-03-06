package com.company;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by zr162 on 3/6/17.
 */
public class team {
    public team(int id, String name){


    }
    public static String findName(int id)throws IOException{
        Scanner sc=new Scanner(new File("Teams.csv"));
        String s = "";
        while (sc.hasNextLine()){

            String[]a =sc.nextLine().split(",");
            if (a[0].equals(new String(id))){
                
            }
        }

    }
}
