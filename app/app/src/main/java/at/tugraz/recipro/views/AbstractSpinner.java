package at.tugraz.recipro.views;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import at.tugraz.recipro.helper.ResourceAccessHelper;
import at.tugraz.recipro.recipro.R;

public abstract class AbstractSpinner<T> extends AppCompatSpinner {
    private int chipviewId = -1;
    private OurChipView custom_optional_chipview = null;
    protected OurTagImplementation.TagType tagType = null;
    private List<T> completeList = new ArrayList<>();
    private List<T> filteredList = new ArrayList<>();
    private ArrayAdapter<T> adapter = null;

    private void fireListUpdate() {
        if (adapter != null) {
            adapter.clear();
            adapter.addAll(filteredList);
        }
    }

    protected abstract List<T> getValueList();

    protected abstract OurTagImplementation getTagImplementation(T value);

    private void setupArrayAdapter() {
        if (adapter == null) {
            adapter = new ArrayAdapter<T>(super.getContext(), R.layout.spinner_item, R.id.spinnerItem, new ArrayList<>());
            new Thread(() -> {
                completeList = getValueList();
                filteredList = completeList;
                fireListUpdate();
            }).start();
            this.setAdapter(adapter);

            setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position != 0) {
                        if (custom_optional_chipview == null) {
                            custom_optional_chipview = ((AppCompatActivity) ResourceAccessHelper.getAppContext()).findViewById(chipviewId);
                            if (custom_optional_chipview != null) {
                                custom_optional_chipview.addOnSomethingChangedListener(() -> {
                                    filteredList = new ArrayList<>();
                                    List<OurTagImplementation<T>> already_added_chips = custom_optional_chipview.getListOfType(tagType);
                                    for (T value : completeList) {
                                        if (!already_added_chips.contains(getTagImplementation(value)))
                                            filteredList.add(value);
                                    }
                                    fireListUpdate();
                                });
                            }
                        }
                        if (custom_optional_chipview != null)
                            custom_optional_chipview.add(getTagImplementation(adapter.getItem(position)));
                        AbstractSpinner.this.setSelection(0);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    private void setupChipview(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AbstractSpinner, defStyleAttr, 0);
        chipviewId = a.getResourceId(R.styleable.AbstractSpinner_refChipView, -1);
        tagType = OurTagImplementation.getEnumFromString(this.getTooltipText().toString());
        a.recycle();
    }

    public AbstractSpinner(Context context) {
        super(context);
        setupArrayAdapter();
    }

    public AbstractSpinner(Context context, int mode) {
        super(context, mode);
        setupArrayAdapter();
    }

    public AbstractSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupChipview(context, attrs, 0);
        setupArrayAdapter();
    }

    public AbstractSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupChipview(context, attrs, defStyleAttr);
        setupArrayAdapter();
    }

    public AbstractSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
        setupChipview(context, attrs, defStyleAttr);
        setupArrayAdapter();
    }

    public AbstractSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode, Resources.Theme popupTheme) {
        super(context, attrs, defStyleAttr, mode, popupTheme);
        setupChipview(context, attrs, defStyleAttr);
        setupArrayAdapter();
    }

    public T getValue() {
        if (getSelectedItemPosition() == 0)
            return null;
        return adapter.getItem(getSelectedItemPosition() + 1);
    }
}
