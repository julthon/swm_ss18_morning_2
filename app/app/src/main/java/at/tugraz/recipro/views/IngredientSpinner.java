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

import com.plumillonforge.android.chipview.Chip;

import java.util.ArrayList;
import java.util.List;

import at.tugraz.recipro.data.Ingredient;
import at.tugraz.recipro.helper.ResourceAccessHelper;
import at.tugraz.recipro.recipro.R;
import at.tugraz.recipro.ws.WSConnection;

public class IngredientSpinner extends AppCompatSpinner {
    private int chipviewId = -1;
    private OurChipView custom_optional_chipview = null;
    private OurTagImplementation.TagType tagType = null;
    private List<Ingredient> completeList = new ArrayList<>();
    private List<Ingredient> filteredList = new ArrayList<>();
    private ArrayAdapter<Ingredient> adapter = null;

    private void fireListUpdate() {
        if(adapter != null) {
            adapter.clear();
            adapter.addAll(filteredList);
        }
    }

    private void setupArrayAdapter() {
        if (adapter == null) {
            adapter = new ArrayAdapter<Ingredient>(super.getContext(), R.layout.spinner_item, R.id.spinnerItem, new ArrayList<>());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<Ingredient> list = WSConnection.getInstance().requestIngredients();
                    ArrayList<Ingredient> list1 = new ArrayList<>();
                    list1.add(new Ingredient(""));
                    list1.addAll(list);
                    completeList = list1;
                    filteredList = completeList;
                    fireListUpdate();
                }
            }).start();
            this.setAdapter(adapter);

            setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position != 0) {
                        if (custom_optional_chipview == null) {
                            custom_optional_chipview = ((AppCompatActivity) ResourceAccessHelper.getAppContext()).findViewById(chipviewId);
                            if (custom_optional_chipview != null) {
                                custom_optional_chipview.addOnSomethingChangedListener(new OnItemSelectedListener() {
                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) { }

                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        filteredList = new ArrayList<>();
                                        List<Chip> already_added_chips = custom_optional_chipview.getListOfType(tagType);
                                        for(Ingredient ing : completeList) {
                                            if(!already_added_chips.contains(new OurTagImplementation(0, ing.getName(), tagType)))
                                                filteredList.add(ing);
                                        }
                                        fireListUpdate();
                                    }
                                });
                            }
                        }
                        if (custom_optional_chipview != null)
                            custom_optional_chipview.add(new OurTagImplementation(0, adapter.getItem(position).getName(), tagType));
                        IngredientSpinner.this.setSelection(0);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    private void setupChipview(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IngredientSpinner, defStyleAttr, 0);
        chipviewId = a.getResourceId(R.styleable.IngredientSpinner_refChipView, -1);
        tagType = OurTagImplementation.getEnumFromString(this.getTooltipText().toString());
        a.recycle();
    }

    public IngredientSpinner(Context context) {
        super(context);
        setupArrayAdapter();
    }

    public IngredientSpinner(Context context, int mode) {
        super(context, mode);
        setupArrayAdapter();
    }

    public IngredientSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupChipview(context, attrs, 0);
        setupArrayAdapter();
    }

    public IngredientSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupChipview(context, attrs, defStyleAttr);
        setupArrayAdapter();
    }

    public IngredientSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
        setupChipview(context, attrs, defStyleAttr);
        setupArrayAdapter();
    }

    public IngredientSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode, Resources.Theme popupTheme) {
        super(context, attrs, defStyleAttr, mode, popupTheme);
        setupChipview(context, attrs, defStyleAttr);
        setupArrayAdapter();
    }

    public Ingredient getValue() {
        if (getSelectedItemPosition() == 0)
            return null;
        return adapter.getItem(getSelectedItemPosition() + 1);
    }
}
