package com.example.piratesvszombiesclassicgames;

public class MancalaAI {
    private int[][] board;
    private boolean turn;
    private int i;
    private int j;
    public int difference;
    public boolean isOver = false;

    public MancalaAI(int[][] board, boolean turn, int i) {
        this.board= new int[7][2];
        for (int k = 0; k < 7; k++) {
            for (int j = 0; j < 2; j++) {
                this.board[k][j] = board[k][j];
            }
        }
        this.turn = turn;
        this.i=i;
        if(turn)
            this.j=1;
        else this.j=0;
    }

    public void clicked(){
        int x = board[i][j];
        if(turn&&j==1&&x!=0){
            board[i][j]=0;
            int k =i-1;
            boolean addTurn = false;
            while(x!=0) {
                x = wside(k, x);
                if (x != 0) {
                    rising(0, 1,1);
                    x--;
                    if (x != 0) {
                        x = bside(1, x);
                        k=6;
                    }
                    else addTurn = true;
                }
            }
            isWinner();
            if(!addTurn)
                turn = false;
        }
        else if(!turn&&j==0&&x!=0){
            board[i][j]=0;
            int k =i+1;
            boolean addTurn = false;
            while(x!=0) {
                x = bside(k, x);
                if (x != 0) {
                    rising(0, 0,1);
                    x--;
                    if (x != 0) {
                        x = wside(6, x);
                        k=1;
                    }
                    else addTurn = true;
                }
            }
            isWinner();
            if(!addTurn)
                turn = true;
        }
        difference=board[0][0]-36;
    }

    public void rising(int i, int j,int sum){
        int x = board[i][j];
        x+=sum;
        if(x<10){
            board[i][j]=x;
        }
        else {
            board[i][j]=x;
        }
    }

    public int wside(int i, int x){
        for (; i >0 && x>0 ; i--) {
            rising(i,1,1);
            x--;
        }
        if(turn&&x==0){
            i++;
            int a = board[i][1];
            if(a==1) {
                int b = board[i][0];
                board[i][0]=0;
                board[i][1]=0;
                rising(0, 1, a + b);
            }
        }
        return x;
    }

    public int bside(int i, int x){
        for (; i <7 && x>0 ; i++) {
            rising(i,0,1);
            x--;
        }
        if(!turn&&x==0){
            i--;
            int b = board[i][0];
            if(b==1) {
                int a = board[i][1];
                board[i][0]=0;
                board[i][1]=0;
                rising(0, 0, a + b);
            }
        }
        return x;
    }

    public void isWinner(){
        if(checkEmpty(0)){
            clearWinner(0);
            winAlert();
        }
        else if (checkEmpty(1)){
            clearWinner(1);
            winAlert();
        }
    }

    public boolean checkEmpty(int j){
        for (int i = 1; i < 7; i++) {
            if(board[i][j]!=0)
                return false;
        }
        return true;
    }

    public void clearWinner(int j){
        int sum =0;
        if(j==1){
            for (int i = 1; i < 7; i++) {
                sum+=board[i][0];
            }
        }
        else{
            for (int i = 1; i < 7; i++) {
                sum+=board[i][1];
            }
        }
        if(turn)
            rising(0,1,sum);
        else rising(0,0,sum);
    }

    public void winAlert(){
        isOver = true;
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public boolean isTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }
}
