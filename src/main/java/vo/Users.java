package vo;

import grisu.backend.hibernate.JobStatDAO;
import grisu.backend.model.User;

public class Users{

	private String dn;
	private String userName;
	private String jobCount;
	private String activeJobCount;
	private String runningJobCount;
	private String pendingJobCount;

	public String getDn() {
		return dn;
	}

	public void setDn(String dn) {
		this.dn = dn;
		
		String usrName = dn.substring(dn.indexOf("CN=")+3, dn.length());
		if(usrName.contains(" ")){
			usrName=usrName.substring(0, usrName.lastIndexOf(" "));
		}
		this.userName=usrName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setJobCount(String jobCount) {
		this.jobCount = jobCount;
	}

	public void setActiveJobCount(String activeJobCount) {
		this.activeJobCount = activeJobCount;
	}
	
	public String getJobCount(){
		return jobCount;
	}
	
	public String getActiveJobCount(){
		return activeJobCount;
	}

	public String getRunningJobCount() {
		return runningJobCount;
	}

	public void setRunningJobCount(String runningJobCount) {
		this.runningJobCount = runningJobCount;
	}

	public String getPendingJobCount() {
		return pendingJobCount;
	}

	public void setPendingJobCount(String pendingJobCount) {
		this.pendingJobCount = pendingJobCount;
	}
}
