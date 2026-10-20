// Unrelated class to sa Tricycle (kasi tao to, hindi sasakyan),
// pero nag-i-implement din siya ng Inspectable interface.
public class Driver implements Inspectable {
    private String licenseNo;
    private String fullName;
    private int yearsExperience;
    private boolean hasValidLicense;

    // Overloaded Constructor 1
    public Driver(String licenseNo, String fullName) {
        this(licenseNo, fullName, 1, true);
    }

    // Overloaded Constructor 2
    public Driver(String licenseNo, String fullName, int yearsExperience, boolean hasValidLicense) {
        setLicenseNo(licenseNo);
        setFullName(fullName);
        setYearsExperience(yearsExperience);
        setHasValidLicense(hasValidLicense);
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        if (licenseNo == null || licenseNo.trim().length() < 5) {
            throw new IllegalArgumentException("License No. must be at least 5 characters.");
        }
        this.licenseNo = licenseNo.trim().toUpperCase();
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Driver Full Name cannot be empty.");
        }
        this.fullName = fullName.trim();
    }

    public int getYearsExperience() {
        return yearsExperience;
    }

    public void setYearsExperience(int yearsExperience) {
        if (yearsExperience < 0 || yearsExperience > 60) {
            throw new IllegalArgumentException("Years of experience must be between 0 and 60.");
        }
        this.yearsExperience = yearsExperience;
    }

    public boolean getHasValidLicense() {
        return hasValidLicense;
    }

    public void setHasValidLicense(boolean hasValidLicense) {
        this.hasValidLicense = hasValidLicense;
    }

    // Para masabing compliant yung driver, dapat valid yung lisensya at at least 1 year na nagda-drive
    @Override
    public boolean isCompliant() {
        return hasValidLicense && yearsExperience >= 1;
    }

    @Override
    public String checkCompliance() {
        if (isCompliant()) {
            return "CLEARED TO DRIVE (" + yearsExperience + " yrs exp)";
        } else {
            return "SUSPENDED (Expired License or under 1 yr exp)";
        }
    }
}