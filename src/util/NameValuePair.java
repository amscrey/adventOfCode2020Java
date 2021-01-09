package util;

public class NameValuePair {
    private String name;
    private String value;

    public NameValuePair(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "util.NameValuePair{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
