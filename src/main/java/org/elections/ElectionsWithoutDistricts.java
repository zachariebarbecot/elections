package org.elections;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ElectionsWithoutDistricts {
    List<String> candidates = new ArrayList<>();
    List<String> officialCandidates = new ArrayList<>();
    ArrayList<Integer> votesWithoutDistricts = new ArrayList<>();
    private Set<String> voters;

    public ElectionsWithoutDistricts(Set<String> voters) {
        this.voters = voters;
    }

    public void addCandidate(String candidate) {
        officialCandidates.add(candidate);
        candidates.add(candidate);
        votesWithoutDistricts.add(0);
    }

    public void voteFor(String candidate) {
        if (candidates.contains(candidate)) {
            int index = candidates.indexOf(candidate);
            votesWithoutDistricts.set(index, votesWithoutDistricts.get(index) + 1);
        } else {
            candidates.add(candidate);
            votesWithoutDistricts.add(1);
        }
    }

    public Map<String, String> results() {
        Map<String, String> results = new HashMap<>();
        Integer nbVotes = 0;
        Integer nullVotes = 0;
        Integer blankVotes = 0;
        int nbValidVotes = 0;

        nbVotes = votesWithoutDistricts.stream().reduce(0, Integer::sum);
        for (int i = 0; i < officialCandidates.size(); i++) {
            int index = candidates.indexOf(officialCandidates.get(i));
            nbValidVotes += votesWithoutDistricts.get(index);
        }

        for (int i = 0; i < votesWithoutDistricts.size(); i++) {
            Float candidatResult = ((float) votesWithoutDistricts.get(i) * 100) / nbValidVotes;
            String candidate = candidates.get(i);
            if (officialCandidates.contains(candidate)) {
                results.put(candidate, String.format(Locale.FRENCH, "%.2f%%", candidatResult));
            } else {
                if (candidates.get(i).isEmpty()) {
                    blankVotes += votesWithoutDistricts.get(i);
                } else {
                    nullVotes += votesWithoutDistricts.get(i);
                }
            }
        }

        float blankResult = ((float) blankVotes * 100) / nbVotes;
        results.put("Blank", String.format(Locale.FRENCH, "%.2f%%", blankResult));

        float nullResult = ((float) nullVotes * 100) / nbVotes;
        results.put("Null", String.format(Locale.FRENCH, "%.2f%%", nullResult));

        int nbElectors = voters.size();
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        float abstentionResult = 100 - ((float) nbVotes * 100 / nbElectors);
        results.put("Abstention", String.format(Locale.FRENCH, "%.2f%%", abstentionResult));

        return results;
    }
}
