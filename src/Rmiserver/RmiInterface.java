package Rmiserver;

import java.rmi.*;

public interface RmiInterface extends Remote{
	public String addCourse(String courseID,String semester) throws RemoteException;
	public String removeCourse(String courseID,String semester) throws RemoteException;
	public String listCourseAvailability(String semester) throws RemoteException;
	public String enrolCourse(String studentID, String courseID,String semester) throws RemoteException;
	public String getClassSchedule(String studentID) throws RemoteException;
	public String dropCourse(String studentID,String courseID) throws RemoteException;

}



