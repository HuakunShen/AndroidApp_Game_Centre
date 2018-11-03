package csc207project.gamecenter.Data;

import java.util.ArrayList;

public class StateStack<E> {
    private ArrayList<E> stack;


    public StateStack(){
        this.stack = new ArrayList<E>();

    }

    public void push(E item){
        stack.add(item);
    }

    public boolean isEmpty(){
        return stack.size() == 0;
    }

    public E popLast(){
        E item = stack.get(stack.size()-1);
        stack.remove(stack.size() - 1);
        return item;
    }

    public void popFirst(){
        this.stack.remove(0);
    }

    public int size(){
        return stack.size();
    }
}
