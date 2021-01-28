package ru.otus.model;

import java.util.Objects;

public class Node {
    public String name;
    public Node child;

    public Node(String name, Node child) {
        this.name = name;
        this.child = child;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return name.equals(node.name) &&
                Objects.equals(child, node.child);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, child);
    }
}
