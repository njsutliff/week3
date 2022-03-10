package learn.solar.ui;

import learn.solar.domain.PanelResult;
import learn.solar.domain.PanelService;
import learn.solar.models.Material;
import learn.solar.models.Panel;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class View {
    private final Scanner console = new Scanner(System.in);

    public MenuOption chooseOptionFromMenu() {
        MenuOption[] values = MenuOption.values();
        printHeader("solarfarm menu: ");
        for (int i = 0; i < values.length; i++) {
            System.out.printf("%s. %s%n", i, values[i].getTitle());
        }
        int index = readInt("Select [ 0 - 4 ] ", 0, 4);
        return values[index];
    }

    public void printHeader(String message) {
        System.out.println();
        System.out.println(message);
        System.out.println("=".repeat(message.length()));
    }

    public void printResult(PanelResult result, String successMessage) {
        if (result.isSuccess()) {
            if (result.getPanel() != null) {
                System.out.printf(successMessage, result.getPanel().getId());
            }
        } else {
            printHeader("Errors");
            for (String msg : result.getMessages()) {
                System.out.printf("- %s%n", msg);
            }
        }
    }

    public void printPanels(String sectionName, List<Panel> list) {
        if (list.size() == 0) {
            System.out.println("No Panels found");
        }
        for (Panel p : list) {
            System.out.printf("%s%s %n %s%n %s%n %s%n %s%n %s %n",
                    "[Panel ID: " + p.getId(),
                    " Section: " + p.getSection() + "]",
                    "Row: " + p.getRow(),
                    "Column: " + p.getColumn(),
                    "Installation Year: " + p.getInstallationYear(),
                    "Material: " + p.getMaterial(),
                    "Tracking?: " + p.getTracking());

        }
    }


    public Panel choosePanel(List<Panel> panels) {
        Panel result = null;
        System.out.println("Enter a panel ID# to update:  ");
        int Id = readInt("");
        for (int i = 0; i < panels.size(); i++) {
            if (panels.get(i).getId() == Id) {
                result = panels.get(i);
            }
        }
        if (result == null) {
            printHeader("Error no panel found");
        }
        update(result);
        return result;
    }

    public Panel makePanel() {
        printHeader("create a new Panel now: ");
        PanelResult result = new PanelResult();
        boolean done = false;
        Panel p;

        String section = readSection();

        int row = readInt("enter a row");
        if(row > 250 || row <=0) {
            result.addMessage("Row " + row + " out of range.");
        }
        int col = readInt("enter a column");
        if(col > 250 || col <=0) {
            result.addMessage("Column " + row + " out of range.");

        }
        int year = readInt("Enter an installation year");
        if(year > 2022 || col <=1950) {
            result.addMessage("Year " + year + " out of range.");
        }
        Material material = readMaterial();
        String b = (readString("Is it tracking? yes / no: "));

        int id = returnId();

        p = new Panel(id, section, row, col, year, material, b);
            generatePanelId(p);
        // System.out.println(" Success! New panel created. ");
        if(result.isSuccess()) {
            printResult(result,"Success! Added panel. ");
        }
        return p;
    }
    public int returnId(){
        Random r = new Random();
        return r.nextInt(250)-1;
    }

    private int generatePanelId(Panel p) {
        p.setId(returnId());
        return  p.getId();
    }


    public Panel update(Panel p) {
        p.setRow(readInt("Enter a row to update "));
        System.out.println(p.getRow());
        return p;
    }

    public Panel deletePanel(String section, List<Panel> panels) {
        Panel p = null;
        PanelResult result = new PanelResult();

        System.out.println("Enter a panel row# to delete:  ");
        int row = readInt("", 0, 250);
        System.out.println("Enter a panel col# to delete:  ");
        int col = readInt("", 0, 250);

        for (int i = 0; i < panels.size(); i++) {
            if (panels.get(i).getSection().equals(section)
                    && (panels.get(i).getRow() == row)
                    && (panels.get(i).getColumn() == col)) {
                p = panels.get(i);
                panels.remove(i);

                System.out.println("Deleted success");
                result.addMessage("Panel deleted");
            }
        }
        if (p == null) {
            System.out.println("No panel exists there to delete. ");
        }
        return p;
    }
    public String readSection() {
        System.out.println("Enter a section: ");
        return readString("");
    }

    private String readString(String message) {
        System.out.println(message);
        return console.nextLine();
    }

    private String readRequiredString(String stringToRead) {
        String result = null;
        do {
            result = readString(stringToRead).trim();
            if (result.length() == 0) {
                System.out.println("Empty string.");
            }
        } while (result.trim().length() == 0);
        return result;
    }

    private int readInt(String stringToParseToInt) {
        String value = null;
        int result = 0;
        boolean valid = false;
        do {
            try {
                value = readRequiredString(stringToParseToInt);
                result = Integer.parseInt(value);
                valid = true;
            } catch (NumberFormatException n) {
                n.printStackTrace();
            }
        } while (!valid);
        return result;
    }

    private int readInt(String stringToParseToInt, int min, int max) {

        int result = 0;
        do {
            result = readInt(stringToParseToInt);
            if (result < min || result > max) {
                System.out.printf("Value must be between %s and %s.%n", min, max);
            }
        } while (result < min || result > max);
        return result;
    }
    private Material readMaterial() {
        Material[] values = Material.values();
        Material material = null;
        int index = 0;
        printHeader("pick a material for the solar panel: ");
        for ( index = 0; index < values.length; index++) {
            System.out.printf("%s. %s%n", index, values[index]);
            material = values[index];
        }
        index--;
        String msg = String.format("Select Material Type [0-%s]:", index);

        return Material.values()[readInt(msg, 0, index)];
    }

}
