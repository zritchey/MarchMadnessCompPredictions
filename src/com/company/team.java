package com.company;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
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
                    int weight = (int)Math.pow(1.5,(Double.parseDouble(a[0]) - start));///I expect the weight of the wins to make a large difference in precision
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
        double WWinrate=0.5;
        if(weightedGames!=0)
            WWinrate=weightedWins/(double)weightedGames;
        double otwinrate=0.5;
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

    public ArrayList<Integer> peep=new ArrayList<>();
    //give compact results a 1/5 weight in final winScore calculation
    public void setStats()throws IOException{
        double[]detailTour=statReader("TourneyDetailedResults.csv");

        double[]generalTour=statReader("TourneyCompactResults.csv");

        double[]detailSeason=statReader("RegularSeasonDetailedResults.csv");

        double[]generalSeason=statReader("RegularSeasonCompactResults.csv");

        double comp[]=new double [generalTour.length];
        double hold[]={generalTour[3],detailTour[3],generalSeason[3],detailSeason[3]};

        for(int i=0;i<comp.length;i++){
            int n=0;
            double total=0;

            if(generalSeason[i]!=0){
                int ln=(int)Math.ceil(Lines("RegularSeasonCompactResults.csv")/1000);
                total+=ln*generalSeason[i];
                n+=ln;
            }
            if(generalTour[i]!=0){
                int ln=(int)Math.ceil(Lines("TourneyCompactResults.csv")/1000);
                total+=ln*generalTour[i];
                n+=ln;
            }
            if(detailSeason[i]!=0){
                int ln=(int)Math.ceil(Lines("RegularSeasonDetailedResults.csv")/1000);
                total+=ln*detailSeason[i];
                n+=ln;
            }
            if(detailTour[i]!=0){
                int ln=(int)Math.ceil(Lines("TourneyDetailedResults.csv")/1000);
                total+=ln*detailTour[i];
                n+=ln;
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
        print();
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

    public double compare(team t2){
        double diff=Math.log(0.5+Math.abs(this.winRate-t2.winRate));

        if (this.winRate<t2.winRate){
            diff*=-1;
        }
        if (this.avgPts+this.winPts*this.winRate<t2.avgPts+t2.winPts*t2.winRate)
            diff-=Math.log(1+0.2*Math.abs(diff));
        else
            diff+=Math.log(1+0.2*Math.abs(diff));
        if ((Math.abs(this.avgPts-t2.avgPts)<5)){
            int chance=(int)(100*((this.overtimeGameRate+t2.overtimeGameRate)/2));


            if(overtimeWinRate*overtimeWinPts<t2.overtimeWinRate*t2.overtimeWinPts)
                diff-=Math.log(1+0.075*Math.abs(overtimeWinRate-t2.overtimeWinRate));
            else
                diff+=Math.log(1+0.075*Math.abs(overtimeWinRate-t2.overtimeWinRate));

        }

        if(diff>0.45)
            diff=0.45;
        if(diff<-0.45)
            diff=-0.45;

        return 0.5+diff;
    }

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
    public int Lines(String path){
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
          return ln;
    }

    public void print(){
        System.out.println("\n\n\nTeam:"+name+"\nWin Record:"+winRate+"%\nAverage Winning points:"+winPts+"\nAverage losing points: "+lossPts+"\nAverage points scored:"+avgPts+
        "\nOvertime Win Record:"+overtimeWinRate+"%\nOvertime Game Rate:"+overtimeGameRate+"%\nOvertime Winning Points Scored:"+overtimeWinPts+"\nOvertime Losing Points Scored:"+overtimeLossPts+
                "\nAverage Overtime Points Scored:"+avgOvertimePts);
    }
}