// Subclass 1: RegularTricycle (ito yung bumabyahe ng linya / sabay-sabay ang pasahero)
public class RegularTricycle extends Tricycle {
    private int maxSeats;

    // Overloaded Constructor 1
    public RegularTricycle(String bodyNumber, String plateNumber) {
        super(bodyNumber, plateNumber);
        setMaxSeats(4); // default 4 seats
    }

    // Overloaded Constructor 2: Gumagamit ng super() para ipasa sa Tricycle parent class
    public RegularTricycle(String bodyNumber, String plateNumber, double baseFare, boolean hasFranchiseSticker, int maxSeats) {
        super(bodyNumber, plateNumber, baseFare, hasFranchiseSticker);
        setMaxSeats(maxSeats);
    }

    public int getMaxSeats() {
        return maxSeats;
    }

    public void setMaxSeats(int maxSeats) {
        // Validation: 1 hanggang 6 lang pwedeng isakay sa regular trip
        if (maxSeats < 1 || maxSeats > 6) {
            throw new IllegalArgumentException("Max Seats for Regular Line must be between 1 and 6.");
        }
        this.maxSeats = maxSeats;
    }

    // Override para sa Polymorphism:
    // Sa Regular, ang kita per byahe ay base fare times kung ilang upuan meron.
    @Override
    public double calculateTripCollection() {
        return getBaseFare() * maxSeats;
    }

    @Override
    public String getDispatchType() {
        return "Regular Line (" + maxSeats + " seats)";
    }
}