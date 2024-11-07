/**
 * RBT
 * Red-Black Tree Insert
 * @author Sean Idisi
 */
import java.util.*;
public class RBT {
    public Node root;

    public RBT() {}

    public boolean isRed(Node x) {
        if (x == null) return false;
        return x.getColor() == Node.Color.RED;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public boolean contains(int x) {
        return nodeContainsData(root, x);
    }

    private boolean nodeContainsData(Node r, int x) {
        while (r != null) {
            if (r.getData() - x < 0) {
                r = r.getLeft();
            } else if (r.getData() - x > 0) {
                r = r.getRight();
            } else {
                return true;
            }
        }
        return false;
    }

    public List<Integer> serializeTree() {
        return serializeTree(root);
    }

    private List<Integer> serializeTree(Node r) {
        if (r == null) return new LinkedList<>();
        int data = r.getData();
        List<Integer> left = serializeTree(r.getLeft());
        List<Integer> right = serializeTree(r.getRight());
        left.add(data);
        left.addAll(right);
        return left;
    }

    public int maxHeight() {
        return maxHeight(root);
    }

    private int maxHeight(Node r) {
        if (r==null) return 0;
        return 1 + Math.max(maxHeight(r.getLeft()), maxHeight(r.getRight()));
    }




    // ************************************************************************
    // * INSERT INTO RED-BLACK TREE
    // ************************************************************************

    public void insert(int x) {
        root = nodeInsertData(root, x);
        root.setColor(Node.Color.BLACK);
    }

    private Node getGrandParent(Node h) {
        if ((h != null) && (h.getParent() != null)) {
            return h.getParent().getParent();
        } else {
            return null;
        }
    }

    private Node getUncle(Node h) {
        Node grandParent = getGrandParent(h);
        if (grandParent == null) {
            return null;
        }
        if (h.getParent() == grandParent.getLeft()) {
            return grandParent.getRight();
        } else {
            return grandParent.getLeft();
        }
    }

    private Node nodeInsertData(Node r, int x) {
        if (r == null) return new Node(x, Node.Color.RED);
        Node curr = r;
        Node parent = null;

        while(curr != null) {
            parent = curr;
            if(x < curr.getData()){
                curr = curr.getLeft();
            } else if (x > curr.getData()) {
                curr = curr.getRight();
            } else {
                return root;
            }
        }
        Node newNode = new Node(x, Node.Color.RED);
        if (x < parent.getData()) {
            parent.setLeft(newNode);
        } else {
            parent.setRight(newNode);
        }

        newNode.setParent(parent);
        fixViolations(newNode);
        return root;
    }

    private void fixViolations(Node newNode) {
        newNode.setColor(Node.Color.RED);

        while (newNode != root && newNode.getParent().getColor() == Node.Color.RED) {
            Node parent = newNode.getParent();
            Node grandParent = getGrandParent(newNode);

            if (grandParent != null) {
                if (parent == grandParent.getLeft()) {
                    Node uncle = grandParent.getRight();

                    // Case 1: Uncle is RED (Recoloring)
                    if (uncle != null && uncle.getColor() == Node.Color.RED) {
                        flipColors(grandParent);
                        newNode = grandParent;
                    } else {
                        // Case 2: Uncle is BLACK and newNode is a right child (Left Rotation)
                        if (newNode == parent.getRight()) {
                            newNode = parent;
                            rotateLeft(newNode);
                        }else {
                            // Case 3: Uncle is BLACK and newNode is a left child (Right Rotation)
                            parent.setColor(Node.Color.BLACK);
                            grandParent.setColor(Node.Color.RED);
                            rotateRight(grandParent);
                        }
                    }
                } else {
                    Node uncle = grandParent.getLeft();

                    // Case 1: Uncle is RED (Recoloring)
                    if (uncle != null && uncle.getColor() == Node.Color.RED) {
                        flipColors(grandParent);
                        newNode = grandParent;
                    } else {
                        // Case 2: Uncle is BLACK and newNode is a left child (Right Rotation)
                        if (newNode == parent.getLeft()) {
                            newNode = parent;
                            rotateRight(newNode);
                        }else {
                            // Case 3: Uncle is BLACK and newNode is a right child (Left Rotation)
                            parent.setColor(Node.Color.BLACK);
                            grandParent.setColor(Node.Color.RED);
                            rotateLeft(grandParent);
                        }
                    }
                }
            }else{
                break;
            }
        }
        root.setColor(Node.Color.BLACK);
    }





    private Node rotateRight(Node h) {
        Node parent = h.getParent();
        Node leftChild = h.getLeft();
        h.setLeft(leftChild.getRight());
        if(leftChild.getRight() != null){
            leftChild.getRight().setParent(h);
        }
        leftChild.setRight(h);
        h.setParent(leftChild);
        if(parent == null){
            root = leftChild;
        } else if (parent.getLeft() == h) {
            parent.setLeft(leftChild);
        } else {
            parent.setRight(leftChild);
        }

        leftChild.setParent(parent);
        return leftChild;

    }

    private Node rotateLeft(Node h) {
        Node parent = h.getParent();
        Node rightChild = h.getRight();
        h.setRight(rightChild.getLeft());

        if(rightChild.getLeft() != null){
            rightChild.getLeft().setParent(h);
        }
        rightChild.setLeft(h);
        h.setParent(rightChild);
        if(parent == null){
            root = rightChild;
        } else if (parent.getLeft() == h) {
            parent.setLeft(rightChild);
        } else {
            parent.setRight(rightChild);
        }

        rightChild.setParent(parent);
        return rightChild;
    }

    // flip the colors of a node and its two children
    private void flipColors(Node h) {
        h.setColor(h.getColor() == Node.Color.RED ? Node.Color.BLACK : Node.Color.RED);
        h.getLeft().setColor(h.getLeft().getColor() == Node.Color.RED ? Node.Color.BLACK : Node.Color.RED);
        h.getRight().setColor(h.getRight().getColor() == Node.Color.RED ? Node.Color.BLACK : Node.Color.RED);
    }
}
