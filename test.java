import java.sql.Connection;

public class test {
    public static void main(String[] args) {
        try(Connection connection = DB.getConnection()){

        }
        catch(Exception e){
            System.out.println(e);
        }
    }
}
