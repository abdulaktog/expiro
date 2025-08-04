package bst;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class ProductBST {
    private static ProductBST instance; // Singleton instance
    private ProductNode root; // Tree root

    private ProductBST() {
        root = null;
    }

    public static ProductBST getInstance() {
        if (instance == null) {
            instance = new ProductBST();
        }
        return instance;
    }

    public void insert(String key, String data) {
        root = insertRec(root, key, data);
    }

    private ProductNode insertRec(ProductNode root, String key, String data) {
        if (root == null) {
            root = new ProductNode(key, data);
            return root;
        }
        if (key.compareTo(root.key) < 0)
            root.left = insertRec(root.left, key, data);
        else if (key.compareTo(root.key) > 0)
            root.right = insertRec(root.right, key, data);
        return root;
    }

    public LocalDate search(String key) {
        ProductNode result = searchRec(root, key);
        if (result != null)
            return result.data;
        else
            return LocalDate.of(1, 1, 1);
    }

    private ProductNode searchRec(ProductNode root, String key) {
        if (root == null || root.key.equals(key))
            return root;
        if (root.key.compareTo(key) < 0)
            return searchRec(root.right, key);
        return searchRec(root.left, key);
    }

    //  ADD BELOW
    public List<ProductNode> inOrderTraversal() {
        List<ProductNode> products = new ArrayList<>();
        inOrderTraversalRec(root, products);
        return products;
    }

    private void inOrderTraversalRec(ProductNode node, List<ProductNode> products) {
        if (node != null) {
            inOrderTraversalRec(node.left, products);
            products.add(node);
            inOrderTraversalRec(node.right, products);
        }
    }
}
