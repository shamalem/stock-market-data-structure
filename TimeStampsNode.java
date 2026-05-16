public class TimeStampsNode {
    private String stockId ;
    private long timeStamp; // node key
    private TimeStampsNode left;
    private TimeStampsNode mid;
    private TimeStampsNode right;
    private TimeStampsNode parent;
    private Float price ;
    private Boolean is_leaf=true;

    public TimeStampsNode(String stockId,TimeStampsNode left,TimeStampsNode mid,TimeStampsNode right,Float price,long timeStamp) {
        this.stockId= stockId;
        this.mid=mid;
        this.left=left;
        this.right=right;
        this.price=price;
        this.timeStamp=timeStamp;

    }
    public TimeStampsNode() {

        this(null, null, null,null,null,-1);
    }







    // getters & setters

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public Boolean getIs_leaf() {
        return is_leaf;
    }

    public void setIs_leaf(Boolean is_leaf) {
        this.is_leaf = is_leaf;
    }

    public TimeStampsNode getLeft() {
        return left;
    }

    public void setLeft(TimeStampsNode left) {
        this.left = left;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public TimeStampsNode getMid() {
        return mid;
    }

    public void setMid(TimeStampsNode mid) {
        this.mid = mid;
    }

    public TimeStampsNode getParent() {
        return parent;
    }

    public void setParent(TimeStampsNode parent) {
        this.parent = parent;
    }

    /*public double getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }*/

    public TimeStampsNode getRight() {
        return right;
    }

    public void setRight(TimeStampsNode right) {
        this.right = right;
    }

    public String getStockId() {
        return stockId;
    }

    public Float getPrice() {
        return price;
    }
}


