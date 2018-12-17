package Rmiserver;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.HashSet;
import Rmiserver.CompServer;
import Rmiserver.SoenServer;
import Rmiserver.InseServer;

public class Functions extends UnicastRemoteObject implements RmiInterface{

//	private HashMap<String, HashMap<String, Integer>> hashDataComp= new HashMap<String, HashMap<String, Integer>>();
	static HashMap<String, Integer> fetchValues= new HashMap<String, Integer>();
//	private HashMap<String, HashMap<String, Integer>> hashDataSoen= new HashMap<String, HashMap<String, Integer>>();
	static HashMap<String, Integer> fetchValuesS= new HashMap<String, Integer>();
//	private HashMap<String, HashMap<String, Integer>> hashDataInse= new HashMap<String, HashMap<String, Integer>>();
	static HashMap<String, Integer> fetchValuesI= new HashMap<String, Integer>();
	static HashMap<String, HashMap<String, HashSet<String>>> hashStudent= new HashMap<String, HashMap<String, HashSet<String>>>();
	static HashMap<String, Integer> hashdatacompinner = new HashMap<String, Integer>();
	ArrayList<String> arrl = new ArrayList<String>(3);
	static HashMap<String, HashSet<String>> studinner = new HashMap<String, HashSet<String>>();
	static HashSet<String> addCourse;
	static HashSet<String> fetchCourseResult= new HashSet();
	static HashSet<String> fetchCourse= new HashSet();
	static HashSet<String> fetchCourseF= new HashSet();
	static HashSet<String> fetchCourseW= new HashSet();
	static HashSet<String> fetchCourseS= new HashSet();
	int capacity;
	String semester;
	boolean flag = true;
	Boolean enroll=false;
	
	public Functions() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}
	

	@Override
	public String addCourse(String courseID, String semester) throws RemoteException {
		String Result = "fail";
		String AdvDept =courseID.substring(0,4); 
		if(AdvDept.equals("COMP")) {
			fetchValues = CompServer.hashComp.get(semester);
			if(!fetchValues.containsKey(courseID)) {
				fetchValues.put(courseID,10);
				CompServer.hashComp.put(semester,fetchValues);	
				System.out.println(courseID + "successfully added for" + semester + "semester.");
				Result = "pass";
				List<HashMap<String,HashMap<String,Integer>>> d = Arrays.asList(CompServer.hashComp);
				System.out.println(d);
			}else {
				System.out.println(courseID + "is already present for" + semester + "semester.");
			}	
			}else if(AdvDept.equals("SOEN")) {
				fetchValuesS = SoenServer.hashSoen.get(semester);
				if(!fetchValuesS.containsKey(courseID)) {
					fetchValuesS.put(courseID,10);
					SoenServer.hashSoen.put(semester,fetchValuesS);
					System.out.println(courseID + "successfully added for" + semester + "semester.");
					Result = "pass";
					List<HashMap<String,HashMap<String,Integer>>> d = Arrays.asList(SoenServer.hashSoen);
					System.out.println(d);
				}else {
					System.out.println(courseID + "is already present for" + semester + "semester.");
				}
		}else if(AdvDept.equals("INSE")) {
			fetchValuesI = InseServer.hashInse.get(semester);
			if(!fetchValuesI.containsKey(courseID)) {
				fetchValuesI.put(courseID,10);
				InseServer.hashInse.put(semester,fetchValuesI);
				System.out.println(courseID + "successfully added for" + semester + "semester.");
				Result = "pass";
				List<HashMap<String,HashMap<String,Integer>>> d = Arrays.asList(InseServer.hashInse);
				System.out.println(d);
			}else {
				System.out.println(courseID + "is already present for" + semester + "semester.");
			}
		}
		return Result;
	}
	@Override
	public String removeCourse(String courseID, String semester) throws RemoteException {
		String Result = "fail";
		String AdvDept =courseID.substring(0,4);
		if(AdvDept.equals("COMP")) {
			fetchValues = CompServer.hashComp.get(semester);
			if(fetchValues.containsKey(courseID)) {
				fetchValues.remove(courseID);
				CompServer.hashComp.put(semester,fetchValues);	
				flag = true;
				System.out.println(courseID + "successfully removed from" + semester + "semester.");
			}else {
				System.out.println(courseID + "is not present for" + semester + "semester!");
			}
			
		}else if(AdvDept.equals("SOEN")) {
			fetchValuesS = SoenServer.hashSoen.get(semester);
			if(fetchValuesS.containsKey(courseID)) {
				fetchValuesS.remove(courseID);	
				SoenServer.hashSoen.put(semester,fetchValuesS);	
				flag = true;
				System.out.println(courseID + "successfully removed from" + semester + "semester.");
			}else {
				System.out.println(courseID + "is not present for" + semester + "semester!");
			}
			
		}else if(AdvDept.equals("INSE")) {
			fetchValuesI = InseServer.hashInse.get(semester);
			if(fetchValuesI.containsKey(courseID)) {
				fetchValuesI.remove(courseID);	
				InseServer.hashInse.put(semester,fetchValuesI);
				flag = true;
				System.out.println(courseID + "successfully removed from" + semester + "semester.");
			}else {
				System.out.println(courseID + "is not present for" + semester + "semester!");
			}
		}if(flag) {
			Result = "pass";
			hashStudent.keySet().forEach(studentID -> {
				HashMap<String, HashSet<String>> studinner = new HashMap<String, HashSet<String>>();
				studinner = hashStudent.get(studentID);
				fetchCourse = studinner.get(semester);
				if (fetchCourse.contains(courseID)){
					fetchCourse.remove(courseID);
					studinner.put(semester, fetchCourse);
					hashStudent.put(studentID, studinner);
					System.out.println(courseID +" "+"is successfully removed from course cart of student"+" "+studentID+" "+"for semester"+" "+semester);
				}
			});	
			System.out.println("hashStudent:" + " " + hashStudent);
		}
		return Result;
	}
	@Override
	public String listCourseAvailability(String semester) throws RemoteException {
		String Sem = semester.substring(0,semester.length()-1).toUpperCase().trim();
		System.out.println("Semester is" + " " + Sem );
		char D = semester.charAt(semester.length() - 1);
		String Dept = Character.toString(D);
		System.out.println("Department is" + " " + Dept );
		if(Dept.equals("C")) {
			List<HashMap<String,HashMap<String,Integer>>> i = Arrays.asList(CompServer.hashComp);
			System.out.println(i);
			CompServer.listCourse1(Sem);
		}else if(Dept.equals("I")) {
			List<HashMap<String,HashMap<String,Integer>>> i = Arrays.asList(InseServer.hashInse);
			System.out.println(i);
			InseServer.listCourse2(Sem);
		}else if(Dept.equals("S")){
			List<HashMap<String,HashMap<String,Integer>>> i = Arrays.asList(SoenServer.hashSoen);
			System.out.println(i);
			SoenServer.listCourse3(Sem);
		}		
		return "pass";
	}
	
	public static String Student(String studentID, String courseID, String semester) throws RemoteException {
		String Result = "fail";
		int counter=0;
		String StuDept = studentID.substring(0,4).toUpperCase();
		String CDept = courseID.substring(0,4).toUpperCase();
		System.out.println("Student Department is:"+StuDept);
		System.out.println("Course Department is:"+CDept);
		if(!hashStudent.containsKey(studentID)) { 
			addCourse = new HashSet<String>();
			addCourse.add(courseID);
			studinner.put(semester,addCourse);
			hashStudent.put(studentID,studinner);
			System.out.println(studentID + " " + "successfully enrolled for" + " " + courseID  );
			Result = "pass";
			System.out.println(hashStudent);
			}else {
				studinner = hashStudent.get(studentID);
				System.out.println("studinner"+studinner);
				if(!studinner.containsKey(semester)) {
					fetchCourse = new HashSet<String>();
					System.out.println("B");
					}else {
					fetchCourse = studinner.get(semester); }
					System.out.println("fetchCourse"+fetchCourse);
				if (fetchCourse.size() <= 2) {
					if(studinner.containsKey("FALL")) {
						fetchCourseF = studinner.get("FALL");}
						System.out.println("fetchCourseF"+fetchCourseF);
						if(studinner.containsKey("WINTER")) {
						fetchCourseW = studinner.get("WINTER");}
						System.out.println("fetchCourseW"+fetchCourseW);
						if(studinner.containsKey("SUMMER")) {
						fetchCourseS = studinner.get("SUMMER");}
						System.out.println("fetchCourseS"+fetchCourseS);
						if(!(fetchCourseF.contains(courseID)||fetchCourseW.contains(courseID)||fetchCourseS.contains(courseID))) {
							if(CDept.equals(StuDept)) {
								System.out.println("a");
								fetchCourse.add(courseID);
								studinner.put(semester,fetchCourse);
								hashStudent.put(studentID,studinner);
								System.out.println(studentID + " " + "successfully enrolled for" + " " + courseID  );
								System.out.println(hashStudent);
								Result = "pass";	
							}else {
								System.out.println("b");
								System.out.println("fetchCourseResult"+fetchCourseResult);
								boolean bResult1 = fetchCourseResult.addAll(fetchCourseF);
								boolean bResult2 = fetchCourseResult.addAll(fetchCourseW);
								boolean bResult3 = fetchCourseResult.addAll(fetchCourseS);
								System.out.println("fetchCourseResult"+fetchCourseResult);
								for (String Courseid : fetchCourseResult) {
									System.out.println(Courseid);
									String CourseDept = Courseid.substring(0,4).toUpperCase();
									if(!(CourseDept.equals(StuDept))) {
										counter = counter + 1;
										System.out.println("counter is" + counter);
									}}
									if(counter<2){
										fetchCourse.add(courseID);
										studinner.put(semester,fetchCourse);
										hashStudent.put(studentID,studinner);
										System.out.println(studentID + " " + "successfully enrolled for" + " " + courseID  );
										System.out.println(hashStudent);
										Result = "pass";	
									}else {
										Result = "fail";
										System.out.println("Student is already enrolled for maximum no. of out of department courses");
									}			
							}							
						}// no duplicate course
						else {
							Result = "fail";
							System.out.println("Student is already enrolled for course" + courseID);
						}
				}//size>4
				else {
					Result = "fail";
					System.out.println("Student is already enrolled for maximum no. of courses in" + " " +semester);
				}
		}//hashstudent contains student
		System.out.println("Result is"+Result);
		return Result;
	}
	
	
	@Override
	public String enrolCourse(String studentID, String courseID, String semester) throws RemoteException {
		String Result = "fail";
		String s = null;
		HashMap<String, HashSet<String>> studinner = new HashMap<String, HashSet<String>>();
		String StuDept = studentID.substring(0,4).toUpperCase();
	//	String CourseDept = courseID.substring(0,4).toUpperCase();
		if(StuDept.equals("COMP")) {
			s = CompServer.CourseEnrol(studentID, courseID, semester);
		//	String s = CompServer.CourseEnrol(studentID,courseID,semester);
			System.out.println(s);
		}else if(StuDept.equals("INSE")) {
			s = SoenServer.CourseEnrol(studentID,courseID,semester);
			System.out.println(s);
		}else if(StuDept.equals("SOEN")) {
			s = SoenServer.CourseEnrol(studentID,courseID,semester);
			System.out.println(s);
		}else {
			System.out.println("Invalid Student Department!");
		}
		if(s.equals("pass")) {
			Result = "pass";
		}
		return Result;
	}
			
	@Override
	public String getClassSchedule(String studentID) throws RemoteException {
		if(!hashStudent.containsKey(studentID)) {
			System.out.println(studentID + " " + "is not enrolled in any of the courses.");
		}else {
			studinner = hashStudent.get(studentID);
			if(studinner.containsKey("FALL")) {
			fetchCourseF = studinner.get("FALL");}
			if(studinner.containsKey("WINTER")) {
			fetchCourseW = studinner.get("WINTER");}
			if(studinner.containsKey("SUMMER")) {
			fetchCourseS = studinner.get("SUMMER");}
			System.out.println(studentID + " " + "is enrolled in below courses:");
			System.out.println("Fall:" + " " + fetchCourseF);
			System.out.println("Winter:" + " " + fetchCourseW);
			System.out.println("Summer:" + " " + fetchCourseS);
		}
		return "pass";
	}

	@Override
	public String dropCourse(String studentID, String courseID) throws RemoteException {
		String Result = "fail";
		HashMap<String, HashSet<String>> studinner = new HashMap<String, HashSet<String>>();
		System.out.println("Inside drop");
		boolean flag=false;
		if(!hashStudent.containsKey(studentID)) {
			System.out.println(studentID + " " + "is not enrolled in any course.");
		}else {
			System.out.println(studentID + "is the student id");
			studinner = hashStudent.get(studentID);
			System.out.println("studinner is" + " " + studinner);	
			System.out.println("hashStudent:" + " " + hashStudent);
			System.out.println("student id is" + studentID);
			if(studinner.containsKey("FALL")) {
			fetchCourseF = studinner.get("FALL");} //inner hashmap to hashset
			System.out.println(fetchCourseF);
			if(studinner.containsKey("WINTER")) {
			fetchCourseW = studinner.get("WINTER");}
			System.out.println(fetchCourseW);
			if(studinner.containsKey("SUMMER")) {
			fetchCourseS = studinner.get("SUMMER");}
			System.out.println(fetchCourseS);
			if(fetchCourseF.contains(courseID)) {
				semester = "FALL";
				System.out.println("fetchCourseF is " +fetchCourseF);
				fetchCourseF.remove(courseID); //removing course from list
				System.out.println("fetchCourseF is " +fetchCourseF);
				System.out.println("2");
				System.out.println(hashStudent);
				studinner.put("FALL",fetchCourseF);
				System.out.println("3");
				System.out.println(hashStudent);
				System.out.println("student id is" + studentID);
				hashStudent.put(studentID,studinner);
				System.out.println("6");
				System.out.println(hashStudent);
				flag=true;
			}else if(fetchCourseW.contains(courseID)) {
				semester = "WINTER";
				fetchCourseW.remove(courseID);
				studinner.put("WINTER",fetchCourseW);
				hashStudent.put(studentID,studinner);
				flag=true;
			}else if(fetchCourseS.contains(courseID)) {
				semester = "SUMMER";
				fetchCourseS.remove(courseID);
				studinner.put("SUMMER",fetchCourseS);
				hashStudent.put(studentID,studinner);
				flag=true;
			}else {
				System.out.println(studentID + " " + "is not enrolled in course" + courseID);
			}
			if (flag){
				Result = "pass";
			System.out.println(studentID + " " + "successfully removed course" + " " + courseID);
			String StuDept = studentID.substring(0,4).toUpperCase();
			if(StuDept.equals("COMP")) {
				String s =CompServer.DropCourse(courseID,semester);
				System.out.println(s);
			}else if(StuDept.equals("INSE")) {
				String s =InseServer.DropCourse(courseID,semester);
				System.out.println(s);
			}else if(StuDept.equals("SOEN")) {
				String s =SoenServer.DropCourse(courseID,semester);
				System.out.println(s);
			}
			}
/*			if(StuDept.equals("COMP")) {
				fetchValues = CompServer.hashComp.get(semester);
				capacity = fetchValues.get(courseID);
				capacity = capacity + 1;
				fetchValues.put(courseID, capacity);
				CompServer.hashComp.put(semester, fetchValues);
				List<HashMap<String,HashMap<String,Integer>>> g = Arrays.asList(CompServer.hashComp);
				System.out.println(g);
			}else if(StuDept.equals("SOEN")) {
				fetchValuesS = SoenServer.hashSoen.get(semester);
				capacity = fetchValuesS.get(courseID);
				capacity = capacity + 1;
				fetchValuesS.put(courseID, capacity);
				SoenServer.hashSoen.put(semester, fetchValuesS);
				List<HashMap<String,HashMap<String,Integer>>> h = Arrays.asList(SoenServer.hashSoen);
				System.out.println(h);
			}else if(StuDept.equals("INSE")) {
				fetchValuesI = InseServer.hashInse.get(semester);
				capacity = fetchValuesI.get(courseID);
				capacity = capacity + 1;
				fetchValuesI.put(courseID, capacity);
				InseServer.hashInse.put(semester, fetchValuesI);
				List<HashMap<String,HashMap<String,Integer>>> i = Arrays.asList(InseServer.hashInse);
				System.out.println(i);
			}
*/		}
		return Result;
	}
}




