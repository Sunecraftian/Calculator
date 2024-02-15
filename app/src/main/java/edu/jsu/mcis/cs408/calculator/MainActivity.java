package edu.jsu.mcis.cs408.calculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import edu.jsu.mcis.cs408.calculator.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private ConstraintLayout layout;
    private ConstraintSet set;
    private LayoutParams params;

    private final int BTN_WIDTH = 5;
    private final int BTN_HEIGHT = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);

        initLayout();
    }

    private void initLayout() {
        layout = binding.layout;
        set = new ConstraintSet();

        int[] btnIDs = new int[BTN_WIDTH * BTN_HEIGHT];
        String[] btnTags = getResources().getStringArray(R.array.btnTags);
        String[] btnText = getResources().getStringArray(R.array.btnText);

        int[][] horizontals = new int[BTN_HEIGHT][BTN_WIDTH];
        int[][] verticals = new int[BTN_WIDTH][BTN_HEIGHT];

        // Create Display TextView
        TextView display = new TextView(this);
        display.setId(View.generateViewId());
        display.setTag("display");
        display.setGravity(Gravity.END);
        display.setText("0");
        display.setTextSize(48);
//        display.setBackgroundColor(Color.CYAN);

        layout.addView(display);

        // Create Buttons
        for (int i = 0; i < (BTN_WIDTH * BTN_HEIGHT); i++) {
            int id = View.generateViewId();
            int row = i / BTN_WIDTH;
            int col = i % BTN_WIDTH;
            btnIDs[i] = id;
            Button button = new Button(this);
            button.setId(id);
            button.setTag(btnTags[i]);
            button.setText(btnText[i]);
            horizontals[row][col] = id;
            verticals[col][row] = id;
            layout.addView(button);
        }


        // Create ConstraintSet from Layout
        set.clone(layout);

        // Constrain Display to North, East, and West Guides
        set.connect(display.getId(), ConstraintSet.LEFT, binding.guideWest.getId(), ConstraintSet.LEFT, 0);
        set.connect(display.getId(), ConstraintSet.RIGHT, binding.guideEast.getId(), ConstraintSet.RIGHT, 0);
        set.connect(display.getId(), ConstraintSet.TOP, binding.guideNorth.getId(), ConstraintSet.TOP, 0);

        // Constrain Buttons with Horizontal Chains
        for (int i = 0; i < BTN_HEIGHT; i++) {
            set.createHorizontalChain(binding.guideWest.getId(), ConstraintSet.RIGHT, binding.guideEast.getId(), ConstraintSet.LEFT, horizontals[i], null, ConstraintSet.CHAIN_SPREAD);
        }

        for (int i = 0; i < BTN_WIDTH; i++) {
            set.createVerticalChain(display.getId(), ConstraintSet.BOTTOM, binding.guideSouth.getId(), ConstraintSet.TOP, verticals[i], null, ConstraintSet.CHAIN_SPREAD);
        }

        set.applyTo(layout);

        for (int btnID : btnIDs) {
            Button button = layout.findViewById(btnID);
            params = button.getLayoutParams();
            params.width = 0;
            params.height = 0;
        }

        params = display.getLayoutParams();
        params.width = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT;
        params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        display.setLayoutParams(params);

    }
}