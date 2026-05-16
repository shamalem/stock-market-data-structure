public class PriceKey  implements Comparable<PriceKey> {
    private Float price;
    private String stockId;
    private PriceKey left, mid, right, parent, next, prev;
    private Boolean is_leaf=true;
    private int size;
    public PriceKey(float price, String stockId, PriceKey left, PriceKey mid, PriceKey right, PriceKey parent) {
        this.price = price;
        this.stockId = stockId;
        this.left = left;
        this.mid = mid;
        this.right = right;
        this.parent = parent;
        this.updateSize();
        this.next=null;
        this.prev=null;



    }

    @Override
    public int compareTo(PriceKey other) {
        // Compare prices first
        int cmp = this.price.compareTo(other.price);
        if (cmp != 0) {
            return cmp;
        }
        // If prices are equal, compare stock IDs
        return this.stockId.compareTo(other.stockId);
    }

    public PriceKey(){
        this(0,null,null,null,null,null);
    }
    public void updateSize() {
        if(this.price == Float.MIN_VALUE ||this.price== Float.MAX_VALUE) {
            this.size = 0;
            return;
        }
        if (is_leaf) {
            size = 1;
        } else {
            size = 0;
            if (left != null) size += left.size;
            if (mid != null) size += mid.size;
            if (right != null) size += right.size;
        }
    }
    public int getSize() {
        return size;
    }

    public Float getPrice() {
        return (float) price;
    }
    public String getStockId() {
        return stockId;
    }
    public PriceKey getLeft() {
        return left;
    }
    public PriceKey getMid() {
        return mid;
    }
    public PriceKey getRight() {
        return right;
    }
    public PriceKey getParent() {
        return parent;
    }
    public void setPrice(Float price) {
        this.price = price;
    }
    public void setStockId(String stockId) {
        this.stockId = stockId;
    }
    public void setLeft(PriceKey left) {
        this.left = left;
    }
    public void setMid(PriceKey mid) {
        this.mid = mid;
    }
    public void setRight(PriceKey right) {
        this.right = right;
    }
    public void setParent(PriceKey parent) {
        this.parent = parent;
    }
    public Boolean getIs_leaf() {
        return is_leaf;
    }
    public void setIs_leaf(Boolean is_leaf) {
        this.is_leaf = is_leaf;
    }



    public void setPrev(PriceKey prev) {
        this.prev = prev;
    }

    public void setNext(PriceKey next) {
        this.next = next;
    }

    public PriceKey getNext() {
        return next;
    }



}