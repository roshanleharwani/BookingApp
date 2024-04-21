import java.sql.*;
import java.util.Scanner;
public class BookingSystem{
    Scanner input = new Scanner(System.in);
    public static void menu(){
        System.out.println("----------------------------Book Your Show----------------------------");
        System.out.println("1. Main Menu");
        System.out.println("2. Show Upcoming Events");
        System.out.println("3. Show My Previous Transactions");
        System.out.println("4. Exit");
    }
    public static void mainMenu(){
        System.out.println("---------------------------Main Menu---------------------------");
        System.out.println("1. Book Ticket");
        System.out.println("2. Cancel Booking");
        System.out.println("3. Refund Status");
        System.out.println("4. Events");
        System.out.println("5. Exit");
    }
    public static void showUpcomingEvents() {
        System.out.println("---------------------------Upcoming Events ---------------------------");
        try (Connection connection = DB.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(Query.fetchEvents);

            System.out.printf("%-10s%-20s%-15s%-30s%-10s%n", "EventID", "Name", "Date", "Venue", "Price");

            while (result.next()) {
                int eventId = result.getInt(1);
                String name = result.getString(2);
                Date date = result.getDate(3);
                String venue = result.getString(4);
                double price = result.getDouble(6);
                System.out.printf("%-10d%-20s%-15s%-30s%-10.2f%n", eventId, name, date, venue, price);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void lastTransaction(){

        System.out.println("--------------------------Last Transaction---------------------------");
        System.out.println("Enter Customer Name: ");
        String customerName = input.next();
        try(Connection connection = DB.getConnection()){

        }
        catch(Sql e){
            System.out.println(e);
        }
    }
}