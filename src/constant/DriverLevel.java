package constant;

public enum DriverLevel {

    LEVEL_A("LEVEL_A"),
    LEVEL_B("LEVEL_B"),
    LEVEL_C("LEVEL_C"),
    LEVEL_D("LEVEL_D"),
    LEVEL_E("LEVEL_E"),
    LEVEL_F("LEVEL_F");

    public String value;

    DriverLevel(String value) {
        this.value = value;
    }
}
