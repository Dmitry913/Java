package machine;
import java.util.Scanner;

public class CoffeeMachine {
    public enum State {
        ACTION, BUY, FILL, FINISH
    }

    public  enum Filling {
        WATER, MILK, COFFEE, CUPS
    }
    Filling materialFill;
    State state;

    //water; milk; coffee; cups; money
    public int[] materials;

    public CoffeeMachine() {
        this.state = State.ACTION;
        materials = new int[]{400, 540, 120, 9, 550};
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CoffeeMachine machine = new CoffeeMachine();
        while(machine.state != State.FINISH){
            if (machine.state == State.ACTION) {
                System.out.println("Write action (buy, fill, take, remaining, exit): ");
            }
            machine.processingData(scanner.next());
        }
    }


    public void processingData(String data) {
        switch (state) {
            case ACTION:
                switch (data) {
                    case "buy":
                        System.out.println("What do you want to buy? 1 - espresso," +
                                " 2 - latte, 3 - cappuccino, back - to main menu: ");
                        state = State.BUY;
                        break;
                    case "fill":
                        state = State.FILL;
                        materialFill = Filling.WATER;
                        System.out.println("Write how many ml of water do you want to add: ");
                        break;
                    case "take":
                        take();
                        System.out.println("Write action (buy, fill, take, remaining, exit): ");
                        break;
                    case "remaining":
                        printState();
                        break;
                    case "exit":
                        state = State.FINISH;
                        break;
                }
                break;
            case BUY:
                buy(data);
                state = State.ACTION;
                break;
            case FILL:
                fill(Integer.parseInt(data));
                if (materialFill == Filling.CUPS) {
                    state = State.ACTION;
                } else {
                    materialFill = Filling.values()[materialFill.ordinal() + 1];
                }
                break;
        }
    }


    public void take() {
        System.out.printf("I gave you $%d%n", materials[4]);
        materials[4] = 0;
    }

    public void printState() {
        System.out.println("The coffee machine has:");
        System.out.printf("%d of water%n", materials[0]);
        System.out.printf("%d of milk%n", materials[1]);
        System.out.printf("%d of coffee beans%n", materials[2]);
        System.out.printf("%d of disposable cups%n", materials[3]);
        System.out.printf("%d of money%n", materials[4]);
    }

    public void buy(String act) {
        if (materials[3] < 1) {
            System.out.println("Sorry, not enough disposable cups!");
        } else if (!"back".equals(act)){
            switch (act) {
                case "1":
                    if (materials[0] < 250) {
                        System.out.println("Sorry, not enough water!");
                    } else if (materials[2] < 16) {
                        System.out.println("Sorry, not enough coffee beans!");
                    } else {
                        System.out.println("I have enough resources, making you a coffee!");
                        materials[0] -= 250;
                        materials[2] -= 16;
                        materials[4] += 4;
                        materials[3]--;
                    }
                    break;
                case "2":
                    if (materials[0] < 350) {
                        System.out.println("Sorry, not enough water!");
                    } else if (materials[2] < 20) {
                        System.out.println("Sorry, not enough coffee beans!");
                    } else if (materials[1] < 75) {
                        System.out.println("Sorry, not enough milk!");
                    } else {
                            System.out.println("I have enough resources, making you a coffee!");
                            materials[0] -= 350;
                            materials[1] -= 75;
                            materials[2] -= 20;
                            materials[4] += 7;
                            materials[3]--;
                        }
                    break;
                case "3":
                    if (materials[0] < 200) {
                        System.out.println("Sorry, not enough water!");
                    } else if (materials[2] < 12) {
                        System.out.println("Sorry, not enough coffee beans!");
                    } else if (materials[1] < 100) {
                        System.out.println("Sorry, not enough milk!");
                    } else {
                        System.out.println("I have enough resources, making you a coffee!");
                        materials[0] -= 200;
                        materials[1] -= 100;
                        materials[2] -= 12;
                        materials[4] += 6;
                        materials[3]--;
                    }
                    break;
            }
        }
    }

    public void fill(int addition) {
        switch (materialFill) {
            case WATER:
                materials[0] += addition;
                System.out.println("Write how many ml of milk do you want to add: ");
                break;
            case MILK:
                materials[1] += addition;
                System.out.println("Write how many grams of coffee beans do you want to add: ");
                break;
            case COFFEE:
                materials[2] += addition;
                System.out.println("Write how many disposable cups of coffee do you want to add:");
                break;
            case CUPS:
                materials[3] += addition;
                break;
        }
    }
}
