package apiAutomation.framework.dataFactory;

import apiAutomation.framework.models.jiraModels.request.CreateIssueRequest;
import apiAutomation.framework.models.jiraModels.request.Fields;
import apiAutomation.framework.models.jiraModels.request.Issuetype;
import apiAutomation.framework.models.jiraModels.request.Project;
import apiAutomation.utilities.ConfigurationManager;
import apiAutomation.utilities.ConfigurationProfile;
import apiAutomation.utilities.TestDataLoader;

import java.security.SecureRandom;
import java.util.List;

public final class JiraDataFactory {

    private static final List<String> ANIMALS = List.of(
            "Lion", "Tiger", "Elephant", "Giraffe", "Zebra", "Kangaroo", "Panda", "Penguin",
            "Dolphin", "Eagle", "Sheep", "Goat", "Rabbit", "Fox", "Wolf", "Bear", "Deer", "Hippo", "Rhino",
            "Cheetah", "Leopard", "Crocodile", "Alligator", "Ostrich", "Flamingo", "Peacock", "Parrot", "Toucan",
            "Swan", "Otter", "Seal", "Walrus", "Moose", "Buffalo", "Antelope", "Lemur", "Meerkat", "Armadillo",
            "Sloth", "Chameleon", "Iguana", "Tortoise", "Hedgehog", "Porcupine", "Raccoon", "Skunk", "Beaver",
            "Badger", "Weasel", "Ferret", "Mongoose", "Vulture", "Hawk", "Falcon", "Cobra", "Viper", "Python",
            "Anaconda", "Toad", "Frog", "Salamander", "Newt", "Crab", "Lobster", "Shrimp", "Jellyfish", "Wasp",
            "Starfish", "Seahorse", "Clam", "Oyster", "Squid", "Octopus", "Butterfly", "Moth", "Beetle", "Ant",
            "Dragonfly", "Grasshopper", "Cicada", "Termite", "Cockroach", "Fly", "Mosquito", "Bee"
    );

    private static final SecureRandom RANDOM = new SecureRandom();
    private static String randomAnimalName() {
        return ANIMALS.get(RANDOM.nextInt(ANIMALS.size()));
    }
    private static String randomBugSummary() {
        return randomAnimalName() + " bug - " + RANDOM.nextInt(1, 9999);
    }



    private static final String CREATE_ISSUE_REQUEST_TEMPLATE = "data/jira/CreateIssueRequest.json";
    static ConfigurationManager config = ConfigurationManager.getInstance(ConfigurationProfile.JIRA);

    private static CreateIssueRequest issueRequestTemplate() {
        return TestDataLoader.load(CREATE_ISSUE_REQUEST_TEMPLATE, CreateIssueRequest.class);
    }

    public static CreateIssueRequest createIssueRequestBody() {

        CreateIssueRequest request = issueRequestTemplate();
        Fields fields = request.getFields();

        Issuetype issueType = fields.getIssuetype();
        Project project = fields.getProject();

        project.setKey(config.getProperty("jira.projectKey"));
        issueType.setName(config.getProperty("jira.issueType"));

        fields.setSummary(randomBugSummary());
        return request;
    }

}

