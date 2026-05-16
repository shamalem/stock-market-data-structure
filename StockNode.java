public class StockNode {
    private String stockId; // Node key
    private StockNode left;
    private StockNode mid;
    private StockNode right;
    private StockNode parent;
    private Boolean is_leaf = true;
    private Float price;
    private TimeStampTree timeStamps;
    private StocksByPriceTree stocksByPriceTree;

    public StockNode(String stockId, StockNode left, StockNode mid, StockNode right, Float price) {
        this.stockId = stockId;
        this.mid = mid;
        this.left = left;
        this.right = right;
        this.price = price;

    }

    public StockNode() {

        this(null, null, null, null, null);


    }


    // getters & setters
    public Boolean getIs_leaf() {
        return is_leaf;
    }

    public void setIs_leaf(Boolean is_leaf) {
        this.is_leaf = is_leaf;
    }

    public StockNode getLeft() {
        return left;
    }

    public void setLeft(StockNode left) {
        this.left = left;
    }

    public StockNode getMid() {
        return mid;
    }

    public void setMid(StockNode mid) {
        this.mid = mid;
    }

    public StockNode getParent() {
        return parent;
    }

    public void setParent(StockNode parent) {
        this.parent = parent;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public StockNode getRight() {
        return right;
    }

    public void setRight(StockNode right) {
        this.right = right;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public TimeStampTree getTimeStamps() {
        return timeStamps;
    }

    public void setTimeStamps(TimeStampTree newTree) {
        this.timeStamps= newTree;
    }


}
