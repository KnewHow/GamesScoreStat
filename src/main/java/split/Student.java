package split;

public class Student {
    String uid;
    String FirstName;
    String FamilyName;
    String comment;
    Integer score;

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

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "split.Student{" +
                "uid='" + uid + '\'' +
                ", FirstName='" + FirstName + '\'' +
                ", FamilyName='" + FamilyName + '\'' +
                // ", comment='" + comment + '\'' +
                ", score=" + score +
                '}';
    }
}
