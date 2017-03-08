package com.company;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by zr162 on 3/6/17.
 */
public class team {
    public static ArrayList<team> teamList=new ArrayList<team>();
    public team(int id){
        name=findName(id);
        if(name.equals("fail")){
            this.id=0;
        }
        else{
            this.id=id;
        }
    }

    public double[] setWinScore(String path) {
        int start=0;//
        int winGames=0;//
        int pointWin=0;//
        int pointLoss=0;//
        int lossGames=0;//
        int weightedGames=0;//
        int weightedWins=0;//
        int otWin=0;//
        int otLoss=0;//
        int otwinPts=0;//
        int otlossPts=0;//
        int otGames=0;//
        try {
            Scanner sc = new Scanner(new File(path));
            sc.nextLine();
            while (sc.hasNextLine()){
                String[]a=sc.nextLine().split(",");
                if (start==0){
                    start=Integer.parseInt(a[0])-1;
                }
                //indx 2 is WTeam
                //indx 4 is LTeam
                if(Integer.parseInt(a[2])==id||Integer.parseInt(a[4])==id) {
                    int weight = (int)Math.pow((Integer.parseInt(a[0]) - start),2);
                    weightedGames += weight;
                    if (Integer.parseInt(a[2]) == id) {//if the team wins
                        winGames++;
                        pointWin+=Integer.parseInt(a[3]);
                        weightedWins+=weight;
                        if(Integer.parseInt(a[7])!=0) {
                            otGames++;
                            otWin++;
                            otwinPts+=Integer.parseInt(a[3]);
                        }

                    }
                    else{
                        lossGames++;
                        pointLoss+=Integer.parseInt(a[5]);
                        if(Integer.parseInt(a[7])!=0) {
                            otGames++;
                            otLoss++;
                            otlossPts+=Integer.parseInt(a[5]);
                        }
                    }

                }
            }
            sc.close();
        }
        catch(IOException ioe){

        }
        //winPts,lossPts,avgPts,winRate,overtimeWinRate,overtimeWinPts,overtimeLossPts,avgOvertimePts
        double[]stats={pointWin/(double)winGames,pointLoss/(double)lossGames,(pointLoss+(double)pointWin)/(winGames+(double)lossGames),weightedWins/(double)weightedGames,otWin/(double)otGames,otwinPts/(double)otWin,otlossPts/(double)otLoss,(otwinPts+(double)otlossPts)/(otWin+(double)otLoss)};
        return stats;
    }
    //give compact results a 1/5 weight in final winScore calculation
    public void setStats(){
        double[]details=setWinScore("TourneyDetailedResults.csv");
        double[]general=setWinScore("TourneyCompactResults.csv");
        double comp[]=new double [general.length];
        for(int i=0;i<comp.length;i++){
            comp[i]=((4*general[i])+details[i])/5;
        }
        winPts=comp[0];
        lossPts=comp[1];
        avgPts=comp[2];
        winRate=comp[3];
        overtimeWinRate=comp[4];
        overtimeWinPts=comp[5];
        overtimeLossPts=comp[6];
        avgOvertimePts=comp[7];

    }
    private double winPts;
    private double lossPts;
    private double avgPts;
    private double winRate;
    private double overtimeWinRate;
    private double overtimeWinPts;
    private double overtimeLossPts;
    private double avgOvertimePts;

    public final int id;
    public final String name;
    public static String findName(int id){
        String s = "fail";
        try{
        Scanner sc=new Scanner(new File("Teams.csv"));
            while (sc.hasNextLine()){

                String[]a =sc.nextLine().split(",");
                if (a[0].equals(Integer.toString(id))){
                s=a[1];
                    break;
                }
            }
            sc.close();
        }catch(IOException ioe){

        }
        return s;
    }
}
