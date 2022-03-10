package learn.solar.data;

import learn.solar.models.Material;
import learn.solar.models.Panel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.awt.image.renderable.ParameterBlock;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PanelFileRepositoryTest {
    static final String SEED_FILE_PATH = "./data/panel-seed.csv";
    static final String TEST_FILE_PATH = "./data/panel-test.csv";

    PanelFileRepository panelRepository = new PanelFileRepository(TEST_FILE_PATH);


    @BeforeAll
    static void setupTest() throws DataException {
        Path seedPath = Paths.get(SEED_FILE_PATH);
        Path testPath = Paths.get(TEST_FILE_PATH);

        try {
            Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new DataException(e.getMessage(), e);
        }
    }


    @Test
    void findBySection() throws DataException {
        List<Panel> p = panelRepository.findBySection("test-section");
        assertEquals("test-section", p.get(0).getSection());
    }

    @Test
    void ShouldAdd() throws DataException {
        Panel p = new Panel(1, "test-section", 1, 1, 2014, Material.AMORPHOUS_SILICON, "yes");
        panelRepository.add(p);
        List<Panel> list = panelRepository.findBySection("test-section");
        boolean test = list.contains(p);
        assertEquals(test, true);
    }

    @Test
    void update() throws DataException {
        List<Panel> list = panelRepository.findBySection("test-section");
        Panel toUpdate = list.get(0);
        toUpdate.setColumn(2);
        assertTrue(panelRepository.update(toUpdate));
    }

    @Test
    void updateIfFailed() throws DataException {
        Panel shouldNotUpdate = new Panel(4, "not-section", 5, 6, 2010, Material.CADMIUM_TELLURIDE, "yes");
        assertFalse(panelRepository.update(shouldNotUpdate));
    }

    @Test
    void deleteById() throws DataException {
        List<Panel> panels = panelRepository.findBySection("test-section");
        Panel panelToDelete = panelRepository.findBySection("test-section").get(1);
        panelRepository.deleteById(1);
        assertEquals(panels.get(1).getInstallationYear(), panelToDelete.getInstallationYear());
        System.out.println(panelToDelete.getSection());
    }


}