package at.tugraz.recipro.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AdapterView;

import com.plumillonforge.android.chipview.Chip;
import com.plumillonforge.android.chipview.ChipView;
import com.plumillonforge.android.chipview.OnChipClickListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class OurChipView extends ChipView {

    List<AdapterView.OnItemSelectedListener> eventListeners = new LinkedList<>();

    @Override
    public void add(Chip chip) {
        if(!mAdapter.getChipList().contains(chip)) {
            super.add(chip);
            for (AdapterView.OnItemSelectedListener listener : eventListeners)
                listener.onItemSelected(null, null, 0, 0);
        }
    }

    public void addOnSomethingChangedListener(AdapterView.OnItemSelectedListener listener) {
        eventListeners.add(listener);
    }

    private void addChipClickListener() {
        super.setOnChipClickListener(new OnChipClickListener() {
            @Override
            public void onChipClick(Chip chip) {
                OurChipView.super.remove(chip);
                for(AdapterView.OnItemSelectedListener listener : eventListeners)
                    listener.onItemSelected(null, null, 0, 0);
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

    public List<Chip> getListOfType(OurTagImplementation.TagType type) {
        ArrayList<Chip> tags = new ArrayList<>();
        for (Chip chip : super.getChipList()) {
            OurTagImplementation c = (OurTagImplementation) chip;
            if (c.getTagType() == type || type == null)
                tags.add(c);
        }
        return tags;
    }
}
