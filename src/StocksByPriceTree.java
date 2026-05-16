public class StocksByPriceTree {
    PriceKey root;
    public StocksByPriceTree(PriceKey root) {
        this.root = root;
        if (root != null) {
            this.root.setPrice(Float.MAX_VALUE);
            PriceKey left = new PriceKey();
            left.setPrice(Float.MIN_VALUE);
            root.setLeft(left);
            left.updateSize();
            left.setParent(this.root);
            PriceKey mid = new PriceKey();
            mid.setPrice(Float.MAX_VALUE);
            mid.updateSize();
            root.setMid(mid);
            mid.setParent(this.root);
            root.setIs_leaf(false);
            root.updateSize(); // Update size after tree initialization
        }
    }


    public void Update_Key(PriceKey Node) {
        Node.setStockId(Node.getLeft().getStockId());
        Node.setPrice(Node.getLeft().getPrice());


        if (Node.getMid()!= null) {
            Node.setStockId(Node.getMid().getStockId());
            Node.setPrice(Node.getMid().getPrice());


        }
        if (Node.getRight()!= null) {
            Node.setStockId(Node.getRight().getStockId());
            Node.setPrice(Node.getRight().getPrice());

        }
        Node.updateSize(); // Update size after modifying keys

    }

    public void Set_Children(PriceKey node, PriceKey left, PriceKey middle, PriceKey right) {
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
    public PriceKey InsertAndSplit(PriceKey parent, PriceKey child) {
        if (parent == null || child == null) {
            return null;  // Early exit if inputs are invalid
        }
        PriceKey left = parent.getLeft();
        PriceKey middle = parent.getMid();
        PriceKey right = parent.getRight();

        if (right == null) {
            if (child.compareTo(left) < 0) {
                Set_Children(parent, child, left, middle);
            } else if (child.compareTo(middle) < 0) {
                Set_Children(parent, left, child, middle);
            } else {
                Set_Children(parent, left, middle, child);
            }
            return null;
        }
        PriceKey newNode = new PriceKey();
        newNode.setIs_leaf(false);

        if ( child.compareTo(left) < 0) {
            Set_Children(parent,child,left,null);
            Set_Children(newNode,middle,right,null);
        } else if ( child.compareTo(middle) < 0) {
            Set_Children(parent, left, child, null);
            Set_Children(newNode, middle, right, null);
        } else if (child.compareTo(right) < 0) {
            Set_Children(parent, left, middle, null);
            Set_Children(newNode, child, right, null);
        } else {
            Set_Children(parent, left, middle, null);
            Set_Children(newNode, right, child, null);
        }


        return newNode;
    }


    public void Insert(PriceKey node) {
        if (node == null || this.root == null) return; // Prevent inserting null nodes

        PriceKey traverse = this.root;
        while (!traverse.getIs_leaf()) {
            if (node.compareTo(traverse.getLeft()) < 0 && traverse.getLeft() != null) {
                traverse = traverse.getLeft();
            } else if (traverse.getMid() != null &&(double) node.compareTo(traverse.getMid())< 0) {
                traverse = traverse.getMid();
            } else if (traverse.getRight() != null) {
                traverse = traverse.getRight();
            } else {
                break; // Exit if right is null and no valid movement can be made
            }
        }
        PriceKey x = traverse.getParent();
        x.setIs_leaf(false);
        node = InsertAndSplit(x, node);

        while (x != this.root) {
            x = x.getParent();
            if (node != null) {
                node = InsertAndSplit(x, node);
            } else {
                Update_Key(x);
            }
        }
        if (node != null) {
            PriceKey newRoot = new PriceKey();
            newRoot.setIs_leaf(false);
            Set_Children(newRoot, x, node, null);
            this.root = newRoot;
        }

    }
    public PriceKey Borrow_or_Merge(  PriceKey node) {
        PriceKey parent = node.getParent();
        if (node == parent.getLeft()) {
            PriceKey middle = parent.getMid();
            if (middle.getRight() != null) {
                Set_Children(node, node.getLeft(), middle.getLeft(), null);
                Set_Children(middle, middle.getMid(), middle.getRight(), null);
            } else {
                Set_Children(middle,node.getLeft(),middle.getLeft(),middle.getMid());
                Set_Children(parent,middle,parent.getRight(),null);
            }
            return parent;
        }
        else if(node==parent.getMid()){
            PriceKey left=parent.getLeft();
            if(left.getRight()!=null){
                Set_Children(node,left.getRight(),node.getLeft(),null);
                Set_Children(left,left.getLeft(),left.getMid(),null);
            }
            else{
                Set_Children(left,left.getLeft(),left.getMid(),node.getLeft());
                Set_Children(parent,left,parent.getRight(),null);
            }
            return parent;
        }
        else{
            PriceKey middle=parent.getMid();
            if(middle.getRight()!=null) {
                Set_Children(node, middle.getRight(), node.getLeft(), null);
                Set_Children(middle, middle.getLeft(), middle.getMid(), null);
            }
            else{
                Set_Children(middle, middle.getLeft(), middle.getMid(), node.getLeft());
                Set_Children(parent, parent.getLeft(), middle, null);
            }

        }
        return parent;


    }

    public void delete(PriceKey node) {
        if (node == null || node.getParent() == null) return;

        PriceKey parent = node.getParent();
        if (node==(parent.getLeft())) {
            Set_Children(parent, parent.getMid(), parent.getRight(), null);
        }
        else if (node==(parent.getMid())) {
            Set_Children(parent, parent.getLeft(), parent.getRight(), null);
        }
        else {
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
    PriceKey search_price(PriceKey root, Float price) {
        if (root.getIs_leaf()) {
            if (root.getPrice().equals(price)) {
                return root;
            }
            return null;
        }
        if (price <= root.getLeft().getPrice()) {
            return search_price(root.getLeft(),price);
        } else if (price <= root.getMid().getPrice()) {
            return search_price(root.getMid(), price);
        } else {
            return search_price(root.getRight(), price);
        }
    }

    public PriceKey search_priceFromRight(PriceKey root, Float price) {
        if (root.getIs_leaf()) {
            if (root.getPrice().equals(price)) {
                return root;
            }
            return null;
        }
        if( root.getRight()!= null && price<=root.getRight().getPrice())
            return search_price(root.getRight(), price);

        else if (root.getMid()!=null && price <= root.getMid().getPrice()) {
            return search_price(root.getMid(), price);
        }
      else
            return search_price(root.getLeft(),price);
    }



    public PriceKey Search(PriceKey root, PriceKey priceKey) {
        if (root == null || priceKey == null) return null;

        if (root.getIs_leaf()) {
            boolean check1 = root.getPrice().equals(priceKey.getPrice());
            boolean check2 = root.getStockId().equals(priceKey.getStockId());
            if (check1 && check2) return root;
            return null;
        }
        else {
            if (root.getLeft() != null && priceKey.compareTo(root.getLeft()) <= 0) {
                return Search(root.getLeft(), priceKey);
            } else if (root.getMid() != null && priceKey.compareTo(root.getMid()) <= 0) {
                return Search(root.getMid(), priceKey);
            } else if (root.getRight() != null) {
                return Search(root.getRight(), priceKey);
            }
        }
        return null;
    }


}

