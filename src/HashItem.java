
public class HashItem {

	private long checksum;
	private EntryType entryType;
	private int depth;
	private int eval;
	
	public long getChecksum() {
		return checksum;
	}
	public void setChecksum(long checksum) {
		this.checksum = checksum;
	}
	public EntryType getEntryType() {
		return entryType;
	}
	public void setEntryType(EntryType entryType) {
		this.entryType = entryType;
	}
	public int getDepth() {
		return depth;
	}
	public void setDepth(Integer depth) {
		this.depth = depth;
	}
	public int getEval() {
		return eval;
	}
	public void setEval(int eval) {
		this.eval = eval;
	}
}
