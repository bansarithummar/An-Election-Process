import java.rmi.Naming;
import java.util.Scanner;
import java.util.*;

public class Client {

	private Client() {
	}

	public static void main(String[] args) {
		int flag = 0;

		Scanner s = new Scanner(System.in);// reading from the system
		try {
			rmiInterface stub = (rmiInterface) Naming.lookup("rmi://localhost/Election");

			while (flag == 0) {
				int choice = 0;
				System.out.println("\nEnter 5 choices listed below or enter 0 to quit:");
				System.out.println("1. Register to Vote");
				System.out.println("2. Verify Voter");
				System.out.println("3. Vote");
				System.out.println("4. Tally Results");
				System.out.println("5. Announce Winner");
				System.out.println("0. Quit");
				System.out.println("-----------");
				try {
					choice = Integer.parseInt(s.nextLine());
				} catch (NumberFormatException e) {
					s.skip("[\r\n]+");
				}
				{
					switch (choice) {
					case 1:
						String name = "";
						System.out.print("Enter the voter name:");
						name = s.nextLine();
						System.out.println("Your voter ID is: ");
						String voterID = stub.registerVoter(name);
						System.out.println(voterID);

						break;
					case 2:
						System.out.println("Enter your voter id: ");
						String id = s.nextLine();
						System.out.println("Your ID is registered under name:" + stub.VerifyVoter(id));
						break;
					case 3:
						System.out.println("Candidate List");
						ArrayList<String> Candidates = stub.candidateList();
						for (int i = 0; i < Candidates.size(); i++) {
							System.out.println("[" + i + "]: " + Candidates.get(i));
						}
						System.out.println("Enter you VoterId for begin voting: ");
						String vID = s.nextLine();
						System.out.println("Enter the number of the candiate that you would like to vote: ");
						int can_index = Integer.parseInt(s.nextLine());
						System.out.println(stub.Vote(vID, can_index));
						break;
					case 4:
						System.out.println("The votes for each candidate:");
						System.out.println(stub.tally());
						break;
					case 5:
						System.out.println(stub.Winner());
						break;
					case 0:
						flag++;
					}
					// end of an if condition
				}
			}

			s.close();
		} catch (Exception e) {
			System.err.println("Exception: " + e.toString());
			e.printStackTrace();
		}
	}
}