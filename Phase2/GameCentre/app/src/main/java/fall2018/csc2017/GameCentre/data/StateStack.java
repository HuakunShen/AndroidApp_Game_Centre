package fall2018.csc2017.GameCentre.data;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

public class StateStack<E> implements Serializable {

    /**
     * The stack for information storage.
     */
    private ArrayList<E> stack;

    /**
     * The capacity of the stack.
     */
    private int capacity;

    /**
     * Constructor for the StateStack class.
     *
     * @param capacity: The user's setting for capacity.
     */
    public StateStack(int capacity) {
        this.stack = new ArrayList<E>();
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    /**
     * set undo limit (capacity for this stack)
     * @param capacity
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Puts an item into the stack.
     *
     * @param item The item to be put into the stack.
     */
    public void put(E item) {
        if (stack.size() == capacity) {
            stack.remove(0);
            stack.add(item);
            Log.d("Push into Stack", "stackSize = " + ((Integer) stack.size()).toString());
        } else {
            stack.add(item);
        }
    }

    /**
     * Gets the item on the top of the stack.
     */
    public E get() {
        return stack.get(stack.size() - 1);
    }

    /**
     * Gets the item on the top of the stack.
     */
    public E pop() {
        return stack.remove(stack.size() - 1);
    }


    /**
     * Returns if the StateStack is Empty.
     */
    public boolean isEmpty() {
        return stack.size() == 0;
    }

    /**
     * Returns the size of the StateStack.
     */
    public int size() {
        return stack.size();
    }
}
