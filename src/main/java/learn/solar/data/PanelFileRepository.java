package learn.solar.data;

import learn.solar.models.Material;
import learn.solar.models.Panel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PanelFileRepository implements PanelRepository {
    private static final String DELIMITER = ",";
    private final String filePath;
    private static final String HEADER = "id,section,row,column,installationYear,material,tracking?";

    public PanelFileRepository(String filePath) {
        this.filePath = filePath;
    }


    /**
     * finds all Panels in a section, uses the private findAll method
     *
     * @param section to find Panels in
     * @return list of all Panels in a section
     */
    @Override
    public List<Panel> findBySection(String section) throws DataException {
        ArrayList<Panel> sectionList = new ArrayList<Panel>();

                List<Panel> panel = findAll();
                for (Panel p : panel) {
                    if (Objects.equals(p.getSection(), section)) {
                        sectionList.add(p);
                    }
                }
        return sectionList;
    }

    /**
     * create a Panel
     *
     * @return Panel created
     */
    @Override
    public Panel add(Panel p) throws DataException {
        List<Panel> all = findBySection(p.getSection());

        all.add(p);
        writeAll(all);
        return p;
    }


    /**
     * update a Panel.
     * not required to update Section, Row, or Column, but you must allow editing of other fields.
     *
     * @return true if panel found and updated.
     */
    @Override
    public boolean update(Panel p) throws DataException {
        List<Panel> all = findAll();
        for (int i = 0; i < all.size(); i++) {
            if (p.getRow() == all.get(i).getRow()&& p.getColumn() == all.get(i).getColumn()
                        && p.getSection().equals(all.get(i).getSection())){
                all.set(i, p);
                writeAll(all);
                return true;
            }
        }
        return false;
    }

    /**
     * Delete a panel by Id
     *
     * @param Id to delete panel
     * @return true if deleted else false
     */
    @Override
    public boolean deleteById(int Id) throws DataException {
        List<Panel> all = findAll();
        for (Panel p : all) {
            if (p.getId() == Id) {
                all.remove(p);
                writeAll(all);
                return true;
            }
        }
        return false;
    }

    /**
     * finds all Panels in the data source (file),
     *
     * @return List of all Panels in file
     */
    public List<Panel> findAll() throws DataException {

        ArrayList<Panel> result = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine(); // skip header
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                Panel panel = deserialize(line);
                if (panel != null) {
                    result.add(panel);
                }
            }
        } catch (IOException ex) {
            throw new DataException(ex.getMessage(), ex);
        }

        return result;
    }

    /**
     * Writes data to filepath given a list of all Panels
     *
     * @param panels a list of all Panels
     */
    private void writeAll(List<Panel> panels) throws DataException {
        try (PrintWriter writer = new PrintWriter(filePath)) {
            writer.println(HEADER);
            for (Panel p : panels) {
                writer.println(serialize(p));
            }
        } catch (IOException ex) {
            throw new DataException(ex.getMessage(), ex);
        }
    }

    /**
     * Convert a Panel into a String in the file.
     *
     * @return String of the Panel
     */
    private String serialize(Panel p) {
        return String.format("%s,%s,%s,%s,%s,%s,%s",
                p.getId(),
                p.getSection(),
                p.getRow(),
                p.getColumn(),
                p.getInstallationYear(),
                p.getMaterial(),
                p.getTracking());
    }

    /**
     * Convert a String into a Panel.
     *
     * @return
     */
    private Panel deserialize(String s) {
        String[] fields = s.split(DELIMITER, -1);
        if (fields.length == 7) {
            Panel panel = new Panel();
            panel.setId(Integer.parseInt(fields[0]));
            panel.setSection(fields[1]);
            panel.setRow(Integer.parseInt(fields[2]));
            panel.setColumn(Integer.parseInt((fields[3])));
            panel.setInstallationYear(Integer.parseInt(fields[4]));
            panel.setMaterial(Material.valueOf(fields[5]));
            panel.setTracking((fields[6]));
            return panel;
        }
        return null;
    }
}
