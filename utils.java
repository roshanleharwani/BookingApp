import java.sql.*;

interface BookingSystem{
    static public void getUpcomingEvents(){};
    static public void getBookedTransaction(String CustomerName,Connection connection) throws SQLException{};
    static public void getCancelledTransaction(String CustomerName,Connection connection) throws SQLException{};
    static public void getLastTransaction(){};
    static public void bookTicket(){};
    static public void cancelTicket(){};
    static public void getRefundStatus(){};

}


class DB {
    public static Connection getConnection()throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/app","root","kali");
    }
}

class Query {
    static String getEvents = "Select * from event where event_date > CURDATE()";

    static String isCustomerBookingExist = "select count(*) from bookedtickets where customer_name = ?;";

    static String isCancellationExist = "select count(*) from cancelledtickets where customer_name = ?;";

    static String bookedTransactions = "select * from bookedtickets where customer_name = ?";

    static String cancelledTransactions = "select * from cancelledtickets where customer_name = ? ";

    static String bookTicket = "INSERT INTO BookedTickets (event_id, customer_name, booking_date, num_tickets_booked, total_amount) VALUES (?, ?, CURRENT_DATE(), ?, ?);";

    static String cancelTicket = "INSERT INTO cancelledtickets (booking_id, cancel_date, num_tickets_cancelled, refund_amount, customer_name) SELECT ?, CURRENT_DATE(), ?, ?, customer_name FROM bookedtickets WHERE booking_id = ?;";

    static  String deleteBooking = "DELETE FROM BookedTickets WHERE booking_id = ?";

    static String updateBooking = "update bookedtickets set num_tickets_booked = ? where booking_id = ?;";

    static String bookedTickets = "select num_tickets_booked from bookedtickets where booking_id = ?;";

    static String getRefundStatus = "SELECT refund_amount FROM CancelledTickets WHERE cancel_id = ?";

    static String isEvent = "SELECT COUNT(*) AS event_count FROM event WHERE event_id = ?;";

    static String price = "select price_per_ticket from event where event_id = ?; ";

    static String totalTickets = " select total_tickets from event where event_id = ?;";

    static String updateTable = " update event set total_tickets = ? where event_id = ?;";

    static String isBookingExist = "select count(*) from bookedtickets where booking_id = ?;";

    static String eventId = "select event_id from bookedtickets where booking_id = ?;";

    static String isCancelled  = "select count(*) from cancelledtickets where cancel_id = ?;";

}

