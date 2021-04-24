import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;

public class Main {

    private static void inputShips(BattleShip gamer) {
        Scanner scanner = new Scanner(System.in);
        for (String ship : new String[]{"Aircraft Carrier (5", "Battleship (4", "Submarine (3", "Cruiser (3", "Destroyer (2"}) {
            System.out.printf("Enter the coordinates of the %s cells):\n", ship);
            String shipPosition = scanner.nextLine();
            while (!gamer.isCorrectPlace(shipPosition, ship)) {
                shipPosition = scanner.nextLine();
            }
            gamer.setShip(shipPosition);
        }
    }

    private static void promptEnterKey() {
        System.out.println("Press Enter and pass the move to another player");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void doTurn(BattleShip first, BattleShip second, int number) {
        Scanner scanner = new Scanner(System.in);
        second.printFogWar();
        System.out.println("---------------------");
        first.printField();
        System.out.printf("Player %d, it's your turn:\n", number);
        String shot = scanner.next();
        while (!second.isCorrectShot(shot)) {
            shot = scanner.next();
        }
        second.takeShot(shot);
    }

    public static void main(String[] args) {
        System.out.println("Player 1, place your ships to the game field");
        BattleShip player1 = new BattleShip();
        Main.inputShips(player1);
        promptEnterKey();
        System.out.println("Player 2, place your ships to the game field\n");
        BattleShip player2 = new BattleShip();
        Main.inputShips(player2);
        while(true) {
            promptEnterKey();
            Main.doTurn(player1, player2, 1);
            if (player2.isEnd()) {
                break;
            }
            promptEnterKey();
            Main.doTurn(player2, player1, 2);
            if (player1.isEnd()) {
                break;
            }
        }
        System.out.println("You sank the last ship. You won. Congratulations!");
    }
}


class BattleShip {
    private final Stack<Ship> fleet;

    private final char[][] array;

    public BattleShip() {
//        fleet = new Ship[4];
        fleet = new Stack<>();
        this.array = new char[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                this.array[i][j] = '~';
            }
        }
        printField();
    }

    public void printField() {
        System.out.print("  1 2 3 4 5 6 7 8 9 10");
        for (int i = 0; i < 10; i++) {
            System.out.print("\n" + (char) ('A' + i));
            for (int j = 0; j < 10; j++) {
                System.out.print(" " + array[i][j]);
            }
        }
        System.out.println();
    }

    public void printFogWar() {
        System.out.print("  1 2 3 4 5 6 7 8 9 10");
        for (int i = 0; i < 10; i++) {
            System.out.print("\n" + (char) ('A' + i));
            for (int j = 0; j < 10; j++) {
                System.out.print(" " + (array[i][j] == 'O' ? '~' : array[i][j]));
            }
        }
        System.out.println();
    }

    private boolean isFreeCell(int y, int x) {
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                try {
                    if (array[y + i][x + j] != '~') {
                        return false;
                    }
                } catch (IndexOutOfBoundsException ignored) {
                }
            }
        }
        return true;
    }

    public boolean isCorrectPlace(String position, String typeShip) {
        Ship ship = new Ship(position);
        if (ship.getStartY() == ship.getEndY() ^ ship.getStartX() == ship.getEndX()) {
            if (ship.getSize() == (int)typeShip.charAt(typeShip.length() - 1) - '0') {
                for (int i = 0; i < ship.getSize(); i++) {
                    // если он горизонтальный, то проверяем свободность, и если получаем тру, то не проверяем вертикальность,
                    // если он не горизонтальный, то проверяем сразу же свободу по вертикали
                    if (!(ship.isHorizontal() && isFreeCell(ship.getStartY(), ship.getStartX() + i) ||
                            isFreeCell(ship.getStartY() + i, ship.getStartX() - 1 + i))) {
                        System.out.println("Error! You placed it too close to another one. Try again:");
                        return false;
                    }
                }
                return true;
            } else {
                System.out.printf("Error! Wrong length of the %s! Try again:\n", typeShip.substring(0, typeShip.indexOf('(') - 1));
            }
        } else {
            System.out.println("Error! Wrong ship location! Try again:");
        }
        return false;
    }

    public void setShip(String position) {
        Ship ship = new Ship(position);
//        fleet[fleet.length - 1] = ship;
        fleet.push(new Ship(position));
//        fleet[fleet.length - 1] = new Ship(position);
        for (int i = 0; i < ship.getSize(); i++) {
            // если он горизонтальный, то проверяем свободность, и если получаем тру, то не проверяем вертикальность,
            // если он не горизонтальный, то проверяем сразу же свободу по вертикали
            if (ship.isHorizontal()) {
                array[ship.getStartY()][ship.getStartX() - 1 + i] = 'O';
            } else {
                array[ship.getStartY() + i][ship.getStartX() - 1] = 'O';
            }
        }
        printField();
    }

    public boolean isCorrectShot(String shot) {
        int y = shot.charAt(0) - 'A';
        int x = Integer.parseInt(shot.substring(1));
        if (y >= 0 && y < 10 && x > 0 && x < 11) {
            return true;
        } else {
            System.out.println("Error! You entered the wrong coordinates! Try again:");
            return false;
        }
    }

    public void takeShot(String shot) {
        int y = shot.charAt(0) - 'A';
        int x = Integer.parseInt(shot.substring(1)) - 1;
        for (Ship ship : fleet) {
            if (ship.isHit(shot)) {
                if (ship.isKilled()) {
                    System.out.println("You sank a ship!");
                } else {
                    System.out.println("You hit a ship!");
                }
                array[y][x] = 'X';
                return;
            }
        }
        array[y][x] = 'M';
        System.out.println("You missed.");
    }

    public boolean isEnd() {
        int countKilled = 0;
        for (Ship ship : fleet) {
            if (ship.isKilled()) {
                countKilled++;
            }
        }
        return (countKilled == 5);
    }
}


class Ship {
    // y  координата строчки
    private int y1;
    private int x1;
    private int y2;
    private int x2;
    private final int size;
    private int hitPoint;

    public Ship(String position) {
        this.y1 = position.charAt(0) - 'A';
        this.x1 = Integer.parseInt(position.substring(1, position.indexOf(" ")));
        this.y2 = position.charAt(position.indexOf(" ") + 1) - 'A';
        this.x2 = Integer.parseInt(position.substring(position.indexOf(" ") + 2));
        // присваиваем y1 и x1 наименьшую координату
        if (x1 > x2) {
            x1 += x2;
            x2 = x1 - x2;
            x1 -= x2;
        } else if (y1 > y2) {
            y1 += y2;
            y2 = y1 - y2;
            y1 -= y2;
        }
        this.size = x2 - x1 + y2 - y1 + 1;
        hitPoint = this.size;
    }

    public boolean isHorizontal() {
        return y1 == y2;
    }

    public int getSize() {
        return size;
    }

    public int getStartX() {
        return x1;
    }

    public int getStartY() {
        return y1;
    }

    public int getEndX() {
        return x2;
    }

    public int getEndY() {
        return y2;
    }

    public boolean isHit(String shot) {
        int y = shot.charAt(0) - 'A';
        int x = Integer.parseInt(shot.substring(1));
        if (y1 <= y && y <= y2 && x1 <= x && x <= x2) {
            if (!isKilled()) {
                hitPoint--;
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean isKilled() {
        return (hitPoint == 0);
    }
}
