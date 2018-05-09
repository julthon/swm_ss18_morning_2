package at.tugraz.recipro.Views;

import android.content.Context;
import android.nfc.Tag;
import android.util.AttributeSet;

import com.plumillonforge.android.chipview.Chip;
import com.plumillonforge.android.chipview.ChipView;
import com.plumillonforge.android.chipview.OnChipClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OurChipView extends ChipView {

    private void addChipClickListener() {
        super.setOnChipClickListener(new OnChipClickListener() {
            @Override
            public void onChipClick(Chip chip) {
                OurChipView.super.remove(chip);
            }
        });
    }

    public OurChipView(Context context) {
        super(context);
        addChipClickListener();
    }

    public OurChipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        addChipClickListener();
    }

    public OurChipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addChipClickListener();
    }

    public HashMap<Integer, OurTagImplementation> getListOfType(OurTagImplementation.TagType type) {
        HashMap<Integer, OurTagImplementation> includedIngredients = new HashMap<>();
        for(Chip chip : super.getChipList()) {
            OurTagImplementation c = (OurTagImplementation) chip;
            if(c.getTagType() == type || type == null)
                includedIngredients.put(c.getId(), c);
        }
        return includedIngredients;
    }

    public void removeAllChips() {
        for(Map.Entry<Integer, OurTagImplementation> entry : getListOfType(null).entrySet())
            this.remove(entry.getValue());
    }
}
