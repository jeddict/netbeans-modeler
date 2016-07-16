/**
 * Copyright [2016] Gaurav Gupta
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.netbeans.modeler.specification.model.document.property;

import java.util.NoSuchElementException;

public class PropertyLinkedList<E> {
 
    private Node head;
    private Node tail;
    private int size;
     
    public PropertyLinkedList() {
        size = 0;
    }

    /**
     * @return the head
     */
    public Node getHead() {
        return head;
    }

    /**
     * @return the tail
     */
    public Node getTail() {
        return tail;
    }
    /**
     * this class keeps track of each element information
     * @author java2novice
     *
     */
    public class Node {
        E element;
        Node next;
        Node prev;
 
        public Node(E element, Node next, Node prev) {
            this.element = element;
            this.next = next;
            this.prev = prev;
        }
    }
    /**
     * returns the size of the linked list
     * @return
     */
    public int size() { return size; }
     
    /**
     * return whether the list is empty or not
     * @return
     */
    public boolean isEmpty() { return size == 0; }
     
    /**
     * adds element at the starting of the linked list
     * @param element
     */
    public void addFirst(E element) {
        Node tmp = new Node(element, getHead(), null);
        if(getHead() != null ) {head.prev = tmp;}
        head = tmp;
        if(getTail() == null) { tail = tmp;}
        size++;
        System.out.println("adding: "+element);
    }
     
    /**
     * adds element at the end of the linked list
     * @param element
     */
    public void addLast(E element) {
         
        Node tmp = new Node(element, null, getTail());
        if(getTail() != null) {tail.next = tmp;}
        tail = tmp;
        if(getHead() == null) { head = tmp;}
//        size++;
        System.out.println("adding: "+element);
    }
    
      public void addLast(Node tmp) {
         
//        Node tmp = new Node(element, null, getTail());
          
        if(getTail() != null) {
            tmp.prev = tail;
            tail.next = tmp;
        }
        tail = tmp;
        if(getHead() == null) { 
            tmp.prev=null;
            head = tmp;
        }
//        size++;
        System.out.println("adding: "+tmp);
    }
     
    /**
     * this method walks forward through the linked list
     */
//    public void iterateForward(){
//         
//        System.out.println("iterating forward..");
//        Node tmp = getHead();
//        while(tmp != null){
//            System.out.println(tmp.element);
//            tmp = tmp.next;
//        }
//    }
//     
//    /**
//     * this method walks backward through the linked list
//     */
//    public void iterateBackward(){
//         
//        System.out.println("iterating backword..");
//        Node tmp = getTail();
//        while(tmp != null){
//            System.out.println(tmp.element);
//            tmp = tmp.prev;
//        }
//    }
     
    /**
     * this method removes element from the start of the linked list
     * @return
     */
//    public E removeFirst() {
//        if (size == 0) throw new NoSuchElementException();
//        Node tmp = getHead();
//        head = getHead().next;
//        head.prev = null;
////        size--;
//        System.out.println("deleted: "+tmp.element);
//        return tmp.element;
//    }
//     
//    /**
//     * this method removes element from the end of the linked list
//     * @return
//     */
//    public E removeLast() {
//        if (size == 0) throw new NoSuchElementException();
//        Node tmp = getTail();
//        tail = getTail().prev;
//        tail.next = null;
////        size--;
//        System.out.println("deleted: "+tmp.element);
//        return tmp.element;
//    }
    
     
}

/**
 *   DoublyLinkedListImpl<Integer> dll = new DoublyLinkedListImpl<Integer>();
        dll.addFirst(10);
        dll.addFirst(34);
        dll.addLast(56);
        dll.addLast(364);
        dll.iterateForward();
        dll.removeFirst();
        dll.removeLast();
        dll.iterateBackward();
 */