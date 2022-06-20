import java.util.Comparator;
import java.util.Vector;

//Exercise 8A - Advanced - Generics, Comparator-driven Heap
public class HeapSort<E> {
	private Comparator<foodHygieneData> comp = null;
	private Vector<foodHygieneData> heap = new Vector<foodHygieneData>();
	private int end = 0;
	private boolean minHeap;

// Create the heap array
	public HeapSort(boolean minHeap, Comparator<foodHygieneData> comp) {
		this.minHeap = minHeap;
		this.comp = comp;
	}

// inserts a new value into the heap
	public void insert(foodHygieneData value) {
		heap.add(end, value);
		end++;
		upheap(end - 1);
	}

	private boolean needsSwap(foodHygieneData child, foodHygieneData par) {
		return (minHeap && comp.compare(child, par) < 0) || (!minHeap && comp.compare(par, child) < 0);
	}

// upheap operation
	private void upheap(int pos) {
		if (pos > 0) // stop at root
		{
			foodHygieneData childVal = heap.get(pos);
			int parIdx = (pos - 1) / 2;
			foodHygieneData parVal = heap.get(parIdx);
			if (needsSwap(childVal, parVal)) {
				heap.set(pos, parVal);
				heap.set(parIdx, childVal);
				upheap(parIdx);
			}
		}
	}

// Breadth-first, level-order traversal
// See - I told you it was easy!
// As the array holds the elements in level-order, the traversal
// is just printing out each element in array order
	public void breadthFirst() {
		for (foodHygieneData val : heap)
			System.out.println(val + ", ");
	}

// Exercise 8A - remove operation
	public foodHygieneData remove() {
		foodHygieneData last = heap.get(end - 1);
		foodHygieneData root = heap.get(0);
		heap.set(0, last);

		heap.remove(end - 1);
// instead of simply setting the value to null as below;
// we are using a dynamic array, so we'll simply
// remove the value
// heap.set(end-1,null);
		end--;
// reorganise heap from root
		downheap(0);
		return root;
	}

/// collection sort 

/// collection rev

// Exercise 8A - downheap
	private void downheap(int pos) {
		int swap = 0;
		int child = 2 * pos + 1; // i.e. the left child index position
// check left child is still in array and if child greater than parent,
// swap required
		if (child < end && needsSwap(heap.get(child), heap.get(pos)))
			swap = child;
// check right child is still in array and if right child greater than
// parent AND left child, swap this instead
		if (child + 1 < end
				&& (needsSwap(heap.get(child + 1), heap.get(pos)) && needsSwap(heap.get(child + 1), heap.get(child))))
			swap = child + 1;
// only swap if required
		if (swap > 0) {
			foodHygieneData pval = heap.get(pos);
			foodHygieneData cval = heap.get(swap);
			heap.set(pos, cval);
			heap.set(swap, pval);
// recursive call
			downheap(swap);
		}
	}

// Exercise 8A - Heap sort - isEmpty method
	public boolean isEmpty() {
		return heap.size() == 0;
	}

	public int size() {
		return heap.size();
	}

	public int getHeight() {
		return (int) Math.floor(Math.log(heap.size()) / Math.log(2));
	}

}