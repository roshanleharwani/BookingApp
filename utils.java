import java.sql.*;

class DB {
    public static Connection getConnection()throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/app","root","kali");
    }
}

class Query {
    static String fetchEvents = "Select * from event where event_date > CURDATE()";
    static String bookedTransactions = "select * from bookedtickets where customer_name = ?";
    static String cancelledTransactions = "select * from cancelledtickets where customer_name = ? ";
    static String bookTicket = "INSERT INTO BookedTickets (event_id, customer_name, booking_date, num_tickets_booked, total_amount) VALUES (?, ?, NOW(), ?,? )";
    static String cancelTicket = "INSERT INTO CancelledTickets (booking_id, cancel_date, num_tickets_cancelled, refund_amount) " +
                                 "SELECT booking_id, NOW(), num_tickets_booked, total_amount " +
                                 "FROM BookedTickets " +
                                 "WHERE booking_id = ? " +
                                 "AND NOW() < (SELECT event_date FROM Event WHERE Event.event_id = BookedTickets.event_id)";
    static  String deleteBooking = "DELETE FROM BookedTickets WHERE booking_id = ?";
    static String fetchRefund = "SELECT refund_amount\n" +
                                "FROM CancelledTickets\n" +
                                "WHERE cancel_id = ?";
}
