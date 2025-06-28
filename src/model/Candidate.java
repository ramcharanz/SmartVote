package model;

public class Candidate {
    private int id;
    private String name;
    private String party;
    private int votes;

    public Candidate(int id, String name, String party) {
        this.id = id;
        this.name = name;
        this.party = party;
        this.votes = 0;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getParty() { return party; }
    public int getVotes() { return votes; }
    public void vote() { votes++; }

    @Override
    public String toString() {
        return id + ": " + name + " (" + party + ")";
    }
}
