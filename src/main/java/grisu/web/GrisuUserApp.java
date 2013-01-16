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
import com.vaadin.ui.Window.ResizeEvent;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Driver;
import com.vaadin.ui.Window.ResizeListener;

//import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;

public class GrisuUserApp extends Application {
	@Override
	public void init() {
		Window mainWindow = new Window("Grisu");

		final Table t2 = new Table("Job Table");

		final UserTable userTab = new UserTable("Users");
	//	userTab.populate();

		HorizontalSplitPanel horiSplitPanel = new HorizontalSplitPanel();
		mainWindow.setContent(horiSplitPanel);
		horiSplitPanel.addComponent(userTab);
		userTab.setHeight("100%");

		VerticalSplitPanel vertiSplitPanel = new VerticalSplitPanel();
		final JobTable jobTab = new JobTable("Jobs for selected user");
		vertiSplitPanel.addComponent(jobTab);

		horiSplitPanel.addComponent(vertiSplitPanel);
		horiSplitPanel.setHeight("100%");
		horiSplitPanel.getFirstComponent().setSizeFull();
		
		horiSplitPanel.setSplitPosition(30);

		userTab.addListener(new ValueChangeListener() {
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				Users selectedUser = (Users) userTab.getSelectedUser();
				if(selectedUser!=null)
					jobTab.populate(selectedUser.getDn());
			}
		});
		

		final JobDetComponent jobDets = new JobDetComponent();
		vertiSplitPanel.addComponent(jobDets);

		jobTab.addListener(new ValueChangeListener() {
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				JobStat selectedJob = (JobStat) jobTab.getSelectedJob();
				jobDets.populate(selectedJob);
			}
		});

		userTab.populate();
		
		mainWindow.getContent().setSizeFull();

		mainWindow.addListener(new ResizeListener() {

			public void windowResized(ResizeEvent e) {
				// TODO Auto-generated method stub
				userTab.setPageSize();

			}
		});

		setMainWindow(mainWindow);
	}

	public static void main(String[] args) {
		GrisuUserApp g = new GrisuUserApp();
		g.init();
	}
}
