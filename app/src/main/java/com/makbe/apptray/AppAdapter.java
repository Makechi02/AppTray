package com.makbe.apptray;

import android.content.Context;
import android.content.Intent;
import android.view.*;
import android.widget.*;

import java.util.List;

public class AppAdapter extends BaseAdapter {

	private final Context context;
	private final List<App> apps;
	private final int cellHeight;

	public AppAdapter(Context context, List<App> apps, int cellHeight) {
		this.context = context;
		this.apps = apps;
		this.cellHeight = cellHeight;
	}

	@Override
	public int getCount() {
		return apps.size();
	}

	@Override
	public Object getItem(int position) {
		return apps.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View view;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.item_app, parent, false);
		} else {
			view = convertView;
		}

		LinearLayout layout = view.findViewById(R.id.layout_app);
		ImageView appIcon = view.findViewById(R.id.app_icon);
		TextView appLabel = view.findViewById(R.id.app_name);

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, cellHeight);
		layout.setLayoutParams(layoutParams);

		App app = apps.get(position);

		appIcon.setImageDrawable(app.getIcon());
		appLabel.setText(app.getName());

		layout.setOnClickListener(v -> {
			Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(app.getPackageName());
			if (launchIntent != null) {
				context.startActivity(launchIntent);
			}
		});

		return view;
	}
}
