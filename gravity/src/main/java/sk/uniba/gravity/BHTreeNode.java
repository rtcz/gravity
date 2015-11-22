package sk.uniba.gravity;

public class BHTreeNode {

	private BHTreeNode[] quadrants;// = new BHTreeNode[4];
	private BarycenterNode node;
	
	public BHTreeNode() {

	}
	
	public void addNode(MassNode node) {
		if (isEmpty()) {
			this.node = new BarycenterNode(node);
		} else if (isExternal()) {
			
		} else {
			
		}
	}
	
	public boolean isEmpty() {
		return node == null;
	}
	
	public boolean isExternal() {
		return quadrants == null;
	}
}
