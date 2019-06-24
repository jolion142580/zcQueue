package com.yiko;

import java.util.*;
import java.math.BigDecimal;
public class main {
    public String cid;
    public String DC;
    public String BEEF;
    public String CS;
    public String STRAW;
    public String EGG2;
    public double DC1;
    public double BEEF1;
    public double CS1;
    public double STRAW1;
    public double EGG21;
    public double TotalWeight;
    public double TotalWeight1;
    public double TotalCost;
    public main() {
        String menuOpt="";
   Scanner in = new Scanner(System.in);
   System.out.println("Menu Assignment 2018 S2");
   System.out.println("Warehouse Program Part 3(food)");
   do
   {
    System.out.println();
    System.out.printf("Select Your Option:\n");
    System.out.printf("C - Company Table\n");
    System.out.printf("F - Food Table\n");
    System.out.printf("QU - Generate Quote\n");
    System.out.printf("QF - Generate Quote To filesSelcet Option:\n");
    System.out.printf("Q - Quit\n");
    System.out.println();
    menuOpt = in.nextLine();      
            System.out.println("You entered : " + menuOpt);
    if (menuOpt.compareToIgnoreCase("C") == 0 ) {opta1();}
    if (menuOpt.compareToIgnoreCase("F") == 0 ) {opta2();}
    if (menuOpt.compareToIgnoreCase("QU") == 0 ) {opta3();}
    if (menuOpt.compareToIgnoreCase("QF") == 0 ) {opta4();}
} while (menuOpt.compareToIgnoreCase("Q")!=0); // Note the != this time 
System.out.printf("\nEnding Now\n");
}
public void opta1(){
System.out.println("Company Id |Full Name           |Pay Tax  |Pickup Bay Id|");
System.out.println("|----------+--------------------+---------+-------------|");
System.out.println("|BFSC      |Blue Fish Space Co  |No       |MERCY        |");
System.out.println("|ECP       |Elon Cannon Personal|Yes      |KIT          |");
System.out.println("|NAASA     |BAASA               |Yes      |MERCY        |");
System.out.println("|AARG      |AARG                |Yes      |KAT          |");
System.out.println("|PUB       |General Public      |Yes      |MAIL         |");
System.out.println("|----------+--------------------+---------+-------------|");
    }
public void opta2()
{
System.out.println("|Meal Description |ID     |Weight(grams)|Cost       |");
System.out.println("|-----------------+-------+-------------+-----------|");
System.out.println("|Date cake        |DC     |134          |$59        |");
System.out.println("|Beef sandwitch   |BEEF   |164          |$49.50     |");
System.out.println("|Cheese sandwitch |CS     |206          |$93.70     |");
System.out.println("|Strawberry desert|STRAW  |177          |$157.05    |");
System.out.println("|Scrambled eggs   |EGG2   |112          |$77.15     |");
System.out.println("|-----------------+-------+-------------+-----------|");
    }
public void opta3()
{System.out.print("Enter Ordering Company ID:");
        Scanner scan = new Scanner(System.in);
        if (scan.hasNextLine()) {
        cid = scan.nextLine();
        cid = cid.toLowerCase();
        while (!cid.equals("naasa")&&!cid.equals("bfsc")&&!cid.equals("ecp")&&!cid.equals("arrg")&&!cid.equals("pub")){
            System.out.println("ERROR");
            System.out.println("Please Enter a valid Company Code");
            cid = scan.nextLine();
        }
        if (cid.equals("bfsc")){
          System.out.print("Company Code is: BFSC");
          System.out.println();
        }
        if (cid.equals("ecp")){
          System.out.print("Company Code is: ECP");
          System.out.println();
        }
                  if (cid.equals("naasa")){
          System.out.print("Company Code is: NAASA");
          System.out.println();
        }
        if (cid.equals("aarg")){
          System.out.print("Company Code is: AARG");
          System.out.println();
        }
        if (cid.equals("pub")){
          System.out.print("Company Code is: PUB");
          System.out.println();
        }
        System.out.println("Please enter number of DC Date Cake");
        if (scan.hasNextLine()) {
        DC = scan.nextLine();
        while (!DC.equals("0")&&!DC.equals("1")&&!DC.equals("2")&&!DC.equals("3")&&!DC.equals("4")&&!DC.equals("5")&&!DC.equals("6")&&!DC.equals("7")&&!DC.equals("8")&&!DC.equals("9")&&!DC.equals("10")&&!DC.equals("11")&&!DC.equals("12")&&!DC.equals("13")&&!DC.equals("14")&&!DC.equals("15")&&!DC.equals("16")&&!DC.equals("17")&&!DC.equals("18")&&!DC.equals("19")&&!DC.equals("20")&&!DC.equals("21")&&!DC.equals("22")&&!DC.equals("23")&&!DC.equals("24")){
            System.out.println("ERROR");
            System.out.println("Please Enter a valid NUMBER of DC Date Cake");
            DC = scan.nextLine();
        }
        DC1 = Double.parseDouble(DC);
        }
        System.out.println("Please enter number of BEEF Beef Sandwitch");
        if (scan.hasNextLine()) {
        BEEF = scan.nextLine();
        while (!BEEF.equals("0")&&!BEEF.equals("1")&&!BEEF.equals("2")&&!BEEF.equals("3")&&!BEEF.equals("4")&&!BEEF.equals("5")&&!BEEF.equals("6")&&!BEEF.equals("7")&&!BEEF.equals("8")&&!BEEF.equals("9")&&!BEEF.equals("10")&&!BEEF.equals("11")&&!BEEF.equals("12")&&!BEEF.equals("13")&&!BEEF.equals("14")&&!BEEF.equals("15")&&!BEEF.equals("16")&&!BEEF.equals("17")&&!BEEF.equals("18")&&!BEEF.equals("19")&&!BEEF.equals("20")&&!BEEF.equals("21")&&!BEEF.equals("22")&&!BEEF.equals("23")&&!BEEF.equals("24")){
            System.out.println("ERROR");
            System.out.println("Please Enter a valid NUMBER of BEEF Beef Sandwitch");
            BEEF = scan.nextLine();
        }
        BEEF1 = Double.parseDouble(BEEF);
        }
        System.out.println("Please enter number of CS Cheese sandwitch");
        if (scan.hasNextLine()) {
        CS = scan.nextLine();
        CS1 = Double.parseDouble(CS);
        while (!CS.equals("0")&&!CS.equals("1")&&!CS.equals("2")&&!CS.equals("3")&&!CS.equals("4")&&!CS.equals("5")&&!CS.equals("6")&&!CS.equals("7")&&!CS.equals("8")&&!CS.equals("9")&&!CS.equals("10")&&!CS.equals("11")&&!CS.equals("12")&&!CS.equals("13")&&!CS.equals("14")&&!CS.equals("15")&&!CS.equals("16")&&!CS.equals("17")&&!CS.equals("18")&&!CS.equals("19")&&!CS.equals("20")&&!CS.equals("21")&&!CS.equals("22")&&!CS.equals("23")&&!CS.equals("24")){
            System.out.println("ERROR");
            System.out.println("Please Enter a valid NUMBER of CS Cheese sandwitch");
            CS = scan.nextLine();
        }
        }
        System.out.println("Please enter number of STRAW Strawberry Desert");
        if (scan.hasNextLine()) {
        STRAW = scan.nextLine();
        STRAW1 = Double.parseDouble(STRAW);
        while (!STRAW.equals("0")&&!STRAW.equals("1")&&!STRAW.equals("2")&&!STRAW.equals("3")&&!STRAW.equals("4")&&!STRAW.equals("5")&&!STRAW.equals("6")&&!STRAW.equals("7")&&!STRAW.equals("8")&&!STRAW.equals("9")&&!STRAW.equals("10")&&!STRAW.equals("11")&&!STRAW.equals("12")&&!STRAW.equals("13")&&!STRAW.equals("14")&&!STRAW.equals("15")&&!STRAW.equals("16")&&!STRAW.equals("17")&&!STRAW.equals("18")&&!STRAW.equals("19")&&!STRAW.equals("20")&&!STRAW.equals("21")&&!STRAW.equals("22")&&!STRAW.equals("23")&&!STRAW.equals("24")){
            System.out.println("ERROR");
            System.out.println("Please Enter a valid NUMBER of STRAW Strawberry Desert");
            STRAW = scan.nextLine();
        }
        }
        System.out.println("Please enter number of EGG2 Scrambled Eggs");
        if (scan.hasNextLine()) {
        EGG2 = scan.nextLine();
        EGG21 = Double.parseDouble(EGG2);
        while (!EGG2.equals("0")&&!EGG2.equals("1")&&!EGG2.equals("2")&&!EGG2.equals("3")&&!EGG2.equals("4")&&!EGG2.equals("5")&&!EGG2.equals("6")&&!EGG2.equals("7")&&!EGG2.equals("8")&&!EGG2.equals("9")&&!EGG2.equals("10")&&!EGG2.equals("11")&&!EGG2.equals("12")&&!EGG2.equals("13")&&!EGG2.equals("14")&&!EGG2.equals("15")&&!EGG2.equals("16")&&!EGG2.equals("17")&&!EGG2.equals("18")&&!EGG2.equals("19")&&!EGG2.equals("20")&&!EGG2.equals("21")&&!EGG2.equals("22")&&!EGG2.equals("23")&&!EGG2.equals("24")){
            System.out.println("ERROR");
            System.out.println("Please Enter a valid NUMBER of EGG2 Scrambled Eggs");
            EGG2 = scan.nextLine();
        }
        }
        System.out.println("|----------------------------------------------------------|");
        if (cid.equals("bfsc")){
          System.out.print("|Quotation For BFSC  Blue Fish Space Co(pack at dock MERCY)|");
          System.out.println();
        }
        if (cid.equals("ecp")){
          System.out.print("|Quotation For ECP   Elon Cannon Personal(pack at dock KIT)|");
          System.out.println();
        }
                  if (cid.equals("naasa")){
          System.out.print("|Quotation For NAASA              NAASA(pack at dock MERCY)|");
          System.out.println();
        }
        if (cid.equals("aarg")){
          System.out.print("|Quotation For AARG                     (pack at dock KAT) |");
          System.out.println();
        }
        if (cid.equals("pub")){
          System.out.print("|Quotation For PUB        General Public(pack at dock MAIL)|");
          System.out.println();
        }
        System.out.println("|----------------------------------------------------------|");
        System.out.printf("|Total Weight (G):");
        DC1 = Double.parseDouble(DC);
        BEEF1 = Double.parseDouble(BEEF);
        CS1 = Double.parseDouble(CS);
        STRAW1 = Double.parseDouble(STRAW);
        EGG21 = Double.parseDouble(EGG2);
        TotalWeight = EGG21*112+STRAW1*177+CS1*206+BEEF1*164+DC1*134;
        System.out.print(TotalWeight);
        System.out.println();
        System.out.printf("|Billing Weight (KG):");
        TotalWeight1 = 0.001*TotalWeight;
        BigDecimal BillingWeight = new BigDecimal(TotalWeight1).setScale(0,BigDecimal.ROUND_UP);
        System.out.print(BillingWeight);
        System.out.println();
        System.out.printf("|Total Food Cost:");
        TotalCost = EGG21*77.15+STRAW1*157.05+CS1*93.70+BEEF1*49.50+DC1*59;
        System.out.printf("%.2f",TotalCost);
        System.out.println();
        System.out.printf("|Packing Cost:");
        BigDecimal over15 = new BigDecimal("0");
        BigDecimal overweight = new BigDecimal("457");
        BigDecimal orbit = new BigDecimal("18127");
        BigDecimal Costorbit = new BigDecimal("0");
        if(TotalWeight*0.001 <= 5){
            System.out.print("3192");
        }
        if((TotalWeight*0.001 > 5)&&(TotalWeight*0.001 < 15)){
            System.out.print("6823.50");
        }
        if(TotalWeight*0.001 > 15){
            over15 = BillingWeight.multiply(overweight);
            System.out.print(over15);
        }
        System.out.println();
        System.out.printf("|To Orbit Cost:");
        Costorbit = BillingWeight.multiply(orbit);
        System.out.print(Costorbit);
        System.out.println();
        System.out.printf("|Total Meal Cost:");
        BigDecimal TotalCostBD= new BigDecimal(TotalCost);
        BigDecimal Totalmealcost = null;
        BigDecimal less5 = new BigDecimal("3192");
        BigDecimal over5 = new BigDecimal("6823.50");
        if(TotalWeight*0.001 <= 5){
            Totalmealcost = TotalCostBD.add(Costorbit).add(less5);
            BigDecimal Totalmealcost1 = Totalmealcost.setScale(2,BigDecimal.ROUND_DOWN);
            System.out.print(Totalmealcost1);
        }
        if((TotalWeight*0.001 > 5)&&(TotalWeight*0.001 < 15)){
            Totalmealcost = TotalCostBD.add(Costorbit).add(over5);
            BigDecimal Totalmealcost1 = Totalmealcost.setScale(2,BigDecimal.ROUND_DOWN);
            System.out.print(Totalmealcost1);
        }
        if(TotalWeight*0.001 > 15){
            over15 = BillingWeight.multiply(overweight);
            Totalmealcost = TotalCostBD.add(Costorbit).add(over15);
            BigDecimal Totalmealcost1 = Totalmealcost.setScale(2,BigDecimal.ROUND_DOWN);
            System.out.print(Totalmealcost1);
        }
        
        System.out.println();
        
    }
    }
public void opta4()
{
    }
}
