package sm.projekt;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SettingsAdapter extends ArrayAdapter<CharSequence> {
    private final Context context;
    private final String[] options;
    private final String[] icons;

    public SettingsAdapter(@NonNull Context context, String[] options, String[] icons) {
        super(context, android.R.layout.simple_list_item_1, options);
        this.context = context;
        this.options = options;
        this.icons = icons;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.settings_list_item, parent, false);

        TextView textView = row.findViewById(android.R.id.text1);
        ImageView imageView = row.findViewById(R.id.icon);

        textView.setText(options[position]);

        String iconName = icons[position];
        int iconResId = context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());
        if (iconResId != 0) {
            Drawable icon = context.getResources().getDrawable(iconResId);
            imageView.setImageDrawable(icon);
        }

        return row;
    }
}