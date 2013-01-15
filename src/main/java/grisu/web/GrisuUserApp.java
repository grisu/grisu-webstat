package grisu.web;

import grisu.backend.hibernate.JobDAO;
import grisu.backend.hibernate.JobStatDAO;
import grisu.backend.hibernate.UserDAO;
import grisu.backend.model.User;
import grisu.backend.model.job.JobStat;
import grisu.control.exceptions.NoSuchJobException;

import java.sql.SQLClientInfoException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import vo.Users;

import com.vaadin.Application;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.ui.*;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Driver;

//import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;

public class GrisuUserApp extends Application {
	@Override
	public void init() {
		Window mainWindow = new Window("Grisu");


			
			final Table t2 = new Table("Job Table");
			//JobStatDAO jobStatDAO = new JobStatDAO();
			
			UserDAO userDao = new UserDAO();
			List<User> users = userDao.findAllUsers();
			
			
			List<Users> userList = new LinkedList<Users>();
			Users temp;
			String dn;
			String usrName;
			for(User usr:users)
			{
				temp= new Users();
				dn= usr.getDn();
				
				temp.setDn(usr.getDn());
				
				usrName = dn.substring(dn.indexOf("CN=")+3, dn.length());
				if(usrName.contains(" ")){
					usrName=usrName.substring(0, usrName.lastIndexOf(" "));
				}
				temp.setUserName(usrName);
				userList.add(temp);
			}
			
			//List<JobStat> jobs = jobStatDAO.findAllJobs();
//			List<JobStat> jobs=null;
//			try {
//				jobs = jobStatDAO.getSimilarJobNamesByDN("DC=nz,DC=org,DC=bestgrid,DC=slcs,O=The University of Auckland,CN=Markus Binsteiner _bK32o4Lh58A3vo9kKBcoKrJ7ZY", "gromacs%");
//			} catch (NoSuchJobException e) {
//				e.printStackTrace();
//			}
			//System.out.println("job list size:"+jobs.size());
			System.out.println("user list size:"+users.size());
			//BeanItemContainer<JobStat> jobStatContainer = new BeanItemContainer<JobStat>(JobStat.class);
			BeanItemContainer<Users> userContainer = new BeanItemContainer<Users>(Users.class);
			
			//jobStatContainer.addAll(jobs);
			userContainer.addAll(userList);
			
			final UserTable userTab = new UserTable("Users");
			userTab.populate(userContainer);
			
			Table temptab = new Table();
			temptab.setContainerDataSource(userContainer);
			
			
//			Panel userPanel=new Panel("Users");
//			userPanel.setScrollable(true);
//			userPanel.addComponent(userTab);
			//mainWindow.addComponent(userPanel);
			HorizontalSplitPanel horiSplitPanel= new HorizontalSplitPanel();
			mainWindow.setContent(horiSplitPanel);
			horiSplitPanel.addComponent(userTab);
			userTab.setHeight("100%");
//			mainWindow.addComponent(temptab);
			
			//horiSplitPanel.addComponent(new JobDetailsComponent());
			
			
			VerticalSplitPanel vertiSplitPanel=new VerticalSplitPanel();
			final JobTable jobTab = new JobTable("Jobs for selected user");
			vertiSplitPanel.addComponent(jobTab);
			
			//vertiSplitPanel.addComponent(temptab);
			
			horiSplitPanel.addComponent(vertiSplitPanel);
			System.out.println("compheight"+horiSplitPanel.getFirstComponent().getParent().getHeightUnits());
			horiSplitPanel.setHeight("100%");
			System.out.println("panelheight"+horiSplitPanel.getHeightUnits()+".."+mainWindow.getHeightUnits());
			horiSplitPanel.getFirstComponent().setSizeFull();
			//horiSplitPanel.getFirstComponent()
			userTab.addListener(new ValueChangeListener() {
				
				public void valueChange(ValueChangeEvent event) {
					// TODO Auto-generated method stub
					Users selectedUser=(Users)userTab.getSelectedUser();
					jobTab.populate(selectedUser.getDn());
				}
			});
			
			final JobDetComponent jobDets = new JobDetComponent();
			vertiSplitPanel.addComponent(jobDets);
			
			jobTab.addListener(new ValueChangeListener() {
				
				public void valueChange(ValueChangeEvent event) {
					// TODO Auto-generated method stub
					JobStat selectedJob = (JobStat)jobTab.getSelectedJob();
					jobDets.populate(selectedJob);
				}
			});
			
			
			
			
		//	t2.setContainerDataSource(jobStatContainer);
/**			t2.setVisibleColumns(new Object[] {"jobname"});
			
			t2.setSelectable(true);
			t2.setColumnCollapsingAllowed(true);
			t2.setColumnReorderingAllowed(true);
			t2.setImmediate(true);
			
			Panel p = new Panel();
			p.setCaption("job panel");
			p.setScrollable(true);
			p.addComponent(t2);
			p.getContent().setSizeUndefined();
			
			mainWindow.addComponent(p);
			
			//final 
			final Panel jobDetailsPanel = new Panel("Job Details");
			mainWindow.addComponent(jobDetailsPanel);
			
//			final Label id = new Label("Job ID: ");
//			final Label jobHibernateId = new Label("jobHibernateId: ");
//			final Label jobname = new Label("Job ID: ");
//			final Label dn = new Label("Job ID: ");
//			final Label jsdl = new Label("Job ID: ");
//			final Label fqan = new Label("Job ID: ");
//			final Label active = new Label("Job ID: ");
//			final Label submittedJobDescription = new Label("Job ID: ");
//			final Label status = new Label("Job ID: ");
//			final Label submissionType = new Label("Job ID: ");
//			
//			jobDetailsPanel.addComponent(jobname);
//			jobDetailsPanel.addComponent(dn);
//			jobDetailsPanel.addComponent(jsdl);
//			jobDetailsPanel.addComponent(fqan);
//			jobDetailsPanel.addComponent(status);
//			jobDetailsPanel.addComponent(submittedJobDescription);
//			jobDetailsPanel.addComponent(submissionType);
			
			// Send changes in selection immediately to server.


			// Shows feedback from selection.
			final Label current = new Label("Selected: -");
			jobDetailsPanel.addComponent(current);

			final JobDetComponent jobDetComp = new JobDetComponent();
			jobDetailsPanel.addComponent(jobDetComp);
			
			final UserTable tmp = new UserTable();
			jobDetailsPanel.addComponent(tmp);
			
	//		tmp.setTable(jobStatContainer);
//			tmp.addListener(new ValueChangeListener() {
//	            public void valueChange(ValueChangeEvent event) {
//
//	            }
//	        });
			
			tmp.addListener(new ValueChangeListener() {
				
				public void valueChange(ValueChangeEvent event) {
					// TODO Auto-generated method stub
					current.setValue((JobStat)tmp.getTabcleClickVal());
					jobDetComp.setJobDetails((JobStat)tmp.getTabcleClickVal());
				}
			});
			
			// Handle selection change.
			t2.addListener(new Property.ValueChangeListener() {
			    public void valueChange(ValueChangeEvent event) {
			        JobStat currJob = (JobStat) t2.getValue();
			        jobDetComp.setJobDetails(currJob);
			    }
			});
			
			**/
			
		setMainWindow(mainWindow);
	}

	public static void main(String[] args){
		GrisuUserApp g = new GrisuUserApp();
		g.init();
	}
}
