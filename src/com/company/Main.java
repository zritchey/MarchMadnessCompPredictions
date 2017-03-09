package com.company;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        long time=System.currentTimeMillis();
        SimpleDateFormat sdf=new SimpleDateFormat("mm:ss:SS");
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
            String path="Predictions.csv";
            fw=new FileWriter(path);
            bw=new BufferedWriter(fw);
            for (int i=0;i<team.teamList.size()-1;i++){
                team t1=team.teamList.get(i);
                for (int num=i;num<team.teamList.size();num++){
                    team t2=team.teamList.get(num);
                    if (t1.id<t2.id)
                        bw.append(2017+"_"+t1.id+"_"+t2.id+","+t1.compare(t2)+"\n");
                    else if(t2.id<t1.id)
                        bw.append(2017+"_"+t2.id+"_"+t1.id+","+t2.compare(t1)+"\n");

                }
            }
            bw.close();
            fw.close();
            sc=new Scanner(new File(path));
            while(sc.hasNextLine()){
                System.out.println(sc.nextLine());
            }
            sc.close();
        }
        catch(IOException ioe){

        }


        for (team t:team.teamList){
            t.print();
        }
        System.out.println("\n\n\n\nTime elapsed: "+sdf.format(System.currentTimeMillis()-time));


    }
}
