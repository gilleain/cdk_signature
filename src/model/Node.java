package model;

public class Node {
    
    private String label;
    
    public Node(String label) {
        this.label = label;
    }
    
    public Node(Node other) {
        this.label = other.label;
    }

    public String getLabel() {
        return label;
    }
    
}
