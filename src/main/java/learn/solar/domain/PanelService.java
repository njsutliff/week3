package learn.solar.domain;

import learn.solar.data.DataException;
import learn.solar.data.PanelFileRepository;
import learn.solar.data.PanelRepository;
import learn.solar.models.Material;
import learn.solar.models.Panel;

import java.util.ArrayList;
import java.util.List;

public class PanelService {

    private PanelRepository repository;

    public PanelService(PanelRepository repository) {
        this.repository = repository;
    }

    public List<Panel> findBySection(String section) throws DataException {
        return repository.findBySection(section);
    }
    public String getSection() throws  DataException{
        List<Panel> list = repository.findAll();
        List<String> sectionList = new ArrayList<>();
        String section;
        for (Panel p : list){
            section = p.getSection();
            if(!sectionList.contains(section)){
                sectionList.add(section);
            }
        }
        return sectionList.toString();
    }
    public PanelResult add(Panel p) throws DataException {
        PanelResult result = validate(p);
        if (!result.isSuccess()) {
            return result;
        }
        result.setPanel(p);
        repository.add(p);
        return result;
    }

    public PanelResult update(Panel p) throws DataException {
        PanelResult result = validate(p);
        if (!result.isSuccess()) {
            return result;
        }
        if (!repository.update(p)) {
            result.addMessage("Failed to update Panel id: " + p.getId());
            return result;
        }
        if (result.isSuccess()) {
            result.setPanel(p);
        } else {
            String message = String.format("Panel id %s was not found.", p.getId());
            result.addMessage(message);
        }
        return result;
    }

    public PanelResult deleteById(int Id) throws DataException {
        PanelResult result = new PanelResult();
        if (!repository.deleteById(Id)) {
            result.addMessage("Failed to delete panel with id" + Id);
            return result;
        } else {
            repository.deleteById(Id);
        }
        return result;
    }// general purpose validation

    private PanelResult validate(Panel p) {
        PanelResult result = new PanelResult();
        if (p == null) {
            result.addMessage("Panel cannot be null.");
        }
        if (p.getSection().isBlank()) {
            result.addMessage("Section name cannot be blank.");
        }
        if (p.getRow() <= 0 || p.getRow() >= 250) {
            result.addMessage(String.format("%s%s%s", "Row ", p.getRow(), " out of range"));
        }
        if (p.getColumn() <= 0 || p.getColumn() >= 250) {
            result.addMessage(String.format("%s%s%s", "Column ", p.getColumn(), " out of range"));
        }
        if (p.getInstallationYear() > 2022) {
            result.addMessage("Year cannot be in the future, not " + p.getInstallationYear());
        }
        if (p.getInstallationYear() < 1950) {
            result.addMessage("Year cannot be before solar panels were created i.e. not  " + p.getInstallationYear());
        }
        if (p.getMaterial() != Material.AMORPHOUS_SILICON
                && p.getMaterial() != Material.CADMIUM_TELLURIDE
                && p.getMaterial() != Material.COPPER_IRIDIUM_GALLIUM_SELENIDE
                && p.getMaterial() != Material.MONOCRYSTALINE_SILICON
                && p.getMaterial() != Material.MULTICRYSTALLINE_SILICON
                && p.getMaterial() == null) {
            result.addMessage("Material must be one of the required materials");
        }
        if (!p.getTracking().equalsIgnoreCase("yes") && !p.getTracking().equalsIgnoreCase("no")) {
            result.addMessage("Tracking must be either yes or no, not " + p.getTracking());
        }
        return result;
    }
}
