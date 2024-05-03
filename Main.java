import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int choice;
        boolean shouldExit = false;
        while(!shouldExit){
            BookTheShow.menu();
            System.out.println();
            System.out.print(">> ");
            choice = input.nextInt();
            switch(choice){
                case 1:
                    while(true){
                    BookTheShow.mainMenu();
                    System.out.println();
                    System.out.print(">> ");
                    choice = input.nextInt();
                    if(choice == 1){
                        BookTheShow.bookTicket();
                    }
                    else if(choice == 2){
                        BookTheShow.cancelTicket();
                    }
                    else if(choice == 3){
                        BookTheShow.getRefundStatus();
                    }
                    else if(choice == 4){
                        BookTheShow.getUpcomingEvents();
                    }
                    else if(choice == 5){
                        break;
                    }
                    else{
                        System.out.println("Invalid Choice");
                    }
                    System.out.println();
                    }
                    break;
                case 2:
                    System.out.println();
                    BookTheShow.getUpcomingEvents();
                    System.out.println();
                    break;
                case 3:
                    System.out.println();
                    BookTheShow.getLastTransaction();
                    break;
                case 4:
                    System.out.println();
                    BookTheShow.getEventBookings();
                case 5:
                    System.out.println();
                    shouldExit= true;
                    break;
            }
        }
    }
}