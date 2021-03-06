package Client;



import Server.Program.Console;
import common.Exceptions.CoordinatesException;
import common.Exceptions.EmptyStringException;
import common.Exceptions.IncorrectInputException;
import common.Task.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Scanner;

public class AskManager {
    private Scanner scanner;
    private BufferedReader fileReader;
    private Console console;
    private Scanner userScanner;
    private boolean fileMode;

    private final Double MAX_X = 334D;

    public AskManager(Scanner scanner){
        this.scanner = scanner;
    }
    public void setConsole (Console console) {
        this.console = console;
    }

    /**
     * Sets a scanner to scan user input.
     * @param userScanner Scanner to set.
     */
    public void setUserScanner(Scanner userScanner) {
        this.userScanner = userScanner;
    }

    /**
     * @return Scanner, which uses for user input.
     */
    public Scanner getUserScanner() {
        return userScanner;
    }

    /**
     * Sets marine asker mode to 'File Mode'.
     */
    public void setFileMode() {
        fileMode = true;
    }

    /**
     * Sets marine asker mode to 'User Mode'.
     */
    public void setUserMode() {
        fileMode = false;
    }
    /**
     * Asks the name
     * @return name
     * **/
    public String askName(){
        console.println("Введите название товара: ");
        String name;
        while(true){
            try{
                if(console.getConsoleStatus() == 1){
                    name = scanner.nextLine().trim();
                } else {
                    name = fileReader.readLine().trim();
                }
                if (name == null) throw new NullPointerException();
                if (name.isEmpty()) throw new EmptyStringException("Данные не были введены!");
                break;
            } catch (EmptyStringException e){
                if(console.getConsoleStatus() == 1) System.out.println(e.getMessage());
            } catch (NullPointerException e){
                System.out.println("Empty string");
                if(console.getConsoleStatus() != 1) return null;
            } catch (IOException e){
                System.out.println("Problems with access to file");
                return null;
            }

        }
        return name;
    }
    /**
     * Asks the coordinates
     * @return coordinate
     * **/
    public Coordinates askCoordinates(){
        float x = askX();
        Double y = askY();
        try {
            return new Coordinates(x,y);
        } catch(CoordinatesException e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    /**
     * Asks the X coordinate
     * @return X coordinate
     * **/
    public float askX(){
        console.println("Введите координату X: ");
        float x;
        while(true){
            String rawX;
            try{
                if(console.getConsoleStatus() == 1){
                    rawX = scanner.nextLine().trim();
                } else {
                    rawX = fileReader.readLine().trim();
                }
                if (rawX.isEmpty()) throw new EmptyStringException("Данные не были введены!");
                x = Float.parseFloat(rawX);
                if (x > MAX_X) throw new IncorrectInputException("X координата не может быть больше " + MAX_X);
                break;
            } catch (EmptyStringException e){
                if(console.getConsoleStatus() == 1) System.out.println(e.getMessage());
            }catch (IncorrectInputException e){
                System.out.println(e.getMessage());
                return -1;
            }
            catch (NullPointerException | NumberFormatException e){
                System.out.println("Введите корректное значение!");
                if (console.getConsoleStatus() != 1) return -1;
            } catch (IOException e){
                System.out.println("Проблемы с доступом к файлу");
                return -1;
            }
        }

        return x;
    }
    /**
     * Asks the Y coordinate
     * @return Y coordinate
     * **/
    public double askY(){
        console.println("Введите координату Y: ");
        double y;
        while(true){
            String rawY;
            try{
                if (console.getConsoleStatus() == 1){
                    rawY = scanner.nextLine().trim();
                } else {
                    rawY = fileReader.readLine().trim();
                }
                if (rawY.isEmpty()) throw new EmptyStringException("Данные не были введены!");
                if (rawY == null) throw new NullPointerException();
                y = Double.parseDouble(rawY);
                break;
            } catch (EmptyStringException e){
                if(console.getConsoleStatus() == 1) System.out.println(e.getMessage());
            } catch (NumberFormatException | NullPointerException e) {
                System.out.println("Введите корректное значение!");
                if (console.getConsoleStatus() != 1) return -1;
            } catch (IOException e){
                System.out.println("Проблемы с доступом к файлу");
                return -1;
            }
        }

        return y;
    }
    /**
     * Asks price
     * @return price
     * **/
    public int askPrice(){
        console.println("Введите цену: ");
        int price;
        while(true){
            String rawPrice;
            try{
                if (console.getConsoleStatus() == 1){
                    rawPrice = scanner.nextLine().trim();
                } else {
                    rawPrice = fileReader.readLine().trim();
                }
                if (rawPrice.isEmpty()) throw new EmptyStringException("Данные не были введены!");
                if (rawPrice == null) throw new NullPointerException();

                price = Integer.parseInt(rawPrice);
                if (price <= 0) throw new IncorrectInputException("Цена должна быть больше 0");
                break;
            } catch (EmptyStringException e){
                if(console.getConsoleStatus() == 1) System.out.println(e.getMessage());
            } catch (IncorrectInputException e){
                if(console.getConsoleStatus() == 1) System.out.println(e.getMessage());
            } catch (NumberFormatException | NullPointerException e) {
                System.out.println("Введите корректное значение!");
                if (console.getConsoleStatus() != 1) return -1;
            } catch (IOException e){
                System.out.println("Проблемы с доступом к файлу");
                return -1;
            }
        }

        return price;
    }

    /**
     * Asks price
     * @return price
     * **/
    public String askPartNumber(){
        console.println("Введите PartNumber: ");
        String partNum;
        while(true){
            try{
                if (console.getConsoleStatus() == 1){
                    partNum = scanner.nextLine().trim();
                } else {
                    partNum = fileReader.readLine().trim();
                }
                if (partNum.isEmpty()) throw new EmptyStringException("Данные не были введены!");
                if (partNum.length() < 25) throw new IncorrectInputException("Длина строки должна быть не меньше 25");
                break;
            } catch (EmptyStringException e){
                if(console.getConsoleStatus() == 1) System.out.println(e.getMessage());
            } catch (IncorrectInputException e){
                if(console.getConsoleStatus() == 1) System.out.println(e.getMessage());
            } catch (NumberFormatException | NullPointerException e) {
                System.out.println("Введите корректное значение!");
                if (console.getConsoleStatus() != 1) return null;
            } catch (IOException e){
                System.out.println("Проблемы с доступом к файлу");
                return null;
            }
        }

        return partNum;
    }

    /**
     * Asks the manufactureCost
     * @return manufactureCost
     * **/
    public double askManufactureCost(){
        console.println("Введите manufactureCost: ");
        double manufactureCost;
        while(true){
            String rawManufactureCost;
            try{
                if (console.getConsoleStatus() == 1){
                    rawManufactureCost = scanner.nextLine().trim();
                } else {
                    rawManufactureCost = fileReader.readLine().trim();
                }
                if (rawManufactureCost.isEmpty()) throw new EmptyStringException("Данные не были введены!");
                if (rawManufactureCost == null) throw new NullPointerException();
                manufactureCost = Double.parseDouble(rawManufactureCost);
                break;
            } catch (EmptyStringException e){
                if(console.getConsoleStatus() == 1) System.out.println(e.getMessage());
            } catch (NumberFormatException | NullPointerException e) {
                System.out.println("Введите корректное значение!");
                if (console.getConsoleStatus() != 1) return -1;
            } catch (IOException e){
                System.out.println("Проблемы с доступом к файлу");
                return -1;
            }
        }

        return manufactureCost;
    }

    /**
     * Asks the UnitOfMeasure
     * @return UnitOfMeasure
     * **/
    public UnitOfMeasure askUnitOfMeasure(){
        Class unitOfMeasureClass = UnitOfMeasure.class;
        console.println("Доступные единицы измерения: ");
        if(console.getConsoleStatus() == 1)
            for (Object enumConstant : unitOfMeasureClass.getEnumConstants()){
                Console.println(enumConstant);
            }
        console.println("Введите единицы измерения: ");
        UnitOfMeasure unitOfMeasure;
        while(true){
            String rawUnitOfMeasure;
            try{
                if(console.getConsoleStatus() == 1){
                    rawUnitOfMeasure = scanner.nextLine().trim();
                } else {
                    rawUnitOfMeasure = fileReader.readLine().trim();
                }
                if (rawUnitOfMeasure.isEmpty()) throw new EmptyStringException("Данные не были введены!");
                unitOfMeasure = UnitOfMeasure.valueOf(rawUnitOfMeasure);


            } catch (EmptyStringException e){
                if(console.getConsoleStatus() == 1) System.out.println(e.getMessage());
                continue;
            } catch (IllegalArgumentException e){
                System.out.println("Неправильно введена константа");
                continue;
            } catch (IOException e){
                System.out.println("Проблемы с доступом к файлу");
                continue;
            }
            break;
        }

        return unitOfMeasure;
    }


    /**
     * Asks the askOrgName
     * @return askOrgName
     * **/
    public String askOrgName(){
        console.println("Введите Название организации: ");
        String orgName;
        while(true){
            try{
                if (console.getConsoleStatus() == 1){
                    orgName = scanner.nextLine().trim();
                } else {
                    orgName = fileReader.readLine().trim();
                }
                if (orgName.isEmpty()) throw new EmptyStringException("Данные не были введены!");
                if (orgName == null) throw new NullPointerException();
                break;
            } catch (EmptyStringException e){
                if(console.getConsoleStatus() == 1) System.out.println(e.getMessage());
            } catch (NumberFormatException | NullPointerException e) {
                System.out.println("Введите корректное значение!");
                if (console.getConsoleStatus() != 1) return null;
            } catch (IOException e){
                System.out.println("Проблемы с доступом к файлу");
                return null;
            }
        }

        return orgName;
    }
    /**
     * Asks the askOrgName
     * @return askOrgName
     * **/
    public String askOrgFullName(){
        console.println("Введите полное название организации: ");
        String orgFullName;
        while(true){
            try{
                if (console.getConsoleStatus() == 1){
                    orgFullName = scanner.nextLine().trim();
                } else {
                    orgFullName = fileReader.readLine().trim();
                }
                if (orgFullName.isEmpty()) throw new EmptyStringException("Данные не были введены!");
                if (orgFullName == null) throw new NullPointerException();
                break;
            } catch (EmptyStringException e){
                if(console.getConsoleStatus() == 1) System.out.println(e.getMessage());
            } catch (NumberFormatException | NullPointerException e) {
                System.out.println("Введите корректное значение!");
                if (console.getConsoleStatus() != 1) return null;
            } catch (IOException e){
                System.out.println("Проблемы с доступом к файлу");
                return null;
            }
        }

        return orgFullName;
    }

    /**
     * Asks the annualTurnover
     * @return annualTurnover
     * **/
    public Long askAnnualTurnover(){
        console.println("Введите годовой оборот: ");
        Long annualTurnover;
        while(true){
            String rawAnnualTurnover;
            try{
                if (console.getConsoleStatus() == 1){
                    rawAnnualTurnover = scanner.nextLine().trim();
                } else {
                    rawAnnualTurnover = fileReader.readLine().trim();
                }
                if (rawAnnualTurnover.isEmpty()) throw new EmptyStringException("Данные не были введены!");
                if (rawAnnualTurnover == null) throw new NullPointerException();
                annualTurnover = Long.parseLong(rawAnnualTurnover);
                if (annualTurnover < 0) throw new IncorrectInputException("Число должно быть больше 0");
                break;
            } catch (EmptyStringException e){
                if(console.getConsoleStatus() == 1) System.out.println(e.getMessage());
            } catch (IncorrectInputException e){
                if(console.getConsoleStatus() == 1) System.out.println(e.getMessage());
            } catch (NumberFormatException | NullPointerException e) {
                System.out.println("Введите корректное значение!");
                if (console.getConsoleStatus() != 1) return null;
            } catch (IOException e){
                System.out.println("Проблемы с доступом к файлу");
                return null;
            }
        }

        return annualTurnover;
    }

    /**
     * Asks the OrganizationType
     * @return OrganizationType
     * **/
    public OrganizationType askType(){
        Class typeClass = OrganizationType.class;
        console.println("Доступные типы организаций: ");
        if(console.getConsoleStatus() == 1)
            for (Object enumConstant : typeClass.getEnumConstants()){
                Console.println(enumConstant);
            }
        console.println("Введите тип организации: ");
        OrganizationType type;
        while(true){
            String rawType;
            try{
                if(console.getConsoleStatus() == 1){
                    rawType = scanner.nextLine().trim();
                } else {
                    rawType = fileReader.readLine().trim();
                }
                if (rawType.isEmpty()) throw new EmptyStringException("Данные не были введены!");
                type = OrganizationType.valueOf(rawType);
                if (type == null) throw new IllegalArgumentException();

            } catch (EmptyStringException e){
                if(console.getConsoleStatus() == 1) System.out.println(e.getMessage());
                continue;
            } catch (IllegalArgumentException e){
                System.out.println("Неправильно введена константа");
                continue;
            } catch (IOException e){
                System.out.println("Проблемы с доступом к файлу");
                continue;
            }
            break;
        }

        return type;
    }
    /**
     * Asks the PostalAddress
     * @return PostalAddress
     * **/
    public Address askPostalAddress() {
        return new Address(askZipCode(), askTown());
    }
    /**
     * Asks the Town
     * @return Town
     * **/
    public Location askTown() {
        return new Location(askTownX(), askTownY(), askTownName());
    }

    /**
     * Asks the ZipCode
     * @return ZipCode
     * **/
    public String askZipCode(){
        console.println("Введите Zip Code: ");
        String zipCode;
        while(true){
            try{
                if (console.getConsoleStatus() == 1){
                    zipCode = scanner.nextLine().trim();
                } else {
                    zipCode = fileReader.readLine().trim();
                }
                if (zipCode.isEmpty()) throw new EmptyStringException("Данные не были введены!");
                if (zipCode == null) throw new NullPointerException();
                break;
            } catch (EmptyStringException e){
                if(console.getConsoleStatus() == 1) System.out.println(e.getMessage());
            } catch (NumberFormatException | NullPointerException e) {
                System.out.println("Введите корректное значение!");
                if (console.getConsoleStatus() != 1) return null;
            } catch (IOException e){
                System.out.println("Проблемы с доступом к файлу");
                return null;
            }
        }

        return zipCode;
    }

    /**
     * Asks the Town X coordinate
     * @return Town X coordinate
     * **/
    public float askTownX(){
        console.println("Введите координату X: ");
        float townX;
        while(true){
            String rawTownX;
            try{
                if(console.getConsoleStatus() == 1){
                    rawTownX = scanner.nextLine().trim();
                } else {
                    rawTownX = fileReader.readLine().trim();
                }
                if (rawTownX.isEmpty()) throw new EmptyStringException("Данные не были введены!");

                townX = Float.parseFloat(rawTownX);
                break;
            } catch (EmptyStringException e){
                if(console.getConsoleStatus() == 1) System.out.println(e.getMessage());
            } catch (NullPointerException | NumberFormatException e){
                System.out.println("Введите корректное значение!");
                if (console.getConsoleStatus() != 1) return -1;
            } catch (IOException e){
                System.out.println("Проблемы с доступом к файлу");
                return -1;
            }
        }

        return townX;
    }
    /**
     * Asks the Town Y coordinate
     * @return Town Y coordinate
     * **/
    public Float askTownY(){
        console.println("Введите координату Y: ");
        Float townY;
        while(true){
            String rawTownY;
            try{
                if(console.getConsoleStatus() == 1){
                    rawTownY = scanner.nextLine().trim();
                } else {
                    rawTownY = fileReader.readLine().trim();
                }
                if (rawTownY.isEmpty()) throw new EmptyStringException("Данные не были введены!");
                if (rawTownY == null) throw new NullPointerException();
                townY = Float.parseFloat(rawTownY);
                break;
            } catch (EmptyStringException e){
                if(console.getConsoleStatus() == 1) System.out.println(e.getMessage());
            } catch (NullPointerException | NumberFormatException e){
                System.out.println("Введите корректное значение!");
                if (console.getConsoleStatus() != 1) return null;
            } catch (IOException e){
                System.out.println("Проблемы с доступом к файлу");
                return null;
            }
        }

        return townY;
    }
    /**
     * Asks the askTownName
     * @return askTownName
     * **/
    public String askTownName(){
        console.println("Введите Название города: ");
        String townName;
        while(true){
            try{
                if (console.getConsoleStatus() == 1){
                    townName = scanner.nextLine().trim();
                } else {
                    townName = fileReader.readLine().trim();
                }
                if (townName.isEmpty()) throw new EmptyStringException("Данные не были введены!");
                if (townName == null) throw new NullPointerException();
                break;
            } catch (EmptyStringException e){
                if(console.getConsoleStatus() == 1) System.out.println(e.getMessage());
            } catch (NumberFormatException | NullPointerException e) {
                System.out.println("Введите корректное значение!");
                if (console.getConsoleStatus() != 1) return null;
            } catch (IOException e){
                System.out.println("Проблемы с доступом к файлу");
                return null;
            }
        }

        return townName;
    }

}
