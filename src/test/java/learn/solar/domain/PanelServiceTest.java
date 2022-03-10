package learn.solar.domain;

import learn.solar.data.DataException;
import learn.solar.data.PanelRepository;
import learn.solar.data.PanelRepositoryTestDouble;
import learn.solar.models.Material;
import learn.solar.models.Panel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
public class PanelServiceTest {
    PanelService service = new PanelService(new PanelRepositoryTestDouble());

    @Test
    void shouldFindBySection() throws DataException {
        List<Panel> result = service.findBySection("test-section");
        assertEquals(1, result.size());
    }
    @Test
    void shouldNotFindSectionNotThere() throws DataException {
        List<Panel> result = service.findBySection("fake-section");
        assertEquals(0, result.size());
    }
    @Test
    void shouldNotAddNull() throws DataException {
        PanelResult expected = makeResult("Panel cannot be null.");
        PanelResult actual = service.add(null);
        assertEquals(expected.getMessages(), actual.getMessages());
    }
    @Test
    void shouldNotAddEmptySection() throws DataException {
        PanelResult expected = makeResult("Section name cannot be blank.");
        PanelResult actual = service.add(new Panel(1,"",1,1,2014, Material.CADMIUM_TELLURIDE,"yes"));
        assertEquals(expected.getMessages(),actual.getMessages());

    }
    @Test
    void testAddAValidPanel() throws DataException {
        Panel testPanel = new Panel(1,"test-section",1,1,2014, Material.CADMIUM_TELLURIDE,"yes");
        PanelResult result = new PanelResult();
        result.setPanel(testPanel);
        PanelResult actual = service.add(testPanel);

        assertEquals(result.getPanel(),actual.getPanel());
    }

    @Test
    void testUpdateIfFailed() throws DataException {
        Panel testPanel = new Panel(249,"test-section",1,1,2014, Material.CADMIUM_TELLURIDE,"yes");
        PanelResult expected = new PanelResult();
        expected.setPanel(testPanel);
        PanelResult result = service.update(testPanel);
        System.out.println(result.getMessages());
        assertNotEquals(result.getMessages(),expected.getMessages() );
    }
    @Test
    void testUpdateIfTrue() throws DataException {
        Panel testPanel = new Panel(1,"test-section",1,1,2014, Material.CADMIUM_TELLURIDE,"yes");

        PanelResult expected = new PanelResult();
        expected.setPanel(testPanel);
        PanelResult result = service.update(testPanel);

        assertEquals(result.getMessages(),expected.getMessages() );
    }
    @Test
    void testDelete() throws DataException {

        PanelResult expected = new PanelResult();
        PanelResult result = service.deleteById(1);

        assertEquals(result.getMessages(),expected.getMessages());

    }@Test
    void testDoNotDelete() throws DataException {
        PanelResult expected = makeResult("Failed to delete panel with id" + 999999);
        PanelResult actual = service.deleteById(999999);
        assertEquals(expected.getMessages(), actual.getMessages());
    }

    private PanelResult makeResult(String message) {
        PanelResult result = new PanelResult();
        result.addMessage(message);
        return result;
    }
}
