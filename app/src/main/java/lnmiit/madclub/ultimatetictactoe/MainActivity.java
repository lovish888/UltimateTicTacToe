package dexter.com.ultimate_tictactoe;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    View view[];
    int set[][][];
    ImageView imageView[][][];
    public static final int NUM_IDS = 9;
    ArrayList<Integer> tieList = new ArrayList<>();

    // to detect last grid
    int lastGrid = -1;

    // Big Grid
    int win[][];

    //to store all the free indexes
    HashMap<Integer, ArrayList<Integer>> hashMap;

    //Not completed Indexes
    ArrayList<Integer> finalIndexes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        hashMap = new HashMap<>();
        win = new int[NUM_IDS / 3][NUM_IDS / 3];
        view = new View[NUM_IDS];
        set = new int[NUM_IDS][NUM_IDS / 3][NUM_IDS / 3];
        imageView = new ImageView[NUM_IDS][NUM_IDS / 3][NUM_IDS / 3];
        finalIndexes = new ArrayList<>();

        int[] viewIds = new int[]{R.id.a1, R.id.a2, R.id.a3, R.id.a4, R.id.a5, R.id.a6, R.id.a7, R.id.a8, R.id.a9};
        int[][] imageViewById = new int[][]{{R.id.box1, R.id.box2, R.id.box3}, {R.id.box4, R.id.box5, R.id.box6}, {R.id.box7, R.id.box8, R.id.box9}};

        ArrayList<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < NUM_IDS; i++) {
            indexes.add(i);
            finalIndexes.add(i);
        }
        for (int i = 0; i < NUM_IDS; i++) {
            hashMap.put(i, (ArrayList<Integer>) indexes.clone());
        }

        int tagId = 0;
        for (int i = 0; i < NUM_IDS; i++) {
            view[i] = findViewById(viewIds[i]);
            for (int j = 0; j < NUM_IDS / 3; j++) {
                for (int k = 0; k < NUM_IDS / 3; k++) {

                    imageView[i][j][k] = (ImageView) view[i].findViewById(imageViewById[j][k]);
                    imageView[i][j][k].setOnClickListener(this);
                    imageView[i][j][k].setTag(String.valueOf(tagId));
                    tagId++;
                }
            }
        }
        startGame();
    }

    private void startGame() {
        Random random = new Random();
        int randomChance = random.nextInt(2) + 1;
        // if 1 then computer chance else human
        if (randomChance == 1) {
            computerChance();
        } else {
        }
    }

    private void computerChance() {
        //System.out.println("HASHMAP : " + hashMap);
        //System.out.println("Computer chance");
        if (lastGrid == -1) {
            Random random = new Random();
            int gridId = random.nextInt(finalIndexes.size());
            //System.out.println("COMPUTER CHANCE : " + gridId);
            //System.out.println("FINAL INDEXES : " + finalIndexes);
            ArrayList<Integer> smallGrid = hashMap.get(finalIndexes.get(gridId));
            //System.out.println("SMALLGRID : " + smallGrid);
            int randomIndex = random.nextInt(smallGrid.size());
            //System.out.println("RANDOM INDEX : " + randomIndex);
            randomIndex = smallGrid.get(randomIndex);
            markSet(finalIndexes.get(gridId), randomIndex, 1);
        } else {
            Random random = new Random();
            ArrayList<Integer> smallGrid = hashMap.get(lastGrid);
            if (smallGrid == null)
                return;
            int randomIndex = random.nextInt(smallGrid.size());
            randomIndex = smallGrid.get(randomIndex);
            markSet(lastGrid, randomIndex, 1);
        }
    }

    // if player 1 then computer else human
    private void markSet(int gridId, int index, int player) {
//        printState(player);
        int i = index / 3;
        int j = index % 3;

        //int tagId = NUM_IDS * gridId + index;

        if (set[gridId][i][j] == 0) {
            if (hashMap.containsKey(index)) {
                lastGrid = index;
            } else {
                lastGrid = -1;
            }
//            System.out.println("LAST GRID : " + lastGrid);
//            System.out.println("GRID ID : " + gridId);
            set[gridId][i][j] = player;
            if (player == 1) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    imageView[gridId][i][j].setImageDrawable(getResources().getDrawable(R.drawable.cross, getTheme()));
                } else {
                    imageView[gridId][i][j].setImageDrawable(getResources().getDrawable(R.drawable.cross));
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    imageView[gridId][i][j].setImageDrawable(getResources().getDrawable(R.drawable.zero, getTheme()));
                } else {
                    imageView[gridId][i][j].setImageDrawable(getResources().getDrawable(R.drawable.zero));
                }
            }

            ArrayList<Integer> temp = hashMap.get(gridId);
//            System.out.println("ARRAYLIST TEMP : " + temp);
            temp.remove((Integer) index);
            boolean checkWin = winOrLose(set[gridId]);

            if (checkWin) {
                lastGrid = -1;
                win[gridId / 3][gridId % 3] = player;
                for (int k = 0; k < temp.size(); k++) {
                    int tempIndex = temp.get(k);
                    i = tempIndex / 3;
                    j = tempIndex % 3;
                    set[gridId][i][j] = -1;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        imageView[gridId][i][j].setImageDrawable(getDrawable(R.drawable.ic_remove_black_24dp));
                    }
                }
                if (player == 1) {
                    Log.e("WON ", "Computer Wins : " + gridId);
                    view[gridId].setBackgroundColor(getResources().getColor(R.color.colorComputer));
                } else {
                    Log.e("WON ", "User Wins : " + gridId);
                    view[gridId].setBackgroundColor(getResources().getColor(R.color.colorUser));
                }

                hashMap.remove(gridId);
                for (int z = 0; z < finalIndexes.size(); z++) {
                    if (finalIndexes.get(z) == gridId) {
                        finalIndexes.remove(z);
                        break;
                    }
                }
            } else {
                boolean draw = checkTie(set[gridId]);
                if (draw) {
                    lastGrid = -1;
                    win[gridId / 3][gridId % 3] = -1;
                    tieList.add(gridId);
                }
                if (temp.size() == 0) {
                    hashMap.remove(gridId);
                    finalIndexes.remove((Integer)gridId);
                } else {
                    hashMap.put(gridId, temp);
                }
            }

            //ActualWin
            boolean winFinal = winOrLose(win);
            if (winFinal) {
                if (player == 1) {
                    Toast.makeText(MainActivity.this, "Computer win", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "You win", Toast.LENGTH_SHORT).show();
                }
                Restart();
            } else if (checkTie(win)) {
                Toast.makeText(MainActivity.this, "Tie", Toast.LENGTH_SHORT).show();
                Restart();
            }
        }
        if (player == 1) {
            if (lastGrid != -1) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    disableColor();
                    view[lastGrid].setBackgroundColor(getResources().getColor(R.color.colorTrans));
                }
            } else {
                disableColor();
            }
        } else {
        }
        for(int m=0;m<tieList.size();m++){
            view[tieList.get(m)].setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        }

//        printState(player);
    }

    private boolean winOrLose(int check[][]) {
        if (checkRow(check)) {
            return true;
        }
        if (checkColumn(check)) {
            return true;
        }
        if (checkDiagonal(check)) {
            return true;
        }
        return false;
    }

    private boolean checkRow(int check[][]) {
        if (check[0][0] == check[0][1] && check[0][0] == check[0][2] && check[0][0] != 0) {
            return true;
        }
        if (check[1][0] == check[1][1] && check[1][0] == check[1][2] && check[1][0] != 0) {
            return true;
        }
        if (check[2][0] == check[2][1] && check[2][0] == check[2][2] && check[2][0] != 0) {
            return true;
        }
        return false;
    }

    private boolean checkColumn(int check[][]) {
        if (check[0][0] == check[1][0] && check[0][0] == check[2][0] && check[0][0] != 0) {
            return true;
        }
        if (check[0][1] == check[1][1] && check[0][1] == check[2][1] && check[0][1] != 0) {
            return true;
        }
        if (check[0][2] == check[1][2] && check[0][2] == check[2][2] && check[0][2] != 0) {
            return true;
        }
        return false;
    }

    private boolean checkDiagonal(int check[][]) {
        if (check[0][0] == check[1][1] && check[0][0] == check[2][2] && check[0][0] != 0) {
            return true;
        }
        if (check[0][2] == check[1][1] && check[0][2] == check[2][0] && check[0][2] != 0) {
            return true;
        }
        return false;
    }

    private boolean checkTie(int check[][]) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (check[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        String tagId = (String) view.getTag();
        int gridId = Integer.parseInt(tagId) / NUM_IDS;
        Log.e("Clicked ", String.valueOf(tagId));
        int index = Integer.parseInt(tagId) % NUM_IDS;
        if (lastGrid == -1) {
            markSet(gridId, index, 2);
            computerChance();
        } else if (lastGrid == gridId) {
            markSet(lastGrid, index, 2);
            computerChance();
        }
    }

    public void disableColor() {
        for (int i = 0; i < finalIndexes.size(); i++) {
            view[finalIndexes.get(i)].setBackgroundColor(getResources().getColor(android.R.color.white));
        }
    }

//    private void printState(int player){
//        Log.e("State", String.valueOf(player));
//        System.out.println("HASHMAP : " + hashMap);
//        System.out.println("FINALARRAYLIST : " + finalIndexes);
//        Log.e("State","ENDED");
//
//    }

    private void Restart() {
        hashMap = new HashMap<>();
        win = new int[NUM_IDS / 3][NUM_IDS / 3];
        view = new View[NUM_IDS];
        set = new int[NUM_IDS][NUM_IDS / 3][NUM_IDS / 3];
        imageView = new ImageView[NUM_IDS][NUM_IDS / 3][NUM_IDS / 3];
        finalIndexes = new ArrayList<>();

        int[] viewIds = new int[]{R.id.a1, R.id.a2, R.id.a3, R.id.a4, R.id.a5, R.id.a6, R.id.a7, R.id.a8, R.id.a9};
        int[][] imageViewById = new int[][]{{R.id.box1, R.id.box2, R.id.box3}, {R.id.box4, R.id.box5, R.id.box6}, {R.id.box7, R.id.box8, R.id.box9}};

        ArrayList<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < NUM_IDS; i++) {
            indexes.add(i);
            finalIndexes.add(i);
        }
        for (int i = 0; i < NUM_IDS; i++) {
            hashMap.put(i, (ArrayList<Integer>) indexes.clone());
        }

        int tagId = 0;
        for (int i = 0; i < NUM_IDS; i++) {
            view[i] = findViewById(viewIds[i]);
            view[i].setBackgroundColor(getResources().getColor(android.R.color.white));
            for (int j = 0; j < NUM_IDS / 3; j++) {
                for (int k = 0; k < NUM_IDS / 3; k++) {

                    imageView[i][j][k] = (ImageView) view[i].findViewById(imageViewById[j][k]);
                    imageView[i][j][k].setOnClickListener(this);
                    imageView[i][j][k].setTag(String.valueOf(tagId));
                    imageView[i][j][k].setImageDrawable(null);
                    tagId++;
                }
            }
        }
        tieList = new ArrayList<>();
        startGame();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_rules) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage(Html.fromHtml("<b>Basic Rules</b><br><br>" +
                    "1. In each turn, you mark one of the small squares.<br><br>" +
                    "2. But the trick is, you don’t get to pick which of the nine boards to play on. That’s determined by your opponent’s previous move. Whichever square he picks on a particular board, that’s the board you must play in next turn. (And whichever square you pick will determine which board he plays on next). For example if you play on the 5th square of 2nd box, then you opponent can only play on 5th box and the same goes on.<br><br>" +
                    "3. When you win three squares in a row, column or diagonal, you’ve won that box and when you win three boxes in a row, column or diagonal, you’ve won that Game.\n" +
                    "<br><br><br>" +
                    "<b>More Clarification</b><br><br>" +
                    "1. What if my opponent sends me to a board that’s already been won? In that case – you get to play anywhere you like, on any of the other boards. (This means you should avoid sending your opponent to an already-won board)<br><br>" +
                    "2. If one of the small boards results in a tie, the board counts for neither X nor O.<br><br>"));
            builder.setTitle("Rules");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        return true;
    }
}
