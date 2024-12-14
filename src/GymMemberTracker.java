import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.io.*;

public class GymMemberTracker {

    static HashSet<Member> members = new HashSet<>();

    public static void main(String[] args) {
        // Üyeleri dosyadan yükle
        members = new HashSet<>(MemberManager.loadMembers());

        // Ana pencereyi oluştur
        JFrame frame = new JFrame("Gym Üye Takip Sistemi");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createTitledBorder("Gym Üye Takip Sistemi"));
        mainPanel.setBackground(new Color(240, 248, 255));

        JLabel baslik = new JLabel("Spor Salonu Üye Takip Sistemi", JLabel.CENTER);
        baslik.setFont(new Font("Arial", Font.BOLD, 24));
        baslik.setForeground(new Color(34, 34, 59));
        mainPanel.add(baslik, BorderLayout.NORTH);

        DefaultListModel<Member> memberListModel = new DefaultListModel<>();
        JList<Member> memberList = new JList<>(memberListModel);
        memberList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        memberList.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(memberList);

        // Listeyi üyelerle doldur
        members.forEach(memberListModel::addElement);

        // Butonlar paneli
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 6, 10, 10));

        JButton addButton = new JButton("Üye Ekle");
        JButton editButton = new JButton("Üye Düzenle");
        JButton deleteButton = new JButton("Üye Sil");
        JButton sortButton = new JButton("Sıralama");
        JButton searchButton = new JButton("Sorgulama");
        JButton showAllButton = new JButton("Tüm Üyeleri\nGöster");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(sortButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(showAllButton);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Üye ekle butonu
        addButton.setBackground(new Color(34, 139, 34));
        addButton.setPreferredSize(new Dimension(150, 75));
        addButton.addActionListener(e -> {
            MemberForm.showMemberForm(frame, memberListModel, null);
            updateMembersFromModel(memberListModel);
        });

        // Üye düzenle butonu
        editButton.setBackground(new Color(0, 145, 255));
        editButton.setPreferredSize(new Dimension(150, 75));
        editButton.addActionListener(e -> {
            Member selectedMember = memberList.getSelectedValue();
            if (selectedMember != null) {
                MemberForm.showMemberForm(frame, memberListModel, selectedMember);
                updateMembersFromModel(memberListModel);
            } else {
                JOptionPane.showMessageDialog(frame, "Lütfen düzenlemek için bir üye seçin.", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Üye sil butonu
        deleteButton.setBackground(new Color(255, 0, 0));
        deleteButton.setPreferredSize(new Dimension(150, 75));
        deleteButton.addActionListener(e -> {
            Member selectedMember = memberList.getSelectedValue();
            if (selectedMember != null) {
                members.remove(selectedMember);
                memberListModel.removeElement(selectedMember);
                MemberManager.saveMembers(new ArrayList<>(members));
            } else {
                JOptionPane.showMessageDialog(frame, "Lütfen silmek için bir üye seçin.", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Sıralama butonu
        sortButton.setBackground(new Color(255, 221, 0));
        sortButton.setPreferredSize(new Dimension(150, 75));
        sortButton.addActionListener(e -> {
            String[] options = {"İsim", "Başlangıç Tarihi"};
            String choice = (String) JOptionPane.showInputDialog(frame, "Sıralama Kriteri Seçin:",
                    "Sıralama", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            if (choice != null) {
                List<Member> sortedMembers = members.stream()
                        .sorted((m1, m2) -> {
                            if (choice.equals("İsim")) {
                                return m1.firstName.compareTo(m2.firstName);
                            } else {
                                return m1.startDate.compareTo(m2.startDate);
                            }
                        })
                        .collect(Collectors.toList());

                memberListModel.removeAllElements();
                sortedMembers.forEach(memberListModel::addElement);
                updateMembersFromModel(memberListModel);
            }
        });

        // Sorgulama butonu
        searchButton.setBackground(new Color(2, 255, 197));
        searchButton.setPreferredSize(new Dimension(150, 75));
        searchButton.addActionListener(e -> {
            String[] options = {"Ad", "Başlangıç Tarihi"};
            String choice = (String) JOptionPane.showInputDialog(frame, "Sorgulama Kriteri Seçin:",
                    "Sorgulama", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            if (choice != null) {
                String query = JOptionPane.showInputDialog(frame, "Aradığınız bilgiyi girin:");
                if (query != null && !query.trim().isEmpty()) {
                    List<Member> filteredMembers = members.stream()
                            .filter(m -> {
                                if (choice.equals("Ad")) {
                                    return m.firstName.toLowerCase().contains(query.toLowerCase());
                                } else {
                                    return m.startDate.contains(query);
                                }
                            })
                            .collect(Collectors.toList());

                    memberListModel.removeAllElements();
                    if (filteredMembers.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Hiçbir üye bulunamadı.", "Sorgulama Sonucu", JOptionPane.INFORMATION_MESSAGE);
                    }
                    filteredMembers.forEach(memberListModel::addElement);
                }
            }
        });

        // Tüm üyeleri göster butonu
        showAllButton.setBackground(new Color(255, 255, 255));
        showAllButton.setPreferredSize(new Dimension(150, 75));
        showAllButton.addActionListener(e -> {
            memberListModel.removeAllElements();
            members.forEach(memberListModel::addElement);
        });

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private static void updateMembersFromModel(DefaultListModel<Member> model) {
        members.clear();
        for (int i = 0; i < model.size(); i++) {
            members.add(model.getElementAt(i));
        }
        MemberManager.saveMembers(new ArrayList<>(members));
    }
}