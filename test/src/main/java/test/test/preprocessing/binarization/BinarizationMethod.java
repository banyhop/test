package test.test.preprocessing.binarization;

public enum BinarizationMethod {
    OTSU("Otsu"),
    SAUVOLA("Sauvola");

    private String name;

    BinarizationMethod(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
