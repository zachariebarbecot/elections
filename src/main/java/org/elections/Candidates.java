package org.elections;

import java.util.HashSet;
import java.util.Set;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toUnmodifiableSet;

public class Candidates {

    private final Set<Candidate> values = new HashSet<>();

    public void add(Candidate candidate) {
        values.add(candidate);
    }

    public void voteFor(String candidateName) {
        var candidate = findByName(candidateName);
        values.remove(candidate);
        add(candidate.addVote());
    }

    public Integer getTotalVotes() {
        return values.stream()
                .mapToInt(Candidate::getNumberOfVotes)
                .sum();
    }

    public Integer getTotalValidVotes() {
        return values.stream()
                .filter(Candidate::isOfficial)
                .mapToInt(Candidate::getNumberOfVotes)
                .sum();
    }

    public Integer getTotalNullVotes() {
        return values.stream()
                .filter(not(Candidate::isOfficial))
                .filter(not(Candidate::isNameEmpty))
                .mapToInt(Candidate::getNumberOfVotes)
                .sum();
    }

    public Integer getTotalBlankVotes() {
        return values.stream()
                .filter(not(Candidate::isOfficial))
                .filter(Candidate::isNameEmpty)
                .mapToInt(Candidate::getNumberOfVotes)
                .sum();
    }

    public Set<Candidate> getAllOfficials() {
        return values.stream()
                .filter(Candidate::isOfficial)
                .collect(toUnmodifiableSet());
    }

    private Candidate findByName(String name) {
        return values.stream()
                .filter(candidate -> candidate.isSameName(name))
                .findFirst()
                .orElse(Candidate.valueOf(name, Candidate.Type.NOT_OFFICIAL));
    }
}
