package gui;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.HashMap;

public class SmartVote extends JFrame {
    private final Admin admin = new Admin();
    private final Map<Integer, Candidate> candidates = new HashMap<>();
    private final Map<String, Voter> voters = new HashMap<>();

    public SmartVote() {
        showMainMenu();
    }

    private void showMainMenu() {
        getContentPane().removeAll();
        setTitle("SmartVote - Main Menu");
        setLayout(new GridLayout(4, 1));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 250);

        JButton adminBtn = new JButton("🔐 Admin Login");
        JButton registerBtn = new JButton("📝 Register Voter");
        JButton voterLoginBtn = new JButton("🗳️ Voter Login");
        JButton quitBtn = new JButton("🚪 Exit");

        adminBtn.addActionListener(e -> showAdminLogin());
        registerBtn.addActionListener(e -> registerVoter());
        voterLoginBtn.addActionListener(e -> voterLogin());
        quitBtn.addActionListener(e -> System.exit(0));

        add(adminBtn);
        add(registerBtn);
        add(voterLoginBtn);
        add(quitBtn);
        setVisible(true);
    }

    private void showAdminLogin() {
        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        Object[] inputs = {"Username:", userField, "Password:", passField};
        int option = JOptionPane.showConfirmDialog(this, inputs, "Admin Login", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            if (admin.login(userField.getText(), new String(passField.getPassword()))) {
                showAdminDashboard();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid admin credentials!");
            }
        }
    }

    private void showAdminDashboard() {
        getContentPane().removeAll();
        setLayout(new GridLayout(6, 1));
        setTitle("SmartVote - Admin Dashboard");

        JButton addBtn = new JButton("➕ Add Candidate");
        JButton delBtn = new JButton("➖ Remove Candidate");
        JButton votersBtn = new JButton("📋 View Voters");
        JButton resultsBtn = new JButton("📊 View Results");
        JButton logBtn = new JButton("📝 Activity Log");
        JButton logoutBtn = new JButton("🔓 Logout");

        addBtn.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Candidate Name:");
            String party = JOptionPane.showInputDialog("Party Name:");
            if (name != null && party != null) {
                int id = candidates.size() + 1;
                Candidate c = new Candidate(id, name, party);
                candidates.put(id, c);
                admin.log("Added candidate: " + c);
            }
        });

        delBtn.addActionListener(e -> {
            String idStr = JOptionPane.showInputDialog("Enter Candidate ID to remove:");
            if (idStr != null) {
                try {
                    int id = Integer.parseInt(idStr);
                    candidates.remove(id);
                    admin.log("Removed candidate ID: " + id);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid ID!");
                }
            }
        });

        votersBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder("Registered Voters:\n\n");
            for (Voter v : voters.values()) sb.append(v).append("\n");
            showText(sb.toString(), "Voters");
        });

        resultsBtn.addActionListener(e -> {
            int total = candidates.values().stream().mapToInt(Candidate::getVotes).sum();
            StringBuilder sb = new StringBuilder("Election Results:\n\n");
            for (Candidate c : candidates.values()) {
                double pct = total == 0 ? 0 : (100.0 * c.getVotes() / total);
                sb.append(c.getName()).append(" (").append(c.getParty()).append(") — ")
                  .append(c.getVotes()).append(" votes (").append(String.format("%.2f", pct)).append("%)\n");
            }
            showText(sb.toString(), "Results");
        });

        logBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder("Activity Log:\n\n");
            for (String log : admin.getLog()) sb.append(log).append("\n");
            showText(sb.toString(), "Log");
        });

        logoutBtn.addActionListener(e -> {
            admin.logout();
            showMainMenu();
        });

        add(addBtn);
        add(delBtn);
        add(votersBtn);
        add(resultsBtn);
        add(logBtn);
        add(logoutBtn);

        revalidate(); repaint();
    }

    private void registerVoter() {
        String id = JOptionPane.showInputDialog("Enter Voter ID:");
        String name = JOptionPane.showInputDialog("Enter Voter Name:");
        if (id != null && name != null) {
            if (voters.containsKey(id)) {
                JOptionPane.showMessageDialog(this, "Voter already registered.");
            } else {
                Voter v = new Voter(id, name);
                v.verify();
                voters.put(id, v);
                JOptionPane.showMessageDialog(this, "Voter registered and verified!");
            }
        }
    }

    private void voterLogin() {
        String id = JOptionPane.showInputDialog("Enter Voter ID:");
        if (id == null || !voters.containsKey(id)) {
            JOptionPane.showMessageDialog(this, "Voter not found.");
            return;
        }

        Voter voter = voters.get(id);
        if (!voter.isVerified()) {
            JOptionPane.showMessageDialog(this, "Voter not verified.");
        } else if (voter.hasVoted()) {
            JOptionPane.showMessageDialog(this, "You have already voted.");
        } else {
            castVote(voter);
        }
    }

    private void castVote(Voter voter) {
        Object[] options = candidates.values().stream().map(Object::toString).toArray();
        if (options.length == 0) {
            JOptionPane.showMessageDialog(this, "No candidates available.");
            return;
        }
        Object choice = JOptionPane.showInputDialog(this, "Select candidate to vote:",
                "Voting Booth", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        if (choice != null) {
            int id = Integer.parseInt(choice.toString().split(":")[0]);
            candidates.get(id).vote();
            voter.vote();
            JOptionPane.showMessageDialog(this, "✅ Vote cast successfully!\nThank you, " + voter.getName() + "!");
        }
    }

    private void showText(String content, String title) {
        JTextArea area = new JTextArea(content);
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(500, 400));
        JOptionPane.showMessageDialog(this, scroll, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SmartVote::new);
    }
}
