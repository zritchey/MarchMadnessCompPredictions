package com.company;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(new File("Teams.csv"));
            sc.nextLine();
            FileWriter fw=new FileWriter("failures");
            BufferedWriter bw=new BufferedWriter(fw);
            while (sc.hasNextLine()){
                String s=sc.nextLine();
                int i=Integer.parseInt(s.substring(0,s.indexOf(',')));
                team play=new team(i);
                if (play.name.equalsIgnoreCase("fail"))
                    bw.append("Team Id #"+i+"has failed.\n");
                else{
                    play.setStats();
                    team.teamList.add(play);
                }
            }
            sc.close();
            bw.close();
            fw.close();
            sc=new Scanner

        }
        catch(IOException ioe){

        }

        for (team t:team.teamList){
            t.print();
        }


    }
}
