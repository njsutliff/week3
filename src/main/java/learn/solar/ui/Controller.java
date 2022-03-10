package learn.solar.ui;

import learn.solar.data.DataException;
import learn.solar.domain.PanelResult;
import learn.solar.domain.PanelService;
import learn.solar.models.Panel;

import java.util.ArrayList;
import java.util.List;

public class Controller {

    private final PanelService service;
    private final View view;

    public Controller(PanelService service, View view) {
        this.service = service;
        this.view = view;
    }

    public void run() throws DataException {
        System.out.println("Welcome to solarfarm!");
        try {
            MenuLoop();
        } catch (DataException e) {
            view.printHeader("FATAL ERROR" + e);
        }
    }

    public void MenuLoop() throws DataException {
        MenuOption option;
        do {
            option = view.chooseOptionFromMenu();
            System.out.println(option.getTitle());
            switch (option) {
                case EXIT:
                    view.printHeader(MenuOption.EXIT.getTitle());
                    break;
                case ADD_PANEL:
                    addPanel();
                    break;
                case REMOVE_PANEL:
                    deletePanel();
                    break;
                case UPDATE_PANEL:
                    updatePanel();
                    break;
                case FIND_BY_SECTION:
                    viewBySection();
                    break;
            }

        } while (option != MenuOption.EXIT);
    }

    private void viewBySection() throws DataException {
        view.printHeader(MenuOption.FIND_BY_SECTION.getTitle());
        System.out.println("Sections available: ");
        view.printHeader(service.getSection());
        String section = view.readSection();
        List<Panel> results = service.findBySection(section);
        view.printHeader(String.format("%s", "Panels in section " + section));
        view.printPanels(section, results);

    }

    private void addPanel() throws DataException {
        System.out.println("Sections available: ");
        view.printHeader(service.getSection());

        Panel p = view.makePanel();
        List<Panel> previous = service.findBySection(p.getSection());
        view.printPanels(p.getSection(), previous);
        boolean duplicate = false;

        for (Panel panel : previous) {
            if (panel.equals(p)) {
                view.printHeader("Error, duplicate panel! ");
                System.out.println("Panel" + panel.getId() + "equals" + p.getId());
                duplicate = true;
            }
        }
        if (!duplicate) {
            PanelResult result = service.add(p);
            result.getMessages();
            if (!result.isSuccess()) {
                view.printResult(result, "Added successfully");
            }
        }
    }

    private void updatePanel() throws DataException {
        view.printHeader(MenuOption.UPDATE_PANEL.getTitle());
        System.out.println("Sections available: ");
        view.printHeader(service.getSection());

        String section = view.readSection();
        List<Panel> panels = service.findBySection(section);
        view.printPanels(section, panels);

        Panel p = view.choosePanel(panels);
        if (p != null) {
            Panel updatedEncounter = view.update(p);
            PanelResult result = service.update(p);
            view.printResult(result, "Updated successfully");
        }

    }

    private void deletePanel() throws DataException {
        view.printHeader(MenuOption.REMOVE_PANEL.getTitle());
        System.out.println("Sections available: ");
        view.printHeader(service.getSection());

        String section = view.readSection();
        List<Panel> panels = service.findBySection(section);
        view.printPanels(section, panels);

        Panel p = view.deletePanel(section, panels);
        if (p != null) {
            service.deleteById(p.getId());
        }
    }


}
