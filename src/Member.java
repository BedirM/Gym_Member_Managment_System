import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;

public class Member implements Serializable {
    String firstName;
    String lastName;
    int age;
    String membershipType;
    String startDate;
    String endDate;




    public Member(String firstName, String lastName, int age, String membershipType, String startDate, String endDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.membershipType = membershipType;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "<html><b>" + firstName + " " + lastName + "</b><br/>Yaş: " + age + "<br/>Tip: " + membershipType + "<br/>Başlangıç: " + startDate + "<br/>Bitiş: " + endDate + "</html>";
    }
    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, age, membershipType, startDate, endDate);
    }
}

