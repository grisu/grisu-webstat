package grisu.web;

import grisu.backend.hibernate.JobStatDAO;
import grisu.backend.hibernate.UserDAO;
import grisu.backend.model.job.JobStat;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vo.Users;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.DefaultItemSorter;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;

public class UserTable extends CustomComponent {

	@AutoGenerated
	private Panel mainLayout;
	@AutoGenerated
	private Table tblUserDets;
	@AutoGenerated
	private Label lblClient;
	@AutoGenerated
	private Table tblUser;

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	private List<Users> userList;
	BeanItemContainer<Users> userContainer;

	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	
	private static Logger log = LoggerFactory.getLogger(Thread.currentThread().getClass());
	
	public UserTable() {
		
		log.debug("In Constructor");
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// TODO add user code here
		tblUser.setSelectable(true);
		tblUser.setColumnReorderingAllowed(true);
		//mainLayout.setCaption(caption);
		tblUser.setSizeFull();
		tblUser.setPageLength(15);
		tblUser.setHeight("100%");
		tblUser.setWidth("100%");

		
		tblUserDets.addContainerProperty("property", String.class, null);
		tblUserDets.addContainerProperty("value", String.class, null);
		
		tblUserDets.setVisibleColumns(new String [] {"property", "value"});
		tblUserDets.setColumnHeaders(new String [] {"Field", "Value"});
				
		tblUserDets.setCaption("Additional job properties");
//		tblUserDets.setCaption("User Details");
		tblUserDets.setPageLength(6);
		tblUserDets.setSizeFull();
				
		log.debug("Exiting constructor");
	}

	public void addListener(ValueChangeListener valueChangeListener) {
		// TODO Auto-generated method stub
		tblUser.addListener(valueChangeListener);
	}

	public void setTable(BeanItemContainer<JobStat> jobStatContainer) {
		// TODO Auto-generated method stub
		tblUser.setContainerDataSource(jobStatContainer);
	}

	public Object getTabcleClickVal() {
		// TODO Auto-generated method stub
		return tblUser.getValue();
	}

	public Object getSelectedUser() {
		// TODO Auto-generated method stub
		return tblUser.getValue();
	}

	//populate the user-list in left pane
	public void populate() {
		// TODO Auto-generated method stub
	
		log.debug("Entering populate()");
		
		UserDAO userDao = new UserDAO();
		List<String> userDNs=userDao.findUserDNsFromJobStat();
		userList = new LinkedList<Users>();
		
		Users temp = null;
		for(String dn:userDNs){
			temp= new Users();
			temp.setDn(dn);
			userList.add(temp);
		}
		
		/**
		Thread jobCountUpdater = new Thread(){
			public void run() {
				tblUser.setImmediate(true);
				tblUser.setCacheRate(1);
				System.out.println("jobCountUpdater starts");
//				JobStatDAO jsDao = new JobStatDAO();
//				Item tblItem=null;
//				String dn=null;
//				int running;
//				int totJobCount;
//				for(Object id:tblUser.getItemIds())
//				{
//					tblItem=tblUser.getItem(id);
//					dn=(String) tblItem.getItemProperty("dn").getValue();
//					totJobCount=jsDao.findJobCount(dn);
//					if(totJobCount>0){
//						running = jsDao.findRunningJobCount(dn);
//						tblItem.getItemProperty("jobCount").setValue(""+totJobCount);
//						tblItem.getItemProperty("pendingJobCount").setValue(jsDao.findPendingJobCount(dn));
//						tblItem.getItemProperty("runningJobCount").setValue(""+running);							
//					}
//					else{
//
//						tblItem.getItemProperty("jobCount").setValue("0");
//						tblItem.getItemProperty("pendingJobCount").setValue("0");
//						tblItem.getItemProperty("runningJobCount").setValue("0");
//					}
//				}
				refresh();
				tblUser.setContainerDataSource(tblUser.getContainerDataSource());
				tblUser.refreshRowCache();
				//tblUser.getApplication().getMainWindow().executeJavaScript("window.location.href=window.location.href;");
				
				tblUser.requestRepaint();
//				tblUser.refreshCurrentPage();
				tblUser.setVisibleColumns(new Object [] {"userName", "pendingJobCount", "runningJobCount", "jobCount"});
				tblUser.setColumnHeaders(new String[] {"User Names","Pending Jobs","Running Jobs", "Total jobs"});
				System.out.println("jobcountupdater ends");
			}
		};
		**/
		
		userContainer = new BeanItemContainer<Users>(Users.class);

		userContainer.addAll(userList);
		tblUser.setContainerDataSource(userContainer);
		
		//jobCountUpdater.start();
		
		tblUser.setVisibleColumns(new Object [] {"userName", "pendingJobCount", "runningJobCount", "jobCount"});
		tblUser.setColumnHeaders(new String[] {"User Names","Pending Jobs","Running Jobs", "Total jobs"});
		
		tblUser.select(tblUser.firstItemId());

		//tblUser.sort("userName", true);
		tblUser.setSortContainerPropertyId("userName");
		tblUser.setSortAscending(true);
		
		fireComponentEvent();
		
		if(userList.size()<15)
			tblUser.setPageLength(userList.size());
		
		userContainer.setItemSorter(new DefaultItemSorter(new Comparator<Object>() {
			
			public int compare(Object obj1, Object obj2) {
				// TODO Auto-generated method stub
				if(obj1 instanceof String && obj2 instanceof String)
				{
					int i1, i2;
					String s1=(String)obj1;
					String s2=(String)obj2;
					try{
						if(s1.contains("("))
						{
							s1=s1.substring(0,s1.indexOf("(")-1);
							s2=s2.substring(0,s2.indexOf("(")-1);
						}
						i1=Integer.parseInt(s1);
						i2=Integer.parseInt(s2);
						return (i1-i2);
					}
					catch(NumberFormatException nfe){
						return ((String)obj1).toLowerCase().compareTo(((String) obj2).toLowerCase());
					}
				}
				else	
				{
					return ((Integer)obj1-(Integer)obj2);
				}
			}
		}));
		
		tblUser.setCellStyleGenerator(new Table.CellStyleGenerator() {
			
			public String getStyle(Object itemId, Object propertyId) {
				// TODO Auto-generated method stub
				return "black";
			}
		});
		
		log.info("Exiting populate()");
		System.out.println("usertab: exit populate"+System.currentTimeMillis());
	}

	//on refresh
	public void refresh() {
		// TODO Auto-generated method stub
		JobStatDAO jsDao = new JobStatDAO();
		Item tblItem=null;
		String dn=null;
		
		System.out.println("inside refresh");
		
		UserDAO userDao = new UserDAO();
		int newUserCount = userDao.findDNCountfromJobStat();
		int oldUserCount=userContainer.size();
		if(newUserCount>oldUserCount){
			List<String> newUserList = userDao.findDNSortedOnId();
			Users temp = null;
			for(int counter=oldUserCount; counter<newUserCount; counter++){
				dn = newUserList.get(counter);
				temp = new Users();
				temp.setDn(dn);
//				synchronized (userContainer) {
					userContainer.addBean(temp);
//				}
			}
		}

		for(Object id:tblUser.getItemIds())
		{
			tblItem=tblUser.getItem(id);
			dn=(String) tblItem.getItemProperty("dn").getValue();
			try{
//				synchronized (tblItem) {
					tblItem.getItemProperty("jobCount").setValue(""+jsDao.findJobCount(dn));
//				}
				
//				synchronized (tblItem) {
					tblItem.getItemProperty("pendingJobCount").setValue(""+jsDao.findPendingJobCount(dn));
//				}
				
//				synchronized (tblItem) {
					tblItem.getItemProperty("runningJobCount").setValue(""+jsDao.findRunningJobCount(dn));
//				}
			}
			catch(Exception e){
				
			}
		}
		System.out.println("exiting refresh");
	}	
	
	public void populateUserDets(String[] userDetails){
		tblUserDets.setCaption("Details of "+((Users)tblUser.getValue()).getUserName()+"'s jobs");
		tblUserDets.removeAllItems();
		tblUserDets.addItem(new Object [] {"Total Jobs", userDetails[0]},null);
		tblUserDets.addItem(new Object [] {"DN", userDetails[1]},null);
		tblUserDets.addItem(new Object [] {"Client(s)", userDetails[2]},null);
		tblUserDets.addItem(new Object [] {"Application(s)", userDetails[3]},null);
		tblUserDets.addItem(new Object [] {"Average number of CPUs", userDetails[4]},null);
		tblUserDets.addItem(new Object [] {"Average Walltime per CPU", userDetails[5]},null);
	}
	
	
	@AutoGenerated
	private Panel buildMainLayout() {
		// common part: create layout
		mainLayout = new Panel();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		
		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");
		
		// tblUser
		tblUser = new Table();
		tblUser.setImmediate(true);
		tblUser.setWidth("100.0%");
		tblUser.setHeight("100.0%");
		mainLayout.addComponent(tblUser);
		
		// lblClient
		lblClient = new Label();
		lblClient.setImmediate(false);
		lblClient.setWidth("-1px");
		lblClient.setHeight("25px");
		lblClient.setValue("	 ");
		mainLayout.addComponent(lblClient);
		
		// tblUserDets
		tblUserDets = new Table();
		tblUserDets.setImmediate(false);
		tblUserDets.setWidth("-1px");
		tblUserDets.setHeight("-1px");
		mainLayout.addComponent(tblUserDets);
		
		return mainLayout;
	}

}
