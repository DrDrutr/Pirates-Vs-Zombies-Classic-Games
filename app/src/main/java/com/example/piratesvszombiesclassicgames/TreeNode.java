package com.example.piratesvszombiesclassicgames;

import java.util.ArrayList;

public class TreeNode<T> {
    private T board;
    private ArrayList<TreeNode<T>> sons;

    public TreeNode(T board) {
        this.board = board;
        sons = new ArrayList<>();
    }

    public T getBoard() {
        return board;
    }

    public void setBoard(T board) {
        this.board = board;
    }

    public ArrayList<TreeNode<T>> getSons() {
        return sons;
    }

    public void setSons(ArrayList<TreeNode<T>> sons) {
        this.sons = sons;
    }
}
