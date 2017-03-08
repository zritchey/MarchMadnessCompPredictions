package com.company;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.Double.NaN;


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

    public double[] statReader(String path)throws IOException {
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
                    int weight = (int)Math.exp((Double.parseDouble(a[0]) - start));
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
        double winNum=0;
        if(winGames!=0)
            winNum=pointWin/(double)winGames;
        double lossNum=0;
        if(lossGames!=0)
            lossNum=pointLoss/(double)lossGames;
        double avgNum=0;
        if(!(winGames==0&&lossGames==0))
            avgNum=(pointLoss+(double)pointWin)/(winGames+(double)lossGames);
        double WWinrate=0;
        if(weightedGames!=0)
            WWinrate=weightedWins/(double)weightedGames;
        double otwinrate=0;
        if(otGames!=0)
            otwinrate=otWin/(double)otGames;
        double otwinNum=0;
        if(otWin!=0)
            otwinNum=otwinPts/(double)otWin;
        double otlossNum=0;
        if(otLoss!=0)
            otlossNum=otlossPts/(double)otLoss;
        double avgotNum=0;
        if(!(otWin==0&&otLoss==0))
            avgotNum=(otwinPts+(double)otlossPts)/(otWin+(double)otLoss);
        double otRate=0;
        if(!(winGames==0&&lossGames==0))
            otRate=otGames/(winGames+lossGames);

        //winPts,lossPts,avgPts,winRate,overtimeWinRate,overtimeWinPts,overtimeLossPts,avgOvertimePts
        double[]stats={winNum,lossNum,avgNum,WWinrate,otwinrate,otwinNum,otlossNum,avgotNum,otRate};
        return stats;
    }

    //give compact results a 1/5 weight in final winScore calculation
    public void setStats()throws IOException{
        double[]detailTour=statReader("TourneyDetailedResults.csv");
        System.out.println("tourney details done "+id+"_"+name);
        double[]generalTour=statReader("TourneyCompactResults.csv");
        System.out.println("tourney compact done "+id+"_"+name);
        double[]detailSeason=statReader("RegularSeasonDetailedResults.csv");
        System.out.println("Season details done "+id+"_"+name);
        double[]generalSeason=statReader("RegularSeasonCompactResults.csv");
        System.out.println("tourney details done "+id+"_"+name);
        double comp[]=new double [generalTour.length];
        double hold[]=new double [4];

        for(int i=0;i<comp.length;i++){
            int n=0;
            double total=0;
            if(generalSeason[i]!=0){
                total+=145*generalSeason[i];
                n+=145;
            }
            if(generalTour[i]!=0){
                total+=2*generalTour[i];
                n+=2;
            }
            if(detailSeason[i]!=0){
                total+=70*detailSeason[i];
                n+=70;
            }
            if(detailTour[i]!=0){
                total+=detailTour[i];
                n++;
            }
            if (n==0)
                n++;
            comp[i]=total/n;
        }
        winPts=comp[0];
        lossPts=comp[1];
        avgPts=comp[2];
        winRate=comp[3];
        overtimeWinRate=comp[4];
        overtimeWinPts=comp[5];
        overtimeLossPts=comp[6];
        avgOvertimePts=comp[7];
        overtimeGameRate=comp[8];

    }
    private double winPts;
    private double lossPts;
    private double avgPts;
    private double winRate;
    private double overtimeGameRate;
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
    public int tLines(String path){
        int ln=0;
        try {
            Scanner sc = new Scanner(new File(path));
            sc.nextLine();
            while(sc.hasNextLine()){
                ln++;
                sc.nextLine();
            }
            sc.close();
        }
        catch(IOException ioe){
            System.out.println("ERROR:"+ioe+"\nCould not read File:"+path);
        }
          return (int)Math.ceil((double)ln/1000);
    }

    public void print(){
        System.out.println("\n\n\nTeam:"+name+"\nWin Record:"+winRate+"%\nAverage Winning points:"+winPts+"\nAverage losing points: "+lossPts+"\nAverage points scored:"+avgPts+
        "\nOvertime Win Record:"+overtimeWinRate+"%\nOvertime Game Rate:"+overtimeGameRate+"%\nOvertime Winning Points Scored:"+overtimeWinPts+"\nOvertime Losing Points Scored:"+overtimeLossPts+
                "\nAverage Overtime Points Scored:"+avgOvertimePts);
    }
}