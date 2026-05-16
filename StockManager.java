
public class StockManager {
    private StockTree stocks;
     StocksByPriceTree stocksByPrice;

    public StockManager() {
        // add code here
    }

    // 1. Initialize the system
    public void initStocks() {
        StockNode stockRoot = new StockNode();
        this.stocks = new StockTree(stockRoot);
        PriceKey PriceKey = new PriceKey(0.0f, null, null, null, null, null);
        this.stocksByPrice = new StocksByPriceTree(PriceKey);
    }


    public void addStock(String stockId, long timestamp, Float price) {

        if (this.stocks.Search(this.stocks.root, stockId) != null || price <= 0) {
            throw new IllegalArgumentException("Stock with ID " + stockId + " already exists.");
        }
        StockNode newStock = new StockNode(stockId, null, null, null, price);
        TimeStampsNode newStamp = new TimeStampsNode(stockId, null, null, null, price, timestamp);
        TimeStampTree newTree = new TimeStampTree(newStamp);
        stocks.Insert(newStock);
        PriceKey priceKey = new PriceKey(price, stockId, null, null, null, null);
        stocksByPrice.Insert(priceKey);
        newStock.setTimeStamps(newTree);
      //  priceKey.updateSize();

    }

    // 3. Remove a stock
    public void removeStock(String stockId) {
        if (this.stocks.Search(this.stocks.root, stockId) == null) {
            throw new IllegalArgumentException("Stock with ID " + stockId + " does not exist.");
        }
        StockNode s = this.stocks.Search(this.stocks.root, stockId);
        PriceKey p = new PriceKey(s.getPrice(),stockId,null,null,null,null);
        PriceKey p2 = this.stocksByPrice.Search(this.stocksByPrice.root,p);
        this.stocks.Delete(s);
        this.stocksByPrice.delete(p2);



    }

    // 4. Update a stock price
    public void updateStock(String stockId, long timestamp, Float priceDifference) {
        if (this.stocks.Search(this.stocks.root, stockId) == null || priceDifference == 0) {
            throw new IllegalArgumentException();
        }
        StockNode s = this.stocks.Search(this.stocks.root, stockId);
        PriceKey p = new PriceKey(s.getPrice(),stockId,null,null,null,null);
        PriceKey p2 = this.stocksByPrice.Search(this.stocksByPrice.root,p);
        s.setPrice((s.getPrice() + priceDifference));
        this.stocksByPrice.delete(p2); // delete where price with this stockid
        PriceKey newPriceKey = new PriceKey(s.getPrice(), stockId, null, null, null, null);
        stocksByPrice.Insert(newPriceKey);
        TimeStampsNode newStamp = new TimeStampsNode(stockId, null, null, null,
                (priceDifference), timestamp);
        s.getTimeStamps().Insert(newStamp);
        
    }

    // 5. Get the current price of a stock
    public Float getStockPrice(String stockId) {


        StockNode stock = this.stocks.Search(this.stocks.root, stockId);
        if (stock == null) {
            // Debug output to check what IDs are available in the system at the time of error.
            // System.out.println("Failed to find stock: " + stockId);
            throw new IllegalArgumentException("Stock with ID " + stockId + " does not exist in the system.");
        }
        return stock.getPrice();

    }

    // 6. Remove a specific timestamp from a stock's history
    public void removeStockTimestamp(String stockId, long timestamp) {
        StockNode stockNode = this.stocks.Search(this.stocks.root, stockId);
        if (stockNode == null) {
            throw new IllegalArgumentException("Stock does not exist: " + stockId);
        }
        TimeStampTree timestampTree = stockNode.getTimeStamps();
        TimeStampsNode timestampNode = timestampTree.Search(timestampTree.getRoot(), timestamp);
        if (timestampNode == null) {
            throw new IllegalArgumentException("Timestamp does not exist for stock: " + stockId);
        }
        timestampTree.Delete(timestampNode);
        PriceKey p = new PriceKey(stockNode.getPrice(),stockId,null,null,null,null);
        PriceKey p2 = this.stocksByPrice.Search(this.stocksByPrice.root,p);
        stockNode.setPrice(stockNode.getPrice() - timestampNode.getPrice()); // cancel update
        PriceKey newKey = new PriceKey(stockNode.getPrice(), stockId, null, null, null, null);
        this.stocksByPrice.delete(p2); // delete node with same price and stockid
        stocksByPrice.Insert(newKey);
    }

    public int rank(PriceKey x) {
        if (x == null) return -1; // Invalid case
        if(x.getPrice() == Float.MIN_VALUE) return 0 ;
        int rank = x.getSize();
        PriceKey y = x.getParent();

        while (y != null) {
            if (x == y.getMid()) {
                rank += (y.getLeft() != null) ? y.getLeft().getSize() : 0;
            } else if (x == y.getRight()) {
                rank += ((y.getLeft() != null) ? y.getLeft().getSize() : 0) +
                        ((y.getMid() != null) ? y.getMid().getSize() : 0);
            }
            // Move up the tree
            x = y;
            y = y.getParent();
        }

        return rank;
    }

    // 7. Get the amount of stocks in a given price range
    public int getAmountStocksInPriceRange(Float price1, Float price2) {
        if (price1 == null || price2 == null || price2 < price1)throw new IllegalArgumentException();
        PriceKey stock1 = stocksByPrice.search_price(this.stocksByPrice.root, price1);
        PriceKey stock2 = stocksByPrice.search_price(this.stocksByPrice.root, price2);
        if(stock1 !=null && stock2!=null && price1.equals(price2)) {
            if(stock1 == stock2) stock2=stocksByPrice.search_priceFromRight(this.stocksByPrice.root,price2);
            int rank1= rank(stock1);
            int rank2=rank(stock2);

            return rank2-rank1+1;
        }
        if (stock1 == null) stock1 = search_price_next(this.stocksByPrice.root, price1);
        if (stock2 == null) stock2 = search_price_prev(this.stocksByPrice.root, price2);

        int rank1= rank(stock1);
        int rank2=rank(stock2);
        if(rank2 <= rank1) return 0;
        return rank2 - rank1 +1 ;
    }

    PriceKey search_price_prev(PriceKey root, Float price) {
        if (root.getIs_leaf()) {
            if (root.getPrice().equals( price)) {
                return root;
            } else return predecessor(root);
        }
        if (price.compareTo(root.getLeft().getPrice())<=0) {
            return search_price_prev(root.getLeft(), price);
        } else if (price.compareTo(root.getMid().getPrice())<=0) {
            return search_price_prev(root.getMid(), price);
        } else {
            return search_price_prev(root.getRight(), price);
        }
    }



    // 8. Get a list of stock IDs within a given price range
    public String[] getStocksInPriceRange(Float price1, Float price2) {
        if (price1 == null || price2 == null) throw new IllegalArgumentException();

        PriceKey start = search_price_next(this.stocksByPrice.root, price1);
        PriceKey end = search_price_prev(this.stocksByPrice.root, price2);
        if (start == null || end == null || rank(end) < rank(start)) return new String[0];
        updateNextPointers(this.stocksByPrice.root, start, end);
        int count = getAmountStocksInPriceRange(price1,price2) ;
        String[] stockIds = new String[count];

        PriceKey current = start;
        int index = 0;
        while (current != null &&index<count) {
            if(current.getPrice()==Float.MIN_VALUE){
                stockIds[index++] = current.getParent().getStockId();
                current=current.getNext();
            }
            else stockIds[index++] = current.getStockId();
            current = current.getNext();
        }

        return stockIds;
    }
    public PriceKey getSibling(PriceKey node) {
        if (node == null || node.getParent() == null) {
            return null; // No sibling exists if the node is root or null
        }

        PriceKey parent = node.getParent();

        // Identify which sibling to return based on the position of `node`
        if (node == parent.getLeft()) {
            return parent.getMid() != null ? parent.getMid() : parent.getRight();
        } else if (node == parent.getMid()) {
            return parent.getRight() != null ? parent.getRight() : parent.getLeft();
        } else { // node == parent.getRight()
            return parent.getMid() != null ? parent.getMid() : parent.getLeft();
        }
    }
    public PriceKey predecessor(PriceKey node) {
        PriceKey parent = node.getParent();
        PriceKey sibling;
        if (parent == null) {
            return null;
        }
        while (node == parent.getLeft() || (parent.getLeft() == null && node == parent.getMid())) {
            node = parent;
            parent = parent.getParent();
            if (parent == null) {
                return null;
            }
        }
        if (node == parent.getRight()) {
            sibling = parent.getMid();
        } else {
            sibling = parent.getLeft();
        }
        if (sibling == null) {
            return null;
        }

        while (! (sibling.getIs_leaf()) ) {
            if (sibling.getRight() != null) {
                sibling = sibling.getRight();
            } else {
                return sibling;
            }
        }

        // Ensure it's a valid predecessor
        if (!(sibling.getPrice() == Float.MAX_VALUE)) {
            return sibling;
        } else {
            return null;
        }
    }
    public PriceKey search_price_next(PriceKey root, Float price) {

            if (root == null || root.getIs_leaf()) {
                // If it's a leaf node, return it only if it's not a sentinel node.
                return (root != null && root.getPrice() != Float.MIN_VALUE && root.getPrice() != Float.MAX_VALUE) ? root : null;
            }

            PriceKey candidate = null;

            // If price is smaller than or equal to left child
            if (price.compareTo(root.getLeft().getPrice()) <= 0) {
                candidate = search_price_next(root.getLeft(), price);
            }

            // If middle exists and price is within its range
            if (candidate == null && root.getMid() != null && price.compareTo(root.getMid().getPrice()) <= 0) {
                candidate = search_price_next(root.getMid(), price);
            }

            // If right exists and price is within its range
            if (candidate == null && root.getRight() != null && price.compareTo(root.getRight().getPrice()) <= 0) {
                candidate = search_price_next(root.getRight(), price);
            }

            return candidate;
        }




    public Float round(Float value) {
        int temp = (int) (value * 100 + 0.5f);
        return  temp / 100f;

    }

    public void printStocksAndPrices(PriceKey root) {
        if (root == null) {
            return;
        }
        if (!root.getIs_leaf()) {
            printStocksAndPrices(root.getLeft());
            printStocksAndPrices(root.getMid());
            printStocksAndPrices(root.getRight());
        }
    }




    public void updateNextPointers(PriceKey root, PriceKey start, PriceKey end) {
        if (root == null || start == null || end == null) {
            return;
        }

        PriceKey prev = null;
        prev = connectLeaves(root, prev, start, end);
    }
    private PriceKey connectLeaves(PriceKey node, PriceKey prev, PriceKey start, PriceKey end) {
        if (node == null) {
            return prev; // Return the last connected leaf if node is null
        }
        // Check if the current node is a leaf
        if (node.getIs_leaf()) {
            if (node == start) {
                prev = node; // Start connecting from this node
            } else if (prev != null) {
                prev.setNext(node); // Connect the previous node to the current one
                node.setPrev(prev); // Set the current node's previous pointer
                prev = node; // Update the prev pointer to the current node
            }
            if (node == end) {
                return prev; // Stop connecting once we reach the end
            }
        } else {
            // Recursively connect leaves in the left, middle, and right subtrees
            prev = connectLeaves(node.getLeft(), prev, start, end);
            prev = connectLeaves(node.getMid(), prev, start, end);
            prev = connectLeaves(node.getRight(), prev, start, end);
        }

        return prev;
    }




}


