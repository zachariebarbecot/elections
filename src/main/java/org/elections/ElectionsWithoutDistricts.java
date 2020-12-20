package org.elections;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toMap;

public class ElectionsWithoutDistricts {
    private final Candidates candidates = new Candidates();
    private final Set<String> voters;

    public ElectionsWithoutDistricts(Set<String> voters) {
        this.voters = voters;
    }

    public void addCandidate(String candidateName) {
        candidates.add(
                Candidate.valueOf(candidateName, Candidate.Type.OFFICIAL)
        );
    }

    public void voteFor(String candidateName) {
        candidates.voteFor(candidateName);
    }

    public Map<String, String> results() {
        return formatResults(
                candidates.getTotalVotes(),
                candidates.getTotalValidVotes(),
                candidates.getTotalNullVotes(),
                candidates.getTotalBlankVotes()
        );
    }

    private Map<String, String> formatResults(Integer nbVotes, Integer nbValidVotes, Integer nullVotes, Integer blankVotes) {
        Map<String, String> results = new HashMap<>();
        results.put("Blank", formatResult(compute(blankVotes, nbVotes)));
        results.put("Null", formatResult(compute(nullVotes, nbVotes)));
        results.put("Abstention", formatResult(100 - compute(nbVotes, voters.size())));
        results.putAll(
                candidates.getAllOfficials().stream()
                        .collect(toMap(
                                Candidate::getName,
                                candidate -> formatResult(compute(candidate.getNumberOfVotes(), nbValidVotes))
                        ))
        );
        return results;
    }

    private Float compute(Integer i1, Integer i2) {
        return i1.floatValue() * 100 / i2.floatValue();
    }

    private String formatResult(Float result) {
        return String.format(Locale.FRENCH, "%.2f%%", result);
    }
}
