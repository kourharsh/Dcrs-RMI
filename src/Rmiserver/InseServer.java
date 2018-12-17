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

public class InseServer {
		static HashMap<String, Integer> hashin;
		static HashMap<String, HashMap<String, Integer>> hashInse= new HashMap<String, HashMap<String, Integer>>();
		static HashMap<String, Integer> hash2 = new HashMap<String, Integer>();
		static String DataReceived;
		static String DataReceivedC;
		static String DataReceivedS;
		static String Sem;
		static Logger logger = Logger.getLogger(AdvisorClient.class.getName());
		static private FileHandler fileTxt;
	    static private SimpleFormatter formatterTxt;
		static String semReceived;
		static String Operation;
		static HashMap<String,Integer> InseSem;
		static HashMap<String, Integer> fetchValuesI= new HashMap<String, Integer>();
	   public static void startInseServer(int RMIPort) throws RemoteException, MalformedURLException {
			try {
				Runnable task = () -> {
					receive();
				};
				Thread thread = new Thread(task);
				thread.start();
				fileTxt = new FileHandler("C:\\Users\\WEB\\eclipse-workspace\\DCRS_RMI_\\Log Files\\InseServer.log");
				formatterTxt = new SimpleFormatter();
			    fileTxt.setFormatter(formatterTxt);
			    logger.addHandler(fileTxt);
				CommonServer.startRegistry(RMIPort);
		        Functions rmiInse = new Functions();
		        Naming.rebind("rmi://localhost:" + RMIPort + "/rmiInse", rmiInse);
		        System.out.println("RmiInse Server registered");
		        logger.info("******RmiInse Server registered******");
		        inputValues("FALL");
		        inputValues("SUMMER");
		        inputValues("WINTER");
			}
			catch(Exception e) {
				System.out.println("Exception in RmiInse: " + e);
			}
		}
	   
	   public static void inputValues(String term) {

			if(term == "FALL") {
				logger.info("******Adding Fall Courses in Inse Hashmap******");
				hashin = new HashMap<String, Integer>();
				hashin.put("INSE6789", 10);
				hashin.put("INSE3421", 10);
				hashin.put("INSE4387", 10);
				hashin.put("INSE5436", 10);
				if(!hashInse.containsKey(term)) {
					hashInse.put(term, hashin);
				}
			}

			if(term == "SUMMER") {
				logger.info("******Adding Summer Courses in Inse Hashmap******");
				hashin = new HashMap<String, Integer>();
				hashin.put("INSE4327", 10);
				hashin.put("INSE6543", 10);
				hashin.put("INSE6432", 10);
				hashin.put("INSE5412", 10);
				if(!hashInse.containsKey(term)) {
					hashInse.put(term, hashin);
				}

			}
			if(term == "WINTER") {
				logger.info("******Adding Winter Courses in Inse Hashmap******");
				hashin = new HashMap<String, Integer>();
				hashin.put("INSE6732", 10);
				hashin.put("INSE6715", 10);
				hashin.put("INSE6732", 10);
				hashin.put("INSE6165", 10);
				if(!hashInse.containsKey(term)) {
					hashInse.put(term, hashin);
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
	   public static String FetchHashmap(String Semester) {
		   logger.info("FetchHashmap:Fetch Inse Server Data for semester"+" "+Semester);
		   if(hashInse.containsKey(Semester)) {
		   HashMap<String,Integer> Insesem = hashInse.get(Semester);
		   List<HashMap<String,Integer>> i = Arrays.asList(Insesem);
		   System.out.println(i);
			String	hash = i.toString();
			System.out.println("hash is" +hash);
		    return hash;
		   }else {
			   return "No semester exists";
		   }
	   }
	   private static void receive() {
			DatagramSocket aSocket = null;
			try {
				String	rep = null;
				aSocket = new DatagramSocket(7777);
			//	byte[] buffer = new byte[1000];
				System.out.println(" Inse Server 7777 Started............");
				while (true) {
					byte[] buffer = new byte[1000];
					DatagramPacket request = new DatagramPacket(buffer, buffer.length);
					aSocket.receive(request);
					semReceived = new String(request.getData());
					System.out.println("Semester received is :" + " " + semReceived);
					String func=semReceived.split(",")[0];
					func=func.trim();
					if(func.equals("E")) {
						System.out.println("Semester reseived is :" + " " + semReceived);
						String []parm=semReceived.split(",",4);
						String studentid=parm[1];
						studentid=studentid.trim();
						String courseID =parm[2];
						courseID=courseID.trim();
					    String semester =parm[3];
						semester=semester.trim();
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
			fetchValuesI = hashInse.get(semester);
			int capacity = fetchValuesI.get(courseID);
			if(Operation.equals("ENROLL")) {
				capacity = capacity-1;
				logger.info("Capacity:Capacity decreased by 1");
			}else if(Operation.equals("DROP")) {
				capacity = capacity+1;
				logger.info("Capacity:Capacity increased by 1");
			}
			fetchValuesI.put(courseID, capacity);
			hashInse.put(semester, fetchValuesI);
			logger.info("Capacity:Inse Hashmap updated with new capacity of course"+" "+courseID);
			List<HashMap<String,HashMap<String,Integer>>> g = Arrays.asList(hashInse);
			System.out.println(g);
		}
	
	public static String checkCourse(String semester,String courseId) {
		System.out.println("semester"+semester+"---------"+"courseId"+courseId);
		logger.info("checkCourse: Checking availabilty of course"+" "+courseId+"in semester"+" "+semester);
		String result="fail";
		fetchValuesI = hashInse.get(semester);
		if(fetchValuesI.containsKey(courseId)) {
			int capacity = fetchValuesI.get(courseId);
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


	public static void listCourse2(String semester) {
		logger.info("******listCourse2:INSE SERVER******");
		System.out.println("I am printed--1-");
		if(hashInse.containsKey(semester)) {
			InseSem = hashInse.get(semester);
		}
		List<HashMap<String,Integer>> g = Arrays.asList(InseSem);
		System.out.println(g);
		logger.info("******listCourse3:Sending message to COMP SERVER******");
		sendMessage(8888);
		DataReceivedC = DataReceived;
		System.out.println(DataReceivedC);
		logger.info("******listCourse3:Sending message to SOEN SERVER******");
		sendMessage(5555);
		DataReceivedS = DataReceived;
		System.out.println(DataReceivedS);
	}

	public static String CourseEnrol(String studentID, String courseID, String semester) throws RemoteException {
		logger.info("CourseEnrol: Enrolling course"+" "+courseID+"in semester"+" "+semester+" "+"for student"+" "+studentID);
		   String CourseDept = courseID.substring(0,4).toUpperCase();
		   if(CourseDept.equals("INSE")) {
			   fetchValuesI = hashInse.get(semester);
				if(fetchValuesI.containsKey(courseID)) {
					int capacity = fetchValuesI.get(courseID);
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
		   }else if(CourseDept.equals("SOEN")) {
			   		logger.info("CourseEnrol:"+"Sending message to SOEN server");
					sendMessage(5555);
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
		if(CourseDept.equals("INSE")) {
			Capacity(semester,courseID,"DROP");
		}else if(CourseDept.equals("COMP")) {
			 Operation = "D,";
			  Sem=Operation.concat(courseID).concat(",").concat(semester);
			  logger.info("DropCourse:"+"Sending message to COMP server");
			  sendMessage(8888);
			  System.out.println("DataReceived is :"+DataReceived);
		}else if(CourseDept.equals("SOEN")) {
			 Operation = "D,";
			  Sem=Operation.concat(courseID).concat(",").concat(semester);
			  logger.info("DropCourse:"+"Sending message to SOEN server");
			  sendMessage(5555);
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