package learn.solar.domain;

import learn.solar.models.Panel;

import java.util.ArrayList;

public class PanelResult {
    private ArrayList<String> messages = new ArrayList<>();
    private Panel panel;
    public ArrayList<String> getMessages() {
         return new ArrayList<>(messages);
    }

    public Panel getPanel() {
        return panel;
    }

    public void setPanel(Panel panel) {
        this.panel = panel;
    }

    public boolean isSuccess() {
        return messages.size() == 0;
    }

    public void addMessage(String message) {
        messages.add(message);
    }
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }


}
