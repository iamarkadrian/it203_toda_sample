// Subclass 2: SpecialTricycle (ito naman yung arkila o special trip)
public class SpecialTricycle extends Tricycle {
    private double zoneFee;

    // Overloaded Constructor 1
    public SpecialTricycle(String bodyNumber, String plateNumber) {
        super(bodyNumber, plateNumber);
        setZoneFee(30.0);
    }

    // Overloaded Constructor 2
    public SpecialTricycle(String bodyNumber, String plateNumber, double baseFare, boolean hasFranchiseSticker, double zoneFee) {
        super(bodyNumber, plateNumber, baseFare, hasFranchiseSticker);
        setZoneFee(zoneFee);
    }

    public double getZoneFee() {
        return zoneFee;
    }

    public void setZoneFee(double zoneFee) {
        // Validation: bawal negative yung zone fee, max 1000
        if (zoneFee < 0.0 || zoneFee > 1000.0) {
            throw new IllegalArgumentException("Special Zone Fee must be between 0.0 and 1000.0 PHP.");
        }
        this.zoneFee = zoneFee;
    }

    // Override para sa Polymorphism:
    // Pag Special/Arkila, bayad yung buong byahe (3x ng base fare) plus yung dagdag na zone fee.
    @Override
    public double calculateTripCollection() {
        return (getBaseFare() * 3) + zoneFee;
    }

    @Override
    public String getDispatchType() {
        return "Special Charter (+PHP " + zoneFee + " zone fee)";
    }
}