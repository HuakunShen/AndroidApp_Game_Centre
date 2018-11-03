package csc207project.gamecenter.Data;

import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_LONG;

public class StateStack<E> {
    private ArrayList<E> stack;


    public StateStack(){
        this.stack = new ArrayList<E>();

    }

    public void push(E item){
        Log.d("StateStack push check: ", "push item board");
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
