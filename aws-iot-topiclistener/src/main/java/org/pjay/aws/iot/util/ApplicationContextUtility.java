/**
 * 
 */
package org.pjay.aws.iot.util;

/**
 * @author Vijay Konduru
 *
 */
public class ApplicationContextUtility {
	
	private static ApplicationContextUtility applicationContextUtility = new ApplicationContextUtility();
	
	// Autowire Not Working
	//@Autowired
	private ApplicationContextBean applicationContextBean;
	
	private ApplicationContextUtility() {
	}
	
	public static ApplicationContextUtility getInstance(){
		return applicationContextUtility;
	}
	
	public ApplicationContextBean getApplicationContextBean() {
		return applicationContextBean;
	}
	
	public void setApplicationContextBean(ApplicationContextBean applicationContextBean) {
		this.applicationContextBean = applicationContextBean;
	}
}
