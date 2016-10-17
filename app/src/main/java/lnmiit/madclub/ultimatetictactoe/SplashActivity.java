package dexter.com.ultimate_tictactoe;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.View;
import android.widget.Button;

public class SplashActivity extends Activity {
    Button rulesButton, playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        rulesButton = (Button) findViewById(R.id.rules_button);
        playButton = (Button) findViewById(R.id.play_button);

        rulesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
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
                        startActivity(new Intent(SplashActivity.this,MainActivity.class));
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
            }
        });
    }
}
