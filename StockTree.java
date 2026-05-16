
public class StockTree {
    StockNode root;

    public StockTree(StockNode root) {
        this.root = root;
        if (root != null) {
            this.root.setStockId("\uFFFF\uFFFF\uFFFF\uFFFF");
            StockNode left = new StockNode();
            this.root.setStockId("\uFFFF\uFFFF\uFFFF\uFFFF");
            left.setStockId("");
            root.setLeft(left);
            left.setParent(this.root);

            StockNode mid = new StockNode();
            mid.setStockId("\uFFFF\uFFFF\uFFFF\uFFFF");
            root.setMid(mid);
            mid.setParent(this.root);
            root.setIs_leaf(false);
        }
    }

    public void Update_Key(StockNode Node) {
        Node.setStockId(Node.getLeft().getStockId());
        if(Node.getLeft().getPrice() != null)
        Node.setPrice(Node.getLeft().getPrice());

        if (Node.getMid()!= null) {
            Node.setStockId(Node.getMid().getStockId());
            if(Node.getMid().getPrice() !=null);
             Node.setPrice(Node.getMid().getPrice());

        }
        if (Node.getRight()!= null) {
            Node.setStockId(Node.getRight().getStockId());
            if(Node.getRight().getPrice()!=null)
            Node.setPrice(Node.getRight().getPrice());

        }

    }


    public void Set_Children(StockNode node, StockNode left, StockNode middle, StockNode right) {
        if (node != null) {
            node.setLeft(left);
            node.setMid(middle);
            node.setRight(right);

            if (left != null) {
                left.setParent(node);
            }
            if (middle != null) {
                middle.setParent(node);
            }
            if (right != null) {
                right.setParent(node);
            }
            Update_Key(node);
        }
    }
    public StockNode InsertAndSplit(StockNode parent, StockNode child) {
        if (parent == null || child == null) {
            return null;  // Early exit if inputs are invalid
        }
        StockNode left = parent.getLeft();
        StockNode middle = parent.getMid();
        StockNode right = parent.getRight();

        if (right == null) {
            if (child.getStockId().compareTo(left.getStockId()) < 0) {
                Set_Children(parent, child, left, middle);
            } else if (child.getStockId().compareTo(middle.getStockId()) < 0) {
                Set_Children(parent, left, child, middle);
            } else {
                Set_Children(parent, left, middle, child);
            }
            return null;
        }
            StockNode newNode = new StockNode();
            newNode.setIs_leaf(false);

            if (child.getStockId().compareTo(left.getStockId()) < 0) {
                Set_Children(parent,child,left,null);
                Set_Children(newNode,middle,right,null);
            } else if (child.getStockId().compareTo(middle.getStockId()) < 0) {
                Set_Children(parent, left, child, null);
                Set_Children(newNode, middle, right, null);
            } else if (child.getStockId().compareTo(right.getStockId()) < 0) {
                Set_Children(parent, left, middle, null);
                Set_Children(newNode, child, right, null);
            } else {
                Set_Children(parent, left, middle, null);
                Set_Children(newNode, right, child, null);
            }


        return newNode;
    }


    public void Insert(StockNode node) {
        if (node == null || this.root == null) return; // Prevent inserting null nodes

        StockNode traverse = this.root;
        while (!traverse.getIs_leaf()) {
            if (node.getStockId().compareTo(traverse.getLeft().getStockId()) < 0 && traverse.getLeft() != null) {
                traverse = traverse.getLeft();
            } else if (traverse.getMid() != null && node.getStockId().compareTo(traverse.getMid().getStockId()) < 0) {
                traverse = traverse.getMid();
            } else if (traverse.getRight() != null) {
                traverse = traverse.getRight();
            } else {
                break; // Exit if right is null and no valid movement can be made
            }
        }

        StockNode x = traverse.getParent();
        if (x != null) {
            x.setIs_leaf(false);
            node = InsertAndSplit(x, node);
            while (x != this.root && node != null) {
                x = x.getParent();
                node = InsertAndSplit(x, node);
            }
        }

        if (node != null) {
            StockNode newRoot = new StockNode();
            newRoot.setIs_leaf(false);
            Set_Children(newRoot, this.root, node, null);
            this.root = newRoot;
        }
    }

    public StockNode Borrow_or_Merge(StockNode node) {
        if (node == null || node.getParent() == null) return null;

        StockNode parent = node.getParent();
        if (node.equals(parent.getLeft())) {
            StockNode middle = parent.getMid();
            if (middle != null && middle.getRight() != null) {
                Set_Children(node, node.getLeft(), middle.getLeft(), null);
                Set_Children(middle, middle.getMid(), middle.getRight(), null);
            } else if (middle != null) {
                Set_Children(middle, node.getLeft(), middle.getLeft(), middle.getMid());
                Set_Children(parent, middle, parent.getRight(), null);
            }
        } else if (node.equals(parent.getMid())) {
            StockNode left = parent.getLeft();
            if (left != null && left.getRight() != null) {
                Set_Children(node, left.getRight(), node.getLeft(), null);
                Set_Children(left, left.getLeft(), left.getMid(), null);
            } else if (left != null) {
                Set_Children(left, left.getLeft(), left.getMid(), node.getLeft());
                Set_Children(parent, left, parent.getRight(), null);
            }
        } else {
            StockNode middle = parent.getMid();
            if (middle != null && middle.getRight() != null) {
                Set_Children(node, middle.getRight(), node.getLeft(), null);
                Set_Children(middle, middle.getLeft(), middle.getMid(), null);
            } else if (middle != null) {
                Set_Children(middle, middle.getLeft(), middle.getMid(), node.getLeft());
                Set_Children(parent, parent.getLeft(), middle, null);
            }
        }
        return parent;
    }

    public void Delete(StockNode node) {
        if (node == null || node.getParent() == null) return;

        StockNode parent = node.getParent();
        if (node.equals(parent.getLeft())) {
            Set_Children(parent, parent.getMid(), parent.getRight(), null);
        } else if (node.equals(parent.getMid())) {
            Set_Children(parent, parent.getLeft(), parent.getRight(), null);
        } else {
            Set_Children(parent, parent.getLeft(), parent.getMid(), null);
        }

        while (parent != null) {
            if (parent.getMid() == null) {
                if (parent != this.root) {
                    parent = Borrow_or_Merge(parent);
                } else {
                    this.root = parent.getLeft();
                    if (this.root != null) this.root.setParent(null);
                    break;
                }
            } else {
                Update_Key(parent);
                parent = parent.getParent();
            }
        }
    }

    public StockNode Search(StockNode root, String stockId) {
        if (root == null || stockId == null) return null;

        if (root.getIs_leaf()) {
            return root.getStockId().equals(stockId) ? root : null;
        } else {
            if (root.getLeft() != null && stockId.compareTo(root.getLeft().getStockId()) <= 0) {
                return Search(root.getLeft(), stockId);
            } else if (root.getMid() != null && stockId.compareTo(root.getMid().getStockId()) <= 0) {
                return Search(root.getMid(), stockId);
            } else if (root.getRight() != null) {
                return Search(root.getRight(), stockId);
            }
        }
        return null;
    }

}
