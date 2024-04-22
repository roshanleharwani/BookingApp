import java.sql.*;
import java.util.Scanner;
public class BookingSystem{
    static Scanner input = new Scanner(System.in);
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
    static public boolean isCustomerExist(String name) {
        try (Connection connection = DB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(Query.isExitsInBooking);
            statement.setString(1, name);
            ResultSet result = statement.executeQuery();
            result.next();
            if (result.getInt(1) >= 1) {
                return true;
            } else {
                PreparedStatement statement1 = connection.prepareStatement(Query.isExistsInCancellation);
                statement1.setString(1, name);
                ResultSet result1 = statement1.executeQuery();
                result1.next();
                return (result1.getInt(1) >= 1);
            }
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }
    public static void bookedTransaction(String customerName, Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(Query.bookedTransactions);
        statement.setString(1, customerName);
        ResultSet result = statement.executeQuery();

        // Check if there are any booked transactions
        if (result.next()) {
            System.out.println("---------------------------Booked Transactions---------------------------");
            System.out.printf("%-10s%-15s%-15s%-15s%-10s%n", "BookingID", "EventID", "BookingDate", "TicketBooked", "Price");

            // Print booked transactions
            do {
                int bookingId = result.getInt(1);
                int eventId = result.getInt(2);
                String bookingDate = result.getString(4);
                int ticketBooked = result.getInt(5);
                double price = result.getDouble(6);
                System.out.printf("%-10s%-15s%-15s%-15s$%-10s%n", bookingId, eventId, bookingDate, ticketBooked, price);
            } while (result.next());
        }
        result.close();
    }

    public static void cancelledTransaction(String customerName, Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(Query.cancelledTransactions);
        statement.setString(1, customerName);
        ResultSet result = statement.executeQuery();

        // Check if there are any cancelled transactions
        if (result.next()) {
            System.out.println("-------------------------Cancelled Transactions-------------------------");
            System.out.printf("%-10s%-15s%-15s%-15s%-10s%n", "CancelID", "EventID", "CancelDate", "Cancelled", "Price");

            // Print cancelled transactions
            do {
                int cancelId = result.getInt(1);
                String cancelDate = result.getString(3);
                int ticketCancelled = result.getInt(4);
                double price = result.getDouble(5);
                System.out.printf("%-10s%-15s%-15s%-15s$%-10s%n", cancelId, "N/A", cancelDate, ticketCancelled, price);
            } while (result.next());
        }
        result.close();
    }

    public static void lastTransaction() {
        System.out.println("--------------------------Last Transaction---------------------------");
        Scanner input = new Scanner(System.in); // Define Scanner object
        System.out.print("Enter Customer Name: ");
        String customerName = input.nextLine();
        boolean customerExists = isCustomerExist(customerName); // Check if customer exists

        if (customerExists) {
            try (Connection connection = DB.getConnection()) {
                bookedTransaction(customerName, connection);
                cancelledTransaction(customerName, connection);
            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            System.out.println("Customer does not exist.");
        }
    }
    public static boolean isEventExist(int eventID, Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(Query.isEvent);
        statement.setInt(1, eventID);
        ResultSet result = statement.executeQuery();
        result.next();
        if (result.getInt(1) == 1) {
            return true;
        }
        return false;
    }
    public static double fetchPrice(int eventID, Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(Query.price);
        statement.setInt(1, eventID);
        ResultSet result = statement.executeQuery();
        result.next();
        return result.getDouble(1);
    }
    public static void BookTicket(){
        System.out.println("--------------------------Book Ticket---------------------------");
        System.out.print("Enter Event ID: ");
        int eventID = input.nextInt();
        input.nextLine();
        System.out.print("Enter Customer Name: ");
        String customerName = input.nextLine();
        System.out.println("Enter Number of Tickets: ");
        int ticket = input.nextInt();
        try(Connection connection = DB.getConnection()){
            if(isEventExist(eventID, connection)){
                double price = fetchPrice(eventID, connection)*ticket;
                PreparedStatement statement = connection.prepareStatement(Query.bookTicket);
                statement.setInt(1, eventID);
                statement.setString(2, customerName);
                statement.setInt(3, ticket);
                statement.setDouble(4, price);
                statement.executeUpdate();
                System.out.println();
                System.out.println("Ticket Booked Successfully");
                System.out.println("FOR EVENT ID: " + eventID);
                System.out.println("Customer Name: " + customerName);
                System.out.println("Number of Tickets: " + ticket);
                System.out.println("Total Amount: $" + price);
            }
            else{
                System.out.println("Event does not exist.");
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }


}