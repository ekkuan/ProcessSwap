import java.util.*;

public class SwapTest {

	private static LinkedList<Job> jList = new LinkedList<Job>();
	private static LinkedList<Segment> sList = new LinkedList<Segment>();
	private static int globalCount;

	public static void main(String[] args) throws Exception {

		Job job;
		Segment seg;
		sList.add(new Segment(0, 0, 100, null));
		String input = "";
		int pid = 0;
		int size = 0;
		Scanner kb = new Scanner(System.in);
		while (true) {
			System.out.print("> ");
			input = kb.nextLine();
			String[] inputs = input.split(" ");
			String[] opt = null;
			try {  
				opt = inputs[1].split(",");
			} catch (IndexOutOfBoundsException e) {
			} catch (NumberFormatException e) {
				return;
			}
			switch (inputs[0]) {
			case "add":
				try {
					pid = Integer.parseInt(opt[0]);
					size = Integer.parseInt(opt[1]);
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out
							.println("Please enter a Process ID and value to add");
					break;
				} catch (NumberFormatException e){
					System.out
							.println("Please enter a Process ID and value to add");
					break;
				}
				if (!dupeJob(pid)) {
					jList.add(new Job(pid, size));
				} else {
					System.out
							.println("This Process ID already exists. User another one.");
				}
				break;
			case "jobs":
				System.out.println(jList.toString());
				break;
			case "list":
				System.out.println(sList.toString());
				break;
			case "ff":
				if (checker(inputs)) {
					firstFit(getJob(Integer.parseInt(inputs[1])));
				}
				break;
			case "nf":
				if (checker(inputs)) {
					nextFit(getJob(Integer.parseInt(inputs[1])));
				}
				break;
			case "bf":
				if (checker(inputs)) {
					bestFit(getJob(Integer.parseInt(inputs[1])));
				}
				break;
			case "wf":
				if (checker(inputs)) {
					worstFit(getJob(Integer.parseInt(inputs[1])));
				}
				break;
			case "de":
				try {
					pid = Integer.parseInt(inputs[1]);
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out
							.println("Please enter a Process ID and value to deallocate");
					break;
				}
				if (dupeJob(pid)) {
					remProc(pid);
				} else {
					System.out.println("This Process ID is not in the list.");
				}
				break;
			default:
				System.out.println("Enter a proper command.");
				break;
			}

		}

	}

	private static void remProc(int pid) throws Exception{
		Segment tmp = new Segment(0, 0, 0, null);
		int i = 0;
		for (Segment s : sList) {
			if (s.getPid() == pid) {
				i = sList.indexOf(s);
			}
		}
		sList.get(i).setPid(0);
		try {
			if (sList.get(i).getNext().getPid() == 0) {
				sList.get(i).setLength(sList.get(i).getLength() + sList.get(i).getNext().getLength());
				sList.get(i).setNext(sList.get(i).getNext().getNext());
				sList.remove(i+1);
			}
			if (sList.get(i-1).getPid() == 0) {
				sList.get(i).setLength(sList.get(i).getLength() + sList.get(i-1).getLength());
				sList.get(i).setNext(sList.get(i));
				sList.get(i).setStart(sList.get(i-1).getStart());
				sList.remove(i-1);
			}
		} catch (ArrayIndexOutOfBoundsException e) {
		} catch (NullPointerException e) {
		} catch (IndexOutOfBoundsException e) {
		}

	}

	private static boolean worstFit(Job j) throws Exception {
		boolean space = false;
		int largest = 0;
		Segment temp = new Segment(0, 0, 0, null);
		for (Segment s : sList) {
			if (s.getLength() >= j.getSize() && s.getPid() == 0 && largest == 0) {
				largest = s.getLength();
				temp = s;
				space = true;
			} else if (s.getLength() >= j.getSize() && s.getPid() == 0
					&& largest < s.getLength()) {
				largest = s.getLength();
				temp = s;
			}
		}
		int count = sList.indexOf(temp);
		if (count != -1) {
			globalCount = count;
		}
		if (space) {
			addProc(count, j);
		} else {
			System.out.println("Not enough space");
		}
		return space;
	}

	private static boolean bestFit(Job j) throws Exception {
		boolean space = false;
		int smallest = 0;
		Segment temp = new Segment(0, 0, 0, null);
		for (Segment s : sList) {
			if (s.getLength() >= j.getSize() && s.getPid() == 0
					&& smallest == 0) {
				smallest = s.getLength();
				temp = s;
				space = true;
			} else if (s.getLength() >= j.getSize() && s.getPid() == 0
					&& smallest > s.getLength()) {
				smallest = s.getLength();
				temp = s;
			}
		}
		int count = sList.indexOf(temp);
		if (count != -1) {
			globalCount = count;
		}
		if (space) {
			addProc(count, j);
		} else {
			System.out.println("Not enough space");
		}
		return space;
	}

	private static boolean nextFit(Job j) throws Exception {
		boolean space = false;
		for (int i = globalCount; i < sList.size(); ++i) {
			Segment s = sList.get(i);
			if (s.getLength() >= j.getSize() && s.getPid() == 0) {
				space = true;
				break;
			} else if (s.getLength() == 0) {
				space = false;
			}
		}
		if (space) {
			addProc(globalCount, j);
		} else {
			System.out.println("Not enough space");
		}
		return space;
	}

	private static boolean firstFit(Job j) throws Exception {
		int count = 0;
		boolean space = false;
		for (Segment s : sList) {
			if (s.getLength() >= j.getSize() && s.getPid() == 0) {
				space = true;
				break;
			} else if (s.getLength() == 0) {
				space = false;
			}
			count++;
		}
		globalCount = count;
		if (space) {
			addProc(count, j);
		} else {
			System.out.println("Not enough space");
		}
		return space;
	}

	private static boolean checker(String[] in) {
		boolean result = false;
		int proc = 0;
		try {
			proc = Integer.parseInt(in[1]);
			if (dupeJob(proc)) {
				result = true;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Please enter a Process ID to add");
		}
		return result;
	}

	private static void addProc(int i, Job j) throws Exception {
		if (dupeProc(j)){
			Segment temp = sList.get(i);
			Segment newNext = new Segment(0, temp.getStart() + j.getSize(), temp.getLength() - j.getSize(), null);
			sList.add(i+1, newNext);
			sList.get(i).setPid(j.getPid());
			sList.get(i).setLength(j.getSize());
			sList.get(i).setNext(newNext);
		} else {
			System.out.println("This pid already exists in the list.");
		}
	}

	private static Job getJob(int pid) {
		for (Job j : jList) {
			if (j.getPid() == pid) {
				return j;
			}
		}
		return null;
	}
	
	private static boolean dupeProc(Job j) {
		boolean result = true;
		for (Segment s: sList) {
			if (j.getPid() == s.getPid()) {
				result = false;
			}
		}
		return result;
	}

	private static boolean dupeJob(int pid) {
		boolean result = false;
		for (Job j : jList)
			if (j.getPid() == pid)
				result = true;
		return result;
	}

}
