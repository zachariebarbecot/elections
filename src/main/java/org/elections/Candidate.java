package org.elections;

import java.util.Objects;

public class Candidate {
    enum Type {OFFICIAL, NOT_OFFICIAL;}

    private final String name;
    private final Type type;
    private final Integer numberOfVotes;

    private Candidate(String name, Type type, Integer numberOfVotes) {
        this.name = Objects.requireNonNull(name);
        this.type = Objects.requireNonNull(type);
        this.numberOfVotes = Objects.requireNonNull(numberOfVotes);
    }

    public static Candidate valueOf(String name, Type type) {
        return new Candidate(name, type, 0);
    }

    public Candidate addVote() {
        return new Candidate(name, type, numberOfVotes + 1);
    }

    public Boolean isSameName(String otherName) {
        return name.equals(otherName);
    }

    public Boolean isNameEmpty() {
        return name.isEmpty();
    }

    public Boolean isOfficial() {
        return type.equals(Type.OFFICIAL);
    }

    public String getName() {
        return name;
    }

    public Integer getNumberOfVotes() {
        return numberOfVotes;
    }
}
