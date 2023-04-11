package com.example.piratesvszombiesclassicgames;

import android.graphics.Point;

import java.util.ArrayList;

public class computerFunctions {
    private String[][] board = new String[8][8];
    private String isClick = "";
    private Point isClickI = null;
    private boolean turn = false;
    private int wc = 12;
    private int bc = 12;
    private boolean haveToEat = false;
    private boolean isChainActivated=false;
    private int counter;
    private ArrayList<Integer> white=new ArrayList<>();
    private ArrayList<Integer> black=new ArrayList<>();
    private int ic;
    private int c;
    public boolean isOver= false;
    public int difference;

    public computerFunctions(String[][] board, boolean turn, boolean haveToEat, int wc, int bc, int counter, ArrayList<Integer> white, ArrayList<Integer> black,int ic, int c) {
        for (int i = 0; i < 7; i+=2){
            for (int j = 1; j < 8; j+=2){
                this.board[i][j]=board[i][j];
            }
        }
        for (int i = 1; i < 8; i+=2){
            for (int j = 0; j < 7; j+=2){
                this.board[i][j]=board[i][j];
            }
        }
        this.turn = turn;
        this.haveToEat=haveToEat;
        this.wc = wc;
        this.bc = bc;
        this.counter = counter;
        for(int i =0; i<white.size();i++)
            this.white.add(white.get(i));
        for(int i =0; i<black.size();i++)
            this.black.add(black.get(i));
        this.ic=ic;
        this.c=c;
    }

    public boolean canMove() {
        int i;
        int j;
        if(turn){
            i=white.get(ic)/10;
            j=white.get(ic)%10;
            if(haveToEat){
                if(whiteEat1(i,j)){
                    isClick=board[i][j];
                    return true;
                }
                else return false;
            }
            else if(whiteTurn(i,j))
                return true;
            else return false;
        }
        else {
            i=black.get(ic)/10;
            j=black.get(ic)%10;
            if(haveToEat){
                if(blackEat1(i,j)){
                    isClick=board[i][j];
                    return true;
                }
                else return false;
            }
            else {
                if(blackTurn(i,j))
                    return true;
                else return false;
            }
        }
    }

    public boolean whiteTurn(int i, int j){
        switch (c){
            case 1:
                if(!(j==7||i==0)){
                    if(northEast(i,j,"w","b","wk","bk"))
                        return true;
                }
                else return false;
                break;
            case 2:
                if(!(j==0||i==0)){
                    if(northWest(i,j,"w","b","wk","bk"))
                        return true;
                }
                else return false;
                break;
            case 3:
                if(!(i==7||j==0)){
                    if (southWest(i, j, "w", "b", "wk", "bk"))
                        return true;
                }
                else return false;
                break;
            case 4:
                if(!(i==7||j==7)){
                    if (southEast(i, j, "w", "b", "wk", "bk"))
                        return true;
                }
                else return false;
                break;
        }
        return false;
    }
    public boolean blackTurn(int i, int j){
        switch (c){
            case 1:
                if(!(j==7||i==0)){
                    if(northEast(i,j,"b","w","bk","wk"))
                        return true;
                }
                else return false;
                break;
            case 2:
                if(!(i==0||j==0)){
                    if(northWest(i,j,"b","w","bk","wk"))
                        return true;
                }
                else return false;
                break;
            case 3:
                if(!(i==7||j==0)){
                    if (southWest(i, j, "b","w","bk","wk"))
                        return true;
                }
                else return false;
                break;
            case 4:
                if(!(i==7||j==7)){
                    if (southEast(i, j, "b","w","bk","wk"))
                        return true;
                }
                else return false;
                break;
        }
        return false;
    }

    public boolean northWest(int i, int j, String a, String b, String ak, String bk){
        if(!(board[i-1][j-1]==a||board[i-1][j-1]==ak)){
            if(!((board[i-1][j-1]==b||board[i-1][j-1]==bk)&&(i-2<0||j-2<0))){
                if(!((board[i-1][j-1]==b||board[i-1][j-1]==bk)&&(board[i-2][j-2]==b||board[i-2][j-2]==bk||board[i-2][j-2]==a||board[i-2][j-2]==ak))){
                    if(board[i-1][j-1]==b||board[i-1][j-1]==bk){
                        board[i-2][j-2]="c";
                    }
                    else {
                        board[i-1][j-1]="c";
                    }
                    isClick = board[i][j];
                    isClickI = new Point(i,j);
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }
    public boolean northEast(int i, int j, String a, String b, String ak, String bk){
        if(!(board[i-1][j+1]==a||board[i-1][j+1]==ak)){
            if(!((board[i-1][j+1]==b||board[i-1][j+1]==bk)&&(i-2<0||j+2>7))){
                if(!((board[i-1][j+1]==b||board[i-1][j+1]==bk)&&(board[i-2][j+2]==b||board[i-2][j+2]==bk||board[i-2][j+2]==a||board[i-2][j+2]==ak))){
                    if(board[i-1][j+1]==b||board[i-1][j+1]==bk){
                        board[i-2][j+2]="c";
                    }
                    else {
                        board[i-1][j+1]="c";
                    }
                    isClick = board[i][j];
                    isClickI = new Point(i,j);
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }
    public boolean southWest(int i, int j, String a, String b, String ak, String bk){
        if(!(board[i+1][j-1]==a||board[i+1][j-1]==ak)){
            if(!((board[i+1][j-1]==b||board[i+1][j-1]==bk)&&(i+2>7||j-2<0))){
                if(!((board[i+1][j-1]==b||board[i+1][j-1]==bk)&&(board[i+2][j-2]==b||board[i+2][j-2]==bk||board[i+2][j-2]==a||board[i+2][j-2]==ak))){
                    if(board[i+1][j-1]==b||board[i+1][j-1]==bk){
                        board[i+2][j-2]="c";
                    }
                    else {
                        board[i+1][j-1]="c";
                    }
                    isClick = board[i][j];
                    isClickI = new Point(i,j);
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }
    public boolean southEast(int i, int j, String a, String b, String ak, String bk){
        if(!(board[i+1][j+1]==a||board[i+1][j+1]==ak)){
            if(!((board[i+1][j+1]==b||board[i+1][j+1]==bk)&&(i+2>7||j+2>7))){
                if(!((board[i+1][j+1]==b||board[i+1][j+1]==bk)&&(board[i+2][j+2]==b||board[i+2][j+2]==bk||board[i+2][j+2]==a||board[i+2][j+2]==ak))){
                    if(board[i+1][j+1]==b||board[i+1][j+1]==bk){
                        board[i+2][j+2]="c";
                    }
                    else {
                        board[i+1][j+1]="c";
                    }
                    isClick = board[i][j];
                    isClickI = new Point(i,j);
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public void move(){
        int i =-1;
        int j =-1;
        for (int a = 0; a < 7; a+=2){
            for (int b = 1; b < 8; b+=2){
                if(board[a][b]=="c"){
                    i=a;
                    j=b;
                }
            }
        }
        if(i==-1&&j==-1)
            for (int a = 1; a < 8; a+=2){
                for (int b = 0; b < 7; b+=2){
                    if(board[a][b]=="c"){
                        i=a;
                        j=b;
                    }
                }
            }
        if(i!=-1&&j!=-1){
        board[i][j]=isClick;
        if(turn){
            this.white.set(ic,i*10+j);
        }
        else{
            this.black.set(ic,i*10+j);
        }
        isClick="n";
        int ic = isClickI.x;
        int jc = isClickI.y;
        board[ic][jc] = "n";
        boolean isEaten = false;
        if (Math.abs(ic - i) == 2) {
            int imax = Math.max(ic, i);
            int jmax = Math.max(jc, j);
            switch (board[imax - 1][jmax - 1]) {
                case "b":
                    bc--;
                    int x=0;
                    for (int a=0;a<black.size();a++)
                        if(black.get(a)/10==imax - 1&&black.get(a)%10==jmax - 1)
                            x=a;
                    black.remove(x);
                    break;
                case "bk":
                    bc--;
                    int z=0;
                    for (int a=0;a<black.size();a++)
                        if(black.get(a)/10==imax - 1&&black.get(a)%10==jmax - 1)
                            z=a;
                    black.remove(z);
                    break;
                case "w":
                    wc--;
                    int y=0;
                    for (int a=0;a<white.size();a++)
                        if(white.get(a)/10==imax - 1&&white.get(a)%10==jmax - 1)
                            y=a;
                    white.remove(y);
                    break;
                case "wk":
                    wc--;
                    int w=0;
                    for (int a=0;a<white.size();a++)
                        if(white.get(a)/10==imax - 1&&white.get(a)%10==jmax - 1)
                            w=a;
                    white.remove(w);
                    break;
            }
            board[imax - 1][jmax - 1]="n";
            isEaten = true;
        }
        isClick = null;
        isClickI=null;
        cleanCheck();
        if(("wk"==board[i][j]||"bk"==board[i][j])&&!isEaten)
            counter++;
        else counter=0;
        if (turn&&i == 0&&board[i][j]!="wk") {
            board[i][j]="wk";
        }
        if (!turn&&i == 7&&board[i][j]!="bk") {
            board[i][j]="bk";
        }
        if(!isWin()) {
            cleanCheck();
            isClick = null;
            isClickI=null;
            isChainActivated = false;
            haveToEat = false;
            if (turn) {
                if (!isEaten) {
                    turn = false;
                    if (mustEatBlack()) {
                        cleanCheck();
                        haveToEat = true;
                        isClick = null;
                        isClickI=null;
                    }
                } else if (!isChain(i, j, "w", "b", "wk", "bk")) {
                    turn = false;
                    if (mustEatBlack()) {
                        cleanCheck();
                        haveToEat = true;
                        isClick = null;
                        isClickI=null;
                    }
                } else isChainActivated = true;
            } else {
                if (!isEaten) {
                    turn = true;
                    if (mustEatWhite()) {
                        cleanCheck();
                        haveToEat = true;
                        isClick = null;
                        isClickI=null;
                    }
                } else if (!isChain(i, j, "b", "w", "bk", "wk")) {
                    turn = true;
                    if (mustEatWhite()) {
                        cleanCheck();
                        haveToEat = true;
                        isClick = null;
                        isClickI=null;
                    }
                } else isChainActivated = true;
            }
        }
        else {
            isOver=true;
            cleanCheck();
            isClick = null;
            isClickI=null;
            isChainActivated = false;
        }
        }
    }

    public void cleanCheck(){
        for (int a = 0; a < 7; a+=2){
            for (int b = 1; b < 8; b+=2){
                if(board[a][b]=="c"){
                    board[a][b]="n";
                }
            }
        }
        for (int a = 1; a < 8; a+=2){
            for (int b = 0; b < 7; b+=2){
                if(board[a][b]=="c"){
                    board[a][b]="n";
                }
            }
        }
    }

    public boolean isChain(int i, int j, String a, String b, String ak, String bk){
        if(i==0&&j==7)
            return eatSouthWest(i, j, a, b, ak, bk);
        else if(i==7&&j==0)
            return  eatNorthEast(i, j, a, b, ak, bk);
        else if(i==0){
            boolean h= eatSouthWest(i, j, a, b, ak, bk);
            boolean w=eatSouthEast(i, j, a, b, ak, bk);
            return h||w;
        }
        else if(i==7){
            boolean h =eatNorthWest(i, j, a, b, ak, bk);
            boolean w=eatNorthEast(i, j, a, b, ak, bk);
            return h||w;

        }
        else if(j==0){
            boolean h=eatNorthEast(i, j, a, b, ak, bk);
            boolean w=eatSouthEast(i, j, a, b, ak, bk);
            return h||w;
        }
        else if(j==7){
            boolean h=eatNorthWest(i, j, a, b, ak, bk);
            boolean w=eatSouthWest(i, j, a, b, ak, bk);
            return h||w;
        }
        else {
            boolean h=eatSouthWest(i, j, a, b, ak, bk);
            boolean w=eatSouthEast(i, j, a, b, ak, bk);
            boolean q=eatNorthWest(i, j, a, b, ak, bk);
            boolean r=eatNorthEast(i, j, a, b, ak, bk);
            return h||w||q||r;
        }
    }

    public boolean eatNorthWest(int i, int j, String a, String b, String ak, String bk){
        /*String ak = a+"k";
        String bk = b+"k";*/
        if(!(board[i-1][j-1]==a||board[i-1][j-1]==ak)){
            if(!((board[i-1][j-1]==b||board[i-1][j-1]==bk)&&(i-2<0||j-2<0))){
                if(!((board[i-1][j-1]==b||board[i-1][j-1]==bk)&&(board[i-2][j-2]==b||board[i-2][j-2]==bk||board[i-2][j-2]==a||board[i-2][j-2]==ak))){
                    if(board[i-1][j-1]==b||board[i-1][j-1]==bk){
                        board[i-2][j-2]="c";
                        isClick = board[i][j];
                        isClickI = new Point(i,j);
                        return true;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
        return false;
    }
    public boolean eatNorthEast(int i, int j, String a, String b, String ak, String bk){
        /*String ak = a+"k";
        String bk = b+"k";*/
        if(!(board[i-1][j+1]==a||board[i-1][j+1]==ak)){
            if(!((board[i-1][j+1]==b||board[i-1][j+1]==bk)&&(i-2<0||j+2>7))){
                if(!((board[i-1][j+1]==b||board[i-1][j+1]==bk)&&(board[i-2][j+2]==b||board[i-2][j+2]==bk||board[i-2][j+2]==a||board[i-2][j+2]==ak))){
                    if(board[i-1][j+1]==b||board[i-1][j+1]==bk){
                        board[i-2][j+2]="c";
                        isClick = board[i][j];
                        isClickI = new Point(i,j);
                        return true;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
        return false;
    }
    public boolean eatSouthWest(int i, int j, String a, String b, String ak, String bk){
        /*String ak = a+"k";
        String bk = b+"k";*/
        if(!(board[i+1][j-1]==a||board[i+1][j-1]==ak)){
            if(!((board[i+1][j-1]==b||board[i+1][j-1]==bk)&&(i+2>7||j-2<0))){
                if(!((board[i+1][j-1]==b||board[i+1][j-1]==bk)&&(board[i+2][j-2]==b||board[i+2][j-2]==bk||board[i+2][j-2]==a||board[i+2][j-2]==ak))){
                    if(board[i+1][j-1]==b||board[i+1][j-1]==bk){
                        board[i+2][j-2]="c";
                        isClick = board[i][j];
                        isClickI = new Point(i,j);
                        return true;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
        return false;
    }
    public boolean eatSouthEast(int i, int j, String a, String b, String ak, String bk){
        /*String ak = a+"k";
        String bk = b+"k";*/
        if(!(board[i+1][j+1]==a||board[i+1][j+1]==ak)){
            if(!((board[i+1][j+1]==b||board[i+1][j+1]==bk)&&(i+2>7||j+2>7))){
                if(!((board[i+1][j+1]==b||board[i+1][j+1]==bk)&&(board[i+2][j+2]==b||board[i+2][j+2]==bk||board[i+2][j+2]==a||board[i+2][j+2]==ak))){
                    if(board[i+1][j+1]==b||board[i+1][j+1]==bk){
                        board[i+2][j+2]="c";
                        isClick = board[i][j];
                        isClickI = new Point(i,j);
                        return true;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public boolean mustEatWhite(){
        for (int i = 0; i < 7; i+=2){
            for (int j = 1; j < 8; j+=2){
                if (whiteEat(i,j))
                    return true;
            }
        }
        for (int i = 1; i < 8; i+=2){
            for (int j = 0; j < 7; j+=2){
                if (whiteEat(i,j))
                    return true;
            }
        }
        return false;
    }
    public boolean mustEatBlack(){
        for (int i = 0; i < 7; i+=2){
            for (int j = 1; j < 8; j+=2){
                if(blackEat(i,j))
                    return true;
            }
        }
        for (int i = 1; i < 8; i+=2){
            for (int j = 0; j < 7; j+=2){
                if(blackEat(i,j))
                    return true;
            }
        }
        return false;
    }

    public boolean whiteEat(int i ,int j){
        switch (board[i][j]){
            case "w":
                switch (j){
                    case 0:
                        if(eatNorthEast(i,j,"w","b","wk","bk"))
                            return true;
                        break;
                    case 7:
                        if(eatNorthWest(i,j,"w","b","wk","bk"))
                            return true;
                        break;
                    default:
                        boolean h =eatNorthEast(i,j,"w","b","wk","bk");
                        boolean w =eatNorthWest(i,j,"w","b","wk","bk");
                        return h||w;
                }
                break;
            case "wk":
                if(i==0&&j==7) {
                    if (eatSouthWest(i, j, "w", "b", "wk", "bk"))
                        return true;
                }
                else {if(i==7&&j==0) {
                    if (eatNorthEast(i, j, "w", "b", "wk", "bk"))
                        return true;
                }
                else if (i == 0) {
                    boolean h =eatSouthWest(i, j, "w", "b", "wk", "bk");
                    boolean w = eatSouthEast(i, j, "w", "b", "wk", "bk");
                    return h||w;
                } else if (i == 7) {
                    boolean h =eatNorthEast(i,j,"w","b","wk","bk");
                    boolean w =eatNorthWest(i,j,"w","b","wk","bk");
                    return h||w;
                } else if (j == 0) {
                    boolean h =eatNorthEast(i,j,"w","b","wk","bk");
                    boolean w = eatSouthEast(i, j, "w", "b", "wk", "bk");
                    return h||w;
                } else if (j == 7) {
                    boolean w =eatNorthWest(i,j,"w","b","wk","bk");
                    boolean h =eatSouthWest(i, j, "w", "b", "wk", "bk");
                    return h||w;
                } else {
                    boolean w =eatNorthWest(i,j,"w","b","wk","bk");
                    boolean h =eatSouthWest(i, j, "w", "b", "wk", "bk");
                    boolean h1 =eatNorthEast(i,j,"w","b","wk","bk");
                    boolean w1 = eatSouthEast(i, j, "w", "b", "wk", "bk");
                    return h||w||h1||w1;
                }
                }
                break;
        }
        return false;
    }
    public boolean blackEat(int i ,int j){
        switch (board[i][j]){
            case "b":
                switch (j){
                    case 0:
                        if(eatSouthEast(i,j,"b","w","bk","wk"))
                            return true;
                        break;
                    case 7:
                        if(eatSouthWest(i,j,"b","w","bk","wk"))
                            return true;
                        break;
                    default:
                        boolean h =eatSouthEast(i,j,"b","w","bk","wk");
                        boolean w =eatSouthWest(i,j,"b","w","bk","wk");
                        return h||w;
                }
                break;
            case "bk":
                if(i==0&&j==7) {
                    if (eatSouthWest(i, j, "b","w","bk","wk"))
                        return true;
                }
                else {if(i==7&&j==0) {
                    if (eatNorthEast(i, j, "b","w","bk","wk"))
                        return true;
                }
                else if (i == 0) {
                    boolean h =eatSouthWest(i, j, "b","w","bk","wk");
                    boolean w = eatSouthEast(i, j, "b","w","bk","wk");
                    return h||w;
                } else if (i == 7) {
                    boolean h =eatNorthEast(i,j,"b","w","bk","wk");
                    boolean w =eatNorthWest(i,j,"b","w","bk","wk");
                    return h||w;
                } else if (j == 0) {
                    boolean h =eatNorthEast(i,j,"b","w","bk","wk");
                    boolean w = eatSouthEast(i, j, "b","w","bk","wk");
                    return h||w;
                } else if (j == 7) {
                    boolean w =eatNorthWest(i,j,"b","w","bk","wk");
                    boolean h =eatSouthWest(i, j, "b","w","bk","wk");
                    return h||w;
                } else {
                    boolean w =eatNorthWest(i,j,"b","w","bk","wk");
                    boolean h =eatSouthWest(i, j, "b","w","bk","wk");
                    boolean h1 =eatNorthEast(i,j,"b","w","bk","wk");
                    boolean w1 = eatSouthEast(i, j, "b","w","bk","wk");
                    return h||w||h1||w1;
                }
                }
                break;
        }
        return false;
    }

    public boolean whiteEat1(int i ,int j){
        switch (c){
            case 1:
                if(!(i==0||j==7)){
                    if(eatNorthEast(i,j,"w","b","wk","bk"))
                        return true;
                }
                else return false;
                break;
            case 2:
                if(!(i==0||j==0)){
                    if(eatNorthWest(i,j,"w","b","wk","bk"))
                        return true;
                }
                else return false;
                break;
            case 3:
                if(!(i==7||j==0)){
                    if (eatSouthWest(i, j, "w", "b", "wk", "bk"))
                        return true;
                }
                else return false;
                break;
            case 4:
                if(!(i==7||j==7)){
                    if (eatSouthEast(i, j, "w", "b", "wk", "bk"))
                        return true;
                }
                else return false;
                break;
        }
        return false;
    }
    public boolean blackEat1(int i,int j){
        switch (c){
            case 1:
                if(!(i==0||j==7)){
                    if(eatNorthEast(i,j,"b","w","bk","wk"))
                        return true;
                }
                else return false;
                break;
            case 2:
                if(!(i==0||j==0)){
                    if(eatNorthWest(i,j,"b","w","bk","wk"))
                        return true;
                }
                else return false;
                break;
            case 3:
                if(!(i==7||j==0)){
                    if (eatSouthWest(i, j, "b","w","bk","wk"))
                        return true;
                }
                else return false;
                break;
            case 4:
                if(!(i==7||j==7)){
                    if (eatSouthEast(i, j, "b","w","bk","wk"))
                        return true;
                }
                else return false;
                break;
        }
        return false;
    }

    public boolean isWin(){
        if((wc==0)||!canMoveWhite())
        {
            return true;
        }
        else {
            if((bc==0)||!canMoveBlack())
            {
                return true;
            }
            else{
                if(counter==30)
                {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean canMoveWhite(){
        for (int i = 0; i < 7; i+=2){
            for (int j = 1; j < 8; j+=2){
                if (whiteMove(i,j))
                    return true;
            }
        }
        for (int i = 1; i < 8; i+=2){
            for (int j = 0; j < 7; j+=2){
                if (whiteMove(i,j))
                    return true;
            }
        }
        return false;
    }
    public boolean canMoveBlack(){
        for (int i = 0; i < 7; i+=2){
            for (int j = 1; j < 8; j+=2){
                if(blackMove(i,j))
                    return true;
            }
        }
        for (int i = 1; i < 8; i+=2){
            for (int j = 0; j < 7; j+=2){
                if(blackMove(i,j))
                    return true;
            }
        }
        return false;
    }

    public boolean whiteMove(int i ,int j){
        switch (board[i][j]){
            case "w":
                switch (j){
                    case 0:
                        if(northEast(i,j,"w","b","wk","bk"))
                            return true;
                        break;
                    case 7:
                        if(northWest(i,j,"w","b","wk","bk"))
                            return true;
                        break;
                    default:
                        if(northEast(i,j,"w","b","wk","bk")||eatNorthWest(i,j,"w","b","wk","bk")){
                            return true;
                        }
                        break;
                }
                break;
            case "wk":
                if(i==0&&j==7) {
                    if (southWest(i, j, "w", "b", "wk", "bk"))
                        return true;
                }
                else {
                    if (i == 7 && j == 0) {
                        if (northEast(i, j, "w", "b", "wk", "bk"))
                            return true;
                    }
                    else if (i == 0) {
                        if (southWest(i, j, "w", "b", "wk", "bk") || southEast(i, j, "w", "b", "wk", "bk"))
                            return true;
                    } else if (i == 7) {
                        if (northWest(i, j, "w", "b", "wk", "bk") || northEast(i, j, "w", "b", "wk", "bk"))
                            return true;
                    } else if (j == 0) {
                        if (northEast(i, j, "w", "b", "wk", "bk") || southEast(i, j, "w", "b", "wk", "bk"))
                            return true;
                    } else if (j == 7) {
                        if (northWest(i, j, "w", "b", "wk", "bk") || southWest(i, j, "w", "b", "wk", "bk"))
                            return true;
                    } else {
                        if (southWest(i, j, "w", "b", "wk", "bk") || southEast(i, j, "w", "b", "wk", "bk") || northWest(i, j, "w", "b", "wk", "bk") || northEast(i, j, "w", "b", "wk", "bk"))
                            return true;
                    }
                }
                break;
        }
        return false;
    }
    public boolean blackMove(int i,int j){
        switch (board[i][j]){
            case "b":
                switch (j){
                    case 0:
                        if(southEast(i,j,"b","w","bk","wk"))
                            return true;
                        break;
                    case 7:
                        if(southWest(i,j,"b","w","bk","wk"))
                            return true;
                        break;
                    default:
                        if(southEast(i,j,"b","w","bk","wk")||southWest(i,j,"b","w","bk","wk"))
                            return true;
                        break;
                }
                break;
            case "bk":
                if(i==0&&j==7) {
                    if (southWest(i, j, "b", "w", "bk", "wk"))
                        return true;
                }
                else {
                    if (i == 7 && j == 0) {
                        if (northEast(i, j, "b", "w", "bk", "wk"))
                            return true;
                    }
                    else if (i == 0) {
                        if (southWest(i, j, "b", "w", "bk", "wk") || southEast(i, j, "b", "w", "bk", "wk"))
                            return true;
                    } else if (i == 7) {
                        if (northWest(i, j, "b", "w", "bk", "wk") || northEast(i, j, "b", "w", "bk", "wk"))
                            return true;
                    } else if (j == 0) {
                        if (northEast(i, j, "b", "w", "bk", "wk") || southEast(i, j, "b", "w", "bk", "wk"))
                            return true;
                    } else if (j == 7) {
                        if (northWest(i, j, "b", "w", "bk", "wk") || southWest(i, j, "b", "w", "bk", "wk"))
                            return true;
                    } else {
                        if (southWest(i, j, "b", "w", "bk", "wk") || southEast(i, j, "b", "w", "bk", "wk") || northWest(i, j, "b", "w", "bk", "wk") || northEast(i, j, "b", "w", "bk", "wk"))
                            return true;
                    }
                }
                break;
        }
        return false;
    }

    public String[][] getBoard() {
        return board;
    }
    public void setBoard(String[][] board) {
        this.board = board;
    }
    public String getIsClick() {
        return isClick;
    }
    public void setIsClick(String isClick) {
        this.isClick = isClick;
    }
    public boolean isTurn() {
        return turn;
    }
    public void setTurn(boolean turn) {
        this.turn = turn;
    }
    public int getWc() {
        return wc;
    }
    public void setWc(int wc) {
        this.wc = wc;
    }
    public int getBc() {
        return bc;
    }
    public void setBc(int bc) {
        this.bc = bc;
    }
    public boolean isHaveToEat() {
        return haveToEat;
    }
    public void setHaveToEat(boolean haveToEat) {
        this.haveToEat = haveToEat;
    }
    public boolean isChainActivated() {
        return isChainActivated;
    }
    public void setChainActivated(boolean chainActivated) {
        isChainActivated = chainActivated;
    }
    public int getCounter() {
        return counter;
    }
    public void setCounter(int counter) {
        this.counter = counter;
    }

    public ArrayList<Integer> getWhite() {
        return white;
    }

    public void setWhite(ArrayList<Integer> white) {
        this.white = white;
    }

    public ArrayList<Integer> getBlack() {
        return black;
    }

    public void setBlack(ArrayList<Integer> black) {
        this.black = black;
    }

    public int getIc() {
        return ic;
    }
    public void setIc(int ic) {
        this.ic = ic;
    }
    public int getC() {
        return c;
    }
    public void setC(int c) {
        this.c = c;
    }
}
