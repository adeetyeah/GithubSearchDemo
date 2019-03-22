package com.example.githubsearchdemo;

/**
 * POJO to represent an organization's repository details. Currently stores
 * the repository name, description, github URL and the number of stars.
 * More data can be added in the future.
 */
class OrgRepoData {
    private String name;
    private String description;
    private String githubUrl;
    private int stars;

    // Constructor
    OrgRepoData(String name, String description, String githubUrl, int stars) {
        this.name = name;
        this.description = description;
        this.githubUrl = githubUrl;
        this.stars = stars;
    }

    int getStars() {
        return stars;
    }

    String getDescription() {
        return description;
    }

    String getGithubUrl() {
        return githubUrl;
    }

    String getName() {
        return name;
    }
}
