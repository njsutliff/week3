package learn.solar;

import learn.solar.data.DataException;
import learn.solar.data.PanelFileRepository;
import learn.solar.data.PanelRepository;
import learn.solar.domain.PanelService;
import learn.solar.ui.Controller;
import learn.solar.ui.View;

public class App {

    public static void main(String[] args) throws DataException {
        PanelFileRepository repository = new PanelFileRepository("./data/panel.csv");
        PanelService service = new PanelService(repository);

        View view = new View();

        Controller controller = new Controller(service, view);
        controller.run();
        }
}
