// Abstract base class natin. Hindi to pwedeng i-instantiate directly (bawal yung new Tricycle()).
// Dito nakalagay yung common attributes ng lahat ng tricycle sa TODA.
public abstract class Tricycle implements Inspectable {

    // Private lahat ng fields para pasok sa Encapsulation
    private String bodyNumber;
    private String plateNumber;
    private double baseFare;
    private boolean hasFranchiseSticker;

    // Static member para ma-track natin kung ilang tricycle na yung nare-register overall
    private static int totalRegistered = 0;

    // Overloaded Constructor 1: Pag body number at plate number lang ang binigay,
    // automatic 15.0 yung base fare tapos true agad yung sticker.
    public Tricycle(String bodyNumber, String plateNumber) {
        this(bodyNumber, plateNumber, 15.0, true);
    }

    // Overloaded Constructor 2: Full parameters, tinatawag yung setters para dumaan sa validation
    public Tricycle(String bodyNumber, String plateNumber, double baseFare, boolean hasFranchiseSticker) {
        setBodyNumber(bodyNumber);
        setPlateNumber(plateNumber);
        setBaseFare(baseFare);
        setHasFranchiseSticker(hasFranchiseSticker);
        totalRegistered++; // dagdag sa static counter kada gawa ng object
    }

    // Getters at Setters na may kasamang validation logic
    public String getBodyNumber() {
        return bodyNumber;
    }

    public void setBodyNumber(String bodyNumber) {
        // Validation: bawal blanko o mas maikli sa 3 characters
        if (bodyNumber == null || bodyNumber.trim().length() < 3) {
            throw new IllegalArgumentException("Body Number must be at least 3 characters.");
        }
        this.bodyNumber = bodyNumber.trim().toUpperCase();
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        // Validation: bawal walang laman yung plate number
        if (plateNumber == null || plateNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Plate Number cannot be empty.");
        }
        this.plateNumber = plateNumber.trim().toUpperCase();
    }

    public double getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(double baseFare) {
        // Validation: dapat nasa 10 pesos hanggang 500 pesos lang yung base fare
        if (baseFare < 10.0 || baseFare > 500.0) {
            throw new IllegalArgumentException("Base Fare must be between 10.0 and 500.0 PHP.");
        }
        this.baseFare = baseFare;
    }

    public boolean getHasFranchiseSticker() {
        return hasFranchiseSticker;
    }

    public void setHasFranchiseSticker(boolean hasFranchiseSticker) {
        this.hasFranchiseSticker = hasFranchiseSticker;
    }

    public static int getTotalRegistered() {
        return totalRegistered;
    }

    public static void setTotalRegistered(int count) {
        if (count >= 0) {
            totalRegistered = count;
        }
    }

    // Abstract methods: walang body dito kasi yung subclasses (Regular at Special)
    // ang bahalang mag-compute kung paano yung sarili nilang kita at dispatch type.
    public abstract double calculateTripCollection();
    public abstract String getDispatchType();

    // Implementation nung Inspectable interface para sa Tricycle
    @Override
    public boolean isCompliant() {
        return hasFranchiseSticker && plateNumber.length() >= 4;
    }

    @Override
    public String checkCompliance() {
        if (isCompliant()) {
            return "COMPLIANT (Valid Sticker & Plate)";
        } else {
            return "NON-COMPLIANT (Missing Sticker or Invalid Plate)";
        }
    }
}