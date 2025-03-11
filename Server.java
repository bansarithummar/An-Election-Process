import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Scanner;

public class Server extends UnicastRemoteObject implements rmiInterface {
	/* 
	 */
	private static final long serialVersionUID = 1L;

	public static ArrayList<String> VoterID = new ArrayList<String>();
	public static ArrayList<String> Voter = new ArrayList<String>();
	public static ArrayList<String> Vote = new ArrayList<String>();
	public static ArrayList<String> temps = new ArrayList<String>();
	public static HashMap<String, Integer> votecount = new HashMap<String, Integer>();

	public Server() throws RemoteException {
	}

	public String generateID() throws RemoteException {
		StringBuilder sb = new StringBuilder(10);
		try {
			String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";

			for (int i = 0; i < 10; i++) {

				int index = (int) (AlphaNumericString.length() * Math.random());

				sb.append(AlphaNumericString.charAt(index));
			}
			System.out.println(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public String registerVoter(String name) throws RemoteException {

		Voter.add(name);
		String voterID = generateID();

		VoterID.add(voterID);
		Vote.add(null);
		return voterID;
	}

	public ArrayList<String> candidateList() throws RemoteException { // return the candidate list
		return temps;
	}

	public String VerifyVoter(String id) {
		int index = VoterID.indexOf(id);
		if (index != -1) {
			String name = Voter.get(index);
			return name;
		}
		return "Voter Not Verified";
	}

	public String Vote(String id, int candidateIndex) {
		int index = VoterID.indexOf(id);

		// Case 1: not verified
		if (index == -1) {
			return "This Voter Id is not valid!!!";
		}

		// Case 2: already voted
		if (Vote.get(index) != null) {
			return "You have already voted earlier!!!";
		}

		// Case 3: all valid
		String candi = temps.get(candidateIndex);
		Vote.set(index, candi);
		votecount.put(candi, votecount.get(candi) + 1);
		return "Your vote has been successfully submited!!!";
	}

	public HashMap<String, Integer> tally() throws RemoteException { 
		return votecount;
	}

	public String Winner() throws RemoteException {
		int maxVote = 0;
		ArrayList<String> WinnerList = new ArrayList<String>();
		String winner = temps.get(0);
		int count = 1;
		WinnerList.add(winner);
		for (HashMap.Entry<String, Integer> entry : votecount.entrySet()) {
			String key = entry.getKey();
			Integer value = entry.getValue();
			if (value > maxVote) {
				WinnerList = new ArrayList<String>();
				maxVote = value;
				winner = key;
				count = 1;
				WinnerList.add(winner);
			} else if (value == maxVote) {
				count += 1;
				winner = key;
				WinnerList.add(winner);
			} else {

			}
		}

		if (maxVote == 0) {
			return "No one has voted yet!! No Data to show";
		}

		if (count == 1) {
			return winner + " has won the election and got " + maxVote + " votes!!";
		}

		return "More than one candidate got same maximum number of votes. These candidates got same votes"
				+ WinnerList.toString();

	}

	public static void main(String args[]) throws IOException {

		try {

			Server obj = new Server();
			Naming.rebind("rmi://localhost:1099/Election", obj);
			System.err.println("Server ready for the Election");

			String token1 = "";

			Scanner f = new Scanner(new File("name.txt")).useDelimiter(",\\s*");

			while (f.hasNext()) {
				token1 = f.next();
				temps.add(token1);
			}
			f.close();

			String[] tempsArray = temps.toArray(new String[0]);

			System.out.println(temps);
			System.out.println(" The list of Candidates are: ");
			System.out.println("---------");

			for (String s : tempsArray) {
				System.out.println(s);
				votecount.put(s, 0);
			}
			System.out.println("--------");
			System.out.println("Current Vote Pool of the candidates");
			System.out.println(" ");
			System.out.println(votecount);
		} catch (Exception e) {
			System.err.println("Exception: " + e.toString());
			e.printStackTrace();
		}
	}
}
