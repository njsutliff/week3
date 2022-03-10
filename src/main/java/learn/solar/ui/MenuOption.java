package learn.solar.ui;

public enum MenuOption {
    EXIT("Exit"),
    FIND_BY_SECTION("Find Panels by Section"),
    ADD_PANEL("Add a Panel"),
    UPDATE_PANEL("Update a Panel"),
    REMOVE_PANEL("Remove a Panel");

    MenuOption(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    private final String title;
}
