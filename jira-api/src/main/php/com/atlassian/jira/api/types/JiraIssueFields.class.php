<?php
/* This class is part of the XP framework
 *
 * $Id$ 
 */

  /**
   * Represent the JIRA issue fields
   *
   * @see https://developer.atlassian.com/display/JIRADEV/The+Shape+of+an+Issue+in+JIRA+REST+APIs
   * @test xp://com.atlassian.jira.unittest.api.types.JiraIssueFieldsTest
   * @purpose  Issue
   */
  class JiraIssueFields extends Object {
    protected
      $summary= NULL,
      $progress= NULL,
      $issueType= NULL,
      $timespent= NULL,
      $reporter= NULL,
      $created= NULL,
      $updated= NULL,
      $priority= NULL,
      $description= NULL,
      $issuelinks= NULL,
      $subtasks= NULL,
      $status= NULL,
      $labels= NULL,
      $workratio= NULL,
      $project= NULL,
      $environment= NULL,
      $aggregateprogress= NULL,
      $components= NULL,
      $timeoriginalestimate= NULL,
      $votes= NULL,
      $fixVersions= NULL,
      $resolution= NULL,
      $resolutionDate= NULL,
      $aggregatetimeoriginalestimate= NULL,
      $duedate= NULL,
      $watchers= NULL,
      $assignee= NULL,
      $versions= NULL,
      $timeestimate= NULL;
    
    /**
     * Return summary
     * 
     * @return string
     */
    public function getSummary() {
      return $this->summary;
    }

    /**
     * Set summary
     * 
     * @param string summary The summary text
     */
    public function setSummary($summary) {
      $this->summary= $summary;
    }

    public function getProgress() {
      return $this->progress;
    }

    public function setProgress($progress) {
      $this->progress= $progress;
    }

    /**
     * Return issue type
     * 
     * @return com.atlassian.jira.api.types.JiraIssueType
     */
    public function getIssueType() {
      return $this->issueType;
    }

    /**
     * Set issue type
     * 
     * @param com.atlassian.jira.api.types.JiraIssueType issueType The issue type to set
     */
    public function setIssueType($issueType) {
      $this->issueType= $issueType;
    }

    public function getTimespent() {
      return $this->timespent;
    }

    public function setTimespent($timespent) {
      $this->timespent= $timespent;
    }
    
    /**
     * Get reporter
     * 
     * @return com.atlassian.jira.api.types.JiraPerson
     */
    public function getReporter() {
      return $this->reporter;
    }

    /**
     * Set reporter
     * 
     * @param com.atlassian.jira.api.types.JiraPerson assignee The reporter person
     */
    public function setReporter($reporter) {
      $this->reporter = $reporter;
    }

    /**
     * Return creation date
     * 
     * @return util.Date
     */
    public function getCreated() {
      return $this->created;
    }

    /**
     * Set creation date
     * 
     * @param util.Date created The creation date
     */
    public function setCreated($created) {
      $this->created = $created;
    }

    /**
     * Return date of last update
     * 
     * @return util.Date
     */
    public function getUpdated() {
      return $this->updated;
    }

    /**
     * Set date of last update
     * 
     * @param util.Date updated The updated date
     */
    public function setUpdated($updated) {
      $this->updated= $updated;
    }

    public function getPriority() {
      return $this->priority;
    }

    public function setPriority($priority) {
      $this->priority= $priority;
    }

    public function getDescription() {
      return $this->description;
    }

    public function setDescription($description) {
      $this->description= $description;
    }

    public function getIssuelinks() {
      return $this->issuelinks;
    }

    public function setIssuelinks($issuelinks) {
      $this->issuelinks= $issuelinks;
    }

    public function getSubtasks() {
      return $this->subtasks;
    }

    public function setSubtasks($subtasks) {
      $this->subtasks= $subtasks;
    }

    /**
     * Return status
     * 
     * @return com.atlassian.jira.api.types.JiraStatus
     */
    public function getStatus() {
      return $this->status;
    }

    /**
     * Set status
     * 
     * @param com.atlassian.jira.api.types.JiraStatus status The status
     */
    public function setStatus($status) {
      $this->status = $status;
    }

    public function getLabels() {
      return $this->labels;
    }

    public function setLabels($labels) {
      $this->labels= $labels;
    }

    public function getWorkratio() {
      return $this->workratio;
    }

    public function setWorkratio($workratio) {
      $this->workratio= $workratio;
    }

    /**
     * Return associated project
     * 
     * @return com.atlassian.jira.api.types.JiraProject
     */
    public function getProject() {
      return $this->project;
    }

    /**
     * Set associated project
     * 
     * @param com.atlassian.jira.api.types.JiraProject project The project to associate
     */
    public function setProject($project) {
      $this->project= $project;
    }

    public function getEnvironment() {
      return $this->environment;
    }

    public function setEnvironment($environment) {
      $this->environment= $environment;
    }

    public function getAggregateprogress() {
      return $this->aggregateprogress;
    }

    public function setAggregateprogress($aggregateprogress) {
      $this->aggregateprogress= $aggregateprogress;
    }

    /**
     * Return list of associated components
     * 
     * @return com.atlassian.jira.api.types.JiraComponent[]
     */
    public function getComponents() {
      return $this->components;
    }

    /**
     * Set associated components
     * 
     * @param com.atlassian.jira.api.types.JiraComponent[] components The associated components
     */
    public function setComponents($components) {
      $this->components= $components;
    }

    public function getTimeoriginalestimate() {
      return $this->timeoriginalestimate;
    }

    public function setTimeoriginalestimate($timeoriginalestimate) {
      $this->timeoriginalestimate= $timeoriginalestimate;
    }

    public function getVotes() {
      return $this->votes;
    }

    public function setVotes($votes) {
      $this->votes= $votes;
    }

    public function getFixVersions() {
      return $this->fixVersions;
    }

    public function setFixVersions($fixVersions) {
      $this->fixVersions= $fixVersions;
    }

    public function getResolution() {
      return $this->resolution;
    }

    public function setResolution($resolution) {
      $this->resolution= $resolution;
    }

    /**
     * Return date of issue resolution
     * 
     * @return util.Date
     */
    public function getResolutionDate() {
      return $this->resolutionDate;
    }

    /**
     * Set date of issue resolution
     * 
     * @param util.Date resolutionDate The date of issue resolution
     */
    public function setResolutionDate($resolutionDate) {
      $this->resolutionDate= $resolutionDate;
    }

    public function getAggregatetimeoriginalestimate() {
      return $this->aggregatetimeoriginalestimate;
    }

    public function setAggregatetimeoriginalestimate($aggregatetimeoriginalestimate) {
      $this->aggregatetimeoriginalestimate= $aggregatetimeoriginalestimate;
    }

    /**
     * Return due date
     * 
     * @return util.Date
     */
    public function getDuedate() {
      return $this->duedate;
    }

    /**
     * Set due date
     * 
     * @param util.Date duedate The due date
     */
    public function setDuedate($duedate) {
      $this->duedate= $duedate;
    }

    /**
     * Get watchers
     * 
     * @return com.atlassian.jira.api.types.JiraPerson[]
     */
    public function getWatchers() {
      return $this->watchers;
    }

    /**
     * Set watchers
     * 
     * @param com.atlassian.jira.api.types.JiraPerson[] watchers The watchers to set
     */
    public function setWatchers($watchers) {
      $this->watchers= $watchers;
    }

    /**
     * Get assignee
     * 
     * @return com.atlassian.jira.api.types.JiraPerson
     */
    public function getAssignee() {
      return $this->assignee;
    }

    /**
     * Set assignee
     * 
     * @param com.atlassian.jira.api.types.JiraPerson assignee The person to assign
     */
    public function setAssignee($assignee) {
      $this->assignee= $assignee;
    }

    public function getVersions() {
      return $this->versions;
    }

    public function setVersions($versions) {
      $this->versions= $versions;
    }

    public function getTimeestimate() {
      return $this->timeestimate;
    }

    public function setTimeestimate($timeestimate) {
      $this->timeestimate= $timeestimate;
    }
  }

?>
