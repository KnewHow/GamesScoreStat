package split;

public class TargetData {
    String uid;
    String FirstName;
    String FamilyName;
    Integer base;
    Integer improve;
    Integer total;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getFamilyName() {
        return FamilyName;
    }

    public void setFamilyName(String familyName) {
        FamilyName = familyName;
    }

    public Integer getBase() {
        return base;
    }

    public void setBase(Integer base) {
        this.base = base;
    }

    public Integer getImprove() {
        return improve;
    }

    public void setImprove(Integer improve) {
        this.improve = improve;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "split.TargetData{" +
                "uid='" + uid + '\'' +
                ", FirstName='" + FirstName + '\'' +
                ", FamilyName='" + FamilyName + '\'' +
                ", base=" + base +
                ", improve=" + improve +
                ", total=" + total +
                '}';
    }
}
