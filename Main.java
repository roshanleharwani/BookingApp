import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int choice;
        boolean shouldExit = false;
        while(!shouldExit){
            BookingSystem.menu();
            System.out.println();
            System.out.print(">> ");
            choice = input.nextInt();
            switch(choice){
                case 1:
                    while(true){
                    BookingSystem.mainMenu();
                    System.out.println();
                    System.out.print(">> ");
                    choice = input.nextInt();
                    if(choice == 1){
                        BookingSystem.bookTicket();
                    }
                    else if(choice == 2){
                        BookingSystem.cancelTicket();
                    }
                    else if(choice == 3){
                        BookingSystem.getRefundStatus();
                    }
                    else if(choice == 4){
                        BookingSystem.showUpcomingEvents();
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
                    BookingSystem.showUpcomingEvents();
                    System.out.println();
                    break;
                case 3:
                    System.out.println();
                    BookingSystem.lastTransaction();
                    break;
                case 4:
                    System.out.println();
                    shouldExit= true;
                    break;
            }
        }
    }
}