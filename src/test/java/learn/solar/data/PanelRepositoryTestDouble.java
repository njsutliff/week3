package learn.solar.data;

import learn.solar.domain.PanelService;
import learn.solar.models.Material;
import learn.solar.models.Panel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PanelRepositoryTestDouble implements PanelRepository{

    List<Panel> testList = new ArrayList<>(List.of(
            new Panel(1,"test-section",1,1,2014, Material.CADMIUM_TELLURIDE,"yes"),
            new Panel(2,"other-section",1,2,2014, Material.CADMIUM_TELLURIDE,"yes")));
    @Override
    public List<Panel> findBySection(String section) throws DataException {
        List<Panel> result = new ArrayList<>();
        for(int i = 0; i < testList.size(); i++){
            if(testList.get(i).getSection().equals(section)){
                result.add(testList.get(i));
            }
        }
        return result;
    }

    @Override
    public Panel add(Panel p) throws DataException {
        return p;
    }

    @Override
    public List<Panel> findAll() throws DataException {
        List<Panel> result = new ArrayList<>();
        return result;
    }

    @Override
    public boolean update(Panel p) throws DataException {
        return p.getId()==1;
    }

    @Override
    public boolean deleteById(int Id) throws DataException {
        return Id==1;
    }
}
