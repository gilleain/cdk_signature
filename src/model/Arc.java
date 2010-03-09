package model;

public class Arc {
    
    private Node left;
    
    private Node right;
    
    private int order;
    
    public boolean isNew = false;
    
    public Arc(Node left, Node right) {
        this(left, right, 2);
    }
    
    public Arc(Node left, Node right, int order) {
        this.left = left;
        this.right = right;
        this.order = order;
    }
    
    public Arc(Arc other) {
        this.left = new Node(other.left);
        this.right = new Node(other.right);
        this.order = other.order;
    }
    
    public boolean contains(Node node) {
        return this.left == node || this.right == node;
    }
    
    public Node getLeft() {
        return this.left;
    }
    
    public Node getRight() {
        return this.right;
    }

    public int getOrder() {
        return this.order;
    }

}
