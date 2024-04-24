import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class BookTheShow implements BookingSystem {
    static Scanner input = new Scanner(System.in);
    public static void menu(){
        System.out.println();
        System.out.println("----------------------------Book Your Show----------------------------");
        System.out.println("1. Main Menu");
        System.out.println("2. Show Upcoming Events");
        System.out.println("3. Show My Previous Transactions");
        System.out.println("4. Exit");
    }
    public static void mainMenu(){
        System.out.println();
        System.out.println("---------------------------Main Menu---------------------------");
        System.out.println("1. Book Ticket");
        System.out.println("2. Cancel Booking");
        System.out.println("3. Refund Status");
        System.out.println("4. Events");
        System.out.println("5. Exit");
    }
    public static void getUpcomingEvents() {
        System.out.println("---------------------------Upcoming Events ---------------------------");
        try (Connection connection = DB.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(Query.getEvents);

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
            PreparedStatement statement = connection.prepareStatement(Query.isCustomerBookingExist);
            statement.setString(1, name);
            ResultSet result = statement.executeQuery();
            result.next();
            if (result.getInt(1) >= 1) {
                return true;
            } else {
                PreparedStatement statement1 = connection.prepareStatement(Query.isCancellationExist);
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
    public static void getBookedTransaction(String customerName, Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(Query.bookedTransactions);
        statement.setString(1, customerName);
        ResultSet result = statement.executeQuery();
        System.out.println();
        // Check if there are any booked transactions
        if (result.next()) {
            System.out.println("---------------------------Booked Transactions---------------------------");
            System.out.printf("%-10s%-15s%-15s%-15s%-10s%n", "BookingID", "EventID", "BookingDate", "TicketBooked", "Price");
            System.out.println();
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

    public static void getCancelledTransaction(String customerName, Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(Query.cancelledTransactions);
        statement.setString(1, customerName);
        ResultSet result = statement.executeQuery();
        System.out.println();
        // Check if there are any cancelled transactions
        if (result.next()) {
            System.out.println("-------------------------Cancelled Transactions-------------------------");
            System.out.printf("%-10s%-15s%-15s%-15s%-10s%n", "CancelID", "EventID", "CancelDate", "Cancelled", "Price");
            System.out.println();
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

    public static void getLastTransaction() {
        System.out.println("--------------------------Last Transaction---------------------------");
        Scanner input = new Scanner(System.in); // Define Scanner object
        System.out.print("Enter Customer Name: ");
        String customerName = input.nextLine();
        boolean customerExists = isCustomerExist(customerName); // Check if customer exists

        if (customerExists) {
            try (Connection connection = DB.getConnection()) {
                getBookedTransaction(customerName, connection);
                getCancelledTransaction(customerName, connection);
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
        return result.getInt(1) == 1;
    }
    public static double fetchPrice(int eventID, Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(Query.price);
        statement.setInt(1, eventID);
        ResultSet result = statement.executeQuery();
        result.next();
        return result.getDouble(1);
    }
    public static int getTickets(int eventID, Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(Query.totalTickets);
        statement.setInt(1, eventID);
        ResultSet result = statement.executeQuery();
        result.next();
        return result.getInt(1);
    }
    public static int getBookedTickets(int bookingId, Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(Query.bookedTickets);
        statement.setInt(1, bookingId);
        ResultSet result = statement.executeQuery();
        result.next();
        return result.getInt(1);
    }
    public static void updateTickets(int eventID,int updatedQty, Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(Query.updateTable);
        statement.setInt(1,updatedQty );
        statement.setInt(2, eventID);
        statement.executeUpdate();
    }
    public static  boolean isBookingExist(int bookingID, Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(Query.isBookingExist);
        statement.setInt(1, bookingID);
        ResultSet result = statement.executeQuery();
        result.next();
        return result.getInt(1) == 1;
    }
    public static int fetchEventId(int bookingID, Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(Query.eventId);
        statement.setInt(1, bookingID);
        ResultSet result = statement.executeQuery();
        result.next();
        return result.getInt(1);
    }
    public static boolean isCancelled(int cancelId, Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(Query.isCancelled);
        statement.setInt(1, cancelId);
        ResultSet result = statement.executeQuery();
        result.next();
        return result.getInt(1) == 1;
    }
    public static void bookTicket(){
        System.out.println("--------------------------Book Ticket---------------------------");
        System.out.print("Enter Event ID: ");
        int eventID = input.nextInt();
        input.nextLine();
        System.out.print("Enter Customer Name: ");
        String customerName = input.nextLine();
        System.out.print("Enter Number of Tickets: ");
        int ticket = input.nextInt();
        try(Connection connection = DB.getConnection()){
            if(isEventExist(eventID, connection)){
                double price = fetchPrice(eventID, connection)*ticket;
                PreparedStatement statement = connection.prepareStatement(Query.bookTicket);
                statement.setInt(1, eventID);
                statement.setString(2, customerName);
                statement.setInt(3, ticket);
                statement.setDouble(4, price);
                int availableTicket = getTickets(eventID, connection);
                if(ticket<= availableTicket){
                    connection.setAutoCommit(false);
                    statement.executeUpdate();
                    updateTickets(eventID, availableTicket-ticket, connection);
                    connection.commit();
                    System.out.println();
                    System.out.println("Ticket Booked Successfully");
                    System.out.println("FOR EVENT ID: " + eventID);
                    System.out.println("Customer Name: " + customerName);
                    System.out.println("Number of Tickets: " + ticket);
                    System.out.println("Total Amount: $" + price);
                }
                else{
                    System.out.println("Required Tickets are not available");
                    System.out.println("Available Tickets: " + availableTicket);
                }
            }
            else{
                System.out.println("Event Not Found.");
            }
        }
        catch (Exception e){
            System.out.println("Something went wrong");
        }
    }
    public static void cancelTicket() {
        Connection connection = null;
        try {
            System.out.print("Enter Booking ID: ");
            int bookingId = input.nextInt();
            System.out.print("Enter Number Of Tickets: ");
            int tickets = input.nextInt();

            try {
                connection = DB.getConnection();
                connection.setAutoCommit(false);

                if (isBookingExist(bookingId, connection)) {
                    int event_id = fetchEventId(bookingId, connection);
                    double refundAmount = fetchPrice(event_id,connection) * tickets;

                    try (PreparedStatement statement = connection.prepareStatement(Query.cancelTicket)) {
                        statement.setInt(1, bookingId);
                        statement.setInt(2, tickets);
                        statement.setDouble(3, refundAmount);
                        statement.setInt(4, bookingId);
                        statement.executeUpdate();
                    }

                    // Update booked tickets in bookedtickets table
                    int bookedTickets = getBookedTickets(bookingId, connection);
                    if (tickets < bookedTickets) {
                        try (PreparedStatement statement = connection.prepareStatement(Query.updateBooking)) {
                            statement.setInt(1, bookedTickets - tickets);
                            statement.setInt(2, bookingId);
                            statement.executeUpdate();
                        }
                    } else if (tickets == bookedTickets) {
                        try (PreparedStatement statement = connection.prepareStatement(Query.deleteBooking)) {
                            statement.setInt(1, bookingId);
                            statement.executeUpdate();
                        }
                    } else {
                        System.out.println("Number of ticket booked are: " + bookedTickets);
                        System.out.println("Please try lower or equal to Booked Tickets");
                    }

                    // Update available tickets in event table
                    int availableTicket = getTickets(event_id, connection);
                    updateTickets(event_id, availableTicket + tickets, connection);

                    connection.commit();
                } else {
                    System.out.println("Booking Not Found !!");
                }
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
                if (connection != null) {
                    connection.rollback(); // Rollback transaction on error
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input.");
        } catch (Exception e) {
            System.out.println("Something went wrong: " + e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    public static  void getRefundStatus(){
        System.out.println("Enter Cancel ID: ");
        int cancelId = input.nextInt();
        try(Connection connection = DB.getConnection()){
            if(isCancelled(cancelId, connection)){
                PreparedStatement statement = connection.prepareStatement(Query.getRefundStatus);
                statement.setInt(1, cancelId);
                ResultSet result = statement.executeQuery();
                result.next();
                System.out.println("Refund of $"+result.getInt(1)+" initiated to the source account");
            }
        }
        catch (Exception e){
            System.out.println("Something went wrong");
        }
    }

}