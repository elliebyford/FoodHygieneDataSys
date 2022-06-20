import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BSTDupNode<E extends Comparable<E>> {
	public BSTDupNode<E> parent; // The parent node
	public BSTDupNode<E> left;
	public BSTDupNode<E> right;
	public E element; // the actual data the node stores
	public List<E> elements; // elements in the case of duplicate keys
	private Comparator<E> comp; // Ex 8B Advanced - the comparator

	public BSTDupNode(E value) {
		this(value, null);
	}

// 8B Advanced - allow programmer-specified Comparator.
	public BSTDupNode(E value, Comparator<E> comp) {
		this.element = value;
		this.comp = comp;
	}

// a convenient method for debugging
	public String toString() {
		return "(" + super.toString() + "), " + element + " L:" + (left != null) + ", R:" + (right != null);
	}

// For 8B Advanced #2 - uses the custom comparator if specified, or the
// "natural-order" (i.e. .compareTo) if it is null
	private int nodeCompare(E key, BSTDupNode<E> node) {
		int cmp = 0;
		if (comp != null)
			cmp = comp.compare(key, node.element);
		else
			cmp = key.compareTo(node.element);
		return cmp;

	}

// Exercise 8B.01 - Search
	public BSTDupNode<E> search(E key) {
// Using the comparable functionality to do the comparison
		int cmp = nodeCompare(key, this);
		if (cmp == 0)
			return this;
		else if (cmp < 0 && this.left != null)
			return this.left.search(key);
		else if (cmp > 0 && this.right != null)
			return this.right.search(key);
		return null;
	}

	public BSTDupNode<E> insert(E key) {
		return insert(this, key);
	}

// Exercise 8B.02 - Insert 
// For Ex 8B.05 - Advanced #2 - simply stack the duplicates
	public BSTDupNode<E> insert(BSTDupNode<E> node, E key) {
		if (node == null)
			return new BSTDupNode<E>(key, comp);

		int cmp = nodeCompare(key, node);

// allow duplicates - stack duplicates left
		if (cmp <= 0) {
			node.left = insert(node.left, key);
			if (node.left != null)
				node.left.parent = node;
		} else if (cmp > 0) {
			node.right = insert(node.right, key);
			if (node.right != null)
				node.right.parent = node;

		}
		return node;
	}

// Exercise 8B.05 - Advanced #2 Insert
	public BSTDupNode<E> insertdups(E key) {
		return insertdups(this, key);
	}

// Exercise 8B.05 - Advanced #2 Insert
	public BSTDupNode<E> insertdups(BSTDupNode<E> node, E key) {
		if (node == null)
			return new BSTDupNode<E>(key, comp);

		int cmp = nodeCompare(key, node);

// allow duplicates - make a list
		if (cmp == 0) {
			if (node.elements == null) {
				node.elements = new ArrayList<>();
			}
			node.elements.add(key);
		} else if (cmp < 0) {
			node.left = insertdups(node.left, key);
			if (node.left != null)
				node.left.parent = node;
		} else if (cmp > 0) {
			node.right = insertdups(node.right, key);
			if (node.right != null)
				node.right.parent = node;

		}
		return node;
	}

	public List<E> inOrderTraverse() {
		return inOrderTraverse(null);
	}

	public List<E> inOrderTraverse(List<E> list) {
		if (list == null)
			list = new ArrayList<E>();
		if (left != null)
			left.inOrderTraverse(list);

		list.add(this.element);
		if (this.elements != null)
			list.addAll(this.elements);

		if (right != null)
			right.inOrderTraverse(list);

		return list;
	}

// Exercise 8B.02 Advanced - remove/delete method
	public BSTDupNode<E> delete(E key) {
		int cmp = nodeCompare(key, this);
		if (cmp < 0) {
			if (this.left != null)
				this.left = this.left.delete(key);
		} else if (cmp > 0) {
			if (this.right != null)
				this.right = this.right.delete(key);
		} else {
			if (this.right == null)
				return this.left;
			if (this.left == null)
				return this.right;
// if node has both children
			BSTDupNode<E> x = this.right.min(); // find min in r subtree
			x.right = this.right.deleteMin(); // delete min in r subtree
			x.left = this.left; // copy l subtree "as-is"
			return x;
		}
		return this;
	}

	private BSTDupNode<E> min() {
		if (this.left == null)
			return this;
		else
			return this.left.min();
	}

	private BSTDupNode<E> deleteMin() {
		if (this.left == null)
			return this.right;
		this.left = this.left.deleteMin();
		return this;
	}

	public BSTDupNode<E> simpleDelete(E key) {
		int cmp = nodeCompare(key, this);
		if (cmp < 0) {
			if (this.left != null)
				this.left = this.left.simpleDelete(key);
		} else if (cmp > 0) {
			if (this.right != null)
				this.right = this.right.simpleDelete(key);
		} else {
			if (this.right == null)
				return this.left;
			if (this.left == null)
				return this.right;
		}
		return this;
	}

// Task 7B - preorder traversal print
	public void preOrderTraversePrint() {

		System.out.println(this.element);
		if (left != null)
			left.preOrderTraversePrint();
		if (right != null)
			right.preOrderTraversePrint();
	}

// Task 7B - postorder traversal print
	public void postOrderTraversePrint() {
		if (left != null)
			left.postOrderTraversePrint();
		if (right != null)
			right.postOrderTraversePrint();

		System.out.println(this.element);
	}

// Task 7B - postorder traversal print
	public void inOrderTraversePrint() {
		if (left != null)
			left.inOrderTraversePrint();

		System.out.println(this.element);

		if (right != null)
			right.inOrderTraversePrint();
	}

// Task 7.02 - euler tour, combined traversal print
	public void eulerTraversePrint() {
		System.out.print("L:" + this.element + ", ");
		if (left != null)
			left.eulerTraversePrint();

		System.out.print("B:" + this.element + ", ");

		if (right != null)
			right.eulerTraversePrint();
		System.out.print("R:" + this.element + ", ");
	}

// 7B - breadth-first traversal - using a queue
	public void levelOrderTraversePrint() {
		Queue<BSTDupNode<E>> q = new LinkedList<BSTDupNode<E>>();
		q.offer(this);
		while (!q.isEmpty()) {
			BSTDupNode<E> node = q.poll();
			System.out.println("Node: " + node.element);

			if (node.left != null)
				q.offer(node.left);
			if (node.right != null)
				q.offer(node.right);
		}
	}

// From Task 7A, Intermediate - but implemented here for completeness
	public int size() {
		int size = 1;
		if (left != null)
			size += left.size();
		if (right != null)
			size += right.size();
		return size;
	}

// From Task 7A, Intermediate - but implemented here for completeness
	public int depth() {
		if (this.parent != null)
			return this.parent.depth() + 1;
		return 0;
	}

// From Task 7A, Intermediate - but implemented here for completeness
	public int height() {
		int max = 0;
		if (left != null) {
			int currentHeight = left.height() + 1;
			if (currentHeight > max)
				max = currentHeight;
		}
		if (right != null) {
			int currentHeight = right.height() + 1;
			if (currentHeight > max)
				max = currentHeight;
		}
		return max;
	}

	public void add(foodHygieneData data) {
		// TODO Auto-generated method stub
		
	}

}
