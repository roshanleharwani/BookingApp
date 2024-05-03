import java.sql.*;

interface BookingSystem {
    static public void getUpcomingEvents() {};
    static public void getBookedTransaction(String CustomerName, Connection connection) throws SQLException {};
    static public void getCancelledTransaction(String CustomerName, Connection connection) throws SQLException {};
    static public void getLastTransaction() {};
    static public void bookTicket() {};
    static public void cancelTicket() {};
    static public void getRefundStatus() {};

}

class Ticket {
    private String CustomerName = null;
    private Integer bookingId = null;
    private String date = null;
    private Integer NumberOfTickets = null;

    public int getNumberOfTickets() {
        return NumberOfTickets;
    }

    public void setNumberOfTickets(int numberOfTickets) {
        NumberOfTickets = numberOfTickets;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

class Bookedticket extends Ticket {
    private Integer eventId =null;
    private Double amountPaid = null;

    public Bookedticket(Connection connection, int bookingId) {
        try {
            PreparedStatement statement = connection.prepareStatement(Query.getBookedTicket);
            statement.setInt(1, bookingId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                eventId = resultSet.getInt("event_id");
                amountPaid = resultSet.getDouble("total_amount");
                String name = resultSet.getString("customer_name");
                String date = resultSet.getString("booking_date");
                Integer tickets = resultSet.getInt("num_tickets_booked");
                super.setBookingId(bookingId);
                super.setDate(date);
                super.setCustomerName(name);
                super.setNumberOfTickets(tickets);
            }
            else{
                System.out.println("Booking Not Found");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public int getEventId() {
        return eventId;
    }
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
    public double getAmountPaid() {
        return amountPaid;
    }
    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }
}

class Canceledticket extends Ticket {
    private  Integer cancelId = null;
    private  Double refundAmount =null;

    public Canceledticket(Connection connection, int Id) {
        try {
            PreparedStatement statement = connection.prepareStatement(Query.getCancelledTicket);
            statement.setInt(1, Id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                cancelId = Id;
                refundAmount = resultSet.getDouble("refund_amount");
                String name = resultSet.getString("customer_name");
                String date = resultSet.getString("cancel_date");
                Integer tickets = resultSet.getInt("num_tickets_cancelled");
                Integer bookingid = resultSet.getInt("booking_id");
                super.setBookingId(bookingid);
                super.setDate(date);
                super.setCustomerName(name);
                super.setNumberOfTickets(tickets);
            }
            else{
                System.out.println("Cancellation Not Found");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public double getRefundAmount() {
        return refundAmount;}
    public void setRefundAmount(double refundAmount) {
        this.refundAmount = refundAmount;
    }
    public int getCancelId() {
        return cancelId;
    }
    public void setCancelId(int cancelId) {
        this.cancelId = cancelId;
    }
}

    class DB {
        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "root", "kali");
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

        static String deleteBooking = "DELETE FROM BookedTickets WHERE booking_id = ?";

        static String updateBooking = "update bookedtickets set num_tickets_booked = ? where booking_id = ?;";

        static String getRefundStatus = "SELECT refund_amount FROM CancelledTickets WHERE cancel_id = ?";

        static String isEvent = "SELECT COUNT(*) AS event_count FROM event WHERE event_id = ?;";

        static String price = "select price_per_ticket from event where event_id = ?; ";

        static String totalTickets = " select total_tickets from event where event_id = ?;";

        static String updateTable = " update event set total_tickets = ? where event_id = ?;";

        static String getBookedTicket = "select * from BookedTickets where booking_id = ?;";

        static String getCancelledTicket = "select * from cancelledtickets where cancel_id = ?;";

        static String getEventsBookings = "select * from bookedtickets where event_id = ?;";

        static String getEventBookingAmount = "select sum(total_amount) from bookedtickets where event_id = ?;";
    }

