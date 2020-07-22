import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

public interface rmiInterface extends Remote {

	String registerVoter(String x) throws RemoteException;

// void DisplayUsers() throws RemoteException;
	ArrayList<String> candidateList() throws RemoteException;

	String VerifyVoter(String id) throws RemoteException;

	String Vote(String id, int candidateIndex) throws RemoteException;

	HashMap<String, Integer> tally() throws RemoteException;

	String Winner() throws RemoteException;
// boolean ViDgenerator(int x) throws RemoteException;
}