package fr.vecolo.vekanban.plugin_1;

import fr.vecolo.vekanban.plugin_api.exceptions.CardLabelRessourceException;
import fr.vecolo.vekanban.plugin_api.models.*;
import fr.vecolo.vekanban.plugin_api.services.CardLabelService;
import fr.vecolo.vekanban.plugin_api.services.CardService;
import fr.vecolo.vekanban.plugin_api.services.UserService;
import org.kohsuke.github.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class GithubPluginService {
    private final static Logger logger = LoggerFactory.getLogger(GithubPluginService.class);

    private final GitHub github;
    private final UserService userService;
    private final CardService cardService;
    private final CardLabelService cardLabelService;

    @Autowired
    public GithubPluginService(UserService userService, CardService cardService, CardLabelService cardLabelService) throws IOException {
        this.userService = userService;
        this.cardService = cardService;
        this.cardLabelService = cardLabelService;
        github = GitHub.connectAnonymously();
    }

    @Transactional
    public void run(Board board, String githubUrl) throws Exception {
        if (!StringUtils.hasLength(githubUrl) || board == null) {
            throw new GithubPluginException("Empty repo or board");
        }
        githubUrl = cleanRepoLink(githubUrl);
        logger.info("Running retrieve on '" + githubUrl + "' for '" + board.getName() + "' board");
        GHRepository repository;
        try {
            repository = github.getRepository(githubUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        logger.info("Repo name  : " + repository.getName());
        for (GHIssue issue : repository.getIssues(GHIssueState.OPEN)) {
            if (!issue.isPullRequest()) {
                processIssue(board, issue, CardStatus.TODO);
            }
        }
        for (GHIssue issue : repository.getIssues(GHIssueState.CLOSED)) {
            if (!issue.isPullRequest()) {
                processIssue(board, issue, CardStatus.DONE);
            }
        }
    }

    private String cleanRepoLink(String githubUrl) {
        githubUrl = githubUrl.replace("https://", "");
        githubUrl = githubUrl.replace("http://", "");
        githubUrl = githubUrl.replace("www.", "");
        githubUrl = githubUrl.replace("github.com/", "");
        return githubUrl;
    }

    @Transactional
    protected void processIssue(Board board, GHIssue issue, CardStatus status) throws Exception {
        Card card = processCard(board, issue, status);

        processAssignedUser(board, issue, card);

        processLabels(board, issue, card);

        cardService.saveOrUpdateCard(card);
    }

    @Transactional
    protected void processLabels(Board board, GHIssue issue, Card card) throws CardLabelRessourceException {
        if (!issue.getLabels().isEmpty()) {
            List<CardLabel> cardLabels = new ArrayList<>();
            List<CardLabel> labels = cardLabelService.getAllCardLabelFromBoard(board);
            for (GHLabel label : issue.getLabels()) {
                CardLabel cardLabel = labels.stream().filter(l -> l.getName().equals(label.getName())).findFirst().orElse(null);
                if (cardLabel == null) {
                    cardLabel = new CardLabel(label.getName(), "#" + label.getColor(), board);
                    cardLabel = cardLabelService.saveOrUdateCardLabel(cardLabel);
                }
                cardLabels.add(cardLabel);
            }
            card.setLabels(cardLabels);
        }
    }

    @Transactional
    protected void processAssignedUser(Board board, GHIssue issue, Card card) throws Exception {
        GHUser assignedUser = issue.getAssignee();
        if (assignedUser != null) {
            logger.info("Assigné : " + assignedUser.getLogin() + " - " + assignedUser.getName());
            List<User> users = userService.getMembersFromBoard(board);
            users.add(board.getOwner());
            User user = users.stream().filter(u -> u.getPseudo().equals(assignedUser.getLogin())).findFirst().orElse(null);
            card.setAssignedUser(user);
        }
    }

    protected Card processCard(Board board, GHIssue issue, CardStatus status) {
        logger.info(issue.getNumber() + " : " + issue.getTitle());
//        System.out.println("Description :\n" + issue.getBody());
        Card card = new Card(issue.getTitle(), issue.getBody(), status, null);
        card.setAssignedBoard(board);
        return card;
    }
}
