package awedoctime;

public class CreateID {
    private static int PCounter = 0;
    private static int SCounter = 0;
    
    public static String P(){
        PCounter++;
        return "P" + PCounter;
        
        
    }
    
    public static String S(){
        SCounter++;
        return "S" + SCounter;
    }
}
