package euismod.munchkincounter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.Random;

public class FullscreenActivity extends Activity implements NumberPicker.OnValueChangeListener {

    private NumberPicker playerLevel, playerGear, monsterLevel, monsterModifiers, playerModifiers/*, playerGender*/;
    private CheckBox victoryCheckbox;
    int stepSize = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        playerLevel                 = (NumberPicker) findViewById(R.id.playerLevel);
        playerGear                  = (NumberPicker) findViewById(R.id.playerGear);
        monsterLevel                = (NumberPicker) findViewById(R.id.monsterLevel);
        monsterModifiers            = (NumberPicker) findViewById(R.id.monsterModifiers);
        playerModifiers             = (NumberPicker) findViewById(R.id.playerModifiers);
        NumberPicker playerGender   = (NumberPicker) findViewById(R.id.genderPicker);

        String[] genders = {"M", "F"};
        playerGender.setDisplayedValues(genders);
        setMinMax(playerGender, 0, 1);

        setMinMax(playerLevel, 1, 10);
        playerLevel.setWrapSelectorWheel(false);
        playerLevel.setOnValueChangedListener(this);

        setMinMax(playerGear, 0, 200);
        playerGear.setWrapSelectorWheel(false);
        playerGear.setOnValueChangedListener(this);

        setMinMax(monsterLevel, 0, 200);
        monsterLevel.setWrapSelectorWheel(false);
        monsterLevel.setOnValueChangedListener(this);

        setMinMax(monsterModifiers, 0, 200);
        monsterModifiers.setWrapSelectorWheel(false);
        monsterModifiers.setOnValueChangedListener(this);

        setMinMax(playerModifiers, 0, 200);
        playerModifiers.setWrapSelectorWheel(false);
        playerModifiers.setOnValueChangedListener(this);

        victoryCheckbox = (CheckBox) findViewById(R.id.victoryCheckbox);

        // preset the step size to 1
        ((RadioButton) findViewById(R.id.stepSize1)).toggle();
    }

    public void onRollDiceButtonClicked(View v) {
        Random rand = new Random();
        int randomNum = rand.nextInt(6) + 1;

        ((TextView) findViewById(R.id.diceResult)).setText(String.valueOf(randomNum));
        ((CheckBox) findViewById(R.id.diceRolledCheckbox)).toggle();

        Vibrator x = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        x.vibrate(300);
    }

    public void onRadioStepSizeClicked(View view) {
        stepSize = Integer.parseInt(((RadioButton) view).getText().toString());
    }

    public void resolveCombatClicked(View v) {
        if (victoryCheckbox.isChecked()) {
            // auto increase level on resolve
            playerLevel.setValue(playerLevel.getValue() + 1);
            victoryCheckbox.setChecked(false);
        }

        monsterModifiers.setValue(0);
        monsterLevel.setValue(0);
        playerModifiers.setValue(0);
    }

    protected void setMinMax(NumberPicker np, int min, int max) {
        np.setMinValue(min);
        np.setMaxValue(max);
    }

    public void onValueChange (NumberPicker picker, int oldVal, int newVal) {
        // let step size apply to all pickers
        picker.setValue((newVal < oldVal) ? oldVal - stepSize : oldVal + stepSize);

        // check combat success
        calculateCombatSuccess();

    }

    protected void calculateCombatSuccess()
    {
        // only check if fighting a monster, it's possible gear/lvl changes during combat
        if (monsterLevel.getValue() > 0) {
            int monsterStrength = monsterLevel.getValue() + monsterModifiers.getValue();
            int playerStrength = playerLevel.getValue() + playerGear.getValue() + playerModifiers.getValue();

            victoryCheckbox.setChecked(playerStrength > monsterStrength);
        }
    }

}
