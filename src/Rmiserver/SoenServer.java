package Rmiserver;

import java.rmi.*;
import java.rmi.server.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.net.*;
import java.io.*;
import Rmiserver.Functions;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import Rmiclient.AdvisorClient;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class SoenServer {
		static HashMap<String, Integer> hashin;
		static HashMap<String, HashMap<String, Integer>> hashSoen= new HashMap<String, HashMap<String, Integer>>();
		static HashMap<String, Integer> hash3 = new HashMap<String, Integer>();
		static String DataReceived;
		static String DataReceivedI;
		static String DataReceivedC;
		static String Sem;
		static Logger logger = Logger.getLogger(AdvisorClient.class.getName());
		static private FileHandler fileTxt;
	    static private SimpleFormatter formatterTxt;
	//	static String semReceived;
		static String Operation;
		static HashMap<String,Integer> SoenSem;
		static HashMap<String, Integer> fetchValuesS= new HashMap<String, Integer>();
	   public static void startSoenServer(int RMIPort) throws RemoteException, MalformedURLException {
			try {
				Runnable task = () -> {
					receive();
				};
				Thread thread = new Thread(task);
				thread.start();		
				fileTxt = new FileHandler("C:\\Users\\WEB\\eclipse-workspace\\DCRS_RMI_\\Log Files\\SoenServer.log");
				formatterTxt = new SimpleFormatter();
			    fileTxt.setFormatter(formatterTxt);
			    logger.addHandler(fileTxt);
				CommonServer.startRegistry(RMIPort);
		        Functions rmiSoen = new Functions();
		        Naming.rebind("rmi://localhost:" + RMIPort + "/rmiSoen", rmiSoen);
		        System.out.println("RmiSoen Server registered"); 
		        logger.info("******RmiSoen Server registered******");
		        inputValues("FALL");
		        inputValues("SUMMER");
		        inputValues("WINTER");
			}
			catch(Exception e) {
				System.out.println("Exception in RmiSoen: " + e);
			}
		}
	   
	   
	   public static void inputValues(String term) {

				if(term == "FALL") {
					logger.info("******Adding Fall Courses in Soen Hashmap******");
					hashin = new HashMap<String, Integer>();
					hashin.put("SOEN6543", 10);
					hashin.put("SOEN6441", 10);
					hashin.put("SOEN5431", 10);
					hashin.put("SOEN9812", 10);
					if(!hashSoen.containsKey(term)) {
						hashSoen.put(term, hashin);
					}
				}

				if(term == "SUMMER") {
					logger.info("******Adding Summer Courses in Comp Hashmap******");
					hashin = new HashMap<String, Integer>();
					hashin.put("SOEN6541", 10);
					hashin.put("SOEN7541", 10);
					hashin.put("SOEN6421", 10);
					hashin.put("SOEN6544", 10);
					if(!hashSoen.containsKey(term)) {
						hashSoen.put(term, hashin);
					}

				}
				if(term == "WINTER") {
					logger.info("******Adding Winter Courses in Comp Hashmap******");
					hashin = new HashMap<String, Integer>();
					hashin.put("SOEN6548", 10);
					hashin.put("SOEN6785", 10);
					hashin.put("SOEN7623", 10);
					hashin.put("SOEN4378", 10);
					if(!hashSoen.containsKey(term)) {
						hashSoen.put(term, hashin);
					}
				}
			}	
	   
	   private static void sendMessage(int serverPort) {
			DatagramSocket aSocket = null;
			try {
				aSocket = new DatagramSocket();
				byte[] message = Sem.getBytes();
				InetAddress aHost = InetAddress.getByName("localhost");
				DatagramPacket request = new DatagramPacket(message, Sem.length(), aHost, serverPort);
				aSocket.send(request);
				System.out.println("Request message sent from the client to server with port number " + serverPort + " is: "
						+ new String(request.getData()));
				byte[] buffer = new byte[1000];
				DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
				aSocket.receive(reply);
				DataReceived = null;
				DataReceived = new String(reply.getData()).trim();
				System.out.println("Reply received from the server with port number " + serverPort + " is: "
						+ DataReceived);
			} catch (SocketException e) {
				System.out.println("Socket: " + e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("IO: " + e.getMessage());
			} finally {
				if (aSocket != null)
					aSocket.close();
			}
		}
	   private static String FetchHashmap(String Semester) {
		   logger.info("FetchHashmap:Fetch Soen Server Data for semester"+" "+Semester);
		   if(hashSoen.containsKey(Semester)) {
		   HashMap<String,Integer> Soensem = hashSoen.get(Semester);
		   List<HashMap<String,Integer>> i = Arrays.asList(Soensem);
		   System.out.println(i);
			String	hash = i.toString();
			System.out.println("hash is" +hash);
		    return hash;
	   } else {
			   return "No semester exists";
		   }
	   }
	   
	   private static void receive() {
			DatagramSocket aSocket = null;
			try {
				String	rep = null;
				String  semReceived = null;
				aSocket = new DatagramSocket(5555);
		//		byte[] buffer = new byte[10000];
				System.out.println("Soen Server 5555 Started............");
				while (true) {
					byte[] buffer = new byte[10000];
					DatagramPacket request = new DatagramPacket(buffer, buffer.length);
					System.out.println("Buffer length is"+buffer.length);
					aSocket.receive(request);	
					semReceived = new String(request.getData());
					System.out.println(new String(request.getData()));
					System.out.println("Data received to SOEN server is :" + " " + semReceived);
					String func=semReceived.split(",")[0];
					func=func.trim();
					System.out.println("function is:"+ " "+func);
					if(func.equals("E")) {
						System.out.println("Semester received is :" + " " + semReceived);
						String []parm=semReceived.split(",",4);
						String studentid=parm[1];
						studentid=studentid.trim();
						System.out.println("studentid received is :" + " " + studentid);
						String courseID =parm[2];
						courseID=courseID.trim();
					    System.out.println("courseid received is :" + " " + courseID);
					    String semester =parm[3];
						semester=semester.trim();
						System.out.println("semester received is :" + " " + semester);
						String d=checkCourse(semester,courseID);
						System.out.println("d is"+d);
						if (d.equals("pass")) {
							System.out.println("rep here is"+rep);
						rep = Functions.Student(studentid,courseID,semester);}
						else {
							rep ="fail";
						}
						System.out.println("rep here is--"+rep);
						if(rep.equals("pass")) {
							Capacity(semester,courseID,"ENROLL");}
						}else if(func.equals("L")){
							System.out.println("Semester received is :" + " " + semReceived);
							String []parm=semReceived.split(",",2);
							String Semester=parm[1];
							Semester=Semester.trim();
							rep = FetchHashmap(Semester.trim());
						}else if(func.equals("D")) {
							System.out.println("Semester received is :" + " " + semReceived);
							String []parm=semReceived.split(",",3);
							String courseID =parm[1];
							courseID=courseID.trim();
							System.out.println("courseID received is :" + " " + courseID);
						    String semester =parm[2];
							semester=semester.trim();
							System.out.println("semester received is :" + " " + semester);
							Capacity(semester,courseID,"DROP");
							rep="pass";
							}
					System.out.println("rep is :" + " " + rep);
					buffer = rep.getBytes();
					DatagramPacket reply = new DatagramPacket(buffer, buffer.length, request.getAddress(),
							request.getPort());
					aSocket.send(reply);
				}
			} catch (SocketException e) {
				System.out.println("Socket: " + e.getMessage());
			} catch (IOException e) {
				System.out.println("IO: " + e.getMessage());
			} finally {
				if (aSocket != null)
					aSocket.close();
			}
		}
	
	public static void Capacity(String semester,String courseID,String Operation) {
			logger.info("Capacity:Change capacity for"+" "+courseID+" "+"in semester"+" "+semester);
			fetchValuesS = hashSoen.get(semester);
			int capacity = fetchValuesS.get(courseID);
			if(Operation.equals("ENROLL")) {
				capacity = capacity-1;
				logger.info("Capacity:Capacity decreased by 1");
			}else if(Operation.equals("DROP")) {
				capacity = capacity+1;
				logger.info("Capacity:Capacity increased by 1");
			}
			fetchValuesS.put(courseID, capacity);
			hashSoen.put(semester, fetchValuesS);
			logger.info("Capacity:Soen Hashmap updated with new capacity of course"+" "+courseID);
			List<HashMap<String,HashMap<String,Integer>>> g = Arrays.asList(hashSoen);
			System.out.println(g);
		}
	   
	public static void listCourse3(String semester) {
		logger.info("******listCourse3:SOEN SERVER******");
		if(hashSoen.containsKey(semester)) {
			SoenSem = hashSoen.get(semester);
		}
		List<HashMap<String,Integer>> g = Arrays.asList(SoenSem);
		System.out.println(g);
		logger.info("******listCourse3:Sending message to COMP SERVER******");
		sendMessage(8888);
		DataReceivedC = DataReceived;
		System.out.println(DataReceivedC);
		logger.info("******listCourse3:Sending message to INSE SERVER******");
		sendMessage(7777);
		DataReceivedI = DataReceived;
		System.out.println(DataReceivedI);	
	}

	public static String checkCourse(String semester,String courseId) {
		System.out.println("semester"+semester+"---------"+"courseId"+courseId);
		logger.info("checkCourse: Checking availabilty of course"+" "+courseId+"in semester"+" "+semester);
		String result="fail";
		fetchValuesS = hashSoen.get(semester);
		if(fetchValuesS.containsKey(courseId)) {
			int capacity = fetchValuesS.get(courseId);
			if(capacity>0) {
				logger.info("checkCourse:course"+" "+courseId+"is available in semester"+" "+semester);
				System.out.println("Course is ok!");
				result="pass";}else {
					System.out.println(courseId +" "+"does not have an availability!");
					logger.info("checkCourse:course"+" "+courseId+"is not available in semester"+" "+semester);
				}
			}else {
				System.out.println(courseId +" "+" is not a valid course!");
				logger.info("checkCourse:course"+" "+courseId+"is not a valid course in semester"+" "+semester);
			}
		return result;
	}

	public static String CourseEnrol(String studentID, String courseID, String semester) throws RemoteException {
		logger.info("CourseEnrol: Enrolling course"+" "+courseID+"in semester"+" "+semester+" "+"for student"+" "+studentID);
		   String CourseDept = courseID.substring(0,4).toUpperCase();
		   if(CourseDept.equals("SOEN")) {
			   fetchValuesS = hashSoen.get(semester);
				if(fetchValuesS.containsKey(courseID)) {
					int capacity = fetchValuesS.get(courseID);
					if(capacity>0) {
						String Result = Functions.Student(studentID,courseID,semester);
						if(Result.equals("pass")) {
							Capacity(semester,courseID,"ENROLL");
							logger.info("CourseEnrol:"+" "+studentID+" "+"enrolled in course"+" "+courseID);
							System.out.println("Student enrolled!");
							return Result;
						}else {
							logger.info("CourseEnrol:"+" "+studentID+" "+"could not be enrolled in course"+" "+courseID);
							System.out.println("Student enrolling failed!");
							return Result;
						}
					}else {
						logger.info("CourseEnrol:"+" "+courseID +" "+"does not have an availability!");
						System.out.println(courseID +" "+"does not have an availability!");
						return "fail";
					}
				}else {
					logger.info("CourseEnrol:"+" "+courseID +" "+"is not a valid course!");
					System.out.println(courseID +" "+" is not a valid course!");
					return "fail";
				}		   
		   }else {
			   Operation = "E,";
			   Sem=Operation.concat(studentID).concat(",").concat(courseID).concat(",").concat(semester);
			   System.out.println(Sem);
			   if(CourseDept.equals("COMP")) {
				   	logger.info("CourseEnrol:"+"Sending message to COMP server");
					sendMessage(8888);
					System.out.println("IN Runnable task2");
					System.out.println("DataReceived is :"+DataReceived);
				System.out.println("Below TASK2");
		   }else if(CourseDept.equals("INSE")) {
			   		logger.info("CourseEnrol:"+"Sending message to INSE server");
					sendMessage(7777);
					System.out.println("IN Runnable task2");
					System.out.println("DataReceived is :"+DataReceived);
		   }
			   if (DataReceived.equals("pass")){
				   logger.info("CourseEnrol:"+" "+studentID+" "+"enrolled in course"+" "+courseID);
				   System.out.println("In Data received!");
					System.out.println("Student enrolled!");
					return "pass";
				}else {
					System.out.println("In else of Data received!");
					return "fail";
				}   
		   }
		   	
	}

	public static String DropCourse(String courseID, String semester) {
		logger.info("DropCourse: Drop course"+" "+courseID+"in semester"+" "+semester);
		String CourseDept = courseID.substring(0,4).toUpperCase();
		if(CourseDept.equals("SOEN")) {
			Capacity(semester,courseID,"DROP");
		}else if(CourseDept.equals("INSE")) {
			 Operation = "D,";
			  Sem=Operation.concat(courseID).concat(",").concat(semester);
			  logger.info("DropCourse:"+"Sending message to INSE server");
			  sendMessage(7777);
			  System.out.println("DataReceived is :"+DataReceived);
		}else if(CourseDept.equals("COMP")) {
			 Operation = "D,";
			  Sem=Operation.concat(courseID).concat(",").concat(semester);
			  logger.info("DropCourse:"+"Sending message to COMP server");
			  sendMessage(8888);
			  System.out.println("DataReceived is :"+DataReceived);
		}
		if (DataReceived.equals("pass")){
			logger.info("DropCourse:"+" "+courseID+"dropped in semester"+" "+semester);
			   System.out.println("In Data received!");
				return "pass";
			}else {
				logger.info("DropCourse:"+" "+courseID+"could not be dropped in semester"+" "+semester);
				System.out.println("In else of Data received!");
				return "fail";
			}
	}
	

	}
