/**
 A class designed to play checkers, when a move is available to clic
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CheckersGame{
    //variables
    private CheckersButton[][] checkerboard;
    private boolean dejaVu;
    private CheckersButton jumped, selected;
    private boolean jumpUL, jumpUR, jumpLR, jumpLL, up, right, p1Turn, firstClick;
    ImageIcon black = new ImageIcon("black.jpg");
    ImageIcon red = new ImageIcon("red.jpg");
    public static void main(String[] args){
        GameStart();
    }


    public static void GameStart() {

        final JFrame startFrame = new JFrame("Checker Game");
        startFrame.setVisible(true);
        GridBagLayout fl=new GridBagLayout();
        startFrame.setSize(400, 400);
        JButton start=new JButton("START");
        JButton player1=new JButton("Player 1 (RED)");
        player1.setEnabled(false);
        JButton player2=new JButton("Player 2 (BLACK)");
        player2.setEnabled(false);
        Container pane1 = startFrame.getContentPane();
        pane1.setLayout(fl);
        pane1.add(start);
        pane1.add(player1);
        pane1.add(player2);

        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        start.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    new CheckersGame();
                    startFrame.setVisible(false);
                    startFrame.dispose();
                }
            });
        //}
    }

    public CheckersGame(){
        jumped = new CheckersButton();
        selected = new CheckersButton();
        dejaVu = false;
        jumpUL = false;
        jumpUR = false;
        jumpLR = false;
        jumpLL = false;
        up = false;
        right = false;
        checkerboard = new CheckersButton[8][8];
        firstClick=true;
        p1Turn=true;

        //build 8x8 array
        boolean crosspattern = true;
        for(int i=0;i<8;i++){
            crosspattern = !crosspattern;
            for(int o=0;o<8;o++){

                if((crosspattern)&&(i<=2)){
                    CheckersButton temp = new CheckersButton(2, i, o);
                    temp.addActionListener(temp);
                    temp.disableButton();
                    checkerboard[i][o] = temp;
                }else if((crosspattern)&&(i>=5)){
                    CheckersButton temp = new CheckersButton(1, i, o);
                    temp.addActionListener(temp);
                    //temp.disableButton();
                    checkerboard[i][o] = temp;
                }else{
                    CheckersButton temp = new CheckersButton(0, i, o);
                    temp.addActionListener(temp);
                    temp.disableButton();
                    checkerboard[i][o] = temp;
//                    checkerboard[o][i] = new CheckersButton(0, i, o);
                }
                crosspattern = !crosspattern;
            }
        }
        //build GUI in here
        JFrame checkerBoard=new JFrame();
        Container pane= checkerBoard.getContentPane();
        checkerBoard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        checkerBoard.setVisible(true);
        checkerBoard.setSize(500,500);
        checkerBoard.setTitle("CheckerBoard");
        pane.setLayout(new GridLayout(8, 8));
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                pane.add(checkerboard[i][j]);
            }
        }
    }

    //listener for buttons, checks if the piece is valid then highlights other valid locations
    public void buttonClick(int x, int y){
        if(firstClick){
            disableAllButtons();
            checkerboard[x][y].enableButton();
            checkUpperLeft(x,y,checkerboard[x][y].getPlayer());
            checkUpperRight(x,y,checkerboard[x][y].getPlayer());
            checkLowerLeft(x,y,checkerboard[x][y].getPlayer());
            checkLowerRight(x,y,checkerboard[x][y].getPlayer());
            selected.setxValue(x);
            selected.setyValue(y);
            firstClick=false;
        }
        else if(!firstClick){
            if((x==selected.getxValue())||(y==selected.getyValue())){
                disableAllButtons();
                enablePlayersButtons(checkerboard[x][y].getPlayer());
                return;
            }
            else if((x>selected.getxValue())&&(y>selected.getyValue())){
                right=true;
                up=false;
            }
            else if((x<selected.getxValue())&&(y>selected.getyValue())){
                right=true;
                up=true;
            }
            else if((x>selected.getxValue())&&(y<selected.getyValue())){
                right=false;
                up=false;
            }
            else if((x<selected.getxValue())&&(y<selected.getyValue())){
                right=false;
                up=true;
            }
            System.out.println("up: " + up + " right: " + right);
            setAdjacentPiece(x, y);
            p1Turn = !p1Turn;
            disableAllButtons();
            if(p1Turn==true){
                enablePlayersButtons(1);
            }else{
                enablePlayersButtons(2);
            }
            firstClick=true;
        }
    }

    //disables all buttons so specific ones can be enabled solely
    public void disableAllButtons(){
        for(int i = 0;i<8;i++){
            for(int o=0;o<8;o++){
                checkerboard[i][o].disableButton();
            }
        }
    }

    //enables an entire player's buttons, for when they start their turn
    public void enablePlayersButtons(int p){
        for(int i = 0;i<8;i++){
            for(int o=0;o<8;o++){
                if(checkerboard[i][o].getPlayer()==p){
                    checkerboard[i][o].enableButton();
                }
            }
        }
    }

    //check for possible move to piece's upper left
    public void checkUpperLeft(int x, int y, int player){
        if ((x==0)||(y==0)){
            return;
        }
        else if((checkerboard[x-1][y-1].getPlayer() ==0)&&(dejaVu)){
            checkerboard[x-1][y-1].enableButton();
            jumpUL = true;
            System.out.println("In Upper Left");
            dejaVu = false;
        }
        else if(checkerboard[x-1][y-1].getPlayer() == 0){
            checkerboard[x-1][y-1].enableButton();
            return;
        }
        else if((checkerboard[x-1][y-1].getPlayer() == player)&&(!dejaVu)){
            return;
        }
        else if((checkerboard[x-1][y-1].getPlayer() != player)&&(!dejaVu)){
            dejaVu=true;
            checkUpperLeft(x - 1, y - 1, player);
        }
        dejaVu=false;
    }

    //check for possible move to piece's upper right
    public void checkUpperRight(int x, int y, int player){
        if ((x==7)||(y==0)){
            return;
        }
        else if((checkerboard[x+1][y-1].getPlayer() ==0)&&(dejaVu)){
            checkerboard[x+1][y-1].enableButton();
            jumpUR = true;
            System.out.println("In Upper Right");
            dejaVu = false;
        }
        else if(checkerboard[x+1][y-1].getPlayer() == 0){
            checkerboard[x+1][y-1].enableButton();
            return;
        }
        else if((checkerboard[x+1][y-1].getPlayer() == player)&&(!dejaVu)){
            return;
        }
        else if((checkerboard[x+1][y-1].getPlayer() != player)&&(!dejaVu)){
            dejaVu=true;
            checkUpperRight(x + 1, y - 1, player);
        }
        dejaVu = false;
    }

    //check for possible move to piece's bottom left
    public void checkLowerLeft(int x, int y, int player){
        if ((x==0)||(y==7)){
            return;
        }
        else if((checkerboard[x-1][y+1].getPlayer() ==0)&&(dejaVu)){
            checkerboard[x-1][y+1].enableButton();
            jumpLL = true;
            System.out.println("In Lower Left");
            dejaVu = false;
        }
        else if(checkerboard[x-1][y+1].getPlayer() == 0){
            checkerboard[x-1][y+1].enableButton();
            return;
        }
        else if((checkerboard[x-1][y+1].getPlayer() == player)&&(!dejaVu)){
            return;
        }
        else if ((checkerboard[x - 1][y+1].getPlayer() != player)&&(!dejaVu)){
            dejaVu=true;
            checkLowerLeft(x - 1, y + 1, player);
        }
        dejaVu=false;
    }

    //check for possible move to piece's bottom right
    public void checkLowerRight(int x, int y, int player){
        if ((x==7)||(y==7)){
            return;
        }
        else if((checkerboard[x+1][y+1].getPlayer() ==0)&&(dejaVu)){
            checkerboard[x+1][y+1].enableButton();
            jumpLR = true;
            System.out.println("In Lower Right");
            dejaVu = false;
        }
        else if(checkerboard[x+1][y+1].getPlayer() == 0){
            checkerboard[x+1][y+1].enableButton();
            return;
        }
        else if((checkerboard[x+1][y+1].getPlayer() == player)&&(!dejaVu)){
            return;
        }
        else if ((checkerboard[x+1][y+1].getPlayer() != player)&&(!dejaVu)){
            dejaVu=true;
            checkLowerRight(x + 1, y + 1, player);
        }
        dejaVu = false;
    }

    //check for win after move
    public void checkWin(){
        int p1=0;
        int p2=0;
        for(int i=0;i<8;i++){
            for(int o=0;o<8;o++){
                if(checkerboard[i][o].getPlayer()==1){
                    p1++;
                }
                else if(checkerboard[i][o].getPlayer()==2){
                    p2++;
                }
            }
        }
//        int winner = 0;
//        winner = Math.max(p1,p2);
//        if((p1==0)||(p2==0)){
//            JOptionPane.showMessageDialog(null,"Congratulations! Player "+winner+" has won.");
//        }
        if (p1>p2) {
            JOptionPane.showMessageDialog(null,"Congratulations! Player "+p1+" has won.");
        } else if (p1<p2) {
            JOptionPane.showMessageDialog(null,"Congratulations! Player "+p2+" has won.");
        } else {
            if (p1>p2) {
                JOptionPane.showMessageDialog(null,"Game tied.");
            }
        }
    }
    //move a piece to adjacent square
    public void setAdjacentPiece(int x, int y){
//        System.out.println("x: " + x + " y: " + y);
//        System.out.println("UL: " + jumpUL + " UR: " + jumpUR + " LL: " + jumpLL + " LR: " + jumpLR);
        if(jumpUL || jumpUR || jumpLL || jumpLR){
            jumpPiece(x,y);
            jumpUL = jumpUR = jumpLL = jumpLR = false;
            checkWin();
            return;
        } else if((up==true)&&(right==true)){
            checkerboard[x][y].setPlayer(checkerboard[x + 1][y - 1].getPlayer());
            checkerboard[x + 1][y - 1].setPlayer(0);
        } else if((up==false)&&(right==true)){
            checkerboard[x][y].setPlayer(checkerboard[x - 1][y - 1].getPlayer());
            checkerboard[x-1][y-1].setPlayer(0);
        }
        else if((up==true)&&(right==false)){
            checkerboard[x][y].setPlayer(checkerboard[x+1][y+1].getPlayer());
            checkerboard[x+1][y+1].setPlayer(0);
        }
        else if((up==false)&&(right==false)){
            checkerboard[x][y].setPlayer(checkerboard[x-1][y+1].getPlayer());
            checkerboard[x-1][y+1].setPlayer(0);
        }
        jumpUL = jumpUR = jumpLL = jumpLR = false;
    }

    //move a piece to far square and jump a piece
    public void jumpPiece(int x, int y){
        System.out.println("In jump: " + "x: " + x + " y: " + y);
        if((up==true)&&(right==true)){
            checkerboard[x][y].setPlayer(checkerboard[x + 2][y - 2].getPlayer());
            checkerboard[x+2][y-2].setPlayer(0);
            removePiece(x + 1, y - 1);
        }
        else if((up==false)&&(right==true)){
            checkerboard[x][y].setPlayer(checkerboard[x-2][y-2].getPlayer());
            checkerboard[x-2][y-2].setPlayer(0);
            removePiece(x-1,y-1);

        }
        else if((up==true)&&(right==false)){
            checkerboard[x][y].setPlayer(checkerboard[x+2][y+2].getPlayer());
            checkerboard[x+2][y+2].setPlayer(0);
            removePiece(x+1,y+1);

        }
        else if((up==false)&&(right==false)){
            checkerboard[x][y].setPlayer(checkerboard[x-2][y+2].getPlayer());
            checkerboard[x-2][y+2].setPlayer(0);
            removePiece(x-1,y+1);

        }
        checkWin();
    }

    //remove a jumped piece
    public void removePiece(int x, int y){
        checkerboard[x][y].setPlayer(0);
    }

    class CheckersButton extends JButton implements ActionListener{
        private boolean selected;
        private int player, xValue, yValue;
//        JButton button;
        public CheckersButton(int _player, int _xValue, int _yValue){
            super(""+_player);
            player = _player;
            xValue = _xValue;
            yValue = _yValue;
            if (player == 1)
                this.setIcon(red);
            else if (player == 2)
                this.setIcon(black);
            //button = new JButton(""+player);
//            if you want black buttons
//            if(player==0) {
//                this.setIcon(black);
//            }
        }
        public CheckersButton(){
            super(""+0);
            player = 0;
            xValue = 0;
            yValue = 0;
        }
        public void disableButton(){
            this.setEnabled(false);
        }
        public void enableButton(){
            this.setEnabled(true);
        }
        public int getxValue(){
            return xValue;
        }
        public int getyValue(){
            return yValue;
        }
        public void actionPerformed(ActionEvent ae){
            System.out.println(xValue + " " + yValue);
            buttonClick(xValue,yValue);
        }
        public int getPlayer(){
            return player;
        }
        public void setPlayer(int _player){
            player=_player;
            //this.setText(""+player);
            if (player == 1)
                this.setIcon(red);
            else if (player == 2)
                this.setIcon(black);
            else
                this.setIcon(null);
        }
        public void setxValue(int x){
            xValue=x;
        }
        public void setyValue(int y){
            yValue=y;
        }
    }

}