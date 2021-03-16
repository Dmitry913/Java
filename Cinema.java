package cinema;

import java.util.Scanner;

public class Cinema {
    char[][] hall;
    int sizeHall;

    enum State {
        START, SHOW, BUY, STATICS, EXIT
    }

    State state;
    int incomeCurrent;
    int incomeTotal;
    int numPurchaseTick;

    public Cinema(int rowNum, int seatNum) {
        hall = new char[rowNum][seatNum];
        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < seatNum; j++) {
                hall[i][j] = 'S';
            }
        }
        sizeHall = rowNum * seatNum;
        state = State.START;
        incomeCurrent = 0;
        numPurchaseTick = 0;
        if (sizeHall <= 60) {
            incomeTotal = sizeHall * 10;
        } else {
            incomeTotal = hall[0].length * (hall.length / 2) * 10 + hall[0].length * (hall.length - hall.length / 2) * 8;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of rows:");
        int rowNum = scanner.nextInt();
        System.out.println("Enter the number of seats in each row:");
        int seatNum = scanner.nextInt();
        Cinema cinema = new Cinema(rowNum, seatNum);
        while (cinema.state != State.EXIT) {
            switch (cinema.state) {
                case START:
                    System.out.println("1. Show the seats\n" +
                            "2. Buy a ticket\n" +
                            "3. Statistics\n" +
                            "0. Exit");
                    switch (scanner.nextInt()) {
                        case 1:
                            cinema.state = State.SHOW;
                            break;
                        case 2:
                            cinema.state = State.BUY;
                            break;
                        case 3:
                            cinema.state = State.STATICS;
                            break;
                        case 0:
                            cinema.state = State.EXIT;
                            break;
                    }
                    break;
                case SHOW:
                    cinema.printCinema();
                    cinema.state = State.START;
                    break;
                case BUY:
                    System.out.println("Enter a row number:");
                    int row = scanner.nextInt();
                    System.out.println("Enter a seat number in that row:");
                    int seat = scanner.nextInt();
                    if (cinema.reservedSeat(row, seat)) {
                        cinema.state = State.START;
                    }
                    break;
                case STATICS:
                    cinema.printStatic();
                    cinema.state = State.START;
                    break;
            }
        }
    }

    public void printStatic() {
        System.out.printf("Number of purchased tickets: %d\n", numPurchaseTick);
        System.out.printf("Percentage: %.2f%%%n", 100d * numPurchaseTick / sizeHall);
        System.out.printf("Current income: $%d\n", incomeCurrent);
        System.out.printf("Total income:$%d\n", incomeTotal);
    }

    public void printCinema() {
        System.out.println("Cinema:");
        System.out.print(" ");
        for (int i = 1; i < hall[0].length + 1; i++) {
            System.out.print(" " + i);
        }
        System.out.println();
        int i = 1;
        for (char[] row : hall) {
            System.out.print(i);
            for (char seat : row) {
                System.out.print(" " + seat);
            }
            System.out.println();
            i++;
        }
    }

    //As in coordinate system
    public boolean reservedSeat(int y, int x) {
        if (y < 1 || y > hall.length || x < 1 || x > hall[0].length) {
            System.out.println("Wrong input!");
        } else if (hall[y - 1][x - 1] == 'B') {
            System.out.println("That ticket has already been purchased!");
        } else {
            numPurchaseTick++;
            int price;
            if (hall.length * hall[0].length > 60) {
                price = y > hall.length / 2 ? 8 : 10;
            } else {
                price = 10;
            }
            System.out.println("Ticket price: $" + price);
            hall[y - 1][x - 1] = 'B';
            incomeCurrent += price;
            return true;
        }
        return false;
    }

}
