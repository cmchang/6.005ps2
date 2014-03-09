package awedoctime;
import java.util.Date;

public class CreateID {
    public static String P(){
        java.util.Date date= new java.util.Date();
        return "P"+date.getTime();
    }
    
    public static String S(){
        java.util.Date date= new java.util.Date();
        return "S"+date.getTime();
    }
}
