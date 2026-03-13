import java.util.Scanner;

public class BookMyStayApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("====================================");
        System.out.println("      WELCOME TO BOOK MY STAY       ");
        System.out.println("====================================");

        System.out.println("\nFind and Book the Best Stays Easily!");

        System.out.println("\n------ MAIN MENU ------");
        System.out.println("1. View Available Rooms");
        System.out.println("2. Book a Room");
        System.out.println("3. View Booking Details");
        System.out.println("4. Exit");

        System.out.print("\nEnter your choice: ");
        int choice = sc.nextInt();

        switch(choice) {
            case 1:
                System.out.println("\nDisplaying available rooms...");
                break;

            case 2:
                System.out.println("\nRedirecting to booking page...");
                break;

            case 3:
                System.out.println("\nShowing booking details...");
                break;

            case 4:
                System.out.println("\nThank you for using BookMyStayApp!");
                break;

            default:
                System.out.println("\nInvalid choice! Please try again.");
        }

        sc.close();
    }
}