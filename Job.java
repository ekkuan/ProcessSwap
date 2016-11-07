public final class Job {
	private final int pid;
	private final int size;

	public Job(int pid, int size) {
		this.pid = pid;
		this.size = size;
	}

	public int getPid() {
		return pid;

	}

	public int getSize() {
		return size;
	}

	@Override
	public String toString() {
		return String.format("[%d %d]", pid, size);
	}
}