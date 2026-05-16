 public class DoublyLinkedList {
        // Node class represents each element in the doubly linked list
       public class Node {
            String value; // The stock ID
            Node next;    // Pointer to the next node
            Node prev;    // Pointer to the previous node

            Node(String value) {
                this.value = value;
                this.next = null;
                this.prev = null;
            }

            public Node getNext() {
                return next;
            }

            public Node getPrev() {
                return prev;
            }

            public String getValue() {
                return value;
            }
        }

        private Node head; // Head of the list
        private Node tail; // Tail of the list
        private int size;  // Number of elements in the list

        public DoublyLinkedList() {
            this.head = null;
            this.tail = null;
            this.size = 0;
        }

        // Add a stock ID to the end of the list
        public void add(String value) {
            Node newNode = new Node(value);
            if (head == null) {
                head = newNode; // If the list is empty, the new node becomes the head
                tail = newNode;
            } else {
                tail.next = newNode; // Append the new node to the end
                newNode.prev = tail; // Link the new node to the previous tail
                tail = newNode;      // Update the tail pointer
            }
            size++;
        }

        // Add a stock ID to the beginning of the list
        public void addFirst(String value) {
            Node newNode = new Node(value);
            if (head == null) {
                head = newNode; // If the list is empty, the new node becomes the head
                tail = newNode;
            } else {
                newNode.next = head; // Link the new node to the current head
                head.prev = newNode; // Link the current head back to the new node
                head = newNode;      // Update the head pointer
            }
            size++;
        }

        // Remove a stock ID from the list
        public void remove(String value) {
            Node current = head;

            while (current != null) {
                if (current.value.equals(value)) {
                    if (current.prev != null) {
                        current.prev.next = current.next;
                    } else {
                        head = current.next; // Update head if removing the first node
                    }

                    if (current.next != null) {
                        current.next.prev = current.prev;
                    } else {
                        tail = current.prev; // Update tail if removing the last node
                    }

                    size--;
                    return;
                }
                current = current.next;
            }
        }

        // Convert the list to an array
        public String[] toArray() {
            String[] result = new String[size];
            Node current = head;
            int index = 0;

            while (current != null) {
                result[index++] = current.value;
                current = current.next;
            }

            return result;
        }


        public int getSize() {
            return size;
        }

     public void setSize(int size) {
         this.size = size;
     }

     public void setHead(Node head) {
         this.head = head;
     }

     public Node getHead() {
         return head;
     }
     public Node getTail() {
         return head;
     }
     public void setTail(Node tail){
            this.tail=tail;}

 }