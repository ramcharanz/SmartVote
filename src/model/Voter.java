package model;

public class Voter {
    private String id;
    private String name;
    private boolean verified;
    private boolean voted;

    public Voter(String id, String name) {
        this.id = id;
        this.name = name;
        this.verified = false;
        this.voted = false;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public boolean isVerified() { return verified; }
    public boolean hasVoted() { return voted; }

    public void verify() { verified = true; }
    public void vote() { voted = true; }

    @Override
    public String toString() {
        return id + ": " + name + " | Verified: " + verified + ", Voted: " + voted;
    }
}
