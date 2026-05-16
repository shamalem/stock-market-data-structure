public class TimeStampTree {

    private TimeStampsNode root;
    public TimeStampTree(TimeStampsNode root) {
        this.root = root;
        this.root.setTimeStamp(Long.MAX_VALUE);
        TimeStampsNode left = new TimeStampsNode();
        left.setTimeStamp(Long.MIN_VALUE);
        root.setLeft(left);
        left.setParent(this.root);

        TimeStampsNode mid = new TimeStampsNode();
        mid.setTimeStamp(Long.MAX_VALUE);
        root.setMid(mid);
        mid.setParent(this.root);
        root.setIs_leaf(false);
    }

    public void Update_Key(TimeStampsNode Node) {
        Node.setTimeStamp(Node.getLeft().getTimeStamp());
        if(Node.getLeft().getPrice() != null)
            Node.setPrice(Node.getLeft().getPrice());
        if (Node.getMid() != null) {
            Node.setTimeStamp(Node.getMid().getTimeStamp());
            if(Node.getMid().getPrice() != null)
                Node.setPrice(Node.getMid().getPrice());

        }
        if (Node.getRight() != null) {
            Node.setTimeStamp(Node.getRight().getTimeStamp());
            if(Node.getRight().getPrice() != null)
                Node.setPrice(Node.getRight().getPrice());
        }
    }

    public void Set_Children(TimeStampsNode Node, TimeStampsNode left, TimeStampsNode middle, TimeStampsNode right) {
        Node.setLeft(left);
        Node.setMid(middle);
        Node.setRight(right);
        left.setParent(Node);
        if (middle != null) {
            middle.setParent(Node);
        }
        if (right != null) {
            right.setParent(Node);
        }
        Update_Key(Node);
    }

    public TimeStampsNode InsertAndSplit(TimeStampsNode parent, TimeStampsNode child) {
        TimeStampsNode left = parent.getLeft();
        TimeStampsNode middle = parent.getMid();
        TimeStampsNode right = parent.getRight();
        if (right == null) {
            if (child.getTimeStamp()<(left.getTimeStamp())) {
                Set_Children(parent, child, left, middle);
            } else if (child.getTimeStamp()< middle.getTimeStamp()) {
                Set_Children(parent, left, child, middle);
            } else {
                Set_Children(parent, left, middle, child);
            }
            return null;
        }
        TimeStampsNode newNode = new TimeStampsNode();
        newNode.setIs_leaf(false); // newNode is parent for a child --> not leaf
        if (child.getTimeStamp() < left.getTimeStamp()) {
            Set_Children(parent, child, left, null);
            Set_Children(newNode, middle, right, null);
        } else if (child.getTimeStamp()<middle.getTimeStamp()) {
            Set_Children(parent, left, child, null);
            Set_Children(newNode, middle, right, null);
        } else if (child.getTimeStamp()<right.getTimeStamp()) {
            Set_Children(parent, left, middle, null);
            Set_Children(newNode, child, right, null);
        } else {
            Set_Children(parent, left, middle, null);
            Set_Children(newNode, right, child, null);
        }
        return newNode;
    }

    public void Insert(TimeStampsNode node) {
        TimeStampsNode traverse = this.root;
        while (!traverse.getIs_leaf()) {
            if (node.getTimeStamp()< traverse.getLeft().getTimeStamp()) {
                traverse = traverse.getLeft();
            } else if (node.getTimeStamp()< traverse.getMid().getTimeStamp()) {
                traverse = traverse.getMid();
            } else {
                traverse = traverse.getRight();
            }
        }
        TimeStampsNode x = traverse.getParent();
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
            TimeStampsNode newRoot = new TimeStampsNode();
            newRoot.setIs_leaf(false);
            Set_Children(newRoot, x, node, null);
            this.root = newRoot;
        }
    }
    public TimeStampsNode Borrow_or_Merge(  TimeStampsNode node) {
        TimeStampsNode parent = node.getParent();
        if (node == parent.getLeft()) {
            TimeStampsNode middle = parent.getMid();
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
            TimeStampsNode left=parent.getLeft();
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
            TimeStampsNode middle=parent.getMid();
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
    public void Delete( TimeStampsNode node){

        TimeStampsNode parent = node.getParent();
        if(node == parent.getLeft()){
            Set_Children(parent, parent.getMid(), parent.getRight(), null);
        }
        else if (node == parent.getMid()){
            Set_Children(parent, parent.getLeft(), parent.getRight(), null);
        }
        else {
            Set_Children(parent, parent.getLeft(),parent.getMid(), null);
        }
        while(parent != null){
            if(parent.getMid() == null){
                if(parent != this.root){
                    parent = Borrow_or_Merge(parent);
                }
                else{
                    this.root = parent.getLeft();
                    parent.getLeft().setParent(null);
                    return;
                }
            }
            else {
                Update_Key(parent);
                parent = parent.getParent();
            }

        }
    }
    public TimeStampsNode Search(TimeStampsNode root,long timeStamp){

        if(root.getIs_leaf()){
            if(root.getTimeStamp()==timeStamp){
                return root;
            }
            else {
                return null;
            }
        }
        if(timeStamp<=root.getLeft().getTimeStamp()){
            return Search(root.getLeft(),timeStamp);
        }
        else if ( timeStamp<=root.getMid().getTimeStamp()){
            return Search(root.getMid(), timeStamp);
        }
        return Search(root.getRight(),timeStamp);
    }

    public TimeStampsNode getRoot() {
        return root;
    }
}


