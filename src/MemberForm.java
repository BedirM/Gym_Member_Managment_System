import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;

public class MemberForm {

    public static void showMemberForm(JFrame parent, DefaultListModel<Member> model, Member existingMember) {
        JDialog dialog = new JDialog(parent, "Üye Formu", true);
        dialog.setSize(500, 400);
        dialog.setLayout(new GridLayout(8, 2, 10, 10));
        dialog.setLocationRelativeTo(parent);

        JTextField firstNameField = new JTextField(existingMember != null ? existingMember.firstName : "");
        JTextField lastNameField = new JTextField(existingMember != null ? existingMember.lastName : "");
        JTextField ageField = new JTextField(existingMember != null ? String.valueOf(existingMember.age) : "");
        JComboBox<String> membershipTypeBox = new JComboBox<>(new String[]{"VIP", "Standart"});
        membershipTypeBox.setSelectedItem(existingMember != null ? existingMember.membershipType : "VIP");
        JTextField startDateField = new JTextField(existingMember != null ? existingMember.startDate : "");
        JTextField endDateField = new JTextField(existingMember != null ? existingMember.endDate : "");

        dialog.add(new JLabel("Ad:"));
        dialog.add(firstNameField);
        dialog.add(new JLabel("Soyad:"));
        dialog.add(lastNameField);
        dialog.add(new JLabel("Yaş:"));
        dialog.add(ageField);
        dialog.add(new JLabel("Üyelik Tipi:"));
        dialog.add(membershipTypeBox);
        dialog.add(new JLabel("Başlangıç Tarihi (YYYY-MM-DD):"));
        dialog.add(startDateField);
        dialog.add(new JLabel("Bitiş Tarihi (YYYY-MM-DD):"));
        dialog.add(endDateField);

        JButton saveButton = new JButton("Kaydet");
        saveButton.addActionListener(e -> {
            try {
                String firstName = firstNameField.getText().trim();
                String lastName = lastNameField.getText().trim();
                int age = Integer.parseInt(ageField.getText().trim());
                String membershipType = (String) membershipTypeBox.getSelectedItem();
                String startDate = startDateField.getText().trim();
                String endDate = endDateField.getText().trim();

                if (firstName.isEmpty() || lastName.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
                    throw new IllegalArgumentException("Tüm alanlar doldurulmalıdır.");
                }

                validateDate(startDate);
                validateDate(endDate);

                if (existingMember != null) {
                    // Mevcut üyeyi güncelle
                    existingMember.firstName = firstName;
                    existingMember.lastName = lastName;
                    existingMember.age = age;
                    existingMember.membershipType = membershipType;
                    existingMember.startDate = startDate;
                    existingMember.endDate = endDate;

                    model.setElementAt(existingMember, model.indexOf(existingMember));
                } else {
                    // Yeni üye ekle
                    Member newMember = new Member(firstName, lastName, age, membershipType, startDate, endDate);
                    model.addElement(newMember);
                }

                // DefaultListModel'den List<Member> oluştur
                List<Member> memberList = new ArrayList<>();
                for (int i = 0; i < model.size(); i++) {
                    memberList.add(model.get(i));
                }

                // Üyeleri kaydet
                MemberManager.saveMembers(memberList);
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Yaş bir sayı olmalıdır.", "Girdi Hatası", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException | ParseException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Girdi Hatası", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(new JLabel());
        dialog.add(saveButton);

        dialog.setVisible(true);
    }

    private static void validateDate(String dateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        sdf.parse(dateStr);
    }
}
