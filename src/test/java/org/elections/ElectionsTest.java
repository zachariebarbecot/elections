package org.elections;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

class ElectionsTest {

    @Test
    void run_legacy_for_election_without_districts() {
        Map<String, List<String>> voters = voters();

        LegacyElections legacyElections = new LegacyElections(voters, false);
        legacyElections.addCandidate("Michel");
        legacyElections.addCandidate("Jerry");
        legacyElections.addCandidate("Johnny");

        legacyElections.voteFor("Bob", "Jerry", "District 1");
        legacyElections.voteFor("Jerry", "Jerry", "District 2");
        legacyElections.voteFor("Anna", "Johnny", "District 1");
        legacyElections.voteFor("Johnny", "Johnny", "District 3");
        legacyElections.voteFor("Matt", "Donald", "District 3");
        legacyElections.voteFor("Jess", "Joe", "District 1");
        legacyElections.voteFor("Simon", "", "District 2");
        legacyElections.voteFor("Carole", "", "District 3");

        ElectionsWithoutDistricts elections = new ElectionsWithoutDistricts(voters.values().stream().flatMap(List::stream).collect(toSet()));
        elections.addCandidate("Michel");
        elections.addCandidate("Jerry");
        elections.addCandidate("Johnny");

        elections.voteFor("Jerry");
        elections.voteFor("Jerry");
        elections.voteFor("Johnny");
        elections.voteFor("Johnny");
        elections.voteFor("Donald");
        elections.voteFor("Joe");
        elections.voteFor("");
        elections.voteFor("");

        assertThat(elections.results()).isEqualTo(legacyElections.results());
    }

    @Test
    void electionWithoutDistricts() {
        Map<String, List<String>> voters = voters();
        LegacyElections elections = new LegacyElections(voters, false);
        elections.addCandidate("Michel");
        elections.addCandidate("Jerry");
        elections.addCandidate("Johnny");

        elections.voteFor("Bob", "Jerry", "District 1");
        elections.voteFor("Jerry", "Jerry", "District 2");
        elections.voteFor("Anna", "Johnny", "District 1");
        elections.voteFor("Johnny", "Johnny", "District 3");
        elections.voteFor("Matt", "Donald", "District 3");
        elections.voteFor("Jess", "Joe", "District 1");
        elections.voteFor("Simon", "", "District 2");
        elections.voteFor("Carole", "", "District 3");

        Map<String, String> results = elections.results();

        Map<String, String> expectedResults = Map.of(
                "Jerry", "50,00%",
                "Johnny", "50,00%",
                "Michel", "0,00%",
                "Blank", "25,00%",
                "Null", "25,00%",
                "Abstention", "11,11%");
        assertThat(results).isEqualTo(expectedResults);
    }

    @Test
    void electionWithDistricts() {
        Map<String, List<String>> voters = voters();
        LegacyElections elections = new LegacyElections(voters, true);
        elections.addCandidate("Michel");
        elections.addCandidate("Jerry");
        elections.addCandidate("Johnny");

        elections.voteFor("Bob", "Jerry", "District 1");
        elections.voteFor("Jerry", "Jerry", "District 2");
        elections.voteFor("Anna", "Johnny", "District 1");
        elections.voteFor("Johnny", "Johnny", "District 3");
        elections.voteFor("Matt", "Donald", "District 3");
        elections.voteFor("Jess", "Joe", "District 1");
        elections.voteFor("July", "Jerry", "District 1");
        elections.voteFor("Simon", "", "District 2");
        elections.voteFor("Carole", "", "District 3");

        Map<String, String> results = elections.results();

        Map<String, String> expectedResults = Map.of(
                "Jerry", "66,67%",
                "Johnny", "33,33%",
                "Michel", "0,00%",
                "Blank", "22,22%",
                "Null", "22,22%",
                "Abstention", "0,00%");
        assertThat(results).isEqualTo(expectedResults);
    }

    private Map<String, List<String>> voters() {
        return Map.of(
                "District 1", Arrays.asList("Bob", "Anna", "Jess", "July"),
                "District 2", Arrays.asList("Jerry", "Simon"),
                "District 3", Arrays.asList("Johnny", "Matt", "Carole")
        );
    }
}
