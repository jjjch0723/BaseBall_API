package main;

public class Main {

	public static void main(String[] args) {
		scheduleController sCont = new scheduleController();
		resultController rCont = new resultController();
		
		sCont.sCont();
		try {
			Thread.sleep(4000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		rCont.rCont();
		
		System.out.println("Data Crowling, Data insert Succesful");
	}

}
