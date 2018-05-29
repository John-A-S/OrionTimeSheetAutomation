package com.orion.qa.Listeners;

/*import java.util.ArrayList;
import java.util.List;
*/
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import net.rcarz.jiraclient.BasicCredentials;
import net.rcarz.jiraclient.Field;
import net.rcarz.jiraclient.Issue;
import net.rcarz.jiraclient.JiraClient;
import net.rcarz.jiraclient.JiraException;

import com.orion.qa.base.OrionBase;
import com.orion.qa.utils.CommonMethods;


/*import net.rcarz.jiraclient.greenhopper.Backlog;
import net.rcarz.jiraclient.greenhopper.Epic;
import net.rcarz.jiraclient.greenhopper.GreenHopperClient;
import net.rcarz.jiraclient.greenhopper.Marker;
import net.rcarz.jiraclient.greenhopper.RapidView;
import net.rcarz.jiraclient.greenhopper.Sprint;
import net.rcarz.jiraclient.greenhopper.SprintIssue;
import net.rcarz.jiraclient.greenhopper.SprintReport;
*/


// NOTE: SCROLL DOWN FOR CODES RELATED TO UPDATING THE EXISTING BUG IN JIRA
// https://infomaticscorp.atlassian.net/rest/api/latest/issue/createmeta?projectKeys=TES&issuetypeNames=Bug

public class TestNGtoJIRAListener implements ITestListener {
	
	public void createNewJiraIssue(ITestResult result, 
			String projectName, 
			String defectType, 
			String defectSummary,
	        String defectDescription, 
	        String defectAssignee) {

    	OrionBase.log.info("Reading JIRA credentials from the input file to log defects");

		BasicCredentials creds = new BasicCredentials(CommonMethods.readTestData("JIRA", "JIRA_UserName"), CommonMethods.readTestData("JIRA", "JIRA_Password"));
		JiraClient jira = new JiraClient(CommonMethods.readTestData("JIRA", "JIRA_URL"), creds);
		
		try {
	        if (result.getStatus() == ITestResult.FAILURE) {
	        	OrionBase.log.info("Creating defect in JIRA for the Project: "+projectName);
	        	//Create new issue 
	        	Issue newIssue = jira.createIssue(projectName, defectType)
	        							.field(Field.SUMMARY, defectSummary)
	        							.field(Field.DESCRIPTION, defectDescription + result.toString() + "\n" + result.getThrowable())
	        							.field(Field.PRIORITY, "Medium")
	        							.field(Field.ASSIGNEE, defectAssignee).execute();
	        	OrionBase.log.info("New issue has been created in JIRA with Key : " + newIssue.getKey());
	        }
	    } catch (JiraException ex) {
	        System.err.println(ex.getMessage());

	        if (ex.getCause() != null)
	            System.err.println(ex.getCause().getMessage());
	    }
	}

	public void onTestStart(ITestResult result) {
		// TODO Auto-generated method stub
	}

	public void onTestSuccess(ITestResult result) {
		// TODO Auto-generated method stub
	}

	public void onTestFailure(ITestResult result) {
		// TODO Auto-generated method stub
    	OrionBase.log.info("Inside JIRA Listener.  Ready to create an issue due to test failure");
		
		createNewJiraIssue(result, 
					CommonMethods.readTestData("JIRA", "JIRA_ProjectName"),
					CommonMethods.readTestData("JIRA", "JIRA_DefectType"),
					CommonMethods.readTestData("JIRA", "JIRA_DefectSummary"),
					"Issue details are : \n",  
					CommonMethods.readTestData("JIRA", "JIRA_Assignee"));	
	}

	public void onTestSkipped(ITestResult result) {
		// TODO Auto-generated method stub
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub
		
	}

	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub
		
	}

	public void onFinish(ITestContext context) {
		// TODO Auto-generated method stub
		
	}
	

	
	
	
	
	

	
	
	
	
	/*	public void QuickStartJiraIntegeration() {
		  BasicCredentials creds = new BasicCredentials("batman", "pow! pow!");
	        JiraClient jira = new JiraClient("https://jira.example.com/jira", creds);

	        try {
	             Retrieve issue TEST-123 from JIRA. We'll get an exception if this fails. 
	            Issue issue = jira.getIssue("TEST-123");

	             Print the issue key. 
	            System.out.println(issue);

	             You can also do it like this: 
	            System.out.println(issue.getKey());

	             Vote for the issue. 
	            issue.vote();

	             And also watch it. Add Robin too. 
	            issue.addWatcher(jira.getSelf());
	            issue.addWatcher("robin");

	             Open the issue and assign it to batman. 
	            issue.transition()
	                .field(Field.ASSIGNEE, "batman")
	                .execute("Open");
	                
	             Assign the issue 
	            issue.update()
	                .field(Field.ASSIGNEE, "batman")
	                .execute();

	             Add two comments, with one limited to the developer role. 
	            issue.addComment("No problem. We'll get right on it!");
	            issue.addComment("He tried to send a whole Internet!", "role", "Developers");

	             Print the reporter's username and then the display name 
	            System.out.println("Reporter: " + issue.getReporter());
	            System.out.println("Reporter's Name: " + issue.getReporter().getDisplayName());

	             Print existing labels (if any). 
	            for (String l : issue.getLabels())
	                System.out.println("Label: " + l);

	             Change the summary and add two labels to the issue. The double-brace initialiser
	               isn't required, but it helps with readability. 
	            issue.update()
	                .field(Field.SUMMARY, "tubes are clogged")
	                .field(Field.LABELS, new ArrayList() {{
	                    addAll(issue.getLabels());
	                    add("foo");
	                    add("bar");
	                }})
	                .field(Field.PRIORITY, Field.valueById("1"))  you can also set the value by ID 
	                .execute();

	             You can also update values with field operations. 
	            issue.update()
	                .fieldAdd(Field.LABELS, "baz")
	                .fieldRemove(Field.LABELS, "foo")
	                .execute();

	             Print the summary. We have to refresh first to pickup the new value. 
	            issue.refresh();
	            System.out.println("New Summary: " + issue.getSummary());

	             Now let's start progress on this issue. 
	            issue.transition().execute("Start Progress");

	             Add the first comment and update it 
	            Comment comment = issue.addComment("I am a comment!");
	            comment.update("I am the first comment!");
	            issue.getComments().get(0).update("this works too!");

	             Pretend customfield_1234 is a text field. Get the raw field value... 
	            Object cfvalue = issue.getField("customfield_1234");

	             ... Convert it to a string and then print the value. 
	            String cfstring = Field.getString(cfvalue);
	            System.out.println(cfstring);

	             And finally, change the value. 
	            issue.update()
	                .field("customfield_1234", "new value!")
	                .execute();

	             Pretend customfield_5678 is a multi-select box. Print out the selected values. 
	            List<CustomFieldOption> cfselect = Field.getResourceArray(
	                CustomFieldOption.class,
	                issue.getField("customfield_5678"),
	                jira.getRestClient()
	            );
	            for (CustomFieldOption cfo : cfselect)
	                System.out.println("Custom Field Select: " + cfo.getValue());
	               
	             Print out allowed values for the custom multi-select box. 
	            List<CustomFieldOption> allowedValues = jira.getCustomFieldAllowedValues("customfield_5678", "TEST", "Task");
	            for (CustomFieldOption customFieldOption : allowedValues)
	                System.out.println(customFieldOption.getValue());

	             Set two new values for customfield_5678. 
	            issue.update()
	                .field("customfield_5678", new ArrayList() {{
	                    add("foo");
	                    add("bar");
	                    add(Field.valueById("1234"));  you can also update using the value ID 
	                }})
	                .execute();
	                
	             Add an attachment 
	            File file = new File("C:\\Users\\John\\Desktop\\screenshot.jpg");
	            issue.addAttachment(file);

	             And finally let's resolve it as incomplete. 
	            issue.transition()
	                .field(Field.RESOLUTION, "Incomplete")
	                .execute("Resolve Issue");

	             Create a new issue. 
	            Issue newIssue = jira.createIssue("TEST", "Bug")
	                .field(Field.SUMMARY, "Bat signal is broken")
	                .field(Field.DESCRIPTION, "Commissioner Gordon reports the Bat signal is broken.")
	                .field(Field.REPORTER, "batman")
	                .field(Field.ASSIGNEE, "robin")
	                .execute();
	            System.out.println(newIssue);

	             Link to the old issue 
	            newIssue.link("TEST-123", "Dependency");

	             Create sub-task 
	            Issue subtask = newIssue.createSubtask()
	                .field(Field.SUMMARY, "replace lightbulb")
	                .execute();

	             Search for issues 
	            Issue.SearchResult sr = jira.searchIssues("assignee=batman");
	            System.out.println("Total: " + sr.total);
	            for (Issue i : sr.issues)
	                System.out.println("Result: " + i);

	             Search with paging (optionally 10 issues at a time). There are optional
	               arguments for including/expanding fields, and page size/start. 
	            Issue.SearchResult sr = jira.searchIssues("project IN (GOTHAM) ORDER BY id");
	            while (sr.iterator().hasNext())
	                System.out.println("Result: " + sr.iterator().next());

	        } catch (JiraException ex) {
	            System.err.println(ex.getMessage());

	            if (ex.getCause() != null)
	                System.err.println(ex.getCause().getMessage());
	        }

	}
*/
	
	/*
	public void GreenHopperExample() {
		BasicCredentials creds = new BasicCredentials("batman", "pow! pow!");
        JiraClient jira = new JiraClient("https://jira.example.com/jira", creds);
        GreenHopperClient gh = new GreenHopperClient(jira);

        try {
             Retrieve all Rapid Boards 
            List<RapidView> allRapidBoards = gh.getRapidViews();

             Retrieve a specific Rapid Board by ID 
            RapidView board = gh.getRapidView(123);

             Print the name of all current and past sprints 
            List<Sprint> sprints = board.getSprints();
            for (Sprint s : sprints)
                System.out.println(s);

             Get the sprint report, print the sprint start date
               and the number of completed issues 
            SprintReport sr = board.getSprintReport(s);
            System.out.println(sr.getSprint().getStartDate());
            System.out.println(sr.getCompletedIssues().size());

             Get backlog data 
            Backlog backlog = board.getBacklogData();

             Print epic names 
            for (Epic e : backlog.getEpics())
                System.out.println(e);

             Print all issues in the backlog 
            for (SprintIssue si : backlog.getIssues())
                System.out.println(si);

             Print the names of sprints that haven't started yet 
            for (Marker m : backlog.getMarkers())
                System.out.println(m);

             Get the first issue on the backlog and add a comment 
            SprintIssue firstIssue = backlog.getIssues().get(0);
            Issue jiraIssue = firstIssue.getJiraIssue();
            jiraIssue.addComment("a comment!");
        } catch (JiraException ex) {
            System.err.println(ex.getMessage());

            if (ex.getCause() != null)
                System.err.println(ex.getCause().getMessage());
        }
    }
*/


}
