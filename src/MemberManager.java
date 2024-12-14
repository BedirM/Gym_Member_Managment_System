import java.io.*;
import java.util.ArrayList;
import java.util.List;

class MemberManager {

    private static final String FILE_PATH = "members.gym";

    // Üyeleri dosyadan yükle
    public static List<Member> loadMembers() {
        List<Member> members = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(","); // Veriyi virgülle ayır
                    if (parts.length == 6) {
                        String firstName = parts[0].trim();
                        String lastName = parts[1].trim();
                        int age = Integer.parseInt(parts[2].trim());
                        String membershipType = parts[3].trim();
                        String startDate = parts[4].trim();
                        String endDate = parts[5].trim();
                        members.add(new Member(firstName, lastName, age, membershipType, startDate, endDate));
                    }
                }
            } catch (IOException | NumberFormatException e) {
                System.out.println("Veri yüklenirken bir hata oluştu: " + e.getMessage());
            }
        } else {
            System.out.println("Mevcut veri bulunamadı, sıfırdan başlıyoruz.");
        }
        return members;
    }

    // Üyeleri dosyaya kaydet
    public static void saveMembers(List<Member> members) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Member member : members) {
                writer.write(String.format("%s,%s,%d,%s,%s,%s%n",
                        member.firstName,
                        member.lastName,
                        member.age,
                        member.membershipType,
                        member.startDate,
                        member.endDate));
            }
        } catch (IOException e) {
            System.out.println("Veri kaydedilirken bir hata oluştu: " + e.getMessage());
        }
    }
}
