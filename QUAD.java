/*
Goal: Implementation and comparsion of McAfee double auction, QUAD, and posted price mechanism for multi unit item. 

Author: Anjani Samhitha Jasti
	SCOPE, VIT-AP University

Date: 19.02.2022 	 
*/

import java.util.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.*;
public class QUAD {

    //Total number of buyers and sellers
    static int totalbuyers;
    static int totalsellers;

    //Buyers payment, Sellers payment and Total payment in McAfee
    static double mbuyerspayment;
    static double msellerspayment;
    static double totalMpayment;

    //Buyers payment, Sellers payment and Total payment in QUAD
    static double buyers1payment;
    static double sellers1payment;
    static double total1payment;

    //Buyers payment, Sellers payment and Total payment in benchmark mechanism PPM
    static double buyers2payment;
    static double sellers2payment;
    static double total2payment;

    //Accepted values in McAfee
    static int k1;
    
    //Difference of buyers and sellers price in three algorithms
    static double mcdiff1;
    static double cs1diff1;
    static double cs2diff1;

    //Number of total buyers and sellers
    static int traders;
    
    public static void main(String[] args) {
        Scanner sc =new Scanner(System.in);
        int n=600; // Upper bound on number of agents

        //The b values carry number of buyers in both groups of QUAD Algorithm
        int b1=0; //for QUAD
        int b2=0; //for QUAD
        int b3=0; //for PPM
        //MCAFEE
        // The below arrays are for storing buyers and sellers values for McAfee Algorithm
        double[] buyers=new double[n];
        double[] buyers2=new double[n];
        double[] sellers=new double[n];
        
        int s1,s2;

        //Buyers range
        double min=5;
        double max=25;  

        //The below Lists are for storing group 1 and group 2 values for QUAD
        ArrayList<Double> grpA=new ArrayList<Double>();
        ArrayList<Double> grpB=new ArrayList<Double>();

        //The below List is for storing group values for PPM
        ArrayList<Double> group=new ArrayList<Double>();

        //The below two loops store the buyers values in their respective data structures for three algorithms 
        for(int i=0;i<300;i++){    
            double randomValue =(int) ((Math.random() * (max - min)) + min);
            System.out.println("Value generated: " + randomValue);               
                grpA.add(randomValue);
                group.add(randomValue);
                buyers[i]=randomValue;
                buyers2[i]=buyers[i];
                b3++;
                b1++;           
        }
        for(int i=300;i<600;i++){ 
            double randomValue =(int) ((Math.random() * (max - min)) + min);
            System.out.println("Value generated: " + randomValue);               
            grpB.add(randomValue);
            group.add(randomValue);
            buyers[i]=randomValue;
            buyers2[i]=buyers[i];
            b2++;
        }  
        

        //Sellers Range
        min=3;
        max=30;

        //The below two loops store the buyers values in their respective data structures for three algorithms 
        System.out.println("Selling values are: ");
        for(int i=0;i<300;i++){    
            double randomValue =(int) ((Math.random() * (max - min)) + min);
            System.out.println("Value generated: " + randomValue);
                grpA.add(randomValue);
                group.add(randomValue);
                sellers[i]=randomValue;
            
        }
        for(int i=300;i<600;i++){ 
            double randomValue =(int) ((Math.random() * (max - min)) + min);
            System.out.println("Value generated: " + randomValue);               
            grpB.add(randomValue);
            group.add(randomValue);
            sellers[i]=randomValue;
        }

        //Sorting the buyers in descending order
        buyers=sortdes(buyers,0,(n-1));
        //Sorting the sellers in ascending order
        sellers=sortesc(sellers,0,(n-1));
        try {
            //Pairing the buyers and sellers after calculation
            pair(sellers, buyers,buyers2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        
        totalbuyers=b1+b2;
        totalsellers=(grpA.size()-b1)+(grpB.size()-b2);
        //Finding the equilibrium price of both groups in QUAD
        double equiprice1=calcequi(grpA,b1);
        double equiprice2=calcequi(grpB,b2);
        double equiprice3=calcequi(group,b3);

        //Comparing the equilibrium price of both groups in QUAD
        System.out.println("For Group A: ");
        finstep(grpA,b1,equiprice2);
        System.out.println("For Group B: ");
        finstep(grpB,b2,equiprice1);

        //Comparing the equilibrium price of group in PPM
        System.out.println("For group in second algorithm: ");
        finstep2(group,b3,equiprice3);

        System.out.println("Total buyers= "+totalbuyers);
        System.out.println("Total sellers= "+totalsellers);
        System.out.println("Total payment by buyers= "+buyers1payment);
        System.out.println("Total payment by sellers= "+sellers1payment);
        printfinal();
        plotting();
        
    }
    //The below method sorts the sellers in ascending order
    static double[] sortesc(double[] biddings,int low,int high){
        if(low<high){
            int pi=partition(biddings,low,high);
            sortesc(biddings,low,pi-1);
            sortesc(biddings,pi+1,high);
        }
        return biddings;
    }

    //The below method sorts the prices in descending order
    static double[] sortdes(double[] biddings,int low,int high){
        if(low<high){
            int pi=partitiondes(biddings,low,high);
            sortdes(biddings,low,pi-1);
            sortdes(biddings,pi+1,high);
        }
        return biddings;
    }

    //Implemented sorting algorithm- Merge Sort
    static int partition(double[] biddings,int low,int high){
        double pivot=biddings[high];
        int i=(low-1);
        for(int j=low;j<high;j++){
            if(biddings[j]<=pivot){
                i++;
                double temp=biddings[i];
                biddings[i]=biddings[j];
                biddings[j]=temp;
            }
        }
        double temp=biddings[i+1];
        biddings[i+1]=biddings[high];
        biddings[high]=temp;
        return i+1;
    }
    //Sorting in Descending Order
    static int partitiondes(double[] biddings,int low,int high){
        double pivot=biddings[high];
        int i=(low-1);
        for(int j=low;j<high;j++){
            if(biddings[j]>=pivot){
                i++;
                double temp=biddings[i];
                biddings[i]=biddings[j];
                biddings[j]=temp;
            }
        }
        double temp=biddings[i+1];
        biddings[i+1]=biddings[high];
        biddings[high]=temp;
        return i+1;
    }

    //Pairing the buyers and sellers value
    static void pair(double[] sellers,double[] buyers,double[] buyers2) throws IOException{
        int tmp=0;
        double avg=0;
        boolean flag=false;
        
        int size=0;
        if(buyers.length>sellers.length){
            size=sellers.length;
        }
        else{
            size=buyers.length;
        }
        for(int i=0;i<size;i++){
            if(buyers[i]<sellers[i]&&i!=0){
                avg=(buyers[i]+sellers[i])/2;
                if(avg<=buyers[i-1]&&avg>=sellers[i-1]){
                    flag=true;
                    k1=i;
                    break;
                }
                
            }
            else{
                flag=false;
            }
        }
        System.out.println("Accepted values: "+k1);
        double[] buyerstrue=new double[buyers.length];
        double Total_utility=0;
        if(flag==true){
            for(int i=0;i<k1;i++){
                buyerstrue[i]=avg;
                Total_utility+=Math.abs(buyers[i]-avg);
                System.out.println("Utility of "+buyers[i]+" is "+(buyers[i]-avg));
                sellers[i]=avg;
            }
            for(int i=k1+1;i<buyerstrue.length;i++){
                buyerstrue[i]=buyers[i];
            }
        }
        else{
            for(int i=0;i<buyerstrue.length;i++){
                buyerstrue[i]=buyers[i];
            } 
        }

        tmp=0;
        double sum=0;
        if(flag==true){
            for(int i=0;i<k1;i++){
                if(buyerstrue[i]>=sellers[i]){
                    mbuyerspayment+=buyerstrue[i];
                    msellerspayment+=sellers[i];
                        
                }
            }
        }
        else{
            for(int i=0;i<size;i++){
                if(buyerstrue[i]>=sellers[i]){
                   
                    mbuyerspayment+=buyerstrue[i];
                    msellerspayment+=sellers[i];
                   
                }
            }
        }
        totalMpayment=mbuyerspayment+msellerspayment;
        mcdiff1=Math.abs(mbuyerspayment-msellerspayment);
        System.out.println("MacAfee buyer payment: "+mbuyerspayment);
        System.out.println("MacAfee seller payment: "+msellerspayment);
        System.out.println("Total McAfee payment: "+totalMpayment);
    }

    //The below method for calculating the equivalent amount of group in QUAD algorithm
    public static double calcequi(ArrayList<Double> grp,int b){
        double theta=2;
       double price=0;
       int demand=0;
       int supply=0;
        double equiPrice=0;
        
       while(demand>=supply){
           demand=0;
           supply=0;
        for(int j=0;j<b;j++) {
            if(price<=grp.get(j)){
                demand++;
            }
             
        }
        System.out.println("Price is "+price);
        System.out.println("Demand is "+demand);
        for(int j=b;j<grp.size();j++){
            if(price>=grp.get(j)){
                supply++;
            }
            
        }
        System.out.println("Supply is "+supply);
        
        if(demand==supply){
            equiPrice=price;
            break;
        }
        price+=2;
        equiPrice=price;
       }
       
        System.out.println("Equilibrium price of group is "+equiPrice);
        return equiPrice;
    }

    //The below method is for pairing buyers and sellers in QUAD Algorithm
    public static void finstep(ArrayList<Double> grp,int b,double equip){
        double buyersprice=0;
        double sellersprice=0;
        int numofbuyers=0;
        int numofsellers=0;
        ArrayList<Double> buy=new ArrayList<Double>();
        ArrayList<Double> sell=new ArrayList<Double>();
        for(int i=0;i<b;i++){
            if(grp.get(i)>=equip)
            {
                System.out.println("Buyer "+i+" is ready to buy for "+grp.get(i));
                numofbuyers++;
                buyers1payment+=grp.get(i);
                buy.add(grp.get(i));
            }
             
        }
        for(int i=b;i<grp.size();i++){
            if(grp.get(i)<=equip){
                System.out.println("Seller "+i+" is ready to sell for "+grp.get(i));
                numofsellers++;
                sellers1payment+=grp.get(i);
                sell.add(grp.get(i));
            }

        }
        cs1diff1=Math.abs(buyers1payment-sellers1payment);
        total1payment=buyers1payment+sellers1payment;
        System.out.println("Payment by buyers in second algorithm is "+buyers1payment);
        System.out.println("Payment by sellers in second algorithm is "+sellers1payment);
        System.out.println("Total payment in second algorithm is "+total1payment);
        traders=totalbuyers+totalsellers;
    }
    public static void finstep2(ArrayList<Double> grp,int b,double equip){
        double buyersprice=0;
        double sellersprice=0;
        int numofbuyers=0;
        int numofsellers=0;

        //The below lists are for storing final accepted bid values of buyers and sellers
        ArrayList<Double> buy=new ArrayList<Double>();
        ArrayList<Double> sell=new ArrayList<Double>();

        //Comparing the equilibrium price with buyers
        for(int i=0;i<b;i++){
            if(grp.get(i)>=equip)
            {
                System.out.println("Buyer "+i+" is ready to buy for "+grp.get(i));
                numofbuyers++;
                buyers2payment+=grp.get(i);
                buy.add(grp.get(i));
            }
             
        }
        
        //Comparing the equilibrium price with sellers
        for(int i=b;i<grp.size();i++){
            if(grp.get(i)<=equip){
                System.out.println("Seller "+i+" is ready to sell for "+grp.get(i));
                numofsellers++;
                sellers2payment+=grp.get(i);
                sell.add(grp.get(i));
            }

        }
        cs2diff1=Math.abs(buyers2payment-sellers2payment);
        total2payment=buyers2payment+sellers2payment;
        System.out.println("Payment by buyers in second algorithm is "+buyers2payment);
        System.out.println("Payment by sellers in second algorithm is "+sellers2payment);
        System.out.println("Total payment in second algorithm is "+total2payment);
        traders=totalbuyers+totalsellers;
    }
    public static void plotting(){
        try{
            File file =new File("largedata1.txt");
            FileWriter fw = new FileWriter(file,true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);        

            if(!file.exists()){
               file.createNewFile();
            }
            //Writing the required values into the file.
                pw.println(totalbuyers+" "+totalsellers+" "+mbuyerspayment+" "+msellerspayment+" "+totalMpayment+" "+buyers1payment+" "+sellers1payment+" "+total1payment+" "+buyers2payment+" "+sellers2payment+" "+total2payment+" "+traders+" "+mcdiff1+" "+cs1diff1+" "+cs2diff1);
                pw.close();
           
         }
         catch(IOException e){
            System.out.println("An error occurred while creating");
            e.printStackTrace();;
        }
         
    }
    public static void printfinal(){
        System.out.println("-------------FINAL VALUES------------");
        System.out.println("MCAFEE");
        System.out.println("Buyer payment-"+mbuyerspayment+" Seller Payment-"+msellerspayment+" Total payment-"+totalMpayment+" ");
        System.out.println("Crowd Sourcing");
        System.out.println("Buyer payment-"+buyers1payment+" Seller Payment-"+sellers1payment+" Total buyer payment-"+total1payment);
        System.out.println("Difference Values--");
        System.out.println("mcafee- "+mcdiff1);
        System.out.println("cs1- "+cs1diff1);
        System.out.println("cs2- "+cs2diff1);
        
    }
}
