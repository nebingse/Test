import java.util.ArrayList;
import java.util.Collections;

public class ReverseMap implements ReverseLookUp{
	
	private ArrayList<StringTree> reverseMap= new ArrayList<>();

	@Override
	public void add(int oldCount, int newCount, String s) {
		if (reverseMap.size() <= newCount) {
			// you have to resize the list
			
			while (reverseMap.size() <= (newCount * 2)) {
				reverseMap.add(new StringTree());
			}
		}

		// get the node at the old position
		StringTree pos = null;
		
		if ((oldCount > 0) && (reverseMap.size() > oldCount)) {
			pos = reverseMap.get(oldCount);
		}
		
		// if we have an object, remove the old word.
		if (pos != null) {
			pos.remove(s);
		}
		
		if (newCount > reverseMap.size()) {
			for (int idx = reverseMap.size();idx <= newCount; idx++) {
				reverseMap.add(new StringTree());
			}
		}
		
		// get the node at the new position
		pos = reverseMap.get(newCount);
		
		if (pos == null) {
			// if null we need to start a new node at that pos
			pos = new StringTree();
			reverseMap.set(newCount, pos);
		}
		
		// add the word
		pos.add(s);
	}

	@Override
	public boolean isEmpty() {
		return reverseMap.size() == 0;
	}

	@Override
	public String pop(int term) {

		int favs = term;
		for (int idx = reverseMap.size() - 1;idx >= 0; idx--) {
			// okay, if there are entries in this position
			if (reverseMap.get(idx) != null && reverseMap.get(idx).size() > 0) {
				
				if (favs >= reverseMap.get(idx).size() && favs != 0) {

					favs -= reverseMap.get(idx).size();
					continue;
				}
				
				// if we get here, favs should be <= size
				return reverseMap.get(idx).get(favs);
			}
		}
		return null;
	}
	
	/**
	 * A class to implement a Binary Tree and keep the tree balanced to
	 * increase searching efficiency
	 * @author snebing
	 *
	 */
	public class StringTree {

		public static final int REBALANCE_POINT = 60;

		public StringNode root = null;
		public int size = 0;
		private int changesSinceBalance = 0;

		public int size() {
			return size;
		}
		
		/**
		 * add: Adds a word to the tree.
		 *
		 * @param word the word to be added.
		 */
		public void add(final String word) {
			// if root was null, this is the first node
			if (root == null) {
				root = new StringNode(word, null);
				size++;
				changesSinceBalance++;
				return;
			}

			add(root, word);
		}

		/**
		 * Adds the node to the tree
		 * @param node the node to be added
		 * @param word the word to be added
		 */
		private void add(final StringNode node, final String word) {
			// do the comparison
			int cmp = node.compare(word);

			// if zero, match to value
			if (cmp == 0) {
				return;
			}

			if (cmp < 0) {
				// word goes to the left
				if (node.left == null) {
					node.left = new StringNode(word, node);
					size++;
					changesSinceBalance++;
				} else {
					add(node.left, word);
				}
			}

			if (cmp > 0) {
				// word goes to the right

				if (node.right == null) {
					node.right = new StringNode(word, node);
					size++;
					changesSinceBalance++;
				} else {
					add(node.right, word);
				}
			}

			// if we have had so many changes since the last rebalance, let's rebalance
			// the tree to keep walks and searches reasonable
			if (changesSinceBalance > REBALANCE_POINT) {
				rebalance();
			}
		}

		/**
		 * appendNodes: Used to extract the node values using an in order traversal.
		 * this will give us a list of all values sorted.
		 * @param words the current words in the tree
		 * @param current the current node to start at
		 */
		private void appendNodes(ArrayList<String> words, StringNode current) {
			// if current node is null, nothing to do here.
			if (current == null) {
				return;
			}

			// inorder traversal on the left
			appendNodes(words, current.left);

			// now add current node value
			words.add(current.value);

			// inorder traversal on the right
			appendNodes(words, current.right);

			// purge our values
			current.left = null;
			current.right = null;
			current.value = null;
		}

		/**
		 * Gets the nth value from the tree.
		 * @param idx the position to get a word from
		 * @return the word.
		 */
		public String get(int idx) {
			// create an iterator instance
			NodeIter iter = new NodeIter(idx);

			// recursively handle inorder traversal on the bst.
			visit(root, iter);

			// iter should have the value at the specified index.
			return iter.value;
		}

		/**
		 * balance factor for a node is the difference in heights of the left and right.
		 * if balance factor is -1, 0 or 1 the node is considered to be balanced.
		 *
		 * @param node the starting point for the height.
		 * @return
		 */
		private int balanceFactor(StringNode node) {
			if (node == null) {
				return 0;
			}

			return height(node.left) - height(node.right);
		}

		/**
		 * The height is basically the largest distance from this node to a leaf on
		 * the left or the right).
		 *
		 * @param node the starting point
		 * @return the height of the tree
		 */
		private int height(StringNode node) {
			if (node == null) {
				return 0;
			}

			if ((node.left == null) && (node.right == null)) {
				return 1;
			}

			int heightLeft = height(node.left);
			int heightRight = height(node.right);

			return Math.max(heightLeft, heightRight) + 1;
		}

		/**
		 * isBalanced: Checks if the given node is balanced.
		 *
		 * @param node
		 * @return true if the node is balanced, otherwise it is unbalanced and the
		 * tree might perform better if it were rebalanced.
		 */
		private boolean isBalanced(StringNode node) {
			if (node == null) {
				return true;
			}

			// to be balanced, left and right must be balanced
			if (!isBalanced(node.left)) {
				return false;
			}
			if (!isBalanced(node.right)) {
				return false;
			}

			// left and right is good, but balance factor for node should be good.
			int factor = Math.abs(balanceFactor(node));

			return (factor <= 1);
		}


		/**
		 * rebalance - rebuilds the tree to balance the distance from the root to all of the leaves.
		 * when the tree is balanced, you minimize the max number of string comparisons you might
		 * need to perform when inserting nodes, for example.
		 */
		public void rebalance() {
			if (size < 3) {
				// no need to rebalance
				return;
			}

			if (isBalanced(root)) {
				// tree is already balanced, do not have to rebalance 
				return;
			}

			// okay, so first we need the ordered list of nodes...
			ArrayList<String> words = new ArrayList<>();

			// recursively walk the nodes using inorder traversal to put words in order in the list.
			appendNodes(words, root);

			// extracted the words, elements are whacked...
			root = null;
			size = words.size();
			changesSinceBalance = 0;

			// words are now in order, so now we should recreate tree in proper order...
			root = recreateNodes(words, 0, size - 1);

			// the recreate does not set the parent nodes, let us do that now...
			updateParents(root, null);
		}

		/**
		 * recreates the binary tree by splitting the given list into left and right and middle value.
		 * by inserting in these parts, the tree will be balanced on the node I am currently building.
		 * @param words the words in the tree
		 * @param start the position to start at
		 * @param end the position to end at
		 * @return the recreated tree.
		 */
		private StringNode recreateNodes(ArrayList<String> words, int start, int end) {
			// if the start/end or invalid, then recursion is done
			if (start > end) {
				return null;
			}

			// find the middle of the list for processing
			int midPoint = (start + end) / 2;

			// extract the word from the list
			String word = words.get(midPoint);

			// create a new node
			StringNode node = new StringNode(word, null);

			// the left side of the list should become the left node to balance
			node.left = recreateNodes(words, start, midPoint - 1);

			// the right side of the list should become the right node
			node.right = recreateNodes(words, midPoint + 1, end);

			// node is balanced
			return node;
		}

		/**
		 * removes the given word from the tree.
		 * @param word the word to remove
		 */
		public void remove(final String word) {
			if (root == null) {
				// nothing to do
				return;
			}

			StringNode current = root;

			boolean left = false;

			// walk the tree until we find the node
			while (current.compare(word) != 0) {
				// go either to left or right
				if (current.compare(word) < 0) {
					left = true;
					current = current.left;
				} else {
					left = false;
					current = current.right;
				}

				// if null, then word is not in the tree.
				if (current == null) {
					return;
				}
			}

			// current is the node that should be removed.

			// If current is a leaf, easy removal
			if (current.left == null && current.right == null) {
				if (current.parent == null) {
					root = null;
				} else {
					// not the root node, just null the parent link
					if (left == true) {
						current.parent.left = null;
					} else {
						current.parent.right = null;
					}
				}
			} else if (current.right == null) {
				// if the right side is null, just need to move up the left
				if (current.parent == null) {
					root = current.left;
				} else if (left) {
					current.parent.left = current.left;
				} else {
					current.parent.right = current.left;
				}
			} else if (current.left == null) {
				// if the left side is null, just need to move up the right
				if (current.parent == null) {
					root = current.right;
				} else if (left) {
					current.parent.left = current.right;
				} else {
					current.parent.right = current.right;
				}
			} else if (current.left != null && current.right != null) {
				// This is the toughest case.  We need to find the lowest value from
				// the right side; it will become our value replacing the current value.
				// it is the only way to preserve the current "order".

				// remove the successor node from the right side.
				StringNode successor = removeSuccessor(current);

				current.value = successor.value;
			}

			// okay, word has been removed
			size --;
			changesSinceBalance ++;

			// if we have had so many changes since the last rebalance, let's rebalance
			// the tree to keep walks and searches reasonable
			if (changesSinceBalance > REBALANCE_POINT) {
				rebalance();
			}
		}

		/**
		 * removes the successor node to the given node that is supposed to be removed.
		 * @param deleteNode the node to be removed
		 * @return the next node
		 */
		private StringNode removeSuccessor(StringNode deleteNode) {
			StringNode successor = deleteNode.right;

			if (deleteNode.right.left == null) {
				// there are no lower nodes, so deleteNode.right is the successor

				// since it is the successor, the parent of deleteNode, the right
				// must jump
				if (deleteNode.parent != null) {
					deleteNode.parent.right = successor.right;
				}
			} else {
				// try to go all the way down the left
				while (successor.left != null) {
					successor = successor.left;
				}

				// check if successor has the right child, it cannot have left child for sure
				// if it does have the right child, add it to the left of successorParent.
				if (successor.parent != null) {
					successor.parent.left = successor.right;
				}
			}

			// clear these references
			successor.right = null;
			successor.parent = null;

			return successor;
		}

		/**
		 * updates all of the parent node pointers for each node in the string tree.
		 * @param node the current node
		 * @param parent the parent of the node
		 */
		private void updateParents(StringNode node, StringNode parent) {
			if (node == null) {
				return;
			}

			// the node on the left should have us as the parent
			updateParents(node.left, node);
			// the node on the right should have us as a parent
			updateParents(node.right, node);

			// node parent should be the given parent.
			node.parent = parent;
		}

		/**
		 * visit the given node using an inorder traversal of the tree in order to find
		 * and stop at the nth value in the tree.
		 * @param node the starting position
		 * @param iter a NodeIter object.
		 */
		private void visit(StringNode node, NodeIter iter) {
			// if node is null, we have nothing to check
			if (node == null) {
				return;
			}

			// check the node on the left.  If we are done, we will immediately return.
			visit(node.left, iter);
			if (iter.done) {
				return;
			}

			// mark our value and, if we are done, immediately return.
			iter.mark(node.value);
			if (iter.done) {
				return;
			}

			// visit the node on the right since we didn't hit the index yet.
			visit(node.right, iter);
		}

	}


	/**
	 * StringNode is a node container for the string-based BST.
	 */
	public class StringNode {
		public String value;
		public StringNode left = null;
		public StringNode right = null;
		public StringNode parent = null;

		public StringNode(final String value, StringNode parent) {
			this.value = value;
			this.parent = parent;
		}

		public int compare(final String word) {
			if (word == null) {
				return -1;
			}
			if (value == null) {
				return 1;
			}
			return word.compareTo(value);
		}
	}
	
	/**
	 * NodeIter when finding an "index" value in the inorder traversal, this object
	 * contains the curIdx (position in the inorder walk), desiredIdx (index to stop at),
	 * value (the value stored found from the right position) and a done flag
	 * (to leave processing early when possible).
	 */
	public class NodeIter {
		public int curIdx = -1;
		public int desiredIdx;
		public String value = null;
		public boolean done = false;

		public NodeIter(int index) {
			desiredIdx = index;
		}

		public void mark(final String word) {
			// increment our current position so we know what offset we are at
			curIdx++;

			// if we hit the desired index
			if (curIdx == desiredIdx) {
				// save the word in the iter
				value = word;
				// set the flag so we can leave early.
				done = true;
			}
		}
	}
}
