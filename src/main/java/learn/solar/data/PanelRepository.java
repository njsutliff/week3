package learn.solar.data;

import learn.solar.models.Panel;

import java.util.List;

public interface PanelRepository {
    List<Panel> findBySection(String section) throws DataException;
    Panel add(Panel p) throws DataException;
     List<Panel> findAll() throws DataException;

    boolean update(Panel p) throws DataException;

    boolean deleteById(int Id) throws DataException;
}
